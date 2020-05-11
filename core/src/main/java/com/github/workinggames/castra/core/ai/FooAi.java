package com.github.workinggames.castra.core.ai;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.ArmySize;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.stage.World;

@Slf4j
public class FooAi implements Ai, Telegraph
{
    private static final int MAX_ACTIONS_PER_TICK = 3;

    private final World world;
    private final Player aiPlayer;
    private final Array<SettlementInfo> settlementInfos;
    private final Map<Settlement, SettlementInfo> settlementInfosMap;
    private final Map<Army, ArmyInfo> armyInfoMap;
    private final RandomXS128 random = new RandomXS128();

    private int actionsSinceLastTick;
    private int lastTickTime;

    public FooAi(World world, Player aiPlayer)
    {
        this.world = world;
        this.aiPlayer = aiPlayer;
        lastTickTime = 0;
        settlementInfos = new Array<>();
        settlementInfosMap = new HashMap<>();
        armyInfoMap = new HashMap<>();
        initializeSettlementInfos();
        subscribeMessages();
    }

    public void initializeSettlementInfos()
    {
        // TODO get game configuration for initial army size, as soon as it is configurable
        int initialArmySize = 100;

        for (Settlement settlement : world.getSettlements())
        {
            SettlementInfo settlementInfo = new SettlementInfo(settlement, initialArmySize);
            settlementInfos.add(settlementInfo);
            settlementInfosMap.put(settlement, settlementInfo);
        }
    }

    private void subscribeMessages()
    {
        MessageManager messageManager = MessageManager.getInstance();
        messageManager.addListener(this, MessageType.SOLDIER_SPAWNED);
        messageManager.addListener(this, MessageType.ARMY_CREATED);
        messageManager.addListener(this, MessageType.BATTLE_STARTED);
        messageManager.addListener(this, MessageType.BATTLE_JOINED);
        messageManager.addListener(this, MessageType.BATTLE_ENDED);
    }

    public void update()
    {
        float time = world.getTimepiece().getTime();
        // only do something if a complete 1 second tick has passed
        if (time - lastTickTime >= 1)
        {
            lastTickTime = MathUtils.floor(time);
            calculateSettlementCosts();
            actionsSinceLastTick = 0;
        }

        // sort destinations by cost
        settlementInfos.sort();
        if (!settlementInfos.isEmpty())
        {
            makeDecisions();
        }
    }

    private void calculateSettlementCosts()
    {
        for (SettlementInfo settlement : settlementInfos)
        {
            settlement.updateCosts(aiPlayer, world.getGameConfiguration().isOpponentSettlementDetailsVisible());
        }
    }

    private void makeDecisions()
    {
        // most valuable settlements are listed first, decide action
        int availableSoldiers = getAvailableSoldiers();
        boolean finished = actionsSinceLastTick == MAX_ACTIONS_PER_TICK;
        while (!finished)
        {
            for (SettlementInfo settlementInfo : settlementInfos)
            {
                if (settlementInfo.getSoldiersPresent() + settlementInfo.getOpponentSoldiersInbound() >
                    settlementInfo.getPlayerSoldiersInbound())
                {
                    // all estimates are not considering travel times
                    int requiredSoldiers;
                    if (settlementInfo.getSettlement().getOwner().isNeutral())
                    {
                        // neutral owner, no soldier spawn
                        requiredSoldiers = settlementInfo.getSoldiersPresent() -
                            settlementInfo.getOpponentSoldiersInbound() -
                            settlementInfo.getPlayerSoldiersInbound() + 1;
                    }
                    else if (settlementInfo.getSettlement().getOwner().equals(aiPlayer))
                    {
                        // own settlement, based on present soldier count, minimum soldiers added through spawn
                        requiredSoldiers = settlementInfo.getSoldiersPresent() -
                            settlementInfo.getOpponentSoldiersInbound() + settlementInfo.getPlayerSoldiersInbound() -
                            MathUtils.floor(estimateSpawnFactor(settlementInfo.getSoldiersPresent(),
                                settlementInfo.getSettlement().getSize()));
                    }
                    else
                    {
                        // enemy settlement, more soldiers needed due to spawn
                        requiredSoldiers = settlementInfo.getSoldiersPresent() +
                            settlementInfo.getOpponentSoldiersInbound() - settlementInfo.getPlayerSoldiersInbound() +
                            MathUtils.ceil(estimateSpawnFactor(settlementInfo.getSoldiersPresent(),
                                settlementInfo.getSettlement().getSize()));
                    }
                    if (requiredSoldiers < availableSoldiers)
                    {
                        moveSoldiers(requiredSoldiers, settlementInfo);
                        availableSoldiers = availableSoldiers - requiredSoldiers;
                        actionsSinceLastTick++;
                        if (actionsSinceLastTick == MAX_ACTIONS_PER_TICK)
                        {
                            return;
                        }
                    }
                    else
                    {
                        finished = true;
                    }
                }
            }
        }
    }

    private int getAvailableSoldiers()
    {
        int result = 0;
        for (SettlementInfo settlementInfo : settlementInfos)
        {
            if (settlementInfo.getSettlement().getOwner().equals(aiPlayer))
            {
                result = result + settlementInfo.getSoldiersPresent() - settlementInfo.getOpponentSoldiersInbound();
            }
        }
        return result;
    }

    private float estimateSpawnFactor(int soldiers, SettlementSize settlementSize)
    {
        // battle takes place every 0.1 seconds
        float minBattleTime = soldiers / 0.1f;
        return settlementSize.getSpawnIntervalInSeconds() % minBattleTime;
    }

    private void moveSoldiers(int required, SettlementInfo target)
    {
        Array<SettlementInfo> reversed = new Array<>(this.settlementInfos);
        reversed.reverse();
        int moved = 0;
        for (SettlementInfo source : reversed)
        {
            if (source.getSettlement().getOwner().equals(aiPlayer) && !source.equals(target))
            {
                int available = source.getSoldiersPresent() - source.getOpponentSoldiersInbound();
                int moving = required;
                if (available < required)
                {
                    moving = available;
                }
                if (moving > 0)
                {
                    world.createArmy(source.getSettlement(), target.getSettlement(), moving);
                    moved = moved + moving;
                }
                if (moving == 0 || moved == required)
                {
                    return;
                }
            }
        }
    }

    @Override
    public boolean handleMessage(Telegram msg)
    {
        switch (msg.message)
        {
            case MessageType.SOLDIER_SPAWNED:
            {
                Settlement settlement = (Settlement) msg.extraInfo;
                processSoldierSpawn(settlement);
                break;
            }
            case MessageType.ARMY_CREATED:
            {
                Army army = (Army) msg.extraInfo;
                processNewArmy(army);
                break;
            }
            case MessageType.BATTLE_JOINED:
            {
                Army army = (Army) msg.extraInfo;
                processJoinedArmy(army);
                break;
            }
            case MessageType.BATTLE_STARTED:
            {
                Battle battle = (Battle) msg.extraInfo;
                processNewBattle(battle);
                break;
            }
            case MessageType.BATTLE_ENDED:
            {
                Battle battle = (Battle) msg.extraInfo;
                processFinishedBattle(battle);
                break;
            }
            default:
            {
                break;
            }
        }
        return true;
    }

    private void processSoldierSpawn(Settlement settlement)
    {
        SettlementInfo settlementInfo = settlementInfosMap.get(settlement);
        settlementInfo.setSoldiersPresent(settlementInfo.getSoldiersPresent() + 1);
    }

    private void processNewArmy(Army army)
    {
        SettlementInfo sourceSettlementInfo = settlementInfosMap.get(army.getSource());
        SettlementInfo targetSettlementInfo = settlementInfosMap.get(army.getTarget());

        int soldiers;
        if (army.getOwner().equals(aiPlayer) || world.getGameConfiguration().isOpponentArmyDetailsVisible())
        {
            soldiers = army.getSoldiers();
        }
        else
        {
            soldiers = randomSoldiersBasedOnArmySizeAndTargetSoldiers(army.getArmySize(),
                sourceSettlementInfo,
                targetSettlementInfo);
        }

        ArmyInfo armyInfo = new ArmyInfo(army, null, soldiers);
        armyInfoMap.put(army, armyInfo);

        // reduce soldiers stationed in source settlement by soldier count/estimate
        sourceSettlementInfo.setSoldiersPresent(sourceSettlementInfo.getSoldiersPresent() - soldiers);
        if (!army.getOwner().equals(aiPlayer))
        {
            targetSettlementInfo.setOpponentSoldiersInbound(targetSettlementInfo.getOpponentSoldiersInbound() +
                soldiers);
        }
        else
        {
            targetSettlementInfo.setPlayerSoldiersInbound(targetSettlementInfo.getPlayerSoldiersInbound() + soldiers);
        }
    }

    private int randomSoldiersBasedOnArmySizeAndTargetSoldiers(
        ArmySize armySize, SettlementInfo source, SettlementInfo target)
    {
        int min = armySize.getMinimumSoldiers();
        int max = source.getSoldiersPresent();
        switch (armySize)
        {
            case SMALL:
                int maxSoldiersBeforeMediumArmySize = ArmySize.MEDIUM.getMinimumSoldiers() - 1;
                if (max > maxSoldiersBeforeMediumArmySize)
                {
                    max = maxSoldiersBeforeMediumArmySize;
                }
                min = estimateMinSize(min, max, target);
            case MEDIUM:
                int maxSoldiersBeforeLargeArmySize = ArmySize.LARGE.getMinimumSoldiers() - 1;
                if (max > maxSoldiersBeforeLargeArmySize)
                {
                    max = maxSoldiersBeforeLargeArmySize;
                }
                min = estimateMinSize(min, max, target);

            case LARGE:
                min = estimateMinSize(min, max, target);
        }
        return getRandomValueInclusive(min, max);
    }

    private int estimateMinSize(int min, int max, SettlementInfo target)
    {
        // let's assume the opponent wants to take over the settlement and not just weaken it
        if (target.getSoldiersPresent() + 1 < max && target.getPlayerSoldiersInbound() == 0)
        {
            return target.getSoldiersPresent();
        }
        return min;
    }

    private int getRandomValueInclusive(int minimum, int maximum)
    {
        return minimum + random.nextInt(maximum - minimum + 1);
    }

    private void processJoinedArmy(Army army)
    {
        ArmyInfo armyInfo = armyInfoMap.get(army);
        // if opponent army, find the battle which was joined and add the soldier estimate before removing this army
    }

    private void processNewBattle(Battle battle)
    {
        Army army = battle.getArmy();
        ArmyInfo armyInfo = armyInfoMap.get(army);
        armyInfo.setBattle(battle);
    }

    private void processFinishedBattle(Battle battle)
    {
        Army army = battle.getArmy();
        ArmyInfo armyInfo = armyInfoMap.get(army);
        SettlementInfo targetSettlementInfo = settlementInfosMap.get(army.getTarget());
        if (army.getOwner().equals(aiPlayer))
        {
            targetSettlementInfo.setPlayerSoldiersInbound(targetSettlementInfo.getPlayerSoldiersInbound() -
                armyInfo.getSoldierEstimate());
        }
        else
        {
            targetSettlementInfo.setOpponentSoldiersInbound(targetSettlementInfo.getOpponentSoldiersInbound() -
                armyInfo.getSoldierEstimate());
        }
        // owner changed
        if (!targetSettlementInfo.getLastOwner().equals(targetSettlementInfo.getSettlement().getOwner()))
        {
            targetSettlementInfo.setLastOwner(targetSettlementInfo.getSettlement().getOwner());
            if (!targetSettlementInfo.getSettlement().getOwner().equals(aiPlayer))
            {
                targetSettlementInfo.setSoldiersPresent(armyInfo.getSoldierEstimate() -
                    targetSettlementInfo.getSoldiersPresent());
            }
        }

        armyInfoMap.remove(army);
    }
}
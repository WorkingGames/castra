package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.ai.Ai;
import com.github.workinggames.castra.core.ai.AiUtils;
import com.github.workinggames.castra.core.model.ArmySize;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

public class ConfrontingAi implements Ai, Telegraph
{
    private static final int FIRST_ACTION_TIME = 1;
    private static final int MAXIMUM_SOLDIER_INVEST_IN_NEUTRAL = 30;
    private static final int MINIMUM_TURNS_WITHOUT_ATTACKING_NEUTRAL = 15;
    private static final int MAXIMUM_TURNS_WITHOUT_ATTACKING_NEUTRAL = 30;
    private static final float MINIMUM_IDLE_TIME = 0.5f;
    private static final float MAXIMUM_IDLE_TIME = 1.5f;

    @Getter
    private final StateMachine<Ai, AiState> stateMachine;
    private final World world;
    private final MessagingHandler messagingHandler;
    private final GameInfo gameInfo;
    private final AttackGeneral attackGeneral;

    private float nextActionTime;
    private boolean attacked;
    private final int turnsBeforeLoosingTemper = MathUtils.random(MINIMUM_TURNS_WITHOUT_ATTACKING_NEUTRAL,
        MAXIMUM_TURNS_WITHOUT_ATTACKING_NEUTRAL);
    private boolean faking;
    private int turnsWithoutAttack = 0;

    public ConfrontingAi(World world, Player aiPlayer)
    {
        this.world = world;
        stateMachine = new DefaultStateMachine<>(this, AiState.WAIT);
        gameInfo = new GameInfo(world, aiPlayer);
        messagingHandler = new MessagingHandler(this, gameInfo);
        attackGeneral = new AttackGeneral(new AiUtils(world), aiPlayer, world.getGameConfiguration());
        nextActionTime = FIRST_ACTION_TIME;
        faking = !world.getGameConfiguration().isOpponentArmyDetailsVisible();
    }

    @Override
    public boolean handleMessage(Telegram msg)
    {
        return messagingHandler.handleMessage(msg);
    }

    public void update()
    {
        float time = world.getTimepiece().getTime();

        gameInfo.updateSettlementInfos();

        if (time >= nextActionTime)
        {
            stateMachine.changeState(AiState.ATTACK);
            nextActionTime = time + MathUtils.random(MINIMUM_IDLE_TIME, MAXIMUM_IDLE_TIME);
        }
    }

    public void attack()
    {
        ArrayMap<Integer, SettlementInfo> settlementInfos = gameInfo.getSettlementInfoBySettlementId();
        // This Ai won't bother with neutral Settlements and only attack the opponent and run after his armies
        int soldiersAvailable = gameInfo.getSoldiersAvailable();
        Array<Attack> attackOptions = attackGeneral.getOpponentAttackOptions(settlementInfos, soldiersAvailable);
        attackOptions.addAll(attackGeneral.getOpponentArmyOptions(settlementInfos, soldiersAvailable));

        // To prevent a stare off contest when facing the same Ai, it will loose its temper and will once attack a neutral settlement to break the tie
        if (!attacked && turnsWithoutAttack >= turnsBeforeLoosingTemper)
        {
            attackOptions.addAll(attackGeneral.getNeutralAttackOptions(settlementInfos,
                MAXIMUM_SOLDIER_INVEST_IN_NEUTRAL));
        }

        attackOptions.sort();
        if (!attackOptions.isEmpty())
        {
            if (!faking)
            {
                Attack attack = attackOptions.first();
                createArmies(attack);
                attacked = true;
            }
            else
            {
                fakeAttack(attackOptions);
                // faking only makes sense if the real attack is deployed as fast as allowed
                nextActionTime = world.getTimepiece().getTime() + MINIMUM_IDLE_TIME;
                // next move should not be another fake
                faking = false;
            }
        }
        else
        {
            turnsWithoutAttack++;
            stateMachine.changeState(AiState.WAIT);
        }
    }

    private void fakeAttack(Array<Attack> attackOptions)
    {
        ArrayMap<Integer, SettlementInfo> settlementInfos = gameInfo.getSettlementInfoBySettlementId();
        // there is no sense in faking if there is only one attack option
        if (attackOptions.size > 1)
        {
            // let's find a suitable target for the fake attack, it should be something a small army would capture,
            // so the opponent will think it will be more soldiers than just the actual 1
            for (Attack attack : attackOptions)
            {
                SettlementInfo targetInfo = settlementInfos.get(attack.getTargetSettlementId());
                // we don't actually want to capture the settlement, so let's pick one where the opponent might invest a fair amount of soldiers
                // and it would be smart to choose a settlement with a minimum distance so the army will take some time and give the opponent time
                // to fall for the bluff
                int minDefenders = MathUtils.random(1, 10);
                AttackSource attackSource = attack.getAttackSources().first();
                float distanceInTicks = targetInfo.getSettlementDistancesInTicks().get(attackSource.getSettlementId());
                float minDistanceInTicks = MathUtils.random(2, 3);
                if (targetInfo.getDefenders() > minDefenders &&
                    distanceInTicks > minDistanceInTicks &&
                    targetInfo.getDefenders() < ArmySize.MEDIUM.getMinimumSoldiers() - 1)
                {
                    world.createArmy(gameInfo.getSettlement(attackSource.getSettlementId()),
                        gameInfo.getSettlement(attack.getTargetSettlementId()),
                        1);
                    break;
                }
            }
        }
    }

    private void createArmies(Attack attack)
    {
        for (AttackSource source : attack.getAttackSources())
        {
            world.createArmy(gameInfo.getSettlement(source.getSettlementId()),
                gameInfo.getSettlement(attack.getTargetSettlementId()),
                source.getSoldiers());
        }
    }
}
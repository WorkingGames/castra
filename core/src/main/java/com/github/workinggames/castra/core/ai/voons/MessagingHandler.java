package com.github.workinggames.castra.core.ai.voons;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;

public class MessagingHandler
{
    private final GameInfo gameInfo;

    public MessagingHandler(Telegraph telegraph, GameInfo gameInfo)
    {
        this.gameInfo = gameInfo;
        subscribeMessages(telegraph);
    }

    public void subscribeMessages(Telegraph telegraph)
    {
        MessageManager messageManager = MessageManager.getInstance();
        messageManager.addListener(telegraph, MessageType.SOLDIER_SPAWNED);
        messageManager.addListener(telegraph, MessageType.ARMY_CREATED);
        messageManager.addListener(telegraph, MessageType.BATTLE_STARTED);
        messageManager.addListener(telegraph, MessageType.BATTLE_JOINED);
        messageManager.addListener(telegraph, MessageType.BATTLE_ENDED);
        messageManager.addListener(telegraph, MessageType.SOLDIER_REMOVED);
        messageManager.addListener(telegraph, MessageType.DEFENDER_ADDED);
        messageManager.addListener(telegraph, MessageType.DEFENDER_REMOVED);
    }

    public boolean handleMessage(Telegram msg)
    {
        switch (msg.message)
        {
            case MessageType.SOLDIER_SPAWNED:
            {
                Settlement settlement = (Settlement) msg.extraInfo;
                gameInfo.soldierSpawned(settlement.getId());
                break;
            }
            case MessageType.ARMY_CREATED:
            {
                Army army = (Army) msg.extraInfo;
                gameInfo.armyCreated(army);
                break;
            }
            case MessageType.BATTLE_JOINED:
            {
                Army army = (Army) msg.extraInfo;
                gameInfo.battleJoined(army);
                break;
            }
            case MessageType.BATTLE_STARTED:
            {
                Battle battle = (Battle) msg.extraInfo;
                gameInfo.battleStarted(battle);
                break;
            }
            case MessageType.BATTLE_ENDED:
            {
                Battle battle = (Battle) msg.extraInfo;
                gameInfo.battleEnded(battle);
                break;
            }
            case MessageType.SOLDIER_REMOVED:
            {
                Battle battle = (Battle) msg.extraInfo;
                gameInfo.soldierRemoved(battle);
                break;
            }
            case MessageType.DEFENDER_ADDED:
            {
                Battle battle = (Battle) msg.extraInfo;
                gameInfo.defenderAdded(battle);
                break;
            }
            case MessageType.DEFENDER_REMOVED:
            {
                Battle battle = (Battle) msg.extraInfo;
                gameInfo.defenderRemoved(battle);
                break;
            }
        }
        return true;
    }
}
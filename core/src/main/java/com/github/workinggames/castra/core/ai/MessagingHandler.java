package com.github.workinggames.castra.core.ai;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;

public class MessagingHandler
{
    private final Quux quux;

    public MessagingHandler(BarAi barAi, Quux quux)
    {
        this.quux = quux;
        subscribeMessages(barAi);
    }

    public void subscribeMessages(Telegraph telegraph)
    {
        MessageManager messageManager = MessageManager.getInstance();
        messageManager.addListener(telegraph, MessageType.SOLDIER_SPAWNED);
        messageManager.addListener(telegraph, MessageType.ARMY_CREATED);
        messageManager.addListener(telegraph, MessageType.BATTLE_STARTED);
        messageManager.addListener(telegraph, MessageType.BATTLE_JOINED);
        messageManager.addListener(telegraph, MessageType.BATTLE_ENDED);
    }

    public boolean handleMessage(Telegram msg)
    {
        switch (msg.message)
        {
            case MessageType.SOLDIER_SPAWNED:
            {
                Settlement settlement = (Settlement) msg.extraInfo;
                quux.soldierSpawned(settlement.getId());
                break;
            }
            case MessageType.ARMY_CREATED:
            {
                Army army = (Army) msg.extraInfo;
                quux.armyCreated(army);
                break;
            }
            case MessageType.BATTLE_JOINED:
            {
                Army army = (Army) msg.extraInfo;
                quux.battleJoined(army);
                break;
            }
            case MessageType.BATTLE_STARTED:
            {
                Battle battle = (Battle) msg.extraInfo;
                quux.battleStarted(battle);
                break;
            }
            case MessageType.BATTLE_ENDED:
            {
                Battle battle = (Battle) msg.extraInfo;
                quux.battleEnded(battle);
                break;
            }
        }
        return true;
    }
}
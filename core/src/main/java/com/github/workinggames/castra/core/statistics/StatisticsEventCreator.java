package com.github.workinggames.castra.core.statistics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.GameConfiguration;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.statistics.event.ArmyDeployed;
import com.github.workinggames.castra.core.statistics.event.BattleEnded;
import com.github.workinggames.castra.core.statistics.event.BattleJoined;
import com.github.workinggames.castra.core.statistics.event.BattleStarted;
import com.github.workinggames.castra.core.statistics.event.GameEnded;
import com.github.workinggames.castra.core.statistics.event.GameStarted;
import com.github.workinggames.castra.core.statistics.event.PlayerDto;
import com.github.workinggames.castra.core.statistics.event.SettlementDto;
import com.github.workinggames.castra.core.task.VortexEventSender;

@Slf4j
@RequiredArgsConstructor
public class StatisticsEventCreator
{
    private final VortexEventSender vortexEventSender;
    private final SimpleDateFormat isoDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public void gameStarted(World world)
    {
        GameConfiguration gameConfiguration = world.getGameConfiguration();

        GameStarted.Attributes attributes = new GameStarted.Attributes(getGameId(world),
            new PlayerDto(gameConfiguration.getPlayer1()),
            new PlayerDto(gameConfiguration.getPlayer2()),
            gameConfiguration.getSeed(),
            Gdx.app.getType(),
            getSettlementDtos(world));
        GameStarted gameStarted = new GameStarted(attributes, getTimestamp());

        vortexEventSender.send(gameStarted);
    }

    public void gameEnded(World world, Player winner)
    {
        GameEnded.Attributes attributes = new GameEnded.Attributes(getGameId(world),
            new PlayerDto(winner),
            getSettlementDtos(world));
        GameEnded gameEnded = new GameEnded(attributes, getTimestamp());

        vortexEventSender.send(gameEnded);
    }

    public void armyDeployed(World world, Army army)
    {
        ArmyDeployed.Attributes attributes = new ArmyDeployed.Attributes(getGameId(world),
            new PlayerDto(army.getOwner()),
            army.getId(),
            army.getSoldiers(),
            new SettlementDto(army.getSource()),
            new SettlementDto(army.getTarget()),
            army.getPath().getDistance());
        ArmyDeployed armyDeployed = new ArmyDeployed(attributes, getTimestamp());

        vortexEventSender.send(armyDeployed);
    }

    public void battleStarted(World world, Battle battle)
    {
        Army army = battle.getArmy();
        BattleStarted.Attributes attributes = new BattleStarted.Attributes(getGameId(world),
            new PlayerDto(army.getOwner()),
            army.getId(),
            army.getSoldiers(),
            new SettlementDto(army.getTarget()));
        BattleStarted battleStarted = new BattleStarted(attributes, getTimestamp());

        vortexEventSender.send(battleStarted);
    }

    public void battleJoined(World world, Battle battle, Army army)
    {
        BattleJoined.Attributes attributes = new BattleJoined.Attributes(getGameId(world),
            new PlayerDto(army.getOwner()),
            battle.getArmy().getSoldiers(),
            army.getId(),
            army.getSoldiers(),
            new SettlementDto(army.getTarget()));
        BattleJoined battleJoined = new BattleJoined(attributes, getTimestamp());

        vortexEventSender.send(battleJoined);
    }

    public void battleEnded(UUID gameId, Army army, boolean captured)
    {
        BattleEnded.Attributes attributes = new BattleEnded.Attributes(gameId.toString(),
            new PlayerDto(army.getOwner()),
            new SettlementDto(army.getTarget()),
            captured);
        BattleEnded battleEnded = new BattleEnded(attributes, getTimestamp());

        vortexEventSender.send(battleEnded);
    }

    private String getTimestamp()
    {
        Date now = new Date(TimeUtils.millis());
        return isoDateFormatter.format(now);
    }

    private String getGameId(World world)
    {
        return world.getGameId().toString();
    }

    private Array<SettlementDto> getSettlementDtos(World world)
    {
        Array<SettlementDto> settlementDtos = new Array<>();
        for (Settlement settlement : world.getSettlements())
        {
            settlementDtos.add(new SettlementDto(settlement));
        }
        return settlementDtos;
    }
}
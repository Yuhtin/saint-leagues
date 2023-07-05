package com.yuhtin.quotes.saint.leagues.hook.impl;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import com.yuhtin.quotes.saint.leagues.hook.LeagueEventHook;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.model.LeagueEventType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.roinujnosde.titansbattle.events.PlayerWinEvent;
import me.roinujnosde.titansbattle.types.Warrior;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TitansBattleHook extends LeagueEventHook {

    private final LeaguesPlugin instance;

    @Override
    public String pluginName() {
        return "TitansBattle";
    }

    @Override
    public void setup(@Nonnull TerminableConsumer consumer) {

        Events.subscribe(PlayerWinEvent.class)
                .handler(event -> {
                    String eventName = event.getGame().getConfig().getName();

                    List<Warrior> players = event.getPlayers();
                    List<String> filteredPlayers = filterPlayersWithClan(players);

                    String clanTag = instance.getSimpleClansAccessor().getClanTag(filteredPlayers.get(0));
                    if (clanTag == null) {
                        instance.getLogger().warning("[TitansBattle] Não foi possível encontrar o clã do jogador " + filteredPlayers.get(0));
                        return;
                    }

                    clanTag = clanTag.toUpperCase();

                    String path = "reward-per-event.TitansBattle." + eventName;

                    int points = instance.getConfig().getInt(path + ".points", -1);
                    if (points == -1) {
                        instance.getLogger().severe("[TitansBattle] Não foi possível encontrar a quantidade de pontos para o evento " + eventName + " (" + path + ")");
                        return;
                    }

                    String name = instance.getConfig().getString(path + ".name", "TitansBattle");

                    LeagueEvent leagueEvent = LeagueEvent.builder()
                            .name(name)
                            .clanTag(clanTag)
                            .leagueEventType(LeagueEventType.WIN_CLAN_EVENTS)
                            .playersInvolved(filteredPlayers)
                            .points(points)
                            .build();

                    instance.getEventRepository().insert(leagueEvent);
                    LeagueClanCache.getInstance().addPoints(clanTag, points);

                    instance.getLogger().info("[TitansBattle] Vitória de " + clanTag + " (+ " + points + " pontos)");
                }).bindWith(consumer);

    }

    private List<String> filterPlayersWithClan(List<Warrior> players) {
        return players.stream()
                .map(Warrior::getName)
                .filter(name -> instance.getSimpleClansAccessor().getClanTag(name) != null)
                .collect(Collectors.toList());
    }
}

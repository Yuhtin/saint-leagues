package com.yuhtin.quotes.saint.leagues.hook.impl;

import com.ystoreplugins.yeventos.event.EventType;
import com.ystoreplugins.yeventos.event.PlayerWinEventEvent;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import com.yuhtin.quotes.saint.leagues.hook.LeagueEventHook;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.model.LeagueEventType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class YEventsHook extends LeagueEventHook {

    private final LeaguesPlugin instance;

    @Override
    public String pluginName() {
        return "yEventos";
    }

    @Override
    public void setup(@Nonnull TerminableConsumer consumer) {

        Events.subscribe(PlayerWinEventEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();

                    String clanTag = instance.getSimpleClansAccessor().getClanTag(player);
                    if (clanTag == null) {
                        instance.getLogger().warning("[yEventos] Não foi possível encontrar o clã do jogador " + player.getName());
                        return;
                    }

                    clanTag = clanTag.toUpperCase();

                    EventType eventType = event.getEventType();

                    String path = "reward-per-event.yEvents." + eventType.name().toUpperCase();

                    int points = instance.getConfig().getInt(path + ".points", -1);
                    if (points == -1) {
                        instance.getLogger().severe("[yEventos] Não foi possível encontrar a quantidade de pontos para o evento " + eventType.name().toUpperCase() + " (" + path + ")");
                        return;
                    }

                    String eventName = instance.getConfig().getString(path + ".name", eventType.name());

                    LeagueEvent leagueEvent = LeagueEvent.builder()
                            .name(eventName)
                            .clanTag(clanTag)
                            .leagueEventType(LeagueEventType.WIN_EVENTS)
                            .playersInvolved(Arrays.asList(player.getName()))
                            .points(points)
                            .build();

                    instance.getEventRepository().insert(leagueEvent);
                    LeagueClanCache.getInstance().addPoints(clanTag, points);

                    instance.getLogger().info("[yEventos] [" + eventName + "] Vitória de " + player.getName() + " [" + clanTag + "] (+ " + points + " pontos)");
                }).bindWith(consumer);

    }

}

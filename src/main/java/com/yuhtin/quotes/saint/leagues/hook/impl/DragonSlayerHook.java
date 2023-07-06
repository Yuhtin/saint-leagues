package com.yuhtin.quotes.saint.leagues.hook.impl;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import com.yuhtin.quotes.saint.leagues.hook.LeagueEventHook;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.model.LeagueEventType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DragonSlayerHook extends LeagueEventHook {

    private final LeaguesPlugin instance;

    @Override
    public String pluginName() {
        return "DragonSlayer";
    }

    @Override
    public void setup(@Nonnull TerminableConsumer consumer) {

        Commands.create()
                .assertConsole()
                .assertUsage("<slayer>")
                .handler(context -> {

                    Player player = context.arg(0).parseOrFail(Player.class);
                    String clanTag = instance.getSimpleClansAccessor().getClanTag(player);
                    if (clanTag == null) {
                        instance.getLogger().warning("[DragonSlayer] Não foi possível encontrar o clã do jogador " + player.getName());
                        return;
                    }

                    clanTag = clanTag.toUpperCase();

                    String path = "reward-per-event.DragonSlayer";

                    int points = instance.getConfig().getInt(path + ".points", -1);
                    if (points == -1) {
                        instance.getLogger().severe("[DragonSlayer] Não foi possível encontrar a quantidade de pontos para o evento (" + path + ")");
                        return;
                    }

                    String eventName = instance.getConfig().getString(path + ".name", "DragonSlayer");

                    LeagueEvent leagueEvent = LeagueEvent.builder()
                            .name(eventName)
                            .clanTag(clanTag)
                            .leagueEventType(LeagueEventType.KILL_DRAGON)
                            .playersInvolved(Arrays.asList(player.getName()))
                            .points(points)
                            .build();

                    instance.getEventRepository().insert(leagueEvent);
                    LeagueClanCache.getInstance().addPoints(clanTag, points, eventName);

                    instance.getLogger().info("[DragonSlayer] Vitória de " + player.getName() + " [" + clanTag + "] (+ " + points + " pontos)");
                }).registerAndBind(consumer, "dragonslayerkill");

    }
}

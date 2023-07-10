package com.yuhtin.quotes.saint.leagues.module;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.repository.repository.TimedClanRepository;
import lombok.AllArgsConstructor;
import me.lucko.helper.Schedulers;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
public class AutoRewardModule implements TerminableModule {

    private final LeaguesPlugin instance;

    @Override
    public void setup(@NotNull TerminableConsumer consumer) {
        Schedulers.sync().runRepeating(runnable -> {
            for (TimedClanRepository repository : LeagueClanCache.getInstance().getRepositories().values()) {
                if (repository.getInitialTime() == -1 || repository.getFinalTime() > System.currentTimeMillis()) continue;
                if (!runReward(repository)) continue;

                resetRepository(repository);
            }
        }, 20L, 60 * 20L).bindWith(consumer);
    }

    private boolean runReward(TimedClanRepository repository) {
        String name = repository.getIntervalTime().name();

        Set<LeagueClan> leagueClans = repository.orderByPoints(3);
        if (leagueClans.isEmpty()) {
            instance.getLogger().severe("[" + name + "] Não foi possível encontrar nenhum clã para ser recompensado!");
            return false;
        }

        int i = 1;
        for (LeagueClan leagueClan : leagueClans) {
            instance.getLogger().info("[" + name + "] Recompensando o clã " + leagueClan.getTag() + " com o " + i + "º lugar!");

            List<String> clanPlayers = instance.getSimpleClansAccessor().getClanPlayers(leagueClan.getTag());
            if (clanPlayers == null) {
                instance.getLogger().severe("[" + name + "] Não foi possível encontrar nenhum jogador no clã " + leagueClan.getTag() + "!");
                break;
            }

            for (String player : clanPlayers) {
                for (String command : instance.getConfig().getStringList("auto-reward." + repository.getIntervalTime().name() + "." + i)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                            .replace("$player", player)
                            .replace("$clanTag", leagueClan.getTag())
                    );
                }
            }

            i++;
        }

        instance.getLogger().info("[" + name + "] Recompensas automáticas executadas com sucesso!");
        return true;
    }

    private void resetRepository(TimedClanRepository repository) {
        String name = repository.getIntervalTime().name();

        instance.getConfig().set("initial-time." + name, System.currentTimeMillis());
        instance.saveConfig();

        repository.setInitialTime(System.currentTimeMillis());
        repository.recreateTable();

        instance.getLogger().info("Sistema " + name.toLowerCase() + " reiniciado com sucesso!");
    }

}

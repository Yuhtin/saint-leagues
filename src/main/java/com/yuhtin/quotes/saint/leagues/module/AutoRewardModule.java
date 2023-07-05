package com.yuhtin.quotes.saint.leagues.module;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.repository.repository.ClanRepository;
import lombok.AllArgsConstructor;
import me.lucko.helper.Schedulers;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
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
        int dateToReward = instance.getConfig().getInt("auto-reward.date");
        int hourToReward = instance.getConfig().getInt("auto-reward.hour");

        Schedulers.sync().runRepeating(runnable -> {
            Calendar calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int date = calendar.get(Calendar.DATE);

            if (date != dateToReward || hour != hourToReward) return;
            if (!runReward()) return;

            resetTables();
            runnable.stop();
        }, 20L, 60 * 20L).bindWith(consumer);
    }

    private boolean runReward() {
        ClanRepository clanRepository = LeaguesPlugin.getInstance().getClanRepository();
        Set<LeagueClan> leagueClans = clanRepository.orderByPoints(3);
        if (leagueClans.isEmpty()) {
            instance.getLogger().severe("Não foi possível encontrar nenhum clã para ser recompensado!");
            return false;
        }

        int i = 1;
        for (LeagueClan leagueClan : leagueClans) {
            instance.getLogger().info("Recompensando o clã " + leagueClan.getTag() + " com o " + i + "º lugar!");

            List<String> clanPlayers = instance.getSimpleClansAccessor().getClanPlayers(leagueClan.getTag());
            if (clanPlayers == null) {
                instance.getLogger().severe("Não foi possível encontrar nenhum jogador no clã " + leagueClan.getTag() + "!");
                break;
            }

            for (String player : clanPlayers) {
                for (String command : instance.getConfig().getStringList("auto-reward.rewards." + i)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                            .replace("$player", player)
                            .replace("$clanTag", leagueClan.getTag())
                    );
                }
            }

            i++;
        }

        instance.getLogger().info("Recompensas automáticas executadas com sucesso!");
        return true;
    }

    private void resetTables() {
        instance.getEventRepository().recreateTable();
        instance.getClanRepository().recreateTable();

        instance.getLogger().info("Sistema reiniciado com sucesso!");
    }

}

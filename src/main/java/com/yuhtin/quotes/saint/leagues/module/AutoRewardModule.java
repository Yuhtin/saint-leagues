package com.yuhtin.quotes.saint.leagues.module;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.repository.repository.TimedClanRepository;
import lombok.AllArgsConstructor;
import me.lucko.helper.Schedulers;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
            for (TimedClanRepository repository : RepositoryManager.getInstance().getRepositories().values()) {
                if (repository.getFinalTime() > System.currentTimeMillis() || !runReward(repository))
                    continue;
                saveLog(repository);
                resetRepository(repository);
            }
        }, 20L, 1200L).bindWith(consumer);
    }

    private boolean runReward(TimedClanRepository repository) {
        String name = repository.getIntervalTime().name();
        Set<LeagueClan> leagueClans = repository.orderByPoints(3);
        if (leagueClans.isEmpty()) {
            this.instance.getLogger().severe("[" + name + "] Não foi possível encontrar nenhum clã para ser recompensado!");
            return false;
        }
        int i = 1;
        for (LeagueClan leagueClan : leagueClans) {
            this.instance.getLogger().info("[" + name + "] Recompensando o clã " + leagueClan.getTag() + " com o " + i + "º lugar!");
            List<String> clanPlayers = this.instance.getSimpleClansAccessor().getClanPlayers(leagueClan.getTag());
            if (clanPlayers == null) {
                this.instance.getLogger().severe("[" + name + "] Não foi possível encontrar nenhum jogador no clã " + leagueClan.getTag() + "!");
                break;
            }
            for (String player : clanPlayers) {
                for (String command : this.instance.getConfig().getStringList("auto-reward." + repository.getIntervalTime().name() + "." + i))
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                            .replace("$player", player)
                            .replace("$clanTag", leagueClan.getTag()));
            }
            i++;
        }
        this.instance.getLogger().info("[" + name + "] Recompensas automáticas executadas com sucesso!");
        return true;
    }

    private void saveLog(TimedClanRepository repository) {
        File file = new File(this.instance.getDataFolder(), getLogFileLocation(repository.getIntervalTime()));
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            try {
                writer.write("Clã com mais pontos no ranking " + repository.getIntervalTime().name().toLowerCase() + ":\n\n");
                for (LeagueClan leagueClan : repository.orderByPoints())
                    writer.write(leagueClan.getTag() + " - " + leagueClan.getTag() + "\n");
                writer.flush();
                writer.close();
            } catch (Throwable throwable) {
                try {
                    writer.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String getLogFileLocation(IntervalTime intervalTime) {
        Calendar calendar = Calendar.getInstance();
        String formatedDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
        return "past-leagues/" + intervalTime.name().toLowerCase() + "/" + formatedDate + ".txt";
    }

    private void resetRepository(TimedClanRepository repository) {
        String name = repository.getIntervalTime().name();
        repository.setInitialTime(System.currentTimeMillis());
        this.instance.getConfig().set("initial-time." + name, repository.getInitialTime());
        this.instance.getConfig().set("reset-time." + name, repository.getIntervalTime().getAddedDate());
        this.instance.saveConfig();
        repository.recreateTable();
        this.instance.getLogger().info("Sistema " + name.toLowerCase() + " reiniciado com sucesso!");
    }
}

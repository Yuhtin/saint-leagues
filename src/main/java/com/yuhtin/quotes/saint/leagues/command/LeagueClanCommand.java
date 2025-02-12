package com.yuhtin.quotes.saint.leagues.command;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.model.LeagueEventType;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import lombok.AllArgsConstructor;
import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
public class LeagueClanCommand implements TerminableModule {

    private final LeaguesPlugin instance;

    @Override
    public void setup(@Nonnull TerminableConsumer consumer) {

        Commands.create()
                .assertPlayer()
                .handler(context -> {
                    instance.getViewCache().getLeagueView().openInventory(context.sender());
                }).registerAndBind(consumer, "liga");

        Commands.create()
                .assertPermission("league.admin")
                .assertUsage("<add/remove> <clan> <pontos> <mensal/trimestral> <motivo>")
                .handler(context -> {
                    String action = context.arg(0).parseOrFail(String.class);
                    String playerName = context.arg(1).parseOrFail(String.class);
                    String clanTag = instance.getClanAcessor().getClanTag(playerName);
                    if (clanTag == null) return;

                    int points = Math.max(0, context.arg(2).parseOrFail(Integer.class));
                    int total = action.equalsIgnoreCase("remove") ? points * -1 : points;

                    IntervalTime time;
                    try {
                        time = IntervalTime.valueOf(context.arg(3).parseOrFail(String.class));
                    } catch (Exception exception) {
                        context.reply("&cIntervalo de tempo inválido! &8(Mensal/Trimestral)");
                        return;
                    }

                    StringBuilder builder = new StringBuilder();
                    for (String arg : context.args().subList(4, context.args().size())) {
                        builder.append(arg).append(" ");
                    }

                    String motive = builder.toString().trim();

                    RepositoryManager manager = RepositoryManager.getInstance();

                    LeagueEvent leagueEvent = LeagueEvent.builder()
                            .name(motive)
                            .clanTag(clanTag)
                            .leagueEventType(LeagueEventType.DEFAULT)
                            .playersInvolved(Arrays.asList(playerName))
                            .points(points)
                            .build();

                    instance.getEventRepository().insert(leagueEvent);

                    manager.addPoints(time, clanTag, total, motive);

                    int current = manager.getPointsByTag(time, clanTag);
                    context.reply("&aClan &f" + clanTag + " &aagora possui &f" + current + " &apontos!");
                }).registerAndBind(consumer, "ligaadm");

        Commands.create()
                .assertPlayer()
                .assertPermission("league.admin")
                .assertUsage("<posição> [mensal/trimestral]")
                .handler(context -> {
                    String action = context.arg(0).parseOrFail(String.class);
                    if (action.equalsIgnoreCase("reload")) {
                        instance.getRankingModule().refreshRanking();

                        context.reply("&aRanking recarregado!");
                        return;
                    }

                    if (!context.arg(1).isPresent()) {
                        context.reply("&cVocê deve especificar o intervalo de tempo! &8(Mensal/Trimestral)");
                        return;
                    }

                    int position = context.arg(0).parseOrFail(Integer.class);
                    if (position < 1 || position > 10) {
                        context.reply("&cPosição inválida! &8(Apenas de 1 à 10)");
                        return;
                    }

                    String intervalName = context.arg(1)
                            .parse(String.class)
                            .orElse("mensal")
                            .toUpperCase();

                    IntervalTime time;
                    try {
                        time = IntervalTime.valueOf(intervalName);
                    } catch (Exception exception) {
                        context.reply("&cIntervalo de tempo inválido! &8(Mensal/Trimestral)");
                        return;
                    }

                    Player player = context.sender();

                    Location location = player.getLocation();
                    String locationString = location.getWorld().getName() + ","
                            + location.getX() + ","
                            + location.getY() + ","
                            + location.getZ() + ","
                            + location.getYaw() + ","
                            + location.getPitch();

                    instance.getConfig().set("ranking.position." + time.name() + "." + position, locationString);
                    instance.saveConfig();

                    context.reply("&aVocê definiu a posição #" + position + " do ranking " + time.fancyName() + "!");
                }).registerAndBind(consumer, "ligasetranking");

    }
}

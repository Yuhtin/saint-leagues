package com.yuhtin.quotes.saint.leagues.command;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import lombok.AllArgsConstructor;
import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

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
                .assertUsage("<add/remove> <clan> <pontos>")
                .handler(context -> {
                    String action = context.arg(0).parseOrFail(String.class);
                    String clanTag = context.arg(1).parseOrFail(String.class).toUpperCase();
                    int points = Math.max(0, context.arg(2).parseOrFail(Integer.class));

                    int currentPoints = LeagueClanCache.getInstance().getPointsByTag(clanTag);
                    if (currentPoints == -1) {
                        context.reply("&cClan não encontrado!");
                        return;
                    }

                    if (action.equalsIgnoreCase("add")) {
                        currentPoints += points;
                    } else {
                        currentPoints -= points;
                    }

                    currentPoints = Math.max(currentPoints, 0);
                    LeagueClanCache.getInstance().setClanPoints(clanTag, currentPoints);

                    context.reply("&aClan &f" + clanTag + " &aagora possui &f" + currentPoints + " &apontos!");
                }).registerAndBind(consumer, "ligaadm");

        Commands.create()
                .assertPlayer()
                .assertPermission("league.admin")
                .assertUsage("<posição>")
                .handler(context -> {
                    String action = context.arg(0).parseOrFail(String.class);
                    if (action.equalsIgnoreCase("reload")) {
                        instance.getRankingModule().refreshRanking();

                        context.reply("&aRanking recarregado!");
                        return;
                    }

                    int position = context.arg(0).parseOrFail(Integer.class);
                    if (position < 1 || position > 10) {
                        context.reply("&cPosição inválida! &8(Apenas de 1 à 10)");
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

                    instance.getConfig().set("ranking.position." + position, locationString);
                    instance.saveConfig();

                    context.reply("&aVocê definiu a posição " + position + " do ranking!");
                }).registerAndBind(consumer, "ligasetranking");

    }
}

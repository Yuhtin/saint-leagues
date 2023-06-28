package com.yuhtin.quotes.saint.leagues.command;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.repository.repository.ClanRepository;
import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueClanCommand implements TerminableModule {

    @Override
    public void setup(@Nonnull TerminableConsumer consumer) {

        Commands.parserRegistry().register(LeagueClan.class, context -> {
            LeagueClan clan = LeaguesPlugin.getInstance().getClanRepository().findByTag(context);
            if (clan == null) return Optional.empty();

            return Optional.of(clan);
        });

        Commands.create()
                .assertPermission("league.admin")
                .assertUsage("<add/remove> <clan> <pontos>")
                .handler(context -> {
                    String action = context.arg(0).parseOrFail(String.class);
                    LeagueClan clan = context.arg(1).parseOrFail(LeagueClan.class);
                    int points = context.arg(2).parseOrFail(Integer.class);

                    ClanRepository clanRepository = LeaguesPlugin.getInstance().getClanRepository();
                    if (action.equalsIgnoreCase("add")) {
                        clan.setPoints(clan.getPoints() + points);
                        clanRepository.insert(clan);
                    } else {
                        clan.setPoints(Math.max(0, clan.getPoints() - points));
                        clanRepository.insert(clan);
                    }

                    context.reply("&aClan &f" + clan.getTag() + " &aagora possui &f" + clan.getPoints() + " &apontos!");
                }).registerAndBind(consumer, "liga");

        Commands.create()
                .assertPlayer()
                .assertPermission("league.admin")
                .assertUsage("<posição>")
                .handler(context -> {
                    int position = context.arg(0).parseOrFail(Integer.class);
                    if (position < 1 || position > 10) {
                        context.reply("&cPosição inválida! &8(Apenas de 1 à 10)");
                        return;
                    }

                    Player player = context.sender();

                    Location location = player.getLocation();
                    String locationString = location.getWorld().getName() + ","
                            + location.getBlockX() + ","
                            + location.getBlockY() + ","
                            + location.getBlockZ() + ","
                            + location.getYaw() + ","
                            + location.getPitch();

                    LeaguesPlugin.getInstance().getConfig().set("ranking.position." + position, locationString);
                    LeaguesPlugin.getInstance().saveConfig();

                    context.reply("&aVocê definiu a posição " + position + " do ranking!");
                }).registerAndBind(consumer, "ligasetranking");

    }
}

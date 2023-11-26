package com.yuhtin.quotes.saint.leagues.placeholder;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import lombok.AllArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class ClansPlaceholder extends PlaceholderExpansion {

    final LeaguesPlugin instance;
    final RepositoryManager manager;

    @Override
    public @NotNull String getIdentifier() {
        return "saintleagues";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Yuhtin";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] split = params.split("-");
        if (params.startsWith("clan")) {
            String playerName = split[1];
            String clanTag = instance.getSimpleClansAccessor().getClanTag(playerName);

            return clanTag == null ? "..." : clanTag;
        }

        if (params.startsWith("points")) {
            IntervalTime interval = IntervalTime.valueOf(split[1].toUpperCase());
            String clanTag = instance.getSimpleClansAccessor().getClanTag(player.getName());

            return String.valueOf(manager.getPointsByTag(interval, clanTag));
        }

        IntervalTime interval = IntervalTime.valueOf(split[0].toUpperCase());
        String type = split[1];
        int position = Integer.parseInt(split[2]);

        LeagueClan clan = manager.getByPosition(interval, position);
        if (clan == null) return "...";

        return type.equalsIgnoreCase("name") ? clan.getTag() : String.valueOf(clan.getPoints());
    }
}

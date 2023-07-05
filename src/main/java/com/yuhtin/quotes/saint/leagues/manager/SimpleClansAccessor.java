package com.yuhtin.quotes.saint.leagues.manager;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class SimpleClansAccessor {

    private final SimpleClans simpleClans;

    public SimpleClansAccessor(PluginManager pluginManager) {
        this.simpleClans = (SimpleClans) pluginManager.getPlugin("SimpleClans");
    }

    public boolean isValid() {
        return simpleClans != null;
    }

    @Nullable
    public String getClanTag(String playerName) {
        ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(playerName);
        if (clanPlayer == null) return null;

        Clan clan = clanPlayer.getClan();
        if (clan == null) return null;

        return clanPlayer.getClan().getTag().toUpperCase();
    }

    @Nullable
    public String getClanTag(Player player) {
        ClanPlayer clanPlayer = simpleClans.getClanManager().getClanPlayer(player.getUniqueId());
        if (clanPlayer == null) return null;

        Clan clan = clanPlayer.getClan();
        if (clan == null) return null;

        return clan.getTag().toUpperCase();
    }

    @Nullable
    public List<String> getClanPlayers(String clanTag) {
        Clan clan = simpleClans.getClanManager().getClan(clanTag);
        if (clan == null) return null;

        return clan.getMembers().stream()
                .map(ClanPlayer::getName)
                .collect(Collectors.toList());
    }

}

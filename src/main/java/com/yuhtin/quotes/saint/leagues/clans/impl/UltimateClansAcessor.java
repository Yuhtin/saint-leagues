package com.yuhtin.quotes.saint.leagues.clans.impl;

import com.yuhtin.quotes.saint.leagues.clans.ClanAcessor;
import me.ulrich.clans.Clans;
import me.ulrich.clans.data.ClanData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UltimateClansAcessor implements ClanAcessor {

    private final Clans clans;

    public UltimateClansAcessor(PluginManager pluginManager) {
        this.clans = (Clans) pluginManager.getPlugin("UltimateClans");
    }

    @Override
    public boolean isValid() {
        return clans != null;
    }

    @Nullable
    @Override
    public String getClanTag(String playerName) {
        Optional<ClanData> data = clans.getPlayerAPI().getPlayerClan(playerName);
        return data.map(ClanData::getTag).orElse(null);
    }

    @Nullable
    @Override
    public String getClanTag(Player player) {
        Optional<ClanData> data = clans.getPlayerAPI().getPlayerClan(player.getUniqueId());
        return data.map(ClanData::getTag).orElse(null);
    }

    @Nullable
    @Override
    public List<String> getClanPlayers(String clanTag) {
        Optional<ClanData> clanByTag = clans.getClanAPI().getClanDataByTag(clanTag);

        return clanByTag.map(clanData -> clanData.getMembers().stream()
                .map(Bukkit::getOfflinePlayer)
                .filter(OfflinePlayer::hasPlayedBefore)
                .map(OfflinePlayer::getName)
                .collect(Collectors.toList()))
                .orElse(null);
    }
}

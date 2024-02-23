package com.yuhtin.quotes.saint.leagues.clans;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ClanAcessor {

    boolean isValid();

    @Nullable
    String getClanTag(String playerName);

    @Nullable
    String getClanTag(Player player);

    @Nullable
    List<String> getClanPlayers(String clanTag);

}

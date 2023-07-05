package com.yuhtin.quotes.saint.leagues.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
@Getter
public enum LeagueEventType {

    KILL_DRAGON(Material.ENDER_DRAGON_SPAWN_EGG),
    WIN_CLAN_EVENTS(Material.DIAMOND_CHESTPLATE),
    WIN_EVENTS(Material.WOODEN_SWORD);

    private final Material material;

}

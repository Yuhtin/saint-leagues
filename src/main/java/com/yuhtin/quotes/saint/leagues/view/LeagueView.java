package com.yuhtin.quotes.saint.leagues.view;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.ViewCache;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.repository.repository.TimedClanRepository;
import com.yuhtin.quotes.saint.leagues.util.ItemBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueView extends SimpleInventory {

    private final ViewCache viewCache;

    public LeagueView(ViewCache viewCache) {
        super("league.main", viewCache.getPlugin().getConfig().getString("view.mainInventoryName"), 3 * 9);
        this.viewCache = viewCache;
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        LeaguesPlugin instance = viewCache.getPlugin();
        RepositoryManager manager = RepositoryManager.getInstance();

        Player player = viewer.getPlayer();
        String clanTag = instance.getClanAcessor().getClanTag(player);

        int playerPoints = instance.getEventRepository()
                .groupByPlayer(player.getName(), manager.getRepository(IntervalTime.MENSAL))
                .stream()
                .map(LeagueEvent::getPoints)
                .reduce(0, Integer::sum);

        List<String> lore = new ArrayList<>();
        if (clanTag == null) {
            instance.getConfig().getStringList("view.profile.noClanLore")
                    .stream()
                    .map(line -> line.replace("%pontos%", String.valueOf(playerPoints)))
                    .forEach(lore::add);
        } else {
            for (String line : instance.getConfig().getStringList("view.profile.clanLore")) {
                if (line.contains("%info%")) {
                    for (IntervalTime time : IntervalTime.values()) {
                        TimedClanRepository repository = manager.getRepository(time);

                        int points = repository.getPointsByTag(clanTag);
                        int position = repository.getPositionByClan(clanTag);

                        lore.add(instance.getConfig().getString("view.clanRankingInfo")
                                .replace("%position%", String.valueOf(position))
                                .replace("%pontos%", String.valueOf(points))
                                .replace("%time%", time.fancyName())
                        );
                    }
                } else {
                    lore.add(line
                            .replace("%pontos%", String.valueOf(playerPoints))
                            .replace("%clan%", clanTag)
                    );
                }
            }
        }

        editor.setItem(11, InventoryItem.of(new ItemBuilder(player.getName())
                        .name(instance.getConfig().getString("view.profile.name"))
                        .setLore(lore)
                        .wrap())
                .defaultCallback(callback -> viewCache.getHistoricView().openInventory(
                        player,
                        $ -> $.getPropertyMap().set("playerName", player.getName())
                ))
        );

        editor.setItem(13, InventoryItem.of(new ItemBuilder(instance.getConfig().getString("view.rewards.material"))
                        .name(instance.getConfig().getString("view.rewards.name"))
                        .setLore(instance.getConfig().getStringList("view.rewards.lore"))
                        .wrap())
                .defaultCallback(callback -> {
                    player.closeInventory();
                    player.chat("/ligarewards");
                })
        );

        editor.setItem(14, InventoryItem.of(new ItemBuilder(instance.getConfig().getString("view.leagueRank.material"))
                        .name(instance.getConfig().getString("view.leagueRank.name"))
                        .setLore(instance.getConfig().getStringList("view.leagueRank.lore"))
                        .wrap())
                .defaultCallback(callback -> viewCache.getRankingView().openInventory(player))
        );

        editor.setItem(15, InventoryItem.of(new ItemBuilder(instance.getConfig().getString("view.leagueHistoric.material"))
                .name(instance.getConfig().getString("view.leagueHistoric.name"))
                .setLore(instance.getConfig().getStringList("view.leagueHistoric.lore"))
                .wrap())
                .defaultCallback(callback -> viewCache.getHistoricView().openInventory(player))
        );
    }
}

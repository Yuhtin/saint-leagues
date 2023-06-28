package com.yuhtin.quotes.saint.leagues.view;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.CustomInventory;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.RankingCache;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueView extends SimpleInventory {

    public LeagueView() {
        super("league.main", "Liga", 3);
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        Player player = viewer.getPlayer();

        String clanTag = "TST";

        LeagueClan clan = RankingCache.getInstance().getByTag(clanTag);
        int rankingPosition = clan.getRankPosition();
        int points = clan.getPoints();

        int playerPoints = LeaguesPlugin.getInstance().getEventRepository()
                .groupByPlayer(player.getName())
                .stream()
                .map(LeagueEvent::getPoints)
                .reduce(0, Integer::sum);

        editor.setItem(12, InventoryItem.of(new ItemBuilder(player.getName())
                        .name("&aSeu Perfil")
                        .setLore(
                                "&fSeu clan: &e" + clanTag + " &8| &e" + points + " pontos &6(#" + rankingPosition + ")",
                                "&fSeus pontos : &e" + playerPoints,
                                "",
                                "&aClique para ver os eventos que participou!"
                        ).wrap())
                .defaultCallback(callback -> {
                    CustomInventory inventory = new HistoricView(player.getName()).init();
                    inventory.openInventory(player);
                })
        );

        editor.setItem(14, InventoryItem.of(new ItemBuilder(Material.NETHER_STAR)
                        .name("&aRanking da liga")
                        .setLore(
                                "&7Veja quais clans estão liderando",
                                "&7a liga atualmente",
                                "",
                                "&aClique para ver o ranking da liga!"
                        ).wrap())
                .defaultCallback(callback -> {
                    CustomInventory inventory = new RankingView().init();
                    inventory.openInventory(player);
                })
        );

        editor.setItem(15, InventoryItem.of(new ItemBuilder(Material.BLUE_BANNER).name("&aHistórico de Eventos")
                .setLore(
                        "&7Veja os eventos anteriores que",
                        "&7ocorream na liga e quantos",
                        "&7pontos foram fornecidos",
                        "",
                        "&aClique para ver o histórico!"
                ).wrap()).defaultCallback(callback -> {
                    CustomInventory inventory = new HistoricView(null).init();
                    inventory.openInventory(player);
                })
        );
    }
}

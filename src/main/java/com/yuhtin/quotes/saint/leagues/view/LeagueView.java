package com.yuhtin.quotes.saint.leagues.view;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import com.yuhtin.quotes.saint.leagues.cache.ViewCache;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueView extends SimpleInventory {

    private final ViewCache viewCache;

    public LeagueView(ViewCache viewCache) {
        super("league.main", "Liga", 3 * 9);
        this.viewCache = viewCache;
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        LeaguesPlugin instance = LeaguesPlugin.getInstance();

        Player player = viewer.getPlayer();
        String clanTag = instance.getSimpleClansAccessor().getClanTag(player);

        int playerPoints = instance.getEventRepository()
                .groupByPlayer(player.getName())
                .stream()
                .map(LeagueEvent::getPoints)
                .reduce(0, Integer::sum);

        String clanLore = "&fSeu clan: &cNenhum";
        if (clanTag != null) {
            int points = LeagueClanCache.getInstance().getPointsByTag(clanTag);
            int position = LeagueClanCache.getInstance().getPositionByClan(clanTag);

            clanLore = "&fSeu clan: &e" + clanTag + " &8| &e" + points + " pontos &6(#" + position + ")";
        }

        editor.setItem(12, InventoryItem.of(new ItemBuilder(player.getName())
                        .name("&aSeu Perfil")
                        .setLore(
                                clanLore,
                                "&fSeus pontos: &e" + playerPoints,
                                "",
                                "&aClique para ver os eventos que participou!"
                        ).wrap())
                .defaultCallback(callback -> {
                    viewCache.getHistoricView().openInventory(
                            player,
                            $ -> $.getPropertyMap().set("playerName", player.getName())
                    );
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
                .defaultCallback(callback -> viewCache.getRankingView().openInventory(player))
        );

        editor.setItem(15, InventoryItem.of(new ItemBuilder(Material.CYAN_BANNER).name("&aHistórico de Eventos")
                .setLore(
                        "&7Veja os eventos anteriores que",
                        "&7ocorream na liga e quantos",
                        "&7pontos foram fornecidos",
                        "",
                        "&aClique para ver o histórico!"
                ).wrap()).defaultCallback(callback -> viewCache.getHistoricView().openInventory(player))
        );
    }
}

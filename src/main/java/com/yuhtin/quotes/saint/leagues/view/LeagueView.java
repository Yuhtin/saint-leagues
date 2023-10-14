package com.yuhtin.quotes.saint.leagues.view;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.cache.ViewCache;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
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
        super("league.main", "Saint Ligas", 3 * 9);
        this.viewCache = viewCache;
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        LeaguesPlugin instance = LeaguesPlugin.getInstance();
        RepositoryManager manager = RepositoryManager.getInstance();

        Player player = viewer.getPlayer();
        String clanTag = instance.getSimpleClansAccessor().getClanTag(player);

        int playerPoints = instance.getEventRepository()
                .groupByPlayer(player.getName(), manager.getRepository(IntervalTime.MENSAL))
                .stream()
                .map(LeagueEvent::getPoints)
                .reduce(0, Integer::sum);

        List<String> lore = new ArrayList<>();
        lore.add("&fSeus pontos: &e" + playerPoints);

        if (clanTag == null) {
            lore.add("&fSeu clan: &cNenhum");
        } else {
            lore.add("&fSeu clan: &e" + clanTag);
            lore.add("");

            for (IntervalTime time : IntervalTime.values()) {
                TimedClanRepository repository = manager.getRepository(time);

                int points = repository.getPointsByTag(clanTag);
                int position = repository.getPositionByClan(clanTag);

                lore.add(" &f" + time.fancyName() + ": &e" + points + " pontos &6(#" + position + ")");
            }
        }

        lore.add("");
        lore.add("&aClique para ver os eventos que participou!");

        editor.setItem(11, InventoryItem.of(new ItemBuilder(player.getName())
                        .name("&aSeu Perfil")
                        .setLore(lore)
                        .wrap())
                .defaultCallback(callback -> viewCache.getHistoricView().openInventory(
                        player,
                        $ -> $.getPropertyMap().set("playerName", player.getName())
                ))
        );

        editor.setItem(13, InventoryItem.of(new ItemBuilder("/texture/a2629f2682dcee30f5855b1e5427cc4bee73d18a276fafc520d693b40ca81b22")
                        .name("&aRecompensas")
                        .setLore(
                                "&7Veja as recompensas que o clan",
                                "&7pode receber ao final da liga",
                                "",
                                "&aClique para ver as recompensas!"
                        ).wrap())
                .defaultCallback(callback -> {
                    player.closeInventory();
                    player.chat("/ligarewards");
                })
        );

        editor.setItem(14, InventoryItem.of(new ItemBuilder("/texture/e34a592a79397a8df3997c43091694fc2fb76c883a76cce89f0227e5c9f1dfe")
                        .name("&aRanking da liga")
                        .setLore(
                                "&7Veja quais clans estão liderando",
                                "&7a liga atualmente",
                                "",
                                "&aClique para ver o ranking da liga!"
                        ).wrap())
                .defaultCallback(callback -> viewCache.getRankingView().openInventory(player))
        );

        editor.setItem(15, InventoryItem.of(new ItemBuilder("/texture/ab527a18dec3d6dac532f5555b9119c31b7a8397b8a06d249d0eb39241c5485f")
                .name("&aHistórico de Eventos")
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

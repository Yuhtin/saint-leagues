package com.yuhtin.quotes.saint.leagues.view;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.enums.DefaultItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.border.Border;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.sun.istack.internal.NotNull;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.repository.repository.EventRepository;
import com.yuhtin.quotes.saint.leagues.util.ItemBuilder;
import lombok.val;
import org.bukkit.Material;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class HistoricView extends PagedInventory {

    private final String playerName;
    private final Map<String, Integer> rankingSorterType = new HashMap<>();

    public HistoricView(String playerName) {
        super("league.historic", "Histórico" + (playerName != null ? " de " + playerName : ""), 5 * 9);
        this.playerName = playerName;
    }

    @Override
    protected void configureViewer(PagedViewer viewer) {
        val configuration = viewer.getConfiguration();

        configuration.backInventory("league.main");
        configuration.itemPageLimit(14);
        configuration.border(Border.of(1, 1, 2, 1));
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        editor.setItem(39, sortRankingItem(viewer));
        editor.setItem(40, DefaultItem.BACK.toInventoryItem(viewer));
    }

    @Override
    protected void update(@NotNull PagedViewer viewer, @NotNull InventoryEditor editor) {
        super.update(viewer, editor);
        configureInventory(viewer, viewer.getEditor());
    }


    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        EventRepository eventRepository = LeaguesPlugin.getInstance().getEventRepository();
        Set<LeagueEvent> events = playerName != null ? eventRepository.groupByPlayer(playerName) : eventRepository.findAll();

        List<InventoryItemSupplier> items = new ArrayList<>();
        for (LeagueEvent event : events) {
            items.add(() -> {

                List<String> playersInvolved = event.getPlayersInvolved();
                return InventoryItem.of(new ItemBuilder(event.getEventType().getMaterial())
                        .name("&a" + event.getName() + " &8(#" + event.getId() + ")")
                        .setLore(
                                "&fData: &e" + event.getFormattedDate(),
                                "&fPontos: &e" + event.getPoints(),
                                "",
                                "&fClan vencedor: &e" + event.getClanTag(),
                                "&fJogadores do clan: &e" + String.join(", ", playersInvolved)
                        ).wrap());

            });
        }

        return items;
    }

    private InventoryItem sortRankingItem(Viewer viewer) {
        AtomicInteger currentFilter = new AtomicInteger(rankingSorterType.getOrDefault(viewer.getName(), -1));
        return InventoryItem.of(new ItemBuilder(Material.HOPPER)
                        .name("&6Ordenar ranking")
                        .setLore(
                                "&7Ordene o ranking da maneira deseja",
                                "",
                                getColorByFilter(currentFilter.get(), -1) + " Todos",
                                getColorByFilter(currentFilter.get(), 0) + " Matar o dragão",
                                getColorByFilter(currentFilter.get(), 1) + " Eventos de clan",
                                getColorByFilter(currentFilter.get(), 2) + " Outros eventos",
                                "",
                                "&aClique para mudar o tipo de ordenação."
                        )
                        .wrap())
                .defaultCallback(event -> {
                    rankingSorterType.put(viewer.getName(), currentFilter.incrementAndGet() > 2 ? -1 : currentFilter.get());
                    event.updateInventory();
                });
    }

    private String getColorByFilter(int currentFilter, int loopFilter) {
        return currentFilter == loopFilter ? " &b▶" : "&8";
    }

}

package com.yuhtin.quotes.saint.leagues.view;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.enums.DefaultItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.border.Border;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.henryfabio.minecraft.inventoryapi.viewer.property.ViewerPropertyMap;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.ViewCache;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.model.LeagueEventType;
import com.yuhtin.quotes.saint.leagues.repository.repository.EventRepository;
import com.yuhtin.quotes.saint.leagues.repository.repository.TimedClanRepository;
import com.yuhtin.quotes.saint.leagues.util.ItemBuilder;
import lombok.val;
import org.bukkit.Material;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class HistoricView extends PagedInventory {

    private final Map<String, Integer> rankingSorterType = new HashMap<>();
    private final Map<String, Integer> periodSorterType = new HashMap<>();
    private final LeaguesPlugin instance;

    public HistoricView(ViewCache viewCache) {
        super("league.historic", "Historico", 5 * 9);
        this.instance = viewCache.getPlugin();
    }

    @Override
    protected void configureViewer(PagedViewer viewer) {
        val configuration = viewer.getConfiguration();

        configuration.backInventory("league.main");
        configuration.itemPageLimit(14);
        configuration.border(Border.of(1, 1, 2, 1));

        ViewerPropertyMap propertyMap = viewer.getPropertyMap();
        String playerName = propertyMap.get("playerName");
        if (playerName != null) {
            configuration.titleInventory("Historico de " + playerName);
        }
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        editor.setItem(39, sortRankingItem(viewer));
        editor.setItem(40, DefaultItem.BACK.toInventoryItem(viewer));
        editor.setItem(41, periodFilter(viewer));
    }

    @Override
    protected void update(PagedViewer viewer, InventoryEditor editor) {
        super.update(viewer, editor);
        configureInventory(viewer, viewer.getEditor());
    }


    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        String playerName = viewer.getPropertyMap().get("playerName");

        int periodFilter = periodSorterType.getOrDefault(viewer.getName(), 0);
        IntervalTime intervalTime = IntervalTime.values()[periodFilter];
        TimedClanRepository timedRepository = RepositoryManager.getInstance().getRepository(intervalTime);

        EventRepository eventRepository = instance.getEventRepository();
        Set<LeagueEvent> events = playerName != null
                ? eventRepository.groupByPlayer(playerName, timedRepository)
                : eventRepository.findAll(timedRepository);

        int filterValue = rankingSorterType.getOrDefault(viewer.getName(), -1);

        List<InventoryItemSupplier> items = new ArrayList<>();
        for (LeagueEvent event : events) {
            LeagueEventType leagueEventType = event.getLeagueEventType();
            if (filterValue != -1 && filterValue != leagueEventType.ordinal()) continue;

            items.add(() -> {

                List<String> playersInvolved = event.getPlayersInvolved();
                return InventoryItem.of(new ItemBuilder(event.getLeagueEventType().getItemStack())
                        .name(instance.getConfig().getString("view.historic.name")
                                .replace("%event%", event.getName())
                                .replace("%id%", event.getId()))
                        .setLore(instance.getConfig().getStringList("view.historic.lore").stream().map(line -> line
                                        .replace("%date%", event.getFormattedDate())
                                        .replace("%points%", String.valueOf(event.getPoints()))
                                        .replace("%clan%", event.getClanTag())
                                        .replace("%players%", String.join(", ", playersInvolved)))
                                .collect(Collectors.toList())
                        ).wrap());

            });
        }

        return items;
    }

    private InventoryItem periodFilter(Viewer viewer) {
        AtomicInteger currentFilter = new AtomicInteger(periodSorterType.getOrDefault(viewer.getName(), 0));

        List<String> lore = new ArrayList<>();
        for (String line : instance.getConfig().getStringList("view.period.lore")) {
            if (line.contains("%info%")) {
                lore.add(getColorByFilter(currentFilter.get(), 0) + " Mensal");
                lore.add(getColorByFilter(currentFilter.get(), 1) + " Trimestral");
            } else {
                lore.add(line);
            }
        }

        return InventoryItem.of(new ItemBuilder(Material.valueOf(instance.getConfig().getString("view.period.material")))
                        .name(instance.getConfig().getString("view.period.name"))
                        .setLore(lore)
                        .wrap())
                .defaultCallback(event -> {
                    periodSorterType.put(viewer.getName(), currentFilter.incrementAndGet() > 1 ? 0 : currentFilter.get());
                    event.updateInventory();
                });
    }

    private InventoryItem sortRankingItem(Viewer viewer) {
        AtomicInteger currentFilter = new AtomicInteger(rankingSorterType.getOrDefault(viewer.getName(), -1));

        List<String> lore = new ArrayList<>();
        for (String line : instance.getConfig().getStringList("view.sortRanking.lore")) {
            if (line.contains("%info%")) {
                lore.add(getColorByFilter(currentFilter.get(), -1) + " Todos");
                lore.add(getColorByFilter(currentFilter.get(), 0) + " Matar o dragão");
                lore.add(getColorByFilter(currentFilter.get(), 1) + " Eventos de clan");
                lore.add(getColorByFilter(currentFilter.get(), 2) + " Outros eventos");
            } else {
                lore.add(line);
            }
        }

        return InventoryItem.of(new ItemBuilder(Material.valueOf(instance.getConfig().getString("view.sortRanking.material")))
                        .name(instance.getConfig().getString("view.sortRanking.name"))
                        .setLore(lore)
                        .wrap())
                .defaultCallback(event -> {
                    rankingSorterType.put(viewer.getName(), currentFilter.incrementAndGet() > 2 ? -1 : currentFilter.get());
                    event.updateInventory();
                });
    }

    private String getColorByFilter(int currentFilter, int loopFilter) {
        return currentFilter == loopFilter ? " " + instance.getConfig().getString("view.filterColor") + "▶" : "&8";
    }

}

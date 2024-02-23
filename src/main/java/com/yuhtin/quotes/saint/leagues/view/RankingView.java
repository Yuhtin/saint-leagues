package com.yuhtin.quotes.saint.leagues.view;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.enums.DefaultItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.border.Border;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.ViewCache;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.util.BannerAlphabetic;
import com.yuhtin.quotes.saint.leagues.util.ItemBuilder;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class RankingView extends PagedInventory {

    private final Map<String, Integer> sorterType = new HashMap<>();

    private final LeaguesPlugin instance;

    public RankingView(ViewCache viewCache) {
        super("league.ranking", "Ranking", 5 * 9);
        this.instance = viewCache.getPlugin();
    }

    @Override
    protected void configureViewer(PagedViewer viewer) {
        val configuration = viewer.getConfiguration();

        configuration.backInventory("league.main");
        configuration.itemPageLimit(14);
        configuration.border(Border.of(1, 1, 2, 1));
    }

    @Override
    protected void configureInventory(@NotNull Viewer viewer, InventoryEditor editor) {
        int index = sorterType.getOrDefault(viewer.getPlayer().getName(), 0);
        IntervalTime intervalTime = IntervalTime.values()[index];

        viewer.getConfiguration().titleInventory("Ranking - " + intervalTime.fancyName());

        editor.setItem(40, DefaultItem.BACK.toInventoryItem(viewer));
        editor.setItem(41, sortRankingItem(viewer));
    }

    @Override
    protected void update(@NotNull PagedViewer viewer, @NotNull InventoryEditor editor) {
        super.update(viewer, editor);
        configureInventory(viewer, viewer.getEditor());
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(@NotNull PagedViewer viewer) {
        RepositoryManager manager = RepositoryManager.getInstance();
        List<InventoryItemSupplier> items = new ArrayList<>();

        Integer integer = sorterType.getOrDefault(viewer.getPlayer().getName(), 0);
        IntervalTime intervalTime = IntervalTime.values()[integer];

        for (String clanTag : manager.getRanking(intervalTime)) {
            items.add(() -> {
                int clanAppearences = LeaguesPlugin.getInstance()
                        .getEventRepository()
                        .countClanAppearences(clanTag);

                BannerAlphabetic bannerAlphabetic = BannerAlphabetic.bannerByLetter(clanTag.charAt(0));
                if (bannerAlphabetic == null) {
                    bannerAlphabetic = BannerAlphabetic.bannerByLetter('A');
                }

                ItemStack itemStack = bannerAlphabetic.getBanner();

                int position = manager.getPositionByClan(intervalTime, clanTag);
                int points = manager.getPointsByTag(intervalTime, clanTag);

                return InventoryItem.of(new ItemBuilder(itemStack)
                        .name(instance.getConfig().getString("view.ranking.name")
                                .replace("%clan%", clanTag)
                                .replace("%position%", String.valueOf(position)))
                        .hideAttributes()
                        .setLore(instance.getConfig().getString("view.ranking.lore")
                                .replace("%pontos%", String.valueOf(points))
                                .replace("%participacoes%", String.valueOf(clanAppearences))
                        ).wrap());
            });
        }

        return items;
    }

    private InventoryItem sortRankingItem(Viewer viewer) {
        AtomicInteger currentFilter = new AtomicInteger(sorterType.getOrDefault(viewer.getName(), 0));

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
                    sorterType.put(viewer.getName(), currentFilter.incrementAndGet() > 1 ? 0 : currentFilter.get());
                    event.updateInventory();
                });
    }

    private String getColorByFilter(int currentFilter, int loopFilter) {
        return currentFilter == loopFilter ? " " + instance.getConfig().getString("view.filterColor") + "▶" : "&8";
    }

}

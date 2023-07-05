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
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import com.yuhtin.quotes.saint.leagues.util.BannerAlphabetic;
import com.yuhtin.quotes.saint.leagues.util.ItemBuilder;
import lombok.val;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class RankingView extends PagedInventory {

    public RankingView() {
        super("league.ranking", "Ranking", 5 * 9);
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
        editor.setItem(40, DefaultItem.BACK.toInventoryItem(viewer));
    }

    @Override
    protected void update(PagedViewer viewer, InventoryEditor editor) {
        super.update(viewer, editor);
        configureInventory(viewer, viewer.getEditor());
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        LeagueClanCache leagueClanCache = LeagueClanCache.getInstance();
        List<InventoryItemSupplier> items = new ArrayList<>();

        for (String clanTag : leagueClanCache.getRanking()) {
            items.add(() -> {
                int clanAppearences = LeaguesPlugin.getInstance()
                        .getEventRepository()
                        .countClanAppearences(clanTag);

                BannerAlphabetic bannerAlphabetic = BannerAlphabetic.bannerByLetter(clanTag.charAt(0));
                if (bannerAlphabetic == null) {
                    bannerAlphabetic = BannerAlphabetic.bannerByLetter('A');
                }

                ItemStack itemStack = bannerAlphabetic.getBanner();

                int position = leagueClanCache.getPositionByClan(clanTag);
                int points = leagueClanCache.getPointsByTag(clanTag);

                return InventoryItem.of(new ItemBuilder(itemStack)
                        .name("&a" + clanTag + " &6(#" + position + ")")
                        .hideAttributes()
                        .setLore(
                                "&fPontos: &e" + points,
                                "&fParticipações em eventos: &e" + clanAppearences
                        ).wrap());
            });
        }

        return items;
    }
}

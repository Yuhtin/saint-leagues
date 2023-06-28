package com.yuhtin.quotes.saint.leagues.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

/**
 * @author zMathi
 */

@AllArgsConstructor
@Getter
public enum BannerAlphabetic {

    A("A", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    B("B", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    C("C", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    D("D", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    E("E", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    F("F", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    G("G", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    H("H", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    I("I", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    J("J", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    K("K", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR), new Pattern(DyeColor.WHITE, PatternType.CROSS), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    L("L", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    M("M", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_TOP), new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    N("N", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.DIAGONAL_RIGHT_MIRROR), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    O("O", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    P("P", new Pattern[]{new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    Q("Q", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_RIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    R("R", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    S("S", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER), new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER)}),
    T("T", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    U("U", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    V("V", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    W("W", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    X("X", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER), new Pattern(DyeColor.WHITE, PatternType.CROSS), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    Y("Y", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.CROSS), new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    Z("Z", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    ONE("1", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    TWO("2", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    THREE("3", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    FOUR("4", new Pattern[]{new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    FIVE("5", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_LEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    SIX("6", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    SEVEN("7", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.DIAGONAL_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_LEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    EIGHT("8", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    NINE("9", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER)}),
    ZERO("0", new Pattern[]{new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.BLACK, PatternType.BORDER)});

    private final String letter;
    private final Pattern[] patterns;

    public boolean isNumber() {
        try {
            Integer.parseInt(letter);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public ItemStack getBanner() {
        return getBanner(DyeColor.WHITE, DyeColor.BLACK);
    }

    public ItemStack getBanner(DyeColor letterColor, DyeColor backColor) {
        Material material = null;
        try {
            material = Material.getMaterial("BANNER");
        } catch (Exception e) {
        }
        if (material == null) {
            try {
                material = Material.getMaterial("BLACK_BANNER");
            } catch (Exception e) {
            }
        }
        if (material == null) {
            return new ItemStack(Material.GLASS);
        }
        ItemStack banner = new ItemStack(material);
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.setDisplayName("§7" + letter);
        bannerMeta.setBaseColor(backColor);

        for (Pattern patterns : patterns) {
            bannerMeta.addPattern(patterns.getColor() == DyeColor.BLACK ? new Pattern(backColor, patterns.getPattern()) : new Pattern(letterColor, patterns.getPattern()));
        }
        banner.setItemMeta(bannerMeta);
        return banner;
    }

    public static BannerAlphabetic bannerByLetter(char letter) {
        for (BannerAlphabetic banners : values()) {
            if (banners.letter.charAt(0) == letter) return banners;
        }
        return null;
    }
}

package com.yuhtin.quotes.saint.leagues.util;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder {

    private static final ItemStack SKULL_ITEM = new ItemStack(Material.PLAYER_HEAD);

    private final ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material type) {
        this(new ItemStack(type));
    }

    public ItemBuilder(Material type, int data) {
        this(new ItemStack(type, 1, (short) data));
    }

    public ItemBuilder(String link) {
        Material material = Material.getMaterial(link);
        if (material != null) {
            item = new ItemStack(material);
            return;
        }

        item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null) return;

        PlayerProfile profile;

        if (!link.startsWith("/texture/")) {
            profile = Bukkit.createPlayerProfile(link);
        } else {
            profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "");
            PlayerTextures textures = profile.getTextures();

            link = "http://textures.minecraft.net" + link;
            try {
                textures.setSkin(new URL(link));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        meta.setOwnerProfile(profile);
        item.setItemMeta(meta);
    }

    public ItemBuilder(Material type, Color color) {
        item = new ItemStack(type);

        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
    }

    public ItemBuilder changeItemMeta(Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = item.getItemMeta();
        consumer.accept(itemMeta);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder name(String name) {
        return changeItemMeta(it -> it.setDisplayName(ColorUtil.colored(name)));
    }

    public ItemBuilder setLore(String... lore) {
        return changeItemMeta(it -> it.setLore(Arrays.asList(ColorUtil.colored(lore))));
    }

    public ItemBuilder setLore(List<String> lore) {
        return changeItemMeta(it -> it.setLore(ColorUtil.colored(lore)));
    }

    public ItemStack wrap() {
        return item;
    }

    public ItemBuilder hideAttributes() {
        return changeItemMeta(it -> {
            for (ItemFlag value : ItemFlag.values()) {
                it.addItemFlags(value);
            }
        });
    }
}

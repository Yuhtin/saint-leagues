package com.yuhtin.quotes.saint.leagues.module;

import com.google.common.collect.Lists;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.AllArgsConstructor;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
public class RankingModule implements TerminableModule {

    public static final List<UUID> STANDS = Lists.newLinkedList();
    public static final List<String> HOLOGRAMS = Lists.newLinkedList();

    private static final Material[] HEADS = new Material[]{
            Material.DIAMOND_BLOCK, Material.GOLD_BLOCK,
            Material.IRON_BLOCK, Material.STONE,
            Material.OAK_WOOD
    };

    private static final Material[] CHESTPLATE = new Material[]{
            Material.DIAMOND_CHESTPLATE, Material.GOLDEN_CHESTPLATE,
            Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE,
            Material.LEATHER_CHESTPLATE
    };

    private static final Material[] LEGGINGS = new Material[]{
            Material.DIAMOND_LEGGINGS, Material.GOLDEN_LEGGINGS,
            Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS,
            Material.LEATHER_LEGGINGS
    };

    private static final Material[] BOOTS = new Material[]{
            Material.DIAMOND_BOOTS, Material.GOLDEN_BOOTS,
            Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS,
            Material.LEATHER_BOOTS
    };


    private final LeaguesPlugin plugin;

    @Override
    public void setup(@Nonnull TerminableConsumer consumer) {
        Schedulers.sync().runRepeating(this::refreshRanking, 20L, 2 * 60 * 20L).bindWith(consumer);

        Events.subscribe(EntityDamageByEntityEvent.class)
                .filter(event -> event.getEntity().hasMetadata("saintleagues"))
                .handler(event -> event.setCancelled(true))
                .bindWith(consumer);
    }

    public void refreshRanking() {
        long start = System.currentTimeMillis();
        plugin.getLogger().info("Atualizando ranking...");

        LeagueClanCache.getInstance().refresh().thenRunSync(() -> {
            long rankingUpdates = System.currentTimeMillis();
            plugin.getLogger().info("Ranking atualizado em " + (rankingUpdates - start) + "ms");

            plugin.getLogger().info("Atualizando stands e hologramas...");
            clearStands();

            LeaguesPlugin instance = LeaguesPlugin.getInstance();
            ConfigurationSection section = instance.getConfig().getConfigurationSection("ranking.position");
            if (section == null) {
                instance.getLogger().warning("Ranking position section is null");
                return;
            }

            for (String key : section.getKeys(false)) {
                int position = Integer.parseInt(key);

                LeagueClan clan = LeagueClanCache.getInstance().getByPosition(position - 1);
                if (clan == null) break;

                String locationString = section.getString(key);
                if (locationString == null) continue;

                String[] split = locationString.split(",");
                Location location = new Location(
                        Bukkit.getWorld(split[0]),
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]),
                        Double.parseDouble(split[3]),
                        Float.parseFloat(split[4]),
                        Float.parseFloat(split[5])
                );

                if (location.getWorld() == null) continue;

                updateRanking(clan, location, position);
            }

            plugin.getLogger().info("Stands e hologramas atualizados em " + (System.currentTimeMillis() - rankingUpdates) + "ms");
        });
    }

    public void clearStands() {
        HOLOGRAMS.forEach(DHAPI::removeHologram);

        for (Hologram hologram : DecentHologramsAPI.get().getHologramManager().getHolograms()) {
            if (hologram.getName().startsWith("saintleagues")) {
                DHAPI.removeHologram(hologram.getName());
            }
        }

        STANDS.forEach(stand -> {
            Entity entity = Bukkit.getEntity(stand);
            if (entity == null) return;

            entity.remove();
        });

        STANDS.clear();
    }

    private void updateRanking(LeagueClan clan, Location location, int position) {
        createHologram(location.clone().add(0, 2.15, 0), Arrays.asList(
                "&a" + position + "º Lugar",
                "&e" + clan.getTag(),
                "&e" + clan.getPoints() + " pontos"
        ));

        createStand(location, position);
    }

    private void createStand(Location location, int position) {
        ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);

        stand.setVisible(false);
        stand.setMetadata("saintleagues", new FixedMetadataValue(plugin, true));
        stand.setSmall(true);
        stand.setCustomNameVisible(false);
        stand.setGravity(false);
        stand.setArms(true);

        EntityEquipment equipment = stand.getEquipment();
        if (equipment == null) return;

        Material head = HEADS[Math.min(HEADS.length, position) - 1];
        equipment.setHelmet(new ItemStack(head));

        Material chestplate = CHESTPLATE[Math.min(CHESTPLATE.length, position) - 1];
        equipment.setChestplate(new ItemStack(chestplate));

        Material leggings = LEGGINGS[Math.min(LEGGINGS.length, position) - 1];
        equipment.setLeggings(new ItemStack(leggings));

        Material boots = BOOTS[Math.min(BOOTS.length, position) - 1];
        equipment.setBoots(new ItemStack(boots));

        stand.setVisible(true);

        STANDS.add(stand.getUniqueId());
    }

    public void createHologram(Location location, List<String> lines) {
        HOLOGRAMS.add(DHAPI.createHologram("saintleagues-" + new Random().nextInt(1000), location, false, lines).getName());
    }
}

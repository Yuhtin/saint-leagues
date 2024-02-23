package com.yuhtin.quotes.saint.leagues.module;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.util.DiscordWebhook;
import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.awt.*;
import java.io.IOException;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor(staticName = "of")
public class DiscordAlertSender {

    private final String tag;
    private final int points;
    private final String motive;

    public void send() {
        LeaguesPlugin instance = LeaguesPlugin.getInstance();
        String link = instance.getConfig().getString("discord-webhook-link", "");
        if (link.isEmpty()) {
            instance.getLogger().warning("Discord webhook link is empty, please set it on config.yml");
            return;
        }

        RepositoryManager manager = RepositoryManager.getInstance();

        int mensalRanking = manager.getPositionByClan(IntervalTime.MENSAL, tag);
        int trimestralRanking = manager.getPositionByClan(IntervalTime.TRIMESTRAL, tag);

        int mensalPoints = manager.getPointsByTag(IntervalTime.MENSAL, tag);
        int trimestralPoints = manager.getPointsByTag(IntervalTime.TRIMESTRAL, tag);

        DiscordWebhook discordWebhook = new DiscordWebhook(link);
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();

        ConfigurationSection section = instance.getConfig().getConfigurationSection("webhook");

        embedObject.setTitle(section.getString("title", "Saint Leagues"));

        embedObject.setDescription(section.getString("description", "Aconteceu algo importante no servidor!")
                .replace("%clan%", tag)
                .replace("%pontos%", String.valueOf(points))
                .replace("%motivo%", motive));

        embedObject.setFooter(section.getString("footer", "Saint Leagues - " + tag), "");

        embedObject.setColor(Color.getColor(section.getString("color", "#ffffff")));

        section.getConfigurationSection("fields").getKeys(false).forEach(key -> {
            String value = section.getString("fields." + key);
            if (value == null) return;

            embedObject.addField(key, value
                    .replace("%clan%", tag)
                    .replace("%pontos%", String.valueOf(points))
                    .replace("%motivo%", motive)
                    .replace("%mensal-position%", String.valueOf(mensalRanking))
                    .replace("%trimestral-position%", String.valueOf(trimestralRanking))
                    .replace("%mensal-points%", String.valueOf(mensalPoints))
                    .replace("%trimestral-points%", String.valueOf(trimestralPoints)), true);
        });

        discordWebhook.addEmbed(embedObject);

        try {
            discordWebhook.execute();
        } catch (IOException exception) {
            exception.printStackTrace();
            instance.getLogger().severe("[Webhook] An error occurred while sending discord webhook message.");
        }

    }

}

package com.yuhtin.quotes.saint.leagues.module;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.util.DiscordWebhook;
import lombok.AllArgsConstructor;

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

        embedObject.setTitle("SAINT LIGA - " + tag + " <a:1Espada:1149729957434634403>");
        embedObject.setDescription("O clã " + tag + " ganhou " + points + " pontos por " + motive.toLowerCase() + ".");
        embedObject.setFooter("Que a vitória seja dos mais fortes! Rede Saint", "");
        embedObject.setColor(Color.RED);

        embedObject.addField("CLAN", tag, true);
        embedObject.addField("PONTOS", String.valueOf(points), true);
        embedObject.addField("RAZÃO", motive, true);
        embedObject.addField("PONTUAÇÃO MENSAL", mensalPoints + " pontos - #" + mensalRanking, true);
        embedObject.addField("PONTUAÇÃO TRIMESTRAL", trimestralPoints + " pontos - #" + trimestralRanking, true);

        discordWebhook.addEmbed(embedObject);

        try {
            discordWebhook.execute();
        } catch (IOException exception) {
            exception.printStackTrace();
            instance.getLogger().severe("[Webhook] An error occurred while sending discord webhook message.");
        }

    }

}

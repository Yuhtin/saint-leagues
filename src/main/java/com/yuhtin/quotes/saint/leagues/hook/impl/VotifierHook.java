package com.yuhtin.quotes.saint.leagues.hook.impl;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.cache.LeagueClanCache;
import com.yuhtin.quotes.saint.leagues.hook.LeagueEventHook;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VotifierHook extends LeagueEventHook {

    private final LeaguesPlugin instance;

    @Override
    public String pluginName() {
        return "Votifier";
    }

    @Override
    public void setup(@NotNull TerminableConsumer consumer) {

        Events.subscribe(VotifierEvent.class)
                .filter(event -> !event.getVote().getServiceName().isEmpty())
                .filter(event -> event.getVote().getUsername().trim().length() > 0)
                .handler(event -> {
                    Vote vote = event.getVote();
                    String username = vote.getUsername().trim();

                    String clanTag = instance.getSimpleClansAccessor().getClanTag(username);
                    if (clanTag == null) return;

                    String path = "reward-per-event.Vote";

                    String name = instance.getConfig().getString(path + ".name");
                    int points = instance.getConfig().getInt(path + ".points");

                    LeagueClanCache.getInstance().addPoints(clanTag, points, name);
                }).bindWith(consumer);

    }

}

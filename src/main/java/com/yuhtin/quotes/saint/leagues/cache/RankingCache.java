package com.yuhtin.quotes.saint.leagues.cache;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.repository.repository.ClanRepository;

import java.util.*;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class RankingCache {

    public static final RankingCache INSTANCE = new RankingCache();

    private final HashMap<String, LeagueClan> ranking = new HashMap<>();

    public static RankingCache getInstance() {
        return INSTANCE;
    }

    public void refresh() {
        ClanRepository clanRepository = LeaguesPlugin.getInstance().getClanRepository();


        Set<LeagueClan> leagueClans = clanRepository.orderByPoints();
        ranking.clear();

        for (LeagueClan clan : leagueClans) {
            clan.setRankPosition(ranking.size() + 1);
            ranking.put(clan.getTag(), clan);
        }
    }

    public LeagueClan getByTag(String tag) {
        return ranking.get(tag);
    }

    public LeagueClan getByPosition(int position) {
        return (LeagueClan) ranking.values().toArray()[position];
    }

    public Collection<LeagueClan> getRanking() {
        return ranking.values();
    }

}

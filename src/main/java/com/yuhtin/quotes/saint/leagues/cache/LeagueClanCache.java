package com.yuhtin.quotes.saint.leagues.cache;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.repository.repository.ClanRepository;
import me.lucko.helper.Schedulers;
import me.lucko.helper.promise.Promise;

import java.util.*;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueClanCache {

    public static final LeagueClanCache INSTANCE = new LeagueClanCache();

    private final HashMap<String, Integer> clanPoints = new HashMap<>();
    private final List<String> ranking = new ArrayList<>();

    public static LeagueClanCache getInstance() {
        return INSTANCE;
    }

    public Promise<Void> refresh() {
        return Schedulers.sync().run(() -> {
            ClanRepository clanRepository = LeaguesPlugin.getInstance().getClanRepository();

            Set<LeagueClan> leagueClans = clanRepository.orderByPoints();
            ranking.clear();

            for (LeagueClan clan : leagueClans) {
                ranking.add(clan.getTag());
            }
        });
    }

    public void addPoints(String tag, int points) {
        int currentPoints = getPointsByTag(tag) + points;
        setClanPoints(tag, currentPoints);
    }

    public void setClanPoints(String tag, int points) {
        clanPoints.put(tag, points);
        LeaguesPlugin.getInstance().getClanRepository().insert(tag, points);
    }

    public int getPointsByTag(String tag) {
        if (tag == null) return -1;
        if (clanPoints.containsKey(tag)) return clanPoints.get(tag);

        ClanRepository clanRepository = LeaguesPlugin.getInstance().getClanRepository();
        LeagueClan clan = clanRepository.findByTag(tag);
        if (clan == null) {
            clan = new LeagueClan(tag, 0);
            clanRepository.insert(tag, 0);
        }

        clanPoints.put(tag, clan.getPoints());
        return clan.getPoints();
    }

    public int getPositionByClan(String tag) {
        if (!ranking.contains(tag)) {
            ranking.add(tag);
        }

        return ranking.indexOf(tag) + 1;
    }

    public LeagueClan getByPosition(int position) {
        if (ranking.size() <= position) return null;

        String tag = ranking.get(position);
        return new LeagueClan(tag, getPointsByTag(tag));
    }

    public Collection<String> getRanking() {
        return ranking;
    }

}

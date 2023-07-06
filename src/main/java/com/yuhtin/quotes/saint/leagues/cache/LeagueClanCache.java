package com.yuhtin.quotes.saint.leagues.cache;

import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.module.DiscordAlertSender;
import com.yuhtin.quotes.saint.leagues.repository.repository.TimedClanRepository;
import lombok.Getter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.promise.Promise;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueClanCache {

    public static final LeagueClanCache INSTANCE = new LeagueClanCache();

    @Getter
    private final HashMap<IntervalTime, TimedClanRepository> repositories = new HashMap<>();

    public static LeagueClanCache getInstance() {
        return INSTANCE;
    }

    public Promise<Void> refresh() {
        return Schedulers.sync().run(() -> {
            for (TimedClanRepository repository : repositories.values()) {
                Set<LeagueClan> leagueClans = repository.orderByPoints();
                repository.getRanking().clear();

                for (LeagueClan clan : leagueClans) {
                    repository.getRanking().add(clan.getTag());
                }
            }
        });
    }

    public int getPositionByClan(IntervalTime interval, String tag) {
        return getRepository(interval).getPositionByClan(tag);
    }

    public int getPointsByTag(IntervalTime interval, String tag) {
        return getRepository(interval).getPointsByTag(tag);
    }

    public void addPoints(IntervalTime interval, String tag, int points) {
        TimedClanRepository repository = getRepository(interval);
        int currentPoints = repository.getPointsByTag(tag) + points;

        repository.getCache().put(tag, currentPoints);
        repository.insert(tag, currentPoints);
    }

    public void addPoints(String tag, int points, String motive) {
        for (TimedClanRepository repository : repositories.values()) {
            int currentPoints = repository.getPointsByTag(tag) + points;

            repository.getCache().put(tag, currentPoints);
            repository.insert(tag, currentPoints);
        }

        DiscordAlertSender.of(tag, points, motive).send();
    }

    public LeagueClan getByPosition(IntervalTime interval, int position) {
        return getRepository(interval).getByPosition(position);
    }

    public Collection<String> getRanking(IntervalTime intervalTime) {
        return repositories.get(intervalTime).getRanking();
    }

    public TimedClanRepository getRepository(IntervalTime intervalTime) {
        return repositories.get(intervalTime);
    }

    public void register(IntervalTime time, TimedClanRepository repository) {
        repositories.put(time, repository);
    }
}

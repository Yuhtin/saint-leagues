package com.yuhtin.quotes.saint.leagues.repository.repository;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.repository.adapters.LeagueClanAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@RequiredArgsConstructor
@Getter
public class TimedClanRepository {

    private final SQLExecutor sqlExecutor;
    private final IntervalTime intervalTime;

    @Setter
    private long initialTime;

    private final List<String> ranking = new ArrayList<>();

    public LeagueClan getByPosition(int position) {
        if (ranking.size() <= position) return null;

        String tag = ranking.get(position);
        return new LeagueClan(tag, getPointsByTag(tag));
    }

    public int getPointsByTag(String tag) {
        if (tag == null) return -1;

        LeagueClan clan = this.findByTag(tag);
        if (clan == null) {
            clan = new LeagueClan(tag, 0);
            this.insert(tag, 0);
        }

        return clan.getPoints();
    }

    public int getPositionByClan(String tag) {
        if (!ranking.contains(tag)) {
            ranking.add(tag);
        }

        return ranking.indexOf(tag) + 1;
    }

    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + getTable() + "(" +
                "clanTag CHAR(10) NOT NULL PRIMARY KEY," +
                "points INT NOT NULL" +
                ");"
        );
    }

    public void recreateTable() {
        sqlExecutor.updateQuery("DROP TABLE IF EXISTS " + getTable());
        createTable();
    }

    public Set<LeagueClan> orderByPoints() {
        return orderByPoints(-1);
    }

    public Set<LeagueClan> orderByPoints(int limit) {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + getTable() + " ORDER BY points DESC" + (limit > 0 ? " LIMIT " + limit : ""),
                statement -> {},
                LeagueClanAdapter.class
        );
    }

    public LeagueClan findByTag(String tag) {
        return sqlExecutor.resultOneQuery(
                "SELECT * FROM " + getTable() + " WHERE clanTag = '" + tag + "'",
                statement -> {
                },
                LeagueClanAdapter.class
        );
    }

    public void insert(String clanTag, int points) {
        this.sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?,?)", getTable()),
                statement -> {
                    statement.set(1, clanTag);
                    statement.set(2, points);
                }
        );
    }

    public long getFinalTime() {
        return LeaguesPlugin.getInstance().getConfig().getLong("reset-time." + this.intervalTime.name());
    }

    public String getTable() {
        return "leagues_clan_data_" + intervalTime.name().toLowerCase();
    }

}

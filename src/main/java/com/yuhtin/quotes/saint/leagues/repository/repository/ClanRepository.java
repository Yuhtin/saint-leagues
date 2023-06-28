package com.yuhtin.quotes.saint.leagues.repository.repository;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.repository.adapters.LeagueClanAdapter;
import com.yuhtin.quotes.saint.leagues.repository.adapters.LeagueEventAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@RequiredArgsConstructor
public final class ClanRepository {

    private static final String TABLE = "leagues_clan_data";

    @Getter
    private final SQLExecutor sqlExecutor;

    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                "clanTag CHAR(3) NOT NULL PRIMARY KEY," +
                "points INT NOT NULL" +
                ");"
        );
    }

    public Set<LeagueClan> orderByPoints() {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE + " ORDER BY points DESC",
                statement -> {},
                LeagueClanAdapter.class
        );
    }

    public LeagueClan findByTag(String tag) {
        return sqlExecutor.resultOneQuery(
                "SELECT * FROM " + TABLE + " WHERE clanTag = '" + tag + "'",
                statement -> {},
                LeagueClanAdapter.class
        );
    }

    public void insert(LeagueClan leagueClan) {
        this.sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?,?)", TABLE),
                statement -> {
                    statement.set(1, leagueClan.getTag());
                    statement.set(2, leagueClan.getPoints());
                }
        );
    }

}


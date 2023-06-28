package com.yuhtin.quotes.saint.leagues.repository.repository;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.repository.adapters.LeagueEventAdapter;
import com.yuhtin.quotes.saint.leagues.repository.adapters.EmptyAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@RequiredArgsConstructor
public final class EventRepository {

    private static final String TABLE = "leagues_event_data";

    @Getter
    private final SQLExecutor sqlExecutor;

    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                "id CHAR(8) NOT NULL PRIMARY KEY," +
                "name CHAR(36) NOT NULL," +
                "clanTag CHAR(3) NOT NULL," +
                "eventType CHAR(36) NOT NULL," +
                "points INT NOT NULL," +
                "timestamp BIGINT NOT NULL" +
                "playersInvolved LONGTEXT NOT NULL," +
                ");"
        );
    }

    public Set<LeagueEvent> findAll() {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE,
                statement -> {},
                LeagueEventAdapter.class
        );
    }

    public Set<LeagueEvent> groupByPlayer(String playerName) {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE + " WHERE playersInvolved LIKE '%" + playerName + "%'",
                statement -> {},
                LeagueEventAdapter.class
        );
    }

    public int countClanAppearences(String clanTag) {
        return sqlExecutor.resultManyQuery(
                "SELECT COUNT(*) FROM " + TABLE + " WHERE clanTag = '" + clanTag + "'",
                statement -> {},
                EmptyAdapter.class
        ).size();
    }

    public LeagueEvent findById(String id) {
        return sqlExecutor.resultOneQuery(
                "SELECT * FROM " + TABLE + " WHERE id = '" + id + "'",
                statement -> {},
                LeagueEventAdapter.class
        );
    }

    public void insert(LeagueEvent leagueEvent) {
        this.sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?,?,?,?,?,?,?)", TABLE),
                statement -> {
                    statement.set(1, leagueEvent.getId());
                    statement.set(2, leagueEvent.getName());
                    statement.set(3, leagueEvent.getClanTag());
                    statement.set(4, leagueEvent.getEventType().name());
                    statement.set(5, leagueEvent.getPoints());
                    statement.set(6, leagueEvent.getTimestamp());
                    statement.set(7, leagueEvent.getPlayersInvolved());
                }
        );
    }

}

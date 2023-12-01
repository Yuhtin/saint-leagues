package com.yuhtin.quotes.saint.leagues.repository.repository;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.model.LeagueEventType;
import com.yuhtin.quotes.saint.leagues.repository.adapters.LeagueEventAdapter;
import com.yuhtin.quotes.saint.leagues.repository.adapters.EmptyAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
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
                "winner_clan CHAR(10) NOT NULL," +
                "event_type CHAR(36) NOT NULL," +
                "points INT NOT NULL," +
                "timestamp BIGINT NOT NULL," +
                "players_involved LONGTEXT NOT NULL" +
                ");"
        );
    }

    public void recreateTable() {
        sqlExecutor.updateQuery("DROP TABLE IF EXISTS " + TABLE);
        createTable();
    }

    public Set<LeagueEvent> findAll(TimedClanRepository timedRepository) {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE
                        + " WHERE timestamp >= " + timedRepository.getInitialTime()
                        + " AND timestamp <= " + timedRepository.getFinalTime(),
                statement -> {},
                LeagueEventAdapter.class
        );
    }

    public Set<LeagueEvent> groupByType(LeagueEventType type) {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE + " WHERE event_type = '" + type.name() + "'",
                statement -> {},
                LeagueEventAdapter.class
        );
    }

    public Set<LeagueEvent> groupByPlayer(String playerName, TimedClanRepository timedRepository) {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE
                        + " WHERE players_involved LIKE '%" + playerName + "%'"
                        + " AND timestamp >= " + timedRepository.getInitialTime()
                        + " AND timestamp <= " + timedRepository.getFinalTime(),
                statement -> {},
                LeagueEventAdapter.class
        );
    }

    public int countClanAppearences(String clanTag) {
        return sqlExecutor.resultManyQuery(
                "SELECT COUNT(*) FROM " + TABLE
                        + " WHERE winner_clan = '" + clanTag + "'",
                statement -> {},
                EmptyAdapter.class
        ).size();
    }

    public Set<LeagueEvent> findByClanInInterval(String clanTag, long start, long end) {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE
                        + " WHERE winner_clan = '" + clanTag
                        + "' AND timestamp >= " + start
                        + " AND timestamp <= " + end,
                statement -> {},
                LeagueEventAdapter.class
        );
    }

    public void insert(LeagueEvent leagueEvent) {
        this.sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?,?,?,?,?,?,?)", TABLE),
                statement -> {
                    List<String> playersInvolved = leagueEvent.getPlayersInvolved();

                    statement.set(1, leagueEvent.getId());
                    statement.set(2, leagueEvent.getName());
                    statement.set(3, leagueEvent.getClanTag());
                    statement.set(4, leagueEvent.getLeagueEventType().name());
                    statement.set(5, leagueEvent.getPoints());
                    statement.set(6, leagueEvent.getTimestamp());
                    statement.set(7, String.join(",", playersInvolved));
                }
        );
    }

}

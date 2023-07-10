package com.yuhtin.quotes.saint.leagues.repository.adapters;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;
import com.yuhtin.quotes.saint.leagues.model.LeagueEventType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueEventAdapter implements SQLResultAdapter<LeagueEvent> {

    @Override
    public LeagueEvent adaptResult(SimpleResultSet resultSet) {
        String playersInvolved = resultSet.get("players_involved");

        return LeagueEvent.builder()
                .id(resultSet.get("id"))
                .name(resultSet.get("name"))
                .clanTag(resultSet.get("winner_clan"))
                .leagueEventType(LeagueEventType.valueOf(resultSet.get("event_type")))
                .points(resultSet.get("points"))
                .timestamp(resultSet.get("timestamp"))
                .playersInvolved(playersInvolved == null ? new ArrayList<>() : List.of(playersInvolved.split(",")))
                .build();
    }
}

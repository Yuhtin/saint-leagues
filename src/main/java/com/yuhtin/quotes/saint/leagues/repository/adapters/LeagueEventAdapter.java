package com.yuhtin.quotes.saint.leagues.repository.adapters;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.quotes.saint.leagues.model.LeagueEvent;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueEventAdapter implements SQLResultAdapter<LeagueEvent> {

    @Override
    public LeagueEvent adaptResult(SimpleResultSet resultSet) {
        return LeagueEvent.builder()
                .id(resultSet.get("id"))
                .name(resultSet.get("name"))
                .clanTag(resultSet.get("clanTag"))
                .eventType(resultSet.get("eventType"))
                .points(resultSet.get("points"))
                .timestamp(resultSet.get("timestamp"))
                .playersInvolved(resultSet.get("playersInvolved"))
                .build();
    }
}

package com.yuhtin.quotes.saint.leagues.model;

import com.yuhtin.quotes.saint.leagues.util.DateFormatUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */

@Data
@Builder
public class LeagueEvent {

    @Builder.Default
    private final String id = UUID.randomUUID().toString().split("-")[0];

    private final String name;

    private final String clanTag;
    private final LeagueEventType leagueEventType;
    private final List<String> playersInvolved;
    private final int points;

    @Builder.Default
    private final long timestamp = System.currentTimeMillis();

    public String getFormattedDate() {
        return DateFormatUtil.of(timestamp);
    }

}


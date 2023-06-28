package com.yuhtin.quotes.saint.leagues.model;

import com.yuhtin.quotes.saint.leagues.util.DateFormatUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */

@Builder
@Data
public class LeagueEvent {

    @Builder.Default
    private final String id = generateRandomID();

    private final String name;

    private final String clanTag;
    private final EventType eventType;
    private final List<String> playersInvolved;
    private final int points;

    private final long timestamp;

    protected String generateRandomID() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    public String getFormattedDate() {
        return DateFormatUtil.of(timestamp);
    }
}

package com.yuhtin.quotes.saint.leagues.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
@Getter
public enum IntervalTime {

    MENSAL(2592000000L),
    TRIMESTRAL(7776000000L),
    ;

    private final long millis;

    public String fancyName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}

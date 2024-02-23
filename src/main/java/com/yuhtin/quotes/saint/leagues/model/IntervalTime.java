package com.yuhtin.quotes.saint.leagues.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
@Getter

public enum IntervalTime {

    MENSAL(1),
    TRIMESTRAL(3),
    ;

    private final int months;

    public long getAddedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, months);

        return calendar.getTimeInMillis();
    }

    public String fancyName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}
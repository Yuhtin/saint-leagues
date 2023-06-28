package com.yuhtin.quotes.saint.leagues.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class LeagueClan {

    private final String tag;
    private int points;
    private int rankPosition;

}

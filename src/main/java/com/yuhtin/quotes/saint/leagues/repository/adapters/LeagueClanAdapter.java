package com.yuhtin.quotes.saint.leagues.repository.adapters;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.quotes.saint.leagues.model.LeagueClan;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class LeagueClanAdapter implements SQLResultAdapter<LeagueClan> {

    @Override
    public LeagueClan adaptResult(SimpleResultSet resultSet) {
        return new LeagueClan(
                resultSet.get("clanTag"),
                Integer.parseInt(resultSet.get("points"))
        );
    }

}

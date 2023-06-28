package com.yuhtin.quotes.saint.leagues.repository.adapters;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.quotes.saint.leagues.util.Null;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class EmptyAdapter implements SQLResultAdapter<Null> {

    @Override
    public Null adaptResult(SimpleResultSet resultSet) {
        return new Null();
    }
}

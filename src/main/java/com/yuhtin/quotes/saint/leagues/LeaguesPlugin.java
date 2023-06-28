package com.yuhtin.quotes.saint.leagues;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.saint.leagues.repository.SQLProvider;
import com.yuhtin.quotes.saint.leagues.repository.repository.ClanRepository;
import com.yuhtin.quotes.saint.leagues.repository.repository.EventRepository;
import lombok.Getter;
import me.lucko.helper.plugin.ExtendedJavaPlugin;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Getter
public class LeaguesPlugin extends ExtendedJavaPlugin {

    private ClanRepository clanRepository;
    private EventRepository eventRepository;

    @Override
    protected void enable() {
        initDatabase();

        getLogger().info("Plugin enabled!");
    }

    private void initDatabase() {
        SQLConnector sqlConnector = SQLProvider.of(this).setup(null);
        SQLExecutor sqlExecutor = new SQLExecutor(sqlConnector);

        clanRepository = new ClanRepository(sqlExecutor);
        eventRepository = new EventRepository(sqlExecutor);

        getLogger().info("Database initialized!");
    }

    public static LeaguesPlugin getInstance() {
        return getPlugin(LeaguesPlugin.class);
    }

}

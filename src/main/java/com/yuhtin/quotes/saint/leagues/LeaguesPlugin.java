package com.yuhtin.quotes.saint.leagues;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.saint.leagues.placeholder.ClansPlaceholder;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.cache.ViewCache;
import com.yuhtin.quotes.saint.leagues.command.LeagueClanCommand;
import com.yuhtin.quotes.saint.leagues.hook.HookModule;
import com.yuhtin.quotes.saint.leagues.manager.SimpleClansAccessor;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.module.AutoRewardModule;
import com.yuhtin.quotes.saint.leagues.module.RankingModule;
import com.yuhtin.quotes.saint.leagues.repository.SQLProvider;
import com.yuhtin.quotes.saint.leagues.repository.repository.EventRepository;
import com.yuhtin.quotes.saint.leagues.repository.repository.TimedClanRepository;
import lombok.Getter;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.Bukkit;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Getter
public class LeaguesPlugin extends ExtendedJavaPlugin {

    private final SimpleClansAccessor simpleClansAccessor = new SimpleClansAccessor(Bukkit.getPluginManager());
    private final ViewCache viewCache = new ViewCache();

    private RankingModule rankingModule;
    private EventRepository eventRepository;

    @Override
    protected void enable() {
        saveDefaultConfig();

        if (!simpleClansAccessor.isValid()) {
            getLogger().severe("SimpleClans not found! Disabling plugin...");
            return;
        }

        InventoryManager.enable(this);

        initDatabase();

        bindModule(new AutoRewardModule(this));
        bindModule(new LeagueClanCommand(this));
        bindModule(new HookModule(this));

        this.rankingModule = new RankingModule(this, RepositoryManager.getInstance());
        bindModule(rankingModule);

        new ClansPlaceholder(this, RepositoryManager.getInstance()).register();

        getLogger().info("Plugin enabled!");
    }

    @Override
    protected void disable() {
        rankingModule.clearStands();
    }

    private void initDatabase() {
        SQLConnector sqlConnector = SQLProvider.of(this).setup(null);
        SQLExecutor sqlExecutor = new SQLExecutor(sqlConnector);

        eventRepository = new EventRepository(sqlExecutor);
        eventRepository.createTable();

        initCache(sqlExecutor);

        getLogger().info("Database initialized!");
    }

    private void initCache(SQLExecutor sqlExecutor) {
        for (IntervalTime time : IntervalTime.values()) {
            long initialTime = getConfig().getLong("initial-time." + time.name().toUpperCase(), -1);
            if (initialTime == -1) {
                initialTime = System.currentTimeMillis();

                getConfig().set("initial-time." + time.name().toUpperCase(), initialTime);
                saveConfig();
            }

            TimedClanRepository repository = new TimedClanRepository(sqlExecutor, time);
            repository.createTable();
            repository.setInitialTime(initialTime);

            RepositoryManager.getInstance().register(time, repository);
        }
    }

    public static LeaguesPlugin getInstance() {
        return getPlugin(LeaguesPlugin.class);
    }

}

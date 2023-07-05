package com.yuhtin.quotes.saint.leagues;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.saint.leagues.cache.ViewCache;
import com.yuhtin.quotes.saint.leagues.command.LeagueClanCommand;
import com.yuhtin.quotes.saint.leagues.hook.HookModule;
import com.yuhtin.quotes.saint.leagues.manager.SimpleClansAccessor;
import com.yuhtin.quotes.saint.leagues.module.AutoRewardModule;
import com.yuhtin.quotes.saint.leagues.module.RankingModule;
import com.yuhtin.quotes.saint.leagues.repository.SQLProvider;
import com.yuhtin.quotes.saint.leagues.repository.repository.ClanRepository;
import com.yuhtin.quotes.saint.leagues.repository.repository.EventRepository;
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
    private ClanRepository clanRepository;
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

        this.rankingModule = new RankingModule(this);
        bindModule(rankingModule);

        getLogger().info("Plugin enabled!");
    }

    @Override
    protected void disable() {
        rankingModule.clearStands();
    }

    private void initDatabase() {
        SQLConnector sqlConnector = SQLProvider.of(this).setup(null);
        SQLExecutor sqlExecutor = new SQLExecutor(sqlConnector);

        clanRepository = new ClanRepository(sqlExecutor);
        eventRepository = new EventRepository(sqlExecutor);

        clanRepository.createTable();
        eventRepository.createTable();

        getLogger().info("Database initialized!");
    }

    public static LeaguesPlugin getInstance() {
        return getPlugin(LeaguesPlugin.class);
    }

}

package com.yuhtin.quotes.saint.leagues;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.saint.leagues.cache.ViewCache;
import com.yuhtin.quotes.saint.leagues.clans.impl.UltimateClansAcessor;
import com.yuhtin.quotes.saint.leagues.command.LeagueClanCommand;
import com.yuhtin.quotes.saint.leagues.hook.HookModule;
import com.yuhtin.quotes.saint.leagues.clans.ClanAcessor;
import com.yuhtin.quotes.saint.leagues.clans.impl.SimpleClansAccessor;
import com.yuhtin.quotes.saint.leagues.model.IntervalTime;
import com.yuhtin.quotes.saint.leagues.module.AutoRewardModule;
import com.yuhtin.quotes.saint.leagues.module.RankingModule;
import com.yuhtin.quotes.saint.leagues.placeholder.ClansPlaceholder;
import com.yuhtin.quotes.saint.leagues.repository.RepositoryManager;
import com.yuhtin.quotes.saint.leagues.repository.SQLProvider;
import com.yuhtin.quotes.saint.leagues.repository.repository.EventRepository;
import com.yuhtin.quotes.saint.leagues.repository.repository.TimedClanRepository;
import lombok.Getter;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.Bukkit;

import java.util.Calendar;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Getter
public class LeaguesPlugin extends ExtendedJavaPlugin {

    private ClanAcessor clanAcessor;

    private ViewCache viewCache;
    private RankingModule rankingModule;
    private EventRepository eventRepository;

    @Override
    protected void enable() {
        InventoryManager.enable(this);
        saveDefaultConfig();

        this.clanAcessor = lookupClanPlugin();
        if (clanAcessor == null || !clanAcessor.isValid()) {
            getLogger().severe("Clan plugin not found! Disabling plugin...");
            getLogger().severe("Please use SimpleClans or UltimateClans");
            return;
        }

        initDatabase();

        bindModule(new AutoRewardModule(this));
        bindModule(new LeagueClanCommand(this));
        bindModule(new HookModule(this));

        this.rankingModule = new RankingModule(this, RepositoryManager.getInstance());
        bindModule(rankingModule);

        this.viewCache = new ViewCache(this);

        new ClansPlaceholder(this, RepositoryManager.getInstance()).register();

        getLogger().info("Plugin enabled!");
    }

    private ClanAcessor lookupClanPlugin() {
        if (Bukkit.getPluginManager().getPlugin("SimpleClans") != null) return new SimpleClansAccessor(Bukkit.getPluginManager());
        if (Bukkit.getPluginManager().getPlugin("UltimateClans") != null) return new UltimateClansAcessor(Bukkit.getPluginManager());

        return null;
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
                getConfig().set("initial-time." + time.name().toUpperCase(), System.currentTimeMillis());
                saveConfig();

                initialTime = System.currentTimeMillis();
            }

            long resetTime = getConfig().getLong("reset-time." + time.name().toUpperCase(), -1);
            if (resetTime == -1) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(initialTime);
                calendar.add(Calendar.MONTH, time.getMonths());

                getConfig().set("reset-time." + time.name().toUpperCase(), calendar.getTimeInMillis());
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

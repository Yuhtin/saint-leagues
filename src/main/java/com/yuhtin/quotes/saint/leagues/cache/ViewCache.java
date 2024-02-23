package com.yuhtin.quotes.saint.leagues.cache;

import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.view.HistoricView;
import com.yuhtin.quotes.saint.leagues.view.LeagueView;
import com.yuhtin.quotes.saint.leagues.view.RankingView;
import lombok.Getter;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Getter
public class ViewCache {

    private final LeaguesPlugin plugin;
    private final HistoricView historicView;
    private final LeagueView leagueView;
    private final RankingView rankingView;

    public ViewCache(LeaguesPlugin plugin) {
        this.plugin = plugin;
        this.leagueView = new LeagueView(this).init();
        this.historicView = new HistoricView(this).init();
        this.rankingView = new RankingView(this).init();
    }

}

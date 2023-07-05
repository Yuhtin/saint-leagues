package com.yuhtin.quotes.saint.leagues.cache;

import com.yuhtin.quotes.saint.leagues.view.HistoricView;
import com.yuhtin.quotes.saint.leagues.view.LeagueView;
import com.yuhtin.quotes.saint.leagues.view.RankingView;
import lombok.Getter;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Getter
public class ViewCache {

    private final HistoricView historicView;
    private final LeagueView leagueView;
    private final RankingView rankingView;

    public ViewCache() {
        this.leagueView = new LeagueView(this).init();
        this.historicView = new HistoricView().init();
        this.rankingView = new RankingView().init();
    }

}

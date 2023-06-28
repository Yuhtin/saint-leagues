package com.yuhtin.quotes.saint.leagues.hook.impl;

import com.yuhtin.quotes.saint.leagues.hook.LeagueEventHook;
import me.lucko.helper.terminable.TerminableConsumer;

import javax.annotation.Nonnull;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class DragonSlayerHook extends LeagueEventHook {

    @Override
    public String pluginName() {
        return "DragonSlayer";
    }

    @Override
    public void setup(@Nonnull TerminableConsumer consumer) {

    }
}

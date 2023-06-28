package com.yuhtin.quotes.saint.leagues.hook;

import com.google.common.reflect.ClassPath;
import com.yuhtin.quotes.saint.leagues.LeaguesPlugin;
import com.yuhtin.quotes.saint.leagues.hook.impl.YClansHook;
import lombok.val;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public class HookModule implements TerminableModule {

    @Override
    public void setup(@Nonnull TerminableConsumer consumer) {

        LeaguesPlugin plugin = LeaguesPlugin.getInstance();
        PluginManager pluginManager = Bukkit.getPluginManager();

        // get all classes that extends LeagueEventHook
        // and register them as a listener

        ClassPath classPath;
        try {
            classPath = ClassPath.from(getClass().getClassLoader());
        } catch (IOException exception) {
            plugin.getLogger().severe("ClassPath could not be instantiated");
            return;
        }

        for (val info : classPath.getTopLevelClassesRecursive("com.yuhtin.quotes.saint.leagues.hook.impl")) {
            try {
                val name = Class.forName(info.getName());
                val object = name.newInstance();

                if (object instanceof LeagueEventHook) {
                    LeagueEventHook hook = (LeagueEventHook) object;
                    plugin.getLogger().info("[" + hook.pluginName() + "] Iniciando hook...");

                    if (!pluginManager.isPluginEnabled(hook.pluginName())) {
                        plugin.getLogger().warning("[" + hook.pluginName() + "] Dependencia " + hook.pluginName() + " não encontrada");
                        plugin.getLogger().warning("[" + hook.pluginName() + "] Desabilitando hook deste plugin!");

                        continue;
                    }

                    plugin.bindModule(hook);
                    plugin.getLogger().info("[" + hook.pluginName() + "] Hook iniciado com sucesso!");
                } else throw new InstantiationException();
            } catch (Exception exception) {
                exception.printStackTrace();
                plugin.getLogger().severe("The " + info.getName() + " class could not be instantiated");
            }
        }

    }
}

package com.warring.fishing;

import com.warring.fishing.enums.TitleLanguage;
import com.warring.fishing.events.FishCatchEvent;
import com.warring.fishing.listeners.FishingListener;
import com.warring.library.WarringPlugin;
import com.warring.library.events.EventStart;
import com.warring.library.titles.TitleAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

public class CoolFishing extends WarringPlugin {

    private static CoolFishing fishing;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fishing = this;
        new FishingListener().register();
        pullFromConfig();
    }

    @Override
    public void onDisable() {
        mapToConfig();
    }

    public void mapToConfig() {
        for (TitleLanguage lang : TitleLanguage.values()) {
            lang.mapToConfig();
        }
    }

    public void pullFromConfig() {
        if (CoolFishing.getInstance().getConfig().getConfigurationSection("MESSAGES") == null) return;
        for (String key : CoolFishing.getInstance().getConfig().getConfigurationSection("MESSAGES").getValues(false).keySet()) {
            ConfigurationSection sec = CoolFishing.getInstance().getConfig().getConfigurationSection("MESSAGES." + key);
            if (sec == null) continue;
            for (TitleLanguage lang : TitleLanguage.values()) {
                if (lang.name().equalsIgnoreCase(key)) {
                    lang.setTitle(sec.getString("TITLE"));
                    lang.setSub(sec.getString("SUBTITLE"));
                    break;
                }
            }
        }
    }

    public static CoolFishing getInstance() {
        return fishing;
    }
}

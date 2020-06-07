package com.warring.fishing.enums;

import com.warring.fishing.CoolFishing;
import org.bukkit.configuration.ConfigurationSection;

public enum TitleLanguage {

    LUCKY_REWARD("&e&lLUCKY", "Fishing Reward"),
    AVERAGE_REWARD("&a&lAVERAGE", "Fishing Reward"),
    FAIR_REWARD("&d&lFAIR", "Fishing Reward"),
    UNLUCKY_REWARD("&f&lUNLUCKY", "Fishing Reward"),
    NONE_REWARD("&4&lNONE", "Fishing Reward"),
    TRY_AGAIN("&4&lTry Again", "Better luck next time");


    private String title;
    private String sub;

    private TitleLanguage(String title, String sub) {
        this.title = title;
        this.sub = sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSub() {
        return sub;
    }

    public static TitleLanguage rewardToLang(FishingReward reward) {
        for (TitleLanguage language : values()) {
            if (language.name().startsWith(reward.name())) {
                return language;
            }
        }
        return null;
    }

    public void putToConfig() {
        CoolFishing.getInstance().getConfig().set("MESSAGES." + name() + ".TITLE", getTitle());
        CoolFishing.getInstance().getConfig().set("MESSAGES." + name() + ".SUBTITLE", getSub());
        CoolFishing.getInstance().saveConfig();
        CoolFishing.getInstance().reloadConfig();
    }

    public static void mapToConfig() {
        for (TitleLanguage lang : TitleLanguage.values()) {
            lang.putToConfig();
        }
    }

    public static void pullFromConfig() {
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

}

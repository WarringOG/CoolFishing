package com.warring.fishing.listeners;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.warring.fishing.CoolFishing;
import com.warring.fishing.enums.FishingReward;
import com.warring.fishing.enums.TitleLanguage;
import com.warring.fishing.events.FishCatchEvent;
import com.warring.fishing.events.FishNoCatchEvent;
import com.warring.library.events.EventStart;
import com.warring.library.pair.Pair;
import com.warring.library.titles.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FishingListener {

    private Map<UUID, Pair<Boolean, FishingReward>> fishingList;
    private Map<UUID, Long> uuidLongMap;

    public FishingListener() {
        fishingList = Maps.newHashMap();
        uuidLongMap = Maps.newHashMap();

    }

    public void register() {
        EventStart.register(PlayerFishEvent.class, e -> {
            if (e.getPlayer().getItemInHand().hasItemMeta()) return;
            if (fishingList.containsKey(e.getPlayer().getUniqueId()) && !e.getState().equals(PlayerFishEvent.State.FISHING)) {
                FishingReward reward = fishingList.get(e.getPlayer().getUniqueId()).getValue();
                FishCatchEvent event = new FishCatchEvent(e.getPlayer(), reward);
                Bukkit.getPluginManager().callEvent(event);
                TitleLanguage lang = TitleLanguage.rewardToLang(reward);
                TitleAPI.sendFullTitle(e.getPlayer(), 20, 20, 20, lang.getTitle(), lang.getSub());
                fishingList.get(e.getPlayer().getUniqueId()).setKey(false);
                return;
            }
            if (uuidLongMap.containsKey(e.getPlayer().getUniqueId())) {
                uuidLongMap.remove(e.getPlayer().getUniqueId());
                return;
            }
            if (e.getState() == PlayerFishEvent.State.FISHING) {
                if (new Random().nextDouble() * 100 <= 90) {
                    procFishing(e.getPlayer());
                }
                return;
            }
        }, CoolFishing.getInstance(), EventPriority.LOWEST);
    }

    public int sendTitle(Player p, int boldLine, boolean front) {
        List<Integer> integerList = Arrays.asList(0,1,2,3,5,6,7,8,10,11,12,13,15,16,17,18,20,21,22,23,25,26,27,28,30,31,32,33,35,36,37,38,40,41,42,43,45,46,47,48,50,51,52,53,55,56,57,58,60,61,62,63,65,66,67,68,70,71,72,73,75,76,77,78,80,81,82,83,85,86,87,88,90,91,92,93,95,96,97,98,100,101,102,103,105,106,107,108,110,111,112,113,115,116,117,118,120,121,122,123,125,126,127,128,130,131,132,133,135,136,137,138,140,141,142,143,145,146,147,148,150,151,152,153);
        if (integerList.contains(boldLine)) {
            if (front) {
                while (integerList.contains(boldLine)) {
                    boldLine++;
                }
            } else {
                while (integerList.contains(boldLine)) {
                    boldLine--;
                }
            }
        }
        String main = "&r&4|&r&4|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&a|&r&a|&r&2|&r&e|&r&2|&r&a|&r&a|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&c|&r&4|&r&4|&r";
        String newMain = main.substring(0, boldLine) + "&f&l" + main.substring(boldLine);
        TitleAPI.sendFullTitle(p, 0, 2, 0, newMain, "&c&lREEL IN!");
        return boldLine;
    }

    public FishingReward getReward(int index) {
        if (index == 79) {
            return FishingReward.LUCKY;
        }
        if (index >= 72 && index <= 82) {
            return FishingReward.AVERAGE;
        }
        if ((index >= 62 && index <= 92)) {
            return FishingReward.FAIR;
        }
        if ((index >= 12 && index <= 142)) {
            return FishingReward.UNLUCKY;
        }
        return FishingReward.NONE;
    }

    public FishingReward procFishing(Player p) {
        final FishingReward[] reward = new FishingReward[1];
        final int[] count = {0};
        final int[] boldLine = {2};
        final boolean[] forwards = {true};
        uuidLongMap.put(p.getUniqueId(), System.currentTimeMillis() + 4000L);
        new BukkitRunnable() {
            @Override
            public void run() {
                cancel();
                if (!uuidLongMap.containsKey(p.getUniqueId())) return;
                if (uuidLongMap.get(p.getUniqueId()) > System.currentTimeMillis()) return;
                uuidLongMap.remove(p.getUniqueId());
                fishingList.put(p.getUniqueId(), new Pair<>(true, FishingReward.NONE));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!fishingList.get(p.getUniqueId()).getKey()) {
                            cancel();
                            fishingList.remove(p.getUniqueId());
                            return;
                        }
                        if (count[0] == 4) {
                            cancel();
                            FishNoCatchEvent event = new FishNoCatchEvent(p);
                            Bukkit.getPluginManager().callEvent(event);
                            TitleAPI.sendFullTitle(p, 20, 20, 20, TitleLanguage.TRY_AGAIN.getTitle(), TitleLanguage.TRY_AGAIN.getSub());
                            fishingList.get(p.getUniqueId()).setValue(getReward(boldLine[0]));
                            fishingList.remove(p.getUniqueId());
                            return;
                        }
                        if (forwards[0]) {
                            if (boldLine[0] >= 150) {
                                forwards[0] = false;
                            }
                            boldLine[0] = sendTitle(p, boldLine[0], true);
                            fishingList.get(p.getUniqueId()).setValue(getReward(boldLine[0]));
                            boldLine[0]++;
                        } else {
                            if (boldLine[0] <= 8) {
                                forwards[0] = true;
                                count[0]++;
                            }
                            boldLine[0] = sendTitle(p, boldLine[0], false);
                            fishingList.get(p.getUniqueId()).setValue(getReward(boldLine[0]));
                            boldLine[0]--;
                        }
                    }
                }.runTaskTimer(CoolFishing.getInstance(), 1L, 1L);
            }
        }.runTaskLater(CoolFishing.getInstance(), 100L);
        return reward[0];
    }
}

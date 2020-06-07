package com.warring.fishing.events;

import com.warring.fishing.enums.FishingReward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FishCatchEvent extends Event {

    private static HandlerList list = new HandlerList();

    private FishingReward reward;
    private Player p;

    public FishCatchEvent(Player p, FishingReward reward) {
        this.p = p;
        this.reward = reward;
    }

    public FishingReward getReward() {
        return reward;
    }

    public Player getPlayer() {
        return p;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}

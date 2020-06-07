package com.warring.fishing.events;

import com.warring.fishing.enums.FishingReward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FishNoCatchEvent extends Event {

    private static HandlerList list = new HandlerList();

    private Player p;

    public FishNoCatchEvent(Player p) {
        this.p = p;
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

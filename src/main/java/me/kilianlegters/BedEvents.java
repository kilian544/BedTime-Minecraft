package me.kilianlegters;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class BedEvents implements Listener {

    private BedTime bedTime;

    public BedEvents(BedTime bedTime){
        Bukkit.getPluginManager().registerEvents(this, bedTime);
        this.bedTime = bedTime;
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event){
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK){
            return;
        }
        bedTime.getBedTimeManager(event.getPlayer().getWorld()).setInBed(event.getPlayer());
    }

    @EventHandler
    public void onbedLeave(PlayerBedLeaveEvent event){
        bedTime.getBedTimeManager(event.getPlayer().getWorld()).removeInBed(event.getPlayer());
        event.getPlayer().setStatistic(Statistic.TIME_SINCE_REST, 0);
    }

}

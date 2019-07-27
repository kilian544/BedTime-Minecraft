package me.kilianlegters;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BedTimeManager extends BukkitRunnable implements Listener {

    private long totalDayTicks = 20 * 60 * 20;
    private long minimalDayTicks = 20 * 60;
    private long calcTicks = totalDayTicks / minimalDayTicks;
    private long deltaTicks = totalDayTicks - minimalDayTicks;

    private BukkitTask bukkitTask;
    private Random random = new Random();

    private World world;
    private List<Player> inBed = new ArrayList<>();

    public BedTimeManager(World world){
        this.world = world;
    }

    @Override
    public void run() {
        doTick();
    }

    private void doTick(){
        if (Bukkit.getOnlinePlayers().size() > 0) {
            double fraction = inBed.size() / Bukkit.getOnlinePlayers().size();
            world.setFullTime(world.getFullTime() + (long) (calcTicks * fraction));
            if (inBed.size() > 0) {
                for (Player player : inBed) {
                    setSleepTicks(player, 0);
                    if (world.isThundering() || world.hasStorm()) {
                        if (random.nextInt((int)(deltaTicks / fraction * Bukkit.getOnlinePlayers().size() / 7)) < 1) {
                            world.setWeatherDuration(0);
                            world.setStorm(false);
                            world.setThundering(false);
                        }
                    }
                }
            }
        }
    }

    public void setInBed(Player player) {
        this.inBed.add(player);
    }

    public void removeInBed(Player player){
        this.inBed.remove(player);
    }

    public void setTask(BukkitTask bukkitTask){
        this.bukkitTask = bukkitTask;
    }

    public void onDisable(){
        this.bukkitTask.cancel();
    }

    private void setSleepTicks(Player player, long ticks) {
        try {
            Object nmsPlr = ReflectionUtil.invokeMethod(player, "getHandle");
            ReflectionUtil.setValue(nmsPlr, false, "sleepTicks", (int) ticks);
        } catch (Exception e) { e.printStackTrace(); }
    }

}

package me.kilianlegters;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BedTime extends JavaPlugin {

    public static HashMap<World, BedTimeManager> bedTimeManagerHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();

        new BedEvents(this);

        for (World world: Bukkit.getWorlds()){
            BedTimeManager bedTimeManager = new BedTimeManager(world);
            bedTimeManager.setTask(bedTimeManager.runTaskTimer(this, 0, 1));
            bedTimeManagerHashMap.put(world, bedTimeManager);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        for (Map.Entry<World, BedTimeManager> entry: bedTimeManagerHashMap.entrySet()){
            entry.getValue().onDisable();
        }
    }

    public BedTimeManager getBedTimeManager(World world) {
        return bedTimeManagerHashMap.get(world);
    }
}

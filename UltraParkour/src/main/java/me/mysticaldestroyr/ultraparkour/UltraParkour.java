package me.mysticaldestroyr.ultraparkour;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class UltraParkour extends JavaPlugin {

    public static HashMap<UUID, String> currentCourse = new HashMap<>();
    public static HashMap<UUID, Integer> currentCheckpoint = new HashMap<>();
    public static HashMap<UUID, Boolean> hasPassedAllCheckpoints = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        PluginData.setup();
        if (!PluginData.get().contains("courses")) {
            PluginData.get().createSection("courses");
        }
        PluginData.get().options().copyDefaults(true);
        PluginData.save();
        getCommand("parkour").setExecutor(new ParkourCommand(this));
        getCommand("checkpoint").setExecutor(new CheckpointCommand(this));
        getServer().getPluginManager().registerEvents(new PressurePlateListener(this), this);
    }
}

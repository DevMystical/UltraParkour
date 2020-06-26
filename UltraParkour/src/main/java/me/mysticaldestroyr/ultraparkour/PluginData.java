package me.mysticaldestroyr.ultraparkour;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginData {

    private static File file;
    private static FileConfiguration dataConfig;

    // Gets the data file and creates it if it does not exist
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("UltraParkour").getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("UltraParkour - Couldn't create data file.");
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(file);
    }

    // Get an instance of the data file
    public static FileConfiguration get() {
        return dataConfig;
    }

    // Save the data file
    public static void save() {
        try {
            dataConfig.save(file);
        } catch (IOException e) {
            System.out.println("UltraParkour - Couldn't save data to file.");
        }
    }

    // Reload the data file
    public static void reload() {
        dataConfig = YamlConfiguration.loadConfiguration(file);
    }
}

package me.mysticaldestroyr.ultraparkour;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PressurePlateListener implements Listener {

    UltraParkour plugin;

    public PressurePlateListener(UltraParkour plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void pressurePlateEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.PHYSICAL) {
            if (event.getClickedBlock().getType().equals(Material.GOLD_PLATE)) {
                Location plate = event.getClickedBlock().getLocation();
                int x = plate.getBlockX();
                int y = plate.getBlockY();
                int z = plate.getBlockZ();
                if (UltraParkour.currentCourse.containsKey(player.getUniqueId())) {
                    String playerCourse = UltraParkour.currentCourse.get(player.getUniqueId());
                    // Listen for an end pressure plate
                    if (PluginData.get().contains("courses." + playerCourse + ".end")) {
                        if (PluginData.get().getString("courses." + playerCourse + ".end.world").contentEquals(plate.getWorld().getName()) &&
                                PluginData.get().getInt("courses." + playerCourse + ".end.x") == x &&
                                PluginData.get().getInt("courses." + playerCourse + ".end.y") == y &&
                                PluginData.get().getInt("courses." + playerCourse + ".end.z") == z) {
                            if (UltraParkour.hasPassedAllCheckpoints.get(player.getUniqueId())) {
                                if (plugin.getConfig().getBoolean("complete-message")) {
                                    Bukkit.broadcastMessage(ChatUtil.translateString(plugin.getConfig().getString("course-complete").replace("%player%", player.getName()).replace("%course%", playerCourse), plugin));
                                } else {
                                    player.sendMessage(ChatUtil.translateString(plugin.getConfig().getString("course-complete").replace("%player%", player.getName()).replace("%course%", playerCourse), plugin));
                                }
                                UltraParkour.currentCourse.remove(player.getUniqueId());
                            } else {
                                player.sendMessage(ChatUtil.translateString("&aYou missed a checkpoint! Do /checkpoint to return to your last one.", plugin));
                            }
                        }
                    }
                    // Listen for all checkpoints once a player is in a course (also check if the course has any checkpoints)
                    if (PluginData.get().contains("courses." + playerCourse + ".checkpoints")) {
                        for (String path : PluginData.get().getConfigurationSection("courses." + playerCourse + ".checkpoints").getKeys(false)) {
                            if (PluginData.get().getString("courses." + playerCourse + ".checkpoints." + path + ".world").contentEquals(plate.getWorld().getName()) &&
                                    PluginData.get().getInt("courses." + playerCourse + ".checkpoints." + path + ".x") == x &&
                                    PluginData.get().getInt("courses." + playerCourse + ".checkpoints." + path + ".y") == y &&
                                    PluginData.get().getInt("courses." + playerCourse + ".checkpoints." + path + ".z") == z) {
                                // If the player has not hit a checkpoint yet
                                if (UltraParkour.currentCheckpoint.get(player.getUniqueId()) == 0 && Integer.parseInt(path) == 1) {
                                    UltraParkour.currentCheckpoint.put(player.getUniqueId(), Integer.valueOf(path));
                                    player.sendMessage(ChatUtil.translateString(plugin.getConfig().getString("checkpoint-reached").replace("%number%", path), plugin));
                                } else {
                                    if (UltraParkour.currentCheckpoint.get(player.getUniqueId()) + 1 == Integer.parseInt(path)) {
                                        if (Integer.valueOf(path) == PluginData.get().get("courses." + playerCourse + ".checkpoints-number")) {
                                            // If the player has reached the last checkpoint
                                            UltraParkour.hasPassedAllCheckpoints.put(player.getUniqueId(), true);
                                        }
                                        UltraParkour.currentCheckpoint.put(player.getUniqueId(), Integer.valueOf(path));
                                        player.sendMessage(ChatUtil.translateString(plugin.getConfig().getString("checkpoint-reached").replace("%number%", path), plugin));
                                    } else if (UltraParkour.currentCheckpoint.get(player.getUniqueId()) + 1 < Integer.parseInt(path)) {
                                        player.sendMessage(ChatUtil.translateString("&aYou missed a checkpoint! Do /checkpoint to return to your last one.", plugin));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Listen for a start pressure plate
                    for (String path : PluginData.get().getConfigurationSection("courses").getKeys(false)) {
                        if (PluginData.get().contains("courses." + path + ".start")) {
                            if (PluginData.get().getString("courses." + path + ".start.world").contentEquals(plate.getWorld().getName()) &&
                                    PluginData.get().getInt("courses." + path + ".start.x") == x &&
                                    PluginData.get().getInt("courses." + path + ".start.y") == y &&
                                    PluginData.get().getInt("courses." + path + ".start.z") == z) {
                                UltraParkour.currentCourse.put(player.getUniqueId(), path);
                                UltraParkour.currentCheckpoint.put(player.getUniqueId(), 0);
                                player.sendMessage(ChatUtil.translateString(plugin.getConfig().getString("course-started").replace("%course-name%", path), plugin));
                                if (!PluginData.get().contains("courses." + path + ".checkpoints-number")) {
                                    UltraParkour.hasPassedAllCheckpoints.put(player.getUniqueId(), true);
                                } else {
                                    UltraParkour.hasPassedAllCheckpoints.put(player.getUniqueId(), false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

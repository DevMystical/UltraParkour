package me.mysticaldestroyr.ultraparkour;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParkourCommand implements CommandExecutor {

    UltraParkour plugin;

    public ParkourCommand(UltraParkour plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Permissions check
            if (player.hasPermission("ultraparkour.admin")) {

                // Determine if the player has given no arguments
                if (args.length == 0) {
                    player.sendMessage(ChatUtil.translateString("&cNo argument found! Please do /parkour help", plugin));
                    return true;
                }

                // Handle reloads
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    return true;
                }

                // If the player does /parkour help, show the help menu
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.GRAY + "===============" + ChatColor.BOLD + " " + ChatColor.GREEN + "Ultra" + ChatColor.YELLOW + "Parkour" + ChatColor.RESET + " " + ChatColor.GRAY + "===============");
                    player.sendMessage(ChatColor.YELLOW + "/parkour create [name] - " + ChatColor.GRAY + "Creates a new parkour course with a given name.");
                    player.sendMessage(ChatColor.YELLOW + "/parkour reset [name] - " + ChatColor.GRAY + "If you mess up the placement of an item in a parkour course, use this to clear the data for a course to redo it.");
                    player.sendMessage(ChatColor.YELLOW + "/parkour [start|checkpoint|end] [name] - " + ChatColor.GRAY + "Add a certain object to a parkour course. Checkpoints are not required, but if you use them you should add them in the order that they will be reached. A start and finish are necessary for funtionality of a parkour course.");
                    player.sendMessage(ChatColor.YELLOW + "/parkour delete [name] - " + ChatColor.GRAY + "Completely delete a parkour course. If you want one of the same name, you will have to use /parkouradmin create again.");
                    player.sendMessage(ChatColor.YELLOW + "/parkour list - " + ChatColor.GRAY + "List all of the parkour courses you have created.");
                    player.sendMessage(ChatColor.YELLOW + "/parkour reload - " + ChatColor.GRAY + "Reload the configuration file for this plugin.");
                    return true;
                }

                // Handle the creation of new courses
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length > 1) {
                        String courseName = args[1];
                        if (PluginData.get().contains("courses." + courseName)) {
                            player.sendMessage(ChatUtil.translateString("&aThat course already exists! Please pick a different name.", plugin));
                        } else {
                            player.sendMessage(ChatUtil.translateString("&aSuccessfully created course '" + courseName + "'!", plugin));
                            PluginData.get().createSection("courses." + courseName);
                            PluginData.save();
                        }
                    } else {
                        player.sendMessage(ChatUtil.translateString("&aPlease give your new course a name!", plugin));
                    }
                    return true;
                }

                // Handle the deletion of courses
                if (args[0].equalsIgnoreCase("delete")) {
                    if (args.length > 1) {
                        String courseName = args[1];
                        if (PluginData.get().contains("courses." + courseName)) {
                            player.sendMessage(ChatUtil.translateString("&aDeleted the course '" + courseName + "'", plugin));
                            PluginData.get().set("courses." + courseName, null);
                            PluginData.save();
                        } else {
                            player.sendMessage(ChatUtil.translateString("&aThat course does not exist!", plugin));
                        }
                    } else {
                        player.sendMessage(ChatUtil.translateString("&aPlease give the name of the course you want to delete!", plugin));
                    }
                    return true;
                }

                // Handle resetting courses
                if (args[0].equalsIgnoreCase("reset")) {
                    if (args.length > 1) {
                        String courseName = args[1];
                        if (PluginData.get().contains("courses." + courseName)) {
                            PluginData.get().set("courses." + courseName, null);
                            PluginData.get().createSection("courses." + courseName);
                            PluginData.save();
                            player.sendMessage(ChatUtil.translateString("&aThe course '" + courseName + "' has been reset!", plugin));
                        } else {
                            player.sendMessage(ChatUtil.translateString("&aThat course does not exist!", plugin));
                        }
                    } else {
                        player.sendMessage(ChatUtil.translateString("&aPlease give the name of the course you want to reset!", plugin));
                    }
                    return true;
                }

                // Handle creating start points
                if (args[0].equalsIgnoreCase("start")) {
                    if (args.length > 1) {
                        String courseName = args[1];
                        if (PluginData.get().contains("courses." + courseName)) {
                            if (player.getLocation().getBlock().getType().equals(Material.GOLD_PLATE)) {
                                if (PluginData.get().contains("courses." + courseName + ".start")) {
                                    player.sendMessage(ChatUtil.translateString("&aYou have already set the start for this course! To start over, do /parkour reset " + courseName + ".", plugin));
                                } else {
                                    player.sendMessage(ChatUtil.translateString("&aSet the position 'start' for course '" + courseName + "'.", plugin));
                                    Location start = player.getLocation();
                                    PluginData.get().createSection("courses." + courseName + ".start");
                                    PluginData.get().set("courses." + courseName + ".start.world", start.getWorld().getName());
                                    PluginData.get().set("courses." + courseName + ".start.x", start.getBlockX());
                                    PluginData.get().set("courses." + courseName + ".start.y", start.getBlockY());
                                    PluginData.get().set("courses." + courseName + ".start.z", start.getBlockZ());
                                    PluginData.get().set("courses." + courseName + ".start.yaw", start.getYaw());
                                    PluginData.save();
                                }
                            } else {
                                player.sendMessage(ChatUtil.translateString("&aYou must be standing on a gold pressure plate to do that!", plugin));
                            }
                        } else {
                            player.sendMessage(ChatUtil.translateString("&aThat course does not exist!", plugin));
                        }
                    } else {
                        player.sendMessage(ChatUtil.translateString("&aPlease give the name of the course you want to add a start to!", plugin));
                    }
                    return true;
                }

                // Handle creating checkpoints
                if (args[0].equalsIgnoreCase("checkpoint")) {
                    if (args.length > 1) {
                        String courseName = args[1];
                        if (PluginData.get().contains("courses." + courseName)) {
                            if (player.getLocation().getBlock().getType().equals(Material.GOLD_PLATE)) {
                                if (PluginData.get().contains("courses." + courseName + ".start")) {
                                    if (PluginData.get().contains("courses." + courseName + ".end")) {
                                        player.sendMessage(ChatUtil.translateString("&aYou have already set the end for this course! To start over, do /parkour reset " + courseName + ".", plugin));
                                    } else {
                                        if (!PluginData.get().contains("courses." + courseName + ".checkpoints")) {
                                            PluginData.get().createSection("courses." + courseName + ".checkpoints");
                                            PluginData.get().set("courses." + courseName + ".checkpoints-number", 0);
                                            PluginData.save();
                                        }
                                        int checkpointsNum = 0;
                                        for (String ignored : PluginData.get().getConfigurationSection("courses." + courseName + ".checkpoints").getKeys(false)) {
                                            checkpointsNum += 1;
                                        }
                                        int checkpointNumber = checkpointsNum + 1;
                                        PluginData.get().set("courses." + courseName + ".checkpoints-number", checkpointNumber);
                                        player.sendMessage(ChatUtil.translateString("&aSet the position 'checkpoint." + checkpointNumber + "' for " + courseName + "'.", plugin));
                                        Location checkpoint = player.getLocation();
                                        PluginData.get().createSection("courses." + courseName + ".checkpoints." + checkpointNumber);
                                        PluginData.get().set("courses." + courseName + ".checkpoints." + checkpointNumber + ".world", checkpoint.getWorld().getName());
                                        PluginData.get().set("courses." + courseName + ".checkpoints." + checkpointNumber + ".x", checkpoint.getBlockX());
                                        PluginData.get().set("courses." + courseName + ".checkpoints." + checkpointNumber + ".y", checkpoint.getBlockY());
                                        PluginData.get().set("courses." + courseName + ".checkpoints." + checkpointNumber + ".z", checkpoint.getBlockZ());
                                        PluginData.get().set("courses." + courseName + ".checkpoints." + checkpointNumber + ".yaw", checkpoint.getYaw());
                                        PluginData.save();
                                    }
                                } else {
                                    player.sendMessage(ChatUtil.translateString("&aYou must create a start point before placing checkpoints!", plugin));
                                }
                            } else {
                                player.sendMessage(ChatUtil.translateString("&aYou must be standing on a gold pressure plate to do that!", plugin));
                            }
                        } else {
                            player.sendMessage(ChatUtil.translateString("&aThat course does not exist!", plugin));
                        }
                    } else {
                        player.sendMessage(ChatUtil.translateString("&aPlease give the name of the course you want to add a checkpoint to!", plugin));
                    }
                    return true;
                }

                // Handling creating end points
                if (args[0].equalsIgnoreCase("end")) {
                    if (args.length > 1) {
                        String courseName = args[1];
                        if (PluginData.get().contains("courses." + courseName)) {
                            if (player.getLocation().getBlock().getType().equals(Material.GOLD_PLATE)) {
                                if (PluginData.get().contains("courses." + courseName + ".start")) {
                                    if (PluginData.get().contains("courses." + courseName + ".end")) {
                                        player.sendMessage(ChatUtil.translateString("&aYou have already set the end for this course! To start over, do /parkour reset " + courseName + ".", plugin));
                                    } else {
                                        player.sendMessage(ChatUtil.translateString("&aSet the position 'end' for course '" + courseName + "'.", plugin));
                                        Location end = player.getLocation();
                                        PluginData.get().createSection("courses." + courseName + ".end");
                                        PluginData.get().set("courses." + courseName + ".end.world", end.getWorld().getName());
                                        PluginData.get().set("courses." + courseName + ".end.x", end.getBlockX());
                                        PluginData.get().set("courses." + courseName + ".end.y", end.getBlockY());
                                        PluginData.get().set("courses." + courseName + ".end.z", end.getBlockZ());
                                        PluginData.get().set("courses." + courseName + ".end.yaw", end.getYaw());
                                        PluginData.save();
                                    }
                                } else {
                                    player.sendMessage(ChatUtil.translateString("&aYou must create a start point before placing an end point!", plugin));
                                }
                            } else {
                                player.sendMessage(ChatUtil.translateString("&aYou must be standing on a gold pressure plate to do that!", plugin));

                            }
                        } else {
                            player.sendMessage(ChatUtil.translateString("&aThat course does not exist!", plugin));
                        }
                    } else {
                        player.sendMessage(ChatUtil.translateString("&aPlease give the name of the course you want to add an end point to!", plugin));
                    }
                    return true;
                }

                // Handle listing courses
                if (args[0].equalsIgnoreCase("list")) {
                    String list = "&aParkour courses (%num%): ";
                    int i = 0;
                    for (String path : PluginData.get().getConfigurationSection("courses").getKeys(false)) {
                        // Get the total number of courses for formatting
                        i++;
                    }
                    int j = 0;
                    for (String path : PluginData.get().getConfigurationSection("courses").getKeys(false)) {
                        if (j == i - 1) {
                            list = list.concat(path);
                            break;
                        } else {
                            list = list.concat(path + ", ");
                            j++;
                        }
                    }
                    player.sendMessage(ChatUtil.translateString(list.replace("%num%", String.valueOf(i)), plugin));
                    return true;
                }

                player.sendMessage(ChatUtil.translateString("Unknown argument.", plugin));

            } else {
                player.sendMessage(ChatUtil.translateString("&cYou do not have permission to do that!", plugin));
            }
        } else {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    plugin.reloadConfig();
                    sender.sendMessage("Config for UltraParkour reloaded.");
                }
            }
        }
        return true;
    }
}
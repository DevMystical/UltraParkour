package me.mysticaldestroyr.ultraparkour;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckpointCommand implements CommandExecutor {

    UltraParkour plugin;

    public CheckpointCommand(UltraParkour plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (UltraParkour.currentCourse.containsKey(player.getUniqueId())) {
                if (UltraParkour.currentCheckpoint.get(player.getUniqueId()) > 0) {
                    String path = "courses." + UltraParkour.currentCourse.get(player.getUniqueId()) + ".checkpoints." + UltraParkour.currentCheckpoint.get(player.getUniqueId()) + ".";
                    World world = Bukkit.getWorld(PluginData.get().getString(path + "world"));
                    double x = PluginData.get().getInt(path + "x");
                    double y = PluginData.get().getInt(path + "y");
                    double z = PluginData.get().getInt(path + "z");
                    float yaw = (float) PluginData.get().getDouble(path + "yaw");
                    Location target = new Location(world, x, y, z, yaw, 0);
                    player.teleport(target);
                    player.sendMessage(ChatUtil.translateString(plugin.getConfig().getString("return-to-checkpoint").replace("%number%", Integer.toString(UltraParkour.currentCheckpoint.get(player.getUniqueId()))), plugin));
                } else {
                    player.sendMessage(ChatUtil.translateString("&aNo checkpoint to return to!", plugin));
                }
            } else {
                player.sendMessage(ChatUtil.translateString("&aYou must be in a parkour course to do that!", plugin));
            }
        } else {
            sender.sendMessage("You must be a player to do that!");
        }
        return true;
    }
}

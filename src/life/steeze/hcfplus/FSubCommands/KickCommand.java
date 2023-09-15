package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KickCommand implements SubCommand {
    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
        if (p.hasPermission("hcf.player.kick")) {
            Faction f = plugin.getData().getFactionOrError(p);
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please supply an argument");
                return;
            }
            UUID target = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            if (target.equals(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "You can't kick yourself. Try leaving instead.");
                return;
            }
            if (f.removePlayer(target)) {
                p.sendMessage(ConfigManager.SUCCESS);
            } else {
                p.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
            }
        }
    }
}

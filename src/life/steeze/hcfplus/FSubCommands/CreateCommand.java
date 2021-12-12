package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Utilities.PlayerData;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class CreateCommand implements SubCommand {
    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) {
        PlayerData data = plugin.getData();
        if (data.isPlayerInFaction(p)) {
            p.sendMessage(ChatColor.RED + "You are already in a faction");
            return;
        }
        if(args.length == 0){
            p.sendMessage(ChatColor.RED + "Please supply an argument");
            return;
        }
        if (data.getFactionStrictFacName(args[0]) != null) {
            p.sendMessage(ChatColor.RED + "Faction name is taken");
            return;
        }
        Pattern pattern = Pattern.compile("[^a-zA-Z]");
        boolean hasBadChar = pattern.matcher(args[0]).find();
        if (!hasBadChar) {
            Faction newFac = new Faction(args[0], p, plugin);
            data.addFaction(newFac);
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Faction " + ChatColor.WHITE + args[0] + ChatColor.YELLOW + " has been founded!");
        } else {
            p.sendMessage(ChatColor.RED + "Try another name with A-Z only");
        }
    }
}

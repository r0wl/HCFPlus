package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.FileUtils.ConfigManager;
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
    public String getPermission() {
        return "hcf.player.create";
    }

    @Override
    public String getDescription() {
        return "Create a faction";
    }

    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) {
        PlayerData data = plugin.getData();
        if (data.isPlayerInFaction(p)) {
            p.sendMessage(ConfigManager.ALREADY_IN_FACTION);
            return;
        }
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Please supply an argument");
            return;
        }
        if (args[0].length() > ConfigManager.MAX_TEAM_NAME) {
            p.sendMessage(ConfigManager.NAME_TOO_LONG);
            return;
        }
        if (data.getFactionStrictFacName(args[0]) != null) {
            p.sendMessage(ConfigManager.NAME_TAKEN);
            return;
        }
        Pattern pattern = Pattern.compile("[^a-zA-Z]");
        boolean hasBadChar = pattern.matcher(args[0]).find();
        if (!hasBadChar) {
            Faction newFac = new Faction(args[0], p, plugin);
            data.addFaction(newFac);
            Bukkit.broadcastMessage(ConfigManager.FACTION_FOUNDED.replaceAll("\\{faction}", args[0]));
        } else {
            p.sendMessage(ConfigManager.INVALID_NAME);
        }
    }
}

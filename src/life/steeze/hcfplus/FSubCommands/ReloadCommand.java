package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadCommand implements SubCommand {
    @Override
    public void perform(Player player, String[] args, HCFPlugin plugin) throws NotInFaction {
        if(!player.hasPermission("hcf.admin")) return;
        ConfigManager.loadConfig(plugin.getConfig());
        player.sendMessage(ChatColor.YELLOW + "Reloading HCFPlus config.yml");
    }
}

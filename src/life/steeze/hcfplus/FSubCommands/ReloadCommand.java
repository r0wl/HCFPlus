package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadCommand implements SubCommand {
    @Override
    public String getPermission() {
        return "hcf.admin";
    }

    @Override
    public String getDescription() {
        return "Reload config.yml";
    }

    @Override
    public void perform(Player player, String[] args, HCFPlugin plugin) throws NotInFaction {
        plugin.reloadConfig();
        ConfigManager.loadConfig(plugin.getConfig());
        player.sendMessage(ChatColor.YELLOW + "Reloading HCFPlus config.yml");
    }
}

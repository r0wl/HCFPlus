package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AcceptCommand implements SubCommand {
    @Override
    public void perform(Player player, String[] args, HCFPlugin plugin) {
        if(plugin.getData().acceptInvite(player)){
            player.sendMessage(ConfigManager.SUCCESS);
        } else {
            player.sendMessage(ConfigManager.NO_INVITE);
        }
    }
}

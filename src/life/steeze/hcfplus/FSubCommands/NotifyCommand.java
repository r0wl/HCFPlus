package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NotifyCommand implements SubCommand {
    @Override
    public void perform(Player player, String[] args, HCFPlugin plugin) throws NotInFaction {
        Faction f = plugin.getData().getFactionOrError(player);
        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Please supply a message");
        }
        f.broadcast(ChatColor.RED + "" + ChatColor.BOLD + "[FACTION]" + ChatColor.RESET + player.getDisplayName() + ": " + String.join(" ", args));
    }
}

package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UnclaimCommand implements SubCommand {
    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
        Faction f = plugin.getData().getFactionOrError(p);
        if(!f.getLeader().equals(p.getUniqueId())){
            p.sendMessage(ConfigManager.MUST_BE_LEADER);
            return;
        }
        if(f.hasClaim()){
            f.setClaim(null);
            p.sendMessage(ConfigManager.SUCCESS);
            f.broadcast(ChatColor.RED + "Your faction no longer has a claim!");
        } else {
            p.sendMessage(ChatColor.RED + "You do not have a claim");
        }

    }
}

package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.*;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DescCommand implements SubCommand {
    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
        if(p.hasPermission("hcf.player.desc")) {
            Faction f = plugin.getData().getFactionOrError(p);
            if (!f.getLeader().equals(p.getUniqueId())) {
                p.sendMessage(ConfigManager.MUST_BE_LEADER);
                return;
            }
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please supply an argument");
                return;
            }
            String desc = String.join(" ", args);
            if (desc.toCharArray().length <= ConfigManager.MAX_DESCRIPTION_LENGTH) {
                f.setDescription(String.join(" ", args));
                p.sendMessage(ConfigManager.SUCCESS);
            } else {
                p.sendMessage(ChatColor.RED + "Description exceeds maximum length.");
            }

        }
    }
}

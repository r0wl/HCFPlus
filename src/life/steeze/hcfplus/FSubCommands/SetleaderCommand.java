package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetleaderCommand implements SubCommand {
    @Override
    public void perform(Player player, String[] args, HCFPlugin plugin) throws NotInFaction {
        if (player.hasPermission("hcf.player.setleader")) {
            Faction f = plugin.getData().getFactionOrError(player);
            if (!f.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(ConfigManager.MUST_BE_LEADER);
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
                return;
            }
            if (!f.getMembers().contains(target.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Player is not in your faction");
                return;
            }
            f.setLeader(target.getUniqueId());
        }
    }
}

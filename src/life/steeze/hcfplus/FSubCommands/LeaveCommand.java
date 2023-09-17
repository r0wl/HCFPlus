package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaveCommand implements SubCommand {
    @Override
    public String getPermission() {
        return "hcf.player.leave";
    }

    @Override
    public String getDescription() {
        return "Leave your faction";
    }

    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
            Faction f = plugin.getData().getFactionOrError(p);
            if (!f.removePlayer(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "Failed to leave faction. (Are you the leader and raidable?)");
            }
    }
}

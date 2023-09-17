package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetColorCommand implements SubCommand {
    @Override
    public String getPermission() {
        return "hcf.player.setcolor";
    }

    @Override
    public String getDescription() {
        return "Set your faction color";
    }

    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
            Faction f = plugin.getData().getFactionOrError(p);
            if (!f.getLeader().equals(p.getUniqueId())) {
                p.sendMessage(ConfigManager.MUST_BE_LEADER);
            }
            p.openInventory(plugin.getColorGUI());
        }
}

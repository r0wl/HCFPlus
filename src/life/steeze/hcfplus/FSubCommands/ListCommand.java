package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ListCommand implements SubCommand {
    @Override
    public String getPermission() {
        return "hcf.player.list";
    }

    @Override
    public String getDescription() {
        return "View all factions";
    }

    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) {
            p.sendMessage(ChatColor.YELLOW + "----==== Factions List ====----");
            for (Faction listed : plugin.getData().getFactions()) {
                p.sendMessage(listed.getColor() + listed.getName() + ChatColor.YELLOW +
                        " | " + ChatColor.WHITE + (listed.getMembers().size() + 1) + " members." + " | " +
                        listed.getDtr() + "/" + (listed.maxDtr()) + " DTR");
            }
    }
}

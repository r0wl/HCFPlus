package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.*;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Claim;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.Objects.Selection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClaimCommand implements SubCommand {
    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
        Faction f = plugin.getData().getFactionOrError(p);
        if(!f.getLeader().equals(p.getUniqueId())){
            p.sendMessage(ConfigManager.MUST_BE_LEADER);
            return;
        }
        if (f.hasClaim()) {
            p.sendMessage(ChatColor.RED + "Your team already has a claim!");
            return;
        }
        if (!plugin.getData().hasSelection(p)) {
            p.sendMessage(ChatColor.RED + "You must make a selection with /pos1 & /pos2, or use this claiming wand!");
            p.getInventory().addItem(plugin.getWand());
            return;
        }
        Selection selection = plugin.getData().getSelection(p);
        if (selection.pos1().distance(selection.pos2()) >= ConfigManager.MAX_CLAIM_DISTANCE) {
            p.sendMessage(ChatColor.RED + "Claim too big.");
            return;
        }
        Claim c = new Claim(selection, plugin);
        if (c.getBounds().getWidthZ() <= ConfigManager.MIN_CLAIM_WIDTH || c.getBounds().getWidthX() <= ConfigManager.MIN_CLAIM_WIDTH) {
            p.sendMessage(ChatColor.RED + "Claim too small or narrow");
            return;
        }
        if (c.overlapsExisting()) {
            p.sendMessage(ChatColor.RED + "Land is already claimed.");
            return;
        }

        f.setClaim(c);
        p.sendMessage(ConfigManager.SUCCESS);
    }
}

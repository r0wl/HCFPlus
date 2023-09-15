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
        if (p.hasPermission("hcf.player.claim")) {
            Faction f = plugin.getData().getFactionOrError(p);
            if (!f.getLeader().equals(p.getUniqueId())) {
                p.sendMessage(ConfigManager.MUST_BE_LEADER);
                return;
            }
            if (f.hasClaim()) {
                p.sendMessage(ConfigManager.ALREADY_HAS_CLAIM);
                return;
            }
            if (!plugin.getData().hasSelection(p)) {
                p.sendMessage(ConfigManager.TRIED_CLAIM_NO_SELECTION);
                p.getInventory().addItem(plugin.getWand());
                return;
            }
            Selection selection = plugin.getData().getSelection(p);
            if (selection.pos1().distance(selection.pos2()) >= ConfigManager.MAX_CLAIM_DISTANCE) {
                p.sendMessage(ConfigManager.CLAIM_TOO_BIG);
                return;
            }
            Claim c = new Claim(selection, plugin);
            if (c.getBounds().getWidthZ() <= ConfigManager.MIN_CLAIM_WIDTH || c.getBounds().getWidthX() <= ConfigManager.MIN_CLAIM_WIDTH) {
                p.sendMessage(ConfigManager.CLAIM_TOO_NARROW);
                return;
            }
            if (c.overlapsExisting()) {
                p.sendMessage(ConfigManager.LAND_ALREADY_CLAIMED);
                return;
            }

            f.setClaim(c);
            p.sendMessage(ConfigManager.SUCCESS);
        }
    }
}

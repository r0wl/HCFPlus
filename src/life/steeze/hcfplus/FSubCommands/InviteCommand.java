package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InviteCommand implements SubCommand {
    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
        if (p.hasPermission("hcf.player.invite")) {
            Faction f = plugin.getData().getFactionOrError(p);
            if (!f.getLeader().equals(p.getUniqueId())) {
                p.sendMessage(ConfigManager.MUST_BE_LEADER);
                return;
            }
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please supply an argument");
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || target.equals(p)) {
                p.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
                return;
            }
            if (plugin.getData().isPlayerInFaction(target)) {
                p.sendMessage(ChatColor.RED + "Player is already in a faction");
                return;
            }
            if (plugin.getData().hasInvitation(p)) {
                p.sendMessage(ChatColor.RED + "Player already has an invitation. Wait for it to expire.");
                return;
            }
            plugin.getData().addInvitation(target, f);
            p.sendMessage(ConfigManager.SUCCESS);
            target.sendMessage(ChatColor.YELLOW + "You have been invited to join " + f.getColor() + f.getName() + ChatColor.YELLOW + ". Do you accept? " + ChatColor.WHITE + "/f accept");
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getData().expireInvitation(target);
                }
            }.runTaskLaterAsynchronously(plugin, 600);
        }
    }
}

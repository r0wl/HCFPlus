package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.SubCommand;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.Objects.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InfoCommand implements SubCommand {
    @Override
    public String getPermission() {
        return "hcf.player.info";
    }

    @Override
    public String getDescription() {
        return "View a faction's info";
    }

    @Override
    public void perform(Player player, String[] args, HCFPlugin plugin) throws NotInFaction {
            // case when /f info is done with no arguments
            if (args.length == 0) {
                Faction f = plugin.getData().getFactionOrError(player);
                f.showInfo(player);
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (plugin.getData().isPlayerInFaction(target)) {
                    plugin.getData().getFaction(target).showInfo(player);
                    return;
                }
            }
            Faction targetF = plugin.getData().getFaction(args[0]);
            if (targetF != null) {
                targetF.showInfo(player);
                return;
            }
            player.sendMessage(ConfigManager.NOT_A_PLAYER_OR_FACTION);
        }
}

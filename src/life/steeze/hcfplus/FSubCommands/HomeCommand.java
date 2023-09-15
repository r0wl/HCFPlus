package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeCommand implements SubCommand {
    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
        if(p.hasPermission("hcf.player.home")) {
            Faction f = plugin.getData().getFactionOrError(p);

            //inform user of pending teleport
            p.sendMessage(ConfigManager.TELEPORT_PENDING);

            //create pending teleport
            int r = new BukkitRunnable() {
                @Override
                public void run() {
                    //tp
                    f.tpHome(p);
                    //remove pending teleport
                    plugin.getData().getPendingTeleports().remove(p);
                }
            }.runTaskLater(plugin, ConfigManager.TELEPORT_DELAY).getTaskId();

            //add to list so taking damage can cancel teleport
            plugin.getData().getPendingTeleports().put(p, r);
        }
    }
}

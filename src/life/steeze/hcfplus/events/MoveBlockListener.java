package life.steeze.hcfplus.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class MoveBlockListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if (e.getTo() == null) return;
        int ox = e.getFrom().getBlockX(),oz = e.getFrom().getBlockZ(),nx = e.getTo().getBlockX(),nz = e.getTo().getBlockZ();
        if (nx == ox && nz == oz) {
            return;
        }
        MoveBlockEvent moveBlockEvent = new MoveBlockEvent(e.getPlayer(), e.getTo());
        Bukkit.getServer().getPluginManager().callEvent(moveBlockEvent);
    }
}

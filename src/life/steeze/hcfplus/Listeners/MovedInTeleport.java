package life.steeze.hcfplus.Listeners;

import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.events.MoveBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MovedInTeleport implements Listener {
    private final HCFPlugin plugin;
    public MovedInTeleport(HCFPlugin plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void movedBlock(MoveBlockEvent e){
        if(!plugin.getData().getPendingTeleports().containsKey(e.getPlayer())){
            return;
        }
        Integer runnable = plugin.getData().getPendingTeleports().remove(e.getPlayer());
        if(runnable != null) Bukkit.getScheduler().cancelTask(runnable);
    }
}

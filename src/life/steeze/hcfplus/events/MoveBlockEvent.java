package life.steeze.hcfplus.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MoveBlockEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private final Location location;

    public static void registerListener(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new MoveBlockListener(), plugin);
    }

    public MoveBlockEvent(Player who, Location l) {
        super(who);
        this.location = l;
    }

    public Location getLocation(){
        return location;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}

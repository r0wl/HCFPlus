package life.steeze.hcfplus.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArcherHitEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    private final Player piercedPlayer;
    public Player getPiercedPlayer(){
        return  piercedPlayer;
    }
    public ArcherHitEvent(Player p){
        this.piercedPlayer = p;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}

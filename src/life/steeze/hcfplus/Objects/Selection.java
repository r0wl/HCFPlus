package life.steeze.hcfplus.Objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.awt.*;

public class Selection {
    private Point start,end;
    private World world;

    public Point pos1(){
        return this.start;
    }
    public Point pos2(){
        return this.end;
    }
    public World getWorld(){
        return this.world;
    }

    public void setPos1(Location l){
        Point p = new Point(l.getBlockX(),l.getBlockZ());
        this.start = p;
        this.world = l.getWorld();
    }
    public void setPos2(Location l){
        Point p = new Point(l.getBlockX(), l.getBlockZ());
        this.end = p;
        this.world = l.getWorld();
    }
}

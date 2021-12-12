package life.steeze.hcfplus.Objects;

import life.steeze.hcfplus.HCFPlugin;
import org.bukkit.*;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Claim implements ConfigurationSerializable {
    private int x,z,x2,z2;
    private BoundingBox bounds;
    private World world;

    private HCFPlugin plugin;

    public Claim(Selection s, HCFPlugin plugin){
        this.x = s.pos1().x;
        this.z = s.pos1().y;
        this.x2 = s.pos2().x;
        this.z2 = s.pos2().y;
        this.bounds = new BoundingBox(this.x,0,this.z,this.x2,255,this.z2);
        this.world = s.getWorld();
        this.plugin = plugin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Claim claim = (Claim) o;
        return x == claim.x &&
                z == claim.z &&
                x2 == claim.x2 &&
                z2 == claim.z2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, x2, z2);
    }

    public Claim(MemorySection map, HCFPlugin plugin){
        this.x = (int) map.get("x");
        this.z = (int) map.get("z");
        this.x2 = (int) map.get("x2");
        this.z2 = (int) map.get("z2");
        this.bounds = new BoundingBox(this.x,0,this.z,this.x2,255,this.z2);
        this.world = Bukkit.getWorld((String) map.get("world"));
        this.plugin = plugin;
    }


    public BoundingBox getBounds(){
        return this.bounds;
    }
    public boolean containsLocation(Location l){
        if(this.bounds.contains(l.toVector()) && this.world.equals(l.getWorld())){
            return true;
        }
        return false;
    }
    @Override
    public String toString(){
        return ChatColor.YELLOW + "(" + this.x + ", " + this.z2 + "), (" + this.x2 + ", " + this.z2 + ")";
    }

    public boolean overlapsExisting(){
        for(Faction f : plugin.getData().getFactions()){
            if(f.hasClaim()) {
                if (this.getBounds().overlaps(f.getClaim().getBounds())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void showBounds(Player p){
        double height = p.getWorld().getHighestBlockYAt(this.getBounds().getCenter().toLocation(p.getWorld())) + 25.0;
        double minX = this.getBounds().getMinX();
        double maxX = this.getBounds().getMaxX();
        double minZ = this.getBounds().getMinZ();
        double maxZ = this.getBounds().getMaxZ();
        for (double i = minX; i < maxX; i += 1d / 3d) {
            for (double j = 62; j < height; j += 4.0) {
                p.spawnParticle(Particle.DRIP_LAVA, i, j, minZ, 1);
                p.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, i, j + 2.0, minZ, 1);
            }
        }
        for (double i = minX; i < maxX; i += 1d / 3d) {
            for (double j = 62; j < height; j += 4.0) {
                p.spawnParticle(Particle.DRIP_LAVA, i, j, maxZ, 1);
                p.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, i, j + 2.0, maxZ, 1);
            }
        }
        for (double i = minZ; i < maxZ; i += 1d / 3d) {
            for (double j = 62; j < height; j += 4.0) {
                p.spawnParticle(Particle.DRIP_LAVA, minX, j, i, 1);
                p.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, minX, j + 2.0, i, 1);
            }
        }
        for (double i = minZ; i < maxZ; i += 1d / 3d) {
            for (double j = 62; j < height; j += 4.0) {
                p.spawnParticle(Particle.DRIP_LAVA, maxX, j, i, 1);
                p.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, maxX, j + 2.0, i, 1);
            }
        }
    }

    public static Claim deserialize(MemorySection map, HCFPlugin plugin){
        return new Claim(map, plugin);
    }

    public String start(){
        return this.x + ", " + this.z;
    }
    public String end(){
        return this.x2 + ", " + this.z2;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("world", this.world.getName());
        map.put("x", this.x);
        map.put("z", this.z);
        map.put("x2", this.x2);
        map.put("z2", this.z2);
        return map;
    }
}

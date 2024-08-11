package life.steeze.hcfplus.Objects;

import life.steeze.hcfplus.HCFPlugin;
import org.bukkit.*;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Claim implements ConfigurationSerializable {
    private final int x,z,x2,z2;
    private final World world;
    private final HCFPlugin plugin;

    public Claim(Selection s, HCFPlugin plugin){
        this.x = s.pos1().x;
        this.z = s.pos1().y;
        this.x2 = s.pos2().x;
        this.z2 = s.pos2().y;
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
        this.world = Bukkit.getWorld((String) map.get("world"));
        this.plugin = plugin;
    }

    public boolean containsLocation(Location l) {
        if (!l.getWorld().equals(this.world)) {
            return false;
        }
        int locX = l.getBlockX();
        int locZ = l.getBlockZ();
        return locX >= Math.min(this.x, this.x2) && locX <= Math.max(this.x, this.x2) &&
                locZ >= Math.min(this.z, this.z2) && locZ <= Math.max(this.z, this.z2);
    }
    @Override
    public String toString(){
        return ChatColor.YELLOW + "(" + this.x + ", " + this.z2 + "), (" + this.x2 + ", " + this.z2 + ")";
    }

    public boolean overlapsExisting(){
        for(Faction f : plugin.getData().getFactions()){
            if(f.hasClaim()) {
                if (this.overlaps(f.getClaim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean overlaps(Claim other) {
        if (!this.world.equals(other.world)) {
            return false;
        }
        return Math.max(this.x, this.x2) >= Math.min(other.x, other.x2) &&
                Math.min(this.x, this.x2) <= Math.max(other.x, other.x2) &&
                Math.max(this.z, this.z2) >= Math.min(other.z, other.z2) &&
                Math.min(this.z, this.z2) <= Math.max(other.z, other.z2);
    }

    public void showBounds(Player p){
        double height = this.world.getHighestBlockYAt(new Location(this.world, (x + x2) / 2.0, 0, (z + z2) / 2.0)) + 25.0;
        double minX = Math.min(this.x, this.x2);
        double maxX = Math.max(this.x, this.x2);
        double minZ = Math.min(this.z, this.z2);
        double maxZ = Math.max(this.z, this.z2);
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

    public int getX2() {
        return x2;
    }
    public int getX(){
        return x;
    }
    public int getZ(){
        return z;
    }
    public int getZ2(){
        return z2;
    }
}

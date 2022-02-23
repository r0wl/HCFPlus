package life.steeze.hcfplus.Timers;

import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class AbilitiesTimer {

    private HCFPlugin plugin;

    public AbilitiesTimer(HCFPlugin plugin){
        this.plugin = plugin;
        classTimer.runTaskTimer(this.plugin, 60, 100);
        abilityTimer.runTaskTimer(this.plugin, 60, 10);
    }

    static final Material[] minerKit = {Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET};
    static final Material[] archerKit = {Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET};
    static final Material[] bardKit = {Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET};

    static final PotionEffect[] minerEffect = {new PotionEffect(PotionEffectType.FAST_DIGGING, 360, 1), new PotionEffect(PotionEffectType.NIGHT_VISION, 160, 0)};
    static final PotionEffect[] archerEffect = {new PotionEffect(PotionEffectType.JUMP, 160, 1), new PotionEffect(PotionEffectType.SPEED, 160, 2)};
    static final PotionEffect[] bardEffect = {new PotionEffect(PotionEffectType.REGENERATION, 160, 1), new PotionEffect(PotionEffectType.SPEED, 160, 0), new PotionEffect(PotionEffectType.WEAKNESS, 120, 0), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 160, 1)};



    ArrayList<Player> bards = new ArrayList<>();
    private void applyBardAbility(Faction f, PotionEffectType effect, int amplifier){
        for(Player p : f.getOnlineMembers()){
            p.addPotionEffect(new PotionEffect(effect, 20, amplifier));
        }
    }
    private String isPlayerWearingKit(Player p){
        Material[] armor = new Material[4];
        int n = 0;
        for(ItemStack i : p.getInventory().getArmorContents()){
            if(i == null){
                return null;
            }
            armor[n] = i.getType();
            n++;
        }
        if(armor[0].equals(Material.IRON_BOOTS)){
            for(int i = 1; i < 4; i++){
                if(!armor[i].equals(minerKit[i])) return null;
            }
            return "miner";
        }
        if(armor[0].equals(Material.LEATHER_BOOTS)){
            for(int i = 1; i < 4; i++){
                if(!armor[i].equals(archerKit[i])) return null;
            }
            return "archer";
        }
        if(armor[0].equals(Material.GOLDEN_BOOTS)){
            for(int i = 1; i < 4; i++){
                if(!armor[i].equals(bardKit[i])) return null;
            }
            return "bard";
        }
        return null;
    }

    BukkitRunnable classTimer = new BukkitRunnable() {
        @Override
        public void run() {
            bards.clear();
            for(Player p : Bukkit.getOnlinePlayers()){
                if(isPlayerWearingKit(p) == null) continue;
                if(isPlayerWearingKit(p).equals("miner")){
                    for(PotionEffect e : minerEffect){
                        p.addPotionEffect(e);
                    }
                    continue;
                }
                if(isPlayerWearingKit(p).equals("archer")){
                    for(PotionEffect e : archerEffect){
                        p.addPotionEffect(e);
                    }
                    continue;
                }
                if(isPlayerWearingKit(p).equals("bard")){
                    bards.add(p);
                    for(PotionEffect e : bardEffect){
                        p.addPotionEffect(e);
                    }
                }
            }
        }
    };

    BukkitRunnable abilityTimer = new BukkitRunnable() {
        @Override
        public void run() {
            for(Player p : bards){
                Faction f = plugin.getData().getFaction(p);
                if(f == null) continue;
                Material m = p.getItemInHand().getType();
                if(m.equals(Material.SUGAR)){
                    applyBardAbility(f, PotionEffectType.SPEED, 1);
                    continue;
                }
                if(m.equals(Material.BLAZE_POWDER)){
                    applyBardAbility(f, PotionEffectType.INCREASE_DAMAGE, 0);
                    continue;
                }
                if(m.equals(Material.IRON_INGOT)){
                    applyBardAbility(f, PotionEffectType.DAMAGE_RESISTANCE, 0);
                    continue;
                }
                if(m.equals(Material.GHAST_TEAR)){
                    applyBardAbility(f, PotionEffectType.REGENERATION, 0);
                    continue;
                }
                if(m.equals(Material.FEATHER)){
                    applyBardAbility(f, PotionEffectType.JUMP, 1);
                }
            }
        }
    };




}

package life.steeze.hcfplus.Timers;

import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DTRTimer {
    private HCFPlugin plugin;
    public DTRTimer(HCFPlugin plugin){
        this.plugin = plugin;
        dtrTimer.runTaskTimer(plugin, 20, ConfigManager.DTR_REGEN);
    }
    BukkitRunnable dtrTimer = new BukkitRunnable() {
        @Override
        public void run() {
            for(Faction f : plugin.getData().getFactions()){
                f.regenDtr();
            }
        }
    };
}

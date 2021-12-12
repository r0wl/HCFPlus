package life.steeze.hcfplus;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import org.bukkit.entity.Player;


public interface SubCommand {
    abstract void perform(Player player, String[] args, HCFPlugin plugin) throws NotInFaction;
}

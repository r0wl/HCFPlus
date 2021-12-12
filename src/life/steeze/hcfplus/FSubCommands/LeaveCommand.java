package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.entity.Player;

public class LeaveCommand implements SubCommand {
    @Override
    public void perform(Player p, String[] args, HCFPlugin plugin) throws NotInFaction {
        Faction f = plugin.getData().getFactionOrError(p);
        f.removePlayer(p.getUniqueId());
    }
}

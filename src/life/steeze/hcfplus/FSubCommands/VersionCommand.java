package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.entity.Player;

public class VersionCommand implements SubCommand {
    @Override
    public void perform(Player player, String[] args, HCFPlugin plugin) throws NotInFaction {
        player.sendMessage(plugin.getDescription().getVersion());
    }

    @Override
    public String getPermission() {
        return "hcf.admin";
    }

    @Override
    public String getDescription() {
        return "Check plugin version";
    }
}

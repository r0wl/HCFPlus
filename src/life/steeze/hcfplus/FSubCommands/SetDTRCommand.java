package life.steeze.hcfplus.FSubCommands;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetDTRCommand implements SubCommand {
    @Override
    public void perform(Player player, String[] args, HCFPlugin plugin) throws NotInFaction {
        //permission check
        if(!player.hasPermission("hcfplus.admin")) {
            player.sendMessage(ChatColor.RED + "No Permission");
            return;
        }
        //check first arg for faction/player with faction
        Faction f = plugin.getData().getFaction(args[0]);
        if(f == null){
            player.sendMessage(ConfigManager.NOT_A_PLAYER_OR_FACTION);
            return;
        }
        //check second arg for integer
        int dtr;
        try {
            dtr = Integer.parseInt(args[1]);
        } catch(NumberFormatException e){
            player.sendMessage(ChatColor.RED + "Invalid number");
            return;
        }
        f.setDtr(dtr);
    }
}

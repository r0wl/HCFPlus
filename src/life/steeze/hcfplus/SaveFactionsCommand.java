package life.steeze.hcfplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveFactionsCommand implements CommandExecutor {

    private final HCFPlugin plugin;

    public SaveFactionsCommand(HCFPlugin hcfPlugin) {
        this.plugin = hcfPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("hcf.admin")){
            plugin.getFactionsFile().saveFactions();
        }
        return true;
    }
}

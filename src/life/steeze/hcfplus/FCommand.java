package life.steeze.hcfplus;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FSubCommands.*;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class FCommand implements CommandExecutor {

    private final String[] help = {(ChatColor.WHITE + "---===" + ChatColor.YELLOW + " Factions Help " + ChatColor.WHITE + "===---"),
            (ChatColor.WHITE + "/f create <name>" + ChatColor.YELLOW + " - Create a faction"),
            (ChatColor.WHITE + "/f desc <your description here>" + ChatColor.YELLOW + " - Tell everyone what you're about"),
            (ChatColor.WHITE + "/f invite <name>" + ChatColor.YELLOW + " - Invite your friends"),
            (ChatColor.WHITE + "/f kick <name>" + ChatColor.YELLOW + " - Kick someone"),
            (ChatColor.WHITE + "/f setleader <name>" + ChatColor.YELLOW + " - Relinquish your power"),
            (ChatColor.WHITE + "/f leave" + ChatColor.YELLOW + " - Leave your faction"),
            (ChatColor.WHITE + "/f info [name]" + ChatColor.YELLOW + " - View a faction's info"),
            (ChatColor.WHITE + "/f setcolor" + ChatColor.YELLOW + " - Set your faction color"),
            (ChatColor.WHITE + "/f list" + ChatColor.YELLOW + " - View all factions"),
            (ChatColor.WHITE + "/f sethome" + ChatColor.YELLOW + " - Set your faction's home"),
            (ChatColor.WHITE + "/f home" + ChatColor.YELLOW + " - Go to your faction's home"),
            (ChatColor.WHITE + "/f claim" + ChatColor.YELLOW + " - Obtain a wand to make your faction's claim"),
            (ChatColor.WHITE + "/f unclaim" + ChatColor.YELLOW + " - Undo your claim"),
            (ChatColor.WHITE + "/f notify <message>" + ChatColor.YELLOW + " - Send a message to your teammates")},

    adminHelp = {
            (ChatColor.WHITE + "/f reload" + ChatColor.YELLOW + " - Reload config.yml"),
            (ChatColor.WHITE + "/f version" + ChatColor.YELLOW + " - Check plugin version"),
            (ChatColor.WHITE + "/f setdtr <faction/player> <DTR>" + ChatColor.YELLOW + " - Set a faction's DTR")
    };

    private void sendHelp(Player commandSender) {
        for(Map.Entry<String, SubCommand> e : subCommandMap.entrySet()){
            SubCommand v = e.getValue();
            String arg = e.getKey();
            if(commandSender.hasPermission(v.getPermission())) commandSender.sendMessage("/f " + arg + " - " + ChatColor.YELLOW + v.getDescription());
            if(commandSender.isOp()) commandSender.sendMessage(ChatColor.GRAY + v.getPermission());
        }
    }

//    private void sendHelp(Player commandSender) {
//        commandSender.sendMessage(help);
//        if(commandSender.hasPermission("hcf.admin")){
//            commandSender.sendMessage(adminHelp);
//        }
//    }
    private Map<String, SubCommand> subCommandMap = new LinkedHashMap<>();

    private final HCFPlugin plugin;
    public FCommand(final HCFPlugin inst) {
        this.plugin = inst;
        InfoCommand info = new InfoCommand();
        subCommandMap.put("create", new CreateCommand());
        subCommandMap.put("desc", new DescCommand());
        subCommandMap.put("invite", new InviteCommand());
        subCommandMap.put("accept", new AcceptCommand());
        subCommandMap.put("kick", new KickCommand());
        subCommandMap.put("setleader", new SetleaderCommand());
        subCommandMap.put("leave", new LeaveCommand());
        subCommandMap.put("info", info);
        subCommandMap.put("who", info);
        subCommandMap.put("setcolor", new SetColorCommand());
        subCommandMap.put("list", new ListCommand());
        subCommandMap.put("sethome", new SetHomeCommand());
        subCommandMap.put("home", new HomeCommand());
        subCommandMap.put("claim", new ClaimCommand());
        subCommandMap.put("unclaim", new UnclaimCommand());
        subCommandMap.put("notify", new NotifyCommand());
        subCommandMap.put("reload", new ReloadCommand());
        subCommandMap.put("version", new VersionCommand());
        subCommandMap.put("setdtr", new SetDTRCommand());
    }



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) return true;
        Player p = (Player) commandSender;
        if (args.length == 0) {
            sendHelp(p);
            return true;
        }
        String argument = args[0].toLowerCase();
        String[] pass = Arrays.copyOfRange(args, 1, args.length);
        if (!subCommandMap.containsKey(argument)) {
            sendHelp(p);
            return true;
        }
        try {
            SubCommand subCmd = subCommandMap.get(argument);
            if (p.hasPermission(subCmd.getPermission())) {
                subCmd.perform(p, pass, plugin);
            } else {
                p.sendMessage(ConfigManager.NO_PERMISSION);
            }
        } catch (NotInFaction e) {
        }
        return true;
    }
}

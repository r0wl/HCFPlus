package life.steeze.hcfplus;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FSubCommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FCommand implements CommandExecutor {
    private void sendHelp(Player commandSender) {
        commandSender.sendMessage(ChatColor.WHITE + "---===" + ChatColor.YELLOW + " Factions Help " + ChatColor.WHITE + "===---");
        commandSender.sendMessage(ChatColor.WHITE + "/f create <name>" + ChatColor.YELLOW + " - Create a faction");
        commandSender.sendMessage(ChatColor.WHITE + "/f desc <your description here>" + ChatColor.YELLOW + " - Tell everyone what you're about");
        commandSender.sendMessage(ChatColor.WHITE + "/f invite <name>" + ChatColor.YELLOW + " - Invite your friends");
        commandSender.sendMessage(ChatColor.WHITE + "/f kick <name>" + ChatColor.YELLOW + " - Kick someone");
        commandSender.sendMessage(ChatColor.WHITE + "/f setleader <name>" + ChatColor.YELLOW + " - Relinquish your power");
        commandSender.sendMessage(ChatColor.WHITE + "/f leave" + ChatColor.YELLOW + " - Leave your faction");
        commandSender.sendMessage(ChatColor.WHITE + "/f info [name]" + ChatColor.YELLOW + " - View a faction's info");
        commandSender.sendMessage(ChatColor.WHITE + "/f setcolor" + ChatColor.YELLOW + " - Set your faction color");
        commandSender.sendMessage(ChatColor.WHITE + "/f list" + ChatColor.YELLOW + " - View all factions");
        commandSender.sendMessage(ChatColor.WHITE + "/f sethome" + ChatColor.YELLOW + " - Set your faction's home");
        commandSender.sendMessage(ChatColor.WHITE + "/f home" + ChatColor.YELLOW + " - Go to your faction's home");
        commandSender.sendMessage(ChatColor.WHITE + "/f claim" + ChatColor.YELLOW + " - Obtain a wand to make your faction's claim");
        commandSender.sendMessage(ChatColor.WHITE + "/f unclaim" + ChatColor.YELLOW + " - Undo your claim");
        commandSender.sendMessage(ChatColor.WHITE + "/f notify <message>" + ChatColor.YELLOW + " - Send a message to your teammates");
    }
    private Map<String, SubCommand> subCommandMap = new HashMap<>();

    private final HCFPlugin plugin;
    public FCommand(final HCFPlugin inst) {
        this.plugin = inst;
        subCommandMap.put("list", new ListCommand());
        subCommandMap.put("create", new CreateCommand());
        subCommandMap.put("accept", new AcceptCommand());
        subCommandMap.put("leave", new LeaveCommand());
        subCommandMap.put("home", new HomeCommand());
        subCommandMap.put("sethome", new SetHomeCommand());
        subCommandMap.put("claim", new ClaimCommand());
        subCommandMap.put("unclaim", new UnclaimCommand());
        subCommandMap.put("setcolor", new SetColorCommand());
        subCommandMap.put("desc", new DescCommand());
        subCommandMap.put("kick", new KickCommand());
        subCommandMap.put("info", new InfoCommand());
        subCommandMap.put("invite", new InviteCommand());
        subCommandMap.put("setleader", new SetleaderCommand());
        subCommandMap.put("reload", new ReloadCommand());
        subCommandMap.put("version", new VersionCommand());
        subCommandMap.put("notify", new NotifyCommand());
    }



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) return true;
        Player p = (Player) commandSender;
        if (args.length == 0) {
            sendHelp(p);
            return true;
        }
        String argument = args[0];
        String[] pass = Arrays.copyOfRange(args, 1, args.length);
        if (!subCommandMap.containsKey(argument)) {
            sendHelp(p);
            return true;
        }
        try {
            subCommandMap.get(argument).perform(p, pass, plugin);
        } catch (NotInFaction e) {
        }
        return true;
    }
}

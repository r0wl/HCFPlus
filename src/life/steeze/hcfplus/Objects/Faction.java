package life.steeze.hcfplus.Objects;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import java.util.*;

public class Faction implements ConfigurationSerializable {

    private HCFPlugin data;



    //Attributes
    private final String name;
    public String getName() {
        return this.name;
    }

    private Location home;
    public void setHome(Player p) {
        if (this.claim == null) {
            p.sendMessage(ChatColor.RED + "You need to have a claim!");
            return;
        }
        if (this.claim.containsLocation(p.getLocation())) {
            p.sendMessage(ChatColor.YELLOW + "Setting home...");
            this.home = p.getLocation();
            p.sendMessage(ChatColor.YELLOW + "Done!");
        } else {
            p.sendMessage(ChatColor.RED + "Your home must be within your claim");
        }

    }
    public void tpHome(Player p) {
        if (this.home != null) {
            p.teleport(this.home);
        } else {
            p.sendMessage(ChatColor.RED + "Your faction does not have a home.");
        }
    }

    private String description;
    public void setDescription(String s) {
        this.description = s;
    }

    private int dtr;
    private int maxDtr(){
        return (this.members.size() + 2);
    }
    public void regenDtr() {
        if (this.dtr < maxDtr()) this.dtr += 1; // Add 1 for the leader of faction
    }
    public int getDtr() {
        return this.dtr;
    }
    public void loseDtr(){
        if(this.dtr > ConfigManager.MINIMUM_DTR) this.dtr--;
    }

    private UUID leader;
    public UUID getLeader() { return this.leader; }

    private ArrayList<UUID> members = new ArrayList<>();
    public ArrayList<UUID> getMembers() { return this.members; }
    public Player[] getOnlineMembers(){
        ArrayList<Player> members = new ArrayList<>();
        if(Bukkit.getPlayer(this.leader) != null) members.add(Bukkit.getPlayer(this.leader));
        for(UUID member : this.members){
            if(Bukkit.getPlayer(member) != null) members.add(Bukkit.getPlayer(member));
        }
        Player[] online = new Player[members.size()];
        members.toArray(online);
        return online;
    }
    public boolean hasMember(Player p) {
        if (this.members.contains(p.getUniqueId())) return true;
        return this.leader.equals(p.getUniqueId());
    }

    private ChatColor color = ChatColor.WHITE;
    public ChatColor getColor(){return this.color;}
    public void setColor(ChatColor color){
        this.color = color;
    }

    private Claim claim;
    public Claim getClaim() {
        return this.claim;
    }
    public void setClaim(Claim c) {
        this.claim = c;
    }
    public boolean hasClaim() {
        if (this.claim == null) return false;
        return true;
    }


    //Hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faction faction = (Faction) o;
        return name.equals(faction.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    //Constructor for new faction
    public Faction(final String name, final Player p, final HCFPlugin data) {
        this.name = name;
        this.leader = p.getUniqueId();
        this.dtr = 2;
        this.data = data;
        this.data.getData().addFPlayer(p, this);
    }
    //Constructor for loaded faction
    public Faction(Map<String, Object> map, final HCFPlugin data) {
        this.data = data;
        this.name = (String) map.get("name");
        this.leader = UUID.fromString((String) map.get("leader"));
        for (String m : (ArrayList<String>) map.get("members")) {
            this.members.add(UUID.fromString(m));
            if(Bukkit.getOfflinePlayer(UUID.fromString(m)).isOnline()) data.getData().addFPlayer(Bukkit.getPlayer(UUID.fromString(m)), this);
        }
        this.description = (String) map.get("description");
        this.dtr = (int) map.get("dtr");
        this.color = ChatColor.getByChar((String) map.get("color"));
        this.home = (Location) map.get("home");
        if (map.get("claim") != null) this.claim = Claim.deserialize((MemorySection) map.get("claim"), data);
        else this.claim = null;

    }


    //Broadcast to all members
    public void broadcast(final String s){
        for (UUID id : this.members) {
            if(Bukkit.getOfflinePlayer(id).isOnline()) {
                Player player = Bukkit.getPlayer(id);
                player.sendMessage(s);
            }
        }
        if(Bukkit.getOfflinePlayer(this.leader).isOnline()){
            Bukkit.getPlayer(this.leader).sendMessage(s);
        }
    }

    //Set new faction leader & add old one to members.
    public void setLeader(final UUID id) {
        this.members.add(this.leader);
        this.leader = id;
        for (UUID uuid : this.members) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                Player p = Bukkit.getPlayer(uuid);
                p.sendMessage(ChatColor.YELLOW + Bukkit.getOfflinePlayer(this.leader).getName() + ChatColor.YELLOW + " is now the leader of the faction!");
            }
        }
        if (Bukkit.getOfflinePlayer(this.leader).isOnline())
            Bukkit.getPlayer(this.leader).sendMessage(ChatColor.YELLOW + "You are now the leader of your faction!");
    }

    // Faction info display.
    public void showInfo(final Player p) {
        p.sendMessage(ChatColor.YELLOW + "----==== Faction info: " + this.color + this.name + ChatColor.YELLOW + " ====----");
        p.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.WHITE + this.description);
        p.sendMessage(ChatColor.YELLOW + "DTR: " + ChatColor.WHITE + this.dtr + (this.dtr <= 0 ? ChatColor.RED + "| RAIDABLE" : ""));
        p.sendMessage(ChatColor.YELLOW + "Leader: " + ChatColor.WHITE + (Bukkit.getOfflinePlayer(this.leader).isOnline() ? Bukkit.getPlayer(this.leader).getName() : Bukkit.getOfflinePlayer(this.leader).getName()));
        p.sendMessage(ChatColor.YELLOW + "Members:" + ChatColor.WHITE);
        for (UUID id : this.members) {
            String name;
            if (Bukkit.getOfflinePlayer(id).isOnline()) {
                name = ChatColor.GREEN + Bukkit.getPlayer(id).getName();
            } else {
                name = ChatColor.RED + Bukkit.getOfflinePlayer(id).getName();
            }
            p.sendMessage(name);
        }
        if(!ConfigManager.SHOW_COORDS_IN_INFO) return;
        if (this.hasClaim()) {
            p.sendMessage(ChatColor.GREEN + "Claim start: " + this.claim.start() +
                    "\n" +"Claim end: " + this.claim.end());
            if (this.claim.getBounds().contains(p.getLocation().toVector())) {
                this.claim.showBounds(p);
            }
        } else {
            p.sendMessage(ChatColor.YELLOW + "This faction does not have a claim.");
        }
    }

    //Disband faction and delete all FPlayers
    public void disband() {
        data.getData().removeFPlayer(Bukkit.getPlayer(this.leader));
        for (UUID id : this.getMembers()) {
            data.getData().removeFPlayer(Bukkit.getPlayer(id));
        }
        data.getData().removeFaction(this);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Faction " + ChatColor.WHITE + this.name + ChatColor.YELLOW + " has been disbanded!");
    }

    //Remove player from faction, whether they left or were kicked. Returns true if player was in the faction, false if player was not.
    public boolean removePlayer(final UUID p) {
        if(p.equals(this.leader)){
            if(this.dtr <= 0) return false;
            this.disband();
            return true;
        }
        if(this.members.remove(p)){
            data.getData().removeFPlayer(Bukkit.getPlayer(p)); //Remove from FPlayers if online
            this.broadcast(Bukkit.getOfflinePlayer(p).getName() + ChatColor.GOLD + " is no longer in the faction.");
            return true;
        }
        return false;
    }

    //Add player to faction, player must be online to join so we use Player
    public void addPlayer(final Player p) {
        this.members.add(p.getUniqueId());
        data.getData().addFPlayer(p, this); //Add player to FPlayers so interactions work.
        this.broadcast(ChatColor.YELLOW + p.getName() + " has joined the faction!");
    }


    //Serialization
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> membersStr = new ArrayList<>();
        for (UUID i : this.members) {
            membersStr.add(i.toString());
        }
        map.put("name", this.name);
        map.put("color", Character.toString(this.color.getChar()));
        map.put("leader", this.leader.toString());
        map.put("members", membersStr);
        map.put("description", this.description);
        map.put("dtr", this.dtr);
        map.put("home", this.home);
        if (this.claim != null) map.put("claim", this.claim.serialize());
        else map.put("claim", null);
        return map;
    }
    public static Faction deserialize(Map<String, Object> map, HCFPlugin data) {
        return new Faction(map, data);
    }

}

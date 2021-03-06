package life.steeze.hcfplus.Utilities;

import life.steeze.hcfplus.Exceptions.NotInFaction;
import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.Objects.Selection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {
    public PlayerData(){
        factions = new ArrayList<>();
        fPlayers = new HashMap<>();
        invites = new HashMap<>();
        selections = new HashMap<>();
        pendingTeleports = new HashMap<>();

        // In case of server /reload
        for(Player p : Bukkit.getOnlinePlayers()){
            for (Faction f : getFactions()) {
                if (f.hasMember(p)) addFPlayer(p, f); break;
            }
        }

    }

    /*
    Pending teleports
     */
    private HashMap<Player, Integer> pendingTeleports;
    public HashMap<Player, Integer> getPendingTeleports(){
        return pendingTeleports;
    }

    /*
    Loaded factions
     */
    private ArrayList<Faction> factions;
    public ArrayList<Faction> getFactions(){
        return factions;
    }
    public void addFaction(Faction f){
        factions.add(f);
    }
    public void removeFaction(Faction f){
        factions.remove(f);
        while(invites.values().remove(f));
    }
    public Faction getFaction(String name){
        for(Faction f : factions){
            if(f.getName().equalsIgnoreCase(name)) return f;
        }
        for(Player p : fPlayers.keySet()){
            if(p.getName().equalsIgnoreCase(name)) return getFaction(p);
        }
        return null;
    }
    public Faction getFaction(Player p){
        return fPlayers.get(p);
    }
    public Faction getFaction(OfflinePlayer p){
        if(p.isOnline()) return getFaction((Player) p);
        UUID id = p.getUniqueId();
        for(Faction f : getFactions()){
            if(f.getMembers().contains(id) || f.getLeader().equals(id)) return f;
        }
        return null;
    }
    public Faction getFactionOrError(Player p) throws NotInFaction {
        if(!fPlayers.containsKey(p)){
            p.sendMessage(ConfigManager.NOT_IN_FACTION);
            throw new NotInFaction();
        }
        return fPlayers.get(p);
    }
    public Faction getFactionStrictFacName(String factionName){
        for(Faction f : factions){
            if(f.getName().equalsIgnoreCase(factionName)) return f;
        }
        return null;
    }

    /*
    Online players & Faction association
     */
    private HashMap<Player, Faction> fPlayers;
    public void addFPlayer(Player p, Faction f){
        fPlayers.put(p, f);
    }
    public void removeFPlayer(Player p){
        fPlayers.remove(p);
    }
    public boolean isPlayerInFaction(Player p){
        return fPlayers.containsKey(p);
    }

    /*
    Online players with active invitations
     */
    private HashMap<Player, Faction> invites;
    public boolean hasInvitation(Player p){
        if(invites.containsKey(p)) return true;
        return false;
    }
    public void addInvitation(Player p, Faction f) {invites.put(p, f);}
    public void expireInvitation(Player p) {invites.remove(p);}
    public boolean acceptInvite(Player p){
        if(invites.containsKey(p)){
            if(invites.get(p).getMembers().size() >= ConfigManager.MAX_MEMBERS) return false;
            invites.get(p).addPlayer(p);
            return true;
        }
        return false;
    }

    /*
    Selections
     */
    private HashMap<Player, Selection> selections;
    public HashMap<Player, Selection> getSelections(){
        return selections;
    }
    public Selection getSelection(Player p){
        return selections.get(p);
    }
    public boolean hasSelection(Player p){
        if(selections.containsKey(p)){
            Selection s = selections.get(p);
            if(s.pos1() == null || s.pos2() == null) return false;
            return true;
        }
        return false;
    }

}

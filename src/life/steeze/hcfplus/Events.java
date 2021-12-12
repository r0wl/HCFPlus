package life.steeze.hcfplus;


import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.Objects.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Events implements Listener {
    private HCFPlugin plugin;
    public Events(HCFPlugin plugin){
        this.plugin = plugin;
    }


    boolean isActionLegal(Player player, Location event){
        Faction playerFac = plugin.getData().getFaction(player);
        for(Faction f : plugin.getData().getFactions()){
            if(f.hasClaim()){
                if(f.getClaim().containsLocation(event)){
                    if(f.getDtr() <= 0) return true;
                    return Objects.equals(f, playerFac);
                }
            }
        }
        return true;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for (Faction f : plugin.getData().getFactions()) {
            if (f.hasMember(e.getPlayer())) {
                plugin.getData().addFPlayer(e.getPlayer(), f);
                break;
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        plugin.getData().removeFPlayer(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
            Faction f = plugin.getData().getFaction(e.getEntity());
            if(f == null) return;
            f.loseDtr();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getDamager().hasPermission("hcf.admin")) return;
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player a = (Player) e.getEntity(), b = (Player) e.getDamager();
            if (plugin.getData().getFaction(a).equals(plugin.getData().getFaction(b))) e.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent e){
        if(plugin.getClaimWand().isWand(e.getItemDrop().getItemStack())){
            e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void armorStandChange(PlayerArmorStandManipulateEvent e) {
        if (e.getPlayer().hasPermission("hcf.admin")) return;
        if (e.isCancelled()) return;
        if(!isActionLegal(e.getPlayer(), e.getRightClicked().getLocation())){
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        Player p = e.getPlayer();
        if(plugin.getClaimWand().isWand(e.getItem())) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Selection s = plugin.getData().getSelection(p);
                if (s == null) {
                    Selection selection = new Selection();
                    selection.setPos1(e.getClickedBlock().getLocation());
                    plugin.getData().getSelections().put(p, selection);
                    p.sendMessage(ChatColor.YELLOW + "Position 1 set.");
                    return;
                }
                s.setPos1(e.getClickedBlock().getLocation());
                p.sendMessage(ChatColor.YELLOW + "Position 1 set.");
                return;
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Selection s = plugin.getData().getSelection(p);
                if (s == null) {
                    Selection selection = new Selection();
                    selection.setPos2(e.getClickedBlock().getLocation());
                    plugin.getData().getSelections().put(p, selection);
                    p.sendMessage(ChatColor.YELLOW + "Position 2 set.");
                    return;
                }
                s.setPos2(e.getClickedBlock().getLocation());
                p.sendMessage(ChatColor.YELLOW + "Position 2 set.");
                return;
            }
        }
        if(e.getPlayer().hasPermission("hcf.admin")) return;
        if(!isActionLegal(p, e.getClickedBlock().getLocation())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(e.getPlayer().hasPermission("hcf.admin")) return;
        if(e.isCancelled()) return;
        if(!isActionLegal(e.getPlayer(), e.getBlock().getLocation())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(e.getPlayer().hasPermission("hcf.admin")) return;
        if(e.isCancelled()) return;
        if(!isActionLegal(e.getPlayer(), e.getBlock().getLocation())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        if(e.getPlayer().hasPermission("hcf.admin")) return;
        if(e.isCancelled()) return;
        if(!isActionLegal(e.getPlayer(), e.getBlock().getLocation())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        if(e.getPlayer().hasPermission("hcf.admin")) return;
        if(e.isCancelled()) return;
        if(!isActionLegal(e.getPlayer(), e.getBlock().getLocation())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBonemeal(BlockFertilizeEvent e) {
        if(e.getPlayer().hasPermission("hcf.admin")) return;
        if(e.isCancelled()) return;
        if(!isActionLegal(e.getPlayer(), e.getBlock().getLocation())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent e) {
        if(e.getPlayer().hasPermission("hcf.admin")) return;
        if(e.isCancelled()) return;
        if(!isActionLegal(e.getPlayer(), e.getBlock().getLocation())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (ConfigManager.FORMAT_CHAT) {
            Faction team = plugin.getData().getFaction(e.getPlayer());
            if (team == null){
                String tempnoTeamFormat = ConfigManager.NO_TEAM_FORMATTED_CHAT.replaceAll("\\{name}", "%s");
                e.setFormat(tempnoTeamFormat.replaceAll("\\{message}", "%s"));
                return;
            }
            String tempchatFormat = ConfigManager.FORMATTED_CHAT.replaceAll("\\{name}", "%s");
            tempchatFormat = tempchatFormat.replaceAll("\\{team}", team.getColor() + team.getName());
            e.setFormat(tempchatFormat.replaceAll("\\{message}", "%s"));
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != plugin.getColorGUI()) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        final Player p = (Player) e.getWhoClicked();
        Faction f = plugin.getData().getFaction(p);
        switch (e.getRawSlot()) {
            case 0:
                f.setColor(ChatColor.WHITE);
                break;
            case 1:
                f.setColor(ChatColor.GRAY);
                break;
            case 2:
                f.setColor(ChatColor.DARK_GRAY);
                break;
            case 3:
                f.setColor(ChatColor.DARK_PURPLE);
                break;
            case 4:
                f.setColor(ChatColor.LIGHT_PURPLE);
                break;
            case 5:
                f.setColor(ChatColor.BLUE);
                break;
            case 6:
                f.setColor(ChatColor.AQUA);
                break;
            case 7:
                f.setColor(ChatColor.DARK_AQUA);
                break;
            case 8:
                f.setColor(ChatColor.GREEN);
                break;
            case 9:
                f.setColor(ChatColor.DARK_GREEN);
                break;
            case 10:
                f.setColor(ChatColor.RED);
                break;
            case 11:
                f.setColor(ChatColor.YELLOW);
                break;
            default:
                return;
        }
        p.sendMessage(ChatColor.YELLOW + "Success!");
        p.closeInventory();
    }
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == plugin.getColorGUI()) {
            e.setCancelled(true);
        }
    }

    public void onMobSpawn(CreatureSpawnEvent e) {
        if(ConfigManager.MOB_SPAWN_IN_CLAIMS) return;
        if (!(e.getEntity() instanceof Monster)) return;
        for (Faction f : plugin.getData().getFactions()) {
            if (f.hasClaim()) {
                if (f.getClaim().containsLocation(e.getLocation())) {
                    e.setCancelled(true);
                }
            }
        }
    }

}

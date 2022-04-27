package life.steeze.hcfplus;


import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.Objects.Selection;
import life.steeze.hcfplus.Timers.AbilitiesTimer;
import life.steeze.hcfplus.events.ArcherHitEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Objects;

public class Events implements Listener {
    private HCFPlugin plugin;
    public Events(HCFPlugin plugin){
        this.plugin = plugin;
    }

    boolean isActionLegal(Player player, Location event){
        Faction playerFac = plugin.getData().getFaction(player);
        for(Faction f : plugin.getData().getFactions()){
            if(!f.hasClaim()) continue;
            if(!f.getClaim().containsLocation(event)) continue;
            if(f.getDtr() <= 0 && ConfigManager.ENABLE_RAIDING) return true;
            return Objects.equals(f, playerFac);
        }
        return true;
    }

    @EventHandler
    public void onPearl(ProjectileLaunchEvent e){
        if(!(e.getEntity() instanceof EnderPearl)) return;
        Player p = (Player) e.getEntity().getShooter();
        new BukkitRunnable() {
            @Override
            public void run() {
                p.setCooldown(Material.ENDER_PEARL, (ConfigManager.PEARL_COOLDOWN*20));
            }
        }.runTaskLater(plugin, 1);
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

            //check if user has pending teleport and cancel
            Integer aR = plugin.getData().getPendingTeleports().remove(a), bR = plugin.getData().getPendingTeleports().remove(b);
            if(aR != null) Bukkit.getScheduler().cancelTask(aR);
            if(bR != null) Bukkit.getScheduler().cancelTask(bR);

            Faction fA = plugin.getData().getFaction(a), fB = plugin.getData().getFaction(b);
            if(fA == null || fB == null) return;
            if (fA.equals(fB)) e.setCancelled(true);
        }
        if(!ConfigManager.USE_KITS) return;
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow){
            Arrow arrow = (Arrow) e.getDamager();
            if(!(arrow.getShooter() instanceof Player)) return;
            Player firer = (Player) arrow.getShooter();
            if(AbilitiesTimer.isPlayerWearingKit(firer).equals("archer")){
                Bukkit.getPluginManager().callEvent(new ArcherHitEvent((Player) e.getEntity()));
            }
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
        Block block = e.getClickedBlock();
        Location location = block.getLocation();
        Player p = e.getPlayer();
        if(plugin.getClaimWand().isWand(e.getItem())) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Selection s = plugin.getData().getSelection(p);
                if (s == null) {
                    Selection selection = new Selection();
                    selection.setPos1(location);
                    plugin.getData().getSelections().put(p, selection);
                    p.sendMessage(ChatColor.YELLOW + "Position 1 set.");
                    return;
                }
                s.setPos1(location);
                p.sendMessage(ChatColor.YELLOW + "Position 1 set.");
                return;
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Selection s = plugin.getData().getSelection(p);
                if (s == null) {
                    Selection selection = new Selection();
                    selection.setPos2(location);
                    plugin.getData().getSelections().put(p, selection);
                    p.sendMessage(ChatColor.YELLOW + "Position 2 set.");
                    e.setCancelled(true);
                    return;
                }
                s.setPos2(location);
                p.sendMessage(ChatColor.YELLOW + "Position 2 set.");
                e.setCancelled(true);
                return;
            }
        }
        if(e.getPlayer().hasPermission("hcf.admin")) return;
        if(!isActionLegal(p, location)) {
            e.getPlayer().sendMessage(ChatColor.RED + "Land is claimed");
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DEFAULT);
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
        if(e.getPlayer() == null) return;
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

        //todo map
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

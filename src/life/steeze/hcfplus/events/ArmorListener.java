package life.steeze.hcfplus.events;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Arnah
 * @since Jul 30, 2015
 */
class ArmorListener implements Listener{

	private final List<Material> blockedMaterials;

	public ArmorListener(List<String> blockedMaterialNames){
		this.blockedMaterials = blockedMaterialNames.stream()
				.map(Material::getMaterial)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	//Event Priority is highest because other plugins might cancel the events before we check.

	@EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onClick(final InventoryClickEvent event){
		boolean shift = false, numberkey = false;
		if(event.isCancelled()) return;
		if(event.getAction() == InventoryAction.NOTHING) return;// Why does this get called if nothing happens??
		if(event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)){
			shift = true;
		}
		if(event.getClick().equals(ClickType.NUMBER_KEY)){
			numberkey = true;
		}
		if(event.getSlotType() != SlotType.ARMOR && event.getSlotType() != SlotType.QUICKBAR && event.getSlotType() != SlotType.CONTAINER) return;
		if(event.getClickedInventory() != null && !event.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
		if (!event.getInventory().getType().equals(InventoryType.CRAFTING) && !event.getInventory().getType().equals(InventoryType.PLAYER)) return;
		if(!(event.getWhoClicked() instanceof Player)) return;
		ArmorType newArmorType = ArmorType.matchType(shift ? event.getCurrentItem() : event.getCursor());
		if(!shift && newArmorType != null && event.getRawSlot() != newArmorType.getSlot()){
			// Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
			return;
		}
		if(shift){
			newArmorType = ArmorType.matchType(event.getCurrentItem());
			if(newArmorType != null){
				boolean equipping = true;
				if(event.getRawSlot() == newArmorType.getSlot()){
					equipping = false;
				}
				if(newArmorType.equals(ArmorType.HELMET) && (equipping ? isAirOrNull(event.getWhoClicked().getInventory().getHelmet()) : !isAirOrNull(event.getWhoClicked().getInventory().getHelmet())) || newArmorType.equals(ArmorType.CHESTPLATE) && (equipping ? isAirOrNull(event.getWhoClicked().getInventory().getChestplate()) : !isAirOrNull(event.getWhoClicked().getInventory().getChestplate())) || newArmorType.equals(ArmorType.LEGGINGS) && (equipping ? isAirOrNull(event.getWhoClicked().getInventory().getLeggings()) : !isAirOrNull(event.getWhoClicked().getInventory().getLeggings())) || newArmorType.equals(ArmorType.BOOTS) && (equipping ? isAirOrNull(event.getWhoClicked().getInventory().getBoots()) : !isAirOrNull(event.getWhoClicked().getInventory().getBoots()))){
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : event.getCurrentItem(), equipping ? event.getCurrentItem() : null);
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
					if(armorEquipEvent.isCancelled()){
						event.setCancelled(true);
					}
				}
			}
		}else{
			ItemStack newArmorPiece = event.getCursor();
			ItemStack oldArmorPiece = event.getCurrentItem();
			if(numberkey){
				if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)){// Prevents shit in the 2by2 crafting
					// e.getClickedInventory() == The players inventory
					// e.getHotBarButton() == key people are pressing to equip or unequip the item to or from.
					// e.getRawSlot() == The slot the item is going to.
					// e.getSlot() == Armor slot, can't use e.getRawSlot() as that gives a hotbar slot ;-;
					ItemStack hotbarItem = event.getClickedInventory().getItem(event.getHotbarButton());
					if(!isAirOrNull(hotbarItem)){// Equipping
						newArmorType = ArmorType.matchType(hotbarItem);
						newArmorPiece = hotbarItem;
						oldArmorPiece = event.getClickedInventory().getItem(event.getSlot());
					}else{// Unequipping
						newArmorType = ArmorType.matchType(!isAirOrNull(event.getCurrentItem()) ? event.getCurrentItem() : event.getCursor());
					}
				}
			}else{
				if(isAirOrNull(event.getCursor()) && !isAirOrNull(event.getCurrentItem())){// unequip with no new item going into the slot.
					newArmorType = ArmorType.matchType(event.getCurrentItem());
				}
				// e.getCurrentItem() == Unequip
				// e.getCursor() == Equip
				// newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
			}
			if(newArmorType != null && event.getRawSlot() == newArmorType.getSlot()){
				ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
				if(event.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
				if(armorEquipEvent.isCancelled()){
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority =  EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent event){
		if(event.useItemInHand().equals(Result.DENY))return;

		if(event.getAction() == Action.PHYSICAL) return;
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Player player = event.getPlayer();
			if(!event.useInteractedBlock().equals(Result.DENY)){
				if(event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()){// Having both of these checks is useless, might as well do it though.
					// Some blocks have actions when you right click them which stops the client from equipping the armor in hand.
					Material mat = event.getClickedBlock().getType();
					if(blockedMaterials.contains(mat)) return;
				}
			}
			ArmorType newArmorType = ArmorType.matchType(event.getItem());
			// Carved pumpkins cannot be equipped using right-click
			if(event.getItem() != null && event.getItem().getType() == Material.CARVED_PUMPKIN) return;

			if(newArmorType != null){
				if(newArmorType.equals(ArmorType.HELMET) && isAirOrNull(event.getPlayer().getInventory().getHelmet()) || newArmorType.equals(ArmorType.CHESTPLATE) && isAirOrNull(event.getPlayer().getInventory().getChestplate()) || newArmorType.equals(ArmorType.LEGGINGS) && isAirOrNull(event.getPlayer().getInventory().getLeggings()) || newArmorType.equals(ArmorType.BOOTS) && isAirOrNull(event.getPlayer().getInventory().getBoots())){
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(event.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(event.getItem()), null, event.getItem());
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
					if(armorEquipEvent.isCancelled()){
						event.setCancelled(true);
						player.updateInventory();
					}
				}
			}
		}
	}
	
	@EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDrag(InventoryDragEvent event){
		// getType() seems to always be even.
		// Old Cursor gives the item you are equipping
		// Raw slot is the ArmorType slot
		// Can't replace armor using this method making getCursor() useless.
		ArmorType type = ArmorType.matchType(event.getOldCursor());
		if(event.getRawSlots().isEmpty()) return;// Idk if this will ever happen
		if(type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)){
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, null, event.getOldCursor());
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			if(armorEquipEvent.isCancelled()){
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBreak(PlayerItemBreakEvent event){
		ArmorType type = ArmorType.matchType(event.getBrokenItem());
		if(type != null){
			Player p = event.getPlayer();
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, event.getBrokenItem(), null);
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		if(event.getKeepInventory()) return;
		for(ItemStack i : p.getInventory().getArmorContents()){
			if(!isAirOrNull(i)){
				Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
				// No way to cancel a death event.
			}
		}
	}

	/**
	 * A utility method to support versions that use null or air ItemStacks.
	 */
	public static boolean isAirOrNull(ItemStack item){
		return item == null || item.getType().equals(Material.AIR);
	}
}

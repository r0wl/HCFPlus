package life.steeze.hcfplus.Listeners;

import life.steeze.hcfplus.FileUtils.ConfigManager;
import life.steeze.hcfplus.HCFPlugin;
import life.steeze.hcfplus.Objects.Claim;
import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.events.MoveBlockEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnteredClaim implements Listener {
    private final HCFPlugin plugin;
    private Map<UUID, Long> lastProcessedTime; // Map to track the last processed time for each player
    private Map<UUID, Claim> playerClaims; // Map to track which claim each player is in
    private final long throttleInterval = 500; // Throttle interval in milliseconds

    public EnteredClaim(HCFPlugin plugin) {
        this.plugin = plugin;
        this.lastProcessedTime = new HashMap<>();
        this.playerClaims = new HashMap<>();
    }

    @EventHandler
    public void movedBlock(MoveBlockEvent e) {
        UUID playerUUID = e.getPlayer().getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Get the last processed time for this player
        Long lastTime = lastProcessedTime.get(playerUUID);

        // If the last processed time is not null and the difference is less than the throttle interval, skip processing
        if (lastTime != null && (currentTime - lastTime) < throttleInterval) {
            return;
        }

        // Update the last processed time for this player
        lastProcessedTime.put(playerUUID, currentTime);

        // Get the player's current claim
        Claim currentClaim = playerClaims.get(playerUUID);
        Location to = e.getLocation();
        Claim newClaim = null;
        Faction newClaimFac = null;

        // Check if the player has entered a new claim
        for (Faction f : plugin.getData().getFactions()) {
            if (!f.hasClaim()) continue;
            Claim claim = f.getClaim();
            if (claim.containsLocation(to)) {
                newClaim = claim;
                newClaimFac = f;
                break;
            }
        }

        // If the player has moved to a new claim
        if (newClaim != null && !newClaim.equals(currentClaim)) {
            e.getPlayer().sendMessage(ConfigManager.ENTERING_MESSAGE.replaceAll("\\{faction}", newClaimFac.getColor() + newClaimFac.getName()));
            playerClaims.put(playerUUID, newClaim);
            newClaim.showBounds(e.getPlayer());
        } else if (newClaim == null && currentClaim != null) {
            // The player has left the current claim and is now in the wilderness
            playerClaims.remove(playerUUID);
        }
    }
}

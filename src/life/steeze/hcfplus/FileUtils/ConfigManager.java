package life.steeze.hcfplus.FileUtils;

import org.bukkit.configuration.file.FileConfiguration;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class ConfigManager {

    public static void loadConfig(final FileConfiguration config){

        MAX_TEAM_NAME = config.getInt("max-team-name");
        MAX_CLAIM_DISTANCE = config.getInt("max-claim-corner-distance");
        MIN_CLAIM_WIDTH = config.getInt("min-claim-width");
        MAX_DESCRIPTION_LENGTH = config.getInt("max-description-length");
        MAX_MEMBERS = config.getInt("max-members");
        DTR_REGEN = config.getInt("dtr-regen");
        MINIMUM_DTR = config.getInt("minimum-dtr");
        MAXIMUM_DTR = config.getInt("maximum-dtr");
        PEARL_COOLDOWN = config.getInt("enderpearl-cooldown");
        TELEPORT_DELAY = config.getLong("teleport-delay") * 20;
        ARCHER_TAG_LENGTH = config.getLong("archer-tag-length") * 20;


        FORMATTED_CHAT = translateAlternateColorCodes('&', config.getString("formatted-chat"));
        NO_TEAM_FORMATTED_CHAT = translateAlternateColorCodes('&', config.getString("no-team-formatted-chat"));

        NOT_IN_FACTION = translateAlternateColorCodes('&', config.getString("msg-must-be-in-faction"));
        SUCCESS = translateAlternateColorCodes('&', config.getString("msg-success"));
        MUST_BE_LEADER = translateAlternateColorCodes('&', config.getString("msg-must-be-leader"));
        PLAYER_NOT_FOUND = translateAlternateColorCodes('&', config.getString("msg-player-not-found"));
        NO_INVITE = translateAlternateColorCodes('&', config.getString("msg-no-invite"));
        ALREADY_HAS_CLAIM = translateAlternateColorCodes('&', config.getString("already-has-claim"));
        TRIED_CLAIM_NO_SELECTION = translateAlternateColorCodes('&', config.getString("tried-claim-no-selection"));
        CLAIM_TOO_BIG = translateAlternateColorCodes('&', config.getString("claim-too-big"));
        CLAIM_TOO_NARROW = translateAlternateColorCodes('&', config.getString("claim-too-small-or-narrow"));
        LAND_ALREADY_CLAIMED = translateAlternateColorCodes('&', config.getString("land-already-claimed"));
        MUST_HAVE_CLAIM = translateAlternateColorCodes('&', config.getString("must-have-claim"));
        HOME_MUST_BE_IN_CLAIM = translateAlternateColorCodes('&', config.getString("home-must-be-in-claim"));
        ALREADY_IN_FACTION = translateAlternateColorCodes('&', config.getString("already-in-faction"));
        NAME_TOO_LONG = translateAlternateColorCodes('&', config.getString("name-too-long"));
        NAME_TAKEN = translateAlternateColorCodes('&', config.getString("name-taken"));
        INVALID_NAME = translateAlternateColorCodes('&', config.getString("invalid-name"));
        FACTION_FOUNDED = translateAlternateColorCodes('&', config.getString("faction-founded"));
        TELEPORT_PENDING = translateAlternateColorCodes('&', config.getString("teleport-pending").replaceAll("\\{seconds}", Long.toString((TELEPORT_DELAY/20))));
        NOT_A_PLAYER_OR_FACTION = translateAlternateColorCodes('&', config.getString("not-a-player-or-faction"));

        FORMAT_CHAT = config.getBoolean("format-chat");
        MOB_SPAWN_IN_CLAIMS = config.getBoolean("mob-spawn-in-claims");
        ENABLE_RAIDING = config.getBoolean("enable-raiding");
        SHOW_COORDS_IN_INFO = config.getBoolean("show-coords-in-info");
        SHOW_COLOR_IN_PLACEHOLDER = config.getBoolean("show-color-in-placeholder");
        USE_KITS = config.getBoolean("use-kits");
        USE_TP_DELAY = config.getBoolean("use-tp-delay");

    }

    /*
    Configurable things
     */
    public static int MAXIMUM_DTR;
    public static int MAX_TEAM_NAME;
    public static int MAX_MEMBERS;
    public static int MAX_CLAIM_DISTANCE;
    public static int MIN_CLAIM_WIDTH;
    public static int MAX_DESCRIPTION_LENGTH;
    public static int DTR_REGEN;
    public static int MINIMUM_DTR;
    public static int PEARL_COOLDOWN;
    public static long ARCHER_TAG_LENGTH;
    public static long TELEPORT_DELAY;
    public static boolean FORMAT_CHAT;
    public static boolean USE_KITS;
    public static boolean ENABLE_RAIDING;
    public static boolean SHOW_COORDS_IN_INFO;
    public static boolean SHOW_COLOR_IN_PLACEHOLDER;
    public static boolean USE_TP_DELAY;
    public static boolean MOB_SPAWN_IN_CLAIMS;
    public static String FORMATTED_CHAT;
    public static String NO_TEAM_FORMATTED_CHAT;
    public static String NOT_IN_FACTION;
    public static String SUCCESS;
    public static String MUST_BE_LEADER;
    public static String PLAYER_NOT_FOUND;
    public static String NOT_A_PLAYER_OR_FACTION;
    public static String NO_INVITE;
    public static String ALREADY_HAS_CLAIM;
    public static String TRIED_CLAIM_NO_SELECTION;
    public static String CLAIM_TOO_BIG;
    public static String CLAIM_TOO_NARROW;
    public static String LAND_ALREADY_CLAIMED;
    public static String MUST_HAVE_CLAIM;
    public static String HOME_MUST_BE_IN_CLAIM;
    public static String ALREADY_IN_FACTION;
    public static String NAME_TOO_LONG;
    public static String NAME_TAKEN;
    public static String INVALID_NAME;
    public static String FACTION_FOUNDED;
    public static String TELEPORT_PENDING;
}
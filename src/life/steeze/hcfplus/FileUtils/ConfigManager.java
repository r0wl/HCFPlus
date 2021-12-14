package life.steeze.hcfplus.FileUtils;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    public static void loadConfig(final FileConfiguration config){
        config.options().copyDefaults(true);
        MAX_TEAM_NAME = config.getInt("max-team-name");
        MAX_CLAIM_DISTANCE = config.getInt("max-claim-corner-distance");
        MIN_CLAIM_WIDTH = config.getInt("min-claim-width");
        MAX_DESCRIPTION_LENGTH = config.getInt("max-description-length");
        MAX_MEMBERS = config.getInt("max-team-size");
        DTR_REGEN = config.getInt("dtr-regen");
        MINIMUM_DTR = config.getInt("minimum-dtr");
        FORMAT_CHAT = config.getBoolean("format-chat");
        FORMATTED_CHAT = config.getString("formatted-chat");
        NO_TEAM_FORMATTED_CHAT = config.getString("no-team-formatted-chat");
        MOB_SPAWN_IN_CLAIMS = config.getBoolean("mob-spawn-in-claims");
        USE_KITS = config.getBoolean("use-kits");
        NOT_IN_FACTION = config.getString("msg-must-be-in-faction");
        SUCCESS = config.getString("msg-success");
        MUST_BE_LEADER = config.getString("msg-must-be-leader");
        PLAYER_NOT_FOUND = config.getString("msg-player-not-found");
        NO_INVITE = config.getString("msg-no-invite");
        ENABLE_RAIDING = config.getBoolean("enable-raiding");

    }

    /*
    Configurable things
     */
    public static int MAX_TEAM_NAME;
    public static int MAX_MEMBERS;
    public static int MAX_CLAIM_DISTANCE;
    public static int MIN_CLAIM_WIDTH;
    public static int MAX_DESCRIPTION_LENGTH;
    public static int DTR_REGEN;
    public static int MINIMUM_DTR;
    public static boolean FORMAT_CHAT;
    public static boolean USE_KITS;
    public static boolean ENABLE_RAIDING;
    public static String FORMATTED_CHAT;
    public static String NO_TEAM_FORMATTED_CHAT;
    public static boolean MOB_SPAWN_IN_CLAIMS;
    public static String NOT_IN_FACTION;
    public static String SUCCESS;
    public static String MUST_BE_LEADER;
    public static String PLAYER_NOT_FOUND;
    public static String NO_INVITE;
}

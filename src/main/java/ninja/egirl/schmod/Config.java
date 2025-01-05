package ninja.egirl.schmod;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class Config extends Vigilant {
    public static Config INSTANCE = new Config();

    @Property(
            type = PropertyType.SWITCH,
            name = "Auto Kick",
            description = "kicks blacklisted players on party join",
            category = "General",
            subcategory = "Auto Kick"
    )
    public boolean autoKickEnabled = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Send Party Chat",
            description = "on kick, send a custom message in party chat",
            category = "General",
            subcategory = "Auto Kick"
    )
    public boolean autoKickPartyChat = false;

    @Property(
            type = PropertyType.TEXT,
            name = "Party Chat Message",
            description = "message sent to party chat on kick",
            category = "General",
            subcategory = "Auto Kick"
    )
    public String autoKickPartyChatMessage = "";

    @Property(
            type = PropertyType.SWITCH,
            name = "Party Finder Warning",
            description = "requires a double click to join parties with a blacklisted player",
            category = "General",
            subcategory = "Party Finder"
    )
    public boolean pfWarning = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Use Whitelist",
            description = "ignores auto-kick if the player who joins is on your personal whitelist\n&7(to control your whitelist, &b/schmod whitelist&7)",
            category = "General",
            subcategory = "Whitelist"
    )
    public boolean whitelistEnabled = false;

    public Config() {
        super(new File("./config/schmod/config.toml"), "Schmod");
        initialize();

        addDependency("autoKickPartyChatMessage", "autoKickPartyChat");

        setCategoryDescription("General", "&9&lSchmod &7for the &3&lSchmeme&f&lShack\n\n\n&7for any bugs or questions, contact &b&lbrendan#8535 &7on Discord");
    }
}

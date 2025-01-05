package ninja.egirl.schmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ninja.egirl.schmod.commands.MainCommand;
import ninja.egirl.schmod.features.AutoKick;
import ninja.egirl.schmod.features.PartyFinder;
import ninja.egirl.schmod.managers.BlacklistManager;
import ninja.egirl.schmod.managers.WhitelistManager;
import ninja.egirl.schmod.runnables.UpdateBlacklistTask;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod(modid = "schmod", name = "Schmod", version = "1.0.0")
public class Schmod {

    public GuiScreen display = null;
    public final Minecraft mc = Minecraft.getMinecraft();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private static File whitelist;

    public Config config = Config.INSTANCE;

    // managers
    private BlacklistManager blacklistManager;
    private WhitelistManager whitelistManager;

    @Mod.EventHandler
    public void onFMLInitialization(FMLPreInitializationEvent event) {
        File dirFile = new File(event.getModConfigurationDirectory(), "schmod");
        whitelist = new File(dirFile, "whitelist.json");
        if (!dirFile.exists()) {
            dirFile.mkdirs();

            if (!whitelist.exists()) {
                whitelist.mkdirs();
            }
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        // register managers
        this.blacklistManager = new BlacklistManager(this);
        this.whitelistManager = new WhitelistManager(this);


        // initialize configs
        config.initialize();
        whitelistManager.init();

        // register listeners
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AutoKick(this));
        MinecraftForge.EVENT_BUS.register(new PartyFinder(this));

        // commands
        ClientCommandHandler.instance.registerCommand(new MainCommand(this));

        // setup update list task
        scheduler.scheduleWithFixedDelay(new UpdateBlacklistTask(this), 0, 300, TimeUnit.SECONDS);
    }

    @Mod.EventHandler
    public void post(FMLPostInitializationEvent event) {
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {

        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if (display != null) {
            try {
                mc.displayGuiScreen(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
            display = null;
        }
    }

    public BlacklistManager getBlacklistManager() {
        return blacklistManager;
    }

    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    public static JsonElement getJson(String jsonUrl) {
        return (new JsonParser()).parse(getInputStream(jsonUrl));
    }

    public static InputStreamReader getInputStream(String url) {
        try {
            URLConnection conn = (new URL(url)).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            return new InputStreamReader(conn.getInputStream());
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static File getWhitelistFile() {
        return whitelist;
    }
}
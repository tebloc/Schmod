package ninja.egirl.schmod.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.events.ClickSlotEvent;
import ninja.egirl.schmod.utils.Utils;

public class PartyFinder {

    private final Schmod instance;

    private long lastClick = 0;

    public PartyFinder(Schmod instance) {
        this.instance = instance;
    }

    @SubscribeEvent
    public void onGuiClick(ClickSlotEvent event) {

        try {

            if (!instance.config.pfWarning) {
                return;
            }

            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
                return;
            }

            if (!getGuiName(Minecraft.getMinecraft().currentScreen).equalsIgnoreCase("party finder")) {
                return;
            }

            if (!event.item.getItem().getRegistryName().equalsIgnoreCase("minecraft:skull")) {
                return;
            }

            for (String loreLine : Utils.getLore(event.item)) {
                for (String ign : instance.getBlacklistManager().getBlacklistMap().keySet()) {

                    if (loreLine.contains(ign)) {
                        if (System.currentTimeMillis() - lastClick > 2000) {

                            event.setCanceled(true);
                            lastClick = System.currentTimeMillis();
                            Utils.sendMessage("&7Double click within 2 seconds to bypass &c(blacklisted player in party)");
                        }
                    }
                }
            }

        } catch (Exception e) {
            Utils.sendMessage("&cSomething went wrong. Check your logs");
            e.printStackTrace();
        }
    }

    private String getGuiName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest) ((GuiChest) gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }
}

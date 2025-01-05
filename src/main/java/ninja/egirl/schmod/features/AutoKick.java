package ninja.egirl.schmod.features;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.utils.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoKick {

    private final Schmod instance;

    public AutoKick(Schmod instance) {
        this.instance = instance;
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {

        new Thread(() -> {

            String unformatted = event.message.getUnformattedText();

            if (unformatted.endsWith("joined the party.")) {

                if (!instance.config.autoKickEnabled) {
                    return;
                }

                String player = Utils.striprank(unformatted).split(" ",2)[0].trim();

                if (instance.getBlacklistManager().getBlacklistMap().containsKey(player)) {
                    if (instance.getWhitelistManager().getWhitelist().contains(Utils.getMcPlayer(player).get("id").getAsString()) && instance.config.whitelistEnabled) {
                        Utils.sendMessage(String.format("&b%s &7is on the blacklist, but was detected in your whitelist and allowed in", player));
                        return;
                    }

                    kickPlayer(player, instance.config.autoKickPartyChat);
                }
            }

            if (unformatted.contains("joined the dungeon group!")) {

                if (!instance.config.autoKickEnabled) {
                    return;
                }

                String player = unformatted.replace("Dungeon Finder > ", "").split(" ", 2)[0].trim();

                if (instance.getBlacklistManager().getBlacklistMap().containsKey(player)) {
                    if (instance.getWhitelistManager().getWhitelist().contains(Utils.getMcPlayer(player).get("id").getAsString()) && instance.config.whitelistEnabled) {
                        Utils.sendMessage(String.format("&b%s &7is on the blacklist, but was detected in your whitelist and allowed in", player));
                        return;
                    }

                    kickPlayer(player, instance.config.autoKickPartyChat);
                }
            }
        }).start();
    }

    private void kickPlayer(String player, boolean sendMessage) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/party kick " + player);
        scheduler.schedule(() -> {
            Utils.sendMessage(String.format("&a%s &7was beamed by the Shack.", player));
        }, 300, TimeUnit.MILLISECONDS);

        if (sendMessage) {
            scheduler.schedule(() -> {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/party chat " + instance.config.autoKickPartyChatMessage.replace("{player}", player));
            }, 250, TimeUnit.MILLISECONDS);
        }
    }
}

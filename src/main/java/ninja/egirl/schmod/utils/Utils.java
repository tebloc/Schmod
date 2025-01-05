package ninja.egirl.schmod.utils;

import com.google.gson.JsonObject;
import gg.essential.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import ninja.egirl.schmod.Schmod;

import java.util.List;

public class Utils {

    public static String prefix = "&9&lSchmod &8»&r ";

    public static void sendMessage(String message) {
        UChat.chat(prefix + message);
    }

    public static JsonObject getMcPlayer(String player) {

        if (player.length() > 16) {
            try {
                return Schmod.getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + player).getAsJsonObject();
            } catch (Exception e) {
                return null;
            }
        }

        try {
            return Schmod.getJson("https://api.mojang.com/users/profiles/minecraft/" + player).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static String striprank(String player) {
        return player.replaceAll("\\[(.*?)\\]", "").trim();
    }

    public static List<String> getLore(ItemStack item) {
        if (item != null) {
            return item.getTooltip(Minecraft.getMinecraft().thePlayer, false);
        }
        return null;
    }
}

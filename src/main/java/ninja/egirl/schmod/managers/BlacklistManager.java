package ninja.egirl.schmod.managers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.essential.universal.UChat;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.utils.Utils;

import java.util.*;

public class BlacklistManager {

    private final Schmod instance;
    private final SortedMap<String, String> blacklist;

    public BlacklistManager(Schmod instance) {
        this.instance = instance;

        this.blacklist = new TreeMap<>();
    }

    public void update() {
        try {
            blacklist.clear();
            Thread.sleep(50L);

            for (Map.Entry<String, JsonElement> entry : getBlacklist().entrySet()) {
                if (entry.getKey() == null) {
                    continue;
                }

                blacklist.put(Utils.getMcPlayer(entry.getKey()).get("name").getAsString(), entry.getValue().getAsJsonObject().get("reason").getAsString());
            }

        } catch (Exception e) {
            Utils.sendMessage("&cSomething went wrong while updating the blacklist. Check your logs");
            e.printStackTrace();
        }
    }

    public void paginateBlacklist(int page) {

        if (page > (Math.ceil(blacklist.size() / 5.0)) || page <= 0) {
            Utils.sendMessage(String.format("&7page range must be &b1-%s", (int) Math.ceil(blacklist.entrySet().size() / 5.0)));
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("&8&l&m---------------------------------------------------\n");
        sb.append("&9&lSchmod &c&lB&6&ll&e&la&a&lc&b&lk&5&ll&d&li&b&ls&5&lt &7(Page " + page + "/" + ((int) Math.ceil(blacklist.entrySet().size() / 5.0)) + ")\n\n");
        getPageSublist(page).forEach(n -> sb.append("&b" + n.getKey() + " &8» &7" + n.getValue() + "\n"));
        sb.append("\n&7Next Page &8» &e/schmod list " + (page + 1) + "\n");
        sb.append("&8&l&m---------------------------------------------------");
        UChat.chat(sb.toString());
    }

    public SortedMap<String, String> getBlacklistMap() {
        return blacklist;
    }

    private JsonObject getBlacklist() {
        return instance.getJson("https://raw.githubusercontent.com/SchmemeShack/ShackData/main/blacklistData.json").getAsJsonObject();
    }

    private List<Map.Entry<String, String>> getPageSublist(int page) {
        try {
            return new ArrayList<>(blacklist.entrySet()).subList((page - 1) * 5, page * 5);
        } catch (IndexOutOfBoundsException e) {
            return new ArrayList<>(blacklist.entrySet()).subList((page - 1) * 5, blacklist.size());
        }
    }
}

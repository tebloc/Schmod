package ninja.egirl.schmod.managers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class WhitelistManager {

    private final Schmod instance;
    private final List<String> whitelist;

    public WhitelistManager(Schmod instance) {
        this.instance = instance;

        this.whitelist = new ArrayList<>();
    }

    public void init() {
        try {
            for (JsonElement s : parseWhitelist()) {
                whitelist.add(s.getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(String name) {
        String uuid = Utils.getMcPlayer(name).get("id").getAsString();
        if (!whitelist.contains(uuid)) {
            whitelist.add(uuid);
            updateWhitelist();
            Utils.sendMessage(String.format("&b%s &7was &aadded &7to your whitelist", Utils.getMcPlayer(name).get("name").getAsString()));
            return;
        }

        Utils.sendMessage(String.format("&c%s &7is already on your whitelist", Utils.getMcPlayer(name).get("name").getAsString()));
    }

    public void removePlayer(String name) {
        String uuid = Utils.getMcPlayer(name).get("id").getAsString();
        if (whitelist.contains(uuid)) {
            whitelist.remove(uuid);
            updateWhitelist();
            Utils.sendMessage(String.format("&b%s &7was &cremoved &7from your whitelist", Utils.getMcPlayer(name).get("name").getAsString()));
            return;
        }
        Utils.sendMessage(String.format("&c%s &7is not on your whitelist", Utils.getMcPlayer(name).get("name").getAsString()));
    }

    public void clearWhitelist() {
        whitelist.clear();
        updateWhitelist();
        Utils.sendMessage("&7Whitelist was cleared");
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    private void updateWhitelist() {
        try {
            Writer writer = new FileWriter(Schmod.getWhitelistFile());
            new Gson().toJson(whitelist, writer);
            writer.close();

        } catch (Exception ex) {
            Utils.sendMessage("&cSomething went wrong. Check your logs");
            ex.printStackTrace();
        }
    }

    private JsonArray parseWhitelist() {

        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(Schmod.getWhitelistFile()));
            return jsonElement.getAsJsonArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
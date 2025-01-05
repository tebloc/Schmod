package ninja.egirl.schmod.commands.subcommands;

import net.minecraft.command.ICommandSender;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.commands.AbstractCommand;
import ninja.egirl.schmod.utils.Utils;

import java.util.List;

public class CheckCommand extends AbstractCommand {

    public CheckCommand(Schmod instance) {
        super(instance);
    }

    @Override
    public void execute(ICommandSender sender, List<String> args) {
        new Thread(() -> {

            if (args.size() < 1) {
                sendMessage("&c/schmod check <player>");
                return;
            }

            if (Utils.getMcPlayer(args.get(0)) == null) {
                sendMessage("&cCouldn't find a player with that IGN");
                return;
            }

            String player = Utils.getMcPlayer(args.get(0)).get("name").getAsString();

            if (instance.getBlacklistManager().getBlacklistMap().containsKey(player)) {
                sendMessage("&b" + player + " &7found in blacklist &c(" + instance.getBlacklistManager().getBlacklistMap().get(player) + ")");
                return;
            }

            sendMessage("&b" + player + " &7was not found in the blacklist");
        }).start();
    }
}

package ninja.egirl.schmod.commands.subcommands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.commands.AbstractCommand;
import ninja.egirl.schmod.utils.Utils;

import java.util.List;

public class WhitelistCommand extends AbstractCommand {

    public WhitelistCommand(Schmod instance) {
        super(instance);
    }
    @Override
    public void execute(ICommandSender sender, List<String> args) throws CommandException {

        if (args.size() == 0) {
            sendMessage("&c/schmod whitelist <add/remove/clear>");
            return;
        }

        try {
            new Thread(() -> {

                switch (args.get(0).toLowerCase()) {

                    case "add":
                        if (args.size() != 2) {
                           sendMessage("&c/schmod whitelist add <player>");
                           break;
                        }

                        if (Utils.getMcPlayer(args.get(1)) == null) {
                            sendMessage("&cCouldn't find a player with that IGN");
                            break;
                        }

                        instance.getWhitelistManager().addPlayer(args.get(1));
                        break;

                    case "remove":
                        if (args.size() != 2) {
                            sendMessage("&c/schmod whitelist remove <player>");
                            break;
                        }

                        if (Utils.getMcPlayer(args.get(1)) == null) {
                            sendMessage("&cCouldn't find a player with that IGN");
                            break;
                        }

                        instance.getWhitelistManager().removePlayer(args.get(1));
                        break;

                    case "clear":
                        instance.getWhitelistManager().clearWhitelist();
                        break;

                    default:
                        sendMessage("&c/schmod whitelist <add/remove/clear>");
                }
            }).start();
        } catch (Exception e) {
            Utils.sendMessage("&cSomething went wrong. Check your logs");
            e.printStackTrace();
        }
    }
}

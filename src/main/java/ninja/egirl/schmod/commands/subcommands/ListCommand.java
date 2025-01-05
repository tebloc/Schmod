package ninja.egirl.schmod.commands.subcommands;

import net.minecraft.command.ICommandSender;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.commands.AbstractCommand;

import java.util.List;

public class ListCommand extends AbstractCommand {

    public ListCommand(Schmod instance) {
        super(instance);
    }

    @Override
    public void execute(ICommandSender sender, List<String> args) {

        if (args.size() == 0) {
            sendMessage("&c/schmod list <page>");
            return;
        }

        if (!isInteger(args.get(0))) {
            sendMessage("&c/schmod list <page>");
            return;
        }

        instance.getBlacklistManager().paginateBlacklist(Integer.parseInt(args.get(0)));
    }

    private boolean isInteger(String num) {
        if (num == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(num);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

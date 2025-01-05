package ninja.egirl.schmod.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.commands.subcommands.CheckCommand;
import ninja.egirl.schmod.commands.subcommands.ListCommand;
import ninja.egirl.schmod.commands.subcommands.WhitelistCommand;
import ninja.egirl.schmod.utils.Utils;

import java.util.*;

public class MainCommand extends CommandBase {

    private final Schmod instance;
    private final Map<String, AbstractCommand> subCommands;

    public MainCommand(Schmod instance) {

        this.instance = instance;

        this.subCommands = new HashMap<>();
        this.subCommands.put("check", new CheckCommand(instance));
        this.subCommands.put("list", new ListCommand(instance));
        this.subCommands.put("whitelist", new WhitelistCommand(instance));
    }

    @Override
    public String getCommandName() {
        return "schmod";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList(Arrays.asList("schmodule", "sch", "schmeme"));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 0) {
            instance.display = instance.config.gui();
            return;
        }

        List<String> params = new ArrayList<>(Arrays.asList(args));
        String commandKey = params.remove(0);
        AbstractCommand toExecute = subCommands.get(commandKey);

        // sub-commands
        if (toExecute == null) {
            Utils.sendMessage("&c/schmod <check/whitelist/list>");
            return;
        }

        toExecute.execute(sender, params);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}

package ninja.egirl.schmod.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import ninja.egirl.schmod.Schmod;
import ninja.egirl.schmod.utils.Utils;

import java.util.List;

public abstract class AbstractCommand {

    protected final Schmod instance;

    public AbstractCommand(Schmod instance) {
        this.instance = instance;
    }

    public abstract void execute(ICommandSender sender, List<String> args) throws CommandException;

    public void sendMessage(String message) {
        Utils.sendMessage(message);
    }
}

package ninja.egirl.schmod.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ClickSlotEvent extends Event {
    public ItemStack item;

    public ClickSlotEvent(ItemStack item) {
        this.item = item;
    }
}

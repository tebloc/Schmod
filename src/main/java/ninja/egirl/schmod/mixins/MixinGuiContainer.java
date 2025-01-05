package ninja.egirl.schmod.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import ninja.egirl.schmod.events.ClickSlotEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer {

    @Inject(method = "handleMouseClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;windowClick(IIIILnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"), cancellable = true)
    private void onMouseClick(Slot slot, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        try {
            if (slot != null && slot.getStack() != null) {

                ItemStack itemStack = slot.getStack();
                if (MinecraftForge.EVENT_BUS.post(new ClickSlotEvent(itemStack))) {
                    ci.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
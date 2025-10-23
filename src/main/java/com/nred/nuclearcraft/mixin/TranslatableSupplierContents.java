package com.nred.nuclearcraft.mixin;

import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(TranslatableContents.class)
public class TranslatableSupplierContents { // Allows Suppliers to be passed to TranslatableContents
    @Final
    @Shadow
    private Object[] args;

    @Inject(at = @At("RETURN"), method = "isAllowedPrimitiveArgument(Ljava/lang/Object;)Z", cancellable = true)
    private static void allowSupplier(Object input, CallbackInfoReturnable<Boolean> rtn) {
        rtn.setReturnValue(rtn.getReturnValue() || input instanceof Supplier<?>);
    }

    @Inject(at = @At(value = "RETURN"), method = "getArgument(I)Lnet/minecraft/network/chat/FormattedText;", cancellable = true)
    public void resolve(int index, CallbackInfoReturnable<FormattedText> rtn) {
        Object object = this.args[index];
        if (object instanceof Supplier<?> supplier) {
            rtn.setReturnValue(FormattedText.of(supplier.get().toString()));
        }
    }
}
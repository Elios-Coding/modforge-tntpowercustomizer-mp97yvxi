package com.modforge.tntpowercustomizer.mixin;

import com.modforge.tntpowercustomizer.TntpowercustomizerMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class TntpowercustomizerModMixin {
    private static final float MULTIPLIER = 2.0f;

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modforge_multiplyDamage(float amount, DamageSource source) {
        try {
            final double m = TntpowercustomizerMod.TNT_MULTIPLIER;
            if (m == 1.0d) return amount;
            if (m <= 0.0d) return 0.0f;
            final double scaled = amount * m;
            if (scaled > Float.MAX_VALUE) return Float.MAX_VALUE;
            return (float) scaled;
        } catch (Throwable t) {
            org.slf4j.LoggerFactory.getLogger("tntpowercustomizer").error("ModForge: failed to scale explosion damage", t);
            return amount;
        }
    }
}

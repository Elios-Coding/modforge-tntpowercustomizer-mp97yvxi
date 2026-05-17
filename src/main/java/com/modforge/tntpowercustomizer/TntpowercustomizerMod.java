package com.modforge.tntpowercustomizer;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TntpowercustomizerMod implements ModInitializer {
    public static final String MOD_ID = "tntpowercustomizer";
    private static final Logger LOGGER = LoggerFactory.getLogger("tntpowercustomizer");

    /** Default multiplier is 1.0 (vanilla). Read by the mixin at runtime. */
    public static volatile double TNT_MULTIPLIER = 1.0d;

    @Override
    public void onInitialize() {
        try {
        LOGGER.info("ModForge: {} loaded. Default TNT multiplier = {}", MOD_ID, TNT_MULTIPLIER);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> {
            try {
                dispatcher.register(literal("tntpower")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(argument("multiplier", DoubleArgumentType.doubleArg(0.0d))
                                .executes(ctx -> {
                                    final double value = DoubleArgumentType.getDouble(ctx, "multiplier");
                                    TNT_MULTIPLIER = value;
                                    // Use the captured final value to avoid any lambda-capture hazards.
                                    ctx.getSource().sendFeedback(() -> Text.literal("Global TNT multiplier set to " + value), true);
                                    return 1;
                                }))
                        .executes(ctx -> {
                            ctx.getSource().sendFeedback(() -> Text.literal("usage: /tntpower <multiplier>"), false);
                            return 1;
                        }));
            } catch (Throwable t) {
                LOGGER.error("ModForge: failed to register /tntpower", t);
            }
        });
    
        } catch (Throwable __modforge_t) {
            LOGGER.error("ModForge: onInitialize failed", __modforge_t);
        }
    }
}

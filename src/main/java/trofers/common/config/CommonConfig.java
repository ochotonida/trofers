package trofers.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import trofers.Trofers;

public class CommonConfig {

    public final ForgeConfigSpec.DoubleValue trophyChance;
    public final ForgeConfigSpec.BooleanValue enableTrophyLoot;
    public final ForgeConfigSpec.BooleanValue enableTrophyEffects;

    protected CommonConfig(ForgeConfigSpec.Builder builder) {
        trophyChance = builder
                .comment(
                        "The chance a non-boss entity from a supported mod drops a trophy when killed by a player",
                        "No trophies will drop from entities when this value is set to 0"
                )
                .translation(String.format("config.%s.trophy_chance", Trofers.MODID))
                .defineInRange("trophy_chance", 0.001, 0, 1);
        enableTrophyLoot = builder
                .comment("Whether trophies can drop loot when right-clicked")
                .translation(String.format("config.%s.enable_trophy_loot", Trofers.MODID))
                .define("enable_trophy_loot", true);
        enableTrophyEffects = builder
                .comment("Whether trophies can apply status effects when right-clicked")
                .translation(String.format("config.%s.enable_trophy_effects", Trofers.MODID))
                .define("enable_trophy_effects", true);
    }
}

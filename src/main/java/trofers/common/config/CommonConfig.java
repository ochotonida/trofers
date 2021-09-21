package trofers.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import trofers.Trofers;

public class CommonConfig {

    public final ForgeConfigSpec.DoubleValue trophyChance;

    protected CommonConfig(ForgeConfigSpec.Builder builder) {
        trophyChance = builder
                .comment(
                        "The chance a non-boss vanilla entity drops a trophy when killed by a player",
                        "No trophies will drop from entities when this value is set to 0"
                )
                .translation(String.format("config.%s.trophy_chance", Trofers.MODID))
                .defineInRange("trophy_chance", 0.001, 0, 1);
    }
}

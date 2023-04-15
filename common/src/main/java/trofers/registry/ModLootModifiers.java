package trofers.registry;

import com.mojang.serialization.Codec;
import trofers.loot.AbstractLootModifier;
import trofers.loot.AddEntityTrophy;
import trofers.loot.AddTrophy;
import trofers.platform.PlatformServices;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModLootModifiers {

    public static Supplier<Codec<AddEntityTrophy>> ADD_ENTITY_TROPHY = register("add_entity_trophy", AddEntityTrophy.CODEC);
    public static Supplier<Codec<AddTrophy>> ADD_TROPHY = register("add_trophy", AddTrophy.CODEC);

    private static <T extends AbstractLootModifier> Supplier<Codec<T>> register(String id, Supplier<Codec<T>> codec) {
        return PlatformServices.platformHelper.registerLootModifier(id, codec);
    }

    public static void registerLootModifiers() {
        // no-op
    }
}

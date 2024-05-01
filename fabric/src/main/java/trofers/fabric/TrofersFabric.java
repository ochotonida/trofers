package trofers.fabric;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import trofers.Trofers;
import trofers.data.AdvancementDrops;
import trofers.data.EntityDrops;
import trofers.network.DataPackLoadedPacket;
import trofers.registry.ModRegistries;
import trofers.trophy.Trophy;

import java.util.List;

public class TrofersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Trofers.init();

        registerRegistries();

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(
                (player, joined) -> NetworkManager.sendToPlayer(player, new DataPackLoadedPacket())
        );
    }

    public void registerRegistries() {
        DynamicRegistries.registerSynced(ModRegistries.TROPHIES, matchConditionsOrDefault(Trophy.MAP_CODEC, Trophy.EMPTY));
        DynamicRegistries.registerSynced(ModRegistries.ENTITY_DROPS, matchConditionsOrDefault(EntityDrops.MAP_CODEC, EntityDrops.NONE));
        DynamicRegistries.registerSynced(ModRegistries.ADVANCEMENT_DROPS, matchConditionsOrDefault(AdvancementDrops.MAP_CODEC, AdvancementDrops.NONE));
    }

    // TODO use conditional loading when fabric dynamic registries supports it
    public static <T> Codec<T> matchConditionsOrDefault(MapCodec<T> codec, T otherwise) {
        return Codec.either(
                ResourceCondition.LIST_CODEC.xmap(
                        conditions -> conditions.stream().allMatch(condition -> condition.test(null)),
                        b -> List.of()
                ).dispatch(ResourceConditions.CONDITIONS_KEY, trophy -> true, b -> b ? codec : MapCodec.unit(otherwise)),
                codec.codec()
        ).xmap(either -> either.map(l -> l, r -> r), Either::left);
    }
}

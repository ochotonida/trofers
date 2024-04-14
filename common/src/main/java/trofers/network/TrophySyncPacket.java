package trofers.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import trofers.registry.ModResourceLoaders;
import trofers.trophy.Trophy;
import trofers.trophy.TrophySearchTreeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TrophySyncPacket {

    private final Map<ResourceLocation, Trophy> trophies;

    public TrophySyncPacket(FriendlyByteBuf buffer) {
        trophies = new HashMap<>();
        while (buffer.readBoolean()) {
            ResourceLocation id = buffer.readResourceLocation();
            Trophy trophy = buffer.readJsonWithCodec(Trophy.CODEC);
            trophies.put(id, trophy);
        }
    }

    public TrophySyncPacket(Map<ResourceLocation, Trophy> trophies) {
        this.trophies = trophies;
    }

    void encode(FriendlyByteBuf buffer) {
        trophies.forEach((id, trophy) -> {
            buffer.writeBoolean(true);
            buffer.writeResourceLocation(id);
            buffer.writeJsonWithCodec(Trophy.CODEC, trophy);
        });
        buffer.writeBoolean(false);
    }

    void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            ModResourceLoaders.TROPHIES.setTrophies(trophies);
            TrophySearchTreeManager.createSearchTree();
        });
    }
}

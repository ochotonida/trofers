package trofers.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TrophySyncPacket {

    private final Map<ResourceLocation, Trophy> trophies;

    @SuppressWarnings("unused")
    public TrophySyncPacket(FriendlyByteBuf buffer) {
        trophies = new HashMap<>();
        while(buffer.readBoolean()) {
            Trophy trophy = Trophy.fromNetwork(buffer);
            trophies.put(trophy.id(), trophy);
        }
    }

    public TrophySyncPacket(Map<ResourceLocation, Trophy> trophies) {
        this.trophies = trophies;
    }

    @SuppressWarnings("unused")
    void encode(FriendlyByteBuf buffer) {
        trophies.values().forEach(trophy -> {
            buffer.writeBoolean(true);
            trophy.toNetwork(buffer);
        });
        buffer.writeBoolean(false);
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> TrophyManager.setTrophies(trophies));
        context.get().setPacketHandled(true);
    }
}

package trofers.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TrophySyncPacket {

    private final Map<ResourceLocation, Trophy> trophies;

    @SuppressWarnings("unused")
    public TrophySyncPacket(PacketBuffer buffer) {
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
    void encode(PacketBuffer buffer) {
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

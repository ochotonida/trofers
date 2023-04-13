package trofers.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import trofers.block.entity.TrophyScreen;
import trofers.trophy.Trophy;
import trofers.trophy.TrophyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TrophySyncPacket {

    private final Map<ResourceLocation, Trophy> trophies;

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

    void encode(FriendlyByteBuf buffer) {
        trophies.values().forEach(trophy -> {
            buffer.writeBoolean(true);
            trophy.toNetwork(buffer);
        });
        buffer.writeBoolean(false);
    }

    void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            TrophyManager.setTrophies(trophies);
            TrophyScreen.SearchTreeManager.createSearchTree();
        });
    }
}

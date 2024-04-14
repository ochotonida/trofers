package trofers.trophy;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import trofers.Trofers;
import trofers.data.ResourceLoader;
import trofers.network.NetworkHandler;
import trofers.network.TrophySyncPacket;

import java.util.Collection;
import java.util.Map;

// TODO use a data pack registry
public class TrophyManager extends ResourceLoader<Trophy> {

    public TrophyManager() {
        super(Trofers.id("trophy_manager"), "trofers/trophies");
    }

    public Collection<Trophy> values() {
        return resources.values();
    }

    public void setTrophies(Map<ResourceLocation, Trophy> trophies) {
        resources = trophies;
    }

    @Override
    protected Trophy deserializeResource(ResourceLocation id, JsonElement element) {
        return Trophy.CODEC.decode(JsonOps.INSTANCE, element) // TODO conditions
                .getOrThrow(false, error -> {})
                .getFirst();
    }

    public void sync(ServerPlayer player) {
        NetworkHandler.CHANNEL.sendToPlayer(player, new TrophySyncPacket(resources));
    }
}

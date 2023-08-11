package trofers.trophy;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.data.ResourceLoader;
import trofers.network.NetworkHandler;
import trofers.network.TrophySyncPacket;

import java.util.Collection;
import java.util.Map;

public class TrophyManager extends ResourceLoader<Trophy> {

    public TrophyManager() {
        super(Trofers.id("trophy_manager"), "trofers/trophies");
    }

    @Nullable
    public Trophy get(ResourceLocation id) {
        return resources.getOrDefault(id, null);
    }

    public Collection<Trophy> values() {
        return resources.values();
    }

    public void setTrophies(Map<ResourceLocation, Trophy> trophies) {
        resources = trophies;
    }

    @Override
    protected Trophy deserializeResource(ResourceLocation id, JsonElement element) {
        return Trophy.fromJson(element, id);
    }

    public void sync(ServerPlayer player) {
        NetworkHandler.CHANNEL.sendToPlayer(player, new TrophySyncPacket(resources));
    }
}

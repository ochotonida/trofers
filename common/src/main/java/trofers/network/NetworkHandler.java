package trofers.network;

import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;
import trofers.Trofers;

public class NetworkHandler {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(Trofers.MOD_ID, "networking_channel"));

    public static void register() {
        CHANNEL.register(SetTrophyPacket.class, SetTrophyPacket::encode, SetTrophyPacket::new, SetTrophyPacket::apply);
        CHANNEL.register(TrophySyncPacket.class, TrophySyncPacket::encode, TrophySyncPacket::new, TrophySyncPacket::apply);
    }
}

package trofers.forge.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import trofers.Trofers;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Trofers.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.registerMessage(0, SetTrophyPacket.class, SetTrophyPacket::encode, SetTrophyPacket::new, SetTrophyPacket::handle);
        INSTANCE.registerMessage(1, TrophySyncPacket.class, TrophySyncPacket::encode, TrophySyncPacket::new, TrophySyncPacket::handle);
    }
}

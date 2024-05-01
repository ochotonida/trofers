package trofers.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import trofers.trophy.TrophySearchTreeManager;

public class NetworkHandler {

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), SetTrophyPacket.TYPE, SetTrophyPacket.CODEC, SetTrophyPacket::apply);
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.s2c(), DataPackLoadedPacket.TYPE, DataPackLoadedPacket.CODEC, (p, c) -> TrophySearchTreeManager.createSearchTree());
        } else {
            NetworkManager.registerS2CPayloadType(DataPackLoadedPacket.TYPE, DataPackLoadedPacket.CODEC);
        }
    }
}

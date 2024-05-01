package trofers.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import trofers.Trofers;

public record DataPackLoadedPacket() implements CustomPacketPayload {

    public static final Type<DataPackLoadedPacket> TYPE = new Type<>(Trofers.id("data_pack_loaded"));

    public static final StreamCodec<FriendlyByteBuf, DataPackLoadedPacket> CODEC = StreamCodec.unit(new DataPackLoadedPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

package trofers.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import trofers.Trofers;
import trofers.block.entity.TrophyBlockEntity;

public record SetTrophyPacket(ResourceLocation trophyId, BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<SetTrophyPacket> TYPE = new Type<>(Trofers.id("set_trophy"));

    public static final StreamCodec<FriendlyByteBuf, SetTrophyPacket> CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            SetTrophyPacket::trophyId,
            BlockPos.STREAM_CODEC,
            SetTrophyPacket::blockPos,
            SetTrophyPacket::new
    );

    void apply(NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer player) {
            context.queue(() -> {
                if (player.isCreative()
                        && player.level().isLoaded(blockPos)
                        && player.level().getBlockEntity(blockPos) instanceof TrophyBlockEntity blockEntity
                ) {
                    blockEntity.setTrophy(trophyId, player);
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

package trofers.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import trofers.block.entity.TrophyBlockEntity;

import java.util.function.Supplier;

public class SetTrophyPacket {

    private final ResourceLocation trophyId;
    private final BlockPos blockPos;

    public SetTrophyPacket(FriendlyByteBuf buffer) {
        this.trophyId = buffer.readResourceLocation();
        this.blockPos = buffer.readBlockPos();
    }

    public SetTrophyPacket(ResourceLocation trophyId, BlockPos blockPos) {
        this.trophyId = trophyId;
        this.blockPos = blockPos;
    }

    void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(trophyId);
        buffer.writeBlockPos(blockPos);
    }

    void apply(Supplier<NetworkManager.PacketContext> context) {
        if (context.get().getPlayer() instanceof ServerPlayer player) {
            context.get().queue(() -> {
                if (player.isCreative()
                        && player.level().isLoaded(blockPos)
                        && player.level().getBlockEntity(blockPos) instanceof TrophyBlockEntity blockEntity
                ) {
                    blockEntity.setTrophy(trophyId);
                }
            });
        }
    }
}

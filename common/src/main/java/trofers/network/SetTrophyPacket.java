package trofers.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import trofers.block.entity.TrophyBlockEntity;
import trofers.trophy.Trophy;
import trofers.trophy.TrophyManager;

import java.util.function.Supplier;

public class SetTrophyPacket {

    private final Trophy trophy;
    private final BlockPos blockPos;

    public SetTrophyPacket(FriendlyByteBuf buffer) {
        this.trophy = TrophyManager.get(buffer.readResourceLocation());
        this.blockPos = buffer.readBlockPos();
    }

    public SetTrophyPacket(Trophy trophy, BlockPos blockPos) {
        this.trophy = trophy;
        this.blockPos = blockPos;
    }

    void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(trophy.id());
        buffer.writeBlockPos(blockPos);
    }

    void apply(Supplier<NetworkManager.PacketContext> context) {
        if (context.get().getPlayer() instanceof ServerPlayer player) {
            context.get().queue(() -> {
                if (player.isCreative()
                        && player.level().isLoaded(blockPos)
                        && player.level().getBlockEntity(blockPos) instanceof TrophyBlockEntity blockEntity
                ) {
                    blockEntity.setTrophy(trophy);
                }
            });
        }
    }
}

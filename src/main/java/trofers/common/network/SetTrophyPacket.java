package trofers.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;
import trofers.common.trophy.block.TrophyBlockEntity;

import java.util.function.Supplier;

public class SetTrophyPacket {

    private final Trophy trophy;
    private final BlockPos blockPos;

    @SuppressWarnings("unused")
    public SetTrophyPacket(PacketBuffer buffer) {
        this.trophy = TrophyManager.get(buffer.readResourceLocation());
        this.blockPos = buffer.readBlockPos();
    }

    public SetTrophyPacket(Trophy trophy, BlockPos blockPos) {
        this.trophy = trophy;
        this.blockPos = blockPos;
    }

    @SuppressWarnings("unused")
    void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(trophy.id());
        buffer.writeBlockPos(blockPos);
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayerEntity player = context.get().getSender();
        if (player != null) {
            context.get().enqueueWork(() -> {
                if (player.isCreative()
                        && player.level.isLoaded(blockPos)
                        && player.level.getBlockEntity(blockPos) instanceof TrophyBlockEntity blockEntity
                ) {
                    blockEntity.setTrophy(trophy);
                }
            });
        }
        context.get().setPacketHandled(true);
    }
}

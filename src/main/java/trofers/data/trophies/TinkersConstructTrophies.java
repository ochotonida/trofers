package trofers.data.trophies;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import slimeknights.tconstruct.world.TinkerWorld;
import trofers.common.trophy.Trophy;

import java.util.ArrayList;
import java.util.List;

public class TinkersConstructTrophies {

    private static final List<EntityTrophyBuilder> TROPHIES = new ArrayList<>();

    private static EntityTrophyBuilder builder(EntityType<?> entityType, int color) {
        EntityTrophyBuilder builder = new EntityTrophyBuilder(entityType, color);
        TROPHIES.add(builder);
        return builder;
    }

    public static List<Trophy> createTrophies() {
        TROPHIES.clear();

        builder(TinkerWorld.enderSlimeEntity.get(), 0xa46de9)
                .sound(SoundEvents.SLIME_SQUISH)
                .getTag().putInt("Size", 1);
        builder(TinkerWorld.skySlimeEntity.get(), 0x62c3b4)
                .sound(SoundEvents.SLIME_SQUISH)
                .getTag().putInt("Size", 1);
        builder(TinkerWorld.terracubeEntity.get(), 0x98a1b1)
                .sound(SoundEvents.SLIME_SQUISH)
                .getTag().putInt("Size", 1);

        return TROPHIES.stream().map(EntityTrophyBuilder::createTrophy).toList();
    }
}

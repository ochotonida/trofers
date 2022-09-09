package trofers.data.trophies;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.common.trophy.Trophy;
import vazkii.quark.base.handler.QuarkSounds;
import vazkii.quark.content.mobs.module.*;

import java.util.ArrayList;
import java.util.List;

public class QuarkTrophies {

    private static final List<EntityTrophyBuilder> TROPHIES = new ArrayList<>();

    private static EntityTrophyBuilder builder(EntityType<?> entityType, int color) {
        EntityTrophyBuilder builder = new EntityTrophyBuilder(entityType, color) {
            @SuppressWarnings("ConstantConditions")
            @Override
            protected ResourceLocation getDefaultSoundEventId() {
                return new ResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(entityType).getNamespace(), "entity.%s.idle".formatted(ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath()));
            }
        };
        TROPHIES.add(builder);
        return builder;
    }

    public static List<Trophy> createTrophies() {
        TROPHIES.clear();
        builder(CrabsModule.crabType, 0xc8431f)
                .rotate(0, 90, 0)
                .sound(QuarkSounds.ENTITY_CRAB_IDLE);
        builder(ForgottenModule.forgottenType, 0x76615c)
                .putHelmet(ForgottenModule.forgotten_hat)
                .putHandItem(Items.BOW)
                .putItem("sheathed", Items.IRON_SWORD)
                .sound(SoundEvents.SKELETON_AMBIENT);
        builder(FoxhoundModule.foxhoundType, 0x8f3e44)
                .sound(QuarkSounds.ENTITY_FOXHOUND_IDLE);
        builder(ShibaModule.shibaType, 0xc98955)
                .sound(SoundEvents.WOLF_AMBIENT);
        builder(StonelingsModule.stonelingType, 0xaeb2b2)
                .sound(QuarkSounds.ENTITY_STONELING_MEEP)
                .putItem("carryingItem", Items.DIAMOND);
        builder(ToretoiseModule.toretoiseType, 0xe32008)
                .getTag().putByte("oreType", (byte) 3);
        builder(WraithModule.wraithType, 0x81969b)
                .sound(SoundEvents.SKELETON_AMBIENT);

        return TROPHIES.stream().map(EntityTrophyBuilder::createTrophy).toList();
    }
}

package trofers.data.providers.trophies;

import cofh.thermal.core.init.TCoreReferences;
import net.minecraft.world.entity.EntityType;
import trofers.trophy.Trophy;

import java.util.ArrayList;
import java.util.List;

public class ThermalTrophies {

    private static final List<EntityTrophyBuilder> TROPHIES = new ArrayList<>();

    private static void add(EntityType<?> entityType, int color) {
        EntityTrophyBuilder builder = new EntityTrophyBuilder(entityType, color);
        TROPHIES.add(builder);
    }

    public static List<Trophy> createTrophies() {
        TROPHIES.clear();

        add(TCoreReferences.BASALZ_ENTITY, 0x41464b);
        add(TCoreReferences.BLITZ_ENTITY, 0xceeaf1);
        add(TCoreReferences.BLIZZ_ENTITY, 0xe0f3f0);

        return TROPHIES.stream().map(EntityTrophyBuilder::createTrophy).toList();
    }
}

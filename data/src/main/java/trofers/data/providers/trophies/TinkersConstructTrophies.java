package trofers.data.providers.trophies;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.data.integration.Compat;

import java.util.Map;

public class TinkersConstructTrophies extends EntityTrophyProvider {

    private static final String EARTH_SLIME = "earth_slime";
    private static final String ENDER_SLIME = "ender_slime";
    private static final String SKY_SLIME = "sky_slime";
    private static final String TERRACUBE = "terracube";

    public TinkersConstructTrophies() {
        super(Compat.TINKERS_CONSTRUCT);
    }

    @Override
    public void addTrophies() {
        slimeBuilder(ENDER_SLIME)
                .accentColor(0xa46de9);
        slimeBuilder(SKY_SLIME)
                .accentColor(0x62c3b4);
        slimeBuilder(TERRACUBE)
                .accentColor(0x98a1b1);
    }

    private EntityTrophyWithLootBuilder slimeBuilder(String entityName) {
        return builder(entityName)
                .sound(SoundEvents.SLIME_SQUISH)
                .putInt("Size", 1);
    }

    @Override
    public void addExtraTrophies(Map<String, Map<ResourceLocation, ResourceLocation>> trophies) {
        ResourceLocation slimeTrophy = trophies.get(ResourceLocation.DEFAULT_NAMESPACE).get(ForgeRegistries.ENTITY_TYPES.getKey(EntityType.SLIME));
        trophies.get(getModId()).put(id(EARTH_SLIME), slimeTrophy);
    }
}

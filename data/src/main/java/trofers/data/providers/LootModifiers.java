package trofers.data.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import trofers.Trofers;
import trofers.data.providers.trophies.EntityTrophyProvider;
import trofers.loot.AbstractLootModifier;
import trofers.loot.AddEntityTrophy;
import trofers.loot.RandomTrophyChanceCondition;
import trofers.registry.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class LootModifiers extends GlobalLootModifierProvider {

    private final TrophyProviders trophyProviders;

    public LootModifiers(PackOutput packOutput, TrophyProviders trophyProviders) {
        super(packOutput, Trofers.MOD_ID);
        this.trophyProviders = trophyProviders;
    }

    protected void start() {
        Map<String, Map<ResourceLocation, ResourceLocation>> trophies = new HashMap<>();
        for (EntityTrophyProvider provider : this.trophyProviders.entityTrophies) {
            Map<ResourceLocation, ResourceLocation> entityToTrophyMap = provider.getEntityToTrophyMap();
            trophies.put(provider.getModId(), entityToTrophyMap);
        }

        for (EntityTrophyProvider provider : this.trophyProviders.entityTrophies) {
            provider.addExtraTrophies(trophies);
        }

        for (String modId : trophies.keySet()) {
            LootItemCondition[] conditions = new LootItemCondition[]{
                    LootItemKilledByPlayerCondition.killedByPlayer().build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            };

            AddEntityTrophy modifier = AddEntityTrophy.create(conditions, ModBlocks.SMALL_PLATE.get(), trophies.get(modId), false);

            String name = modId.equals("minecraft") ? "vanilla" : modId;
            name = name + "_trophies";
            add(name, modifier);
        }
    }

    public void add(String modifier, AbstractLootModifier instance) {
        add(modifier, (IGlobalLootModifier) instance);
    }
}

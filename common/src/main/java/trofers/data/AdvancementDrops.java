package trofers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.Trofers;
import trofers.registry.ModResourceLoaders;

import java.util.Map;
import java.util.Optional;

public class AdvancementDrops extends ConditionalTrophyDrops {

    public static final Codec<AdvancementDrops> CODEC = RecordCodecBuilder.create(instance -> codecStart(instance)
            .and(Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC)
                    .fieldOf("trophies").forGetter(m -> m.trophies))
            .apply(instance, AdvancementDrops::new)
    );

    private final Map<ResourceLocation, ResourceLocation> trophies;

    public AdvancementDrops(LootItemCondition[] conditions, ItemLike trophyBase, Map<ResourceLocation, ResourceLocation> trophies) {
        super(conditions, trophyBase.asItem());
        this.trophies = trophies;
    }

    public static void onAdvancementAwarded(Player player, AdvancementHolder advancement) {
        if (player.level().isClientSide()) {
            return;
        }

        LootParams lootParams = (new LootParams.Builder((ServerLevel) player.level()))
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .create(LootContextParamSets.ADVANCEMENT_REWARD);
        LootContext lootContext = (new LootContext.Builder(lootParams)).create(Optional.empty());

        for (AdvancementDrops advancementDrops : ModResourceLoaders.ADVANCEMENT_DROPS.getAllResources()) {
            advancementDrops.onAdvancementEarned(player, advancement, lootContext);
        }
    }

    public static void onDataPackLoaded(MinecraftServer server) {
        for (AdvancementDrops advancementDrops : ModResourceLoaders.ADVANCEMENT_DROPS.getAllResources()) {
            advancementDrops.validate(server);
        }
    }

    private void validate(MinecraftServer server) {
        for (ResourceLocation advancementId : trophies.keySet()) {
            if (server.getAdvancements().get(advancementId) == null) {
                Trofers.LOGGER.error("Skipping advancement trophy drops entry for missing advancement '%s'".formatted(advancementId));
            } else {
                ResourceLocation trophyId = trophies.get(advancementId);
                if (ModResourceLoaders.TROPHIES.get(trophyId) == null) {
                    Trofers.LOGGER.error("Skipping advancement trophy drops entry for advancement '%s': invalid trophy id '%s'".formatted(advancementId, trophyId));
                }
            }
        }
    }

    private void onAdvancementEarned(Player player, AdvancementHolder advancement, LootContext lootContext) {
        if (matchesConditions(lootContext)) {
            ResourceLocation trophyId = trophies.get(advancement.id());
            awardTrophy(trophyId, stack -> giveItemToPlayer(player, stack));
        }
    }

    private void giveItemToPlayer(Player player, ItemStack stack) {
        if (player.addItem(stack)) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1) * 2);
            player.containerMenu.broadcastChanges();
        } else {
            ItemEntity itemEntity = player.drop(stack, false);
            if (itemEntity != null) {
                itemEntity.setNoPickUpDelay();
                itemEntity.setTarget(player.getUUID());
            }
        }
    }
}

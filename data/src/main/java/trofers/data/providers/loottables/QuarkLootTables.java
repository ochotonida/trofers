package trofers.data.providers.loottables;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
//import vazkii.quark.base.Quark;
//import vazkii.quark.content.mobs.module.*;

public class QuarkLootTables extends LootTableProvider {

    @Override
    public String getModId() {
        return null;//Quark.MOD_ID;
    }

    @Override
    protected void addLootTables() {
        /*add(CrabsModule.crabType, item("crab_shell"));
        add(ForgottenModule.forgottenType, ForgottenModule.forgotten_hat);
        add(FoxhoundModule.foxhoundType, 1, 3, Items.COAL);
        add(ShibaModule.shibaType, Items.BONE);
        add(StonelingsModule.stonelingType, StonelingsModule.diamondHeart);
        add(ToretoiseModule.toretoiseType, 2, 6, Items.COAL, Items.RAW_COPPER, Items.RAW_IRON, Items.REDSTONE, Items.LAPIS_LAZULI);
        add(WraithModule.wraithType, item("soul_bead"));*/
    }

    private Item item(String id) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(getModId(), id));
    }
}

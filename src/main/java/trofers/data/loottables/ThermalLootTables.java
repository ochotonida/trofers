package trofers.data.loottables;

import cofh.lib.util.constants.Constants;
import cofh.thermal.core.init.TCoreReferences;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class ThermalLootTables extends LootTableProvider {

    @Override
    public String getModId() {
        return Constants.ID_THERMAL;
    }

    @Override
    protected void addLootTables() {
        add(TCoreReferences.BASALZ_ENTITY, item("basalz_rod"));
        add(TCoreReferences.BLITZ_ENTITY, item("blitz_rod"));
        add(TCoreReferences.BLIZZ_ENTITY, item("blizz_rod"));
    }

    private Item item(String id) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Constants.ID_THERMAL, id));
    }
}

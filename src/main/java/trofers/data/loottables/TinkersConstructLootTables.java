package trofers.data.loottables;

import net.minecraft.world.item.Items;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.TinkerWorld;

public class TinkersConstructLootTables extends LootTableProvider {

    @Override
    public String getModId() {
        return TConstruct.MOD_ID;
    }

    @Override
    protected void addLootTables() {
        add(TinkerWorld.enderSlimeEntity.get(), 1, 3, TinkerCommons.slimeball.get(SlimeType.ENDER));
        add(TinkerWorld.skySlimeEntity.get(), 1, 3, TinkerCommons.slimeball.get(SlimeType.SKY));
        add(TinkerWorld.terracubeEntity.get(), 1, 3, Items.CLAY_BALL);
    }
}

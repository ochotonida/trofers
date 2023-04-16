package trofers.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import trofers.Trofers;

@Config(name = Trofers.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/quartz_pillar.png")
public class ModConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject()
    public General general = new General();

    @Config(name = "general")
    public static class General implements ConfigData {

        @ConfigEntry.Gui.Tooltip
        @Comment("The chance an entity from a supported mod drops a trophy when killed")
        public final Double trophyChance = 1 / 1000D;

        @ConfigEntry.Gui.Tooltip
        @Comment("Whether trophies can drop loot when right-clicked")
        public final Boolean enableTrophyLoot = true;

        @ConfigEntry.Gui.Tooltip
        @Comment("Whether trophies can apply potion effects when right-clicked")
        public final Boolean enableTrophyEffects = true;

        public double getTrophyChance() {
            return Math.max(0, Math.min(1, trophyChance));
        }
    }
}

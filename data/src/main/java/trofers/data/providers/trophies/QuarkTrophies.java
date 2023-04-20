package trofers.data.providers.trophies;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import trofers.data.integration.Compat;

public class QuarkTrophies extends EntityTrophyProvider {

    private static final String CRAB = "crab";
    private static final String FORGOTTEN = "forgotten";
    private static final String FOXHOUND = "foxhound";
    private static final String SHIBA = "shiba";
    private static final String STONELING = "stoneling";
    private static final String TORETOISE = "toretoise";
    private static final String WRAITH = "wraith";

    public QuarkTrophies() {
        super(Compat.QUARK);
    }

    @Override
    protected String getDefaultSoundName() {
        return "idle";
    }

    @Override
    public void addTrophies() {
        builder(CRAB)
                .accentColor(0xc8431f)
                .rotate(0, 90, 0);
        builder(FORGOTTEN)
                .accentColor(0x76615c)
                .putEquipment(EquipmentSlot.HEAD, damageableItem("forgotten_hat"))
                .putHandItem(Items.BOW)
                .putItem("sheathed", Items.IRON_SWORD)
                .sound(SoundEvents.SKELETON_AMBIENT);
        builder(FOXHOUND)
                .accentColor(0x8f3e44)
                .ambientSound();
        builder(SHIBA)
                .accentColor(0xc98955)
                .sound(SoundEvents.WOLF_AMBIENT);
        builder(STONELING)
                .accentColor(0xaeb2b2)
                .entitySound("meep")
                .putItem("carryingItem", Items.DIAMOND);
        builder(TORETOISE)
                .accentColor(0xe32008)
                .loot(Items.RAW_GOLD)
                .putByte("oreType", (byte) 3);
        builder(WRAITH)
                .accentColor(0x81969b)
                .sound(SoundEvents.SKELETON_AMBIENT);
    }
}

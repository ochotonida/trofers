package trofers.data.providers.trophies;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

public class QuarkTrophies extends EntityTrophyProvider {

    public QuarkTrophies() {
        super("quark");
    }

    @Override
    protected String getDefaultSoundName() {
        return "idle";
    }

    @Override
    public void addTrophies() {
        builder("crab")
                .accentColor(0xc8431f)
                .rotate(0, 90, 0);
        builder("forgotten")
                .accentColor(0x76615c)
                .putEquipment(EquipmentSlot.HEAD, damageableItem("forgotten_hat"))
                .putHandItem(Items.BOW)
                .putItem("sheathed", Items.IRON_SWORD)
                .sound(SoundEvents.SKELETON_AMBIENT);
        builder("foxhound")
                .accentColor(0x8f3e44)
                .sound("ambient");
        builder("shiba")
                .accentColor(0xc98955)
                .sound(SoundEvents.WOLF_AMBIENT);
        builder("stoneling")
                .accentColor(0xaeb2b2)
                .sound("meep")
                .putItem("carryingItem", Items.DIAMOND);
        builder("toretoise")
                .accentColor(0xe32008)
                .putByte("oreType", (byte) 3);
        builder("wraith")
                .accentColor(0x81969b)
                .sound(SoundEvents.SKELETON_AMBIENT);
    }
}

package trofers.data.providers.trophies;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import trofers.data.integration.Compat;

public class AdAstraTrophies extends EntityTrophyProvider {

    private static final String CORRUPTED_LUNARIAN = "corrupted_lunarian";
    private static final String GLACIAN_RAM = "glacian_ram";
    private static final String LUNARIAN = "lunarian";
    private static final String LUNARIAN_WANDERING_TRADER = "lunarian_wandering_trader";
    private static final String MARTIAN_RAPTOR = "martian_raptor";
    private static final String MOGLER = "mogler";
    private static final String PYGRO = "pygro";
    private static final String PYGRO_BRUTE = "pygro_brute";
    private static final String STAR_CRAWLER = "star_crawler";
    private static final String SULFUR_CREEPER = "sulfur_creeper";
    private static final String ZOMBIFIED_MOGLER = "zombified_mogler";
    private static final String ZOMBIFIED_PYGRO = "zombified_pygro";

    public AdAstraTrophies() {
        super(Compat.AD_ASTRA);
    }

    @Override
    public void addTrophies() {
        builder(CORRUPTED_LUNARIAN)
                .accentColor(0x6b8f85)
                .sound(SoundEvents.ZOMBIE_VILLAGER_AMBIENT);
        builder(GLACIAN_RAM)
                .accentColor(0xd9becd)
                .offset(0, 0, -1)
                .sound(SoundEvents.GOAT_AMBIENT);
        builder(LUNARIAN)
                .accentColor(0x7682d1)
                .sound(SoundEvents.VILLAGER_AMBIENT);
        builder(LUNARIAN_WANDERING_TRADER)
                .accentColor(0x425f90)
                .sound(SoundEvents.WANDERING_TRADER_AMBIENT);
        builder(MARTIAN_RAPTOR)
                .accentColor(0x5eb648)
                .sound(SoundEvents.STRIDER_HURT);
        builder(MOGLER)
                .accentColor(0xcd5522)
                .offset(0, 0, 2)
                .sound(SoundEvents.HOGLIN_AMBIENT);
        builder(PYGRO)
                .accentColor(0xb88c41)
                .sound(SoundEvents.PIGLIN_AMBIENT);
        builder(PYGRO_BRUTE)
                .accentColor(0xb88c41)
                .putHandItem(Items.GOLDEN_AXE)
                .sound(SoundEvents.PIGLIN_BRUTE_AMBIENT);
        builder(STAR_CRAWLER)
                .accentColor(0x6feae6)
                .rotate(0, 45, 0)
                .scale(0.1666)
                .sound(SoundEvents.TURTLE_HURT);
        builder(SULFUR_CREEPER)
                .accentColor(0xb89f25)
                .sound(SoundEvents.CREEPER_PRIMED);
        builder(ZOMBIFIED_MOGLER)
                .accentColor(0xad5b2d)
                .offset(0, 0, 2)
                .sound(SoundEvents.ZOGLIN_AMBIENT);
        builder(ZOMBIFIED_PYGRO)
                .accentColor(0xb88c41)
                .putHandItem(Items.GOLDEN_SWORD)
                .sound(SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT);
    }
}

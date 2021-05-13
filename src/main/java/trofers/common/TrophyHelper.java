package trofers.common;

import net.minecraft.nbt.CompoundNBT;

public class TrophyHelper {

    public static int getCombinedColor(int red, int green, int blue) {
        return red << 16 | green << 8 | blue;
    }

    public static int getCombinedColor(CompoundNBT tag) {
        return getCombinedColor(tag.getInt("Red"), tag.getInt("Green"), tag.getInt("Blue"));
    }
}
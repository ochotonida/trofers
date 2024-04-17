package trofers;

import net.minecraft.world.item.ItemStack;
import trofers.trophy.Trophy;

public class TrofersClient {

    public static int getTrophyColor(Trophy trophy, int index) {
        if (trophy != null) {
            if (index == 0) {
                return trophy.colors().base();
            } else if (index == 1) {
                return trophy.colors().accent();
            }
        }
        return 0xFFFFFF;
    }

    public static int getTrophyItemColor(ItemStack stack, int index) {
        return getTrophyColor(Trophy.getTrophy(stack), index);
    }
}

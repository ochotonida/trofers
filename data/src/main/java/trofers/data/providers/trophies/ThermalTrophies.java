package trofers.data.providers.trophies;

import cofh.thermal.lib.common.ThermalIDs;

public class ThermalTrophies extends EntityTrophyProvider {

    public ThermalTrophies() {
        super("thermal");
    }

    @Override
    public void addTrophies() {
        builder(ThermalIDs.ID_BASALZ)
                .accentColor(0x41464b);
        builder(ThermalIDs.ID_BLITZ)
                .accentColor(0xceeaf1);
        builder(ThermalIDs.ID_BLIZZ)
                .accentColor(0xe0f3f0);
    }
}

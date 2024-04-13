package trofers.neoforge.datagen.providers.trophies;

import trofers.neoforge.datagen.integration.Compat;

public class ThermalTrophies extends EntityTrophyProvider {

    public ThermalTrophies() {
        super(Compat.THERMAL);
    }

    @Override
    public void addTrophies() {
        builder("basalz")
                .accentColor(0x41464b);
        builder("blitz")
                .accentColor(0xceeaf1);
        builder("blizz")
                .accentColor(0xe0f3f0);
    }
}

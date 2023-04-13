package trofers.config;

public class ModConfig {

    public static CommonConfig common = new CommonConfig();

    public static void registerCommon() {
        // Pair<CommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        // common = commonSpecPair.getLeft();
        // ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, commonSpecPair.getRight());
    }
}

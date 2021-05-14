package trofers.common;

public enum TrophyAnimation {
    FIXED("fixed", 1),
    SPINNING("spinning", 1.25F),
    TUMBLING("tumbling", 1.5F);

    private final String name;
    private final float displayHeightMultiplier;

    TrophyAnimation(String name, float displayHeightMultiplier) {
        this.name = name;
        this.displayHeightMultiplier = displayHeightMultiplier;
    }

    public float getDisplayHeightMultiplier() {
        return displayHeightMultiplier;
    }

    public String getName() {
        return name;
    }

    public static TrophyAnimation byName(String name) {
        for (TrophyAnimation animation : values()) {
            if (animation.name.equals(name)) {
                return animation;
            }
        }
        return FIXED;
    }
}

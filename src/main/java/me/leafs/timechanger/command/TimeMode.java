package me.leafs.timechanger.command;

public enum TimeMode {
    STATIC,
    DYNAMIC;

    public static TimeMode valueOr(String name, TimeMode def) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }

        return def;
    }
}

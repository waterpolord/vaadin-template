package com.robertgarcia.template.shared.panelmap;

public record TextInputConfig(
        String key,
        String placeholder,
        boolean numeric,
        boolean required,
        boolean editable,
        String width,
        int flexGrow,
        boolean allowNegativeValues
) {
    public static TextInputConfig text(String key, String placeholder, int flexGrow) {
        return new TextInputConfig(key, placeholder, false, false, true, null, flexGrow, false);
    }

    public static TextInputConfig number(String key, String placeholder, String width) {
        return new TextInputConfig(key, placeholder, true, false, true, width, 0, false);
    }
}

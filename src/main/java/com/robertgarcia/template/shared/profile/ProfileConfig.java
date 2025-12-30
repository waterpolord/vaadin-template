package com.robertgarcia.template.shared.profile;

import java.util.List;
import java.util.function.Function;

public record ProfileConfig<T>(
        Function<T, String> titleProvider,
        Function<T, String> subtitleProvider,
        String badgeText,
        List<ProfileSection<T>> sections,
        List<ProfileTab<T>> tabs) {

}

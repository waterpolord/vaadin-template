package com.robertgarcia.template.shared.profile;
import java.util.function.Function;

public record ProfileField<T>(String label, Function<T, String> valueProvider) {
}

package com.robertgarcia.template.shared.profile;

import java.util.List;

public record ProfileSection<T>(String title, List<ProfileField<T>> fields) {
}

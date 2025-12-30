package com.robertgarcia.template.shared.list;

import java.util.List;

public record PagedResult<T>(List<T> items, long total) {}

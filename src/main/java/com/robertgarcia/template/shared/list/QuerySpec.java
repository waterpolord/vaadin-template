package com.robertgarcia.template.shared.list;

public record QuerySpec(java.util.Map<String,Object> filters, String sort, boolean asc, int page, int size) {}

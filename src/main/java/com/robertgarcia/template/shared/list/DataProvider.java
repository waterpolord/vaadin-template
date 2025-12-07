package com.robertgarcia.template.shared.list;

public interface DataProvider<T> {
    PagedResult<T> fetch(QuerySpec spec);
}

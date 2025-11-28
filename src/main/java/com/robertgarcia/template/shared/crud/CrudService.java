package com.robertgarcia.template.shared.crud;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(T entity);
    void delete(T entity);
}


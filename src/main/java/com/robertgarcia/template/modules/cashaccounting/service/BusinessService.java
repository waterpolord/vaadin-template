package com.robertgarcia.template.modules.cashaccounting.service;


import com.robertgarcia.template.modules.cashaccounting.domain.Business;
import com.robertgarcia.template.modules.cashaccounting.domain.TimeType;
import com.robertgarcia.template.modules.cashaccounting.repo.BusinessRepository;
import com.robertgarcia.template.shared.crud.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class BusinessService implements CrudService<Business,Integer> {

    private final BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }


    public List<Business> findAll() {
        return businessRepository.findAllByDeletedIsFalse();
    }


    public Business findById(Integer id) {
        return businessRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    public Business save(Business entity) {
        entity.setDeleted(false);
        entity.setTimeType(TimeType.WEEKLY);
        return businessRepository.save(entity);
    }


    public void delete(Business entity) {
        entity.setDeleted(true);
        businessRepository.save(entity);
    }
}


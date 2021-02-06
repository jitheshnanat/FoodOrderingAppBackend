package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@SuppressWarnings("All")
@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    //To get State By Uuidif no results return null
    public StateEntity getStateByUuid(String uuid){
        try{
            StateEntity stateEntity = entityManager.createNamedQuery("getStateByUuid",StateEntity.class).setParameter("uuid",uuid).getSingleResult();
            return stateEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

}
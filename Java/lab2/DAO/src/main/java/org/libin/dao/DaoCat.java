package org.libin.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.libin.dao.models.Cat;
import org.libin.dao.units.HibernateSessionFactoryUtil;

public class DaoCat {
    public Cat findCatById(long id){
        Session currentSession = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Cat cat = currentSession.get(Cat.class,id);
        currentSession.close();
        return cat;
    }
    public void saveCat(Cat cat){
        Session currentSession = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction currentTransaction = currentSession.beginTransaction();
        currentSession.save(cat);
        currentTransaction.commit();
        currentSession.close();
    }
    public void updateCat(Cat cat){
        Session currentSession = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction currentTransaction = currentSession.beginTransaction();
        currentSession.update(cat);
        currentTransaction.commit();
        currentSession.close();
    }
    public void deleteCat(Cat cat){
        Session currentSession = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction currentTransaction = currentSession.beginTransaction();
        currentSession.delete(cat);
        currentTransaction.commit();
        currentSession.close();
    }
}

package org.libin.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.libin.dao.models.Owner;
import org.libin.dao.units.HibernateSessionFactoryUtil;

public class DaoOwner {
    public Owner findOwnerById(long id){
        Session currentSession = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Owner owner = currentSession.get(Owner.class,id);
        currentSession.close();
        return owner;
    }
    public void saveOwner(Owner owner){
        Session currentSession = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction currentTransaction = currentSession.beginTransaction();
        currentSession.save(owner);
        currentTransaction.commit();
        currentSession.close();
    }
    public void updateOwner(Owner owner){
        Session currentSession = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction currentTransaction = currentSession.beginTransaction();
        currentSession.update(owner);
        currentTransaction.commit();
        currentSession.close();
    }
    public void deleteOwner(Owner owner){
        Session currentSession = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction currentTransaction = currentSession.beginTransaction();
        currentSession.delete(owner);
        currentTransaction.commit();
        currentSession.close();
    }

}

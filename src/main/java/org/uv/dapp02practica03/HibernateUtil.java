package org.uv.dapp02practica03;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author enigma
 */
public class HibernateUtil {
    
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory(){
        try{
            Configuration config = new Configuration();
            config.configure("hibernate.cfg.xml");
            return config.buildSessionFactory();
        } catch (Throwable ex){
            System.err.println("Error al crear session factory" + ex);
            throw new ExceptionInInitializerError(ex);
        } 
    }   
    
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}

package org.uv.dapp02practica03;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class EmpleadoDAO implements IDAOGeneral<EmpleadoPojo, Long> {

    @Override
    public EmpleadoPojo guardar(EmpleadoPojo pojo) {
        if (pojo == null) {
            throw new IllegalArgumentException("El objeto EmpleadoPojo no puede ser nulo");
        }

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            session.save(pojo);
            transaction.commit();

            return pojo;
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al guardar el empleado", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public EmpleadoPojo modificar(EmpleadoPojo pojo, Long id) {
        if (pojo == null || id == null) {
            throw new IllegalArgumentException("El objeto o ID no pueden ser nulos");
        }
        if (!id.equals(pojo.getClave())) {
            throw new IllegalArgumentException("El ID proporcionado no coincide con el ID del objeto");
        }

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            EmpleadoPojo existente = session.get(EmpleadoPojo.class, id);
            if (existente == null) {
                throw new RuntimeException("No se encontró el empleado con ID: " + id);
            }

            session.update(pojo);
            transaction.commit();

            return pojo;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al actualizar el empleado con ID: " + id, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            EmpleadoPojo empleado = session.get(EmpleadoPojo.class, id);
            if (empleado == null) {
                transaction.rollback();
                return false;
            }

            session.delete(empleado);
            transaction.commit();

            return true;
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al eliminar el empleado con ID: " + id, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public EmpleadoPojo buscarById(Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;
        EmpleadoPojo pojo = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            pojo = session.get(EmpleadoPojo.class, id);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); 
            }
            System.err.println("Error al buscar empleado por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close(); 
            }
        }

        return pojo;
    }

    @Override
    public List<EmpleadoPojo> buscarAll() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;
            List<EmpleadoPojo> lstEmpleado = new ArrayList<>();

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaB = session.getCriteriaBuilder();
            CriteriaQuery<EmpleadoPojo> criteria = criteriaB.createQuery(EmpleadoPojo.class);
            Root<EmpleadoPojo> root = criteria.from(EmpleadoPojo.class);
            criteria.select(root);

            Query<EmpleadoPojo> query = session.createQuery(criteria);
            lstEmpleado = query.getResultList();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback(); 
            }
            System.err.println("Error al buscar todos los empleados: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close(); 
            }
        }

        return lstEmpleado;
    }
}
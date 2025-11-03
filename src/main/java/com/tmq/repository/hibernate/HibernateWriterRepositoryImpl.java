package com.tmq.repository.hibernate;

import com.tmq.model.Writer;
import com.tmq.repository.WriterRepository;
import com.tmq.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class HibernateWriterRepositoryImpl implements WriterRepository {
    @Override
    public List<Writer> findByName(String firstName, String lastName) {
        try (Session session = HibernateUtil.getSession()){
            Query<Writer> query = session.createQuery("from Writer where firstName=:firstName and lastName=:lastName", Writer.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName);
            return query.getResultList();
        }
    }

    @Override
    public List<Writer> findAll() {
        try  (Session session = HibernateUtil.getSession()){
            Query<Writer> query = session.createQuery("from Writer", Writer.class);
            return query.getResultList();
        }
    }

    @Override
    public Optional<Writer> findById(Long aLong) {
        try (Session session = HibernateUtil.getSession()){
            Query<Writer> query = session.createQuery("from Writer where id=:id", Writer.class);
            query.setParameter("id", aLong);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public Writer save(Writer writer) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            session.persist(writer);
            session.getTransaction().commit();
            return writer;
        }
    }

    @Override
    public Writer update(Writer writer) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Writer result = session.merge(writer);
            session.getTransaction().commit();
            return result;
        }
    }

    @Override
    public boolean delete(Long aLong) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            session.remove(Writer.builder().id(aLong).build());
            session.getTransaction().commit();
            return true;
        }
    }
}

package com.tmq.repository.hibernate;

import com.tmq.util.HibernateUtil;
import com.tmq.model.Label;
import com.tmq.repository.LabelRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HibernateLabelRepositoryImpl implements LabelRepository {
    @Override
    public Optional<Label> findByName(String name) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Label> query = session.createQuery("from Label where name = :name", Label.class);
            query.setParameter("name", name);
            Label label = query.uniqueResult();
            return Optional.ofNullable(label);
        }
    }

    @Override
    public List<Label> findByName(List<Label> labels) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Label> query = session.createQuery("from Label where name in (:names)", Label.class);
            query.setParameterList("names", labels.stream().map(Label::getName).toList());
            return query.list();
        }
    }

    @Override
    public List<Label> findOrCreateLabels(List<Label> labels) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            List<Label> managedLabels = new ArrayList<>();
            labels.forEach(label -> {
                Query<Label> query = session.createQuery("from Label l where l.name = :label", Label.class);
                query.setParameter("label", label.getName());
                Label labelResult = query.uniqueResult();
                if (labelResult == null) {
                    session.persist(label);
                    session.flush();
                    managedLabels.add(label);
                } else {
                    managedLabels.add(labelResult);
                }
            });
            session.getTransaction().commit();
            return managedLabels;
        }
    }

    @Override
    public List<Label> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            List<Label> labels = session.createQuery("from Label", Label.class).list();
            return labels;
        }
    }

    @Override
    public Optional<Label> findById(Long aLong) {
        try (Session session = HibernateUtil.getSession()) {
            Label label = session.get(Label.class, aLong);
            return Optional.ofNullable(label);
        }
    }

    @Override
    public Label save(Label label) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Label savedLabel = session.merge(label);
            session.getTransaction().commit();
            return savedLabel;
        }
    }

    @Override
    public Label update(Label label) {
        try  (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Label result = session.merge(label);
            session.getTransaction().commit();
            return result;
        }
    }

    @Override
    public boolean delete(Long aLong) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.remove(Label.builder().id(aLong).build());
            session.getTransaction().commit();
            return true;
        }
    }
}

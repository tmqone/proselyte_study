package com.tmq.repository.hibernate;

import com.tmq.exception.FileNotFoundException;
import com.tmq.model.File;
import com.tmq.repository.FileRepository;
import com.tmq.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class HibernateFileRepositoryImpl implements FileRepository {
    @Override
    public List<File> findAll() {
        try (Session session = HibernateUtil.getSession()){
            return session.createQuery("from File", File.class).list();
        }
    }

    @Override
    public Optional<File> findById(Integer integer) {
        try (Session session = HibernateUtil.getSession()){
            return Optional.ofNullable(session.get(File.class, integer));
        }
    }

    @Override
    public File save(File file) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            session.persist(file);
            session.getTransaction().commit();
            return file;
        }
    }

    @Override
    public File update(File file) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            File merge = findById(file.getId()).orElseThrow(FileNotFoundException::new);
            merge.setFilePath(file.getFilePath());
            merge.setName(file.getName());
            session.getTransaction().commit();
            return merge;
        }
    }

    @Override
    public boolean delete(Integer integer) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            session.remove(integer);
            session.getTransaction().commit();
            return true;
        }
    }
}

package com.tmq.repository.hibernate;

import com.tmq.exception.FileNotFoundException;
import com.tmq.model.File;
import com.tmq.repository.FileRepository;
import com.tmq.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateFileRepositoryImpl implements FileRepository {
    private static final HibernateFileRepositoryImpl INSTANCE = new HibernateFileRepositoryImpl();

    public static HibernateFileRepositoryImpl getInstance() {
        return INSTANCE;
    }


    @Override
    public List<File> findAll() {
        try (Session session = HibernateUtil.getSession()){
            return session.createQuery("from File where isDeleted = false", File.class).list();
        }
    }

    public List<File> findAll(Integer userId) {
        try (Session session = HibernateUtil.getSession()){
            return session
                    .createQuery("""
                            from File f 
                            join Event e on e.file = f 
                            where e.user.id = :id and f.isDeleted = false
                            """, File.class)
                    .setParameter("id", userId).list();
        }
    }

    @Override
    public Optional<File> findById(Integer id) {
        try (Session session = HibernateUtil.getSession()){
            return Optional.ofNullable(session.get(File.class, id));
        }
    }

    public Optional<File> findById(Integer id, Integer userId) {
        try (Session session = HibernateUtil.getSession()){
            return Optional.ofNullable(session
                    .createQuery("""
                            from File f 
                            join Event e on e.file = f 
                            where e.user.id = :userId and f.id = :id and f.isDeleted = false
                            """, File.class)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .uniqueResult());
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
            File merge = session.find(File.class, file.getId());
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
            File file = session.get(File.class, integer);
            file.setIsDeleted(true);
            session.getTransaction().commit();
            return true;
        }
    }

    @Override
    public boolean existsById(Integer integer) {
        try (Session session = HibernateUtil.getSession()){
            return session.createQuery("select count(f) from File f where f.id = :id", Long.class)
                    .setParameter("id", integer).uniqueResult() > 0;
        }
    }

    public boolean delete(Integer id, Integer userId) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Optional.ofNullable(session
                    .createQuery("""
                            from File f 
                            join Event e on e.file = f 
                            where e.user.id = :userId and f.id = :id and f.isDeleted = false
                            """, File.class)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .uniqueResult()).ifPresentOrElse(file -> file.setIsDeleted(true), () -> {throw new FileNotFoundException();});
            session.getTransaction().commit();
            return true;
        }
    }
}

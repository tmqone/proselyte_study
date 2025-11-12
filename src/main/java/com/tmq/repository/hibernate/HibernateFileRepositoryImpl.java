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
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from File where isDeleted = false", File.class).list();
        }
    }

    public List<File> findAllByUserId(Integer userId) {
        try (Session session = HibernateUtil.getSession()) {
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
        try (Session session = HibernateUtil.getSession()) {
            return Optional.ofNullable(session
                    .createQuery("from File where id = :id and isDeleted = false", File.class)
                    .setParameter("id", id).uniqueResult());
        }
    }

    public List<File> findByUserId(Integer userId) {
        try (Session session = HibernateUtil.getSession()) {
            return session
                    .createQuery("""
                            from File f 
                            join Event e on e.file = f 
                            where e.user.id = :userId and f.isDeleted = false
                            """, File.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    @Override
    public File save(File file) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(file);
            session.getTransaction().commit();
            return file;
        }
    }

    @Override
    public File update(File file) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            int update = session.createQuery("update File f set f.name = :name, f.filePath = :filepath where f.id = :id")
                    .setParameter("name", file.getName())
                    .setParameter("filepath", file.getFilePath())
                    .setParameter("id", file.getId())
                    .executeUpdate();
            if (update == 0) throw new FileNotFoundException();
            session.getTransaction().commit();
            return file;
        }
    }

    public boolean delete(Integer id) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            int i = session.createQuery("update File set isDeleted = true where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return i > 0;
        }
    }
}

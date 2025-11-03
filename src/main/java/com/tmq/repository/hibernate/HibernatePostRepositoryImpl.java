package com.tmq.repository.hibernate;

import com.tmq.model.PostStatus;
import com.tmq.util.HibernateUtil;
import com.tmq.model.Post;
import com.tmq.repository.PostRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class HibernatePostRepositoryImpl implements PostRepository {
    @Override
    public List<Post> findByWriter(Long writerId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Query<Post> query = session.createQuery("""
                    from Post p
                    join fetch p.writer
                    left join fetch p.labels
                    where p.writer.id = :writer_id and p.postStatus != 'DELETED'
                    """, Post.class);
            query.setParameter("writer_id", writerId);
            List<Post> posts = query.list();
            session.getTransaction().commit();
            return posts;
        }
    }

    @Override
    public List<Post> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Query<Post> query = session.createQuery("""
                    from Post p
                    join fetch p.writer
                    left join fetch p.labels
                    where p.postStatus != 'DELETED'
                    """, Post.class);
            List<Post> posts = query.list();
            session.getTransaction().commit();
            return posts;
        }
    }

    @Override
    public Optional<Post> findById(Long aLong) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Query<Post> query = session.createQuery("""
                    from Post p
                    join fetch p.writer
                    left join fetch p.labels
                    where p.id = :id and p.postStatus != 'DELETED'
                    """, Post.class);
            query.setParameter("id", aLong);
            Post post = query.uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(post);
        }
    }

    @Override
    public Post save(Post post) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public Post update(Post post) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Post foundPost = session.createQuery("""
                SELECT p FROM Post p
                LEFT JOIN FETCH p.writer
                LEFT JOIN FETCH p.labels
                WHERE p.id = :id
                """, Post.class)
                    .setParameter("id", post.getId())
                    .uniqueResult();

            foundPost.setPostStatus(post.getPostStatus());
            foundPost.setLabels(post.getLabels());
            foundPost.setContent(post.getContent());
            foundPost.setUpdated(Instant.now());
            session.persist(foundPost);
            session.getTransaction().commit();
            return foundPost;
        }
    }

    @Override
    public boolean delete(Long aLong) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Post post = session.find(Post.class, aLong);
            post.setPostStatus(PostStatus.DELETED);
            session.getTransaction().commit();
        }
        return true;
    }
}

package com.tmq.service;

import com.tmq.exception.GeneralException;
import com.tmq.exception.PostNotFoundException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.PostStatus;
import com.tmq.model.Writer;
import com.tmq.repository.LabelRepository;
import com.tmq.repository.PostRepository;
import com.tmq.repository.WriterRepository;
import com.tmq.util.DatabaseUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    PostRepository postRepository;
    @Mock
    LabelRepository labelRepository;
    @Mock
    WriterRepository writerRepository;

    @InjectMocks
    PostServiceImpl postService;

    MockedStatic<DatabaseUtil> mocked;
    Connection connection;

    @BeforeEach
    public void setMocked() {
        mocked = Mockito.mockStatic(DatabaseUtil.class);
        connection = Mockito.mock(Connection.class);
        mocked.when(DatabaseUtil::getConnection).thenReturn(connection);
    }

    @AfterEach
    public void mockedInvocation() throws SQLException {
        Mockito.verify(connection).close();
        mocked.close();
        mocked = null;
    }

    private final List<Post> postRepositoryGiven = List.of(
            Post.builder()
                    .id(1L)
                    .writer(
                            Writer.builder()
                                    .id(1L)
                                    .build()
                    )
                    .content("Test")
                    .created(Timestamp
                            .valueOf(LocalDateTime.of(2025, 10, 10, 10, 00))
                            .toInstant())
                    .updated(Timestamp
                            .valueOf(LocalDateTime.of(2025, 10, 10, 10, 00))
                            .toInstant())
                    .postStatus(PostStatus.ACTIVE)
                    .build(),
            Post.builder()
                    .id(2L)
                    .writer(
                            Writer.builder()
                                    .id(1L)
                                    .build()
                    )
                    .content("Test")
                    .created(Timestamp
                            .valueOf(LocalDateTime.of(2025, 10, 10, 10, 00))
                            .toInstant())
                    .updated(Timestamp
                            .valueOf(LocalDateTime.of(2025, 10, 10, 10, 00))
                            .toInstant())
                    .postStatus(PostStatus.DELETED)
                    .build()
    );

    @Test
    public void getById_PostsInRepoExists_LabelsExists_WriterExists() throws SQLException {
        Post postGiven = postRepositoryGiven.getFirst();
        Writer writerRepositoryGiven = Writer.builder()
                .id(1L)
                .firstName("Artem")
                .lastName("Test")
                .build();

        Mockito.when(postRepository.findById(postGiven.getId(), connection))
                .thenReturn(Optional.of(postGiven));
        Mockito.when(labelRepository.findByPostId(1L, connection))
                .thenReturn(List.of(new Label(1L, "Test")));
        Mockito.when(writerRepository.findById(1L, connection))
                .thenReturn(Optional.of(writerRepositoryGiven));

        Post result = postService.getById(postGiven.getId());
        Mockito.verify(labelRepository, Mockito.times(1)).findByPostId(1L, connection);
        Mockito.verify(writerRepository, Mockito.times(1)).findById(1L, connection);
        Mockito.verify(postRepository, Mockito.times(1)).findById(postGiven.getId(), connection);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getLabels());
        Assertions.assertFalse(result.getLabels().isEmpty());
        Assertions.assertEquals(postGiven.getLabels(), result.getLabels());
        Assertions.assertNotNull(result.getWriter());
        Assertions.assertEquals(result.getWriter(), postGiven.getWriter());
        Assertions.assertEquals(postGiven, result);
        Assertions.assertEquals(PostStatus.ACTIVE, result.getPostStatus());
        Assertions.assertEquals(postGiven.getId(), result.getId());
        Assertions.assertEquals(postGiven.getCreated(), result.getCreated());
        Assertions.assertEquals(postGiven.getWriter(), result.getWriter());
        Assertions.assertEquals(postGiven.getContent(), result.getContent());
        Assertions.assertEquals(postGiven.getUpdated(), result.getUpdated());
    }

    @Test
    public void getById_PostsInRepoExists_LabelsNotExists_WriterExists() throws SQLException {
        Post postGiven = postRepositoryGiven.getFirst();
        Writer writerRepositoryGiven = Writer.builder()
                .id(1L)
                .firstName("Artem")
                .lastName("Test")
                .build();

        Mockito.when(postRepository.findById(postGiven.getId(), connection))
                .thenReturn(Optional.of(postGiven));
        Mockito.when(labelRepository.findByPostId(1L, connection))
                .thenReturn(Collections.emptyList());
        Mockito.when(writerRepository.findById(1L, connection))
                .thenReturn(Optional.of(writerRepositoryGiven));

        Post result = postService.getById(postGiven.getId());
        Mockito.verify(labelRepository, Mockito.times(1)).findByPostId(1L, connection);
        Mockito.verify(writerRepository, Mockito.times(1)).findById(1L, connection);
        Mockito.verify(postRepository, Mockito.times(1)).findById(postGiven.getId(), connection);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getLabels().isEmpty());
        Assertions.assertNotNull(result.getWriter());
        Assertions.assertEquals(result.getWriter(), postGiven.getWriter());
        Assertions.assertEquals(postGiven, result);
        Assertions.assertEquals(PostStatus.ACTIVE, result.getPostStatus());
        Assertions.assertEquals(postGiven.getId(), result.getId());
        Assertions.assertEquals(postGiven.getCreated(), result.getCreated());
        Assertions.assertEquals(postGiven.getWriter(), result.getWriter());
        Assertions.assertEquals(postGiven.getContent(), result.getContent());
        Assertions.assertEquals(postGiven.getUpdated(), result.getUpdated());
    }

    @Test
    public void getById_PostInRepoExists_WriterNotExists() throws SQLException {
        Long id = postRepositoryGiven.getFirst().getId();
        Mockito.when(postRepository.findById(id,
                        connection))
                .thenReturn(Optional.of(postRepositoryGiven.getFirst()));
        Mockito.when(writerRepository.findById(1L, connection)).thenReturn(Optional.empty());
        Assertions.assertThrows(WriterNotFoundException.class, () -> postService.getById(id));
        Mockito.verify(postRepository, Mockito.times(1)).findById(id, connection);
    }

    @Test
    public void getAll_PostsInRepoExists_LabelsExists_WriterExists() throws SQLException {
        Writer writerRepositoryGiven = Writer.builder()
                .id(1L)
                .firstName("Artem")
                .lastName("Test")
                .build();
        List<Label> labelRepositoryGiven = List.of(new Label(1L, "Test"), new Label(2L, "Test2"));


        Mockito.when(postRepository.findAll(connection))
                .thenReturn(postRepositoryGiven);
        Mockito.when(labelRepository.findByPostId(1L, connection))
                .thenReturn(List.of(new Label(1L, "Test")));
        Mockito.when(labelRepository.findByPostId(2L, connection))
                .thenReturn(List.of(new Label(2L, "Test2")));
        Mockito.when(writerRepository.findById(1L, connection))
                .thenReturn(Optional.of(writerRepositoryGiven));

        List<Post> result = postService.getAll();
        Mockito.verify(labelRepository, Mockito.times(1)).findByPostId(1L, connection);
        Mockito.verify(labelRepository, Mockito.times(1)).findByPostId(2L, connection);
        Mockito.verify(writerRepository, Mockito.times(2)).findById(1L, connection);
        Mockito.verify(postRepository, Mockito.times(1)).findAll(connection);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirst().getLabels());
        Assertions.assertFalse(result.getFirst().getLabels().isEmpty());
        Assertions.assertFalse(result.getLast().getLabels().isEmpty());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getLabels().getFirst(), labelRepositoryGiven.getFirst());
        Assertions.assertEquals(postRepositoryGiven.getLast().getLabels().getFirst(), labelRepositoryGiven.getLast());
        Assertions.assertNotNull(result.getFirst().getWriter());
        Assertions.assertNotNull(result.getLast().getWriter());
        Assertions.assertEquals(result.getFirst().getWriter(), writerRepositoryGiven);
        Assertions.assertEquals(result.getLast().getWriter(), writerRepositoryGiven);
        Assertions.assertEquals(postRepositoryGiven, result);
        Assertions.assertEquals(postRepositoryGiven.size(), result.size());
        Assertions.assertEquals(PostStatus.ACTIVE, result.getFirst().getPostStatus());
        Assertions.assertEquals(PostStatus.DELETED, result.getLast().getPostStatus());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getId(), result.getFirst().getId());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getCreated(), result.getFirst().getCreated());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getWriter(), result.getFirst().getWriter());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getContent(), result.getFirst().getContent());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getUpdated(), result.getFirst().getUpdated());
        Assertions.assertEquals(postRepositoryGiven.getLast().getId(), result.getLast().getId());
        Assertions.assertEquals(postRepositoryGiven.getLast().getCreated(), result.getLast().getCreated());
        Assertions.assertEquals(postRepositoryGiven.getLast().getWriter(), result.getLast().getWriter());
        Assertions.assertEquals(postRepositoryGiven.getLast().getContent(), result.getLast().getContent());
        Assertions.assertEquals(postRepositoryGiven.getLast().getUpdated(), result.getLast().getUpdated());
    }

    @Test
    public void getAll_PostsInRepoExists_LabelsNotExists_WriterExists() throws SQLException {
        Writer writerRepositoryGiven = Writer.builder()
                .id(1L)
                .firstName("Artem")
                .lastName("Test")
                .build();

        Mockito.when(postRepository.findAll(connection))
                .thenReturn(postRepositoryGiven);
        Mockito.when(writerRepository.findById(1L, connection))
                .thenReturn(Optional.of(writerRepositoryGiven));

        List<Post> result = postService.getAll();
        Mockito.verify(labelRepository, Mockito.times(1)).findByPostId(1L, connection);
        Mockito.verify(labelRepository, Mockito.times(1)).findByPostId(2L, connection);
        Mockito.verify(writerRepository, Mockito.times(2)).findById(1L, connection);
        Mockito.verify(postRepository, Mockito.times(1)).findAll(connection);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirst().getLabels());
        Assertions.assertTrue(result.getFirst().getLabels().isEmpty());
        Assertions.assertTrue(result.getLast().getLabels().isEmpty());
        Assertions.assertNotNull(result.getFirst().getWriter());
        Assertions.assertNotNull(result.getLast().getWriter());
        Assertions.assertEquals(result.getFirst().getWriter(), writerRepositoryGiven);
        Assertions.assertEquals(result.getLast().getWriter(), writerRepositoryGiven);
        Assertions.assertEquals(postRepositoryGiven, result);
        Assertions.assertEquals(postRepositoryGiven.size(), result.size());
        Assertions.assertEquals(PostStatus.ACTIVE, result.getFirst().getPostStatus());
        Assertions.assertEquals(PostStatus.DELETED, result.getLast().getPostStatus());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getId(), result.getFirst().getId());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getCreated(), result.getFirst().getCreated());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getWriter(), result.getFirst().getWriter());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getContent(), result.getFirst().getContent());
        Assertions.assertEquals(postRepositoryGiven.getFirst().getUpdated(), result.getFirst().getUpdated());
        Assertions.assertEquals(postRepositoryGiven.getLast().getId(), result.getLast().getId());
        Assertions.assertEquals(postRepositoryGiven.getLast().getCreated(), result.getLast().getCreated());
        Assertions.assertEquals(postRepositoryGiven.getLast().getWriter(), result.getLast().getWriter());
        Assertions.assertEquals(postRepositoryGiven.getLast().getContent(), result.getLast().getContent());
        Assertions.assertEquals(postRepositoryGiven.getLast().getUpdated(), result.getLast().getUpdated());
    }

    @Test
    public void getAll_PostsInRepoExists_WriterNotExists() throws SQLException {
        Mockito.when(postRepository.findAll(connection))
                .thenReturn(postRepositoryGiven);
        Mockito.when(writerRepository.findById(1L, connection))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(WriterNotFoundException.class, () -> postService.getAll());
        Mockito.verify(postRepository, Mockito.times(1)).findAll(connection);
    }

    @Test
    public void delete_PostExists() throws SQLException {
        Mockito.when(postRepository.delete(1L, connection)).thenReturn(true);
        Assertions.assertTrue(postService.delete(1L));
        Mockito.verify(postRepository, Mockito.times(1)).delete(1L, connection);
    }

    @Test
    public void delete_PostNotExists() throws SQLException {
        Mockito.when(postRepository.delete(1L, connection)).thenReturn(false);
        Assertions.assertThrows(PostNotFoundException.class, () -> postService.delete(1L));
        Mockito.verify(postRepository, Mockito.times(1)).delete(1L, connection);
    }

    @Test
    public void savePostWriterExists() throws SQLException {
        List<Label> labelRepositoryGiven = List.of(new Label(1L, "Test"));
        Post postGiven = Post.builder()
                .id(0L)
                .content("TEST")
                .writer(new Writer(1L, null, null, null))
                .created(null)
                .updated(null)
                .postStatus(PostStatus.ACTIVE)
                .labels(labelRepositoryGiven)
                .build();

        Post returned = Post.builder()
                .id(1L)
                .content("TEST")
                .writer(new Writer(1L, "Artem", "Validzhanov", null))
                .created(Instant.now())
                .updated(Instant.now())
                .postStatus(PostStatus.ACTIVE)
                .labels(labelRepositoryGiven)
                .build();


        Mockito.when(postRepository.save(postGiven, connection)).thenReturn(returned);
        Mockito.when(writerRepository.findById(1L, connection))
                .thenReturn(Optional.of(new Writer(1L, "Artem", "Validzhanov", null)));
        Mockito.when(labelRepository.save(labelRepositoryGiven.getFirst(), connection))
                .thenReturn(returned.getLabels().getFirst());

        postService.save(postGiven);

        Mockito.verify(postRepository, Mockito.times(1)).save(postGiven, connection);
        Mockito.verify(labelRepository, Mockito.times(1)).save(labelRepositoryGiven.getFirst(), connection);
        Mockito.verify(writerRepository, Mockito.times(1)).findById(1L, connection);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(false);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(true);
        Mockito.verify(connection, Mockito.never()).rollback();
        Mockito.verify(connection, Mockito.times(1)).commit();
        Mockito.verify(connection, Mockito.times(1)).close();
        Mockito.verify(labelRepository, Mockito.times(1))
                .saveLabelToPost(1L, 1L, connection);

        Assertions.assertNotNull(returned);
        Assertions.assertEquals(PostStatus.ACTIVE, returned.getPostStatus());
        Assertions.assertNotNull(returned.getId());
        Assertions.assertNotNull(returned.getWriter().getLastName());
        Assertions.assertNotNull(returned.getWriter().getFirstName());
        Assertions.assertNotNull(returned.getLabels().getFirst().getId());
        Assertions.assertNotNull(returned.getCreated());
        Assertions.assertNotNull(returned.getUpdated());
    }

    @Test
    public void updatePost_LabelExists_WriterExists() throws SQLException {
        List<Label> labelRepositoryGiven = List.of(new Label(1L, "Test"));
        Post postGiven = Post.builder()
                .id(1L)
                .content("TEST")
                .writer(new Writer(2L, null, null, null))
                .created(Instant.now())
                .updated(Instant.now())
                .postStatus(PostStatus.ACTIVE)
                .labels(labelRepositoryGiven)
                .build();


        Mockito.when(postRepository.update(postGiven, connection)).thenReturn(postGiven);
        Mockito.when(postRepository.findById(1L, connection)).thenReturn(Optional.of(postGiven));
        Mockito.when(labelRepository.findByName(labelRepositoryGiven.getFirst().getName(), connection))
                .thenReturn(Optional.of(labelRepositoryGiven.getFirst()));

        postService.update(postGiven);

        Mockito.verify(postRepository, Mockito.times(1)).update(postGiven, connection);
        Mockito.verify(labelRepository, Mockito.times(1))
                                        .findByName(labelRepositoryGiven.getFirst().getName(), connection);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(false);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(true);
        Mockito.verify(connection, Mockito.never()).rollback();
        Mockito.verify(connection, Mockito.times(1)).commit();
        Mockito.verify(connection, Mockito.times(1)).close();

        Assertions.assertNotNull(postGiven);
        Assertions.assertEquals(PostStatus.ACTIVE, postGiven.getPostStatus());
        Assertions.assertNotNull(postGiven.getId());
        Assertions.assertNotNull(postGiven.getLabels().getFirst().getId());
        Assertions.assertNotNull(postGiven.getCreated());
        Assertions.assertNotNull(postGiven.getUpdated());
    }

    @Test
    public void updatePost_LabelNotExists_WriterExists() throws SQLException {
        List<Label> labelRepositoryGiven = List.of(new Label(1L, "Test"));
        Post postGiven = Post.builder()
                .id(1L)
                .content("TEST")
                .writer(new Writer(2L, null, null, null))
                .created(Instant.now())
                .updated(Instant.now())
                .postStatus(PostStatus.ACTIVE)
                .labels(labelRepositoryGiven)
                .build();


        Mockito.when(postRepository.update(postGiven, connection)).thenReturn(postGiven);
        Mockito.when(postRepository.findById(1L, connection)).thenReturn(Optional.of(postGiven));
        Mockito.when(labelRepository.findByName(labelRepositoryGiven.getFirst().getName(), connection))
                .thenReturn(Optional.empty());
        Mockito.when(labelRepository.save(labelRepositoryGiven.getFirst(), connection))
                        .thenReturn(labelRepositoryGiven.getFirst());

        postService.update(postGiven);

        Mockito.verify(labelRepository, Mockito.times(1)).saveLabelToPost(1L, 1L, connection);
        Mockito.verify(labelRepository, Mockito.times(1)).save(labelRepositoryGiven.getFirst(), connection);
        Mockito.verify(postRepository, Mockito.times(1)).update(postGiven, connection);
        Mockito.verify(labelRepository, Mockito.times(1))
                .findByName(labelRepositoryGiven.getFirst().getName(), connection);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(false);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(true);
        Mockito.verify(connection, Mockito.never()).rollback();
        Mockito.verify(connection, Mockito.times(1)).commit();
        Mockito.verify(connection, Mockito.times(1)).close();

        Assertions.assertNotNull(postGiven.getLabels().getFirst());
        Assertions.assertNotNull(postGiven);
        Assertions.assertEquals(PostStatus.ACTIVE, postGiven.getPostStatus());
        Assertions.assertNotNull(postGiven.getId());
        Assertions.assertNotNull(postGiven.getLabels().getFirst().getId());
        Assertions.assertNotNull(postGiven.getCreated());
        Assertions.assertNotNull(postGiven.getUpdated());
    }

    @Test
    public void savePost_WriterNotExists() throws SQLException {
        List<Label> labelRepositoryGiven = List.of(new Label(1L, "Test"));
        Post postGiven = Post.builder()
                .id(0L)
                .content("TEST")
                .writer(new Writer(1L, null, null, null))
                .created(null)
                .updated(null)
                .postStatus(PostStatus.ACTIVE)
                .labels(labelRepositoryGiven)
                .build();

        Mockito.when(postRepository.save(postGiven, connection)).thenReturn(postGiven);
        Mockito.when(writerRepository.findById(1L, connection)).thenReturn(Optional.empty());
        Mockito.when(labelRepository.save(labelRepositoryGiven.getFirst(), connection))
                .thenReturn(postGiven.getLabels().getFirst());
        Assertions.assertThrows(GeneralException.class, () -> postService.save(postGiven));
        Mockito.verify(postRepository, Mockito.times(1)).save(postGiven, connection);
        Mockito.verify(labelRepository, Mockito.times(1)).save(labelRepositoryGiven.getFirst(), connection);
        Mockito.verify(writerRepository, Mockito.times(1)).findById(1L, connection);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(false);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(true);
        Mockito.verify(connection, Mockito.times(1)).rollback();
        Mockito.verify(connection, Mockito.never()).commit();
        Mockito.verify(connection, Mockito.times(1)).close();
    }

    @Test
    public void savePost_repositorySqlException() throws SQLException {
        List<Label> labelRepositoryGiven = List.of(new Label(1L, "Test"));
        Post postGiven = Post.builder()
                .id(0L)
                .content("TEST")
                .writer(new Writer(1L, null, null, null))
                .created(null)
                .updated(null)
                .postStatus(PostStatus.ACTIVE)
                .labels(labelRepositoryGiven)
                .build();

        Mockito.when(postRepository.save(postGiven, connection)).thenThrow(SQLException.class);
        Assertions.assertThrows(GeneralException.class, () -> postService.save(postGiven));
        Mockito.verify(postRepository, Mockito.times(1)).save(postGiven, connection);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(false);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(true);
        Mockito.verify(connection, Mockito.times(1)).rollback();
        Mockito.verify(connection, Mockito.never()).commit();
        Mockito.verify(connection, Mockito.times(1)).close();
    }
}

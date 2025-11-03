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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    LabelServiceImpl labelService;

    @InjectMocks
    PostServiceImpl postService;

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
                    .labels(List.of(new Label(1L, "Hello"), new Label(2L, "Hello")))
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
                    .labels(List.of(new Label(1L, "Hello"), new Label(2L, "Hello")))
                    .build()
    );

    @Test
    public void getById_PostInRepoExists_WriterNotExists() {
        Long id = postRepositoryGiven.getFirst().getId();
        Mockito.when(postRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(PostNotFoundException.class, () -> postService.getById(id));
        Mockito.verify(postRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void getAll_PostsInRepoExists_LabelsExists_WriterExists() {
        Writer writerRepositoryGiven = Writer.builder()
                .id(1L)
                .firstName("Artem")
                .lastName("Test")
                .build();
        List<Label> labelRepositoryGiven = List.of(new Label(1L, "Test"), new Label(2L, "Test2"));


        Mockito.when(postRepository.findAll())
                .thenReturn(postRepositoryGiven);

        List<Post> result = postService.getAll();
        Mockito.verify(postRepository, Mockito.times(1)).findAll();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirst().getLabels());
        Assertions.assertFalse(result.getFirst().getLabels().isEmpty());
        Assertions.assertFalse(result.getLast().getLabels().isEmpty());
        Assertions.assertNotNull(result.getFirst().getWriter());
        Assertions.assertNotNull(result.getLast().getWriter());
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
    public void delete_PostExists() {
        Mockito.when(postRepository.delete(1L)).thenReturn(true);
        Assertions.assertTrue(postService.delete(1L));
        Mockito.verify(postRepository, Mockito.times(1)).delete(1L);
    }

    @Test
    public void delete_PostNotExists() {
        Mockito.when(postRepository.delete(1L)).thenReturn(false);
        Assertions.assertThrows(PostNotFoundException.class, () -> postService.delete(1L));
        Mockito.verify(postRepository, Mockito.times(1)).delete(1L);
    }

    @Test
    public void savePostWriterExists() {
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


        Mockito.when(postRepository.save(postGiven)).thenReturn(returned);
        Mockito.when(labelService.findOrCreateLabels(postGiven.getLabels())).thenReturn(labelRepositoryGiven);
        postService.save(postGiven);

        Mockito.verify(postRepository, Mockito.times(1)).save(postGiven);

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
    public void updatePost_LabelExists_WriterExists() {
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


        Mockito.when(labelService.findOrCreateLabels(postGiven.getLabels())).thenReturn(labelRepositoryGiven);
        Mockito.when(postRepository.update(postGiven)).thenReturn(postGiven);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(postGiven));

        postService.update(postGiven);

        Mockito.verify(postRepository, Mockito.times(1)).update(postGiven);
        Assertions.assertNotNull(postGiven);
        Assertions.assertEquals(PostStatus.ACTIVE, postGiven.getPostStatus());
        Assertions.assertNotNull(postGiven.getId());
        Assertions.assertNotNull(postGiven.getLabels().getFirst().getId());
        Assertions.assertNotNull(postGiven.getCreated());
        Assertions.assertNotNull(postGiven.getUpdated());
    }

    @Test
    public void updatePost_LabelNotExists_WriterExists() {
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

        Mockito.when(labelService.findOrCreateLabels(postGiven.getLabels())).thenReturn(labelRepositoryGiven);
        Mockito.when(postRepository.update(postGiven)).thenReturn(postGiven);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(postGiven));

        postService.update(postGiven);

        Mockito.verify(postRepository, Mockito.times(1)).update(postGiven);
        Assertions.assertNotNull(postGiven.getLabels().getFirst());
        Assertions.assertNotNull(postGiven);
        Assertions.assertEquals(PostStatus.ACTIVE, postGiven.getPostStatus());
        Assertions.assertNotNull(postGiven.getId());
        Assertions.assertNotNull(postGiven.getLabels().getFirst().getId());
        Assertions.assertNotNull(postGiven.getCreated());
        Assertions.assertNotNull(postGiven.getUpdated());
    }

    @Test
    public void savePost_WriterNotExists() {
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

        Mockito.when(labelService.findOrCreateLabels(postGiven.getLabels())).thenReturn(labelRepositoryGiven);
        Mockito.when(postRepository.save(postGiven)).thenThrow(GeneralException.class);
        Assertions.assertThrows(GeneralException.class, () -> postService.save(postGiven));
        Mockito.verify(postRepository, Mockito.times(1)).save(postGiven);
    }
}

package com.tmq.mapper;

import com.tmq.dto.PostDto;
import com.tmq.model.Post;

public class PostMapper implements Mapper<Post, PostDto> {
    private final WriterMapper writerMapper = new WriterMapper();
    private final LabelMapper labelMapper = new LabelMapper();

    @Override
    public Post toEntity(PostDto postDto) {
        return Post.builder()
                .id(postDto.id())
                .content(postDto.content())
                .writer(writerMapper.toEntity(postDto.writer()))
                .created(postDto.created())
                .updated(postDto.updated())
                .labels(postDto.labels().stream().map(labelMapper::toEntity).toList())
                .postStatus(postDto.postStatus())
                .build();
    }

    @Override
    public PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .writer(writerMapper.fromEntity(post.getWriter()))
                .created(post.getCreated())
                .updated(post.getUpdated())
                .labels(post.getLabels().stream().map(labelMapper::fromEntity).toList())
                .postStatus(post.getPostStatus())
                .build();
    }
}

package com.tmq.mapper;

public interface Mapper<E, D>{
    E toEntity(D d);
    D fromEntity(E e);
}

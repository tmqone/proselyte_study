package com.tmq.service;

import java.util.List;

public interface Service<DTO> {
    public DTO save(DTO dto);
    public DTO update(DTO dto);
    public DTO delete(DTO dto);
    public DTO findById(DTO id);
    public List<DTO> findAll();
}

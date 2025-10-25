package com.tmq.service;

import com.tmq.exception.LabelNotFoundException;
import com.tmq.model.Label;
import com.tmq.repository.LabelRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    @Override
    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label getByName(String name) {
        return labelRepository.findByName(name)
                .orElseThrow(LabelNotFoundException::new);
    }

    @Override
    public Label getById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(LabelNotFoundException::new);
    }

    @Override
    public Label save(Label label) {
        return labelRepository.save(label);
    }

    @Override
    public List<Label> save(List<Label> labels) {
        return labels.stream().map(labelRepository::save).toList();
    }

    @Override
    public boolean saveLabelToPost(Long labelId, Long postId) {
        return labelRepository.saveLabelToPost(labelId, postId);
    }

    @Override
    public Label update(Label label) {
        return labelRepository.update(label);
    }

    @Override
    public boolean delete(Long id) {
        return labelRepository.delete(id);
    }
}

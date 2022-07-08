package ru.cft.freelanceservice.repository;

import ru.cft.freelanceservice.repository.model.SampleEntity;

import java.util.List;

public interface SampleRepository {

    public List<SampleEntity> selectAll();

}

package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.repository.SampleRepository;
import ru.cft.freelanceservice.repository.model.SampleEntity;
import ru.cft.freelanceservice.service.RegisterService;

import java.util.List;

@Service
public class SampleServiceImpl implements RegisterService {

    private final SampleRepository sampleRepository;

    @Autowired
    public SampleServiceImpl(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public List<SampleEntity> getAllSample() {
        return sampleRepository.selectAll();
    }
}

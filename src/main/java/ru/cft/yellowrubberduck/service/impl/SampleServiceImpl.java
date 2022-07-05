package ru.cft.yellowrubberduck.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.yellowrubberduck.repository.SampleRepository;
import ru.cft.yellowrubberduck.repository.model.SampleEntity;
import ru.cft.yellowrubberduck.service.SampleService;

import java.util.List;

@Service
public class SampleServiceImpl implements SampleService {

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

package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.repository.RegisterRepository;
import ru.cft.freelanceservice.service.RegisterService;

import java.util.List;

@Service
public class SampleServiceImpl implements RegisterService {

    private final RegisterRepository sampleRepository;

    @Autowired
    public SampleServiceImpl(RegisterRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public List<SampleEntity> getAllSample() {
        return sampleRepository.selectAll();
    }
}

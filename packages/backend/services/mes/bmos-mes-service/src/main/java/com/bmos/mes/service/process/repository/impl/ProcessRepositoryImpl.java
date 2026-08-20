package com.bmos.mes.service.process.repository.impl;

import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessRepositoryImpl implements ProcessRepository {

    @Autowired
    private ProcessMapper processMapper;

    @Override
    public Process getById(Long id) {
        return processMapper.selectById(id);
    }
}

package com.bmos.mes.service.process.repository;

import com.bmos.mes.service.process.model.Process;

/**
 * 共其他模块调用的service
 */
public interface ProcessRepository {

    /**
     * 根据id查询工艺
     * @param id
     * @return
     */
    Process getById(Long id);

}

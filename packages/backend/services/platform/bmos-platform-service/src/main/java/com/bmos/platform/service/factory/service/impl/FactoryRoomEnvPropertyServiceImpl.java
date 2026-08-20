package com.bmos.platform.service.factory.service.impl;

import com.bmos.platform.service.factory.mapper.BpFactoryRoomEnvPropertyMapper;
import com.bmos.platform.service.factory.service.FactoryRoomEnvPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (BpFactoryRoomEnvProperty)表服务实现类
 *
 * @author makejava
 * @since 2024-12-30 10:04:54
 */
@Service("bpFactoryRoomEnvPropertyService")
public class FactoryRoomEnvPropertyServiceImpl implements FactoryRoomEnvPropertyService {
    @Resource
    private BpFactoryRoomEnvPropertyMapper bpFactoryRoomEnvPropertyMapper;
}

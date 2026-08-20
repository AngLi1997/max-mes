package com.bmos.mes.service.plan.document.service;

import com.bmos.mes.service.plan.info.vo.PlanPageVO;

import java.util.List;

/**
 * 计划归档服务
 * <p>
 * 用于计划归档
 *
 * @author yigaohui
 * @date 2024/6/6
 **/
public interface IPlanArchiveService {

    /**
     * 计划归档
     *
     * @param planId 计划id
     * @return 归档是否成功
     */
    boolean archive(Long planId);
}

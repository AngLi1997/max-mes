package com.bmos.lims2.server.eln.entry.service;

import com.bmos.lims2.server.eln.entry.dto.FormDataAnnotationSaveDTO;
import com.bmos.lims2.server.eln.entry.dto.FormDataListQueryDTO;
import com.bmos.lims2.server.eln.entry.vo.FormDataAnnotationVO;

import java.util.List;

/**
 * @Description: 执行表单数据-异常批注 服务接口
 * @Author: yigaohui
 * @Date: 2025/12/05 00:00
 */
public interface ExecuteFormDataAnnotationService {

    /**
     * 保存异常批注
     * @param dto 批注保存数据
     */
    void save(FormDataAnnotationSaveDTO dto);

    /**
     * 查询批注集合（查询条件与数据历史一致）
     * @param dto 查询条件
     * @return 批注集合
     */
    List<FormDataAnnotationVO> getAnnotationList(FormDataListQueryDTO dto);
}



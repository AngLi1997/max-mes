package com.bmos.mes.service.tareweigh.config.service;

import com.bmos.mes.service.tag.dto.ScanTareWeighTagDTO;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigCreateDTO;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigEditDTO;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigQuery;
import com.bmos.mes.service.tareweigh.config.vo.TareWeighConfigVO;
import com.bmos.mybatis.page.CommonPage;

import javax.annotation.Nullable;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:33
 */
public interface ITareWeighConfigService {

    /**
     * 分页查询皮重配置
     * @param query
     * @return
     */
    CommonPage<TareWeighConfigVO> queryPage(TareWeighConfigQuery query);

    /**
     * 根据id查询皮重配置信息
     * @param id
     * @return
     */
    @Nullable
    TareWeighConfigVO queryTareWeighConfigById(Long id);

    /**
     * 创建皮重配置
     * @param dto
     */
    void createTareWeighConfig(TareWeighConfigCreateDTO dto);

    /**
     * 修改皮重配置
     * @param dto
     */
    void editTareWeighConfig(TareWeighConfigEditDTO dto);

    /**
     * 删除皮重配置
     * @param id
     */
    void deleteTareWeighConfig(Long id);

    /**
     * 扫描皮重管理标签
     * @param dto
     * @return
     */
    TareWeighConfigVO scanTareWeighTag(ScanTareWeighTagDTO dto);
}

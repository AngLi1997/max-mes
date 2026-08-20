package com.bmos.mes.service.inspect.service;

import com.bmos.mes.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigPageVO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigBindMaterialDTO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigPageDTO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigSaveDTO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigUpdateDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface InspectConfigService {
    /**
     * 保存请验单
     * @param dto
     */
    void save(InspectConfigSaveDTO dto);

    /**
     * 更新请验单
     * @param dto
     */
    void update(InspectConfigUpdateDTO dto);

    /**
     * 删除请验单
     * @param id
     */
    void delete(Long id);

    /**
     * 启用请验单
     * @param id
     */
    void enable(Long id);

    /**
     * 停用请验单
     * @param id
     */
    void disable(Long id);

    /**
     * 获取请验单详情
     * @param id
     * @return
     */
    InspectConfigDetailVO queryDetail(Long id);

    /**
     * 获取请验单列表
     *
     * @param dto
     * @return
     */
    CommonPage<InspectConfigPageVO> queryList(InspectConfigPageDTO dto);

    /**
     * 请验单绑定物料
     * @param dto
     */
    void bindMaterial(InspectConfigBindMaterialDTO dto);

    /**
     * 查询绑定的物料
     * @param id
     * @return
     */
    List<Long> queryBindMaterial(Long id);

    /**
     * 根据配方物料id查询请验单配置数据以及请验单信息（一个物料可能对应多个请验单）
     * @param formulaMaterialId
     * @return
     */
    java.util.List<InspectConfigDetailVO> queryConfigByFormulaMaterialId(Long formulaMaterialId);
}

package com.bmos.platform.service.execute.parameter.service;

import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.bmos.platform.service.execute.parameter.dto.BusinessParameterPageDTO;
import com.bmos.platform.service.execute.parameter.dto.UpdateBusinessParameterDTO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterPageVO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterVO;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public interface BusinessParameterService {
    /**
     * 分页查询
     * @param dto dto
     * @return List<BusinessParameterPageVO>
     */
    List<BusinessParameterPageVO> page(BusinessParameterPageDTO dto);

    /**
     * 详情查询
     * @param id id
     * @return BusinessParameterDetailVO
     */
    BusinessParameterDetailVO detail(Long id);

    /**
     * 详情查询
     * @param code code
     * @return BusinessParameterDetailVO
     */
    BusinessParameterDetailVO detailByCode(String code);

    /**
     * 更新
     * @param dto dto
     */
    void update(UpdateBusinessParameterDTO dto);

    /**
     * 查询参数详情
     * @param code
     * @return
     */
    BusinessParameterDetailFeignVO detailFeignByCode(String code);

    /**
     * 获取所有参数
     * @return
     */
    List<BusinessParameterVO> getAllParam();

}

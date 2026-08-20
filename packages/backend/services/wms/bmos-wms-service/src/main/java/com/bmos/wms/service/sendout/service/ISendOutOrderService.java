package com.bmos.wms.service.sendout.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.sendout.dto.SendOutDTO;
import com.bmos.wms.service.sendout.dto.SendPageQuery;
import com.bmos.wms.service.sendout.dto.SendSubmitDTO;
import com.bmos.wms.service.sendout.vo.SendOutOrderDetailVO;
import com.bmos.wms.service.sendout.vo.SendOutOrderVO;

import javax.annotation.Nullable;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/15 09:30
 */
public interface ISendOutOrderService {

    /**
     * MES提交领料单
     *
     * @param dto 领料单
     */
    void submitSendOutOrderByBatch(SendSubmitDTO dto);

    /**
     * 查询发料分页
     *
     * @param pageQuery
     * @return
     */
    CommonPage<SendOutOrderVO> queryPage(SendPageQuery pageQuery);

    /**
     * 取消发料
     *
     * @param id 发料工单id
     */
    void cancelSendOut(Long id);

    /**
     * 根据发料工单id查询申请发料详情
     *
     * @param id
     * @return
     */
    @Nullable
    SendOutOrderDetailVO queryDetail(Long id);

    /**
     * 发料
     *
     * @param dto
     */
    void sendOut(SendOutDTO dto);
}

package com.bmos.mes.service.weigh.free.service;

import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.weigh.free.dto.FreeWeighDTO;
import com.bmos.mes.service.weigh.free.dto.FreeWeighHistoryPageQuery;
import com.bmos.mes.service.weigh.free.vo.FreeWeighHistoryPage;
import com.bmos.mes.service.weigh.free.vo.FreeWeighResult;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/2/25 10:01
 */
public interface IFreeWeighService {

    /**
     * 称量打码
     * @param dto
     * @return
     */
    FreeWeighResult weighAndPrint(FreeWeighDTO dto);

    /**
     * 获取称具列表
     * @return
     */
    List<WeighBalanceEquipment> getBalanceList();

    /**
     * 查询历史
     * @param pageQuery
     * @return
     */
    CommonPage<FreeWeighHistoryPage> queryHistoryPage(FreeWeighHistoryPageQuery pageQuery);
}

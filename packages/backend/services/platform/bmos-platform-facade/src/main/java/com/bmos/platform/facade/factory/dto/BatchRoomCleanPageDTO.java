package com.bmos.platform.facade.factory.dto;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;

/**
 * 批次编号
 */
@Data
public class BatchRoomCleanPageDTO extends BasePage {

    /**
     * 生产批号
     */
    private String batchNo;

}

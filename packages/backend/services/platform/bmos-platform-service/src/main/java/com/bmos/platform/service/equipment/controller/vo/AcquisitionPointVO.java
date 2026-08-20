package com.bmos.platform.service.equipment.controller.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointStatusEnum;
import com.bmos.platform.service.utils.UserUtils;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@Data
public class AcquisitionPointVO extends AcquisitionPointAddVO {
    private String id;

    /**
     * 状态
     */
    private AcquisitionPointStatusEnum status;



    /**
     * 设备数据code
     */
    private String equipmentTagDataCode;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;

    private String createByName;

    private String updateByName;
}

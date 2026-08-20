package com.bmos.wms.service.log.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.logging.model.LogModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("bw_operation_log")
public class WmsLogModel extends LogModel {

}



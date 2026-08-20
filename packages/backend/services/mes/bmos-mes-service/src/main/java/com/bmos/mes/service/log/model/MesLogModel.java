package com.bmos.mes.service.log.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.logging.model.LogModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@TableName("bm_operation_log")
public class MesLogModel extends LogModel implements Serializable {

}



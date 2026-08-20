package com.bmos.platform.service.log.mq;


import com.bmos.logging.model.LogModel;
import com.bmos.mes.mq.BaseMqTopic;
import com.bmos.mes.mq.annotation.Topic;

@Topic("OPERATION_LOG_TOPIC")
public interface OperationLogTopic extends BaseMqTopic<LogModel>{
}

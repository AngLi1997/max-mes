package com.bmos.platform.service.log.mq;


import com.bmos.logging.model.LogModel;
import com.bmos.mes.mq.annotation.Consumer;
import com.bmos.platform.service.log.convert.PlatformLogConvert;
import com.bmos.platform.service.log.mapper.OperationLogMapper;
import com.bmos.platform.service.log.model.OperationLogModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Consumer(groupId = "operation-log-group-consumer")
public class OperationLogConsume implements OperationLogTopic {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public void consume(LogModel logModel) {
        // 当消费者消费不过来可以使用多线程快速消费
        log.info("收到其他系统的日志插入消息 {}", logModel);
        try{
            OperationLogModel operationLogModel = PlatformLogConvert.INSTANCE.convert2OperationLogModel(logModel);
            operationLogMapper.insert(operationLogModel);
        } catch (Exception e){
            log.error("日志插入失败 {}", logModel, e);
        }

    }
}

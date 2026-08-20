package com.bmos.lims2.server.config.audit;

import com.bmos.lims2.server.audit.utils.AuditMessageSendUtils;
import com.bmos.platform.facade.notify.MessageNotifyFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AuditSendMessageConfiguration implements CommandLineRunner {

    @Autowired
    private MessageNotifyFeign messageNotifyFeign;

    @Override
    public void run(String... args) {
        AuditMessageSendUtils.init(messageNotifyFeign);
    }
}

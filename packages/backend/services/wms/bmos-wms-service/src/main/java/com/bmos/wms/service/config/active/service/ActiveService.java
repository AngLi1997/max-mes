package com.bmos.wms.service.config.active.service;

import com.bmos.adaptor.active.ActiveApiAdaptor;

public interface ActiveService extends ActiveApiAdaptor {
    String save(String activeStr);
}

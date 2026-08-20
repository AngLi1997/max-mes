package com.bmos.mes.service.active.service;

import com.bmos.adaptor.active.ActiveApiAdaptor;

public interface ActiveService extends ActiveApiAdaptor {
    String save(String activeStr);
}

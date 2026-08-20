package com.bmos.lims2.server.active.service;

import com.bmos.adaptor.active.ActiveApiAdaptor;

public interface ActiveService extends ActiveApiAdaptor {
    String save(String activeStr);
}

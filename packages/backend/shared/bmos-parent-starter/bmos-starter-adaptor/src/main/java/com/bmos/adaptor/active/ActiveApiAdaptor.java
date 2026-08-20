package com.bmos.adaptor.active;

public interface ActiveApiAdaptor {
    String getActiveCode();

    // 是否已激活
    RsaVO actived();
}

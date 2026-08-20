package com.bmos.platform.service.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CommonFeignClientFactory {

    @Autowired
    private LimsFeignClient limsFeignClient;

    @Autowired
    private MesFeignClient mesFeignClient;

    @Resource
    private WmsFeignClient wmsFeignClient;

    private final String MES_SERVICE_NAME = "bmos-mes-service";

    private final String LIMS_SERVICE_NAME = "bmos-lims2-service";

    private final String WMS_SERVICE_NAME = "bmos-wms-service";

    public CommonFeignClient getFeignClient(String serviceName){
        switch (serviceName){
            case MES_SERVICE_NAME:
                return mesFeignClient;
            case LIMS_SERVICE_NAME:
                return limsFeignClient;
            case WMS_SERVICE_NAME:
                return wmsFeignClient;
            default:
                break;
        }
        return null;
    }


}

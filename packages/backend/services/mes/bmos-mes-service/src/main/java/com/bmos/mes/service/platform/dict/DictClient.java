package com.bmos.mes.service.platform.dict;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.platform.dict.vo.DictVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-productplan-dict")
public interface DictClient {
    @GetMapping("/api/app/platform/dict/list/dict/down")
    ResponseInfo<List<DictVO>> listDictDown(@RequestParam("dictId") Long dictId);
}

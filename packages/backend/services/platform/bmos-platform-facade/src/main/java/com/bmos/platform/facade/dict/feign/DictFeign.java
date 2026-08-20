package com.bmos.platform.facade.dict.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.platform.facade.dict.enums.DictCategoryEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典相关feign接口
 */
@FeignClient(name = "bmos-platform-service", contextId = "platform-dict")
public interface DictFeign {

    /**
     * 根据字典编码查询字典数据
     * @param code: 字典类型编码
     */
    @GetMapping("/api/app/platform/feign/dict/selectDictDetailByCode")
    ResponseInfo<DictDetailFeignVO> selectDictDetailByCode(@RequestParam("code") String code);

    /**
     * 根据字典分类查询字典数据
     * @param dictTypeList
     * @return
     */
    @GetMapping("/api/app/platform/feign/dict/selectDictByCategory")
    ResponseInfo<List<DictDetailFeignVO>> selectDictByCategory(@RequestParam("dictTypeList") List<String> dictTypeList);

}

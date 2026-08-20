package com.bmos.platform.service.dict.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.platform.facade.dict.enums.DictCategoryEnum;
import com.bmos.platform.facade.dict.feign.DictFeign;
import com.bmos.platform.service.dict.service.DictService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典对外接口实现
 */
@RestController
@RequestMapping("/feign/dict")
@Validated
@Api(tags = "字典配置相关接口")
public class DictFeignController implements DictFeign {

    @Autowired
    DictService dictService;

    @Override
    @GetMapping("/selectDictDetailByCode")
    public ResponseInfo<DictDetailFeignVO> selectDictDetailByCode(@RequestParam("code") String code) {
        return ResponseInfo.success(dictService.selectDictDetailByCode(code));
    }
    @Override
    @GetMapping("/selectDictByCategory")
    public ResponseInfo<List<DictDetailFeignVO>> selectDictByCategory(@RequestParam("dictTypeList") List<String> dictTypeList) {
        return ResponseInfo.success(dictService.selectDictByCategory(dictTypeList));
    }
}

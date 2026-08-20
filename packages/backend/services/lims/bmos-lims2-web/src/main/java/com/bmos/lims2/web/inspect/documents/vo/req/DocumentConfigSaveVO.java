package com.bmos.lims2.web.inspect.documents.vo.req;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.document.dto.DocumentConfigFieldSaveDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApiModel("新增请验单VO")
@Data
public class DocumentConfigSaveVO {

    @NotBlank
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty("请验单数据")
    private List<DocumentConfigFieldSaveDTO> dataList;


    public void validateParams() {
        if (CollUtil.isEmpty(dataList)) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_DATA_CANT_BE_EMPTY);
        }
        // 校验dataList中的code是否重复
        Set<String> codeSet = new HashSet<>();
        for (DocumentConfigFieldSaveDTO data : dataList) {
            if (codeSet.contains(data.getCode())) {
                throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_DATA_CODE_EXIST);
            }
            codeSet.add(data.getCode());
        }
    }

}

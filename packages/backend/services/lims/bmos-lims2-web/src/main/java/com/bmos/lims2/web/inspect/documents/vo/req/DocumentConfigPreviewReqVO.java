package com.bmos.lims2.web.inspect.documents.vo.req;

import com.bmos.lims2.server.inspect.document.dto.DocumentConfigFieldSaveDTO;
import com.bmos.lims2.web.inspect.order.vo.req.CustomFieldValueVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("请验单配置预览请求（未保存字段值）")
public class DocumentConfigPreviewReqVO {


    @ApiModelProperty(value = "自定义字段值列表，可选")
    private List<DocumentConfigFieldSaveDTO> fields;
}



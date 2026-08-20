package com.bmos.platform.service.tag.vo;

import com.bmos.platform.service.tag.dto.TagInstanceField;
import com.bmos.platform.common.enums.tag.PrintCmdType;
import com.bmos.platform.service.tag.model.TagDefine;
import com.bmos.platform.service.tag.model.TagScene;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 标签实例详情vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:14
 */
@Data
@ApiModel("标签实例详情vo")
public class TagInstanceDetailVO {

    /**
     * 标签实例id
     */
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签类型名称
     */
    private Long tagTypeId;

    /**
     * 标签场景名称
     */
    private Long tagSceneId;

    /**
     * 标签定义id
     */
    private Long tagDefineId;

    /**
     * 可用字段
     */
    private List<TagSceneFieldVO> availableFields;

    /**
     * 标签字段配置
     */
    private List<TagInstanceField> configFields;

    /**
     * 标签宽度(mm)
     */
    private Integer tagWidth;

    /**
     * 标签高度(mm)
     */
    private Integer tagHeight;

    /**
     * 指令模板
     */
//    private String cmd;

    /**
     * 指令类型
     */
    private PrintCmdType cmdType;

    /**
     * 预览html模板
     */
    private String previewHtml;

    /**
     * 数据源服务名称
     */
    private String dataSourceServiceName;

    /**
     * 数据源接口地址
     */
    private String dataSourceInterface;

    /**
     * 二维码字段（标签显示的二维码信息， 来源与数据源接口）
     */
    private String qrCodeField;

    public void setTagDefine(TagDefine tagDefine) {
        this.tagDefineId = tagDefine.getId();
        this.setTagWidth(tagDefine.getTagWidth());
        this.setTagHeight(tagDefine.getTagHeight());
        this.setCmdType(tagDefine.getCmdType());
        this.setPreviewHtml(tagDefine.getPreviewHtml());
    }

    public void setTagScene(TagScene tagScene) {
        this.tagSceneId = tagScene.getId();
        this.setDataSourceServiceName(tagScene.getDataSourceServiceName());
        this.setDataSourceInterface(tagScene.getDataSourceInterface());
        this.setQrCodeField(tagScene.getQrCodeField());
    }
}

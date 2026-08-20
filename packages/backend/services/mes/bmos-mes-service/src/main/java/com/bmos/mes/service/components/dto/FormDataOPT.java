package com.bmos.mes.service.components.dto;

import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.weigh.centre.config.util.BmosTreeNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/29 11:33
 */
@Getter
@NoArgsConstructor
public class FormDataOPT implements BmosTreeNode<FormDataOPT, Long, Long> {

    @JsonIgnore
    private Long componentId;

    @JsonIgnore
    private Long componentParentId;

    @JsonIgnore
    private Long fieldId;

    private String componentType;

    private String componentDetail;

    private String configJson;

    @JsonIgnore
    private Long recordItemId;

    @JsonIgnore
    private Long formDataId;

    @Setter
    private String value;

    private List<FormDataOPT> children;

    public FormDataOPT(Long componentId, Long componentParentId, Long fieldId, String componentType, String componentDetail, Long recordItemId, String configJson) {
        this.componentId = componentId;
        this.componentType = componentType;
        this.componentDetail = componentDetail;
        this.fieldId = fieldId;
        this.componentParentId = componentParentId;
        this.recordItemId = recordItemId;
        this.configJson = configJson;
    }

    public void setFormData(ExecuteFormData executeFormData){
        if (executeFormData == null){
            return;
        }
        this.formDataId = executeFormData.getId();
        this.value = executeFormData.getValue();
    }

    @Override
    @JsonIgnore
    public Long getId() {
        return componentId;
    }

    @Override
    @JsonIgnore
    public Long getParentId() {
        return componentParentId;
    }

    @Override
    public void addChild(BmosTreeNode<FormDataOPT, Long, Long> child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add((FormDataOPT) child);
    }

    @Override
    public Long getSortBy() {
        return componentId;
    }
}

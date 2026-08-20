package com.bmos.platform.service.material.dto;

import com.bmos.platform.service.material.model.MaterialCategory;
import com.bmos.platform.service.material.vo.IssueMaterialVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemoteIssueDTO {

    private List<IssueMaterialVO> materialList;

    private List<MaterialCategory> categoryList;

    private List<Integer> businesses;

}

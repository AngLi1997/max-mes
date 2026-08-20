package com.bmos.lims2.server.eln.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("记录项排序更新DTO")
@Data
public class RecordItemSortUpdateDTO {

    @ApiModelProperty("记录版本id")
    @NotNull
    private Long recordVersionId;

    @NotEmpty
    @ApiModelProperty("记录排序")
    @Valid
    private List<ItemSortDTO> itemList;

    @Data
    @ApiModel("记录项排序DTO")
    public static class ItemSortDTO {

        @NotNull
        @ApiModelProperty("记录项id")
        private Long id;

        @NotNull
        @ApiModelProperty("记录排序")
        private Integer sort;
    }



}

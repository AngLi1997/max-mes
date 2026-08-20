package com.bmos.mes.service.dataset.util.options;

import cn.hutool.core.map.multi.RowKeyTable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 工序换班照片数据
 */
@Getter
@Setter
public class ChangeNumberPhotoData {

    private RowKeyTable<Integer, Integer, List<DocxTakePhotoLegendReplaceOption.TakePhotoData>> photoTable;

}

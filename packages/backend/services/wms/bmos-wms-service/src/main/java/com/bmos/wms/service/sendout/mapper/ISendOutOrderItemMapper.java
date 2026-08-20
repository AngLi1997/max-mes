package com.bmos.wms.service.sendout.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.sendout.model.SendOutOrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/15 09:33
 */
@Mapper
public interface ISendOutOrderItemMapper extends BaseMapperX<SendOutOrderItem> {


    /**
     * 根据发料工单id查询发料工单明细
     *
     * @param id 发料工单id
     * @return
     */
    default List<SendOutOrderItem> queryListBySendOrderId(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(SendOutOrderItem.class)
                .eq(SendOutOrderItem::getSendOrderId, id)
        );
    }
}

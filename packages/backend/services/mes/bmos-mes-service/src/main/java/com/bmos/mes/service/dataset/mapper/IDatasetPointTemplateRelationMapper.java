package com.bmos.mes.service.dataset.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.dataset.model.DatasetPointTemplateRelation;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/30 11:13
 */
@Mapper
public interface IDatasetPointTemplateRelationMapper extends BaseMapperX<DatasetPointTemplateRelation> {

    default List<DatasetPointTemplateRelation> selectByTemplateUrl(String templateUrl){
        if (StrUtil.isBlank(templateUrl)){
            return new ArrayList<>();
        }
        return selectList(DatasetPointTemplateRelation::getTemplateUrl, templateUrl);
    }

    default void saveRelations(Collection<String> placeHolders, String fileUrl){
        if (StrUtil.isNotBlank(fileUrl)){
            // 清空模板占位符信息
            delete(new LambdaQueryWrapper<DatasetPointTemplateRelation>()
                    .eq(DatasetPointTemplateRelation::getTemplateUrl, fileUrl)
            );
        }
        List<DatasetPointTemplateRelation> list = new ArrayList<>();
        Pattern compile = Pattern.compile("\\(([^)]+)\\)");
        for (String placeholder : placeHolders) {
            Matcher matcher = compile.matcher(placeholder);
            while (matcher.find()) {
                String group = matcher.group(1);
                String[] split = group.split(",");
                List<String> dsList = new ArrayList<>();
                List<String> dpList = new ArrayList<>();
                for (String s : split) {
                    String[] p = s.split("\\.");
                    if (p.length != 2){
                        continue;
                    }
                    dsList.add(p[0]);
                    dpList.add(p[1]);
                }
                DatasetPointTemplateRelation datasetPointTemplateRelation = new DatasetPointTemplateRelation();
                datasetPointTemplateRelation.setTemplateUrl(fileUrl);
                datasetPointTemplateRelation.setPlaceholder(placeholder);
                datasetPointTemplateRelation.setKeySize(split.length);
                datasetPointTemplateRelation.setDatasetKeys(JSON.toJSONString(dsList));
                datasetPointTemplateRelation.setDatasetPointKeys(JSON.toJSONString(dpList));
                list.add(datasetPointTemplateRelation);
            }
        }
        if (CollectionUtil.isNotEmpty(list)){
            insertBatch(list);
        }
    }
}

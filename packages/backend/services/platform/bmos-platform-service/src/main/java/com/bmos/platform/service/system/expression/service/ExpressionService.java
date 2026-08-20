package com.bmos.platform.service.system.expression.service;

import com.bmos.platform.service.system.expression.dto.*;
import com.bmos.platform.service.system.expression.vo.ExpressionPageVO;
import com.bmos.platform.service.system.expression.vo.ExpressionTreeNodeVO;
import com.bmos.platform.service.system.expression.vo.MesRecordTreeNodeVO;

import java.util.List;
import java.util.Set;

public interface ExpressionService {
    /**
     * 分页查询
     *
     * @param dto dto
     * @return List<ExpressionPageVO>
     */
    List<ExpressionPageVO> page(ExpressionPageDTO dto);

    List<ExpressionPageVO> list();

    /**
     * 保存
     *
     * @param dto dto
     */
    void save(ExpressionSaveDTO dto);

    /**
     * 更新
     *
     * @param dto dto
     */
    void update(ExpressionUpdateDTO dto);

    /**
     * 确认
     * @param id id
     */
    void confirm(Long id);

    /**
     * 公式删除
     *
     * @param id id
     */
    void delete(Long id);

    /**
     * 公式分类删除
     *
     * @param id id
     */
    void deleteCategory(Long id);

    /**
     * 公式解析
     *
     * @param expression 公式
     * @return Set<String>
     */
    Set<String> parse(String expression);

    /**
     * 获取全量公式树
     *
     * @return 公式树
     */
    List<ExpressionTreeNodeVO> getFullExpressionAndCategoryList(Boolean tree);

    /**
     * 表达式计算
     * @param
     * @return
     */
    String calculateExpression(ExpressionCalculateDTO dto);

    /**
     * 公式验证通过
     * @param id
     */
    void verify(Long id);

    /**
     * 获取公式记录绑定树
     * @param id
     * @return
     */
    List<MesRecordTreeNodeVO> getRecordBindTree(Long id);

    /**
     * 公式绑定MES记录
     * @param dto
     */
    void bindBatchRecord(ExpressionBindRecordDTO dto);

    /**
     * 根据公式id获取绑定的记录id列表
     * @param id
     * @return
     */
    List<Long> getBoundRecordIdList(Long id);
}

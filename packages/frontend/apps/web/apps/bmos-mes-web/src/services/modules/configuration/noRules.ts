import request from '../../service';
// 班组配置接口

// 分页列表
export const getNoRulesPage = (params:any)=>{
  return request({
    url: '/app/mes/plan/code/rule/page',
    method: 'GET',
    params,
  })
}

// 编辑
export const updateNoRules = (data:any)=>{
  return request({
    url: '/app/mes/plan/code/rule/update',
    method: 'PUT',
    data,
  })
}

// 查询字典下拉框(用于查编辑框里的生产批号/编号规则下拉)
export const getDictNoRulesList = (params:any)=>{
  return request({
    url: '/app/mes/platform/query/list/dict/down',
    method: 'GET',
    params,
  })
}


// 查询工艺产品树
export const getRulesProcessList = ()=>{
  return request({
    url: '/app/mes/process/product/tree',
    method: 'GET',
  })
}

// 编码规则code查询工艺集合(用于回显工序树的选中)
export const getRulesDetailCode = (code:any)=>{
  return request({
    url: `/app/mes/plan/code/rule/detailCode/${code}`,
    method: 'POST',
  })
}

// 批量配置弹框的确定按钮
export const batchConfigurationSave = (data:any)=>{
  return request({
    url: `/app/mes/plan/code/rule/save`,
    method: 'POST',
    data
  })
}



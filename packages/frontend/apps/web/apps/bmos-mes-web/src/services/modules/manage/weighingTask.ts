import request from '../../service';
// 称量任务相关接口

// 获取称量任务管理列表
export const reqWeighCentreTaskQueryPage = (params: any) => {
  return request({
    url: '/app/mes/weigh/centre/task/queryPage',
    method: 'GET',
    params,
  });
};

//确认任务
export const reqWeighCentreTaskMakeSure = (params: any) => {
  return request({
    url: '/app/mes/weigh/centre/task/makeSure',
    method: 'PUT',
    params,
  })
}

//下发任务
export const reqWeighCentreTaskSend = (params: any) => {
  return request({
    url: '/app/mes/weigh/centre/task/send',
    method: 'PUT',
    params,
  })
}

//取消任务
export const reqWeighCentreTaskCancel = (params: any) => {
  return request({
    url: '/app/mes/weigh/centre/task/cancel',
    method: 'PUT',
    params,
  })
}

//编辑时保存
export const reqWeighCentreTaskEdit = (data: any) => {
  return request({
    url: '/app/mes/weigh/centre/task/edit',
    method: 'PUT',
    data,
  })
}

//任务规划(保存按钮)
export const reqWeighCentreTaskProgramManual = (data: any) => {
  return request({
    url: '/app/mes/weigh/centre/task/programManual',
    method: 'POST',
    data,
  })
}

//自动规划
export const reqWeighCentreTaskProgramAuto = () => {
  return request({
    url: '/app/mes/weigh/centre/task/programAuto',
    method: 'POST',
  })
}

//查询任务详情和称量需求分页(点查看和编辑时调用)
export const reqWeighCentreTaskQueryRequirementListByTaskId = (params: any) => {
  return request({
    url: '/app/mes/weigh/centre/task/queryRequirementListByTaskId',
    method: 'POST',
    params
  })
}

// 查询称量任务对应的物料、称量中心、单位详情相同的未规划的称量需求列表(点添加物料时调用)
export const reqWeighCentreTaskQueryUnPlanedRequirementListByTaskId = (params: any) => {
  return request({
    url: '/app/mes/weigh/centre/task/queryUnPlanedRequirementListByTaskId',
    method: 'GET',
    params,
  })
}

// 任务规划页面列表
export const reqWeighCentreRequirementQueryPage = (params: any) => {
  return request({
    url: '/app/mes/weigh/centre/requirement/queryPage',
    method: 'GET',
    params,
  });
};
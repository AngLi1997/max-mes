import request from '../../service';
// 指令单分解接口

// 分页列表 指令单分解页面 需传递 orderBy=t1.confirm_time&dir=desc
export const planInfoPage = (params: any) => {
  return request({
    url: '/app/mes/plan/info/page',
    method: 'GET',
    params,
  })
}

// 指令单分解详情
export const planInstructionDetail = (id: string) => {
  return request({
    url: `/app/mes/plan/instruction/detail/${id}`,
    method: 'GET'
  })
}

// 指令单生成
export const planInstructionGenerate = (id: string) => {
  return request({
    url: `/app/mes/plan/instruction/generate/${id}`,
    method: 'POST'
  })
}

// 指令单确认列表
export const planInstructionPage = (params: any) => {
  return request({
    url: `/app/mes/plan/instruction/page`,
    method: 'GET',
    params,
  })
}

// 指令单分解保存
export const planInstructionSave = (data: any) => {
  return request({
    url: `/app/mes/plan/instruction/save`,
    method: 'POST',
    data,
  })
}

// 指令单下发
export const planInstructionSend = (id: string) => {
  return request({
    url: `/app/mes/plan/instruction/send/${id}`,
    method: 'POST'
  })
}

// 指令单分解更新
export const planInstructionUpdate = (data: any) => {
  return request({
    url: `/app/mes/plan/instruction/update`,
    method: 'POST',
    data
  })
}

// 查询工序集合
export const getProcedureList = (params: API.MesProcedureListReq) => {
  return request({
    url: `/app/mes/procedure/list`,
    method: 'GET',
    params,
  })
}

// 查询单个工序详情
export const getProcedureDetailById = (id: String) => {
  return request({
    url: `/app/mes/procedure/detail/${id}`,
    method: 'GET',
  })
}

// 根据角色查询人员
export const queryUserListByRole = (params: { roleId: string }) => {
  return request({
    url: `/app/mes/platform/query/user/listByRole`,
    method: 'GET',
    params,
  })
}

//指令单批量确认
export const reqPlanInstructionTeamBatchConfirm = async (data: any) => {
  return await request({
    url: `/app/mes/plan/instruction/team/batchConfirm`,
    method: 'POST',
    data,
  });
};

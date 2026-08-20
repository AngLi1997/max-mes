import request from '../../service';
// 指令单确认接口

// 指令单确认-班组信息
export const instructionTeamConfirm = (data:any)=>{
  return request({
    url: '/app/mes/plan/instruction/team/confirm',
    method: 'POST',
    data,
  })
}

// 指令单确认-获取详情
export const instructionTeamDetail = (id:string)=>{
  return request({
    url: `/app/mes/plan/instruction/team/detail/${id}`,
    method: 'GET'
  })
}

// 指令单保存-班组信息
export const instructionTeamSave = (data:any)=>{
  return request({
    url: `/app/mes/plan/instruction/team/save`,
    method: 'POST',
    data
  })
}
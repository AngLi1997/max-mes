import request from '../../service';

/**
 * @description 查询检验单列表  /app/lims/check/order/page
 * @param params 
 * @returns 
 */
export const getCheckOrderPage = (params?:any)=>{
  return request({
    url: '/app/lims/check/order/page',
    method: 'GET',
    params,
  });
}

/**
 * @description 发起请验  /app/lims/check/order/save
 * @param data
 * @returns
 */
export const saveCheckOrder = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/save',
    method: 'POST',
    data,
  });
}

/**
 * @description 终止操作  /app/lims/check/order/terminate
 * @param data
 * @returns
 */
export const terminateCheckOrder = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/terminate',
    method: 'POST',
    data,
  });
}

/**
 * @description 请验确认  /app/lims/check/order/confirm
 * @param data
 * @returns
 */
export const confirmCheckOrder = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/confirm',
    method: 'POST',
    data,
  });
}

/**
 * @description 取样  /app/lims/check/order/take
 * @param data
 * @returns
 */
export const takeCheckOrder = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/take',
    method: 'POST',
    data,
  });
}

/**
 * @description 查询所有待数据录入的检验单id(批量录入时调用)  /app/lims/check/order/all
 * @param params
 * @returns
 */
export const getAllCheckOrder = (params?:any)=>{
  return request({
    url: '/app/lims/check/order/all',
    method: 'GET',
    params,
  });
}

/**
 * @description 批量查询分析项信息  /app/lims/check/order/analyze/info
 * @param params
 * @returns
 */
export const getCheckOrderAnalyzeInfo = (params?:any)=>{
  return request({
    url: '/app/lims/check/order/analyze/info',
    method: 'GET',
    params,
  });
}

/**
 * @description 检验录入保存  /app/lims/check/order/save/inspect
 * @param data
 * @returns
 */
export const saveCheckOrderInspect = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/save/inspect',
    method: 'POST',
    data,
  });
}

/**
 * @description 检验录入提交  /app/lims/check/order/submit/inspect
 * @param data
 * @returns
 */
export const submitCheckOrderInspect = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/submit/inspect',
    method: 'POST',
    data,
  });
}

/**
 * @description 计算没有结果的检验单分析项数量  /app/lims/check/order/analyze/valid
 * @param params
 * @returns
 */
export const getCheckOrderAnalyzeValid = (params?:any)=>{
  return request({
    url: '/app/lims/check/order/analyze/valid',
    method: 'GET',
    params,
  });
}

/**
 * @description 根据检验单编号查询报告信息  /app/lims/check/order/report/info
 * @param {orderNo}
 * @returns
 */
export const getCheckOrderReportInfo = (orderNo?:String)=>{
  return request({
    url: `/app/lims/check/order/report/info/${orderNo}`,
    method: 'GET',
  });
}

/**
 * @description 报告生成/重新检测  /app/lims/check/order/report
 * @param data
 * @returns
 */
export const generateCheckOrderReport = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/report',
    method: 'POST',
    data,
  });
}

/**
 * @description 审核报告  /app/lims/check/order/audit/report
 * @param data
 * @returns
 */
export const auditCheckOrderReport = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/audit/report',
    method: 'POST',
    data,
  });
}

/**
 * @description 报告签发  /app/lims/check/order/sign/report
 * @param data
 * @returns
 */
export const signCheckOrderReport = (data?:any)=>{
  return request({
    url: '/app/lims/check/order/sign/report',
    method: 'POST',
    data,
  });
}

/**
 * @description 请验单已完成分页查询  /app/lims/check/order/finish/page
 * @param params
 * @returns
 */
export const getCheckOrderFinishPage = (params?:any)=>{
  return request({
    url: '/app/lims/check/order/finish/page',
    method: 'GET',
    params,
  });
}

/**
 * @description 根据请验单编号查询请验单详情信息  /app/lims/check/order/info
 * @param {orderNo}
 * @returns
 */
export const getCheckOrderInfo = (orderNo?:String)=>{
  return request({
    url: `/app/lims/check/order/info/${orderNo}`,
    method: 'GET',
  });
}

/**
 * @description 根据请验单编号查询请验单的流程信息  /app/lims/check/log/order/process
 * @param {orderNo}
 * @returns
 */
export const getCheckOrderLog = (orderNo?:String)=>{
  return request({
    url: `/app/lims/check/log/order/process/${orderNo}`,
    method: 'GET',
  });
}

/**
 * @description 根据分析项查询请验单的流程信息  /app/lims/check/log/analyze
 * @param {analyzeId}
 * @returns
 */
export const getCheckOrderLogAnalyze = (analyzeId?:String)=>{
  return request({
    url: `/app/lims/check/log/analyze/${analyzeId}`,
    method: 'GET',
  });
}

/**
 * @description 根据请验单编号查询请验单的流程信息  /app/lims/check/log/order
 * @param {orderNo}
 * @returns
 */
export const getCheckOrderLogOrder = (orderNo?:String)=>{
  return request({
    url: `/app/lims/check/log/order/${orderNo}`,
    method: 'GET',
  });
}
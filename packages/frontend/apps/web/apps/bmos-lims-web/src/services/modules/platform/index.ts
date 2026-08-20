import request from '../../service';


/**
 * @description 查询公式确认列表  /api/app/platform/expression/list
 * @param params 
 * @returns 
 */
export const getExpressionList = (params?:any)=>{
  return request({
    url: '/app/platform/expression/list',
    method: 'GET',
    params,
  });
}

/**
 * @description 查询参数配置  /api/app/platform/business/parameter/detailByCode/{code}
 * @param params 
 * @returns 
 */
export const getParameterDetailByCode = (code:string)=>{
  return request({
    url: `/app/platform/business/parameter/detailByCode/${code}`,
    method: 'GET',
  });
}

/**
 * @description: 校验签名v2 /api/app/platform/signature/validate
 * @param {any} data
 */

export const mesSignatureValidateV2 = (data: any) => {
  return request({
    url: `/app/platform/signature/validate/v2`,
    method: 'POST',
    data,
  });
};
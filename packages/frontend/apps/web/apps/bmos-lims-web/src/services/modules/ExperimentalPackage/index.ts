import request from '../../service';

/**
 * @description 查询实验包列表  /app/lims/experiment/package/page
 * @param params 
 * @returns 
 */
export const getExperimentalPackagePage = (params?:any)=>{
  return request({
    url: '/app/lims/experiment/package/page',
    method: 'GET',
    params,
  });
}

/**
 * @description 新增实验包 /app/lims/experiment/package/save
 * @param data
 * @returns
 */
export const saveExperimentalPackage = (data?:any)=>{
  return request({
    url: '/app/lims/experiment/package/save',
    method: 'POST',
    data,
  });
}

/**
 * @description 查询实验包详情 /api/app/lims/experiment/package/info/{id}
 * @param {id}
 * @returns
 */
export const getExperimentalPackageInfo = (id?:String)=>{
  return request({
    url: `/app/lims/experiment/package/info/${id}`,
    method: 'GET',
  });
}

/**
 * @description 编辑实验包 /app/lims/experiment/package/update
 * @param data
 * @returns
 */
export const updateExperimentalPackage = (data?:any)=>{
  return request({
    url: '/app/lims/experiment/package/update',
    method: 'PUT',
    data,
  });
}

/**
 * @description 删除实验包 /app/lims/experiment/package/delete
 * @param {id}
 * @returns
 */
export const deleteExperimentalPackage = (id?:String)=>{
  return request({
    url: `/app/lims/experiment/package/delete/${id}`,
    method: 'DELETE',
  });
}
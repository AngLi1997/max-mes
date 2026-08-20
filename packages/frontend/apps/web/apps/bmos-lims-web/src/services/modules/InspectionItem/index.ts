import request from '../../service';


/**
 * @description 查询检验项目列表  /app/lims/experiment/inspect/page
 * @param params 
 * @returns 
 */
export const getInspectionItemPage = (params?:any)=>{
  return request({
    url: '/app/lims/experiment/inspect/page',
    method: 'GET',
    params,
  });
}


/**
 * @description 新增检验项目 /app/lims/experiment/inspect/save
 * @param data
 * @returns
 */
export const saveInspectionItem = (data?:any)=>{
  return request({
    url: '/app/lims/experiment/inspect/save',
    method: 'POST',
    data,
  });
}

/**
 * @description 查询检验项目详情 /api/app/lims/experiment/inspect/info/{id}
 * @param {id}
 * @returns
 */
export const getInspectionItemInfo = (id?:String)=>{
  return request({
    url: `/app/lims/experiment/inspect/info/${id}`,
    method: 'GET',
  });
}

/**
 * @description 编辑检验项目 /app/lims/experiment/inspect/update
 * @param data
 * @returns
 */
export const updateInspectionItem = (data?:any)=>{
  return request({
    url: '/app/lims/experiment/inspect/update',
    method: 'PUT',
    data,
  });
}

/**
 * @description 删除检验项目 /app/lims/experiment/inspect/delete
 * @param {id}
 * @returns
 */
export const deleteInspectionItem = (id?:String)=>{
  return request({
    url: `/app/lims/experiment/inspect/delete/${id}`,
    method: 'DELETE',
  });
}

/**
 * @description 根据实验包id查询实验包下的检验项目信息 /app/lims/experiment/package/inspect/info/{id}
 * @param {id}
 * @returns
 */
export const getInspectionItemByPackageId = (id?:String)=>{
  return request({
    url: `/app/lims/experiment/package/inspect/info/${id}`,
    method: 'GET',
  });
}
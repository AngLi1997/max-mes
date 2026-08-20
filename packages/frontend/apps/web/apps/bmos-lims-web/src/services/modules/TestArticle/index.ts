import request from '../../service';

/**
 * @description 查询物料列表  /app/lims/basic/category/syncTree
 * @param params 
 * @returns 
 */
export const getSyncTree = (params?:any)=>{
  return request({
    url: '/app/lims/basic/category/syncTree',
    method: 'GET',
    params,
  });
}

/**
 * @description 查询物料分类列表  /app/lims/basic/category/syncTreeAll
 * @param params 
 * @returns 
 */
export const getSyncTreeAll = (params?:any)=>{
  return request({
    url: '/app/lims/basic/category/syncTreeAll',
    method: 'GET',
    params,
  });
}

/**
 * @description 查询当前系统内的分类树 /app/lims/basic/category/tree
 * @param data 
 * @returns 
 */
export const getLimsTree = (data?:any)=>{
  return request({
    url: '/app/lims/basic/category/tree',
    method: 'POST',
    data,
  });
}

/**
 * @description 新增检品分类 /app/lims/basic/category/save
 * @param data 
 * @returns
 */
export const saveCategory = (data?:any)=>{
  return request({
    url: '/app/lims/basic/category/save',
    method: 'POST',
    data,
  });
}

/**
 * @description 删除检品分类 /app/lims/basic/category/delete
 * @param {id} id 数据点id
 * @returns
 */
export const deleteCategory = (id?: string)=>{
  return request({
    url: `/app/lims/basic/category/delete/${id}`,
    method: 'DELETE',
  });
}

/**
 * @description 查询检品分类详情 /app/lims/basic/inspection/info
 * @param {id}
 * @returns
 */
export const getCategoryInfo = (id?:String)=>{
  return request({
    url: `/app/lims/basic/inspection/info/${id}`,
    method: 'GET',
  });
}

/**
 * @description 编辑检品分类 /app/lims/basic/category/update
 * @param data 
 * @returns
 */
export const updateCategory = (data?:any)=>{
  return request({
    url: '/app/lims/basic/category/update',
    method: 'PUT',
    data,
  });
}

/**
 * @description 检品列表分页查询 /app/lims/basic/inspection/page
 * @param params
 * @returns
 */
export const getTestArticleList = (params?:any)=>{
  return request({
    url: '/app/lims/basic/inspection/page',
    method: 'GET',
    params,
  });
}


/**
 * 
 * @description 同步物料  /app/lims/basic/inspection/sync
 * @param data 
 * @returns
 */
export const syncData = (data?:any)=>{
  return request({
    url: '/app/lims/basic/inspection/sync',
    method: 'POST',
    data,
  });
}

/**
 * 
 * @description 编辑检品信息 /app/lims/basic/inspection/update
 * @param data 
 * @returns
 */
export const updateTestArticle = (data?:any)=>{
  return request({
    url: '/app/lims/basic/inspection/update',
    method: 'PUT',
    data,
  });
}

/**
 * 
 * @description 删除检品信息 /app/lims/basic/inspection/delete
 * @param {id}
 * @returns
 */
export const deleteTestArticle = (id?: string)=>{
  return request({
    url: `/app/lims/basic/inspection/delete/${id}`,
    method: 'DELETE',
  });
}

/**
 * @description 检品简单信息全量查询 /app/lims/basic/inspection/list
 * @param 
 * @returns
 */
export const getTestArticleListAll = ()=>{
  return request({
    url: '/app/lims/basic/inspection/list',
    method: 'GET',
  });
}

/**
 * @description 根据检品id查询检品绑定的实验包信息 /app/lims/basic/inspection/package/{id}
 * @param {id}
 * @returns
 */
export const getTestArticlePackage = (id?: string)=>{
  return request({
    url: `/app/lims/basic/inspection/package/${id}`,
    method: 'GET',
  });
}
import request from '../../service';

/**
 * @description 查询分析项列表  /app/lims/experiment/analyze/page
 * @param params 
 * @returns 
 */
export const getAnalyzePage = (params?:any)=>{
  return request({
    url: '/app/lims/experiment/analyze/page',
    method: 'GET',
    params,
  });
}

/**
 * @description 新增分析项 /app/lims/experiment/analyze/save
 * @param data
 * @returns
 */
export const saveAnalyze = (data?:any)=>{
  return request({
    url: '/app/lims/experiment/analyze/save',
    method: 'POST',
    data,
  });
}


/**
 * @description 编辑分析项 /app/lims/experiment/analyze/update
 * @param data
 * @returns
 */
export const updateAnalyze = (data?:any)=>{
  return request({
    url: '/app/lims/experiment/analyze/update',
    method: 'PUT',
    data,
  });
}

/**
 * @description 删除分析项 /app/lims/experiment/analyze/delete/{id}
 * @param data
 * @returns
 */
export const deleteAnalyze = (id?:String)=>{
  return request({
    url: `/app/lims/experiment/analyze/delete/${id}`,
    method: 'DELETE',
  });
}
import request from '../../utils/request';
// 查询标准单位列表
export const standardUnitsList = async (params: any) => {
  return request({
    url: '/api/app/platform/unit/list/unit',
    method: 'get',
    params,
  });
};
// 新增标准单位信息
export const addStandardUnit = (data: any) => {
  return request({
    url: '/api/app/platform/unit/save/unit',
    method: 'post',
    data,
  });
};
// 编辑标准单位信息
export const editStandardUnit = (data: any) => {
  return request({
    url: '/api/app/platform/unit/update/unit',
    method: 'post',
    data,
  });
};

// 标准单位启停(1208新加为做日志)
// export const updateStandardState = (data: any) => {
//   return request({
//     url: '/api/app/platform/unit/update/unit/state',
//     method: 'post',
//     data,
//   });
// };


// 删除标准单位信息
export const deleteStandardUnit = (params: any) => {
  return request({
    url: '/api/app/platform/unit/delete/unit',
    method: 'get',
    params,
  });
};



// 扩展单位
// 查询扩展单位列表
export const extendedUnitsList = async (params: any) => {
  return request({
    url: '/api/app/platform/unit/list/unit/extend',
    method: 'get',
    params,
  });
};
// 新增扩展单位信息
export const addExtendedUnit = (data: any) => {
  return request({
    url: '/api/app/platform/unit/save/unit/extend',
    method: 'post',
    data,
  });
};
// 编辑扩展单位信息
export const editExtendedUnit = (data: any) => {
  return request({
    url: '/api/app/platform/unit/update/unit/extend',
    method: 'post',
    data,
  });
};
// 扩展单位启停
export const updateExtendState = (data: any) => {
  return request({
    url: '/api/app/platform/unit/update/extend/state',
    method: 'post',
    data,
  });
};
// 删除扩展单位信息
export const deleteExtendedUnit = (params: any) => {
  return request({
    url: '/api/app/platform/unit/delete/unit/extend',
    method: 'get',
    params,
  });
};

// 查询修约方式
export const getRoundingList= (params: any) => {
  return request({
    url: '/api/app/platform/unit/list/rounding',
    method: 'get',
    params,
  });
};







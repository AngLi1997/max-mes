import request from '../../service';

// /api/app/mes/record/save/formula
export const recordSaveFormula = (data: any) => {
  return request({
    url: '/app/mes/record/save/formula',
    method: 'POST',
    data,
  });
};

export const recordManageSaveFormula = (data: any) => {
  return request({
    url: '/app/mes/record/manage/save/formula',
    method: 'POST',
    data,
  });
};

// /api/app/mes/record/delete/formula
export const recordDeleteFormula = (params: any) => {
  return request({
    url: '/app/mes/record/delete/formula',
    method: 'GET',
    params,
  });
};

// /list/record/version
export const recordVersionList = (params?: any) => {
  return request({
    url: '/app/mes/record/list/record/version',
    method: 'get',
    params,
  });
};

// /list/rounding
export const recordRoundingList = (params?: any) => {
  return request({
    url: '/app/mes/record/list/rounding',
    method: 'get',
    params,
  });
};

// 格式配置计算预览
export const recordFunctionPreview = (data: any) => {
  return request({
    url: '/app/mes/record/function/preview',
    method: 'POST',
    data,
  });
};

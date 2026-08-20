import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: 创建检验任务 /inspect/task/create
 */
export const postInspectTaskCreate = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/task/create`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 编辑检验任务的检验项目(C1) /inspect/task/edit
 */
export const postInspectTaskEdit = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/task/edit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询检验任务列表(C1,C2) /inspect/task/list
 */
export const postInspectTaskList = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/task/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询单个检验任务(C1,C2) /inspect/task/detail
 */
export const postInspectTaskDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/task/detail`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询标本的所有单项数据(C1-1) /inspect/alldata/detail
 */
export const postInspectAlldataDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/alldata/detail`,
    method: 'POST',
    data,
  });
};

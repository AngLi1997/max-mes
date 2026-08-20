import request from '../../service';

// 获取操作历史列表
export const getProductHistoryOperationPageApi = (id: string) => {
  return request({
    url: `/app/mes/operation/history/plan/history/list/${id}`,
    method: 'get',
  });
};

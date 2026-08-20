import request from '@/utils/request/request.js';

// 保存称量数据 /api/app/mes/weigh/data/saveData
export const saveWeighDataApi = data =>
  request.post('/api/app/mes/weigh/data/saveData', data, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': data.mode === '1' ? '称量数据-手动称量' : '称量数据-秤具称量',
    },
  });
// 查询称量结果 /api/app/mes/weigh/data/getWeighList
export const getWeighDataListApi = data =>
  request.get('/api/app/mes/weigh/data/getWeighList', data);

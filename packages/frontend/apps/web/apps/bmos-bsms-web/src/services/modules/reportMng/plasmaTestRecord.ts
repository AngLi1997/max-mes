import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 血浆检测记录表 ---------------

/**
 * @description: 浆站检测数据分页列表 /plasma/test/record/page
 */
export const getPlasmaTestRecordList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma/test/record/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 打印血浆检测记录表 /plasma/test/record/print/{syncBatchNo}
 */
export const printPlasmaTestRecord = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma/test/record/print/${data.syncBatchNo}`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

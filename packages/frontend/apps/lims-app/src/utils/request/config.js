import { IP_CONFIG } from '@/utils/uniStorage/const.js';
import { getStorageSync, setStorageSync } from '@/utils/uniStorage/uniStorage.js';

let baseUrl;

// 开发环境中
if (process.env.NODE_ENV == 'development') {
  baseUrl = '';
}
else {
  // 生产环境中
  baseUrl = '';
}

baseUrl = getStorageSync(IP_CONFIG) || '';
export default {
  baseUrl,
};

export const LOCK_SCREEN_TIME_CODE = 'platform.sys.app-lock-screen-time';

// 不影响锁屏接口URL
export const LOCK_SCREEN_URL = [
  '/api/app/mes/flow/todoPage/fresh',
];

/**
 *  Bmos-Operation
 *  INSERT(0, "新增"),
    UPDATE(1, "编辑"),
    DELETE(2, "删除"),
    EXPORT(3, "导出"),
    RELATE(4, "关联"),
    PROCESS(5, "审核"),
 */

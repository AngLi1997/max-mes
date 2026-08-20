/*
 * @Author: weisheng
 * @Date: 2021-12-21 14:22:03
 * @LastEditTime: 2024-03-15 16:55:30
 * @LastEditors: weisheng
 * @Description:
 * @FilePath: \wot-design-uni\src\uni_modules\wot-design-uni\index.ts
 * 记得注释
 */

export * as clickOut from './components/common/clickoutside';
export { dayjs } from './components/common/dayjs';
export * as CommonUtil from './components/common/util';

export { useQueue } from './components/composables/useQueue';

export type { ConfigProviderThemeVars } from './components/wd-config-provider/types';
export { useMessage } from './components/wd-message-box';

export { useToast } from './components/wd-toast';
export * from './locale';

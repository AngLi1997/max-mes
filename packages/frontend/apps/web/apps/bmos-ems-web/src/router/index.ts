import { LogoBackgroundMode } from '@bmos/components';
import { sendMessage } from '@bmos/utils';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import { constantRoutes } from './constantRoutes';
import EquipmentManagement from './modules/EquipmentManagement';
import Logs from './modules/Logs';
import OutsideRouter from './modules/Outside';
import RegionalManagement from './modules/RegionalManagement';

export const asyncRoutes: RouteRecordRaw[] = [OutsideRouter, RegionalManagement, EquipmentManagement, Logs];

const router = createRouter({
  // @ts-ignore
  history: createWebHistory(import.meta.env.BASE_URL), // 设置基地址为根路径
  routes: [...constantRoutes], // 合并固定路由和异步路由
});

let routerChangeFlag = false;
router.afterEach(to => {
  routerChangeFlag && sendMessage('routerChange', { fullPath: to.fullPath.slice(1) });
  routerChangeFlag = true;

  if (to.meta && to.meta.hiddenMenu) {
    sendMessage('logoColor', { color: LogoBackgroundMode.WHITE });
  } else {
    sendMessage('logoColor', { color: LogoBackgroundMode.BLUE });
  }
});

export default router;

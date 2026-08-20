import { LogoBackgroundMode } from '@bmos/components';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import { constantRoutes } from './constantRoutes';
import BatchRecords from './modules/BatchRecords';
import BatchRelease from './modules/BatchRelease';
import DataSet from './modules/DataSet';
import ExceptionManagement from './modules/ExceptionManagement';
import ImplementRouter from './modules/Implement';
import InspectionManage from './modules/InspectionManage';
import OutsideRouter from './modules/Outside';
import ProductConfig from './modules/ProductConfig';
import ProductionManagement from './modules/ProductionManagement';
import ProductionMaterials from './modules/ProductionMaterials';
import ProductRefer from './modules/ProductRefer';

export const asyncRoutes: RouteRecordRaw[] = [
  ProductConfig,
  ProductionMaterials,
  ProductionManagement,
  BatchRelease,
  ProductRefer,
  OutsideRouter,
  ImplementRouter,
  BatchRecords,
  DataSet,
  ExceptionManagement,
  InspectionManage,
];

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

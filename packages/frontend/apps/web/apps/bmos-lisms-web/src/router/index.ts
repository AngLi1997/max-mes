import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import { constantRoutes } from './constantRoutes';
import ConfigManagement from './modules/ConfigManagement';
import DataInstrument from './modules/DataInstrument';
import InspectionManagement from './modules/InspectionManagement';
import LaboratoryResource from './modules/LaboratoryResource';
import LogManagement from './modules/LogManagement';
import MaterialWarehouse from './modules/MaterialWarehouse';
import Outside from './modules/Outside';
import ReportingManagement from './modules/ReportingManagement';
import SpecimenManagement from './modules/SpecimenManagement';

export const asyncRoutes: RouteRecordRaw[] = [
  Outside,
  SpecimenManagement,
  DataInstrument,
  InspectionManagement,
  ConfigManagement,
  LaboratoryResource,
  LogManagement,
  MaterialWarehouse,
  ReportingManagement,
];

const router = createRouter({
  // @ts-ignore
  history: createWebHistory(import.meta.env.BASE_URL), // 设置基地址为根路径
  routes: [...constantRoutes], // 合并固定路由和异步路由
});

let routerChangeFlag = false;

router.afterEach(to => {
  routerChangeFlag &&
    window.parent?.postMessage({ type: 'routerChange', data: { fullPath: to.fullPath.slice(1) } }, '*');
  routerChangeFlag = true;
});

export default router;

import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import { constantRoutes } from './constantRoutes';
import ConfigurationManagement from './modules/ConfigurationManagement';
import MaterialManagement from './modules/MaterialManagement';
import Outside from './modules/Outside';
import ReportManagement from './modules/ReportManagement';
import SingleDataManagement from './modules/SingleDataManagement';
import SpecimenManagement from './modules/SpecimenManagement';
import TotalDataManagement from './modules/TotalDataManagement';

export const asyncRoutes: RouteRecordRaw[] = [
  Outside,
  SpecimenManagement,
  SingleDataManagement,
  TotalDataManagement,
  ReportManagement,
  MaterialManagement,
  ConfigurationManagement,
];

const router = createRouter({
  // @ts-ignore
  history: createWebHistory(import.meta.env.BASE_URL), // 设置基地址为根路径
  routes: [...constantRoutes], // 合并固定路由和异步路由
});

let routerChangeFlag = false;

router.beforeEach((to, from, next) => {
  const fromId = from.meta?.id;

  if (fromId && !to.query.fromRouteId) {
    const newRoute = {
      ...to,
      query: {
        ...to.query,
        fromRouteId: String(fromId),
      },
    };
    next(newRoute);
  } else {
    next();
  }
});

router.afterEach(to => {
  routerChangeFlag &&
    window.parent?.postMessage({ type: 'routerChange', data: { fullPath: to.fullPath.slice(1) } }, '*');
  routerChangeFlag = true;
});

export default router;

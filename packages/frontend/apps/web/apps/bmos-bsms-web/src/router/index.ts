import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import { constantRoutes } from './constantRoutes';
import DataInstrument from './modules/DataInstrument';
import InspectionManagement from './modules/InspectionManagement';
import OutboundMng from './modules/OutboundMng';
import Outside from './modules/Outside';
import PlasmaManagement from './modules/PlasmaManagement';
import QualityAssuranceManagement from './modules/QualityAssuranceManagement';
import QuarantineManagement from './modules/QuarantineManagement';
import ReportMng from './modules/ReportMng';
import SortingManagement from './modules/SortingManagement';
import SpecimenManagement from './modules/SpecimenManagement';
import SystemMng from './modules/SystemMng';
import UnqualifiedPlasmaMng from './modules/UnqualifiedPlasmaMng';
import WarehouseMmg from './modules/WarehouseMmg';

export const asyncRoutes: RouteRecordRaw[] = [
  Outside,
  DataInstrument,
  InspectionManagement,
  SpecimenManagement,
  PlasmaManagement,
  QuarantineManagement,
  QualityAssuranceManagement,
  UnqualifiedPlasmaMng,
  SortingManagement,
  WarehouseMmg,
  OutboundMng,
  SystemMng,
  ReportMng,
];

const router = createRouter({
  // @ts-ignore
  history: createWebHistory(import.meta.env.BASE_URL), // 设置基地址为根路径
  routes: [...constantRoutes], // 合并固定路由和异步路由
});

let routerChangeFlag = false;

// router.beforeEach((to, from, next) => {
//   // console.log('routerChangeFlag', to, from, next);
//   // 实验室结果管理 ==》 跳转到检验系统
//   if (to.path === '/170130') {
//     // todo

//     // window.open('http://172.30.1.160/app/bmos-platform/bmos/index.html', '_blank');
//     localStorage.setItem('currentAppKey', '/app/bmos-wms/')
//     // 刷新页面
//     window.location.reload();

//     return;
//   }
//   next();
// });

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

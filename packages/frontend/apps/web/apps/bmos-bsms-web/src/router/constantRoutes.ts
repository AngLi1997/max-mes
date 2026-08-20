export const constantRoutes = [
  {
    path: '/',
    name: 'Index',
    meta: {
      title: '首页',
      code: '首页',
    },
    redirect: '/home',
    component: () => import('@/pages/Main/index.vue'),
    children: [
      { path: '/home', component: () => import('@/pages/Home/index.vue') },
      // {
      //   path: '/specimenManagement/sample-in-stored-mng/detail',
      //   meta: {
      //     title: '待入库标本详情',
      //     // id: '170020001',
      //   },
      //   name: 'sample-in-stored-mng-detail',
      //   component: () => import('@/pages/SpecimenManagement/SampleInStoredMng/components/detail/index.vue'),
      // },
      {
        path: '/plasmaManagement/plasma-donor/detail/:id',
        meta: {
          title: '献浆者详情',
          hidden: true,
        },
        name: 'plasma-donor-detail',
        component: () => import('@/pages/PlasmaManagement/BloodDonorManagement/components/Detail/index.vue'),
      },
      {
        path: '/fileImport',
        meta: {
          title: '文件导入',
          hidden: true,
        },
        name: 'fileImport',
        component: () => import('@/components/ImportExcel/index.vue'),
      },
      {
        path: '/specimenManagement/sample-query/detail/:orgSampleNo',
        meta: {
          title: '已入库标本详情',
          hidden: true,
        },
        name: 'sample-query-detail',
        component: () => import('@/pages/SpecimenManagement/SampleQuery/components/ViewCom/index.vue'),
      },
      {
        path: '/plasmaManagement/plasma-inventory-inquiry/detail/:plasmaOrgNo',
        meta: {
          title: '血浆库存详情',
          hidden: true,
        },
        name: 'plasma-inventory-inquiry-detail',
        component: () => import('@/pages/PlasmaManagement/PlasmaInventoryInquiry/components/ViewCom/index.vue'),
      },
      {
        path: '/quarantineManagement/quarantine-check-detail/:id',
        meta: {
          title: '核查详情',
          hidden: true,
        },
        name: 'quarantine-check-detail',
        component: () => import('@/components/QuarantineDetail/index.vue'),
      },
      {
        path: '/qualityAssuranceManagement/view-com/detail/:id',
        meta: {
          title: '放行单详情',
          hidden: true,
        },
        name: 'view-com-detail',
        component: () => import('@/pages/QualityAssuranceManagement/ViewCom/index.vue'),
      },
      {
        path: '/specimenManagement/sample-data-sync/import-excel',
        meta: {
          title: '标本批量同步模板',
          id: '170020001',
          hidden: true,
        },
        name: 'SampleDataSyncImportExcel',
        component: () => import('@/pages/SpecimenManagement/SampleDataSync/components/ImportExcel/index.vue'),
      },
      {
        path: '/specimenManagement/sample-in-stored-mng/view-com/:syncBatchNo',
        meta: {
          title: '待入库标本详情',
          id: '170020002',
          hidden: true,
        },
        name: 'SampleInStoredMngViewCom',
        component: () => import('@/pages/SpecimenManagement/SampleInStoredMng/components/ViewCom/index.vue'),
      },
      {
        path: '/specimenManagement/appearance-unqualified-audit/view-com/:orgSampleNo',
        meta: {
          title: '外观不合格审核详情',
          id: '170020003',
          hidden: true,
        },
        name: 'AppearanceUnqualifiedAuditViewCom',
        component: () => import('@/pages/SpecimenManagement/AppearanceUnqualifiedAudit/components/ViewCom/index.vue'),
      },
      {
        path: '/specimenManagement/acceptance-audit/view-com/:syncBatchNo',
        meta: {
          title: '验收审核详情',
          id: '170020007',
          hidden: true,
        },
        name: 'AcceptanceAuditViewCom',
        component: () => import('@/pages/SpecimenManagement/AcceptanceAudit/components/ViewCom/index.vue'),
      },
      {
        path: '/specimenManagement/specimen-delivery-plan/edit-com/:outPlanBatchNo',
        meta: {
          title: '编辑标本出库计划',
          hidden: true,
        },
        name: 'SpecimenDeliveryPlanEditCom',
        component: () => import('@/pages/SpecimenManagement/SpecimenDeliveryPlan/components/EditCom/index.vue'),
      },
      {
        path: '/specimenManagement/specimen-delivery-plan/view-com/:outPlanBatchNo',
        meta: {
          title: '标本出库计划详情',
          hidden: true,
        },
        name: 'SpecimenDeliveryPlanViewCom',
        component: () => import('@/pages/SpecimenManagement/SpecimenDeliveryPlan/components/ViewCom/index.vue'),
      },
      {
        path: '/inspectionManagement/inspection-request/view-com/:inspectionBatchNo',
        meta: {
          title: '标本请验详情',
          hidden: true,
        },
        name: 'InspectionRequestViewCom',
        component: () => import('@/pages/InspectionManagement/InspectionRequest/components/ViewCom/index.vue'),
      },
      {
        path: '/inspectionManagement/inspection-report/view-com/:inspectionBatchNo',
        meta: {
          title: '检验报告详情',
          hidden: true,
        },
        name: 'InspectionReportViewCom',
        component: () => import('@/pages/InspectionManagement/InspectionReport/components/ViewCom/index.vue'),
      },
      {
        path: '/plasmaManagement/plasma-in-stored-mng/view-com/:syncBatchNo',
        meta: {
          title: '待入库血浆管理详情',
          hidden: true,
        },
        name: 'PlasmaInStoredMngViewCom',
        component: () => import('@/pages/PlasmaManagement/PlasmaInStoredMng/components/ViewCom/index.vue'),
      },
      {
        path: '/plasmaManagement/visual-inspection/view-com/:plasmaOrgNo',
        meta: {
          title: '血浆外观检验详情',
          hidden: true,
        },
        name: 'VisualInspectionViewCom',
        component: () => import('@/pages/PlasmaManagement/VisualInspection/components/ViewCom/index.vue'),
      },
      {
        path: '/plasmaManagement/appearance-unqualified-audit/view-com/:plasmaOrgNo',
        meta: {
          title: '外观不合格审核详情',
          hidden: true,
        },
        name: 'PlasmaAppearanceUnqualifiedAuditViewCom',
        component: () => import('@/pages/PlasmaManagement/AppearanceUnqualifiedAudit/components/ViewCom/index.vue'),
      },
      {
        path: '/qualityAssuranceManagement/specimen-examination-review/view-com/:inspectionBatchNo',
        meta: {
          title: '标本检验审核详情',
          hidden: true,
        },
        name: 'SpecimenExaminationReviewViewCom',
        component: () =>
          import('@/pages/QualityAssuranceManagement/SpecimenExaminationReview/components/ViewCom/index.vue'),
      },
      {
        path: '/qualityAssuranceManagement/feeding-audit/view-com',
        meta: {
          title: '投料质保审核出库计划详情',
          hidden: true,
        },
        name: 'FeedingAuditViewCom',
        component: () => import('@/pages/QualityAssuranceManagement/FeedingAudit/components/ViewCom/index.vue'),
      },
      {
        path: '/qualityAssuranceManagement/quality-assurance-audit/view-com',
        meta: {
          title: '科研调用质保审核详情',
          hidden: true,
        },
        name: 'QualityAssuranceAuditViewCom',
        component: () =>
          import('@/pages/QualityAssuranceManagement/QualityAssuranceAudit/components/ViewCom/index.vue'),
      },
      {
        path: '/qualityAssuranceManagement/destruction-trial/view-com',
        meta: {
          title: '销毁出库初审详情',
          hidden: true,
        },
        name: 'DestructionTrialViewCom',
        component: () => import('@/pages/QualityAssuranceManagement/DestructionTrial/components/ViewCom/index.vue'),
      },
      {
        path: '/qualityAssuranceManagement/destruction-review/view-com',
        meta: {
          title: '销毁出库复审详情',
          hidden: true,
        },
        name: 'DestructionReviewViewCom',
        component: () => import('@/pages/QualityAssuranceManagement/DestructionReview/components/ViewCom/index.vue'),
      },
      {
        path: '/unqualifiedPlasmaMng/check-record-review/view-com/:id',
        meta: {
          title: '不合格核查记录审核详情',
          hidden: true,
        },
        name: 'CheckRecordReviewViewCom',
        component: () => import('@/pages/UnqualifiedPlasmaMng/CheckRecordReview/components/ViewDetail/index.vue'),
      },
      {
        path: '/unqualifiedPlasmaMng/report-detail/view-com',
        meta: {
          title: '不合格血浆核查报告',
          hidden: true,
        },
        name: 'ReportDetailViewCom',
        component: () => import('@/pages/UnqualifiedPlasmaMng/ReportDetail/index.vue'),
      },
      {
        path: '/sortingManagement/sorting-plan/edit-com',
        meta: {
          title: '分拣计划编辑',
          hidden: true,
        },
        name: 'SortingPlanEditCom',
        component: () => import('@/pages/SortingManagement/SortingPlan/components/EditCom/index.vue'),
      },
      {
        path: '/sortingManagement/sorting-plan/view-com',
        meta: {
          title: '分拣计划详情',
          hidden: true,
        },
        name: 'SortingPlanViewCom',
        component: () => import('@/pages/SortingManagement/SortingPlan/components/ViewCom/index.vue'),
      },
      {
        path: '/sortingManagement/sorting-task/view-com',
        meta: {
          title: '分拣任务详情',
          hidden: true,
        },
        name: 'SortingTaskViewCom',
        component: () => import('@/pages/SortingManagement/SortingTask/components/ViewCom/index.vue'),
      },
      {
        path: '/outboundMng/premelting-check/view-com/:batchNo',
        meta: {
          title: '预融核对详情',
          hidden: true,
        },
        name: 'PremeltingCheckViewCom',
        component: () => import('@/pages/OutboundMng/PremeltingCheck/components/ViewCom/index.vue'),
      },
      {
        path: '/outboundMng/delivery-plan/edit-com/:id',
        meta: {
          title: '出库计划编辑',
          hidden: true,
          id: '170100001',
        },
        name: 'DeliveryPlanEditCom',
        component: () => import('@/pages/OutboundMng/DeliveryPlan/components/EditCom/index.vue'),
      },
      {
        path: '/outboundMng/delivery-plan/import-excel/:id',
        meta: {
          title: '出库计划导入',
          hidden: true,
        },
        name: 'DeliveryPlanImportExcel',
        component: () => import('@/pages/OutboundMng/DeliveryPlan/components/ImportExcel/index.vue'),
      },
      {
        path: '/outboundMng/delivery-plan/view-com',
        meta: {
          title: '出库计划详情',
          hidden: true,
        },
        name: 'DeliveryPlanViewCom',
        component: () => import('@/pages/OutboundMng/DeliveryPlan/components/ViewCom/index.vue'),
      },
      {
        path: '/outboundMng/feeding-discharging-plan/view-com',
        meta: {
          title: '投料出库审核详情',
          hidden: true,
        },
        name: 'FeedingDischargingPlanViewCom',
        component: () => import('@/pages/OutboundMng/FeedingDischargingPlan/components/ViewCom/index.vue'),
      },
      {
        path: '/outboundMng/call-out-library-plan/view-com',
        meta: {
          title: '科研调用出库计划详情',
          hidden: true,
        },
        name: 'CallOutLibraryPlanViewCom',
        component: () => import('@/pages/OutboundMng/CallOutLibraryPlan/components/ViewCom/index.vue'),
      },
      {
        path: '/outboundMng/destroy-warehouse-audit/view-com',
        meta: {
          title: '销毁出库审核详情',
          hidden: true,
        },
        name: 'DestroyWarehouseAuditViewCom',
        component: () => import('@/pages/OutboundMng/DestroyWarehouseAudit/components/ViewCom/index.vue'),
      },
      {
        path: '/outboundMng/authorizer-approved/view-com',
        meta: {
          title: '质量授权人批准详情',
          hidden: true,
        },
        name: 'AuthorizerApprovedViewCom',
        component: () => import('@/pages/OutboundMng/AuthorizerApproved/components/ViewCom/index.vue'),
      },
    ],
  },
];

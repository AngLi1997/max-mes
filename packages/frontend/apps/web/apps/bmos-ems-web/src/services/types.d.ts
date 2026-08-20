declare namespace API {
  interface Data<T> {
    code: number;
    data: T;
    message: string;
  }

  /** /api/app/ems/record/address/conversion */
  interface RecordAddressConversionReq {
    /** filePath */
    filePath: string;
  }

  /** 数据内容 */
  type RecordAddressConversionRes = string;

  /** /api/app/ems/record/copy/version */
  interface RecordCopyVersionReq {
    /** 文件地址 */
    filePath?: string;
    /** 新的版本id */
    id?: number;
    /** 记录id */
    recordId?: number;
    /** 备注 */
    remark?: string;
    /** 版本号 */
    version?: string;
    /** 复制版本id */
    versionOldId?: number;
  }

  /** 数据内容 */
  type RecordCopyVersionRes = number;

  /** /api/app/ems/record/delete/category */
  interface RecordDeleteCategoryReq {
    /** id */
    id: string;
  }

  /** 数据内容 */
  type RecordDeleteCategoryRes = boolean;
  /** /api/app/ems/record/fileUpload */
  type MesRecordFileUploadReq = string;
  /** 数据内容 */
  type MesRecordFileUploadRes = string;

  /** /api/app/ems/record/list/category */
  interface RecordListCategoryReq {
    /** categoryName */
    categoryName?: string;
  }

  /** 数据内容 */
  interface RecordListCategoryRes {
    /** 创建时间 */
    createTime?: string;
    /** 主键id */
    id?: number;
    /** 子集 */
    itemList?: Array<RecordListCategoryResItemListVo>;
    /** 分类名称 */
    name?: string;
    /** 上级id */
    parentId?: number;
  }

  /** /api/app/ems/record/list/record */
  interface RecordListRecordReq {
    /** 分类id */
    categoryId?: number;
    /** 分类集合 */
    categoryList?: array;
    /** 排序 */
    dir?: string;
    /** 批记录名称 */
    name?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 批记录id */
    recordId?: number;
  }

  /** 数据内容 */
  type RecordListRecordRes = string;
  /** /api/app/ems/record/record/item/upload */
  type RecordItemUploadReq = string;
  /** 数据内容 */
  type RecordItemUploadRes = string;

  /** /api/app/ems/record/redact/record */
  interface RecordRedactRecordReq {
    /** versionId */
    versionId: number;
  }

  /** 数据内容 */
  type RecordRedactRecordRes = string;

  /** /api/app/ems/record/save/category */
  interface RecordSaveCategoryReq {
    /** 标识 */
    code?: string;
    /** id */
    id?: number;
    /** 分类名称 */
    name?: string;
    /** 上级id */
    parentId?: number;
    /** 排序号 */
    sort?: number;
  }

  /** 数据内容 */
  type RecordSaveCategoryRes = boolean;
  /** /api/app/ems/record/save/component */
  type RecordSaveComponentReq = Array<RecordSaveComponentReqVo>;
  /** 数据内容 */
  type RecordSaveComponentRes = boolean;

  /** /api/app/ems/record/save/product */
  interface RecordSaveProductReq {
    /** 产品id */
    productIdList?: Array<number>;
    /** 记录id */
    recordId?: number;
  }

  /** 数据内容 */
  type RecordSaveProductRes = boolean;

  /** /api/app/ems/record/save/record */
  interface RecordSaveRecordReq {
    /** 分类id */
    categoryId?: number;
    /** 部门id集合 */
    deptIds?: Array<number>;
    /** 存放指令集地址 */
    filePath?: string;
    /** 记录项集合 */
    items?: Array<RecordSaveRecordReqItemsVo>;
    /** 记录名称 */
    name?: string;
    /** 记录管理表id */
    recordId?: number;
    /** 备注 */
    remark?: string;
    /** 版本号 */
    version?: string;
    /** 记录版本id */
    versionId?: number;
  }

  /** 数据内容 */
  type RecordSaveRecordRes = number;

  /** /api/app/ems/record/save/record/item */
  interface SaveRecordItemReq {
    /** html文件内容 */
    fileContent?: string;
    /** 记录项id */
    id?: number;
    /** 单个记录项存放指令集地址 */
    itemPath?: string;
    /** 记录项最大下标 */
    maxNumber?: number;
    /** 记录项名称 */
    name?: string;
    /** 批记录版本id */
    recordVersionId?: number;
  }

  /** 数据内容 */
  type SaveRecordItemRes = string;

  /** /api/app/ems/record/update/category */
  interface RecordUpdateCategoryReq {
    /** 主键id */
    id?: string;
    /** 分类名称 */
    name?: string;
  }

  /** 数据内容 */
  type RecordUpdateCategoryRes = boolean;

  /** /api/app/ems/record/update/version */
  interface RecordUpdateVersionReq {
    /** 版本id */
    id?: number;
    /** 记录管理表id */
    recordId?: number;
    /** 备注 */
    remark?: string;
    /** */
    state: string;
    /** 版本号 */
    version?: string;
  }

  /** 数据内容 */
  type RecordUpdateVersionRes = boolean;

  /** /api/app/ems/flow/model */
  interface MesFlowModelReq {
    /** processModelId */
    processModelId?: string;
  }

  /** 数据内容 */
  type MesFlowModelRes = string;

  /** /api/app/ems/procedure/detail/modify */
  interface ProcedureDetailModifyReq {
    /** 工序id */
    procedureId?: number;
    /** 工序步骤集合 */
    procedureSteps?: Array<ProcedureDetailModifyReqProcedureStepsVo>;
    /** 流程模型 */
    processModel?: string;
  }

  /** */
  type ProcedureDetailModifyRes = string;
  /** /api/app/ems/procedure/detail/save */
  type ProcedureDetailSaveReq = ProcedureDetailModifyReq;
  /** */
  type ProcedureDetailSaveRes = string;

  /** /api/app/ems/procedure/step/config/list */
  interface StepConfigListReq {
    /** 工序步骤id */
    procedureStepId: number;
    /** 工艺id */
    processId: number;
    /** 工艺版本 */
    processVersion?: string;
    /** 是否可复用 */
    reusable: string;
  }

  /** 数据内容 */
  interface StepConfigListRes {
    /** 组件id */
    componentId?: number;
    /** 配置信息JSON */
    configInfo?: string;
  }

  /** /api/app/ems/procedure/step/config/save */
  interface StepConfigSaveReq {
    /** 组件信息 */
    components?: Array<StepConfigSaveReqComponentsVo>;
    /** 流程节点Id */
    nodeId?: string;
    /** 工序步骤id */
    procedureStepId?: number;
    /** 工序id */
    processId?: number;
    /** 记录项id */
    recordItemId?: number;
    /** 是否复用 */
    reusable?: boolean;
    /** 工艺版本号 */
    version?: string;
  }

  /** */
  type StepConfigSaveRes = string;

  /** /api/app/ems/procedure/step/list */
  interface ProcedureStepListReq {
    /** procedureId */
    procedureId?: number;
  }

  /** 数据内容 */
  interface ProcedureStepListRes {
    /** 区域 */
    areaList?: Array<number>;
    /** 设备类 */
    deviceTypeList?: Array<number>;
    /** 时长 */
    duration?: number;
    /** id */
    id?: number;
    /** 名称 */
    name: string;
    /** 工序功能 */
    nodeFunction?: string;
    /** 流程节点id */
    nodeId: string;
    /** 历史工序步骤id */
    procedureStepId?: number;
    /** 记录项id */
    recordItemId?: number;
    /** 批记录版本id */
    recordVersionId?: number;
    /** 是否可复用 */
    reusable?: boolean;
    /** 执行岗 */
    roles?: Array<number>;
    /** 单位 */
    timeUnit?: string;
  }

  /** /api/app/ems/process/detail */
  interface MesProcessDetailReq {
    /** 工艺id */
    processId: number;
    /** 工艺版本 */
    version: string;
  }

  /** 数据内容 */
  type MesProcessDetailRes = string;

  /** /api/app/ems/process/modify */
  interface MesProcessModifyReq {
    /** 关联批记录 */
    batchRecordItems: Array<MesProcessModifyReqBatchRecordItemsVo>;
    /** 描述 */
    description?: string;
    /** 工艺版本id */
    id: number;
    /** 工序信息 */
    procedures: Array<MesProcessModifyReqProceduresVo>;
    /** 工艺id */
    processId: number;
    /** 流程模型 */
    processModel?: string;
    /** 关联工艺 */
    processRelations?: Array<MesProcessModifyReqProcessRelationsVo>;
    /** 产品分类id */
    productCategoryId: number;
    /** 产品id */
    productId: number;
    /** 配方id */
    recipeId: number;
    /** 版本号 */
    version: string;
  }

  /** 数据内容 */
  type MesProcessModifyRes = string;

  /** /api/app/ems/process/page */
  interface MesProcessPageReq {
    /** 排序 */
    dir?: string;
    /** 工艺名称 */
    name?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 产品分类id */
    productCategoryId?: number;
    /** 产品ID */
    productId?: number;
  }

  /** 数据内容 */
  type MesProcessPageRes = string;

  /** /api/app/ems/process/save */
  interface MesProcessSaveReq {
    /** 关联批记录 */
    batchRecordItems: Array<MesProcessSaveReqBatchRecordItemsVo>;
    /** 数据权限部门id */
    deptIds: Array<number>;
    /** 描述 */
    description?: string;
    /** 工艺名称 */
    name: string;
    /** 工序集合 */
    procedures: Array<MesProcessSaveReqProceduresVo>;
    /** 流程模型 */
    processModel?: string;
    /** 关联工艺 */
    processRelations?: Array<MesProcessSaveReqProcessRelationsVo>;
    /** 产品分类id */
    productCategoryId: number;
    /** 产品id */
    productId: number;
    /** 配方id */
    recipeId: number;
  }

  /** 数据内容 */
  type MesProcessSaveRes = string;

  /** /api/app/ems/process/version/changeState */
  interface ProcessVersionChangeStateReq {
    /** id */
    id?: number;
    /**
     * 启停状态
     * [true:启用,
     * false:停用]
     */
    state: boolean;
  }

  /** */
  type ProcessVersionChangeStateRes = string;

  /** /api/app/ems/process/version/page */
  interface ProcessVersionPageReq {
    /** 排序 */
    dir?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 工艺id */
    processId: number;
  }

  /** 数据内容 */
  type ProcessVersionPageRes = string;

  /** /api/app/ems/process/version/save */
  interface ProcessVersionSaveReq {
    /** 关联批记录 */
    batchRecordItems: Array<ProcessVersionSaveReqBatchRecordItemsVo>;
    /** 描述 */
    description?: string;
    /** 工艺版本id */
    id: number;
    /** 源版本号 */
    originVersion: string;
    /** 工序信息 */
    procedures: Array<ProcessVersionSaveReqProceduresVo>;
    /** 工艺id */
    processId: number;
    /** 流程模型 */
    processModel?: string;
    /** 关联工艺 */
    processRelations?: Array<ProcessVersionSaveReqProcessRelationsVo>;
    /** 配方id */
    recipeId: number;
    /** 版本号 */
    version: string;
  }

  /** 数据内容 */
  type ProcessVersionSaveRes = string;

  /** /api/app/ems/resource/permission/list/dept */
  interface PermissionListDeptReq {
    /** resourceId */
    resourceId: number;
  }

  /** 数据内容 */
  type PermissionListDeptRes = array;

  /** /api/app/ems/resource/permission/save */
  interface ResourcePermissionSaveReq {
    /** 部门id集合 */
    deptIds?: Array<number>;
    /** 资源id */
    resourceId?: number;
  }

  /** */
  type ResourcePermissionSaveRes = string;

  /** /api/app/ems/record/copy/record/item */
  interface CopyRecordItemReq {
    /** itemId */
    itemId: number;
    /** itemName */
    itemName: string;
  }

  /** 数据内容 */
  type CopyRecordItemRes = string;

  /** /api/app/ems/record/delete/record/item */
  interface DeleteRecordItemReq {
    /** itemId */
    itemId: number;
  }

  /** 数据内容 */
  type DeleteRecordItemRes = string;
  /** 数据内容 */
  type RecordProductionIdRes = PermissionListDeptRes;

  /** /api/app/ems/process/version/copy */
  interface ProcessVersionCopyReq {
    /** 关联批记录 */
    batchRecordItems: Array<ProcessVersionCopyReqBatchRecordItemsVo>;
    /** 数据权限部门id */
    deptIds?: Array<number>;
    /** 描述 */
    description?: string;
    /** 工艺版本id */
    id: number;
    /** 工艺名称 */
    name: string;
    /** 源版本号 */
    originVersion: string;
    /** 工序信息 */
    procedures: Array<ProcessVersionCopyReqProceduresVo>;
    /** 工艺id */
    processId: number;
    /** 流程模型 */
    processModel?: string;
    /** 关联工艺 */
    processRelations?: Array<ProcessVersionCopyReqProcessRelationsVo>;
    /** 产品分类id */
    productCategoryId: number;
    /** 产品id */
    productId: number;
    /** 配方id */
    recipeId: number;
    /** 版本号 */
    version: string;
  }

  /** 数据内容 */
  type ProcessVersionCopyRes = string;

  /** /api/app/ems/record/list/version */
  interface RecordListVersionReq {
    /** recordId */
    recordId: number;
  }

  /** 数据内容 */
  interface RecordListVersionRes {
    /** 版本号 */
    version?: string;
    /** 版本id */
    versionId?: string;
  }

  /** /api/app/ems/record/list/component */
  interface RecordListComponentReq {
    /** itemId */
    itemId: number;
    /** recordVersionId */
    recordVersionId: number;
  }

  /** 数据内容 */
  type RecordListComponentRes = string;

  /** /api/app/ems/record/list/product/record */
  interface ListProductRecordReq {
    /** productId */
    productId: number;
    /** recordId */
    recordId?: number;
  }

  /** 数据内容 */
  type ListProductRecordRes = RecordListVersionRes;

  /** /api/app/ems/record/list/record/item */
  interface ListRecordItemReq {
    /** versionId */
    versionId: number;
  }

  /** 数据内容 */
  interface ListRecordItemRes {
    /** 组件集合 */
    componentList?: Array<ListRecordItemResComponentListVo>;
    /** html文件地址 */
    fileContent?: string;
    /** 记录id */
    id?: number;
    /** 记录项业务id */
    itemId?: number;
    /** 记录项类型 */
    itemType?: string;
    /** 记录项最大下标 */
    maxNumber?: number;
    /** 记录项名称 */
    name?: string;
    /** 记录名称 */
    recordName?: string;
    /** 记录版本id */
    recordVersionId?: number;
    /** 记录项排序字段 */
    sort?: number;
  }

  /** /api/app/ems/record/list/record/item */
  type ListRecordItemReq = Array<array>;

  /** 数据内容 */
  interface ListRecordItemRes {
    /** 记录项集合 */
    recordItemList?: Array<ListRecordItemRes>;
    /** 记录名称 */
    recordName?: string;
    /** 版本id */
    versionId?: number;
  }

  /** /api/app/ems/record/save/formula */
  interface RecordSaveFormulaReq {
    /** 组件具体空格id */
    filedId?: number;
    /** 公式参数详情 */
    formulaDetailList?: Array<RecordSaveFormulaReqFormulaDetailListVo>;
    /** 公式表达式 */
    formulaExpression?: string;
    /** 公式实际参数字段JSON */
    formulaField?: string;
    /** 公式id */
    formulaId?: number;
    /** 精度 */
    formulaPrecision?: number;
    /** 公式类型 */
    formulaType?: string;
    /** 组件id */
    id?: number;
    /** 标记该组件是否是一个计算结果（0否1是，默认0） */
    isResult?: number;
    /** 记录版本id */
    recordVersionId?: number;
    /** 修约公式code */
    roundCode?: string;
  }

  /** 数据内容 */
  type RecordSaveFormulaRes = boolean;
  /** */
  type CategoryDeleteByIdRes = string;

  /** /api/app/ems/product/material/category/save */
  interface MaterialCategorySaveReq {
    /**
     * 类别信息类型
     * [0:原辅包信息,
     * 1:中间品信息,
     * 2:产品信息]
     */
    categoryType: number;
    /** 分类编码 */
    code: string;
    /** 分类名称 */
    name: string;
    /** 父级Id */
    parentId?: number;
  }

  /** */
  type MaterialCategorySaveRes = string;

  /** /api/app/ems/product/material/category/tree */
  interface MaterialCategoryTreeReq {
    /**
     * 类别信息类型
     * [0:原辅包信息,
     * 1:中间品信息,
     * 2:产品信息]
     */
    categoryType: number;
    /** 关键字 */
    keyword?: string;
  }

  /** 数据内容 */
  interface MaterialCategoryTreeRes {
    /** 子节点 */
    children?: Array<MaterialCategoryTreeResChildrenVo>;
    /** 编码 */
    code?: string;
    /** 创建时间 */
    createTime?: string;
    /** id */
    id?: number;
    /** 合并编码 */
    mergeCode?: string;
    /** 名称 */
    name?: string;
    /** 父节点 */
    parentId?: number;
    /** 展示名称 */
    showName?: string;
  }

  /** /api/app/ems/product/material/category/update */
  interface MaterialCategoryUpdateReq {
    /** id */
    id: number;
    /** 分类名称 */
    name: string;
  }

  /** */
  type MaterialCategoryUpdateRes = string;

  /** /api/app/ems/product/material/changeStatus */
  interface ProductMaterialChangeStatusReq {
    /** id */
    id: number;
    /**
     * 启停
     * [true:启用,
     * false:停用]
     */
    status: boolean;
  }

  /** */
  type ProductMaterialChangeStatusRes = string;
  /** */
  type MaterialDeleteByIdRes = string;

  /** /api/app/ems/product/material/detail */
  interface ProductMaterialDetailReq {
    /** id */
    id: number;
  }

  /** 数据内容 */
  type ProductMaterialDetailRes = string;

  /** /api/app/ems/product/material/page */
  interface ProductMaterialPageReq {
    /** */
    categoryType: number;
    /** 编码 */
    code?: string;
    /** 排序 */
    dir?: string;
    /** 物料分类id */
    materialCategoryId?: number;
    /** */
    materialCategoryIds?: array;
    /** 合并编码 */
    mergeCode?: string;
    /** 名称 */
    name?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
  }

  /** 数据内容 */
  type ProductMaterialPageRes = string;

  /** /api/app/ems/product/material/save */
  interface ProductMaterialSaveReq {
    /** 业务名称 */
    businessName?: string;
    /** 业务注册 */
    businessRegister?: boolean;
    /** 编码 */
    code: string;
    /** 是否是成品 */
    finishProduct: boolean;
    /** 内包规格 */
    innerPackingSpecification?: string;
    /** 制造商 */
    manufacturer?: string;
    /** 物料分类id */
    materialCategoryId: number;
    /** 名称 */
    name: string;
    /** 包装规格 */
    packingSpecification?: string;
    /** 所属物料id */
    principalMaterialId?: number;
    /** 生产周期(天) */
    productionCycle?: number;
    /** 备注 */
    remark?: string;
    /** 规格 */
    specification: string;
    /** 是否是成员物料/成员产品 */
    subMaterial: boolean;
    /** 供应商 */
    supplier?: string;
    /** 拓展单位id */
    unitExtendId?: number;
    /** 单位id */
    unitId: number;
  }

  /** */
  type ProductMaterialSaveRes = string;

  /** /api/app/ems/product/material/update */
  interface ProductMaterialUpdateReq {
    /** 是否是成品 */
    finishProduct: boolean;
    /** id */
    id: number;
    /** 内包规格 */
    innerPackingSpecification?: string;
    /** 制造商 */
    manufacturer?: string;
    /** 包装规格 */
    packingSpecification?: string;
    /** 生产周期(天) */
    productionCycle?: number;
    /** 备注 */
    remark?: string;
    /** 供应商 */
    supplier?: string;
    /** 拓展单位id */
    unitExtendId: number;
  }

  /** */
  type ProductMaterialUpdateRes = string;

  /** /api/app/ems/record/delete/formula */
  interface RecordDeleteFormulaReq {
    /** componentId */
    componentId: number;
  }

  /** 数据内容 */
  type RecordDeleteFormulaRes = boolean;

  /** /api/app/ems/product/material/issueMaterialAndCategory */
  interface ProductMaterialIssueMaterialAndCategoryReq {
    /** */
    businesses?: Array<number>;
    /** */
    categoryList?: Array<ProductMaterialIssueMaterialAndCategoryReqCategoryListVo>;
    /** */
    materialList?: Array<ProductMaterialIssueMaterialAndCategoryReqMaterialListVo>;
  }

  /** */
  type ProductMaterialIssueMaterialAndCategoryRes = string;

  /** /api/app/ems/product/material/syncTree */
  interface ProductMaterialSyncTreeReq {
    /** */
    categoryType: number;
    /** */
    keyword?: string;
    /** */
    parentId: number;
  }

  /** 数据内容 */
  interface ProductMaterialSyncTreeRes {
    /** 是否为分类节点 */
    categoryFlag?: boolean;
    /** 子集 */
    children?: Array<ProductMaterialSyncTreeResChildrenVo>;
    /** 编码 */
    code?: string;
    /** id */
    id?: number;
    /** 合并编码 */
    mergeCode?: string;
    /** 名称 */
    name?: string;
    /** 展示名 */
    showName?: string;
  }

  /** /api/app/ems/procedure/historic/list */
  interface ProcedureHistoricListReq {
    /** 名称 */
    name?: string;
    /** 工艺id */
    processId: number;
  }

  /** 数据内容 */
  interface ProcedureHistoricListRes {
    /** */
    id?: number;
    /** */
    name?: string;
  }

  /** /api/app/ems/procedure/validate/name */
  interface ProcedureValidateNameReq {
    /** 选中的历史数据的id */
    historicId?: number;
    /** 当前数据的id（编辑时的校验传） */
    id?: number;
    /** 名称 */
    name: string;
    /** 工艺id */
    processId: number;
  }

  /** 数据内容 */
  type ProcedureValidateNameRes = boolean;

  /** /api/app/ems/procedure/step/historic/list */
  interface StepHistoricListReq {
    /** 名称 */
    name?: string;
    /** 工序id */
    procedureId: number;
  }

  /** 数据内容 */
  type StepHistoricListRes = ProcedureHistoricListRes;

  /** /api/app/ems/procedure/step/validate/name */
  interface StepValidateNameReq {
    /** 选中的历史数据的id */
    historicId?: number;
    /** 当前数据的id（编辑时的校验传） */
    id?: number;
    /** 名称 */
    name: string;
    /** 工序id */
    procedureId: number;
  }

  /** 数据内容 */
  type StepValidateNameRes = boolean;

  /** /api/app/ems/product/material/principal/list */
  interface MaterialPrincipalListReq {
    /** 是否过滤 */
    filter: string;
    /** 物料分类id */
    materialCategoryId: number;
  }

  /** 数据内容 */
  interface MaterialPrincipalListRes {
    /** 编码 */
    code?: string;
    /** id */
    id?: number;
    /** 合并编码 */
    mergeCode?: string;
    /** 名称 */
    name?: string;
  }

  /** /api/app/ems/product/material/sync */
  interface ProductMaterialSyncReq {
    /**
     * 业务信息类型
     * [0:原辅包信息,
     * 1:中间品信息,
     * 2:产品信息]
     */
    categoryType: number;
    /** 物料分类ids */
    materialCategoryIds?: Array<number>;
    /** 物料ids */
    materialIds?: Array<number>;
  }

  /** */
  type ProductMaterialSyncRes = string;

  /** 数据内容 */
  interface ListDownBoxRes {
    /** 单位id */
    unitId?: number;
    /** 单位名称 */
    unitName?: string;
  }

  /** /api/app/ems/unit/list/down/extend */
  interface ListDownExtendReq {
    /** standardUnitId */
    standardUnitId: number;
  }

  /** 数据内容 */
  interface ListDownExtendRes {
    /** 表达式 */
    expression?: string;
    /** 拓展单位名称 */
    extendUnitName?: string;
    /** 拓展单位id */
    id?: number;
  }

  /** /api/app/ems/product/material/productList */
  interface ProductMaterialProductListReq {
    /** categoryType */
    categoryType: number;
  }

  /** 数据内容 */
  interface ProductMaterialProductListRes {
    /** 产品ID */
    id?: number;
    /** 内包规格 */
    innerPackingSpecification?: string;
    /** 合并编码 */
    mergeCode?: string;
    /** 产品名称 */
    name?: string;
    /** 包装规格 */
    packingSpecification?: string;
    /** 规格 */
    specification?: string;
  }

  /** /api/app/ems/product/material/productTree */
  interface ProductMaterialProductTreeReq {
    /** categoryType */
    categoryType: number;
  }

  /** 数据内容 */
  interface ProductMaterialProductTreeRes {
    /** 是否是分类 */
    categoryFlag?: boolean;
    /** 子集 */
    children?: Array<ProductMaterialProductTreeResChildrenVo>;
    /** */
    createTime?: string;
    /** id */
    id?: number;
    /** 合并编码 */
    mergeCode?: string;
    /** 分类或产品名称 */
    name?: string;
    /** 父级id */
    parentId?: number;
    /** 展示名称 */
    showName?: string;
  }

  /** /api/app/ems/record/list/record/log */
  interface ListRecordLogReq {
    /** versionId */
    versionId: number;
  }

  /** 数据内容 */
  interface ListRecordLogRes {
    /** 操作人 */
    createBy?: string;
    /** 操作时间 */
    createTime?: string;
    /** 操作人名称 */
    createUsername?: string;
    /** 操作类型 */
    operationType?: string;
    /** 操作类型名称 */
    operationTypeName?: string;
    /** 操作 */
    remark?: string;
  }

  /** 数据内容 */
  interface ListRecordTreeRes {
    /** 分类id */
    categoryId?: number;
    /** 子集 */
    children?: Array<ListRecordTreeResChildrenVo>;
    /** 创建时间 */
    createTime?: string;
    /** 主键id */
    id?: number;
    /** 分类名称 */
    name?: string;
    /** 父级id */
    parentId?: number;
  }

  /** 数据内容 */
  interface ListRecordVersionRes {
    /** 记录id */
    id?: number;
    /** 记录名称 */
    name?: string;
    /** 版本集合 */
    versionList?: Array<ListRecordVersionResVersionListVo>;
  }

  /** 数据内容 */
  interface RecordListRoundingRes {
    /** */
    label?: string;
    /** */
    value?: string;
  }

  /** 数据内容 */
  type ProductMaterialSyncTreeAllRes = ProductMaterialSyncTreeRes;

  /** /api/app/ems/product/material/save/batchRecord */
  interface MaterialSaveBatchRecordReq {
    /** 产品id */
    productId?: number;
    /** 批记录id列表 */
    recordIds?: Array<number>;
  }

  /** */
  type MaterialSaveBatchRecordRes = string;

  /** /api/app/ems/product/material/bindRecordIds */
  interface ProductMaterialBindRecordIdsReq {
    /** productId */
    productId: number;
  }

  /** 数据内容 */
  type ProductMaterialBindRecordIdsRes = PermissionListDeptRes;

  /** 数据内容 */
  interface PermissionDeptTreeRes {
    /** */
    children?: Array<PermissionDeptTreeResChildrenVo>;
    /** */
    createTime?: string;
    /** */
    id?: number;
    /** */
    name?: string;
    /** */
    parentId?: number;
  }

  /** /api/app/ems/product/material/category/allChildIds */
  interface MaterialCategoryAllChildIdsReq {
    /** parentId */
    parentId: number;
  }

  /** 数据内容 */
  type MaterialCategoryAllChildIdsRes = PermissionListDeptRes;

  /** /api/app/ems/procedure/list */
  interface MesProcedureListReq {
    /** 工艺id，与 version 同时传 */
    processId?: number;
    /** 工艺版本id，可单独使用 */
    processVersionId?: number;
    /** */
    validate?: string;
    /** 版本号 */
    version?: string;
  }

  /** 数据内容 */
  interface MesProcedureListRes {
    /** 时长 */
    duration?: number;
    /** 班组id集合 */
    groupIds?: Array<number>;
    /** id */
    id?: number;
    /** 名称 */
    name?: string;
    /** 节点id */
    nodeId?: string;
    /** 负责人 */
    principal?: number;
    /** 工序id */
    procedureId?: number;
    /** 流程模型Id */
    processModelId?: string;
    /** 阶段编码 */
    stageCode?: string;
    /** 单位 */
    timeUnit?: string;
  }

  /** /api/app/ems/process/version/list */
  interface ProcessVersionListReq {
    /** 工艺id */
    processId?: number;
    /** 产品id */
    productId?: number;
    /** */
    state?: string;
  }

  /** 数据内容 */
  interface ProcessVersionListRes {
    /** 版本id */
    id?: number;
    /** 名称 */
    name?: string;
    /** 工艺id */
    processId?: number;
    /** 流程模型id */
    processModelId?: string;
    /** 产品分类id */
    productCategoryId?: number;
    /** 产品id */
    productId?: number;
    /** 启用状态 */
    state?: boolean;
    /** 版本号 */
    version?: string;
  }

  /** 数据内容 */
  type PermissionPartitionTreeRes = PermissionDeptTreeRes;

  /** /api/app/ems/procedure/detail/{id} */
  interface ProcedureDetailByIdReq {
    /** id */
    id: number;
  }

  /** 数据内容 */
  type ProcedureDetailByIdRes = string;

  /** /api/app/ems/process/version/record/order */
  interface VersionRecordOrderReq {
    /** 工艺id */
    processId: number;
    /** 工艺版本 */
    processVersion?: string;
  }

  /** 数据内容 */
  interface VersionRecordOrderRes {
    /** 工序，工序步骤名称 */
    procedureName?: string;
    /** 记录项id */
    recordItemId?: number;
    /** 记录项名称 */
    recordItemName?: string;
    /** 排序 */
    recordItemOrder?: number;
  }

  /** /api/app/ems/process/version/save/order */
  interface VersionSaveOrderReq {
    /** 工艺id */
    processId?: number;
    /** 工艺版本 */
    processVersion?: string;
    /** 工艺版本id */
    processVersionId?: number;
    /** 记录项顺序 */
    recordOrders?: Array<VersionSaveOrderReqRecordOrdersVo>;
  }

  /** */
  type VersionSaveOrderRes = string;

  /** /api/app/ems/record/flow/audit/page */
  interface FlowAuditPageReq {
    /** 流程编码 */
    code?: string;
    /** 排序 */
    dir?: string;
    /** 流程模型id */
    id?: number;
    /** 流程名称 */
    name?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 流程状态 */
    state?: number;
  }

  /** 数据内容 */
  type FlowAuditPageRes = string;

  /** /api/app/ems/process/list */
  interface MesProcessListReq {
    /** 是否已启用 */
    active?: string;
    /** 产品id */
    productId?: number;
  }

  /** 数据内容 */
  interface MesProcessListRes {
    /** 版本号 */
    activeVersion?: string;
    /** 版本id */
    id?: number;
    /** 名称 */
    name?: string;
  }

  /** /api/app/ems/process/relation/processes */
  interface ProcessRelationProcessesReq {
    /** 工艺id */
    processId: number;
  }

  /** 数据内容 */
  type ProcessRelationProcessesRes = MesProcessListRes;

  /** /api/app/ems/plan/info/save */
  interface PlanInfoSaveReq {
    /** 生产批号 */
    batchNo?: string;
    /** 批号编码规则code */
    batchNoCode?: string;
    /** 批号回传编号日期 */
    batchNoCodeApplyTime?: string;
    /** 内包规格 */
    innerPackingSpecification?: string;
    /** 包装规格 */
    packingSpecification?: string;
    /** 计划编号 */
    planNo?: string;
    /** 计划编码规则code */
    planNoCode?: string;
    /** 计划编码回传编号日期 */
    planNoCodeApplyTime?: string;
    /** 生产工艺id */
    processId?: number;
    /** 生产工艺名称 */
    processName?: string;
    /** 生产工艺数量 */
    processNum?: number;
    /** 生产工艺版本 */
    processVersion?: string;
    /** 生产时间 */
    productDate?: string;
    /** 产品Id */
    productId?: number;
    /** 产品编码 */
    productMergeCode?: string;
    /** 产品名称 */
    productName?: string;
    /** 产品规格 */
    productSpecification?: string;
    /** 计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次 */
    type?: string;
  }

  /** */
  type PlanInfoSaveRes = string;

  /** /api/app/ems/record/checkout/deployment */
  interface RecordCheckoutDeploymentReq {
    /** flowAuditModel */
    flowAuditModel: string;
  }

  /** 数据内容 */
  type RecordCheckoutDeploymentRes = boolean;

  /** /api/app/ems/record/save/flow/audit */
  interface SaveFlowAuditReq {
    /** 消息通知人员集合 */
    auditMegDTOList?: Array<SaveFlowAuditReqAuditMegDTOListVo>;
    /** 审核人员集合 */
    auditUserList?: Array<SaveFlowAuditReqAuditUserListVo>;
    /** 分类标识 */
    categoryCode?: string;
    /** 是否增加版本 */
    changeVersion?: boolean;
    /** 流程编码 */
    code?: string;
    /** 流程定义id */
    deploymentId?: string;
    /** 流程id */
    flowAuditId?: number;
    /** 流程模型 */
    flowAuditModel?: string;
    /** 流程名称 */
    name?: string;
    /** 备注 */
    remark?: string;
    /** 版本号 */
    version?: string;
    /** 版本id */
    versionId?: number;
  }

  /** 数据内容 */
  type SaveFlowAuditRes = boolean;

  /** /api/app/ems/plan/info/batchSave */
  interface PlanInfoBatchSaveReq {
    /** 批号编码规则code */
    batchNoCode?: string;
    /** 批号回传编号日期 */
    batchNoCodeApplyTime?: string;
    /** 生产计划明细 */
    details?: Array<PlanInfoBatchSaveReqDetailsVo>;
    /** 内包规格 */
    innerPackingSpecification?: string;
    /** 包装规格 */
    packingSpecification?: string;
    /** 计划编码规则code */
    planNoCode?: string;
    /** 计划编码回传编号日期 */
    planNoCodeApplyTime?: string;
    /** 生产工艺id */
    processId?: number;
    /** 生产工艺名称 */
    processName?: string;
    /** 生产工艺数量 */
    processNum?: number;
    /** 生产工艺版本 */
    processVersion?: string;
    /** 产品Id */
    productId?: number;
    /** 产品编码 */
    productMergeCode?: string;
    /** 产品名称 */
    productName?: string;
    /** 产品规格 */
    productSpecification?: string;
  }

  /** */
  type PlanInfoBatchSaveRes = string;

  /** /api/app/ems/plan/info/detail/{id} */
  interface InfoDetailByIdReq {
    /** id */
    id: number;
  }

  /** 生产计划对象 */
  type InfoDetailByIdRes = string;
  /** */
  type InfoDiscardByIdRes = string;

  /** /api/app/ems/plan/info/update */
  interface PlanInfoUpdateReq {
    /** 计划id */
    id?: number;
    /** 生产时间 */
    productDate?: string;
    /** 计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次 */
    type?: string;
  }

  /** */
  type PlanInfoUpdateRes = string;

  /** /api/app/ems/operation/history/list/{businessId} */
  interface HistoryListByBusinessIdReq {
    /** businessId */
    businessId: number;
  }

  /** 数据内容 */
  type HistoryListByBusinessIdRes = ListRecordLogRes;

  /** /api/app/ems/operation/history/page */
  interface OperationHistoryPageReq {
    /** 业务数据id */
    businessId?: number;
    /** 排序 */
    dir?: string;
    /** 业务模块 */
    module?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
  }

  /** 数据内容 */
  type OperationHistoryPageRes = string;
  /** */
  type InfoApproveByIdRes = string;

  /** /api/app/ems/plan/info/page */
  interface PlanInfoPageReq {
    /** 生产批号 */
    batchNo?: string;
    /** 排序 */
    dir?: string;
    /** id集合 */
    ids?: array;
    /** 状态 待分解WAIT_DECOMPOSE 待确认WAIT_CONFIRM 待下发WAIT_SEND 已下发 SEND */
    instructStatus?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 计划编号 */
    planNo?: string;
    /** 负责人 */
    principal?: string;
    /** 生产工艺名称 */
    processName?: string;
    /** 产品Id */
    productId?: number;
    /** 产品名称 */
    productName?: string;
    /** 状态 编辑EDIT 审批中AUDIT 确认CONFIRM 废弃DISCARD */
    status?: string;
    /** 计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次 */
    type?: string;
  }

  /** 数据内容 */
  type PlanInfoPageRes = string;

  /** /api/app/ems/record/delete/flow/audit */
  interface DeleteFlowAuditReq {
    /** versionId */
    versionId: number;
  }

  /** 数据内容 */
  type DeleteFlowAuditRes = boolean;
  /** /api/app/ems/record/deploy/flow/audit */
  type DeployFlowAuditReq = SaveFlowAuditReq;
  /** 数据内容 */
  type DeployFlowAuditRes = boolean;

  /** /api/app/ems/record/detail/flow/audit */
  interface DetailFlowAuditReq {
    /** versionId */
    versionId: number;
  }

  /** 数据内容 */
  type DetailFlowAuditRes = string;

  /** /api/app/ems/plan/instruction/detail/{id} */
  interface InstructionDetailByIdReq {
    /** id */
    生产计划id: number;
    /** */
    id: string;
  }

  /** 数据内容 */
  type InstructionDetailByIdRes = string;
  /** */
  type InstructionGenerateByIdRes = string;

  /** /api/app/ems/plan/instruction/page */
  interface PlanInstructionPageReq {
    /** 生产批号 */
    batchNo?: string;
    /** 排序 */
    dir?: string;
    /** id集合 */
    ids?: array;
    /** 状态 待分解WAIT_DECOMPOSE 待确认WAIT_CONFIRM 待下发WAIT_SEND 已下发 SEND */
    instructStatus?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 计划编号 */
    planNo?: string;
    /** 负责人 */
    principal?: string;
    /** 生产工艺名称 */
    processName?: string;
    /** 产品Id */
    productId?: number;
    /** 产品名称 */
    productName?: string;
    /** 状态 编辑EDIT 审批中AUDIT 确认CONFIRM 废弃DISCARD */
    status?: string;
    /** 计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次 */
    type?: string;
  }

  /** 数据内容 */
  type PlanInstructionPageRes = string;

  /** /api/app/ems/plan/instruction/save */
  interface PlanInstructionSaveReq {
    /** 生产工序节点id */
    nodeId?: string;
    /** 负责人 */
    principal?: number;
    /** 历史工序id(以此判断多给版本的节点是否是同一工序) */
    procedureId?: number;
    /** 生产工序阶段编码 */
    procedureModelCode?: string;
    /** 生产工序id */
    procedureModelId?: number;
    /** 生产工序名称 */
    procedureModelName?: string;
    /** 生产计划id */
    productPlanId?: number;
    /** 排序 */
    sort?: number;
  }

  /** */
  type PlanInstructionSaveRes = string;
  /** */
  type InstructionSendByIdRes = string;

  /** /api/app/ems/plan/instruction/update */
  interface PlanInstructionUpdateReq {
    /** 指令单id */
    id?: number;
    /** 负责人 */
    principal?: number;
    /** 生产计划id */
    productPlanId?: number;
  }

  /** */
  type PlanInstructionUpdateRes = string;

  /** /api/app/ems/plan/instruction/team/confirm */
  interface InstructionTeamConfirmReq {
    /** 生产工序节点id */
    details?: Array<InstructionTeamConfirmReqDetailsVo>;
    /** 指令单id */
    instructionId?: number;
    /** 生产工序节点id */
    nodeId?: string;
    /** 历史工序id(以此判断多给版本的节点是否是同一工序) */
    procedureId?: number;
    /** 历史工序id(以此判断多给版本的节点是否是同一工序) */
    procedureModelId?: number;
    /** 生产计划id */
    productPlanId?: number;
  }

  /** */
  type InstructionTeamConfirmRes = string;

  /** /api/app/ems/plan/instruction/team/detail/{id} */
  interface TeamDetailByIdReq {
    /** id */
    id: number;
  }

  /** 数据内容 */
  type TeamDetailByIdRes = string;
  /** /api/app/ems/plan/instruction/team/save */
  type InstructionTeamSaveReq = InstructionTeamConfirmReq;
  /** */
  type InstructionTeamSaveRes = string;

  /** /api/app/ems/record/checkout/save/record */
  interface CheckoutSaveRecordReq {
    /** recordId */
    recordId: number;
  }

  /** 数据内容 */
  type CheckoutSaveRecordRes = boolean;

  /** /api/app/ems/audit/checkout/deployment */
  interface AuditCheckoutDeploymentReq {
    /** 流程模型 */
    flowAuditModel?: string;
    /** 消息人员 */
    megUserList?: Array<AuditCheckoutDeploymentReqMegUserListVo>;
    /** 人员信息 */
    userList?: Array<AuditCheckoutDeploymentReqUserListVo>;
  }

  /** 数据内容 */
  type AuditCheckoutDeploymentRes = boolean;

  /** /api/app/ems/audit/delete/flow/audit */
  interface DeleteFlowAuditReq {
    /** versionId */
    versionId: number;
  }

  /** 数据内容 */
  type DeleteFlowAuditRes = boolean;
  /** /api/app/ems/audit/deploy/flow/audit */
  type DeployFlowAuditReq = SaveFlowAuditReq;
  /** 数据内容 */
  type DeployFlowAuditRes = boolean;

  /** /api/app/ems/audit/detail/flow/audit */
  interface DetailFlowAuditReq {
    /** versionId */
    versionId: number;
  }

  /** 数据内容 */
  type DetailFlowAuditRes = string;

  /** /api/app/ems/audit/flow/audit/page */
  interface FlowAuditPageReq {
    /** 分类标识 */
    categoryCode?: string;
    /** 流程编码 */
    code?: string;
    /** 排序 */
    dir?: string;
    /** 流程模型id */
    id?: number;
    /** 流程名称 */
    name?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 流程状态 */
    state?: number;
  }

  /** 数据内容 */
  type FlowAuditPageRes = string;
  /** /api/app/ems/audit/save/flow/audit */
  type SaveFlowAuditReq = SaveFlowAuditReq;
  /** 数据内容 */
  type SaveFlowAuditRes = boolean;

  /** /api/app/ems/plan/instruction/start/page */
  interface InstructionStartPageReq {
    /** 生产批号 */
    batchNo?: string;
    /** 排序 */
    dir?: string;
    /** id集合 */
    ids?: array;
    /** 状态 待分解WAIT_DECOMPOSE 待确认WAIT_CONFIRM 待下发WAIT_SEND 已下发 SEND */
    instructStatus?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 计划编号 */
    planNo?: string;
    /** 负责人 */
    principal?: string;
    /** 生产工艺名称 */
    processName?: string;
    /** 产品Id */
    productId?: number;
    /** 产品名称 */
    productName?: string;
    /** 状态 编辑EDIT 审批中AUDIT 确认CONFIRM 废弃DISCARD */
    status?: string;
    /** 计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次 */
    type?: string;
  }

  /** 数据内容 */
  type InstructionStartPageRes = string;

  /** /api/app/ems/log/page */
  interface MesLogPageReq {
    /** 排序 */
    dir?: string;
    /** 结束时间 */
    endTime?: string;
    /** */
    menuId?: number;
    /** */
    operationType?: number;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 开始时间 */
    startTime?: string;
    /** 操作人 */
    userName?: string;
  }

  /** 数据内容 */
  type MesLogPageRes = string;

  /** /api/app/ems/plan/instruction/team/start/confirm */
  interface TeamStartConfirmReq {
    /** 生产计划id */
    planId?: number;
    /** 关联生产计划 */
    relationPlan?: Array<TeamStartConfirmReqRelationPlanVo>;
    /** 生产工序节点id */
    teamConfirmDTO?: Array<InstructionTeamConfirmReq>;
  }

  /** */
  type TeamStartConfirmRes = string;

  /** /api/app/ems/plan/info/startPage */
  interface PlanInfoStartPageReq {
    /** 生产批号 */
    batchNo?: string;
    /** 生产工艺id */
    processId?: number;
    /** 是否关联 未关联FALSE 已关联TRUE */
    relation?: string;
  }

  /** 数据内容 */
  interface PlanInfoStartPageRes {
    /** 生产批号 */
    batchNo?: string;
    /** id */
    id?: number;
  }

  /** /api/app/ems/plan/code/rule/page */
  interface CodeRulePageReq {
    /** 排序 */
    dir?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 工艺名称 */
    processName?: string;
    /** 产品编码 */
    productCode?: string;
    /** 产品名称 */
    productName?: string;
    /** 类型 生产批号规则 PRODUCT_PLAN_BATCH_NO 生产计划批号规则 PRODUCT_PLAN_NO */
    type?: string;
  }

  /** 数据内容 */
  type CodeRulePageRes = string;

  /** /api/app/ems/plan/code/rule/save */
  interface CodeRuleSaveReq {
    /** 编码规则code */
    codeRuleCode?: string;
    /** 编码规则名称 */
    codeRuleName?: string;
    /** 生产工艺id */
    processIds?: Array<number>;
    /** 生产计划编码规则分类 PRODUCT_PLAN_NO 生产计划批号规则 PRODUCT_PLAN_BATCH_NO 生产批号规则 */
    type?: string;
  }

  /** */
  type CodeRuleSaveRes = string;

  /** /api/app/ems/plan/code/rule/update */
  interface CodeRuleUpdateReq {
    /** 编码规则code */
    codeRuleCode?: string;
    /** 编码规则名称 */
    codeRuleName?: string;
    /** 生产计划编码规则id */
    id?: number;
    /** 生产工艺id */
    processId?: number;
    /** 编码类型 */
    type?: string;
  }

  /** */
  type CodeRuleUpdateRes = string;

  /** /api/app/ems/plan/team/save */
  interface PlanTeamSaveReq {
    /** 班组编码 */
    code?: string;
    /** 班组描述 */
    description?: string;
    /** 班组名称 */
    name?: string;
    /** 班组人员 */
    people?: Array<string>;
  }

  /** */
  type PlanTeamSaveRes = string;
  /** */
  type TeamDisableByIdRes = string;
  /** */
  type TeamEnableByIdRes = string;

  /** /api/app/ems/plan/team/list */
  interface PlanTeamListReq {
    /** 班组编码 */
    code?: string;
    /** 班组名称 */
    name?: string;
    /** 状态 */
    status?: string;
  }

  /** 数据内容 */
  interface PlanTeamListRes {
    /** 班组编码 */
    code?: string;
    /** 状态 TRUE 启用 FALSE 禁用 */
    createTime?: string;
    /** 班组描述 */
    description?: string;
    /** 班组名称 */
    id?: number;
    /** 班组名称 */
    name?: string;
    /** 班组人数 */
    peopleNum?: number;
    /** 班组人员 */
    peoples?: Array<string>;
    /** 状态 TRUE 启用 FALSE 禁用 */
    status?: string;
  }

  /** /api/app/ems/plan/team/page */
  interface PlanTeamPageReq {
    /** 班组编码 */
    code?: string;
    /** 排序 */
    dir?: string;
    /** 班组名称 */
    name?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
  }

  /** 数据内容 */
  type PlanTeamPageRes = string;

  /** /api/app/ems/plan/team/update */
  interface PlanTeamUpdateReq {
    /** 班组编码 */
    code?: string;
    /** 班组描述 */
    description?: string;
    /** id */
    id?: number;
    /** 班组名称 */
    name?: string;
    /** 班组人员 */
    people?: Array<string>;
  }

  /** */
  type PlanTeamUpdateRes = string;

  /** /api/app/ems/platform/query/codeRule/getBatchNextUseNo */
  interface QueryCodeRuleGetBatchNextUseNoReq {
    /** 编码规则code */
    code?: string;
    /** 编码规则详情传参 */
    fields?: object;
    /** 生成数量 */
    num?: number;
    /** 工序id */
    processId?: number;
    /** 生产批号规则 PRODUCT_PLAN_BATCH_NO 生产计划批号规则 PRODUCT_PLAN_NO */
    type?: string;
  }

  /** 数据内容 */
  type QueryCodeRuleGetBatchNextUseNoRes = string;

  /** /api/app/ems/platform/query/codeRule/getNextUseNo */
  interface QueryCodeRuleGetNextUseNoReq {
    /** 编码规则code */
    code?: string;
    /** 编码规则详情传参 */
    fields?: object;
    /** 工序id */
    processId?: number;
    /** 生产批号规则 PRODUCT_PLAN_BATCH_NO 生产计划批号规则 PRODUCT_PLAN_NO */
    type?: string;
  }

  /** 数据内容 */
  type QueryCodeRuleGetNextUseNoRes = string;

  /** 数据内容 */
  interface QueryCodeRuleListRes {
    /** 编码 */
    code?: string;
    /** 名称 */
    name?: string;
  }

  /** 数据内容 */
  interface QueryDeptTreeRes {
    /** 子数据 */
    children?: Array<QueryDeptTreeResChildrenVo>;
    /** 编码 */
    code?: string;
    /** 创建时间 */
    createTime?: string;
    /** 部门下是否存在用户 */
    existUser?: boolean;
    /** id */
    id?: number;
    /** 部门名称 */
    name?: string;
    /** 父级编码 */
    parentCode?: string;
    /** 父级id */
    parentId?: number;
    /** 父级部门名称 */
    parentName?: string;
    /** 备注 */
    remark?: string;
  }

  /** 数据内容 */
  interface DeptUserTreeRes {
    /** 子数据 */
    children?: Array<DeptUserTreeResChildrenVo>;
    /** 编码 */
    code?: string;
    /** 创建时间 */
    createTime?: string;
    /** 是否是部门 */
    deptFlag?: boolean;
    /** id */
    id?: string;
    /** 用户账号 */
    loginName?: string;
    /** 部门或人名称 */
    name?: string;
    /** 父级id */
    parentId?: string;
    /** 父级部门名称 */
    parentName?: string;
    /** 备注 */
    remark?: string;
  }

  /** 数据内容 */
  type DeptUserUnassignedRes = string;

  /** /api/app/ems/platform/query/role/detail/{id} */
  interface RoleDetailByIdReq {
    /** id */
    id: number;
  }

  /** 数据内容 */
  type RoleDetailByIdRes = string;

  /** /api/app/ems/platform/query/role/list */
  interface QueryRoleListReq {
    /** */
    ids?: array;
  }

  /** 数据内容 */
  interface QueryRoleListRes {
    /** id */
    id?: number;
    /** 名称 */
    roleName?: string;
  }

  /** 数据内容 */
  type MesAuditTestRes = boolean;
  /** 数据内容 */
  type RuleDetailCodeByCodeRes = PermissionListDeptRes;

  /** /api/app/ems/platform/query/user/listByRole */
  interface QueryUserListByRoleReq {
    /** 角色id */
    roleId: number;
  }

  /** 数据内容 */
  interface QueryUserListByRoleRes {
    /** */
    loginName?: string;
    /** */
    userId?: string;
    /** */
    userName?: string;
  }

  /** 数据内容 */
  interface ProcessProductTreeRes {
    /** 子节点集合 */
    children?: Array<ProcessProductTreeResChildrenVo>;
    /** 创建时间 */
    createTime?: string;
    /** id */
    id?: number;
    /** 名称 */
    name?: string;
    /** 父节点id */
    parentId?: number;
    /** 标记是否是是工艺 */
    processFlag?: boolean;
    /** 标记是否是产品分类 */
    productCategoryFlag?: boolean;
    /** 标记是否是产品 */
    productFlag?: boolean;
  }

  /** /api/app/ems/plan/team/detail/{id} */
  interface TeamDetailByIdReq {
    /** id */
    id: number;
  }

  /** 数据内容 */
  type TeamDetailByIdRes = string;

  /** /api/app/ems/log/export */
  interface MesLogExportReq {
    /** 结束时间 */
    endTime?: string;
    /** 菜单id */
    menuId?: number;
    /** */
    operationType?: number;
    /** 已选择的id列表 */
    selectIds?: array;
    /** 开始时间 */
    startTime?: string;
    /** 操作人 */
    userName?: string;
  }

  /** */
  type MesLogExportRes = string;
  /** 数据内容 */
  type FlowAuditCodeRes = number;

  /** /api/app/ems/flow/plan/manage/page */
  interface PlanManagePageReq {
    /** 生产批号 */
    batchNo?: string;
    /** 排序 */
    dir?: string;
    /** 排序字段（batch_no，start_time） */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 产品Id */
    productId?: number;
  }

  /** 数据内容 */
  type PlanManagePageRes = string;

  /** /api/app/ems/flow/procedure/restart */
  interface FlowProcedureRestartReq {
    /** 执行实例id */
    executionId?: string;
  }

  /** */
  type FlowProcedureRestartRes = string;

  /** /api/app/ems/flow/procedures/{processInstanceId} */
  interface FlowProceduresByProcessInstanceIdReq {
    /** processInstanceId */
    processInstanceId: string;
  }

  /** 数据内容 */
  type FlowProceduresByProcessInstanceIdRes = string;

  /** /api/app/ems/flow/steps/{executionId} */
  interface FlowStepsByExecutionIdReq {
    /** executionId */
    executionId: string;
  }

  /** 数据内容 */
  type FlowStepsByExecutionIdRes = string;

  /** /api/app/ems/flow/todoPage */
  interface MesFlowTodoPageReq {
    /** 生产批号 */
    batchNo?: string;
    /** 排序 */
    dir?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 产品Id */
    productId?: number;
  }

  /** 数据内容 */
  type MesFlowTodoPageRes = string;

  /** /api/app/ems/procedure/step/record/item */
  interface StepRecordItemReq {
    /** 工序步骤流程节点 */
    nodeId: string;
    /** 工艺id */
    processId: number;
    /** 工艺版本号 */
    processVersion: string;
  }

  /** 数据内容 */
  type StepRecordItemRes = string;

  /** /api/app/ems/execute/batch/save */
  interface ExecuteBatchSaveReq {
    /** 批号 */
    batchNo: string;
    /** 复制版本号 */
    copyVersion: number;
    /** 数据集 */
    items: Array<ExecuteBatchSaveReqItemsVo>;
    /** 工序步骤id */
    procedureStepId: number;
    /** 工艺id */
    processId: number;
    /** 工艺版本号 */
    processVersion: string;
    /** 生产计划id */
    productPlanId: number;
    /** 记录项id */
    recordItemId: number;
    /** 记录项版本id */
    recordVersionId: number;
    /** 是否复用 */
    reuse: boolean;
  }

  /** */
  type ExecuteBatchSaveRes = string;

  /** /api/app/ems/execute/save */
  interface MesExecuteSaveReq {
    /** 批号 */
    batchNo: string;
    /** 组件类型 */
    componentType: string;
    /** 复制版本号 */
    copyVersion: number;
    /** 组件id */
    fieldId: number;
    /** 操作时间 */
    operationTime: string;
    /** 操作人 */
    operationUser: string;
    /** 工序步骤id */
    procedureStepId: number;
    /** 工艺id */
    processId: number;
    /** 工艺版本号 */
    processVersion: string;
    /** 生产计划id */
    productPlanId: number;
    /** 记录项id */
    recordItemId: number;
    /** 记录项版本id */
    recordVersionId: number;
    /** 备注 */
    remark?: string;
    /** 是否复用 */
    reuse: boolean;
    /** 操作时间 */
    reviewTime?: string;
    /** 复核人 */
    reviewUser?: string;
    /** 数据值 */
    value: string;
    /** 数据值扩展（如checkbox的所有值） */
    valueExtension: string;
  }

  /** */
  type MesExecuteSaveRes = string;

  /** /api/app/ems/dataset/datapoint/relation */
  interface DatasetDatapointRelationReq {
    /** 数据集版本id */
    datasetVersionId: number;
    /** 工艺版本id */
    processVersionId: number;
  }

  /** 数据内容 */
  interface DatasetDatapointRelationRes {
    /** 数据点id */
    datapointId?: number;
    /** 数据集版本id */
    datasetVersionId?: number;
    /** 组件id */
    fieldId?: number;
    /** 绑定关系id */
    id?: number;
    /** 工艺版本id */
    processVersionId?: number;
  }

  /** */
  type DatapointUnbindByIdRes = string;

  /** /api/app/ems/dataset/page */
  interface MesDatasetPageReq {
    /** 是否是分类 */
    categoryFlag?: string;
    /** 排序 */
    dir?: string;
    /** */
    id: number;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** */
    productIdList?: array;
  }

  /** 数据内容 */
  type MesDatasetPageRes = string;

  /** /api/app/ems/dataset/point/group/move */
  interface PointGroupMoveReq {
    /** 是否是数据点 */
    datapointFlag?: boolean;
    /** 移动的分组id */
    id?: number;
    /** 目标分组id */
    targetId?: number;
  }

  /** */
  type PointGroupMoveRes = string;

  /** /api/app/ems/dataset/point/group/save */
  interface PointGroupSaveReq {
    /** 数据集id */
    datasetId?: number;
    /** 数据集版本id */
    datasetVersionId?: number;
    /** 分组名称列表 */
    nameList?: Array<string>;
    /** 父级id */
    parentId?: number;
  }

  /** */
  type PointGroupSaveRes = string;
  /** */
  type PointGroupByIdRes = string;

  /** /api/app/ems/dataset/point/save */
  interface DatasetPointSaveReq {
    /** 数据集id */
    datasetId?: number;
    /** 数据集版本id */
    datasetVersionId?: number;
    /** 分组id */
    groupId?: number;
    /** 数据点名称列表 */
    nameList?: Array<string>;
    /** 工艺id */
    processId?: number;
  }

  /** 数据内容 */
  type DatasetPointSaveRes = string;
  /** */
  type DatasetPointByIdRes = string;

  /** /api/app/ems/dataset/relation/list */
  interface DatasetRelationListReq {
    /** 数据集类型 */
    datasetType: number;
    /** 工艺id */
    processId: number;
    /** 模板版本id */
    templateVersionId: number;
  }

  /** 数据内容 */
  interface DatasetRelationListRes {
    /** 数据集类型 */
    datasetType?: number;
    /** 数据集id */
    id?: number;
    /** 数据集名称 */
    name?: string;
    /** 工艺名称 */
    processName?: string;
    /** 数据集版本列表 */
    versionList?: Array<DatasetRelationListResVersionListVo>;
  }

  /** /api/app/ems/dataset/save */
  interface MesDatasetSaveReq {
    /** 数据集类型 */
    datasetType?: number;
    /** 描述 */
    description?: string;
    /** 数据集名称 */
    name?: string;
    /** 关联工艺id */
    processId?: number;
    /** 关联工艺名称 */
    processName?: string;
    /** 关联产品id */
    productId?: number;
    /** 关联产品名称 */
    productName?: string;
  }

  /** 数据内容 */
  type MesDatasetSaveRes = number;

  /** /api/app/ems/dataset/template/relation */
  interface DatasetTemplateRelationReq {
    /** 数据集类型 */
    datasetType?: number;
    /** 模板版本id */
    templateVersionId: number;
  }

  /** 数据内容 */
  interface DatasetTemplateRelationRes {
    /** 数据集名称 */
    datasetName?: string;
    /** 数据集版本id */
    datasetVersionId?: number;
    /** 数据集版本号 */
    versionNumber?: number;
  }

  /** */
  type VersionConfirmByIdRes = string;

  /** /api/app/ems/dataset/version/detail */
  interface DatasetVersionDetailReq {
    /** versionId */
    versionId: number;
  }

  /** 数据内容 */
  type DatasetVersionDetailRes = string;

  /** /api/app/ems/dataset/version/page */
  interface DatasetVersionPageReq {
    /** 数据集id */
    datasetId: number;
    /** 排序 */
    dir?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
  }

  /** 数据内容 */
  type DatasetVersionPageRes = string;

  /** /api/app/ems/dataset/version/update */
  interface DatasetVersionUpdateReq {
    /** 备注 */
    remark?: string;
    /** 升级版本id */
    sourceVersionId?: number;
  }

  /** 数据内容 */
  type DatasetVersionUpdateRes = number;

  /** /api/app/ems/execute/field/data/list */
  interface FieldDataListReq {
    /** 复制版本号 */
    copyVersion: number;
    /** 是否查询作废数据 */
    discard?: string;
    /** 组件id */
    fieldId: number;
    /** 历史工序步骤id */
    procedureStepId: number;
    /** 生产计划id */
    productPlanId: number;
    /** 是否复用 */
    reuse: string;
  }

  /** 数据内容 */
  interface FieldDataListRes {
    /** 组件id */
    fieldId?: number;
    /** 操作时间 */
    operationTime?: string;
    /** 操作类型 */
    operationType?: string;
    /** 操作类型名称 */
    operationTypeName?: string;
    /** 操作人id */
    operationUser?: string;
    /** 操作人名称 */
    operationUsername?: string;
    /** 复核时间 */
    reviewTime?: string;
    /** 复核人 */
    reviewUser?: string;
    /** 复核人名称 */
    reviewUsername?: string;
    /** 是否是系统计算 */
    systemCreate?: boolean;
    /** 值 */
    value?: string;
    /** 值扩展 */
    valueExtension?: string;
  }

  /** /api/app/ems/execute/item/latest/data */
  interface ItemLatestDataReq {
    /** 复制版本号，未被复制传0 */
    copyVersion: number;
    /** 是否查询废弃值 */
    discard?: string;
    /** 工序步骤id */
    procedureStepId: number;
    /** 生产计划id */
    productPlanId: number;
    /** 记录项id */
    recordItemId: number;
    /** 是否复用 */
    reuse: string;
  }

  /** 数据内容 */
  interface ItemLatestDataRes {
    /** 组件id */
    fieldId?: number;
    /** 操作类型 */
    operationType?: string;
    /** 值 */
    value?: string;
    /** 值 */
    valueExtension?: string;
  }

  /** /api/app/ems/execute/lock/step */
  interface ExecuteLockStepReq {
    /** 工序步骤id */
    procedureStepId: number;
    /** 生产计划id */
    productPlanId: number;
  }

  /** */
  type ExecuteLockStepRes = string;

  /** /api/app/ems/execute/modify */
  interface MesExecuteModifyReq {
    /** 批号 */
    batchNo: string;
    /** 组件类型 */
    componentType: string;
    /** 复制版本号 */
    copyVersion: number;
    /** 组件id */
    fieldId: number;
    /** 操作时间 */
    operationTime: string;
    /** 操作人 */
    operationUser: string;
    /** 工序步骤id */
    procedureStepId: number;
    /** 工艺id */
    processId: number;
    /** 工艺版本号 */
    processVersion: string;
    /** 生产计划id */
    productPlanId: number;
    /** 记录项id */
    recordItemId: number;
    /** 记录项版本id */
    recordVersionId: number;
    /** 备注 */
    remark: string;
    /** 是否复用 */
    reuse: boolean;
    /** 复核时间 */
    reviewTime: string;
    /** 复核人 */
    reviewUser: string;
    /** 数据值 */
    value: string;
    /** 数据值扩展（如checkbox的所有值） */
    valueExtension: string;
  }

  /** */
  type MesExecuteModifyRes = string;
  /** /api/app/ems/execute/unLock/step */
  type ExecuteUnLockStepReq = ExecuteLockStepReq;
  /** */
  type ExecuteUnLockStepRes = string;

  /** /api/app/ems/execute/copy/recordItem */
  interface ExecuteCopyRecordItemReq {
    /** 批号 */
    batchNo: string;
    /** 复制版本号 */
    copyVersion: number;
    /** 工序步骤id */
    procedureStepId: number;
    /** 工艺id */
    processId: number;
    /** 工艺版本 */
    processVersion: string;
    /** 生产计划id */
    productPlanId: number;
    /** 记录项id */
    recordItemId: number;
    /** 记录项版本id */
    recordVersionId: number;
    /** 是否复用 */
    reuse: boolean;
  }

  /** 数据内容 */
  type ExecuteCopyRecordItemRes = number;

  /** /api/app/ems/execute/copyVersion/list */
  interface ExecuteCopyVersionListReq {
    /** 工序步骤id */
    procedureStepId: number;
    /** 生产计划id */
    productPlanId: number;
    /** 记录项id */
    recordItemId: number;
    /** 记录版本号 */
    recordVersionId: number;
    /** 是否复用 */
    reuse: string;
  }

  /** 数据内容 */
  interface ExecuteCopyVersionListRes {
    /** 是否已作废 */
    discard?: boolean;
    /** 版本号 */
    version?: number;
  }

  /** /api/app/ems/plan/info/audit/page */
  interface InfoAuditPageReq {
    /** 生产批号 */
    batchNo?: string;
    /** 页码 */
    current: number;
    /** */
    existsSearchCondition?: string;
    /** 计划编号 */
    planNo?: string;
    /** 生产工艺名称 */
    processName?: string;
    /** 产品名称 */
    productName?: string;
    /** 页数 */
    size: number;
    /** 计划类型 */
    type?: string;
  }

  /** 数据内容 */
  type InfoAuditPageRes = string;

  /** /api/app/ems/platform/query/list/dict/down */
  interface ListDictDownReq {
    /** dictId */
    dictId?: number;
  }

  /** 数据内容 */
  interface ListDictDownRes {
    /** id */
    id?: number;
    /** 标签 */
    label?: string;
    /** 值 */
    value?: string;
  }

  /** /api/app/ems/product/material/allProductTree */
  interface ProductMaterialAllProductTreeReq {
    /** types */
    types: number;
  }

  /** 数据内容 */
  type ProductMaterialAllProductTreeRes = ProductMaterialProductTreeRes;

  /** /api/app/ems/product/material/finishProductList */
  interface ProductMaterialFinishProductListReq {
    /** categoryType */
    categoryType: number;
  }

  /** 数据内容 */
  type ProductMaterialFinishProductListRes = ProductMaterialProductListRes;

  /** /api/app/ems/dataset/data/rename */
  interface DatasetDataRenameReq {
    /** 是否是数据点 */
    datapointFlag?: boolean;
    /** 数据集版本id */
    datasetVersionId?: number;
    /** id */
    id?: number;
    /** 新名称 */
    name?: string;
  }

  /** */
  type DatasetDataRenameRes = string;

  /** /api/app/ems/execute/relation/integrated/data */
  interface RelationIntegratedDataReq {
    /** productPlanId */
    productPlanId?: number;
  }

  /** 数据内容 */
  type RelationIntegratedDataRes = PermissionListDeptRes;

  /** /api/app/ems/audit/list/flow/audit/history */
  interface FlowAuditHistoryReq {
    /** processInstanceId */
    processInstanceId: string;
    /** deploymentId */
    deploymentId?: string;
  }

  /** 数据内容 */
  type FlowAuditHistoryRes = string;

  /** /api/app/ems/procedure/principal/users */
  interface ProcedurePrincipalUsersReq {
    /** 节点id */
    nodeId: string;
    /** 工艺id */
    processId: number;
    /** 工艺版本 */
    processVersion: string;
  }

  /** 数据内容 */
  type ProcedurePrincipalUsersRes = QueryUserListByRoleRes;

  /** /api/app/ems/procedure/step/listByProcess */
  interface ProcedureStepListByProcessReq {
    /** 工艺id */
    processId: number;
    /** 工艺版本 */
    processVersion?: string;
  }

  /** 数据内容 */
  interface ProcedureStepListByProcessRes {
    /** */
    id?: number;
    /** 工序步骤名称 */
    name?: string;
    /** 历史工序id */
    procedureId?: number;
    /** 工序模型id */
    procedureModelId?: number;
    /** 工序名称 */
    procedureName?: string;
    /** 历史工序步骤id */
    procedureStepId?: number;
    /** 记录项id */
    recordItemId?: number;
    /** 记录项版本id */
    recordVersionId?: number;
    /** 是否复用 */
    reusable?: boolean;
  }

  /** /api/app/ems/flow/complete/task */
  interface FlowCompleteTaskReq {
    /** 流程实例id */
    processInstanceId: string;
    /** 任务Id */
    taskId: string;
  }

  /** */
  type FlowCompleteTaskRes = string;
  /** */
  type FlowTerminateByProcessInstanceIdRes = string;

  /** /api/app/ems/signature/validate */
  interface MesSignatureValidateReq {
    /** 登录名称 */
    loginName: string;
    /** 密码 */
    password: string;
    /** 关联数据(保留字段，顺便传啥) */
    signatureData: string;
  }

  /** 数据内容 */
  type MesSignatureValidateRes = boolean;

  /** /api/app/ems/execute/discard */
  interface MesExecuteDiscardReq {
    /** 复制版本号 */
    copyVersion?: number;
    /** 工序步骤id */
    procedureStepId?: number;
    /** 生产计划id */
    productPlanId?: number;
    /** 记录项id */
    recordItemId?: number;
    /** 是否复用 */
    reuse?: boolean;
  }

  /** */
  type MesExecuteDiscardRes = string;

  /** /api/app/ems/dataset/datapoint/tree */
  interface DatasetDatapointTreeReq {
    /** datasetVersionId */
    datasetVersionId: number;
  }

  /** 数据内容 */
  type DatasetDatapointTreeRes = string;

  /** /api/app/ems/record/query/record/item */
  interface QueryRecordItemReq {
    /** recordItemId */
    recordItemId: number;
    /** recordVersionId */
    recordVersionId: number;
  }

  /** 数据内容 */
  type QueryRecordItemRes = string;

  /** 数据内容 */
  interface FlowAuditCategoryRes {
    /** 分类编码 */
    code?: string;
    /** id */
    id?: number;
    /** 下级集合 */
    itemList?: Array<FlowAuditCategoryResItemListVo>;
    /** 分类名称 */
    name?: string;
    /** 上级id */
    parentId?: number;
  }

  /** /api/app/ems/process/audit/todo/page */
  interface AuditTodoPageReq {
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 工艺名称 */
    processName?: string;
    /** 产品名称 */
    productName?: string;
  }

  /** 数据内容 */
  type AuditTodoPageRes = string;
  /** */
  type VersionAuditByIdRes = string;

  /** /api/app/ems/procedure/step/group/users */
  interface StepGroupUsersReq {
    /** 工序步骤节点id */
    nodeId: string;
    /** 生产计划id */
    productPlanId: number;
  }

  /** 数据内容 */
  type StepGroupUsersRes = QueryUserListByRoleRes;

  /** /api/app/ems/record/audit/page/record/audit */
  interface PageRecordAuditReq {
    /** 记录名称 */
    name?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
  }

  /** 数据内容 */
  type PageRecordAuditRes = string;

  /** /api/app/ems/record/audit/start/flow */
  interface AuditStartFlowReq {
    /** versionId */
    versionId?: number;
  }

  /** 数据内容 */
  type AuditStartFlowRes = boolean;

  /** /api/app/ems/audit/complete */
  interface MesAuditCompleteReq {
    /** 审批意见 */
    comment?: string;
    /** 实例id */
    processInstanceId?: string;
    /** 备注 */
    remark?: string;
    /** 任务id */
    taskId?: string;
  }

  /** 数据内容 */
  type MesAuditCompleteRes = boolean;

  /** /api/app/ems/audit/complete */
  interface MesAuditCompleteReq {
    /** 审批意见 */
    comment?: string;
    /** 实例id */
    processInstanceId?: string;
    /** 备注 */
    remark?: string;
    /** 任务id */
    taskId?: string;
  }

  /** 数据内容 */
  type MesAuditCompleteRes = boolean;

  /** /api/app/ems/audit/complete/not/approve */
  interface CompleteNotApproveReq {
    /** 审批意见 */
    comment?: string;
    /** 实例id */
    processInstanceId?: string;
    /** 备注 */
    remark?: string;
    /** 任务id */
    taskId?: string;
  }

  /** 数据内容 */
  type CompleteNotApproveRes = boolean;
  /** /api/app/ems/audit/complete/not/approve */
  type CompleteNotApproveReq = MesAuditCompleteReq;
  /** 数据内容 */
  type CompleteNotApproveRes = boolean;

  /** /api/app/ems/audit/export/audit/history */
  interface ExportAuditHistoryReq {
    /** 分类code */
    categoryCode: string;
    /** 结束时间 */
    endTime: string;
    /** 流程实例id集合 */
    instanceIdList?: array;
    /** 开始时间 */
    startTime: string;
  }

  /** */
  type ExportAuditHistoryRes = string;

  /** /api/app/ems/audit/export/task/history */
  interface ExportTaskHistoryReq {
    /** 分类编码 */
    categoryCode: string;
    /** 实例id */
    processInstanceId: string;
  }

  /** */
  type ExportTaskHistoryRes = string;

  /** /api/app/ems/audit/list/audit/history */
  interface ListAuditHistoryReq {
    /** 分类code */
    categoryCode: string;
    /** 结束时间 */
    endTime: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 开始时间 */
    startTime: string;
  }

  /** 数据内容 */
  type ListAuditHistoryRes = string;

  /** /api/app/ems/audit/list/task/history */
  interface ListTaskHistoryReq {
    /** processInstanceId */
    processInstanceId: string;
  }

  /** 数据内容 */
  interface ListTaskHistoryRes {
    /** 处理人 */
    assignee?: string;
    /** 处理人名称 */
    assigneeName?: string;
    /** 审批意见 */
    comment?: string;
    /** 审批不通过异常原因 */
    deleteReason?: string;
    /** 节点标识 */
    elementKey?: string;
    /** 节点名称 */
    elementName?: string;
    /** 结束时间 */
    endTime?: string;
    /** 备注 */
    remark?: string;
    /** 状态 */
    state?: number;
    /** 处理状态 */
    stateName?: string;
  }

  /** /api/app/ems/process/recursion/relation/processes */
  interface RecursionRelationProcessesReq {
    /** 工艺id */
    processId: number;
  }

  /** 数据内容 */
  type RecursionRelationProcessesRes = MesProcessListRes;
  /** */
  type MesReleaseBindTemplateVersionRes = string;

  /** /api/app/ems/release/file/export */
  interface ReleaseFileExportReq {
    /** id */
    id: number;
  }

  /** */
  type ReleaseFileExportRes = string;

  /** /api/app/ems/release/file/page */
  interface ReleaseFilePageReq {
    /** 生产批号 */
    batchNo?: string;
    /** 是否是分类 */
    categoryFlag?: string;
    /** 排序 */
    dir?: string;
    /** 结束日期 */
    endTime?: string;
    /** id */
    id?: number;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** */
    productIdList?: array;
    /** 生成人 */
    promoterName?: string;
    /** 开始日期 */
    startTime?: string;
  }

  /** 数据内容 */
  type ReleaseFilePageRes = string;

  /** /api/app/ems/release/template/clear */
  interface ReleaseTemplateClearReq {
    /** 模板版本id */
    templateVersionId?: number;
  }

  /** */
  type ReleaseTemplateClearRes = string;

  /** /api/app/ems/release/template/data */
  interface ReleaseTemplateDataReq {
    /** id */
    id: number;
  }

  /** /api/app/ems/release/template/dataset/bind */
  interface TemplateDatasetBindReq {
    /** 绑定列表 */
    list?: Array<TemplateDatasetBindReqListVo>;
    /** 模板版本id */
    templateVersionId?: number;
  }

  /** */
  type TemplateDatasetBindRes = string;

  /** /api/app/ems/release/template/edit */
  interface ReleaseTemplateEditReq {
    /** 打印区域 */
    borderRange?: string;
    /** 配置 */
    config: string;
    /** 标记哪些格子需要填充 */
    markData?: Array<ReleaseTemplateEditReqMarkDataVo>;
    /** sheet表格数据 */
    sheetData: string;
    /** 模板版本id */
    templateVersionId?: number;
  }

  /** */
  type ReleaseTemplateEditRes = string;

  /** /api/app/ems/release/template/page */
  interface ReleaseTemplatePageReq {
    /** 是否是分类 */
    categoryFlag?: string;
    /** 排序 */
    dir?: string;
    /** id */
    id: number;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** */
    productIdList?: array;
  }

  /** 数据内容 */
  type ReleaseTemplatePageRes = string;

  /** /api/app/ems/release/template/save */
  interface ReleaseTemplateSaveReq {
    /** 配置 */
    config: string;
    /** 模板名称 */
    name?: string;
    /** 工艺id */
    processId?: number;
    /** 工艺名称 */
    processName?: string;
    /** 关联成品id */
    productId?: number;
    /** 关联成品名称 */
    productName?: string;
    /** 备注 */
    remark?: string;
    /** sheet表格数据 */
    sheetData: string;
  }

  /** 数据内容 */
  type ReleaseTemplateSaveRes = number;

  /** /api/app/ems/release/template/sheet */
  interface ReleaseTemplateSheetReq {
    /** templateVersionId */
    templateVersionId: number;
  }

  /** */
  type VersionConfirmByVersionIdRes = string;

  /** /api/app/ems/release/template/version/detail */
  interface TemplateVersionDetailReq {
    /** id */
    id: number;
  }

  /** 数据内容 */
  type TemplateVersionDetailRes = string;

  /** /api/app/ems/release/template/version/list */
  interface TemplateVersionListReq {
    /** processId */
    processId: number;
  }

  /** 数据内容 */
  interface TemplateVersionListRes {
    /** id */
    id?: number;
    /** 模板名称 */
    templateName?: string;
    /** 版本号 */
    versionNumber?: number;
  }

  /** /api/app/ems/release/template/version/page */
  interface TemplateVersionPageReq {
    /** 排序 */
    dir?: string;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 模板id */
    templateId?: number;
  }

  /** 数据内容 */
  type TemplateVersionPageRes = string;

  /** /api/app/ems/release/template/version/upgrade */
  interface TemplateVersionUpgradeReq {
    /** 备注 */
    remark?: string;
    /** 可能导入的新模板sheet */
    sheetData?: string;
    /** 源版本id */
    sourceVersionId?: number;
  }

  /** 数据内容 */
  type TemplateVersionUpgradeRes = number;

  /** /api/app/ems/plan/info/pageTraceable */
  interface PlanInfoPageTraceableReq {
    /** 生产批号 */
    batchNo?: string;
    /** 排序 */
    dir?: string;
    /** 是否成品 */
    finishedProduct?: boolean;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** 生产工艺名称 */
    processName?: string;
    /** 产品分类id */
    productCategoryId?: number;
  }

  /** 数据内容 */
  type PlanInfoPageTraceableRes = string;

  /** /api/app/ems/release/generateBatchRelease */
  interface MesReleaseGenerateBatchReleaseReq {
    /** 文件上传路径 */
    fileUrl?: string;
    /** 批签发记录id */
    id?: number;
  }

  /** */
  type MesReleaseGenerateBatchReleaseRes = string;

  /** /api/app/ems/audit/list/make/user */
  interface ListMakeUserReq {
    /** nodeId */
    nodeId: string;
    /** deploymentId */
    deploymentId: string;
  }

  /** 数据内容 */
  type ListMakeUserRes = PermissionListDeptRes;

  /** /api/app/ems/dataset/datapoint/bind */
  interface DatasetDatapointBindReq {
    /** 组件id */
    fieldId?: number;
    /** 数据点id */
    id?: number;
    /** 工序步骤id */
    procedureStepId?: number;
    /** 记录项id */
    recordId?: number;
    /** 是否复用 */
    reused?: boolean;
  }

  /** 数据内容 */
  type DatasetDatapointBindRes = number;

  /** /api/app/ems/release/audit/submit */
  interface ReleaseAuditSubmitReq {
    /** 批签发记录id */
    id?: number;
  }

  /** */
  type ReleaseAuditSubmitRes = string;

  /** /api/app/ems/release/template/dataset/tree */
  interface TemplateDatasetTreeReq {
    /** 数据集类型 */
    datasetType: number;
    /** 模板版本id */
    templateVersionId: number;
  }

  /** 数据内容 */
  interface TemplateDatasetTreeRes {
    /** 子集 */
    children?: Array<TemplateDatasetTreeResChildrenVo>;
    /** 是否是数据点 */
    datapointFlag?: boolean;
    /** 数据集版本id */
    datasetVersionId?: number;
    /** fieldId */
    fieldId?: number;
    /** 节点id */
    id?: number;
    /** 名称 */
    name?: string;
    /** 父级id */
    parentId?: number;
    /** 关联工序步骤id */
    procedureStepId?: number;
    /** 关联工艺id */
    processId?: number;
    /** 记录项id */
    recordId?: number;
    /** 是否复用 */
    reused?: boolean;
  }

  /** /api/app/ems/release/page */
  interface MesReleasePageReq {
    /** */
    auditState?: number;
    /** 生产批号 */
    batchNo?: string;
    /** 是否是分类 */
    categoryFlag?: string;
    /** 排序 */
    dir?: string;
    /** 成品id */
    id?: number;
    /** 排序 */
    orderBy?: string;
    /** 页码，从 1 开始 */
    pageNum: number;
    /** 每页条数，最大值为 100 */
    pageSize: number;
    /** */
    productIdList?: array;
  }

  /** 数据内容 */
  type MesReleasePageRes = string;

  /** /api/app/ems/release/template/able */
  interface ReleaseTemplateAbleReq {
    /** 启停:启用,停用 */
    status?: boolean;
    /** 模板版本id */
    templateVersionId?: number;
  }

  /** */
  type ReleaseTemplateAbleRes = string;

  /** /api/app/ems/release/page/completed */
  interface ReleasePageCompletedReq {
    /** 产品批号 */
    batchNo?: string;
    /** */
    existsSearchCondition?: string;
    /** 页码 */
    pageNum: number;
    /** 页数 */
    pageSize: number;
    /** 成品名称 */
    productName?: string;
  }

  /** 数据内容 */
  type ReleasePageCompletedRes = string;

  /** /api/app/ems/release/page/history */
  interface ReleasePageHistoryReq {
    /** 产品批号 */
    batchNo?: string;
    /** */
    existsSearchCondition?: string;
    /** 页码 */
    pageNum: number;
    /** 页数 */
    pageSize: number;
    /** 成品名称 */
    productName?: string;
  }

  /** 数据内容 */
  type ReleasePageHistoryRes = string;

  /** /api/app/ems/release/page/todo */
  interface ReleasePageTodoReq {
    /** 产品批号 */
    batchNo?: string;
    /** */
    existsSearchCondition?: string;
    /** 页码 */
    pageNum: number;
    /** 页数 */
    pageSize: number;
    /** 成品名称 */
    productName?: string;
  }

  /** 数据内容 */
  type ReleasePageTodoRes = string;

  /** /api/app/ems/release/detail */
  interface MesReleaseDetailReq {
    /** 批签发记录id */
    id: number;
    /** 任务id */
    taskId: string;
  }

  /** 数据内容 */
  type MesReleaseDetailRes = string;

  /** /api/app/ems/audit/back/to/prev */
  interface BackToPrevReq {
    /** 回退原因 */
    comment?: string;
    /** 流程实例id */
    executionId?: string;
  }

  /** 数据内容 */
  type BackToPrevRes = boolean;

  /** /api/app/ems/release/process/list */
  interface ReleaseProcessListReq {
    /** 产品id */
    productId: number;
  }

  /** 数据内容 */
  type ReleaseProcessListRes = MesProcessListRes;

  /** /api/app/ems/execute/attachment/upload */
  interface ExecuteAttachmentUploadReq {
    /** 批号 */
    batchNo: string;
    /** 复制版本号 */
    copyVersion: number;
    /** 文件 */
    file: string;
    /** 工序步骤id */
    procedureStepId: number;
    /** 工艺id */
    processId: number;
    /** 工艺版本 */
    processVersion: string;
    /** 生产计划id */
    productPlanId: number;
    /** 记录项id */
    recordItemId: number;
    /** 是否复用 */
    reuse: boolean;
    /** 文件类型 */
    type: string;
  }

  /** */
  type ExecuteAttachmentUploadRes = string;

  /** /api/app/ems/execute/attachment/list */
  interface ExecuteAttachmentListReq {
    /** 复制版本号 */
    copyVersion: number;
    /** 工序步骤id */
    procedureStepId: number;
    /** 生产计划id */
    productPlanId: number;
    /** 记录项id */
    recordItemId: number;
    /** 是否复用 */
    reuse: string;
    /** 文件类型 */
    type: string;
  }

  /** 数据内容 */
  interface ExecuteAttachmentListRes {
    /** */
    id?: number;
    /** */
    path?: string;
  }

  /** /api/app/ems/product/material/finishProductTree */
  interface ProductMaterialFinishProductTreeReq {
    /** 分类信息类型 */
    categoryType: number;
    /** 是否成品 */
    isFinishedProduct: string;
  }

  /** 数据内容 */
  type ProductMaterialFinishProductTreeRes = ProductMaterialProductTreeRes;

  /** /api/app/ems/plan/relation/detail/{planId} */
  interface RelationDetailByPlanIdReq {
    /** planId */
    生产计划id: number;
    /** */
    planId: string;
  }

  /** 数据内容 */
  interface RelationDetailByPlanIdRes {
    /** 生产批号 */
    batchNo?: string;
    /** 工序id */
    processId?: number;
    /** 工序名称 */
    processName?: string;
    /** 生产工艺版本 */
    processVersion?: string;
    /** 生产计划id */
    productPlanId?: number;
  }

  /** /api/app/ems/execute/intact/merge/list */
  interface IntactMergeListReq {
    /** 工艺id */
    processId: number;
    /** 工艺版本号 */
    processVersion?: string;
    /** 生产计划id */
    productPlanId: number;
  }

  /** 数据内容 */
  interface IntactMergeListRes {
    /** 附件 */
    attachments?: Array<IntactMergeListResAttachmentsVo>;
    /** 复制版本 */
    copyVersion?: number;
    /** 数据 */
    dataList?: Array<IntactMergeListResDataListVo>;
    /** 文件内容 */
    fileContent?: string;
    /** 排序 */
    order?: number;
    /** 工序步骤id */
    procedureStepId?: number;
    /** 记录项id */
    recordItemId?: number;
    /** 记录项名称 */
    recordName?: string;
  }

  /** /api/app/ems/operation/history/save */
  interface OperationHistorySaveReq {
    /** 生产计划id */
    businessId: number;
    /** 操作类型: */
    type: string;
  }

  /** */
  type OperationHistorySaveRes = string;

  /** /api/app/ems/release/detail/history */
  interface ReleaseDetailHistoryReq {
    /** 批签发id */
    id?: number;
  }

  /** 数据内容 */
  type ReleaseDetailHistoryRes = string;
  /** RecordListCategoryResItemListVo */
  type RecordListCategoryResItemListVo = RecordListCategoryRes;

  /** RecordSaveComponentReqVo */
  interface RecordSaveComponentReqVo {
    /** 组件集合 */
    componentList?: Array<RecordSaveComponentReqVoComponentListVo>;
    /** html文件 */
    fileContent?: string;
    /** 记录id */
    id?: number;
    /** 记录项业务id */
    itemId?: number;
    /** 记录项最大下标 */
    maxNumber?: number;
    /** 记录项名称 */
    name?: string;
    /** 版本id */
    recordVersionId?: number;
    /** 记录项排序号 */
    sort?: number;
  }

  /** RecordSaveRecordReqItemsVo */
  interface RecordSaveRecordReqItemsVo {
    /** html文件内容 */
    fileContent?: string;
    /** 记录项类型 */
    itemType?: string;
    /** 记录项 */
    name?: string;
  }

  /** ProcedureDetailModifyReqProcedureStepsVo */
  interface ProcedureDetailModifyReqProcedureStepsVo {
    /** 区域 */
    areaList?: Array<number>;
    /** 设备类 */
    deviceTypeList?: Array<number>;
    /** 时长 */
    duration?: number;
    /** id */
    id?: number;
    /** 名称 */
    name: string;
    /** 工序功能 */
    nodeFunction?: string;
    /** 流程节点id */
    nodeId: string;
    /** 操作规程 */
    operationSop?: string;
    /** 工序步骤id */
    procedureStepId?: number;
    /** 记录项id */
    recordItemId?: number;
    /** 批记录版本id */
    recordVersionId?: number;
    /** 是否可复用 */
    reusable?: boolean;
    /** 执行岗 */
    roles?: Array<number>;
    /** 单位 */
    timeUnit?: string;
  }

  /** StepConfigSaveReqComponentsVo */
  interface StepConfigSaveReqComponentsVo {
    /** 组件id */
    componentId?: number;
    /** 配置信息JSON */
    configInfo?: string;
  }

  /** MesProcessModifyReqBatchRecordItemsVo */
  interface MesProcessModifyReqBatchRecordItemsVo {
    /** 批记录id */
    batchRecordId: number;
    /** 批记录版本号 */
    batchRecordVersion: string;
    /** 批记录版本id */
    batchRecordVersionId: number;
  }

  /** MesProcessModifyReqProceduresVo */
  interface MesProcessModifyReqProceduresVo {
    /** 时长 */
    duration?: number;
    /** 班组id集合 */
    groupIds?: Array<number>;
    /** id */
    id?: number;
    /** 名称 */
    name: string;
    /** 流程节点id */
    nodeId: string;
    /** 负责人 */
    principal?: number;
    /** 工序id */
    procedureId?: number;
    /** 流程模型id */
    processModelId?: string;
    /** 阶段编码 */
    stageCode?: string;
    /** 单位 */
    timeUnit?: string;
  }

  /** MesProcessModifyReqProcessRelationsVo */
  interface MesProcessModifyReqProcessRelationsVo {
    /** 关联物料id集合 */
    materialIds?: Array<number>;
    /** 关联工艺id */
    relationProcessId?: number;
  }

  /** MesProcessSaveReqBatchRecordItemsVo */
  type MesProcessSaveReqBatchRecordItemsVo = MesProcessModifyReqBatchRecordItemsVo;
  /** MesProcessSaveReqProceduresVo */
  type MesProcessSaveReqProceduresVo = MesProcessModifyReqProceduresVo;
  /** MesProcessSaveReqProcessRelationsVo */
  type MesProcessSaveReqProcessRelationsVo = MesProcessModifyReqProcessRelationsVo;
  /** ProcessVersionSaveReqBatchRecordItemsVo */
  type ProcessVersionSaveReqBatchRecordItemsVo = MesProcessModifyReqBatchRecordItemsVo;
  /** ProcessVersionSaveReqProceduresVo */
  type ProcessVersionSaveReqProceduresVo = MesProcessModifyReqProceduresVo;
  /** ProcessVersionSaveReqProcessRelationsVo */
  type ProcessVersionSaveReqProcessRelationsVo = MesProcessModifyReqProcessRelationsVo;
  /** ProcessVersionCopyReqBatchRecordItemsVo */
  type ProcessVersionCopyReqBatchRecordItemsVo = MesProcessModifyReqBatchRecordItemsVo;
  /** ProcessVersionCopyReqProceduresVo */
  type ProcessVersionCopyReqProceduresVo = MesProcessModifyReqProceduresVo;
  /** ProcessVersionCopyReqProcessRelationsVo */
  type ProcessVersionCopyReqProcessRelationsVo = MesProcessModifyReqProcessRelationsVo;

  /** ListRecordItemResComponentListVo */
  interface ListRecordItemResComponentListVo {
    /** 公式内容（存放单选多选字段） */
    componentDetail?: string;
    /** 组件名称 */
    componentName?: string;
    /** 组件关联表格最大下标值 */
    componentNumber?: number;
    /** 组件类型 */
    componentType?: string;
    /** 空格标识 */
    fieldId?: string;
    /** 公式详情VO */
    formulaDetailList?: Array<ListRecordItemResComponentListVoFormulaDetailListVo>;
    /** 公式表达式 */
    formulaExpression?: string;
    /** 公式实际参数字段JSON */
    formulaField?: string;
    /** 公式id */
    formulaId?: number;
    /** 精度 */
    formulaPrecision?: number;
    /** 公式类型 */
    formulaType?: string;
    /** 公式对象 */
    formulaVO?: string;
    /** 主键表id */
    id?: number;
    /** 标记该组件是否是一个计算结果（0否1是，默认0） */
    isResult?: number;
    /** 记录项id */
    recordItemId?: number;
    /** 修约公式code */
    roundCode?: string;
    /** 子组件 */
    children?: Array<ListRecordItemResComponentListVo>;
    /** node_type */
    node_type?: string;
    /** 图标 */
    icon?: string;
  }

  /** RecordSaveFormulaReqFormulaDetailListVo */
  interface RecordSaveFormulaReqFormulaDetailListVo {
    /** 记录描述 */
    describe?: string;
    /** 关联详情 */
    detail?: string;
    /** 关联组件id */
    fieldId?: number;
    /** 参数 */
    key?: string;
    /** 版本id */
    recordVersionId?: number;
    /** 参数简称 */
    value?: string;
  }

  /** MaterialCategoryTreeResChildrenVo */
  type MaterialCategoryTreeResChildrenVo = MaterialCategoryTreeRes;

  /** ProductMaterialIssueMaterialAndCategoryReqCategoryListVo */
  interface ProductMaterialIssueMaterialAndCategoryReqCategoryListVo {
    /** */
    categoryType?: number;
    /** */
    code?: string;
    /** */
    createBy?: string;
    /** */
    createTime?: string;
    /** */
    deleted?: boolean;
    /** */
    id?: number;
    /** */
    mergeCode?: string;
    /** */
    name?: string;
    /** */
    parentId?: number;
    /** */
    platformCategoryId?: number;
    /** */
    updateBy?: string;
    /** */
    updateTime?: string;
  }

  /** ProductMaterialIssueMaterialAndCategoryReqMaterialListVo */
  interface ProductMaterialIssueMaterialAndCategoryReqMaterialListVo {
    /** */
    categoryType?: number;
    /** */
    code?: string;
    /** */
    createBy?: string;
    /** */
    createTime?: string;
    /** */
    deleted?: boolean;
    /** */
    finishProduct?: boolean;
    /** */
    id?: number;
    /** 内包规格 */
    innerPackingSpecification?: string;
    /** 制造商 */
    manufacturer?: string;
    /** */
    materialCategoryId?: number;
    /** */
    mergeCode?: string;
    /** */
    name?: string;
    /** 包装规格 */
    packingSpecification?: string;
    /** */
    platformMaterialId?: number;
    /** */
    principalMaterialId?: number;
    /** 生产周期(天) */
    productionCycle?: number;
    /** */
    remark?: string;
    /** */
    specification?: string;
    /** */
    status?: boolean;
    /** */
    subMaterial?: boolean;
    /** 供应商 */
    supplier?: string;
    /** */
    unitExtendId?: number;
    /** */
    unitId?: number;
    /** */
    updateBy?: string;
    /** */
    updateTime?: string;
  }

  /** ProductMaterialSyncTreeResChildrenVo */
  type ProductMaterialSyncTreeResChildrenVo = ProductMaterialSyncTreeRes;
  /** ProductMaterialProductTreeResChildrenVo */
  type ProductMaterialProductTreeResChildrenVo = ProductMaterialProductTreeRes;
  /** ListRecordTreeResChildrenVo */
  type ListRecordTreeResChildrenVo = ListRecordTreeRes;

  /** ListRecordVersionResVersionListVo */
  interface ListRecordVersionResVersionListVo {
    /** 版本 */
    version?: string;
    /** 版本id */
    versionId?: number;
  }

  /** PermissionDeptTreeResChildrenVo */
  type PermissionDeptTreeResChildrenVo = PermissionDeptTreeRes;

  /** VersionSaveOrderReqRecordOrdersVo */
  interface VersionSaveOrderReqRecordOrdersVo {
    /** 记录项id */
    recordItemId?: number;
    /** 记录项顺序 */
    recordItemOrder?: number;
  }

  /** SaveFlowAuditReqAuditMegDTOListVo */
  interface SaveFlowAuditReqAuditMegDTOListVo {
    /** 消息类型 */
    messageType?: string;
    /** 节点id */
    nodeId?: string;
    /** 用户id */
    userId?: string;
  }

  /** SaveFlowAuditReqAuditUserListVo */
  interface SaveFlowAuditReqAuditUserListVo {
    /** 处理人 */
    assignee?: number;
    /** 处理人类型 */
    assigneeType?: string;
    /** 节点key */
    nodeId?: string;
  }

  /** PlanInfoBatchSaveReqDetailsVo */
  interface PlanInfoBatchSaveReqDetailsVo {
    /** 生产批号 */
    batchNo?: string;
    /** 计划编号 */
    planNo?: string;
    /** 生产时间 */
    productDate?: string;
    /** 计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次 */
    type?: string;
  }

  /** InstructionTeamConfirmReqDetailsVo */
  interface InstructionTeamConfirmReqDetailsVo {
    /** 生产工序步骤节点id */
    nodeStepId?: string;
    /** 历史工序id(以此判断多给版本的节点是否是同一工序) */
    procedureStepId?: number;
    /** 生产工序步骤id */
    procedureStepModelId?: number;
    /** 生产工序步骤名称 */
    procedureStepModelName?: string;
    /** 执行时长 */
    procedureStepTime?: number;
    /** 执行时长单位 */
    procedureStepTimeUnit?: string;
    /** 排序 */
    sort?: number;
    /** 班组id列表 */
    teamIds?: Array<number>;
  }

  /** AuditCheckoutDeploymentReqMegUserListVo */
  type AuditCheckoutDeploymentReqMegUserListVo = SaveFlowAuditReqAuditMegDTOListVo;
  /** AuditCheckoutDeploymentReqUserListVo */
  type AuditCheckoutDeploymentReqUserListVo = SaveFlowAuditReqAuditUserListVo;

  /** TeamStartConfirmReqRelationPlanVo */
  interface TeamStartConfirmReqRelationPlanVo {
    /** 生产计划批号列表 */
    batchNos?: Array<string>;
    /** 生产计划Id列表 */
    planIds?: Array<number>;
    /** 工艺id */
    processId?: number;
  }

  /** QueryDeptTreeResChildrenVo */
  type QueryDeptTreeResChildrenVo = QueryDeptTreeRes;
  /** DeptUserTreeResChildrenVo */
  type DeptUserTreeResChildrenVo = DeptUserTreeRes;
  /** ProcessProductTreeResChildrenVo */
  type ProcessProductTreeResChildrenVo = ProcessProductTreeRes;

  /** ExecuteBatchSaveReqItemsVo */
  interface ExecuteBatchSaveReqItemsVo {
    /** 组件类型 */
    componentType: string;
    /** 组件id */
    fieldId: number;
    /** 操作时间 */
    operationTime: string;
    /** 操作人 */
    operationUser: string;
    /** 备注 */
    remark?: string;
    /** 复核时间 */
    reviewTime?: string;
    /** 复核人 */
    reviewUser?: string;
    /** 数据值 */
    value: string;
    /** 数据值扩展（如checkbox的所有值） */
    valueExtension: string;
  }

  /** DatasetRelationListResVersionListVo */
  interface DatasetRelationListResVersionListVo {
    /** 是否已绑定 */
    bound?: boolean;
    /** 版本id */
    id?: number;
    /** 备注 */
    remark?: string;
    /** 版本号 */
    versionNumber?: number;
  }

  /** FlowAuditCategoryResItemListVo */
  type FlowAuditCategoryResItemListVo = FlowAuditCategoryRes;

  /** TemplateDatasetBindReqListVo */
  interface TemplateDatasetBindReqListVo {
    /** 数据集类型 */
    datasetType?: number;
    /** 数据集版本列表 */
    datasetVersionIdList?: Array<number>;
  }

  /** ReleaseTemplateEditReqMarkDataVo */
  interface ReleaseTemplateEditReqMarkDataVo {
    /** */
    c?: number;
    /** */
    f?: string;
    /** */
    r?: number;
  }

  /** TemplateDatasetTreeResChildrenVo */
  type TemplateDatasetTreeResChildrenVo = TemplateDatasetTreeRes;

  /** IntactMergeListResAttachmentsVo */
  interface IntactMergeListResAttachmentsVo {
    /** 组件类型 */
    componentType?: string;
    /** 字段id */
    fieldId?: number;
    /** 值 */
    value?: string;
    /** 值扩展 */
    valueExtension?: string;
  }

  /** IntactMergeListResDataListVo */
  type IntactMergeListResDataListVo = IntactMergeListResAttachmentsVo;

  /** RecordSaveComponentReqVoComponentListVo */
  interface RecordSaveComponentReqVoComponentListVo {
    /** 公式内容（存放单选多选字段） */
    componentDetail?: string;
    /** 组件名称 */
    componentName?: string;
    /** 组件关联表格最大下标值 */
    componentNumber?: number;
    /** 组件类型 */
    componentType?: string;
    /** 空格标识 */
    fieldId?: string;
    /** 公式表达式 */
    formulaExpression?: string;
    /** 公式实际参数字段JSON */
    formulaField?: string;
    /** 公式id */
    formulaId?: number;
    /** 精度 */
    formulaPrecision?: number;
    /** 公式类型 */
    formulaType?: string;
    /** 公式对象 */
    formulaVO?: string;
    /** 主键表id */
    id?: number;
    /** 标记该组件是否是一个计算结果（0否1是，默认0） */
    isResult?: number;
    /** 记录id */
    recordId?: number;
    /** 记录项id */
    recordItemId?: number;
    /** 版本号 */
    recordVersion?: string;
    /** 版本id */
    recordVersionId?: number;
    /** 修约公式code */
    roundCode?: string;
  }

  /** ListRecordItemResComponentListVoFormulaDetailListVo */
  interface ListRecordItemResComponentListVoFormulaDetailListVo {
    /** 记录描述 */
    describe?: string;
    /** 关联详情 */
    detail?: string;
    /** 关联组件id */
    fieldId?: string;
    /** 参数 */
    key?: string;
    /** 参数简称 */
    value?: string;
  }
}

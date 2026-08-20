export interface descriptionsDataType {
  productName: string; //产品名称
  productMergeCode: string; //产品编码
  productSpecification: string; //产品规格
  processName: string; //生产工艺
  productLineName: string; //产线
  productionBatch: string; //生产批量
  planNo: string; //计划编号
  batchNo: string; //生产批号
  productDate: string; //计划生产时间
  type: string; //指令单类型
}
export interface descriptionsDomType {
  label: string;
  key:
    | 'productName'
    | 'productMergeCode'
    | 'productSpecification'
    | 'processName'
    | 'productLineName'
    | 'productionBatch'
    | 'planNo'
    | 'batchNo'
    | 'productDate'
    | 'type';
}

export interface procedureListItemType {
  /**
   * 时长
   */
  duration?: number;
  /**
   * 班组id集合
   */
  groupIds?: number[];
  /**
   * id
   */
  id?: number;
  /**
   * 名称
   */
  name?: string;
  /**
   * 节点id
   */
  nodeId?: string;
  /**
   * 负责人
   */
  principal?: number;
  /**
   * 工序id
   */
  procedureId?: number;
  /**
   * 流程模型Id
   */
  processModelId?: string;
  /**
   * 阶段编码
   */
  stageCode?: string;
  /**
   * 单位
   */
  timeUnit?: string;
}

export interface CommonPagePlanPageVO {
  list?: PlanPageVO[];
  pageNum?: number;
  pageSize?: number;
  total?: number;
  totalPage?: number;
  [property: string]: any;
}

/**
 * PlanPageVO:生产计划分页VO
 */
export interface PlanPageVO {
  /**
   * 生产批号
   */
  batchNo?: string;
  /**
   * id
   */
  id?: number;
  /**
   * 状态 待分解WAIT_DECOMPOSE 待确认WAIT_CONFIRM 待下发WAIT_SEND 已下发 SEND
   */
  instructStatus?: string;
  /**
   * 计划编号
   */
  planNo?: string;
  /**
   * 生产工艺id
   */
  processId?: number;
  /**
   * 生产工艺名称
   */
  processName?: string;
  /**
   * 生产工艺版本
   */
  processVersion?: string;
  /**
   * 生产时间
   */
  productDate?: Date;
  /**
   * 产品Id
   */
  productId?: number;
  /**
   * 产品编码
   */
  productMergeCode?: string;
  /**
   * 产品名称
   */
  productName?: string;
  /**
   * 产品规格
   */
  productSpecification?: string;
  /**
   * 状态 编辑EDIT 审核中AUDIT 确认CONFIRM 废弃DISCARD
   */
  status?: {
    value: string;
    label: string;
  };
  /**
   * 指令单类型
   */
  type?: string;
  processModelId?: string;
  planDetailVO?: string;
}

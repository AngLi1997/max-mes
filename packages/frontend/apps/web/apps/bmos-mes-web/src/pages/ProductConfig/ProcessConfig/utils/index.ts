import { FlowNodeEnum } from '@/components/Flow/type';
import { Cell } from '@antv/x6';
import { isNullOrUnDef } from '@bmos/utils';
import { FlowNodeType } from '../enum';

/**
 * @description: 过滤空值
 * @param obj  对象
 * @returns  过滤后的对象
 */
export const filterEmpty = (obj: any) => {
  return Object.keys(obj)
    .filter(key => obj[key] !== null && obj[key] !== undefined && obj[key] !== '')
    .reduce((acc, key) => ({ ...acc, [key]: obj[key] }), {});
};

/**
 * @description: 根据节点类型获取节点类型
 * @param shape 节点类型
 * @returns 节点类型
 */
export const getFlowTypeByShape = (shape: FlowNodeEnum) => {
  const shapeMap = new Map([
    [FlowNodeEnum.START, FlowNodeType.START_EVENT],
    [FlowNodeEnum.END, FlowNodeType.END_EVENT],
    [FlowNodeEnum.EDGE, FlowNodeType.SEQUENCE_FLOW],
    [FlowNodeEnum.CUSTOM, FlowNodeType.CALL_ACTIVITY_TASK],
  ]);
  return shapeMap.get(shape);
};

/**
 * @description: 子任务根据节点类型获取节点类型
 * @param shape 节点类型
 * @returns 节点类型
 */
export const getSubFlowTypeByShape = (shape: FlowNodeEnum) => {
  const shapeMap = new Map([
    [FlowNodeEnum.START, FlowNodeType.START_EVENT],
    [FlowNodeEnum.END, FlowNodeType.END_EVENT],
    [FlowNodeEnum.EDGE, FlowNodeType.SEQUENCE_FLOW],
    [FlowNodeEnum.CUSTOM, FlowNodeType.USER_TASK],
  ]);
  return shapeMap.get(shape);
};

/**
 * @description: 获取流出节点 id 数组
 * @param cell 节点
 * @param edges 边集合
 * @returns 流出节点 id 数组
 */
export const getCellOutgoing = (cell: Cell.Properties, edges: Cell.Properties) => {
  const result: string[] = [];
  if (cell.shape === FlowNodeEnum.START || cell.shape === FlowNodeEnum.CUSTOM || cell.shape === FlowNodeEnum.GATEWAY) {
    const items = edges.filter((item: Cell.Properties) => item.source.cell === cell.id);
    items.forEach((item: Cell.Properties) => {
      result.push(item.id as string);
    });
  } else if (cell.shape === FlowNodeEnum.EDGE) {
    result.push(cell.target.cell);
  }
  return result;
};

/**
 * @description: 获取流入节点 id 数组
 * @param cell 节点
 * @param edges 边集合
 * @returns 流入节点 id 数组
 */
export const getCellIncoming = (cell: Cell.Properties, edges: Cell.Properties) => {
  const result: string[] = [];
  if (cell.shape === FlowNodeEnum.END || cell.shape === FlowNodeEnum.CUSTOM || cell.shape === FlowNodeEnum.GATEWAY) {
    const items = edges.filter((item: Cell.Properties) => item.target.cell === cell.id);
    items.forEach((item: Cell.Properties) => {
      result.push(item.id as string);
    });
  } else if (cell.shape === FlowNodeEnum.EDGE) {
    result.push(cell.source.cell);
  }
  return result;
};

/**
 * @description: 处理流程图数据
 * @param data 流程图数据
 * @returns 处理后的流程图数据
 */
export const processFlowData = (data: { cells: Cell.Properties[] }, modalJson?: any, isSub = false) => {
  const result: any = [];
  const { cells } = data;
  const edges = cells.filter(item => item.shape === 'edge');

  cells.forEach(item => {
    result.push({
      ...(modalJson &&
        modalJson.length && {
          ...modalJson.find((i: any) => i.key === item.id),
        }),
      key: item.id,
      name:
        item.shape === FlowNodeEnum.CUSTOM
          ? item.data?.formData?.name
            ? item.data?.formData?.name
            : item.data.name
          : item.shape,
      type: isSub ? getSubFlowTypeByShape(item.shape as FlowNodeEnum) : getFlowTypeByShape(item.shape as FlowNodeEnum),
      outgoing: getCellOutgoing(item, edges),
      incoming: getCellIncoming(item, edges),
      metaInfo: JSON.stringify(item),
      ...(item.shape === FlowNodeEnum.GATEWAY && {
        type: item.data?.gatewayType,
        conditionOnNodes: item.data?.conditionOnNodes,
      }),
    });
  });
  return result;
};

/**
 * @description: 处理流程图数据 data
 * @param data 流程图数据
 * @returns 处理后的流程图数据 data
 */
export const getProcedures = (data: { cells: Cell.Properties[] }) => {
  const result: any = [];
  const { cells } = data;
  const customCells = cells.filter(item => item.shape === FlowNodeEnum.CUSTOM);
  customCells.forEach(item => {
    const {
      id,
      procedureId,
      processModelId,
      name,
      stageCode,
      principal,
      groupIds,
      roomIdList,
      duration,
      timeUnit,
      formulaMaterialIdList,
      sort,
      label,
      completeCondition,
    } = item.data;
    result.push({
      ...(id && { id }),
      ...(procedureId && { procedureId }),
      ...(processModelId && {
        processModelId,
      }),
      nodeId: item.id,
      name,
      historicalName: item.data?.historicalName ? item.data?.historicalName : label,
      stageCode,
      principal,
      groupIds,
      roomIdList,
      duration,
      timeUnit,
      ...(completeCondition && { completeCondition }),
      formulaMaterialIdList,
      ...(!isNullOrUnDef(sort) && { sort }),
    });
  });
  return result;
};

export const getGatewaySelectNodes = (cells: Cell.Properties[], id: string) => {
  const result: any = [];
  cells.forEach((cell: Cell.Properties) => {
    if (cell.shape === 'edge' && cell.target?.cell === id) {
      const source = cells.find((i: Cell.Properties) => i.id === cell.source?.cell);
      result.push({
        label: source?.data?.label,
        value: source?.id,
      });
    }
  });
  return result;
};

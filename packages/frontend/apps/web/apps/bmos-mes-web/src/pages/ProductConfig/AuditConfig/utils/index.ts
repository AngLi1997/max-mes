import { Cell } from '@antv/x6';
import { FlowNodeEnum } from '@/components/Flow/type';
import { FlowNodeType } from '../enum';

/**
 * @description: 过滤空值
 * @param obj  对象
 * @returns  过滤后的对象
 */
export const filterEmpty = (obj: any) => {
  return Object.keys(obj)
    .filter(
      key => obj[key] !== null && obj[key] !== undefined && obj[key] !== '',
    )
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
export const getCellOutgoing = (
  cell: Cell.Properties,
  edges: Cell.Properties,
) => {
  const result: string[] = [];
  if (cell.shape === FlowNodeEnum.START || cell.shape === FlowNodeEnum.CUSTOM) {
    const items = edges.filter(
      (item: Cell.Properties) => item.source.cell === cell.id,
    );
    items.forEach((item: Cell.Properties) => {
      result.push(item.target.cell);
    });
  }
  return result;
};

/**
 * @description: 获取流入节点 id 数组
 * @param cell 节点
 * @param edges 边集合
 * @returns 流入节点 id 数组
 */
export const getCellIncoming = (
  cell: Cell.Properties,
  edges: Cell.Properties,
) => {
  const result: string[] = [];
  if (cell.shape === FlowNodeEnum.END || cell.shape === FlowNodeEnum.CUSTOM) {
    const items = edges.filter(
      (item: Cell.Properties) => item.target.cell === cell.id,
    );
    items.forEach((item: Cell.Properties) => {
      result.push(item.source.cell);
    });
  }
  return result;
};

/**
 * @description: 处理流程图数据
 * @param data 流程图数据
 * @returns 处理后的流程图数据
 */
export const processFlowData = (
  data: { cells: Cell.Properties[] },
  isSub = false,
) => {
  const result: any = [];
  const { cells } = data;
  const edges = cells.filter(item => item.shape === 'edge');

  cells.forEach(item => {
    result.push({
      key: item.id,
      name: item.shape === FlowNodeEnum.CUSTOM ? item.data.label : item.shape,
      type: isSub
        ? getSubFlowTypeByShape(item.shape as FlowNodeEnum)
        : getFlowTypeByShape(item.shape as FlowNodeEnum),
      outgoing: getCellOutgoing(item, edges),
      incoming: getCellIncoming(item, edges),
      metaInfo: item,
    });
  });
  return result;
};

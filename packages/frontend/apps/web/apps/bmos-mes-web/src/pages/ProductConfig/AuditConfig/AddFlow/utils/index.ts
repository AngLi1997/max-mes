import { FlowNodeEnum } from '@/components/Flow/type';
import { FlowNodeType } from '@/pages/ProductConfig/ProcessConfig/enum';
import { Cell } from '@antv/x6';
import { Recordable } from '@bmos/components';

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
 * @description: 子任务根据节点类型获取节点类型
 * @param shape 节点类型
 * @returns 节点类型
 */
export const getFlowTypeByShape = (shape: FlowNodeEnum) => {
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
  if (cell.shape === FlowNodeEnum.START || cell.shape === FlowNodeEnum.CUSTOM) {
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
  if (cell.shape === FlowNodeEnum.END || cell.shape === FlowNodeEnum.CUSTOM) {
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
export const dealFlowData = (data: { cells: Cell.Properties[] }, originalModalJson?: any) => {
  const result: any = [];
  const { cells } = data;
  const edges = cells.filter(item => item.shape === 'edge');
  cells.forEach(item => {
    result.push({
      ...(originalModalJson &&
        originalModalJson.length && {
          ...originalModalJson.find((i: any) => i.key === item.id),
        }),
      key: item.id,
      name: item.data?.label,
      code: item.data?.formData?.code,
      remark: item.data?.formData?.remark,
      type: getFlowTypeByShape(item.shape as FlowNodeEnum),
      outgoing: getCellOutgoing(item, edges),
      incoming: getCellIncoming(item, edges),
      metaInfo: item,
      ...(item.shape !== 'edge' && {
        payload: {
          settings: JSON.stringify({
            name: item.data?.label,
            ...(item.data?.formData || { ...item.data?.formData }),
            buttons: item.data?.formData?.buttons,
            completeType: item.data?.formData?.completeType,
            // 如果 item.data?.formData?.completeType 为 countersign 会签时
            ...(item.data?.formData?.completeType === 'countersign'
              ? {
                  strategy: item.data?.formData?.strategy,
                }
              : {
                  strategy: [],
                }),
            needCommit: item.data?.formData?.needCommit,
            needRemark: item.data?.formData?.needRemark,
            needPwdValidate: item.data?.formData?.needPwdValidate,
          }),
        },
      }),
    });
  });
  return result;
};

/**
 * @description: 处理人员选择和角色选择
 * @param data 流程图数据
 * @returns 处理后的人员选择和角色选择
 */
export const getAuditUserList = (data: { cells: Cell.Properties[] }) => {
  const result: any = [];
  const { cells } = data;
  const customCells = cells.filter(item => item.shape === FlowNodeEnum.CUSTOM);
  customCells.forEach(item => {
    if (item.data?.formData?.reviewPerson) {
      item.data?.formData?.reviewPerson.forEach((user: any) => {
        result.push({
          nodeId: item.id,
          assignee: user.id || user.value || user,
          assigneeType: 'all_user',
        });
      });
    }
    if (item.data?.formData?.reviewRole) {
      item.data?.formData?.reviewRole.forEach((role: any) => {
        result.push({
          nodeId: item.id,
          assignee: role,
          assigneeType: 'all_role',
        });
      });
    }
  });
  return result;
};

/**
 * @description: 处理消息通知人员和抄送人员
 * @param data 流程图数据
 * @returns 处理后的消息通知人员和抄送人员
 */
export const getAuditMegDTOList = (data: { cells: Cell.Properties[] }) => {
  const result: any = [];
  const { cells } = data;
  const customCells = cells.filter(item => item.shape === FlowNodeEnum.CUSTOM);
  customCells.forEach(item => {
    if (item.data?.formData?.makePerson) {
      item.data?.formData?.makePerson.forEach((user: any) => {
        result.push({
          nodeId: item.id,
          userId: user?.id || user.value || user,
          messageType: 'make',
        });
      });
    }
    if (item.data?.formData?.auditMegDTOList) {
      item.data?.formData?.auditMegDTOList.forEach((user: any) => {
        result.push({
          nodeId: item.id,
          userId: user?.id || user.value || user,
          messageType: 'message',
        });
      });
    }
  });
  return result;
};

/**
 * @description: 处理人员选择和角色选择 的id 以及其他参数
 * @param data 流程图数据
 * @returns 处理后的人员选择和角色选择
 */
export function dealAuditUserList(auditUserList: any[], flowDetail: Recordable): any[] {
  const result: any = [];
  auditUserList.forEach(item => {
    result.push({
      ...flowDetail?.auditUserList?.find((user: any) => user.assignee === item.assignee && user.nodeId === item.nodeId),
      ...item,
    });
  });
  return result;
}

/**
 * @description: 处理消息通知人员和抄送人员 的id 以及其他参数
 * @param data 流程图数据
 * @returns 处理后的消息通知人员和抄送人员
 */
export function dealAuditMegDTOList(auditMegDTOList: any[], flowDetail: Recordable): any[] {
  const result: any = [];
  auditMegDTOList.forEach(item => {
    result.push({
      ...flowDetail?.auditMegDTOList?.find((user: any) => user.userId === item.userId && user.nodeId === item.nodeId),
      ...item,
    });
  });
  return result;
}

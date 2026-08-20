import { FlowNodeEnum } from '@/components/Flow/type';
import { Cell } from '@antv/x6';
import { Recordable } from '@bmos/components';
import { DataNode } from 'ant-design-vue/es/tree';

export const getProcedureSteps = (data: { cells: Cell.Properties[] }) => {
  const result: any = [];
  const { cells } = data;
  const customCells = cells.filter(item => item.shape === FlowNodeEnum.CUSTOM);
  customCells.forEach(item => {
    result.push({
      ...(item.data?.id && { id: item.data.id }),
      ...item.data.formData,
      ...(item.data?.formData?.historicalName
        ? { historicalName: item.data.formData.historicalName }
        : { historicalName: item.data.formData?.label }),
      nodeId: item.id,
      duration: item.data?.formData?.duration,
      timeUnit: item.data?.formData?.timeUnit,
    });
  });
  return result;
};

// 一个树型数据data, 给定 key 如 0-0-0，返回节点和父节点的 title
// export const getLabelByKeyInTree = (data: DataNode[], key: string): string =>
export const getLabelByKeyInTree = (data: DataNode[], key: string): string => {
  let result = '';
  let node: DataNode | undefined;
  const loop = (list: DataNode[]) => {
    list.forEach(item => {
      if (item.key === key) {
        node = item;
        return;
      }
      if (item.children) {
        loop(item.children);
      }
    });
  };
  loop(data);
  if (node) {
    data.forEach((item: DataNode) => {
      if (item.key === node?.recordVersionId) {
        result = `${item.title}/${node?.title}`;
      }
    });
  }

  return result;
};

export const getNodeByKeyInTree = (data: DataNode[], key: string): Recordable => {
  let node: Recordable = {};
  const loop = (list: DataNode[]) => {
    list.forEach(item => {
      if (item.key === key) {
        node = item;
        return;
      }
      if (item.children) {
        loop(item.children);
      }
    });
  };
  loop(data);
  return node;
};

// 根据子节点id获取父节点id
export const getParentIdByNodeId = (data: DataNode[], nodeId: string): string => {
  let result = '';
  const loop = (list: DataNode[]) => {
    list.forEach(item => {
      if (item.key === nodeId) {
        result = item.recordVersionId;
        return;
      }
      if (item.children) {
        loop(item.children);
      }
    });
  };
  loop(data);
  return result;
};

// 根据子节点id获取 item 数据
export const getItemByNodeId = (data: DataNode[], nodeId: string): DataNode => {
  let result: DataNode = {} as DataNode;
  const loop = (list: DataNode[]) => {
    list.forEach(item => {
      if (item.key === nodeId) {
        result = item;
        return;
      }
      if (item.children) {
        loop(item.children);
      }
    });
  };
  loop(data);
  return result;
};

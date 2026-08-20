import { Cell, Edge, Graph, Node } from '@antv/x6';
import { Recordable } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { Ref } from 'vue';
import { FlowEmitFn, FlowNodeEnum } from '../type';
import { FlowState } from './useFlowState';

export type FlowMethods = ReturnType<typeof useFlowMethods>;

export type useFlowMethodsParams = FlowState & {
  emit: FlowEmitFn;
  graph: Ref<Graph>;
};

export const useFlowMethods = (flowMethodsContext: useFlowMethodsParams) => {
  const { graph, defaultNodeConfig, defaultPortConfig, emit } = flowMethodsContext;

  /**
   * @description: 显示端口
   * @param {NodeListOf<HTMLElement>} ports
   * @param {boolean} show
   */
  const showPorts = (ports: NodeListOf<HTMLElement>, show: boolean) => {
    for (let i = 0, len = ports.length; i < len; i = i + 1) {
      const port = ports[i];
      port.style.visibility = show ? 'visible' : 'hidden';
    }
  };

  /**
   * @description: 初始化画布
   * @param {(Node.Metadata | Edge.Metadata)[]} cells
   */
  const initGraph = (cells?: (Node.Metadata | Edge.Metadata)[]) => {
    if (cells) {
      graph.value?.fromJSON(cells);
    } else {
      graph.value?.fromJSON({
        nodes: [
          {
            ...defaultNodeConfig.value,
            shape: 'custom-vue-start-node',
            width: 120,
            height: 44,
            x: 500,
            y: 60,
            ports: {
              ...defaultPortConfig.value,
              items: [
                {
                  id: 'start-top-port',
                  group: 'top',
                },
                {
                  id: 'start-right-port',
                  group: 'right',
                },
                {
                  id: 'start-left-port',
                  group: 'left',
                },
                {
                  id: 'start-bottom-port',
                  group: 'bottom',
                },
              ],
            },
            data: {
              label: t('开始'),
            },
          },
          {
            ...defaultNodeConfig.value,
            shape: 'custom-vue-end-node',
            width: 120,
            height: 44,
            x: 500,
            y: 500,
            ports: {
              ...defaultPortConfig.value,
              items: [
                {
                  id: 'end-top-port',
                  group: 'top',
                },
                {
                  id: 'end-right-port',
                  group: 'right',
                },
                {
                  id: 'end-left-port',
                  group: 'left',
                },
                {
                  id: 'end-bottom-port',
                  group: 'bottom',
                },
              ],
            },
            data: {
              label: t('结束'),
            },
          },
        ],
      });
    }
  };

  /**
   * @description: 拖拽开始
   * @param {DragEvent} e
   */
  const allowDrop = (e: DragEvent) => {
    e.preventDefault();
  };

  /**
   * @description: 拖拽结束 添加节点
   * @param {DragEvent} e
   */
  const drop = (e: DragEvent) => {
    try {
      e.preventDefault();
      const data = e.dataTransfer?.getData('data');
      if (data) {
        const jsonData = JSON.parse(data);
        const shape = jsonData.shape || FlowNodeEnum.CUSTOM;
        const { width, height } = jsonData;
        const { offsetX, offsetY } = e;
        graph.value?.addNode({
          ...defaultNodeConfig.value,
          shape: shape,
          x: offsetX,
          y: offsetY,
          width: width || 206,
          height: height || 44,
          data: {
            ...jsonData,
          },
        });
      }
    } catch (error) {
      message.error(t('添加节点失败'));
    }
  };

  /**
   * @description: 重做
   */
  const undo = () => {
    graph.value?.canUndo() && graph.value?.undo();
  };

  /**
   * @description: 撤销
   */
  const redo = () => {
    graph.value?.canRedo() && graph.value?.redo();
  };

  /**
   * @description: 初始大小
   */
  const reset = () => {
    graph.value?.zoomTo(1);
    // 居中
    graph.value?.centerContent();
  };

  /**
   * @description: 放大 0.2
   */
  const zoomIn = () => {
    graph.value?.zoom(0.2);
  };

  /**
   * @description: 缩小 0.2
   */
  const zoomOut = () => {
    graph.value?.zoom(-0.2);
  };

  /**
   * @description: 根据 id 框选节点
   * @param {string} id 节点 id
  */
  const selectNodeById = (id: string) => {
    const cell = graph.value?.getCellById(id)
    if (cell) {
      graph.value?.resetSelection(cell);
    }
  };

  /**
   * @description: 删除选中节点
   */
  const deleteNode = () => {
    if (isNotStartOrEndCell(graph.value?.getSelectedCells())) {
      graph.value?.removeCells(graph.value?.getSelectedCells());
    } else {
      message.warning(t('开始或结束节点不能删除'));
    }
  };

  /**
   * @description: 是否为自定义节点
   * @param {Cell.Cell[]} cells
   * @return {boolean} true: 是自定义节点 false: 不是自定义节点
   */
  const isCustomNode = (cells: Cell<Cell.Properties>[]) => {
    if (
      cells.length &&
      cells[0].isNode() &&
      cells[0].shape !== FlowNodeEnum.START &&
      cells[0].shape !== FlowNodeEnum.END
    ) {
      return true;
    } else {
      return false;
    }
  };

  /**
   * @description: 是否为自定义节点
   * @param {Cell.Cell[]} cells
   * @return {boolean} true: 不是开始或结束节点 false: 是开始或结束节点
   */
  const isNotStartOrEndCell = (cells: Cell<Cell.Properties>[]) => {
    if (
      cells.length &&
      cells[0].shape !== FlowNodeEnum.START &&
      cells[0].shape !== FlowNodeEnum.END
    ) {
      return true;
    } else {
      return false;
    }
  };

  /**
   * @description: 是否为网关节点
   * @param {Cell.Cell[]} cells
   * @return {boolean} true: 是网关节点 false: 不是网关节点
   */
  const isNotGateway = (cells: Cell<Cell.Properties>[]) => {
    if (
      cells.length &&
      cells[0].shape !== FlowNodeEnum.GATEWAY
    ) {
      return true;
    } else {
      return false;
    }
  };

  /**
   * @description: 获取画布数据
   * @param {boolean} needTransformData 是否需要转换数据 默认为 true
   * @return {Graph.GraphData} 画布数据
   */
  const getFlowData = (needTransformData = true) => {
    try {
      const data = graph.value.toJSON();
      return data;
    } catch (error) {
      return {};
    }
  };

  /**
   * @description: 点击设置
   * @param {Cell} cell 节点
   */
  const handleClickSet = (cell: Cell) => {
    emit('handleClickSet', cell);
  };

  /**
   * @description: 点击下一步
   * @param {Cell} cell 节点
   */
  const handleClickNext = (cell: Cell) => {
    emit('handleClickNext', cell);
  };

  /**
   * @description: 更新节点表单数据
   * @param {string} settingNodeId 节点 id
   * @param {Recordable} formValue 表单数据
   */
  const updateFormValue = (settingNodeId: string, formValue: Recordable) => {
    emit('flowDataChange');
    graph.value?.getCellById(settingNodeId)?.setData(
      {
        ...formValue,
      },
      {
        overwrite: true,
        deep: true,
      },
    );
  };
    /**
   * @description: 更新节点数据
   * @param {string} settingNodeId 节点 id
   * @param {Recordable} formValue 表单数据
   */
    const updateCellDataValue = (settingNodeId: string, formValue: Recordable) => {
      emit('flowDataChange');
      graph.value?.getCellById(settingNodeId)?.setData(
        {
          ...formValue,
        },
        {
          overwrite: true,
          deep: true,
        },
      );
    };

  /**
   * @description: 更新节点表单数据
   * @param {string} settingNodeId 节点 id
   * @param {Recordable} formValue 表单数据
   */
  const updateCellData = (
    settingNodeId: string,
    formValue: Recordable,
    key = 'formData',
  ) => {
    emit('flowDataChange');
    const cell = graph.value?.getCellById(settingNodeId);
    const obj: Recordable = {};
    obj[key] = formValue;
    cell?.setData(
      {
        ...obj,
        ...(formValue.name && { label: formValue.name }),
      },
      {
        overwrite: true,
        deep: true,
      },
    );
  };


  return {
    showPorts,
    allowDrop,
    drop,
    undo,
    redo,
    reset,
    zoomIn,
    zoomOut,
    deleteNode,
    initGraph,
    isCustomNode,
    isNotStartOrEndCell,
    isNotGateway,
    getFlowData,
    handleClickSet,
    handleClickNext,
    updateFormValue,
    updateCellData,
    updateCellDataValue,
    selectNodeById,
  };
};

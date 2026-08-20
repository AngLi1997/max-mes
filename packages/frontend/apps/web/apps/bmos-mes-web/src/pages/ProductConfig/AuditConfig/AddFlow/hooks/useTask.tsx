import { Cell, Graph, Options, Shape } from '@antv/x6';
import { Recordable } from '@bmos/components';

export type UseTaskParams = {};

export const useTask = (useTaskContext: UseTaskParams) => {
  const {} = useTaskContext;

  // 任务右侧抽屉是否打开
  const taskRightDrawerOpen = ref<boolean>(false);
  const settingNodeId = ref<string>('');
  const settingNodeFormData = ref<Recordable>({});

  /**
   * 点击节点设置
   * @param {Cell} cell 点击的节点
   */
  const handleClickSet = (cell: Cell) => {
    taskRightDrawerOpen.value = true;
    settingNodeId.value = cell.id;
    settingNodeFormData.value = cell.data?.formData || {
      name: cell.data.label,
    };
  };

  const connecting = {
    snap: true,
    allowBlank: false,
    allowMulti: true,
    allowLoop: false,
    highlight: true,
    connector: {
      name: 'rounded',
      args: {
        radius: 10,
      },
    },
    connectionPoint: 'anchor',
    router: {
      name: 'manhattan',
    },
    createEdge() {
      return new Shape.Edge({
        attrs: {
          line: {
            stroke: '#999999',
            strokeWidth: 2,
            targetMarker: {
              name: 'block',
              width: 12,
              height: 8,
            },
          },
        },
        zIndex: 0,
      });
    },
    validateConnection(
      this: Graph,
      {
        sourceView,
        targetView,
        sourceMagnet,
        targetMagnet,
        sourceCell,
        targetCell,
      }: Options.ValidateConnectionArgs,
    ) {
      // 只能从输出链接桩创建连接桩
      if (!sourceMagnet) {
        return false;
      }
      // 只能连接到输入链接桩
      if (!targetMagnet) {
        return false;
      }
      // 判断是否为同一个节点
      if (sourceView === targetView) {
        return false;
      }
      // 判断目标链接桩是否开始节点
      if (targetMagnet.getAttribute('port') === 'start-port') {
        return false;
      }
      if (sourceMagnet.getAttribute('port') === 'end-port') {
        return false;
      }

      // 判断目标链接桩是否开始节点
      if (
        targetMagnet.getAttribute('port') === 'start-top-port' ||
        targetMagnet.getAttribute('port') === 'start-bottom-port' ||
        targetMagnet.getAttribute('port') === 'start-left-port' ||
        targetMagnet.getAttribute('port') === 'start-right-port'
      ) {
        return false;
      }

      // 判断源链接桩是否结束节点
      if (
        sourceMagnet.getAttribute('port') === 'end-top-port' ||
        sourceMagnet.getAttribute('port') === 'end-bottom-port' ||
        sourceMagnet.getAttribute('port') === 'end-left-port' ||
        sourceMagnet.getAttribute('port') === 'end-right-port'
      ) {
        return false;
      }

      // 判断开始节点只能有一个出口
      if (
        sourceMagnet.getAttribute('port') === 'start-top-port' ||
        sourceMagnet.getAttribute('port') === 'start-bottom-port' ||
        sourceMagnet.getAttribute('port') === 'start-left-port' ||
        sourceMagnet.getAttribute('port') === 'start-right-port'
      ) {
        const outEdges = this.getOutgoingEdges(sourceCell as Cell);
        if (outEdges && outEdges.length > 1) {
          return false;
        }
      }

      // 判断结束节点只能有一个入口
      if (
        targetMagnet.getAttribute('port') === 'end-top-port' ||
        targetMagnet.getAttribute('port') === 'end-bottom-port' ||
        targetMagnet.getAttribute('port') === 'end-left-port' ||
        targetMagnet.getAttribute('port') === 'end-right-port'
      ) {
        const inEdges = this.getIncomingEdges(targetCell as Cell);
        if (inEdges && inEdges.length > 0) {
          return false;
        }
      }

      // 判断任务节点只能有一个入口和一个出口
      if (
        sourceMagnet.getAttribute('port') === 'port1' ||
        sourceMagnet.getAttribute('port') === 'port2' ||
        sourceMagnet.getAttribute('port') === 'port3' ||
        sourceMagnet.getAttribute('port') === 'port4'
      ) {
        const outEdges = this.getOutgoingEdges(sourceCell as Cell);
        if (outEdges && outEdges.length > 1) {
          return false;
        }
      }
      if (
        targetMagnet.getAttribute('port') === 'port1' ||
        targetMagnet.getAttribute('port') === 'port2' ||
        targetMagnet.getAttribute('port') === 'port3' ||
        targetMagnet.getAttribute('port') === 'port4'
      ) {
        const inEdges = this.getIncomingEdges(targetCell as Cell);
        if (inEdges && inEdges.length > 0) {
          return false;
        }
      }
      return true;
    },
  };

  return {
    handleClickSet,
    taskRightDrawerOpen,
    settingNodeId,
    settingNodeFormData,
    connecting,
  };
};

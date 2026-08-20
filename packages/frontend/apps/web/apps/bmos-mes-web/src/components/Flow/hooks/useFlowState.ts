import { Graph, Options, Shape } from '@antv/x6';
import { t } from '@bmos/i18n';
import { SetupContext, computed, ref, watch } from 'vue';
import { FlowProps } from '../type';

export type FlowState = ReturnType<typeof useFlowState>;

export type useFlowStateParams = {
  props: FlowProps;
  attrs: SetupContext['attrs'];
};

export const useFlowState = ({ props, attrs }: useFlowStateParams) => {
  const leftToolRef = ref<any>(null);
  const dnd = ref<any>(null);

  const getFlowProps = computed(() => {
    return {
      ...attrs,
      ...props,
    } as FlowProps;
  });

  // 基础 画布 配置
  const defaultGraphConfig = ref<Partial<Graph.Options>>({
    autoResize: true,
    grid: {
      size: 10,
      visible: true,
      type: 'dot',
      args: [
        {
          color: '#E9EAED',
          thickness: 2,
        },
        {
          color: '#E9EAED',
          thickness: 2,
        },
      ],
    },
    background: {
      color: '#F5F6F7',
    },
    connecting: {
      snap: true,
      allowBlank: false,
      allowMulti: true,
      allowLoop: false,
      highlight: true,
      connector: {
        name: 'rounded',
        args: {
          radius: 8,
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
        { sourceView, targetView, sourceMagnet, targetMagnet }: Options.ValidateConnectionArgs,
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

        if (
          targetMagnet.getAttribute('port') === 'start-top-port' ||
          targetMagnet.getAttribute('port') === 'start-bottom-port' ||
          targetMagnet.getAttribute('port') === 'start-left-port' ||
          targetMagnet.getAttribute('port') === 'start-right-port'
        ) {
          return false;
        }
        if (
          sourceMagnet.getAttribute('port') === 'end-top-port' ||
          sourceMagnet.getAttribute('port') === 'end-bottom-port' ||
          sourceMagnet.getAttribute('port') === 'end-left-port' ||
          sourceMagnet.getAttribute('port') === 'end-right-port'
        ) {
          return false;
        }
        return true;
      },
    },
    highlighting: {
      magnetAvailable: {
        name: 'stroke',
        args: {
          attrs: {
            fill: '#fff',
            stroke: '#47C769',
          },
        },
      },
    },
    panning: true,
    mousewheel: {
      enabled: true,
      minScale: 0.2,
      maxScale: 4,
    },
    translating: {
      restrict: true,
    },
  });

  // 基础 port 配置
  const defaultPortConfig = ref({
    groups: {
      top: {
        position: 'top',
        attrs: {
          circle: {
            r: 6,
            magnet: true,
            stroke: '#108ee9',
            strokeWidth: 1,
            fill: 'transparent',
            style: {
              visibility: 'hidden',
            },
          },
        },
      },
      right: {
        position: 'right',
        attrs: {
          circle: {
            r: 6,
            magnet: true,
            stroke: '#108ee9',
            strokeWidth: 1,
            fill: 'transparent',
            style: {
              visibility: 'hidden',
            },
          },
        },
      },
      bottom: {
        position: 'bottom',
        attrs: {
          circle: {
            r: 6,
            magnet: true,
            stroke: '#108ee9',
            strokeWidth: 1,
            fill: 'transparent',
            style: {
              visibility: 'hidden',
            },
          },
        },
      },
      left: {
        position: 'left',
        attrs: {
          circle: {
            r: 6,
            magnet: true,
            stroke: '#108ee9',
            strokeWidth: 1,
            fill: 'transparent',
            style: {
              visibility: 'hidden',
            },
          },
        },
      },
    },
    items: [
      {
        id: 'port1',
        group: 'top',
      },
      {
        id: 'port2',
        group: 'right',
      },
      {
        id: 'port3',
        group: 'bottom',
      },
      {
        id: 'port4',
        group: 'left',
      },
    ],
  });

  // 基础 node 配置
  const defaultNodeConfig = ref({
    shape: 'custom-vue-node',
    x: 100,
    y: 60,
    width: 206,
    height: 44,
    ports: defaultPortConfig.value,
    data: {
      label: t('工序节点'),
    },
  });

  // 监听 modalJson 的变化
  const fromJSON = ref<any>(null);
  const isView = computed(() => props.isView);
  watch(
    () => props.modalJson,
    newVal => {
      if (newVal && newVal.length > 0) {
        fromJSON.value = newVal;
      }
    },
    { immediate: true, deep: true },
  );

  return {
    getFlowProps,
    defaultGraphConfig,
    defaultPortConfig,
    defaultNodeConfig,
    fromJSON,
    isView,
    leftToolRef,
    dnd,
  };
};

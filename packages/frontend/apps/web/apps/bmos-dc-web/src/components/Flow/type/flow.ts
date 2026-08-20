import type { Cell } from '@antv/x6';
import { isObject } from '@bmos/utils';
import type { EmitsOptions, ExtractPropTypes, SetupContext } from 'vue';
import flow from '../flow.vue';
import { FlowLeftToolBar } from './toolBar';
import { BMIconComponentMapType } from '@bmos/components';

export const flowProps = {
  modalJson: {
    type: Array,
    default: () => [],
  },
  isView: {
    type: Boolean,
    default: false,
  },
  nextIcon: {
    type: String as PropType<BMIconComponentMapType>,
    default: 'Process2',
  },
  // 是否显示下一步按钮
  showNextIcon: {
    type: Boolean,
    default: true,
  },
  // 是否显示设置按钮
  showSetIcon: {
    type: Boolean,
    default: true,
  },
  // 是否显示分割线
  showDivider: {
    type: Boolean,
    default: true,
  },
  // 是否限制开始结束节点选中等操作
  notLimitStartOrEndTransform: {
    type: Boolean,
    default: false,
  },
  leftIcon: {
    type: String as PropType<BMIconComponentMapType>,
    default: 'Procedure',
  },
  leftMap: {
    type: Array as PropType<FlowLeftToolBar[]>,
    default: () => [],
  },
  // 是否显示左侧工具栏
  isShowLeftToolBar: {
    type: Boolean,
    default: true,
  },
  // 是否显示上方工具栏
  isShowTopToolBar: {
    type: Boolean,
    default: true,
  },
  // 是否允许缩放节点
  isTransform: {
    type: Boolean,
    default: true,
  },
  // 是否鼠标移入节点方法
  mouseenter: {
    type: Function,
  },
  // 是否显示撤回
  showUndo: {
    type: Boolean,
    default: true,
  },
  // 是否显示重做
  showRedo: {
    type: Boolean,
    default: true,
  },
  // 是否显示放大
  showZoomIn: {
    type: Boolean,
    default: true,
  },
  // 是否显示缩小
  showZoomOut: {
    type: Boolean,
    default: true,
  },
  // 是否显示适应画布
  showReset: {
    type: Boolean,
    default: true,
  },
  // 是否显示删除
  showDelete: {
    type: Boolean,
    default: true,
  },
  // 是否控制可点击节点
  isOptionClickNode: {
    type: Boolean,
    default: false,
  },
  // 自定义 connecting 配置
  connecting: {
    type: Object,
    default: () => {},
  }
};

export type Recordable<T = any> = Record<string, T>;

export type Key = string | number;

export type EmitFn<E = EmitsOptions> = SetupContext<E>['emit'];

export const flowEmits = {
  reset: (formModel: Recordable<any>) => isObject(formModel),
  handleClickSet: (cell: Cell) => true,
  handleClickNext: (cell: Cell) => true,
  flowDataChange: () => true,
  nodeClick: (cell: Cell) => true,
  graphRender: (graph: any) => true,
};

export type FlowEmits = typeof flowEmits;

export type FlowEmitFn = EmitFn<FlowEmits>;

export type FlowProps = Partial<ExtractPropTypes<typeof flowProps>>;

export type FlowInstanceType = InstanceType<typeof flow>;

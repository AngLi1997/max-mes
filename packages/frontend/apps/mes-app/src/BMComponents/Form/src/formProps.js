export const formProps = {
  resetOnChange: {
    type: Boolean,
    default: true,
  },
  dynamicRules: {
    type: Object,
    default: () => ({}),
  },
  /** 预置字段默认值 */
  initialValues: {
    type: Object,
    default: () => ({}),
  },
  // 标签宽度  固定宽度
  labelWidth: {
    type: [Number, String],
    default: 0,
  },
  /** 表单配置规则 */
  schemas: {
    type: [Array],
    default: () => [],
  },
  // 默认数据，合并进 formData
  mergeDynamicData: {
    type: Object,
    default: null,
  },
  // 默认 row style
  baseRowStyle: {
    type: Object,
    default: () => ({}),
  },
  /** 栅栏Row配置 */
  rowProps: {
    type: Object,
    default: () => ({}),
  },
  // 默认 col style
  baseColProps: {
    type: Object,
    default: () => ({
      span: 12,
    }),
  },
  /** 禁用表单 */
  disabled: { type: Boolean, default: false },
  /** 是否把 label 添加进校验信息 */
  rulesMessageJoinLabel: { type: Boolean, default: true },

  /** 自定义重置函数 */
  resetFunc: {
    type: Function,
    default: () => () => {},
  },
  /** 自定义提交函数 */
  submitFunc: {
    type: Function,
    default: null,
  },
  transformValueFunc: {
    type: Function,
    default: null,
  },
};

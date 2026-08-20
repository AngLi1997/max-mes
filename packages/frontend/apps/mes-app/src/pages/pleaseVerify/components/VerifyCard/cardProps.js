export const STATUS_MAP = { // 卡片状态
  1: '检验中',
  2: '已完成',
  3: '已撤回',
};

export const formProps = {
  title: { // 标题
    type: String,
    default: '',
  },
  status: { // 卡片状态
    type: Object,
    default: () => ({}),
  },
  planId: {
    type: [Number, String],
    default: '',
  },
  procedureModelId: {
    type: [Number, String],
    default: '',
  },
  cardData: { // 卡片数据
    type: Object,
    default: () => ({}),
  },
};

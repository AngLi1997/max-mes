import { PropType } from 'vue';

type TagItem = {
  key: string;
  value: string;
  className: string;
};

type CardItem = {
  title: string;
  content: string;
  slot?: any;
};

export const viewProps = {
  title: {
    // 大标题
    type: String,
    default: '',
  },
  showHeader: {
    // 是否显示头部
    type: Boolean,
    default: true,
  },
  headerTitle: {
    // 头部标题
    type: String,
    default: '检验单编码',
  },
  tagItem: {
    // 头部标签
    type: Object as PropType<TagItem>,
    default: () => {},
  },
  headerExtra: {
    // 头部额外内容
    type: String,
    default: '',
  },
  showStep: {
    // 是否显示步骤条
    type: Boolean,
    default: true,
  },
  stepItems: {
    // 步骤条内容
    type: Array<any>,
    default: () => [],
  },
  cardItems: {
    // 卡片内容
    type: Array<CardItem>,
    default: () => [],
  },
};

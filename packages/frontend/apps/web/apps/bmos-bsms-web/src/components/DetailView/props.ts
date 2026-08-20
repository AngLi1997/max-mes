import { PropType } from 'vue';
import { t } from '@bmos/i18n';

type TagItem = {
  key: string;
  value: string | number;
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
  showBreadcrumb: {
    // 是否显示面包屑
    type: Boolean,
    default: true,
  },
  showHeader: {
    // 是否显示头部
    type: Boolean,
    default: true,
  },
  headerTitle: {
    // 头部标题
    type: String,
    default: t('检验单编码'),
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
  menuItems: {
    // 菜单内容
    type: Array<any>,
    default: () => [],
  },
};

import { tabsProps } from 'ant-design-vue/es/tabs/src/Tabs';
import { TabPane } from '../type';
export const tabProps = {
  ...tabsProps(),
  tabList: {
    type: Array as PropType<TabPane[]>,
    default: () => [],
  },
};

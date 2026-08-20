import TabPage from './TabComponent.vue'
import type { DefineComponent, ExtractPropTypes } from 'vue'
import { tabProps } from './props/tab';
export default TabPage as unknown as DefineComponent<
  Partial<ExtractPropTypes<typeof tabProps>>
>;
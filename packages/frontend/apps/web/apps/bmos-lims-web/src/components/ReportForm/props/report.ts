import { PropType } from 'vue';
import { getData } from '../utils/sheetData';

export type request = () => Promise<{ data: any }>;

export const ReportProps = {
  id: {
    type: String as PropType<string>,
  },
  url: {
    type: String as PropType<string>,
  },
  edit: {
    //是否预览
    type: Boolean as PropType<boolean>,
    default: true,
  },
  request: {
    type: Function as PropType<request>,
    default: () => getData,
  },
  defaultBorderRange: {
    type: String as PropType<string>,
    default: '',
  },
  showTitleContent: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
};

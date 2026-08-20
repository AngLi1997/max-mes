import { FunctionalComponent } from 'vue';
import { detailFieldNames } from './detailItem';
export interface DetailListPropsType {
  list: Array<Record<any, any>>;
  icon?: FunctionalComponent;
  isView?: boolean;
}

export interface DetailListItemType {
  node: Record<any, any>;
  [key: string]: any;
}

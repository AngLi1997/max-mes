import { FunctionalComponent } from 'vue';

export interface detailFieldNames {
  name: string;
  id: string;
}

export interface DetailItemPropsType {
  item: Record<any, any>;
  icon?: FunctionalComponent;
  isView?: boolean;
}

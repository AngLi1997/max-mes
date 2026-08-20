import { ActionListItem, FormProps, TableColumn } from '@bmos/components';
import type { RadioChangeEvent, RadioGroupProps, UploadChangeParam } from 'ant-design-vue';
import type { ComputedRef, Ref } from 'vue';
import { METHOD } from './enum';
type STATUS_TYPE = {
  OPEN: boolean;
  METHOD: METHOD;
  CATETORY: boolean;
  FORM: number;
};
type schemas = FormProps['schemas'];
export interface UseModalFormType<T = any, _U extends object = any, P = any> {
  fileUploadChange: (info: UploadChangeParam, model: T) => void;
  RadioGroupOptions: RadioGroupProps['options'];
  addRecord: (treeNode: P) => void;
  ADD_RECORD: ComputedRef<schemas>;
  tableData: any;
  columns: any;
  RadioGroupChange: (e: RadioChangeEvent, model?: T) => void;
  handleModalSubmit: (inst: any[], val: T) => void;
  formDefaultValue: T;
  STATUS: STATUS_TYPE;
  typeIsFile: any;
  myFormRef: any;
  downloadFile: any;
}

export type UseCategoryType = {
  HANDLE_ACTION: (type: string, key: KEY) => Promise<Boolean | undefined | void>;
  MODAL_SUBMIT: (val: any) => Promise<Boolean | undefined>;
};

export interface UseTreeType<T = any, U extends object = any> {
  TREE_DATA: T;
  INIT_TREE_DATA: () => {};
  TREE_ACTION: (action: ActionListItem, node: U) => {};
  CURRENT: Ref<{ action: ActionListItem; node: U }>;
  FORM_ITEMS: FormProps;
  ACTION_LIST: Array<{ title: string; action: string }>[];
}

export interface UseColumnsType {
  recordColumn: TableColumn[];
  versionColumn: TableColumn[];
  tableFields: Ref<Array<Record<string, any>>>;
  historyOpen: Ref<boolean>;
  secondRowData: Ref<Record<string, any>>;
}

export interface UseVersionType {
  VersionFormProps: FormProps['schemas'];
  RadioGroupChange: (e: RadioChangeEvent) => {};
  versionDefaultValue: FormProps['initialValues'];
}

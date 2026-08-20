import { NODE_TYPE } from '@/components/Record';
import type { EmitFn, FormProps, Recordable } from '@bmos/components';
import { HAS_FORM_BUSINESS_NODE } from '../hooks/const';

export interface FormConfig {
  title?: string;
  formProps?: FormProps;
  formKeys?: any;
  notShowTitle?: boolean;
}

export interface ConfigFormProps {
  activeComponentType: NODE_TYPE | HAS_FORM_BUSINESS_NODE | null;
  initFormValue: Recordable<any>;
  isView?: boolean;
  procedureId: string;
  versionId: string;
  configList: Array<Record<string, any>>;
  activeNodeData: Recordable<any>;
  nodeList: Array<Record<string, any>>;
}

export type NodeType = keyof typeof NODE_TYPE;

export const formEmits = {
  submit: (_formValues: Recordable<any>) => true,
  cancel: () => true,
};

export type FormEmits = typeof formEmits;

export type FormEmitFn = EmitFn<FormEmits>;

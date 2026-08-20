import { modalFormProps } from '@bmos/components';
import type { EmitsOptions, ExtractPropTypes, SetupContext } from 'vue';
import sign from '../index.vue';

export interface LabelList {
  label: string;
}

export interface UserList {
  loginName: string
  userId: string
  userName: string
}

export const signProps = {
  ...modalFormProps,
  // title
  title: {
    type: String,
    default: t('签名确认'),
  },
  // 系统编码
  systemCode: {
    type: String,
    default: '130',
  },
  // 签名操作对象拼装函数
  signatureDataFn: {
    type: Function,
    default: (formValues: any) => JSON.stringify(formValues),
  },
  // 签名动作
  signatureAction: {
    type: Number,
    default: 18,
  },
  // 签名类型 0 密码认证
  signatureType: {
    type: Number,
    default: 0,
  },
  // 额外字段
  extraSchemas: {
    type: Array,
    default: () => [],
  },
};

export type Recordable<T = any> = Record<string, T>;

export type Key = string | number;

export type EmitFn<E = EmitsOptions> = SetupContext<E>['emit'];

export const signEmits = {
  'update:open': (open: boolean) => typeof open === 'boolean',
  signSuccess: (formModel: Recordable<any>) => true,
};

export type SignEmits = typeof signEmits;

export type SignEmitFn = EmitFn<SignEmits>;

export type SignProps = Partial<ExtractPropTypes<typeof signProps>>;

export type SignInstanceType = InstanceType<typeof sign>;

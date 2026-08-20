import { modalFormProps } from '@bmos/components';
import type { EmitsOptions, ExtractPropTypes, SetupContext } from 'vue';
import sign from '../index.vue';

export interface LabelList {
  label: string;
}

export interface UserList {
  loginName: string;
  userId: string;
  userName: string;
}

export const signProps = {
  ...modalFormProps,
  // 系统编码
  systemCode: {
    type: String,
    default: '210',
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
  // 签名后置操作
  afterSign: {
    type: Function,
    default: () => {},
  },
};

export type Recordable<T = any> = Record<string, T>;

export type Key = string | number;

export type EmitFn<E = EmitsOptions> = SetupContext<E>['emit'];

export const signEmits = {
  'update:open': (open: boolean) => typeof open === 'boolean',
  signSuccess: (url: string) => url,
};

export type SignEmits = typeof signEmits;

export type SignEmitFn = EmitFn<SignEmits>;

export type SignProps = Partial<ExtractPropTypes<typeof signProps>>;

export type SignInstanceType = InstanceType<typeof sign>;

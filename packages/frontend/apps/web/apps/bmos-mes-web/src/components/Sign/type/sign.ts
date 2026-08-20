import { modalFormProps } from '@bmos/components';
import type { EmitsOptions, ExtractPropTypes, SetupContext } from 'vue';
import sign from '../index.vue';

export interface LabelList {
  label: string;
  action?: number;
}

export interface UserList {
  loginName: string;
  userId: string;
  userName: string;
}

export const signProps = {
  ...modalFormProps,
  // 用户名字段以及label
  labelList: {
    type: Array as PropType<LabelList[]>,
    default: () => [
      {
        label: t('用户名'),
      },
    ],
  },
  // title
  title: {
    type: String,
    default: t('签名确认'),
  },
  // 系统编码
  systemCode: {
    type: String,
    default: '120',
  },
  // 签名操作对象
  signatureData: {
    type: String,
    default: '',
  },
  // 签名动作
  signatureAction: {
    type: Number,
    default: 12,
  },
  // 签名类型 0 密码认证
  signatureType: {
    type: Number,
    default: 0,
  },
  // remark
  remark: {
    type: String,
    default: '',
  },
  // userList
  userList: {
    type: Array as PropType<UserList[]>,
    default: () => [],
  },
  // 是否显示alert
  showAlert: {
    type: Boolean,
    default: false,
  },
  // alertDesc
  alertDesc: {
    type: String,
    default: '',
  },
  // alertType
  alertType: {
    type: String as PropType<'success' | 'error' | 'warning' | 'info'>,
    default: 'success',
  },
};

export type Recordable<T = any> = Record<string, T>;

export type Key = string | number;

export type EmitFn<E = EmitsOptions> = SetupContext<E>['emit'];

export const signEmits = {
  'update:open': (open: boolean) => typeof open === 'boolean',
  signSuccess: (_formModel: Recordable<any>) => true,
  cancelModal: () => true,
};

export type SignEmits = typeof signEmits;

export type SignEmitFn = EmitFn<SignEmits>;

export type SignProps = Partial<ExtractPropTypes<typeof signProps>>;

export type SignInstanceType = InstanceType<typeof sign>;

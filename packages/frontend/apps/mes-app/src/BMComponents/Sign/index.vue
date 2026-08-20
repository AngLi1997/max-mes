<template>
  <wd-row :gutter="16">
    <wd-col v-for="(item, index) in labelList" :key="index" :span="spanSize">
      <view class="sign-item">
        <BMDataSelect
          v-model="formData[`loginName${1 + index}`]"
          :label="item.label"
          :field-names="customFieldNames"
          :options="optionsReactive[`options${1 + index}`]"
          :placeholder="t('请选择') + item.label"
          :disabled="item.disabled"
          @select="userSelect(index, $event)"
        />
        <BMPassWordInput v-model="formData[`password${1 + index}`]" />
      </view>
    </wd-col>
  </wd-row>
</template>

<script setup>
import {
  listByMenuIdApi,
  postVerifyPlatformSignatureV2Api,
} from '@/api/systemApi.js';
import { BMDataSelect } from '@/BMComponents';
import { encrypt } from '@/utils/encrypt.js';
import { getCurrentTime } from '@/utils/time.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, reactive, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import BMPassWordInput from './components/passwordInput/index.vue';

const props = defineProps({
  currentTime: {
    type: String,
    default: '',
  },
  modelValue: {
    type: Object,
    default: () => {
      return {
        userName1: '',
        userName2: '',
        loginName1: '',
        loginName2: '',
        password1: '',
        password2: '',
        userId1: '',
        userId2: '',
      };
    },
  },
  labelList: {
    type: Array,
    default: () => [
      {
        label: t('账号1'),
        // 签名动作
        signatureAction: 0,
        options: null,
      },
      {
        label: t('账号2'),
        // 签名动作
        signatureAction: 0,
        options: [],
      },
    ],
  },
  fieldNames: {
    type: Object,
    default: () => ({ value: 'value', label: 'label', id: 'id' }),
  },
  // 系统编码
  systemCode: {
    type: String,
    default: '121',
  },
  // 签名操作对象
  signatureData: {
    type: Object,
    default: () => {
      return {};
    },
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
  // 是否不允许相同
  allowSame: {
    type: Boolean,
    default: false,
  },
  // 签名组件默认值
  defaultValue: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

const emit = defineEmits(['update:modelValue', 'update:currentTime']);

const { showNotify } = useNotify();

const spanSize = computed(() => {
  return 24 / props.labelList.length;
});

const customFieldNames = computed(() => {
  return Object.assign(
    {
      value: 'value',
      label: 'label',
      id: 'id',
    },
    props.fieldNames,
  );
});

const formData = reactive({
  userName1: '',
  userName2: '',
  loginName1: '',
  loginName2: '',
  password1: '',
  password2: '',
  userId1: '',
  userId2: '',
});

const optionsReactive = reactive({
  options1: [],
  options2: [],
});

const userSelect = (index, data) => {
  formData[`userId${1 + index}`] = data[customFieldNames.value.id];
  formData[`userName${1 + index}`] = data[customFieldNames.value.label];
};

const checkSign = () => {
  return new Promise((resolve, reject) => {
    const {
      loginName1,
      loginName2,
      password1,
      password2,
      userId1,
      userId2,
    } = formData;
    if (!loginName1) {
      showNotify({
        type: 'warning',
        message: t('请选择') + props.labelList[0].label,
      });
      reject(new Error('error'));
      return;
    }
    if (!password1) {
      showNotify({
        type: 'warning',
        message: t('请输入') + props.labelList[0].label + t('密码'),
      });
      reject(new Error('error'));
      return;
    }
    if (props.labelList.length > 1) {
      if (!loginName2) {
        showNotify({
          type: 'warning',
          message: t('请选择') + props.labelList[1].label,
        });
        reject(new Error('error'));
        return;
      }
      if (!password2) {
        showNotify({
          type: 'warning',
          message: t('请输入') + props.labelList[1].label + t('密码'),
        });
        reject(new Error('error'));
        return;
      }
    }
    const { systemCode, signatureData, signatureType, remark, allowSame } = props;
    if (!allowSame && userId1 === userId2) {
      showNotify({
        type: 'warning',
        message: `${props.labelList[0].label}${t('和')}${props.labelList[1].label}${t('不能相同')}`,
      });
      reject(new Error('error'));
      return;
    }
    const data = [
      {
        loginName: loginName1,
        password: encrypt(password1),
        signatureAction: props.labelList[0].signatureAction,
      },
    ];
    if (props.labelList.length > 1) {
      data.push({
        loginName: loginName2,
        password: encrypt(password2),
        signatureAction: props.labelList[1].signatureAction,
      });
    }
    const signData = {
      systemCode,
      signatureData: JSON.stringify(signatureData),
      signatureType,
      remark,
      allowSame,
      validates: data,
    };
    postVerifyPlatformSignatureV2Api(signData)
      .then((res) => {
        const failedIndex = res.data.failedIndex || [];
        if (failedIndex.length > 0) {
          let message = '';
          if (failedIndex.length === 1) {
            message = props.labelList[failedIndex[0]].label + t('密码错误');
          }
          else {
            message = props.labelList[0].label + t('密码错误');
          }
          showNotify({
            type: 'warning',
            message,
          });
          reject(new Error('error'));
        }
        else {
          resolve(formData);
        }
      })
      .catch((err) => {
        reject(err);
      });
  });
};

// 设置默认签名值
const setDefaultSignValue = () => {
  const currentUser = getStorageSync(USER_INFO) || {};
  const { userId, loginName, userName } = currentUser;
  if (optionsReactive.options1.length > 0) {
    if (props.labelList[0]?.disabled || optionsReactive.options1.length === 1) {
      formData.loginName1
          = optionsReactive.options1[0][customFieldNames.value.value];
      formData.userId1
          = optionsReactive.options1[0][customFieldNames.value.id];
      formData.userName1 = optionsReactive.options1[0][customFieldNames.value.label];
    }
    else if (props.labelList[0]?.currentUser) {
      // 设备默认签名值为当前登录用户
      formData.loginName1 = loginName;
      formData.userId1 = userId;
      formData.userName1 = userName;
    }
  }
  if (optionsReactive.options2.length > 0) {
    if (props.labelList[1]?.disabled || optionsReactive.options2.length === 1) {
      formData.loginName2
          = optionsReactive.options2[0][customFieldNames.value.value];
      formData.userId2
          = optionsReactive.options2[0][customFieldNames.value.id];
    }
    else if (props.labelList[1]?.currentUser) {
      // 设备默认签名值为当前登录用户
      formData.loginName2 = loginName;
      formData.userId2 = userId;
      formData.userName2 = userName;
    }
  }
};

const resetSignValue = () => {
  // 重置签名值（）
  Object.keys(formData).forEach((key) => {
    formData[key] = props.defaultValue[key] || '';
  });
  setDefaultSignValue();
};

watch(
  () => props.modelValue,
  (val) => {
    Object.keys(val).forEach((key) => {
      formData[key] = val[key];
    });
  },
  {
    immediate: true,
  },
);
watch(
  () => formData,
  () => {
    emit('update:modelValue', formData);
    emit('update:currentTime', getCurrentTime());
  },
  {
    deep: true,
  },
);
watch(
  () => props.labelList,
  async () => {
    if (props.labelList && props.labelList.length > 0) {
      if (props.labelList[0]?.menuId) {
        const res = await listByMenuIdApi({
          menuId: props.labelList[0].menuId,
        });
        optionsReactive.options1 = (res.data || []).map((item) => {
          const { userId, userName, loginName } = item;
          const returnObj = {};
          returnObj[customFieldNames.value.value] = loginName;
          returnObj[customFieldNames.value.label] = userName;
          returnObj[customFieldNames.value.id] = userId;
          return returnObj;
        });
      }
      else if (props.labelList[0]?.options) {
        optionsReactive.options1 = props.labelList[0].options;
      }
      else {
        const currentUser = getStorageSync(USER_INFO) || {};
        const { userId, userName, loginName } = currentUser;
        const returnObj = {};
        returnObj[customFieldNames.value.value] = loginName;
        returnObj[customFieldNames.value.label] = userName;
        returnObj[customFieldNames.value.id] = userId;
        optionsReactive.options1 = [
          returnObj,
        ];
      }
      if (props.labelList.length > 1) {
        if (props.labelList[1]?.menuId) {
          const res = await listByMenuIdApi({
            menuId: props.labelList[1].menuId,
          });
          optionsReactive.options2 = (res.data || []).map((item) => {
            const { userId, userName, loginName } = item;
            const returnObj = {};
            returnObj[customFieldNames.value.value] = loginName;
            returnObj[customFieldNames.value.label] = userName;
            returnObj[customFieldNames.value.id] = userId;
            return returnObj;
          });
        }
        else if (props.labelList[1]?.options) {
          optionsReactive.options2 = props.labelList[1].options;
        }
        else {
          const currentUser = getStorageSync(USER_INFO) || {};
          const { userId, userName, loginName } = currentUser;
          const returnObj = {};
          returnObj[customFieldNames.value.value] = loginName;
          returnObj[customFieldNames.value.label] = userName;
          returnObj[customFieldNames.value.id] = userId;
          optionsReactive.options2 = [
            returnObj,
          ];
        }
      }
      setDefaultSignValue();
    }
  },
  { deep: true, immediate: true },
);

defineExpose({
  checkSign,
  resetSignValue,
});
</script>

<style lang="scss" scoped>
.sign-item {
  background-color: var(--bmos-bg-form);
  padding: 0 9.38rpx;
  box-sizing: border-box;
  border-radius: 4.69rpx;
}
</style>

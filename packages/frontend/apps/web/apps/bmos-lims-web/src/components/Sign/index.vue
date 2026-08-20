<template>
  <BMModalForm
    ref="signModalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
import { BMModalForm, FormProps, FormSchema, ModalFormInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { 
  encrypt
} from '@bmos/utils';
import { Recordable, UserList, signEmits, signProps } from './type';
import { sso } from '@bmos/messager';
import { mesSignatureValidateV2 } from '@/services';
import { message } from 'ant-design-vue';
import { computed, nextTick, reactive, ref, watch } from 'vue';

const props = defineProps(signProps);
const emit = defineEmits(signEmits);

const open = computed({
  get: () => {
    return props.open || false;
  },
  set: (val: boolean) => {
    emit('update:open', val);
  },
});

const formProps = computed<FormProps>(() => ({
    baseColProps: {
      span: 24,
    },
    schemas: [
      ...props.extraSchemas as FormSchema<string>[] & FormSchema<any>[],
      {
        field: 'loginName',
        component: 'Input',
        label: t('用户名'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'password',
        component: 'InputPassword',
        label: t('密码'),
        required: true,
        componentProps: {
          autocomplete: 'new-password',
        },
      },
    ],
}))

const { getUserInfo } = sso;
const userInfo = getUserInfo();

const submit = async (formValues: Recordable) => {
  try {
    const { data } = await mesSignatureValidateV2({
      validates: [{
        loginName: userInfo.loginName,
        password: encrypt(formValues.password),
        signatureAction: props.signatureAction,
      }],
      signatureData: props.signatureDataFn(formValues),
      signatureType: props.signatureType,
      systemCode: props.systemCode,
      // remark: props.remark,
    });
    const { failedIndex } = data;
    if (failedIndex.length > 0) {
      if (failedIndex[0] === 0) {
        message.error(t('密码错误'));
      } else if (failedIndex[0] === 1) {
        message.error(t('密码错误'));
      } else {
        message.error(t('签名失败'));
      }
      return Promise.reject(false);
    }
    emit('signSuccess', formValues);
    open.value = false;
    return Promise.resolve(true);
  } catch (error) {
    return Promise.reject(false);
  }
};

const signModalFormRef = ref<ModalFormInstance>();

const setFormModel = async (key: string, value: any) => {
  await nextTick();
  signModalFormRef.value?.formRef?.setFormModel(key, value);
};

watch(
  () => open.value,
  async (val) => {
    await nextTick();
    if (val) {
      signModalFormRef.value?.formRef?.setFormModel('loginName', userInfo?.userName + '-' + userInfo?.loginName);
    }
  }
)

defineExpose({
  signModalFormRef,
  open,
  setFormModel
})

</script>
<style lang="less"></style>

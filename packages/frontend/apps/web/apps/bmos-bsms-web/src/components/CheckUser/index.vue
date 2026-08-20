<!-- 身份核对 -->
<template>
  <BMModalForm
    ref="signModalFormRef"
    v-model:open="open"
    :title="t('身份核对')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @cancelModal="cancelModal"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, FormProps, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { checkUser } from '@/services';
  import { message } from 'ant-design-vue';
  import { computed, nextTick, ref, watch } from 'vue';
  import router from '@/router';

  const props = defineProps({
    checkNum: {
      type: Number,
      default: 0,
    },
  });

  const open = ref(false);

  const emit = defineEmits(['signSuccess']);

  const formProps = computed<FormProps>(() => ({
    initialValues: {
      // loginName: 'yzl',
      // password: 'Bmos1018',
    },
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'loginName',
        component: 'Input',
        label: t('用户名'),
        required: true,
        componentProps: {
          // disabled: true,
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
  }));

  const submit = async (formValues: any) => {
    try {
      await checkUser({
        loginName: formValues.loginName,
        // password: encrypt(formValues.password),
        password: formValues.password,
      });
      emit('signSuccess', formValues);
      open.value = false;
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const signModalFormRef = ref<ModalFormInstance>();

  const setFormModel = async (key: string, value: any) => {
    await nextTick();
    signModalFormRef.value?.formRef?.setFormModel(key, value);
  };

  const cancelModal = () => {
    open.value = false;
    if (props.checkNum > 0) {
      return;
    }
    // 返回上一页
    router.back();
  };

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        // signModalFormRef.value?.formRef?.setFormModel('loginName', userInfo?.userName + '-' + userInfo?.loginName);
      }
    },
  );

  defineExpose({
    setFormModel,
    open,
  });
</script>

<style scoped></style>

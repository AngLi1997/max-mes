<!-- 进入页面输入核查批号 -->
<template>
  <BMModalForm
    ref="signModalFormRef"
    v-model:open="open"
    :title="t('请输入核查批号')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"
    @cancelModal="cancelModal"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, FormProps, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { sortingPlasmaValidateCheckNo } from '@/services';
  import { message } from 'ant-design-vue';
  import { computed, nextTick, ref, watch } from 'vue';
  import router from '@/router';

  const open = ref(false);

  const emit = defineEmits(['checkSuccess']);

  const formProps = computed<FormProps>(() => ({
    initialValues: {},
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'checkNo',
        component: 'Input',
        label: t('核查批号'),
        required: true,
        componentProps: {
          // disabled: true,
        },
      },
    ],
  }));

  const submit = async (formValues: any) => {
    try {
      await sortingPlasmaValidateCheckNo(formValues);
      emit('checkSuccess', formValues);
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
    open,
    setFormModel,
  });
</script>

<style scoped></style>

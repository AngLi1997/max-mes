<template>
  <!-- 编辑框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('查看详情')"
    :formProps="formProps"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm } from '@bmos/components';
  import { reactive, ref, nextTick, watch } from 'vue';
  import { t } from '@bmos/i18n';
  import { Button } from 'ant-design-vue';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
  });
  const modalFormRef = ref<any>();
  const open = ref<any>(false);
  const openModal = () => {
    open.value = true;
  };
  // 查看的表单
  const formProps = reactive<any>({
    initialValues: {},
    disabled: true,
    transformDateFunc: (date: any) => {
      console.log(date, 'date');
      return date?.format?.('YYYY-MM-DD HH:mm:ss') ?? date;
    },
    labelCol: { span: 6 },
    wrapperCol: { span: 17 },
    schemas: [
      {
        field: 'loginName',
        component: 'Input',
        label: t('用户账号'),
      },
      {
        field: 'userName',
        component: 'Input',
        label: t('用户名称'),
        componentProps: {
          placeholder: '',
        },
      },
      {
        field: 'ip',
        component: 'Input',
        label: 'IP',
      },
      {
        field: 'operationAction',
        component: 'Input',
        label: t('操作动作'),
      },
      {
        field: 'operationState',
        component: 'Select',
        label: t('操作状态'),
        componentProps: {
          options: [
            {
              label: t('成功'),
              value: true,
            },
            {
              label: t('失败'),
              value: false,
            },
          ],
        },
      },
      {
        field: 'operationTime',
        component: 'Input',
        label: t('操作时间'),
      },
      {
        field: 'description',
        component: 'InputTextArea',
        label: t('操作描述'),
        componentProps: {
          placeholder: '',
        },
      },
    ],
  });

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef.setFieldsValue(props.rowData); //回显编辑框
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal, formProps });
</script>
<style lang="less" scoped></style>

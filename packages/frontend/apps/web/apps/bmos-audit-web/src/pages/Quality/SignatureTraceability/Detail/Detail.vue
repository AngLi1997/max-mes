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
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { reactive, ref, nextTick, watch } from 'vue';
  import { t } from '@bmos/i18n';
  import { Button } from 'ant-design-vue';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
  });
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
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
        field: 'systemName',
        component: 'Input',
        label: t('系统名称'),
      },
      {
        field: 'signatureType',
        component: 'Input',
        label: t('签名类型'),
      },
      {
        field: 'signatureAction',
        component: 'Input',
        label: t('签名动作'),
      },
      {
        field: 'userName',
        component: 'Input',
        label: t('签名人'),
        componentProps: {
          placeholder: '',
        },
      },
      {
        field: 'signatureData',
        component: 'InputTextArea',
        label: t('签名对象'),
      },
      {
        field: 'createTime',
        component: 'Input',
        label: t('签名时间'),
      },
      {
        field: 'signatureDataDetail',
        component: 'InputTextArea',
        label: t('签名对象详情'),
      },
      {
        field: 'success',
        component: 'Select',
        label: t('状态'),
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
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
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
        modalFormRef.value?.formRef?.setFieldsValue(props.rowData); //回显编辑框
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal, formProps });
</script>
<style lang="less" scoped></style>

<!-- 采集点管理框 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="ok"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { reqAcquisitionPoint, reqInventoryEditInventoryBatch } from '@/services';
  import { message } from 'ant-design-vue';
  const emit = defineEmits(['updateTable']);

  const props = withDefaults(
    defineProps<{
      rowId?: string;
      type?: string;
      formData?: any;
    }>(),
    {
      rowId: '',
      type: '',
      formData: {},
    },
  );

  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const title = ref<any>('');
  const formProps = computed<any>(() => {
    const initialValues = {};
    const schemas = [
      {
        field: 'type',
        component: 'RadioGroup',
        label: t('采集点类型'),
        required: true,
        componentProps: {
          options: [
            {
              label: t('属性'),
              value: 'ATTR',
            },
            {
              label: t('服务'),
              value: 'SERVICE',
            },
            {
              label: t('事件'),
              value: 'EVENT',
            },
          ],
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              // type: 'number',
              message: t('采集点类型'),
            },
          ];
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('采集点名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        required: true,
        label: t('采集点编码'),
        componentProps: {
          disabled: props.type === 'edit' ? true : false,
        },
      },
      {
        field: 'dataType',
        component: 'Select',
        label: t('数据类型'),
        required: true,
        componentProps: {
          options: [
            {
              label: t('数值类型'),
              value: 'NUMBER',
            },
            {
              label: t('字符串类型'),
              value: 'STRING',
            },
            {
              label: t('时间类型'),
              value: 'DATETIME',
            },
          ],
        },
      },
      {
        field: 'acquisitionPlatform',
        component: 'Select',
        label: t('数采平台'),
        required: true,
        componentProps: {
          options: [
            {
              label: t('HUB'),
              value: 'hub',
            },
            {
              label: t('中控'),
              value: 'supCon',
            },
          ],
        },
      },
      {
        field: 'dataPointName',
        component: 'Input',
        label: t('数据点位名称'),
        required: true,
      },
      {
        field: 'description',
        component: 'InputTextArea',
        label: t('描述'),
        componentProps: {
          // 限制200 个字符
          maxLength: 200,
        },
      },
    ];
    return {
      initialValues,
      schemas,
      disabled: props.type === 'view' ? true : false,
      labelCol: {
        span: 5,
      },
    };
  });
  // 确定
  const ok = async () => {
    if (props.type === 'view') {
      open.value = false;
      return;
    }
    const data: any = await modalFormRef.value?.validate();
    try {
      data?.id ? await reqInventoryEditInventoryBatch(data) : await reqAcquisitionPoint(data);
      data?.id ? message.success(t('编辑成功')) : t('新增成功');
      open.value = false;
      emit('updateTable');
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const openModal = () => {
    open.value = true;
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (!val) return;
      try {
        if (props.type === 'view') {
          title.value = t('采集点详情');
          modalFormRef.value?.formRef?.setFormModels({
            ...props.formData,
            type: props.formData?.type?.value,
          });
        }
        if (props.type === 'add') {
          title.value = t('新增采集点');
          modalFormRef.value?.formRef?.setFormModels({
            type: 'ATTR',
          });
        }
        if (props.type === 'edit') {
          title.value = t('编辑采集点');
          modalFormRef.value?.formRef?.setFormModels({
            ...props.formData,
            type: props.formData?.type?.value,
            dataType: props.formData?.dataType?.value,
            acquisitionPlatform: props.formData?.acquisitionPlatform?.value,
          });
        }
      } catch (error) {}
    },
    {
      immediate: true,
    },
  );
  defineExpose({
    openModal,
  });
</script>

<style lang="less" scoped></style>

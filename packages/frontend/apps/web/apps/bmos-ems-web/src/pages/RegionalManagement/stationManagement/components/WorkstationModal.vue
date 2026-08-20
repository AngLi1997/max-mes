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
  import { loopTree } from '../utils';
  import {
    reqEquipmentStationSave, //新建设备工位
    reqEquipmentStationUpdate, //编辑设备工位
  } from '@/services';
  import { reqStationModuleTreeList } from '@/services';
  import { message } from 'ant-design-vue';

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
  const emit = defineEmits(['updateTable']);
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const title = ref<any>('');
  const treeData = ref<any>([]);
  const formProps = computed<any>(() => {
    const initialValues = {};
    const schemas = [
      {
        field: 'moduleId',
        component: 'TreeSelect',
        label: t('所属分类'),
        required: true,
        componentProps: {
          disabled: props.type === 'edit' ? true : false,
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          treeData: treeData.value,
          request: async () => {
            const res: any = await reqStationModuleTreeList();
            return loopTree(res.data);
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('工位名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        required: true,
        label: t('工位编码'),
        componentProps: {
          disabled: props.type === 'edit' ? true : false,
        },
      },
      {
        field: 'description',
        component: 'InputTextArea',
        label: t('描述'),
        componentProps: {
          maxLength: 200,
        },
      },
    ];
    return { initialValues, schemas, disabled: false };
  });
  // 确定
  const ok = async () => {
    const data: any = await modalFormRef.value?.validate();
    try {
      props.type === 'add' ? await reqEquipmentStationSave(data) : await reqEquipmentStationUpdate(data);
      props.type === 'add' ? message.success(t('新增成功')) : message.success(t('编辑成功'));
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
        if (props.type === 'add') {
          title.value = t('新增工位');
          modalFormRef.value?.formRef?.setFormModels({
            moduleId: props.formData.id && props.formData.id !== 'all' ? props.formData.id : undefined,
          });
        } else {
          title.value = t('编辑工位');
          modalFormRef.value?.formRef?.setFormModels({
            ...props.formData,
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

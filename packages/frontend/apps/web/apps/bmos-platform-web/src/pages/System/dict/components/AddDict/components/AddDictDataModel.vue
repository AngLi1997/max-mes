<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    :okButtonProps="okButtonProps"
    wrapClassName="modalSizeMedium"
    class="add-dict-data-modal"
    @okModal="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import {
    BMModalForm,
    ModalFormType,
    FormProps,
    ModalFormInstance,
    Recordable,
  } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { MODAL_STATUS } from '../../../types';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTableData', params: Recordable, dictValue: string): void;
    (e: 'addTableData', params: Recordable): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      tableData: any[];
      rowData?: any;
      status?: MODAL_STATUS;
    }>(),
    {
      rowData: {},
      status: MODAL_STATUS.ADD,
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const title = ref<string>(t('新增字典数据'));

  const request = async (formModal: any) => {
    const params = {
      ...formModal,
    };
    // const { expressionCategoryId, ...editParams } = params;
    // 判断 tableData 中是否存在相同的数据键值 dictValue
    const isExist = props.tableData.some(item => {
      if (MODAL_STATUS.ADD) {
        return item.dictValue === params.dictValue && item.dictValue !== props.rowData?.dictValue;
      } else {
        return item.dictValue === params.dictValue
      }
    });
    if (isExist) {
      return Promise.reject({ message: t('字典数据已存在') });
    }
    switch (props.status) {
      case MODAL_STATUS.ADD:
        emit('addTableData', params);
        return Promise.resolve();
      case MODAL_STATUS.EDIT:
        emit('updateTableData', {id:props.rowData?.id?props.rowData?.id:undefined,...params}, props.rowData?.dictValue);
        return Promise.resolve();
      default:
        return Promise.reject();
    }
  };
  const submit = async (modalFormType: ModalFormType) => {
    try {
      await modalFormRef.value?.submit(request);
      if (props.status === MODAL_STATUS.EDIT) {
        message.success(t('编辑成功'));
      } else {
        message.success(t('新增成功'));
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        field: 'dictLabel',
        component: 'Input',
        label: t('数据标签'),
        required: true,
        componentProps: {
        maxlength: 100,
      }
      },
      {
        field: 'dictValue',
        component: 'Input',
        label: t('数据键值'),
        required: true,
        componentProps: {
        maxlength: 100,
      }
      },
    ],
  });
// 清空表单方法
const empty=async()=>{
  formProps.initialValues={}
}
defineExpose({empty})
  const okButtonProps = ref({});
  watch(
    () => open.value,
    async val => {
      await nextTick();
      switch (props.status) {
        case MODAL_STATUS.ADD:
          title.value = t('新增字典数据');
          okButtonProps.value = {
            disabled: false,
          }; 
          break;
        case MODAL_STATUS.EDIT:
          title.value = t('编辑字典数据');
          okButtonProps.value = {
            disabled: false,
          };
          modalFormRef.value?.formRef?.setFormProps({
            disabled: false,
          });
          modalFormRef.value?.formRef?.setFieldsValue({
            ...props.rowData,
          });
          break;
        case MODAL_STATUS.VIEW:
          title.value = t('查看字典数据');
          okButtonProps.value = {
            disabled: true,
          };
          modalFormRef.value?.formRef?.setFormProps({
            disabled: true,
          });
          modalFormRef.value?.formRef?.setFieldsValue({
            ...props.rowData,
          });
          break;
        default:
          break;
      }
    },
    {
      immediate: true,
    },
  );
</script>

<style lang="less" scoped>
  .add-dict-data-modal {
  }
</style>

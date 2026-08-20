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
  import { BMModalForm, ModalFormType, FormProps, ModalFormInstance } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { MODAL_STATUS } from '../../types';
  import { reqPlatformDictUpdateDetailPOST, reqPlatformSaveDictDetailPOST } from '@/api';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      status?: MODAL_STATUS;
      selectDictId: string;
      selectDictName: string;
    }>(),
    {
      rowData: {},
      status: MODAL_STATUS.ADD,
      selectDictId: '',
      selectDictName: '',
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
    switch (props.status) {
      case MODAL_STATUS.ADD:
        return await reqPlatformSaveDictDetailPOST(params);
      case MODAL_STATUS.EDIT:
        params.id = props.rowData.id;
        return await reqPlatformDictUpdateDetailPOST(params);
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
      emit('updateTable');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    disabled: false,
    schemas: [
      {
        field: 'dictId',
        component: 'Select',
        label: t('字典名称'),
        required: true,
        componentProps: {
          disabled: true,
          options: [],
        },
      },
      {
        field: 'dictLabel',
        component: 'Input',
        label: t('数据标签'),
        required: true,
        componentProps: {
          maxlength: 100,
        },
      },
      {
        field: 'dictValue',
        component: 'Input',
        label: t('数据键值'),
        required: true,
        componentProps: {
          maxlength: 100,
        },
      },
    ],
  });

  const okButtonProps = ref({});
  watch(
    () => open.value,
    async () => {
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
      await nextTick();
      modalFormRef.value?.formRef?.updateSchema({
        field: 'dictId',
        componentProps: {
          options: [
            {
              label: props.selectDictName,
              value: props.selectDictId,
            },
          ],
        },
      });
      modalFormRef.value?.formRef?.setFormModel('dictId', props.selectDictId);
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

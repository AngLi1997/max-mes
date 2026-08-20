<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    :destroyOnClose="true"
    wrapClassName="modalSizeMedium"
    @okModal="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { reqCategoryTreeSave, reqCategoryTreeUpdate } from '@/api';
  import {
    BMModalForm,
    ModalFormType,
    FormProps,
    ModalFormInstance,
  } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { DataNode } from 'ant-design-vue/es/tree';
  import { t } from '@bmos/i18n';
  import { ALL_TYPE } from '../../../types';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTree', parentId? : string): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      title?: string;
      treeData: DataNode[] | undefined;
      editNode?: any;
      isEdit: boolean;
    }>(),
    {
      title: t('新建子分类'),
      isEdit: false,
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

  const title = ref<string>(t('新增分类'));
  const request = async (formModal: any) => {
    if (props.isEdit) {
      return await reqCategoryTreeUpdate({
        id: props.editNode.data.id,
        name: formModal.name,
        parentId: formModal.parentId === ALL_TYPE.ALL ? '' : formModal.parentId,
      });
    } else {
      return await reqCategoryTreeSave({
        ...formModal,
        parentId:
          props.editNode.data.id === ALL_TYPE.ALL ? '' : props.editNode.data.id,
      });
    }
  };
  const submit = async (modalFormType: ModalFormType) => {
    try {
      await modalFormRef.value?.submit(request);
      if (props.isEdit) {
        message.success(t('编辑成功'));
        emit('updateTree');
      } else {
        message.success(t('新增成功'));
        emit('updateTree', props.editNode.data.id);
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
        field: 'parentId',
        component: 'TreeSelect',
        label: t('上级分类'),
        required: true,
        componentProps: {
          disabled: true,
          treeData: props.treeData,
          fieldNames: {
            label: 'name',
            value: 'id',
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('分类名称'),
        required: true,
      },
      // {
      //   field: 'code',
      //   component: 'Input',
      //   label: t('分类编码'),
      //   required: true,
      //   vIf: () => {
      //     return props.isEdit;
      //   },
      // },
    ],
  });
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        if (props.isEdit) {
          title.value = t('编辑分类');
          modalFormRef.value?.formRef?.setFieldsValue({
            name: props.editNode.data.name,
            parentId:
              props.editNode.data.parentId != '0'
                ? props.editNode.data.parentId
                : ALL_TYPE.ALL,
          });
        } else {
          modalFormRef.value?.formRef?.setFormModel(
            'parentId',
            props.editNode.data.id ? props.editNode.data.id : ALL_TYPE.ALL,
          );
        }
      }
    },
    {
      immediate: true,
    },
  );
</script>

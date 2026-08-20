<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { NoTypeEnum, OperationStatusMap } from '@/types';
  import { postStaticDataConfigCreate, postStaticDataConfigEdit } from '@/services';
  import { useDict } from '@/stores';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);

  const props = withDefaults(
    defineProps<{
      status: OperationStatusMap;
      treeNode?: Recordable;
      rowData?: Recordable;
    }>(),
    {
      status: OperationStatusMap.ADD,
      treeNode: () => ({}),
      rowData: () => ({}),
    },
  );

  const title = computed(() => {
    return `${props.treeNode?.menuName}${props.status === OperationStatusMap.ADD ? t('添加') : t('编辑')}`;
  });

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        props.status === OperationStatusMap.EDIT &&
          modalFormRef.value?.formRef?.setFieldsValue({
            enumsValue: props.rowData?.enumsValue,
            description: props.rowData?.description,
          });
      }
    },
  );
  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'enumsValue',
        component: 'Input',
        label: t('枚举值'),
        required: true,
        componentProps: {
          maxLength: 100,
          showCount: true,
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: () => {
                if (!formModel['enumsValue']) {
                  return Promise.reject(t('请输入枚举值'));
                }
                // if (formModel['enumsValue'].length > 20) {
                //   return Promise.reject(t('枚举值长度不能超过20'));
                // }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'description',
        component: 'Input',
        label: t('描述'),
      },
    ],
  });
  const { setDict } = useDict();
  const submit = async (formModal: Recordable) => {
    try {
      if (props.status === OperationStatusMap.ADD) {
        await postStaticDataConfigCreate({
          ...formModal,
          menuIdentify: props.treeNode?.menuIdentify,
          noType: NoTypeEnum.STATIC_DATA_NO,
          staticDataType: props.treeNode?.menuName,
        });
      } else {
        await postStaticDataConfigEdit({
          id: props.rowData?.id,
          ...formModal,
          staticDataType: props.treeNode?.menuName,
        });
      }
      setDict(props.treeNode?.menuName);
      emit('ok');
      message.success(`${props.status === OperationStatusMap.ADD ? t('添加') : t('编辑')}${t('成功')}`);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>

<style scoped lang="less"></style>

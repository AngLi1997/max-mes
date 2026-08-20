<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('新增设备数采(表格)')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
  import { FormItemRest, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import EDADynamicTable from './components/EDADynamicTable.vue';
  import { isEmpty } from '@bmos/utils';

  const emit = defineEmits<{
    (e: 'updateDetail', formModal: Recordable): void;
  }>();

  const props = withDefaults(
    defineProps<{
      currentNode?: any;
    }>(),
    {
      currentNode: () => ({}),
    },
  );

  const open = defineModel<boolean>('open', {
    type: Boolean,
    default: false,
  });

  const modalFormRef = ref<any>();
  const submit = async (formModal: Recordable) => {
    try {
      emit('updateDetail', formModal);
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const formProps: Ref<FormProps> = ref({
    schemas: [
      {
        field: 'tableInfo',
        component: 'Divider',
        label: t('表格信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'rowNum',
        component: 'InputNumber',
        label: t('内容行数'),
        required: true,
        componentProps: {
          min: 1,
          max: 999,
          precision: 0,
          style: {
            width: '100%',
          },
        },
        colProps: {
          span: 12,
        },
      },
      {
        field: 'rowHeight',
        component: 'InputNumber',
        label: t('行高(px)'),
        required: true,
        defaultValue: 36,
        componentProps: {
          min: 1,
          style: {
            width: '100%',
          },
        },
        colProps: {
          span: 12,
        },
      },
      {
        field: 'tableData',
        component: 'Divider',
        label: t('表格数据'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'tableList',
        noLabel: true,
        component: ({ formModel, formInstance }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <EDADynamicTable
                  v-model:dataList={formModel.tableList}
                  onValidate={() => {
                    formInstance.validateFields(['tableList']);
                  }}
                />
              </FormItemRest>
            </>
          );
        },
        dynamicRules: () => {
          return [
            {
              type: 'array',
              validator: async (_rule: any, value: Array<Recordable>) => {
                if (!value || value.length === 0) {
                  return Promise.reject(t('请添加表格数据'));
                }
                let colNameFlag = false;
                let colDataFlag = false;
                value?.forEach((item: any) => {
                  if (isEmpty(item.colName)) {
                    colNameFlag = true;
                  }
                  if (isEmpty(item.colData)) {
                    colDataFlag = true;
                  }
                });
                if (colNameFlag) {
                  return Promise.reject(t('请填写列名'));
                }
                if (colDataFlag) {
                  return Promise.reject(t('请选择列数据'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
  });
  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        if (props.currentNode?.componentDetail) {
          modalFormRef.value?.formRef?.setFormModels({
            ...JSON.parse(props.currentNode.componentDetail),
          });
        }
      }
    },
  );
</script>

<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { FormItemRest, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import DeliveryTable from './DeliveryTable.vue';
  import { reqInventoryListByCargoIdAndBatchId } from '@/services';
  import { SendOrderType } from '../enum';
  import { isNullOrUnDef } from '@bmos/utils';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable', curFormModal: any): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      sendOutDetail?: Recordable;
    }>(),
    {
      rowData: () => ({}),
      sendOutDetail: () => ({}),
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

  const title = ref<string>(t('批次发料'));

  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      const { productList, realQuantity } = formModal;
      curFormModal.value = {
        productList,
        realQuantity,
        cargoId: props.rowData?.cargoId,
        inventoryBatchId: props.rowData?.inventoryBatchId,
        type: props.sendOutDetail.sendOrderType,
      };
      emit('updateTable', curFormModal.value);
      open.value = false;
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const productList = ref<Recordable[]>([]);
  const modalFormRef = ref<ModalFormInstance>();
  const formProps: Ref<FormProps> = ref({
    schemas: [
      {
        field: 'field6',
        component: 'Divider',
        label: t('出库批次'),
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
        field: 'productList',
        label: '',
        noLabel: true,
        required: true,
        colProps: {
          span: 24,
        },
        defaultValue: [],
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: (rule: any, value: any) => {
                if (isNullOrUnDef(value) || value.length === 0) {
                  return Promise.reject(t('未勾选货品件号'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <FormItemRest>
              <DeliveryTable
                selects={formModel.productList?.map((item: any) => item.id)}
                rowData={props.rowData}
                productList={productList.value}
                onUpdate:selects={(selects: string[], realQuantity: string) => {
                  formModel.productList = selects;
                  formModel.realQuantity = realQuantity;
                }}
              />
            </FormItemRest>
          );
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
        try {
          // 获取详细信息
          const { sendOrderType } = props.sendOutDetail;
          const { cargoId, inventoryBatchId, productList: curProductList } = props.rowData;
          sendOrderType === SendOrderType.BATCH && (title.value = t('批次发料'));
          sendOrderType === SendOrderType.CARGO && (title.value = t('货品发料'));
          const { data } = await reqInventoryListByCargoIdAndBatchId(
            cargoId,
            inventoryBatchId ? inventoryBatchId : undefined,
          );
          productList.value = data;
          modalFormRef.value?.formRef?.setFormModel('productList', curProductList);
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>

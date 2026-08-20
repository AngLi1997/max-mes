<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
  <Sign
    v-model:open="signOpen"
    :signatureData="JSON.stringify(curFormModal)"
    :signatureAction="35"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import Sign from '@/components/Sign';
  import { LabelList } from '@/components/Sign/type';
  import {
    getCargoDetailApi,
    reqInventoryAddInventory,
    reqInventoryQueryInventoryBatchById,
    reqStorageConfigQueryAllTreeWithCargoPosition,
  } from '@/services';
  import { useCargoInfo } from '@/pages/Manage/StorageManage/hooks';
  import { StorageLevel } from '../types';
  import { cloneDeep, loopSelectableNotValueTree } from '@bmos/utils';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
    }>(),
    {
      rowData: () => ({}),
    },
  );
  const route = useRoute();

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const title = ref<string>(t('新增货品件'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('操作人'),
    },
  ];

  const modalFormRef = ref<any>();

  const request = async () => {
    try {
      await reqInventoryAddInventory(curFormModal.value);
      message.success(t('新增货品件成功'));
      emit('updateTable');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (data: Recordable) => {
    const { receiverId } = data;
    curFormModal.value = {
      ...curFormModal.value,
      operatorId: receiverId,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      signOpen.value = true;
      const { cargoId, batchId, positionId, singleQuantity, singleUnitId, size } = formModal;
      curFormModal.value = {
        cargoId,
        batchId,
        positionId,
        singleQuantity,
        singleUnitId,
        size,
      };
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const { getUnitOptions } = useCargoInfo({
    modalFormRef,
  });

  const formProps: Ref<FormProps> = ref({
    baseColProps: {
      span: 11,
    },
    // 处理日期为YYYY-MM-DD格式
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    schemas: [
      {
        field: 'cargoInfo',
        component: 'Divider',
        label: t('货品信息'),
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
        field: 'cargoName',
        component: 'Input',
        label: t('货品名称'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'mergeCode',
        component: 'Input',
        label: t('货品编码'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'specification',
        component: 'Input',
        label: t('货品规格'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'inventoryBatchNo',
        component: 'Input',
        label: t('货品批号'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'inboundInfo',
        component: 'Divider',
        label: t('货品件'),
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
        field: 'singleQuantity',
        component: 'Input',
        label: t('单件量'),
        required: true,
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
                if (!value) return Promise.reject(t('请输入初始量'));
                // 输入正数
                if (Number(value) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                // 整数部分最多为10位,小数位数最多为9位
                const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'singleUnitId',
        component: 'Select',
        label: t('单位'),
        required: true,
        componentProps: {
          options: [],
          fieldNames: {
            label: 'name',
            value: 'unitId',
          },
        },
        componentSlots: () => {
          return {
            option: ({ slotData }: any) => {
              return (
                <div class='flex-between'>
                  <span>{slotData?.name}</span>
                  <span class='fourth-level-text'>{slotData?.expression}</span>
                </div>
              );
            },
          };
        },
      },
      {
        field: 'size',
        component: 'InputNumber',
        componentProps: {
          min: 1,
          max: 99,
          precision: 0,
          style: {
            width: '100%',
          },
        },
        label: t('入库件数'),
        required: true,
      },
      {
        field: 'positionId',
        component: 'TreeSelect',
        label: t('接收货位'),
        required: true,
        componentProps: {
          fieldNames: {
            value: 'id',
            label: 'name',
          },
          request: async () => {
            const { data } = await reqStorageConfigQueryAllTreeWithCargoPosition();
            return loopSelectableNotValueTree(
              cloneDeep(data) as Record<string, any>[],
              'level.value',
              StorageLevel.POSITION,
            );
          },
          showSearch: true,
          treeNodeFilterProp: 'name',
          dropdownMatchSelectWidth: 340,
        },
      },
    ],
  });

  const getInventoryBatchInfo = async () => {
    try {
      const { id } = route.query;
      if (!id) return;
      // 获取货品批次信息
      const { data } = await reqInventoryQueryInventoryBatchById(id as string);
      modalFormRef.value?.formRef?.setFieldsValue({
        cargoName: data.cargoName,
        mergeCode: data.mergeCode,
        specification: data.specification,
        inventoryBatchNo: data.inventoryBatchNo,
      });
      const { data: inventoryData } = await getCargoDetailApi({
        id: data.cargoId,
      });
      modalFormRef.value?.formRef?.setFormModels({
        cargoId: data.cargoId,
        batchId: data.id,
      });
      const unitOptions = await getUnitOptions(inventoryData.platformMaterialId, inventoryData);
      modalFormRef.value?.formRef?.updateSchema({
        field: 'singleUnitId',
        componentProps: {
          options: unitOptions,
        },
      });
    } catch (error) {}
  };

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        // 获取货品批次信息
        getInventoryBatchInfo();
      }
    },
  );
</script>

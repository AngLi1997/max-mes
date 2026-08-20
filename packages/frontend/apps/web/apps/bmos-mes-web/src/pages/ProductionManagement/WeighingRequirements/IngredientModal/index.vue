<template>
  <NormalModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('配料')"
    :submit="submit"
    wrapClassName="modalSizeLarge"
    @cancelModal="cancelModal">
    <!-- Top Information Section -->
    <Space
      style="
        width: 100%;
        justify-content: space-between;
        align-items: center;
        background: #f5f5f5;
        padding: 16px;
        border-radius: 8px;
        margin-bottom: 16px;
      ">
      <template #split>
        <Divider style="top: 0; height: 44px" type="vertical" />
      </template>
      <Flex vertical :gap="8">
        <div class="header-item-title">{{ t('物料信息') }}:</div>
        <div class="header-item-value">{{ topInfo.materialName }}</div>
      </Flex>
      <Flex vertical :gap="8">
        <div class="header-item-title">{{ t('需求目标量') }}:</div>
        <div class="header-item-value">
          <span>{{ topInfo.theoreticalAmount }} {{ topInfo.unit }}</span>
          <Tag :color="enough ? '#59BF78' : '#FF9A2F'" style="margin-left: 8px">
            {{ enough ? t('已满足') : t('未满足') }}
          </Tag>
        </div>
      </Flex>
      <Flex vertical :gap="8">
        <div class="header-item-title">{{ t('已选理论量') }}:</div>
        <div class="header-item-value">{{ selectedTheoreticalAmount }} {{ topInfo.unit }}</div>
      </Flex>
      <Flex vertical :gap="8">
        <div class="header-item-title">{{ t('配料总量') }}:</div>
        <div class="header-item-value">{{ totalIngredientAmount }} {{ topInfo.unit }}</div>
      </Flex>
    </Space>
    <!-- Ingredients Table Section -->
    <BMTable
      ref="tableRef"
      row-key="storageMaterialBatchId"
      :loading="loading"
      :dataSource="tableData"
      :columns="columns"
      :pagination="false"
      :search="false"
      :showToolBar="false"
      :scroll="{ x: 1200, y: 300 }"
      :row-selection="rowSelection"></BMTable>
  </NormalModalForm>
</template>

<script setup lang="ts">
  import { ref, reactive, computed, watch } from 'vue';
  import { BMTable, NormalModalForm, TableColumn } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message, Space, Flex, Tag, Divider } from 'ant-design-vue';
  import type { TableProps } from 'ant-design-vue';
  import {
    reqCalcFormulaQuantity,
    reqQuantityCalculate,
    reqWeighingRequirementsQueryMaterialList,
    reqWeighingRequirementsSave,
    reqWeighingRequirementsValidateSave,
  } from '@/services';
  import { useWarn } from '@/hooks';
  import { addNumber, gtNumber, eqNumber, subtractNumber } from '@bmos/utils';

  defineOptions({
    inheritAttrs: false,
  });

  const loading = ref(false);

  const { warnModal } = useWarn();

  const emit = defineEmits(['ok', 'cancel']);
  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });
  const props = withDefaults(
    defineProps<{
      params: Record<string, any>;
    }>(),
    {
      params: () => ({}),
    },
  );

  const modalFormRef = ref<InstanceType<typeof NormalModalForm>>();

  // Top Information
  const topInfo = reactive({
    materialName: '',
    theoreticalAmount: 0,
    unit: 'KG', // Default unit
  });

  // Table Data and Columns
  interface IngredientItem {
    expiredDate: string;
    factoryBatchNo: string;
    formulaQuantity: string | number;
    hydration: number;
    licenceNo: string;
    materialBatchNo: string;
    materialId: number;
    noHydrationContent: number;
    occupancyQuantity: string | number;
    originalBatchNo: string;
    theoreticalQuantity: string;
    tempTheoreticalQuantity?: string | number;
    producer: string;
    quantity: number;
    remainQuantity: string | number;
    reportNo: string;
    storageMaterialBatchId: number;
    supplier: string;
    unitId: number;
    unitName: string;
  }

  const tableData = ref<IngredientItem[]>([]);

  const columns: TableColumn[] = [
    {
      title: t('物料批号'),
      dataIndex: 'materialBatchNo',
      width: 140,
    },
    {
      title: t('水分%'),
      dataIndex: 'hydration',
      width: 100,
    },
    {
      title: t('含量%'),
      dataIndex: 'noHydrationContent',
      width: 100,
    },
    {
      title: t('库存量'),
      dataIndex: 'quantity',
      width: 100,
    },
    {
      title: t('已占用'),
      dataIndex: 'occupancyQuantity',
      width: 110,
    },
    {
      title: t('配料量'),
      dataIndex: 'formulaQuantity',
      width: 100,
    },
    {
      title: t('剩余量'),
      dataIndex: 'remainQuantity',
      width: 100,
    },
    {
      title: t('单位'),
      dataIndex: 'unitName',
      width: 100,
    },
    {
      title: t('有效期至'),
      dataIndex: 'expiredDate',
      width: 120,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplier',
      width: 110,
    },
    {
      title: t('生产商'),
      dataIndex: 'producer',
      width: 120,
    },
    {
      title: t('原厂批号'),
      dataIndex: 'factoryBatchNo',
      width: 130,
    },
    {
      title: t('原始编码'),
      dataIndex: 'originalBatchNo',
      width: 130,
    },
    {
      title: t('报告单号'),
      dataIndex: 'reportNo',
      width: 130,
    },
    {
      title: t('放行单编号'),
      dataIndex: 'licenceNo',
      width: 130,
    },
  ];

  // Table Row Selection
  const oldSelectedRowKeys = ref<(string | number)[]>([]);
  const selectedRowKeys = ref<(string | number)[]>([]);
  const selectedRows = ref<IngredientItem[]>([]);

  const calcSelectFormulaQuantity = async (keys: (string | number)[], flag = true) => {
    loading.value = true;

    if (oldSelectedRowKeys.value.length > keys.length) {
      const diffKeys = oldSelectedRowKeys.value.filter(key => !keys.includes(key));
      // 将取消勾选的部分数据还原
      for (const item of tableData.value) {
        // 取库存量作为保留小数位数
        const decimalPlaces = item.quantity.toString().split('.')[1]?.length || 0;
        if (diffKeys.includes(item.storageMaterialBatchId)) {
          item.occupancyQuantity = parseFloat(subtractNumber(item.occupancyQuantity, item.formulaQuantity)).toFixed(
            decimalPlaces,
          );
          const { data: useTheoreticalQuantity } = await reqQuantityCalculate({
            formulaMaterialId: props.params.formulaMaterialId,
            hydration: item.hydration,
            noHydrationContent: item.noHydrationContent,
            quantity: gtNumber(item.quantity, item.formulaQuantity) ? item.formulaQuantity : item.quantity,
          });
          item.theoreticalQuantity = parseFloat(addNumber(item.theoreticalQuantity, useTheoreticalQuantity)).toFixed(
            decimalPlaces,
          );
          item.remainQuantity = '';
          item.formulaQuantity = '';
          item.tempTheoreticalQuantity = undefined;
        }
      }
    } else {
      // 将勾选的部分tableData数据更新
      const diffKeys = keys.filter(key => !oldSelectedRowKeys.value.includes(key));
      let curr = subtractNumber(topInfo.theoreticalAmount, selectedTheoreticalAmount.value);

      for (const item of tableData.value) {
        // 取库存量作为保留小数位数
        const decimalPlaces = item.quantity.toString().split('.')[1]?.length || 0;
        if (diffKeys.includes(item.storageMaterialBatchId)) {
          try {
            const { data: totalFormulaQuantity } = await reqCalcFormulaQuantity({
              formulaMaterialId: props.params.formulaMaterialId,
              availableQuantity: parseFloat(subtractNumber(item.quantity, item.occupancyQuantity)).toFixed(
                decimalPlaces,
              ),
              hydration: item.hydration,
              noHydrationContent: item.noHydrationContent,
              requirementQuantity: curr,
            });

            item.formulaQuantity = '';
            if (gtNumber(item.theoreticalQuantity, curr)) {
              // !item.tempTheoreticalQuantity && (item.tempTheoreticalQuantity = curr);
              item.tempTheoreticalQuantity = curr;
              const { data } = await reqCalcFormulaQuantity({
                formulaMaterialId: props.params.formulaMaterialId,
                availableQuantity: parseFloat(subtractNumber(item.quantity, item.occupancyQuantity)).toFixed(
                  decimalPlaces,
                ),
                hydration: item.hydration,
                noHydrationContent: item.noHydrationContent,
                requirementQuantity: item.tempTheoreticalQuantity,
              });
              if (flag) {
                item.occupancyQuantity = parseFloat(addNumber(item.occupancyQuantity, data)).toFixed(decimalPlaces);
              }
              item.theoreticalQuantity = parseFloat(subtractNumber(item.theoreticalQuantity, curr)).toFixed(
                decimalPlaces,
              );
              item.formulaQuantity = parseFloat(addNumber('0', data)).toFixed(decimalPlaces);
            } else {
              if (flag) {
                item.occupancyQuantity = parseFloat(addNumber(item.occupancyQuantity, totalFormulaQuantity)).toFixed(
                  decimalPlaces,
                );
              }
              item.tempTheoreticalQuantity = item.theoreticalQuantity;
              item.theoreticalQuantity = '0';
              item.formulaQuantity = totalFormulaQuantity;
            }
            item.remainQuantity = parseFloat(subtractNumber(item.quantity, item.occupancyQuantity)).toFixed(
              decimalPlaces,
            );
          } catch (error: any) {
            error.message && message.error(error.message);
          }
        }
      }
    }

    loading.value = false;
  };

  const rowSelection = computed<TableProps['rowSelection']>(() => {
    return {
      hideSelectAll: true,
      getCheckboxProps: record => ({
        disabled: !selectedRowKeys.value.includes(record.storageMaterialBatchId) && enough.value, // Column configuration not to be checked
      }),
      selectedRowKeys: selectedRowKeys.value,
      onChange: async (keys: (string | number)[], selectRows: any[]) => {
        // recalculateAmounts();
        await calcSelectFormulaQuantity(keys);
        oldSelectedRowKeys.value = keys;
        selectedRowKeys.value = keys;
        selectedRows.value = selectRows;
      },
    };
  });

  const selectedTheoreticalAmount = computed(() => {
    return (
      selectedRows.value.reduce((total, item) => {
        return addNumber(total, item.tempTheoreticalQuantity ?? item.theoreticalQuantity);
      }, '0') || '0'
    );
  });

  const totalIngredientAmount = computed(() => {
    return (
      selectedRows.value.reduce((total, item) => {
        return addNumber(total, item.formulaQuantity);
      }, '0') || '0'
    );
  });

  const enough = computed(() => {
    return parseFloat(selectedTheoreticalAmount.value) >= topInfo.theoreticalAmount;
  });

  watch(
    () => open.value,
    async (isOpen: boolean) => {
      try {
        oldSelectedRowKeys.value = [];
        if (isOpen) {
          loading.value = true;
          // Populate topInfo from props.params
          topInfo.materialName = props.params.materialName || '-';
          topInfo.theoreticalAmount = parseFloat(props.params.requirementQuantity) || 0;
          topInfo.unit = props.params.unit || 'KG';

          // Mock fetching data for the table based on params
          // In a real app, this would be an API call
          const { data } = await reqWeighingRequirementsQueryMaterialList({
            formulaMaterialId: props.params.formulaMaterialId,
          });
          selectedRowKeys.value = props.params.batches.map((item: any) => item.storageMaterialBatchId);
          selectedRows.value =
            props.params.batches.map((item: any) => {
              return {
                ...item,
                tempTheoreticalQuantity: item.theoreticalQuantity,
              };
            }) || [];
          oldSelectedRowKeys.value = selectedRowKeys.value;
          tableData.value =
            data.filter(
              (item: any) =>
                selectedRowKeys.value.includes(item.storageMaterialBatchId) ||
                parseFloat(item.quantity) !== parseFloat(item.occupancyQuantity),
            ) || [];
          tableData.value.forEach((item: any) => {
            const currRow = selectedRows.value.find(
              (selectedItem: any) => selectedItem.storageMaterialBatchId === item.storageMaterialBatchId,
            );
            if (currRow?.storageMaterialBatchId) {
              const decimalPlaces = item.quantity.toString().split('.')[1]?.length || 0;
              item.formulaQuantity = currRow.formulaQuantity;
              item.tempTheoreticalQuantity = currRow.theoreticalQuantity;
              item.remainQuantity = parseFloat(subtractNumber(item.quantity, item.occupancyQuantity)).toFixed(
                decimalPlaces,
              );
            }
          });
          await calcSelectFormulaQuantity(selectedRowKeys.value, false);
        } else {
          // Reset when modal closes
          oldSelectedRowKeys.value = [];
          tableData.value = [];
          selectedRows.value = [];
          selectedRowKeys.value = [];
          topInfo.materialName = '';
          topInfo.theoreticalAmount = 0;
        }
      } catch (error: any) {
        error.message && message.error(error.message);
      } finally {
        loading.value = false;
      }
    },
  );

  const route = useRoute();

  const submit = async () => {
    if (!enough.value) {
      message.error(t('配料不满足需求量'));
      return;
    }
    const batches = selectedRows.value.filter((item: any) => !eqNumber(item.formulaQuantity, '0'));
    if (batches.length === 0) {
      message.error(t('配料总量不能为0'));
      return;
    }
    const saveFn = async () => {
      try {
        loading.value = true;
        // Replace with actual API call: await actualSubmitApi(payload);
        await reqWeighingRequirementsSave({
          key: props.params.key,
          groupId: route.query.id,
          formulaMaterialId: props.params.formulaMaterialId,
          batches,
        });
        message.success(t('配料操作成功'));
        open.value = false;
        emit('ok');
      } catch (error: any) {
        error.message && message.error(error.message);
      } finally {
        loading.value = false;
      }
    };
    // 循环遍历,判断是否有超出库存的情况
    const { data } = await reqWeighingRequirementsValidateSave({
      key: props.params.key,
      groupId: route.query.id,
      formulaMaterialId: props.params.formulaMaterialId,
      batches,
    });
    if (!data || data.length === 0) {
      await saveFn();
      return;
    }
    warnModal(t('物料批号：{}配料量超出库存可用量，请重新确认配料信息。').replace('{}', data.join(',')), {
      title: t('物料批次库存预警'),
      cancelText: t('取消'),
      okText: t('继续保存'),
      async onOk() {
        try {
          await saveFn();
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };

  const cancelModal = () => {
    open.value = false;
    emit('cancel');
  };
</script>

<style scoped lang="less">
  .header-item {
    &-title {
      width: 100%;
      color: #6c6e73;
      font-size: 14px;
    }
    &-value {
      height: 18px;
      color: #242526;
      font-size: 14px;
    }
  }
</style>

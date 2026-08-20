<!-- 预警管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['auditId']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: { warningType, checked },
      },
    ]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[loadData as DataRequestFn]"
    :columns="[expiryColumns]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="warningType" :options="options" @change="changeType" />
    </template>
    <template #tableHeaderToolbar0>
      <div v-if="warningType !== WarningTypeEnum.SUPPLIER_EXPIRY_WARNING" class="title-flex">
        <Switch v-model:checked="checked"></Switch>
        <span>
          {{
            warningType === WarningTypeEnum.MATERIAL_EXPIRY_WARNING
              ? t('只看剩余日期≥0的数据')
              : t('只看库存大于0的数据')
          }}
        </span>
      </div>
    </template>
  </BMPageComponent>
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getMaterialWarnExpiredPage, getMaterialWarnInventoryPage, getMaterialWarnSupplierPage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { paginationBig } from '@/utils';
  import { WarningTypeEnum } from '@/types';

  defineOptions({
    name: 'MaterialEarlyWarning',
    inheritAttrs: false,
  });

  const warningType = ref<WarningTypeEnum>(WarningTypeEnum.MATERIAL_EXPIRY_WARNING);

  const checked = ref(true);

  const options = [
    { label: t('物料到期预警'), value: WarningTypeEnum.MATERIAL_EXPIRY_WARNING },
    { label: t('物料最低库存预警'), value: WarningTypeEnum.MATERIAL_LOW_STOCK_WARNING },
    { label: t('供应商到期预警'), value: WarningTypeEnum.SUPPLIER_EXPIRY_WARNING },
  ];

  const {
    pageRef,
    setTableColumns,
    setQueryParams,
    expiryColumns,
    lowStockColumns,
    supplierExpiryColumns,
    formFirstProps,
    remarkModalOpen,
    remarkDetails,
  } = useTable();

  const changeType = () => {
    switch (warningType.value) {
      case WarningTypeEnum.MATERIAL_EXPIRY_WARNING:
        setQueryParams(true);
        setTableColumns(expiryColumns);
        break;
      case WarningTypeEnum.MATERIAL_LOW_STOCK_WARNING:
        setQueryParams(true);
        setTableColumns(lowStockColumns);
        break;
      case WarningTypeEnum.SUPPLIER_EXPIRY_WARNING:
        setQueryParams(false);
        setTableColumns(supplierExpiryColumns);
        break;
      default:
        setQueryParams(true);
        setTableColumns(expiryColumns);
        break;
    }
  };

  const loadData = async (params: any) => {
    const data = {
      ...params,
      warningType: undefined,
      checked: undefined,
    };
    switch (warningType.value) {
      case WarningTypeEnum.MATERIAL_EXPIRY_WARNING:
        return getMaterialWarnExpiredPage({ ...data, existsExpired: checked.value });
      case WarningTypeEnum.MATERIAL_LOW_STOCK_WARNING:
        return getMaterialWarnInventoryPage({ ...data, existsInventory: checked.value });
      case WarningTypeEnum.SUPPLIER_EXPIRY_WARNING:
        return getMaterialWarnSupplierPage(data);
      default:
        return getMaterialWarnExpiredPage({ ...data, existsExpired: checked.value });
    }
  };
</script>

<style lang="less" scoped>
  .title-flex {
    display: flex;
    align-items: center;
    gap: 8px;
  }
</style>

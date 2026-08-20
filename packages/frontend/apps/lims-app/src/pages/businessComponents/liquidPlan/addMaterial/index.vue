<template>
  <BMBasicPage
    :title="t('添加物料批次')"
    :loading="loading"
    @left-click="toBack"
    @cancel="toBack"
    @confirm="confirmPage"
  >
    <view class="content">
      <BMDataInfoDisplay
        :basic-items="[
          {
            label: t('物料编码'),
            field: 'materialMergeCode',
          },
          {
            label: t('浓度参数'),
            field: 'consistenceParamName',
          },
          {
            label: t('目标浓度'),
            field: 'targetConcentration',
          },
        ]"
        :info-data="activeInfo"
      />
      <view class="table_box">
        <BMTable
          ref="tableRef"
          align="left"
          v-bind="tableProps"
          @selection-change="selectionChange"
        />
      </view>
    </view>
    <BMModal
      v-model="openDetailFlag"
      :title="t('物料批次详情')"
      size="medium"
      @cancel="openDetailFlag = false"
      @confirm="openDetailFlag = false"
    >
      <BMInfoDisplay
        :is-show-title="false"
        :basic-items="basicItemsData"
        :info-data="showDetailData"
        is-show-one
      />
    </BMModal>
  </BMBasicPage>
</template>

<script setup>
import { boundMaterialBatchApi } from '@/api';
import {
  BMBasicPage,
  BMDataInfoDisplay,
  BMInfoDisplay,
  BMModal,
  BMTable,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';
import {
  activeInfo,
  infoData,
  satisfied,
} from '../hooks/dataHooks';
import { addUseTable } from './hooks/table.jsx';

const emit = defineEmits(['cancel', 'config']);
const openDetailFlag = ref(false);
const loading = ref(false);
const queryInfo = ref({});
const {
  tableRef,
  tableProps,
  selectionChange,
  getTableData,
  selectData,
  showDetailData,
  basicItemsData,
} = addUseTable(openDetailFlag, infoData, activeInfo, satisfied);

const confirmPage = async () => {
  try {
    if (!satisfied.value) {
      uni.showToast({
        title: t('目标浓度未满足，无法完成物料配液'),
        icon: 'none',
      });
      return;
    }
    loading.value = true;
    const params = {
      formulaMaterialId: activeInfo.value.id,
      materialBatchList: selectData.value,
      preparationPlanId: infoData.value.id,
    };
    await boundMaterialBatchApi(params);
    emit('config');
  }
  finally {
    loading.value = false;
  }
};

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  getTableData();
});

// 返回
const toBack = () => {
  emit('cancel');
};
</script>

<style lang="scss" scoped>
  .content {
  height: calc(100% - 1rpx);
  border-bottom: 1px solid #e1e3e5;
  .table_box {
    margin-top: 9.38rpx;
    height: calc(100% - 52rpx);
  }
}
</style>

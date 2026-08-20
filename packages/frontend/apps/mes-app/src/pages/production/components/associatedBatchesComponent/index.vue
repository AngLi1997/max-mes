<template>
  <view class="table-box">
    <BMTable ref="tableRef" v-bind="tableProps" />
    <BMModal
      v-model="openModal"
      size="large"
      :default-padding="false"
      @cancel="chooseCancel"
      @confirm="chooseConfirm"
    >
      <template #title>
        <view class="title-box">
          <wd-segmented
            v-model:value="currentSegmented"
            size="small"
            :options="[
              {
                label: t('计划批次'),
                value: '2',
              },
              {
                label: t('关联批次'),
                value: '0',
              },
              {
                label: t('历史批次'),
                value: '1',
              },
            ]"
            @change="segmentChange"
          >
            <template #label="{ option }">{{ option.label }}</template>
          </wd-segmented>
        </view>
      </template>

      <view class="check-container">
        <view v-if="currentSegmented === '0' || currentSegmented === '2'">
          <BMInputSearch v-model="searchValue" :placeholder="t('生产批号')" />
          <scroll-view
            v-if="options.length"
            scroll-y="auto"
            class="checkbox-box-search"
          >
            <wd-cell-group v-if="showOptions.length" border>
              <wd-checkbox-group
                v-model="checkBoxValue"
                shape="square"
                @change="checkChange1"
              >
                <wd-cell
                  v-for="(option, index) in showOptions"
                  :key="option.id"
                  :title="option.batchNo"
                  center
                  clickable
                  @click="handleCheck(index)"
                >
                  <view @click.stop="() => {}">
                    <wd-checkbox
                      :ref="setItemRef"
                      shape="square"
                      :model-value="option.id"
                    />
                  </view>
                </wd-cell>
              </wd-checkbox-group>
            </wd-cell-group>
            <view v-else>
              <BMNoData type="emptySearch" :text="t('暂无搜索结果')" />
            </view>
          </scroll-view>
          <view v-else>
            <BMNoData type="emptyData" :text="currentSegmented === '0'?t('暂无关联批次'):t('暂无计划批次')" />
          </view>
        </view>
        <view v-else>
          <BMInputSearch
            v-model="searchValue"
            :placeholder="t('输入精确的生产批次号')"
            @search="listSearch"
          />
          <scroll-view
            v-if="historyOptions.length || isResult"
            scroll-y="auto"
            class="checkbox-box-search"
          >
            <wd-cell-group v-if="historyShowOptions.length" border>
              <wd-checkbox-group
                v-model="historyCheckBoxValue"
                shape="square"
                @change="checkChange2"
              >
                <wd-cell
                  v-for="(option, index) in historyShowOptions"
                  :key="option.id"
                  :title="option.batchNo"
                  center
                  clickable
                  @click="handleCheck(index)"
                >
                  <view @click.stop="() => {}">
                    <wd-checkbox
                      :ref="setItemRef"
                      shape="square"
                      :model-value="option.id"
                    />
                  </view>
                </wd-cell>
              </wd-checkbox-group>
            </wd-cell-group>
            <view v-else>
              <BMNoData
                type="emptySearch"
                :text="t('暂无搜索结果')"
                :sub-text="t('请输入精确的生产批号')"
              />
            </view>
          </scroll-view>
          <view v-else>
            <BMNoData type="emptyData" :text="t('请精确搜索生产批次号')" />
          </view>
        </view>
      </view>
    </BMModal>
  </view>
</template>

<script setup lang="jsx">
  import { t } from '@/utils/useBmosI18n.js';
  import { ref, reactive, watch, computed } from 'vue';
  import WdIcon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

  import { BMTable, BMModal, BMInputSearch, BMNoData } from '@/BMComponents/index.js';
  import { getProductionInstructionBatchListApi } from '@/api';
  const props = defineProps({
    dataList: {
      type: Array,
      required: true,
      default: () => []
    }
  });
  const emit = defineEmits(['update:dataList']);
  const openModal = ref(false);
  const currentRow = ref(null);
  const currentSegmented = ref('0');
  const options0 = ref([]);
  const options2 = ref([]);
  const searchValue = ref('');
  const checkBoxValue = ref([]);
  const historyCheckBoxValue = ref([]);
  const isResult = ref(false);

  const checkData = ref([]);
  const checkHistoryData = ref([]);

  const options = computed(() => {
    if (currentSegmented.value === '2') {
      return options2.value;
    }
    return options0.value;
  });

  const showOptions = computed(() => {
    if (searchValue.value === '') {
      return options.value;
    }
    return options.value.filter((option) => {
      return option.batchNo.includes(searchValue.value);
    });
  });

  const historyOptions = computed(() => {
    return checkHistoryData.value.map((item) => {
      return {
        id: item.planId,
        batchNo: item.planBatchNo
      };
    });
  });

  const historyShowOptions = computed(() => {
    if (isResult.value) {
      return options.value;
    }
    return historyOptions.value;
  });

  const itemRefs = ref([]);

  const getShowName = (row) => {
    if (row.relationBatchList.length === 0) {
      return '';
    }
    return row.relationBatchList.map((item) => item.planBatchNo).join(',');
  };

  // 设置ref的函数，用于收集DOM元素
  const setItemRef = (el) => {
    if (el) {
      itemRefs.value.push(el);
    }
  };
  const handleCheck = (index) => {
    const item = itemRefs.value[index];
    item && item.toggle();
  };

  const checkChange1 = () => {
    const data = checkBoxValue.value;
    checkData.value = [];
    data.forEach((value) => {
      const result = [...options0.value, ...options2.value].find(item => item.id === value);
      if (result) {
        checkData.value.push({
          planBatchNo: result.batchNo,
          planId: result.id,
          related: false
        });
      }
    });
  };

  const checkChange2 = () => {
    const data = historyCheckBoxValue.value;
    checkHistoryData.value = checkHistoryData.value.filter(item => {
      if (data.includes(item.planId)) {
        return true;
      } else {
        return false;
      }
    });
    data.forEach((value) => {
      const result = options.value.find(item => item.id === value);
      if (result && checkHistoryData.value.find(item => item.planId === result.id) === undefined) {
        checkHistoryData.value.push({
          planBatchNo: result.batchNo,
          planId: result.id,
          related: true
        });
      }
    });
  };
  const chooseRelationBatch = (row) => {
    currentRow.value = row;
    openModal.value = true;
    searchValue.value = '';
    isResult.value = false;

    checkBoxValue.value = [];
    historyCheckBoxValue.value = [];
    checkData.value = [];
    checkHistoryData.value = [];

    // 构造回显的数据
    currentRow.value.relationBatchList.forEach((item) => {
      if (item.related) {
        historyCheckBoxValue.value.push(item.planId);
        checkHistoryData.value.push(item);
      } else {
        checkBoxValue.value.push(item.planId);
        checkData.value.push(item);
      }
    });
    getProductionInstructionBatchList();
  };

  const chooseConfirm = () => {
    currentRow.value.relationBatchList = [...checkData.value, ...checkHistoryData.value];
    emit('update:dataList', props.dataList);
    openModal.value = false;
    currentSegmented.value = '0';
  };

  const chooseCancel = () => {
    openModal.value = false;
    currentSegmented.value = '0';
  };

  const tableRef = ref();
  // table数据
  const tableProps = reactive({
    noDataText: t('暂无关联'),
    pagination: false,
    data: [],
    border: true,
    showNoData: true,
    tableColProps: [
      {
        label: t('关联工艺'),
        prop: 'processName'
      }, {
        label: t('关联批次'),
        prop: '',
        customRender: ({ row }) => {
          return (<view class='select-box' onClick={() => chooseRelationBatch(row)}>
      <view class='bmos-ellipsis-1'>{ getShowName(row) }</view>
      <WdIcon
  name="jiantou-you"
  size="14.06rpx"
  color="#2871ff"
  style="margin-right: 9.38rpx"
  class-prefix="bmos-app-icon"
/>
  </view>);
        }
      }
    ]
  });

  // 获取关联/历史批次列表
  const getProductionInstructionBatchList = async() => {
    if (currentRow.value === null) {
      return;
    }
    const res = await getProductionInstructionBatchListApi({
      processId: currentRow.value.processId,
      relation: currentSegmented.value === '1' ? 'TRUE' : 'FALSE',
      batchNo: currentSegmented.value === '1' ? searchValue.value : ''
    });
    options0.value = res.data;
    options2.value = [];
    currentRow.value.relationBatchList.forEach((item) => {
      if (!item.related) {
        const index = res.data.findIndex((option) => option.id === item.planId);
        if (index === -1) {
          options2.value.push({
            id: item.planId,
            batchNo: item.planBatchNo
          });
        }
      }
    });
  };

  const segmentChange = () => {
    searchValue.value = '';
    isResult.value = false;
    getProductionInstructionBatchList();
  };

  const listSearch = async() => {
    await getProductionInstructionBatchList();
    if (searchValue.value === '') {
      isResult.value = false;
      return;
    }
    isResult.value = true;
  };

  watch(() => props.dataList, (newVal) => {
    tableProps.data = newVal;
  }, { immediate: true });

</script>

<style lang="scss" scoped>
.table-box {
  height: 357.42rpx;
  :deep(.wd-input) {
    border: 0;
  }
  :deep(.select-box) {
    display: flex;
    justify-content: space-between;
  }
}
.title-box {
  padding: 11.72rpx 9.38rpx;
}
.check-container {
  padding: 0 9.38rpx;
  box-sizing: border-box;
  height: 283.59rpx;

  .checkbox-box-search {
    height: 235.55rpx;
    margin-top: 9.38rpx;
  }
  :deep(.wd-cell__left) {
    max-width: calc(100% - 58.59rpx);
    .wd-cell__label {
      white-space: normal;
    }
  }
  :deep(.wd-cell__right) {
    flex: unset;
  }
}
</style>

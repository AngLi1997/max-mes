<template>
  <view class="history-content">
    <view class="history-title">
      <view class="data_box">
        <BMFormDatePicker
          v-model="showDate" :title="t('采集时间')" format-date="yyyy-MM-dd HH:mm"
          :max-date="new Date(getCurrentTime())" @change="confirmDatePopup"
        />
      </view>
      <wd-segmented
        v-model:value="segmentedValue" :options="segmentedOptions" custom-class="history-segmented"
        @change="segmentedChange"
      >
        <template #label="{ option }">
          {{ option.label }}
        </template>
      </wd-segmented>
    </view>
    <view class="table-container">
      <BMTable ref="tableRef" v-bind="tableProps" />
      <!-- <BmosNoData
        v-if="dataList.length === 0"
        type="emptyProductionBefore"
        :text="t('请先选择采集时间')"
        :position="false"
      /> -->
    </view>
    <!-- 数据选择 -->
    <EquipmentData
      v-model="showEquipmentDataModel" :row-data="selectRowData" :equipment-id="equipmentId"
      :date-value="dateValue" :segmented-value="segmentedValue" @confirm="equipmentDataConfirm"
    />
  </view>
</template>

<script setup lang="jsx">
import {
  BMFormDatePicker,
  BMFormSelect,
  BMIcon,
  BMTable,
} from '@/BMComponents';
import {
  getCurrentTime,
  timestampToTime,
} from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdInput from 'wot-design-uni/components/wd-input/wd-input.vue';
import EquipmentData from './EquipmentData.vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  equipmentId: {
    type: String,
    default: '',
  },
  componentId: {
    type: String,
    default: '',
  },
  historyData: {
    type: Object,
    default: () => { },
  },
  data: {
    type: Object,
    default: () => { },
  },
  clickRow: {
    type: String,
    default: '',
  },
  historyTime: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['getData', 'update:historyTime']);
const showDate = ref('');
watch(
  () => showDate.value,
  (val) => {
    emit('update:historyTime', val);
  },
);
const { showNotify } = useNotify();

const dateValue = ref('');
const selectRowData = ref(null);
const tableRef = ref();

// 表格数据
const dataList = ref([]);
const options = ref({});

watch(
  () => props.historyData,
  (val) => {
    if (val) {
      tableRef.value.tableData.forEach((row) => {
        if (row.type === 2) {
          // 手动采集不回显值
          return;
        }
        const dataList = val[row.dataPointName];
        if (dataList) {
          const data = dataList[dataList.length - 1];
          row.dataPointValue = data.val;
          row.timeStamp = data.time ? timestampToTime(data.time) : '-';
        }
        else {
          row.dataPointValue = '-';
          row.timeStamp = '-';
        }
      });
    }
    else {
      tableRef.value.tableData.forEach((row) => {
        if (row.type === 1) {
          row.dataPointValue = '-';
          row.timeStamp = '-';
        }
      });
    }
  },
  { deep: true },
);

// 分段选择
const segmentedOptions = [
  {
    label: t('向前一分钟'),
    value: 'prevMinute',
  },
  {
    label: t('向后一分钟'),
    value: 'nextMinute',
  },
];
const saveDate = ref();

const saveBeginTime = ref('');
const saveEndTime = ref('');
// 分段选择值
const segmentedValue = ref('prevMinute');

const getOptionsData = () => {
  tableRef.value.tableData.forEach((row) => {
    emit('getData', saveBeginTime.value, saveEndTime.value, row.dataPointName, row.value);
  });
};
// 切换采集方式
const typeChange = (row) => {
  row.dataPointValue = '';
  row.timeStamp = '';
  emit('getData', saveBeginTime.value, saveEndTime.value, row.dataPointName, row.value);
};
// 确认日期
const confirmDatePopup = (date) => {
  dateValue.value = timestampToTime(date);
  saveDate.value = date;
  let beginTime = 0;
  let endTime = 0;
  if (segmentedValue.value === 'prevMinute') {
    // 向前一分钟
    endTime = date;
    const currentDate = new Date(date);
    currentDate.setMinutes(currentDate.getMinutes() - 1);
    beginTime = currentDate.getTime();
  }
  else {
    // 向后一分钟
    const now = new Date();
    const difference = now - saveDate.value;
    if (difference <= 60000) {
      showNotify({
        type: 'warning',
        message: t('不能查询该时间向后一分钟数据'),
      });
      // 清空数据
      dataList.value = [];
      options.value = {};
      return;
    }
    beginTime = date;
    const currentDate = new Date(date);
    currentDate.setMinutes(currentDate.getMinutes() + 1);
    endTime = currentDate.getTime();
  }
  saveBeginTime.value = beginTime;
  saveEndTime.value = endTime;
  dataList.value = props.data.map((item) => {
    item.dataPointValue = '';
    item.type = 1;
    item.timeStamp = '';
    return item;
  });
  tableRef.value.tableData = dataList.value;
  getOptionsData();
};

// 分段选择改变
const segmentedChange = (value) => {
  if (!saveDate.value) {
    showNotify({
      type: 'warning',
      message: t('请选择时间'),
    });
    return;
  }
  if (value.value === 'nextMinute') {
    const now = new Date();
    const difference = now - saveDate.value;
    if (difference <= 60000) {
      showNotify({
        type: 'warning',
        message: t('不能查询该时间向后一分钟数据'),
      });
      // 清空数据
      dataList.value = [];
      options.value = [];
      return;
    }
  }
  confirmDatePopup(saveDate.value);
};

// 选择设备数据
const showEquipmentDataModel = ref(false);
const equipmentDataConfirm = (selectValue, selectedData) => {
  dataList.value = dataList.value.map((item) => {
    if (item.id === selectRowData.value.id) {
      item.selectValue = selectedData.value;
      item.equipmentAcquisitionPoint = selectedData;
    }
    return item;
  });
  showEquipmentDataModel.value = false;
};

const tableProps = reactive({
  pagination: false,
  border: false,
  showNoData: true,
  noDataText: t('请先选择采集时间'),
  noDataType: 'emptyProductionBefore',
  tableColProps: [
    {
      prop: 'showName',
      label: t('设备数据'),
      width: 200,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'selectPoint',
      label: t('采集点匹配'),
      width: 200,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        return (
          <BMFormSelect
            v-model={row.type}
            title={t('选择采集方式')}
            options={[
              {
                label: t('设备采集'),
                value: 1,
              },
              {
                label: t('手动录入'),
                value: 2,
              },
            ]}
            onChange={() => typeChange(row)}
          />
        );
      },
    },
    {
      prop: 'selectValue',
      label: t('数据值'),
      width: 200,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        if (row.type === 2) {
          return (
            <WdInput
              v-model={row.dataPointValue}
              no-border
              type="number"
              custom-class="select-point-input"
              use-suffix-slot
            >
              {{
                suffix: () => (
                  <BMIcon
                    name="kebianji"
                    size="11.72rpx"
                    color="#2871FF"
                  />
                ),
              }}
            </WdInput>
          );
        }
        else {
          return (
            <view>{row.dataPointValue}</view>
          );
        }
      },
    },
    {
      prop: 'time',
      label: t('数采时间'),
      width: 200,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        if (row.type === 2) {
          return <text>-</text>;
        }
        else {
          return <text>{row.timeStamp || ''}</text>;
        }
      },
    },
  ],
});
</script>

<style lang="scss" scoped>
:deep(.select-point-input) {
  .wd-input__body {
    border-bottom: none;
  }
}

:deep(.history-input) {
  .wd-input__body {
    border-bottom: none;
  }
}

:deep(.wd-input.is-not-empty:not(.is-disabled)::after) {
  background-color: none;
}

:deep(.bmos-select .right-box) {
  z-index: 1 !important;
}

.history-content {
  .history-title {
    display: flex;
    align-items: center;

    .history-input {
      width: 234.38rpx;
      height: 37.5rpx;
      border: 1px solid #e1e3e5;
      display: flex;
      align-items: center;
      padding: 11.13rpx;
      box-sizing: border-box;
      border-radius: 4.69rpx;
      margin-right: 15.82rpx;

      .wd-input__body {
        border-bottom: none;
      }
    }

    .history-segmented {
      width: 187.5rpx;
      height: 28.13rpx;
      border-radius: 58.59rpx;
      background-color: #edeff2;

      :deep(.wd-segmented__item) {
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 58.59rpx;
        color: #6c6e73;
      }

      :deep(.is-active) {
        color: #2871ff;
      }

      :deep(.wd-segmented__item--active) {
        background-color: none;
        border-radius: 58.59rpx;
      }
    }

    .data_box {
      border: 0.94rpx solid #e1e3e5;
      margin-right: 14.06rpx;
      border-radius: 4.69rpx;
    }
  }

  .table-container {
    padding: 9.38rpx 0 0;

    :deep(.wd-input) {
      background-color: transparent;
    }

    :deep(.right-box .bmos-app-icon::before) {
      color: #2871ff !important;
    }
  }
}

:deep(.wd-table__cell) {
  overflow: hidden;
}

:deep(.wd-input) {
  border: none !important;
}
</style>

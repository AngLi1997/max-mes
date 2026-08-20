<template>
  <view class="scan-table-container">
    <view class="scan_box">
      <BMScanInput
        v-model="materialScan"
        :placeholder="t('物料件号')"
        suffix-icon="search"
        @confirm="onScanConfirm"
        @clicksuffixicon="onScanConfirm"
      />
    </view>
    <view class="table_box" :style="{ height: tableHeight }">
      <BMTable ref="tableRef" v-bind="tableProps" />
    </view>
    <TotalBox :all-num="allNum" :all-weight="allWeight" />
    <!-- 扫码 -->
    <BMScanNew @success="onScanSuccess" />
  </view>
</template>

<script setup lang="jsx">
import {
  getStorageMaterialInfoByNo,
  postMesUnitCalcSumAdapt,
} from '@/api';
import { BMIcon, BMScanInput, BMScanNew, BMTable } from '@/BMComponents';
import TotalBox from '@/pages/inventoryManagement/components/totalBox/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { computed, reactive, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => [],
  },
  tableHeight: {
    type: String,
    default: '100%',
  },
  materialPositionId: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['update:modelValue']);

const { showNotify } = useNotify();

const tableData = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});
const materialScan = ref();

const allNum = computed(() => {
  return tableData.value.length;
});

const allWeight = ref('-');
const onScanConfirm = () => {
  if (materialScan.value) {
    handleScan(materialScan.value);
  }
};
const onScanSuccess = (res) => {
  if (res.startsWith('01') || res.startsWith('02')) {
    const code = res.substring(2);
    code && handleScan(code);
  }
  else {
    showNotify({ type: 'warning', message: t('请扫描的物料件标签') });
  }
};
const handleScan = async (code) => {
  const isPushed = tableData.value.find(item => item.materialNo === code);
  if (isPushed) {
    showNotify({
      type: 'danger',
      message: t('请勿添加重复物料件'),
    });
    return;
  }
  try {
    const { data } = await getStorageMaterialInfoByNo({ materialNo: code });
    data && tableData.value.push({
      ...data,
    });
  }
  catch (error) {
    error?.message
    && showNotify({
      type: 'danger',
      message: t('扫码失败或物料件不存在'),
    });
  }
};
const openDeleteModal = (row, index) => () => {
  tableData.value.splice(index, 1);
};
const tableProps = reactive({
  pagination: false,
  data: [],
  border: false,
  tableColProps: [
    {
      prop: 'button',
      label: '',
      width: 72,
      customRender: ({ row, index }) => {
        return (
          <BMIcon
            name="shanchu"
            color="var(--bmos-color-error)"
            onClick={openDeleteModal(row, index)}
          />
        );
      },
    },
    {
      prop: 'materialName',
      label: t('物料名称'),
      width: 350,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'mergeCode',
      label: t('物料编码'),
      width: 300,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'materialBatchNo',
      label: t('物料批号'),
      width: 400,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'materialNo',
      label: t('物料件号'),
      width: 400,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'quantity',
      label: t('物料量'),
      width: 300,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'unit',
      label: t('单位'),
      width: 150,
      thProps: {
        align: 'left',
      },
    },
  ],
});

const loading = ref(false);
const waiting = ref(false);

// 单位求和
const getUnitSum = async () => {
  if (loading.value) {
    waiting.value = true;
    return;
  }
  if (tableData.value.length === 0) {
    allWeight.value = '-';
  }
  else if (tableData.value.length === 1) {
    allWeight.value = tableData.value[0].quantity + tableData.value[0].unit;
  }
  else {
    try {
      const targetUnitId = tableData.value[0].finalUnitId;
      const list = tableData.value.map((item) => {
        return {
          unitId: item.finalUnitId,
          value: item.quantity,
        };
      });
      loading.value = true;
      const res = await postMesUnitCalcSumAdapt({
        targetUnitId,
        list,
      });
      allWeight.value = res.data.value ? `${res.data.value}${tableData.value[0].unit}` : '-';
    }
    catch (error) {
      allWeight.value = '-';
    }
  }
  loading.value = false;
  if (waiting.value) {
    waiting.value = false;
    getUnitSum();
  }
};

watch(() => tableData.value, (val) => {
  tableProps.data = val;
  // 获取物料总量（单位换算）
  getUnitSum();
}, { deep: true, immediate: true });
</script>

<style lang="scss" scoped>
.scan-table-container {
  height: 100%;
  .scan_box {
    width: 50%;
    margin: 0 0 9.38rpx 50%;
  }
  .table_box {
    margin-bottom: 9.38rpx;
  }
}
</style>

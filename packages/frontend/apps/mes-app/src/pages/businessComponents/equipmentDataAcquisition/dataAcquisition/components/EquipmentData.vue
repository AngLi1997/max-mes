<template>
  <wd-popup
    v-model="open"
    custom-style="width:375.15rpx;height:291.8rpx;border-radius:7.03rpx;"
    :z-index="999"
    @close="open = false"
  >
    <view class="equipment-data-container">
      <view class="title">
        {{ t("设备数据") }}
      </view>
      <view class="select_box">
        <view class="options">
          <wd-radio-group v-model="selectValue" shape="dot">
            <view v-for="item in options" :key="item.timeStamp" class="radio-item">
              <wd-radio :value="item.timeStamp" />
              <text class="value">{{ item.value }}</text>
              <text class="date">{{ item.timeStamp }}</text>
            </view>
          </wd-radio-group>
          <wd-loadmore :state="state" :error-text="t('点击加载更多')" @reload="loadmore" />
        </view>
      </view>
      <view class="button-container">
        <wd-row gutter="16">
          <wd-col :span="12">
            <BmosButton type="default" :text="t('取消')" @click="close" />
          </wd-col>
          <wd-col :span="12">
            <BmosButton type="primary" :text="t('确定')" @click="confirm" />
          </wd-col>
        </wd-row>
      </view>
    </view>
  </wd-popup>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import BmosButton from '@/components/BmosButton/index.vue';
  import { ref, watch, computed, reactive } from 'vue';
  import { reqPlatformEquipmentAcquisitionPointHistoryDataApi } from '@/api';
  import { getAdjacentMinutes, timestampToTime } from '@/utils/time.js';

  const props = defineProps({
    modelValue: {
      type: Boolean,
      default: false
    },
    rowData: {
      type: Object,
      default: () => ({})
    },
    equipmentId: {
      type: String,
      default: ''
    },
    acquisitionPointId: {
      type: String,
      default: ''
    },
    dateValue: {
      type: String,
      default: ''
    },
    segmentedValue: {
      type: String,
      default: ''
    }
  });
  const open = computed({
    get() {
      return props.modelValue;
    },
    set(value) {
      emit('update:modelValue', value);
    }
  });
  const emit = defineEmits(['confirm', 'update:modelValue']);
  const options = ref([]);
  const selectValue = ref('');

  const selectedData = computed(() => {
    return options.value.find((item) => item.timeStamp === selectValue.value);
  });

  // 弹框关闭
  const close = () => {
    emit('update:modelValue', false);
  };
  // 弹框确认
  const confirm = () => {
    emit('confirm', selectValue.value, selectedData.value);
  };

  const state = ref('loading');
  const pagination = reactive({
    pageNum: 1,
    pageSize: 20,
    total: 0
  });
  // 根据设备id获取设备绑定采集项的所有数据
  const getOptions = async() => {
    try {
      const params = {
        equipmentId: props.equipmentId,
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize,
        acquisitionPointId: props.rowData?.equipmentAcquisitionPoint?.acquisitionPointId
      };
      const { beforeDate, afterDate } = getAdjacentMinutes(props.dateValue);
      if (props.segmentedValue === 'prevMinute') {
        params.startTime = beforeDate;
        params.endTime = props.dateValue;
      } else {
        params.startTime = props.dateValue;
        params.endTime = afterDate;
      }
      const { data } = await reqPlatformEquipmentAcquisitionPointHistoryDataApi(params);
      options.value = options.value.concat(data.list?.map((item) => ({
        ...item,
        timeStamp: timestampToTime(item.timeStamp)
      })));
      pagination.total = data.total;
      if (pagination.pageNum * pagination.pageSize >= pagination.total) {
        state.value = 'finished';
      } else {
        state.value = 'error';
      }
    } catch (error) {
      state.value = 'error';
    }
  };

  const loadmore = () => {
    if (pagination.pageNum * pagination.pageSize >= pagination.total) {
      state.value = 'finished';
      return;
    }
    state.value = 'loading';
    pagination.pageNum++;
    getOptions();
  };

  watch(
    () => open.value,
    (value) => {
      if (value) {
        const { selectPointId } = props.rowData;
        if (selectPointId) {
          selectValue.value = selectPointId;
        } else {
          selectValue.value = '';
        }
        options.value = [];
        props.equipmentId && getOptions();
      }
    }
  );
</script>

<style lang="scss" scoped>
.equipment-data-container {
  .title {
    height: 41.03rpx;
    line-height: 41.03rpx;
    font-size: 15.24rpx;
    text-align: center;
  }
  .select_box {
    height: 189.84rpx;
    overflow: auto;
    padding: 0 9.38rpx;
    box-sizing: border-box;
    .radio-item {
      display: flex;
      align-items: center;
      height: 35.16rpx;
      .wd-radio {
        margin-top: 0;
        margin-right: 18.75rpx;
      }
      .name {
        width: 101.95rpx;
        color: #242526;
        font-size: 11.72rpx;
      }
      .value {
        width: 101.95rpx;
        color: #242526;
        font-size: 11.72rpx;
      }
      .date {
        width: 128.91rpx;
        color: #6c6e73;
        font-size: 10.55rpx;
      }
    }
  }
  .button-container {
    position: absolute;
    bottom: 12.31rpx;
    left: 0;
    width: 100%;
    padding: 0 9.38rpx;
    box-sizing: border-box;
  }
}
</style>

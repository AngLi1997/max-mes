<!-- 分层智控 -->
<template>
  <div class="action-container container-base">
    <div class="action-row">
      <span>{{ t('楼层') }}</span>
      <Select v-model:value="floor" size="small" @change="handleChangeFloor">
        <SelectOption v-for="i in 5" :key="i" :value="i">{{ i }}F</SelectOption>
      </Select>
    </div>
    <div class="action-line"></div>
    <div class="action-row">
      <span>{{ t('工序') }}</span>
      <Switch v-model:checked="process" :disabled="disabledSwitch" @change="processChange" />
    </div>
    <div class="action-row">
      <span>{{ t('房间') }}</span>
      <Switch v-model:checked="room" @change="roomChange" />
    </div>
    <div class="action-row">
      <span>{{ t('设备') }}</span>
      <Switch v-model:checked="device" @change="deviceChange" />
    </div>
    <div class="action-row">
      <span>{{ t('监控') }}</span>
      <Switch v-model:checked="monitor" @change="monitorChange" />
    </div>
  </div>
  <!-- <div v-show="room" class="cleanliness-level container-base">
    <img
      :src="images[`/src/assets/baiePng/${currentLng}/clearliness-level.png`]?.default"
      alt=""
      width="100%"
      height="100%" />
  </div> -->
  <!-- 房间信息 -->
  <RoomDetail v-if="showRoomDetail" :modelId="modelId" @close="roomClose"></RoomDetail>
  <!-- 设备信息 -->
  <EquipmentDetail v-if="showEquipmentDetail" :modelId="modelId" @close="equipmentClose"></EquipmentDetail>
  <!-- 实时监控 -->
  <ModalMonitor v-if="showMonitorDetail" :modelId="modelId" @close="monitorClose"></ModalMonitor>
</template>

<script setup lang="ts">
  import RoomDetail from './components/RoomDetail.vue';
  import EquipmentDetail from './components/EquipmentDetail.vue';
  import ModalMonitor from '../ModalMonitor/index.vue';
  import { t } from '@bmos/i18n';
  import { SelectValue } from 'ant-design-vue/es/select';

  // const images = import.meta.glob('@/assets/baiePng/*/*.png', { eager: true });

  const props = defineProps({
    defaultFloor: {
      type: Number,
      default: 1,
    },
  });
  const emit = defineEmits(['sendMessage', 'closeProcess']);

  // 模型id
  const modelId = ref('');
  // 楼层
  const floor = ref(1);
  // 工序
  const process = ref(false);
  // 房间
  const room = ref(false);
  const showRoomDetail = ref(false);
  // 设备
  const device = ref(false);
  const showEquipmentDetail = ref(false);
  // 监控
  const monitor = ref(false);
  const showMonitorDetail = ref(false);

  const closeAllPopup = () => {
    showRoomDetail.value = false;
    showEquipmentDetail.value = false;
    showMonitorDetail.value = false;
    emit('closeProcess');
  };
  // 切换楼层时禁用开关
  const disabledSwitch = computed(() => {
    return [1, 5].includes(floor.value);
  });

  const handleChangeFloor = (val: SelectValue, flag: boolean = true) => {
    //选择2、3、4楼层默认选中工序按钮
    //选择1、5楼层默认选中房间按钮
    process.value = [2, 3, 4].includes(val as number);
    room.value = [1, 5].includes(val as number);
    device.value = false;
    monitor.value = false;
    closeAllPopup();
    if (flag) {
      emit('sendMessage', {
        type: 'changeFloor',
        value: val,
      });
    }
  };
  //  工序
  const processChange = (val: boolean) => {
    if (val) {
      monitor.value = false;
      room.value = false;
      device.value = false;
    }
    closeAllPopup();
    emit('sendMessage', {
      type: 'changeProcess',
      show: val,
      floor: floor.value,
    });
  };
  // 监控
  const monitorChange = (val: boolean) => {
    if (val) {
      process.value = false;
      room.value = false;
      device.value = false;
    }
    closeAllPopup();
    emit('sendMessage', {
      type: 'changeMonitor',
      show: val,
      floor: floor.value,
    });
  };
  // 房间
  const roomChange = (val: boolean) => {
    if (val) {
      process.value = false;
      monitor.value = false;
      device.value = false;
    }
    closeAllPopup();
    emit('sendMessage', {
      type: 'changeRoom',
      show: val,
      floor: floor.value,
    });
  };
  // 设备
  const deviceChange = (val: boolean) => {
    if (val) {
      process.value = false;
      monitor.value = false;
      room.value = false;
    }
    closeAllPopup();
    emit('sendMessage', {
      type: 'changeDevice',
      show: val,
      floor: floor.value,
    });
  };

  // 房间弹窗
  const openRoomDetail = (data: any) => {
    modelId.value = data.id;
    showEquipmentDetail.value = false;
    showMonitorDetail.value = false;
    showRoomDetail.value = data.show;
  };
  // 房间弹窗关闭
  const roomClose = () => {
    showRoomDetail.value = false;
    emit('sendMessage', {
      type: 'closeDetail',
    });
  };

  // 设备弹窗打开
  const openDeviceDetail = (data: any) => {
    modelId.value = data.id;
    showEquipmentDetail.value = data.show;
    showRoomDetail.value = false;
    showMonitorDetail.value = false;
  };

  // 设备弹窗关闭
  const equipmentClose = () => {
    showEquipmentDetail.value = false;
    emit('sendMessage', {
      type: 'closeDetail',
    });
  };

  // 监控弹窗打开
  const openMonitor = (data: any) => {
    modelId.value = data.id;
    showEquipmentDetail.value = false;
    showRoomDetail.value = false;
    showMonitorDetail.value = data.show;
  };

  // 监控弹窗关闭
  const monitorClose = () => {
    showMonitorDetail.value = false;
    emit('sendMessage', {
      type: 'closeDetail',
    });
  };

  defineExpose({
    openRoomDetail,
    openDeviceDetail,
    openMonitor,
  });
  onMounted(() => {
    floor.value = props.defaultFloor;
    handleChangeFloor(floor.value, false);
  });
</script>

<style lang="less" scoped>
  .container-base {
    position: fixed;
    border-radius: 4px;
    padding: 20px;
    box-sizing: border-box;
    left: 35px;
    background: linear-gradient(90deg, rgba(26, 34, 51, 0.4) 0%, rgba(26, 34, 51, 0.7) 70%);
    border-bottom: 1px solid;
    border-image-source: linear-gradient(
      270deg,
      rgba(85, 158, 255, 0) 5.62%,
      rgba(143, 201, 255, 0.4) 48.16%,
      rgba(85, 158, 255, 0) 91.56%
    );
    border-image-slice: 1;
  }
  .action-container {
    width: 200px;
    height: 312px;
    color: #c3d7e5;
    font-size: 14px;
    top: calc(50% - 312px);
    left: 35px;
    :deep(.dc-select-selector) {
      width: 90px !important;
    }
    :deep(.dc-switch-checked:hover:not(.dc-switch-disabled)) {
      background: unset;
    }
    .action-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      min-height: 34px;
    }
    .action-line {
      width: 100%;
      height: 1px;
      margin: 20px 0;
      background-image: url('@/assets/baiePng/action-line.png');
      background-size: 100% 100%;
    }
  }
  .cleanliness-level {
    width: 200px;
    height: 90px;
    top: 478px;
    padding: 0;
    img {
      object-fit: cover;
    }
  }
</style>

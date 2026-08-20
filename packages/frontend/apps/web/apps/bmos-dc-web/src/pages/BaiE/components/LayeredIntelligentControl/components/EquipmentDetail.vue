<template>
  <div class="equipment-detail">
    <ModalTitle :title="t('设备信息')" icon="equipment" @close="close"></ModalTitle>
    <div class="equipment-detail-content">
      <div class="equipment-item-title">
        {{ `${t(equipmentData.name)}${equipmentData.code ? '-' + equipmentData.code : ''}` }}
      </div>
      <div class="equipment-item-info">
        <img
          :src="images[`/src/assets/baiePng/equipment/${equipmentData.img}.${equipmentData.imgType || 'jpg'}`]?.default"
          alt=""
          width="260px"
          height="172px" />
        <div class="equipment-item-right">
          <ModalItem
            :item="equipmentData"
            :fields="[
              {
                label: '工作容积',
                key: 'workingVolume',
              },
              {
                label: '生产厂商',
                key: 'manufacturer',
              },
              {
                label: '所属工艺',
                key: 'process',
              },
            ]"></ModalItem>
        </div>
      </div>
      <div class="equipment-data">
        <div class="equipment-data-title">
          <div class="equipment-data-title-bg"></div>
          <div class="equipment-data-title-text">{{ t('实时数据') }}</div>
        </div>
        <div class="equipment-data-content">
          <div v-for="item in equipmentData.samplingPoint" :key="item.point" class="equipment-data-item">
            <div class="equipment-data-item-title">
              {{ t(item.name) }}
            </div>
            <div class="equipment-data-item-value">
              {{ item.value || '-' }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import ModalTitle from '@/pages/BaiE/components/ModalComponents/ModalTitle.vue';
  import ModalItem from './ModalItem.vue';
  import { t } from '@bmos/i18n';
  import { equipmentMap } from '@/pages/BaiE/consts/equipmentConst';
  import MqttClient from '@/hooks/useMqtt';
  import { getParameter, getPlatformEquipmentMqttAccredit } from '@/services';
  import { cloneDeep } from '@bmos/utils';

  const images = import.meta.glob('@/assets/baiePng/equipment/*', { eager: true });

  const mqttUrl = ref('');
  const mqttConfig = ref({});
  const mqttInstance = ref(null);
  const props = defineProps({
    modelId: {
      type: String,
      default: '',
    },
  });
  const emit = defineEmits(['close']);

  const equipmentData = ref(cloneDeep(equipmentMap.value.get(props.modelId) || {}));
  const getMqttUrl = async () => {
    try {
      const { data } = await getParameter('platform.sys.acquisition-address');
      mqttConfig.value = JSON.parse(data?.value || '{}').hub || {};
      mqttUrl.value = mqttConfig.value?.mqttAddress || '172.30.1.103:8083';
      return Promise.resolve();
    } catch (error) {
      return Promise.reject();
    }
  };
  const myMqttCreate = async () => {
    const { data: userInfo } = await getPlatformEquipmentMqttAccredit();
    mqttInstance.value = new MqttClient(
      `ws://${mqttUrl.value}/ws/dmcMQTT/`,
      ``,
      (topic, data) => {
        equipmentData.value.samplingPoint.forEach(point => {
          if (point.point === data.name) {
            point.value = data.v;
          }
        });
      },
      userInfo,
    );
    // 订阅
    equipmentData.value.samplingPoint.forEach(item => {
      mqttInstance.value.subscribe(`nup/system/tagValue/${item.point}`);
    });
  };

  const close = () => {
    emit('close');
  };

  onMounted(async () => {
    await getMqttUrl();
    myMqttCreate();
  });
  onUnmounted(() => {
    mqttInstance.value?.endMqtt();
    mqttInstance.value = null;
  });
</script>

<style lang="less" scoped>
  .equipment-detail {
    position: fixed;
    top: calc(50% - 312px);
    left: calc(50% - 320px);
    width: 640px;
    .equipment-detail-title {
      width: 100%;
      height: 40px;
    }
    .equipment-detail-content {
      width: 100%;
      height: 476px;
      padding: 16px 20px;
      overflow-y: auto;
      box-sizing: border-box;
      background: rgba(31, 37, 51, 0.9);
      border-width: 0px 1px 1px 1px;
      border-style: solid;
      border-color: #364159;
      border-radius: 0px 0px 8px 8px;
      color: #fff;
      .equipment-item-title {
        height: 34px;
        line-height: 34px;
        width: 100%;
        padding-left: 20px;
        box-sizing: border-box;
        background-image: url('@/assets/baiePng/process-detail-item-title.png');
        background-size: 100% 100%;
        font-weight: 500;
        font-size: 14px;
      }
      .equipment-item-info {
        display: flex;
        justify-content: space-between;
        height: 190px;
        margin: 15px 0 20px;
        .equipment-item-right {
          width: 320px;
        }
      }
      .equipment-data {
        width: 100%;
        height: 22px;
        .equipment-data-title {
          width: 100%;
          height: 100%;
          position: relative;
          .equipment-data-title-bg {
            width: 70px;
            height: 100%;
            background-image: url('@/assets/baiePng/equipment-data-title.png');
            background-size: 100% 100%;
          }
          .equipment-data-title-text {
            width: 100%;
            height: 100%;
            position: absolute;
            left: 17.5px;
            top: 0;
            font-size: 14px;
            color: #fff;
          }
        }
        .equipment-data-content {
          margin: -5px 0 0 -30px;
          display: flex;
          justify-content: start;
          flex-wrap: wrap;
          .equipment-data-item {
            width: 180px;
            height: 70px;
            box-sizing: border-box;
            padding: 13px 0;
            display: flex;
            margin-top: 15px;
            margin-left: 29px;
            flex-direction: column;
            justify-content: space-between;
            align-items: center;
            background: linear-gradient(
              87.3deg,
              rgba(107, 192, 255, 0) 2.25%,
              rgba(102, 179, 255, 0.15) 48.62%,
              rgba(107, 192, 255, 0) 95.17%
            );
            border-bottom: 1px solid;
            border-image-slice: 1;
            border-image-source: linear-gradient(
              90deg,
              rgba(77, 166, 255, 0) 0%,
              rgba(111, 187, 255, 0.2) 36.65%,
              rgba(153, 213, 255, 0.6) 50%,
              rgba(125, 195, 255, 0.2) 66.22%,
              rgba(77, 166, 255, 0) 100%
            );
            .equipment-data-item-title {
              color: rgba(195, 215, 229, 1);
            }
          }
        }
      }
    }
  }
</style>

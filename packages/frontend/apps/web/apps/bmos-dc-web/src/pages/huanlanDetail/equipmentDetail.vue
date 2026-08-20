<template>
  <div class="equipment-container">
    <div class="equipment-detail-popup-content">
      <div class="equipment-detail-popup-content-top">
        <div class="equipment-detail-popup-content-top-left">
          <img
            v-if="equipmentPng === '全自动照蛋机'"
            src="./png/全自动照蛋机-前检.png"
            alt=""
            width="280px"
            height="160px" />
          <img
            v-else-if="equipmentPng === '全自动照蛋机-后检'"
            src="./png/全自动照蛋机-后检.png"
            alt=""
            width="280px"
            height="160px" />
          <img
            v-else-if="equipmentPng === '全自动病毒接种机'"
            src="./png/全自动病毒接种机.png"
            alt=""
            width="280px"
            height="160px" />
          <img
            v-else-if="equipmentPng === '全自动收获机'"
            src="./png/全自动病毒收获机.png"
            alt=""
            width="280px"
            height="160px" />
          <img v-else-if="equipmentPng === '微滤系统'" src="./png/微滤系统.png" alt="" width="280px" height="160px" />
          <img v-else-if="equipmentPng === '灭活罐'" src="./png/灭活罐.png" alt="" width="280px" height="160px" />
        </div>
        <div class="equipment-detail-popup-content-top-right">
          <div class="equipment-detail-popup-content-top-right-title">
            {{ equipmentDetail.code }} - {{ equipmentDetail.name }}
          </div>
          <div class="equipment-detail-popup-content-top-right-status">
            <span :style="{ position: 'absolute', top: '12px', right: '16px', color: colors[equipmentDetail.status] }">
              {{ equipmentDetail.statusName }}
            </span>
          </div>
          <div class="equipment-detail-popup-content-top-right-info">
            <img src="./png/1.png" alt="" width="110px" height="66px" />
            <img src="./png/2.png" alt="" width="110px" height="66px" />
            <img src="./png/3.png" alt="" width="110px" height="66px" />
          </div>
        </div>
      </div>
      <div class="equipment-detail-popup-content-bottom">
        <div class="equipment-detail-popup-content-bottom-left">
          <div class="equipment-detail-popup-content-bottom-left-title">实时数据</div>
          <div class="data-box">
            <div
              v-for="(item, index) in equipmentDetail.dataPropertyList"
              :key="item.dataPointName"
              class="equipment-detail-popup-content-bottom-left-item"
              :class="{
                'equipment-detail-popup-content-bottom-left-item-bg': index % 2 === 0,
              }">
              <span class="equipment-detail-popup-content-bottom-left-item-name">{{ item.acquisitionPointName }}</span>
              <span>{{ item.dataPointValue }}</span>
            </div>
          </div>
        </div>
        <div class="equipment-detail-popup-content-bottom-right">
          <div class="equipment-detail-popup-content-bottom-right-title">
            <div class="equipment-detail-popup-content-bottom-right-title-left">温度</div>
            <div>单位：℃</div>
          </div>
          <div>
            <img src="./png/target.png" width="100%" height="173px" alt="" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { getParameter, getPlatformEquipmentAppInfo } from '@/services';
  import MqttClient from '@/hooks/useMqtt';

  const route = useRoute();
  const equipmentDetail = ref<any>({});
  const mqttMap = new Map();
  const mqttUrl = ref('');

  const equipmentPng = ref<any>('');
  const colors = ref(['#0BE5E5', '#0BE5E5', '#FA9026', '#FFFFFF', '#E53535']);

  const getMqttUrl = async () => {
    try {
      const { data } = await getParameter('platform.sys.acquisition-address');
      const mqttConfig = JSON.parse(data?.value || '{}').supCon;
      mqttUrl.value = mqttConfig?.mqttAddress || '172.30.1.103:8083';
      return Promise.resolve();
    } catch (error) {
      return Promise.reject();
    }
  };

  const myMqttCreate = () => {
    if (!mqttMap.has(equipmentDetail.value.code)) {
      mqttMap.set(
        equipmentDetail.value.code,
        new MqttClient(
          `ws://${mqttUrl?.value}/mqtt`,
          equipmentDetail.value.code || 'rtdvalue/report',
          (topic, data) => {
            data.RTValue.forEach((item: any) => {
              equipmentDetail.value.dataPropertyList.forEach((item1: any) => {
                if (item.name === item1.dataPointName) {
                  item1.dataPointValue = item.value;
                }
              });
            });
          },
          {},
        ),
      );
    }
  };
  onMounted(async () => {
    const query = route.query;
    equipmentPng.value = query.png;
    if (query.id) {
      const res = await getPlatformEquipmentAppInfo(query.id);
      if (res.data) {
        equipmentDetail.value = res.data;
        await getMqttUrl();
        if (equipmentDetail.value.dataPropertyList && equipmentDetail.value.dataPropertyList.length > 0) {
          myMqttCreate();
        }
      }
    }
  });
</script>
<style lang="less">
  .dc-layout {
    background: rgba(0, 0, 0, 0) !important;
  }
  .dc-content {
    background: rgba(0, 0, 0, 0);
    padding: 0 !important;
  }
</style>

<style lang="less" scoped>
  .data-box {
    max-height: 190px;
    overflow-y: auto;
    &::-webkit-scrollbar {
      width: 0px;
    }
  }
  .equipment-container {
    width: 700px;
    height: 440px;
  }
  .equipment-detail-popup-content {
    width: 100%;
    height: calc(100% - 40px);
    border-radius: 0 0 8px 8px;
    padding: 20px 20px 28px;
    box-sizing: border-box;
  }

  .equipment-detail-popup-content-top {
    width: 100%;
    height: 160px;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
  }

  .equipment-detail-popup-content-top-left {
    width: 280px;
    height: 100%;
  }

  .equipment-detail-popup-content-top-right {
    width: 360px;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .equipment-detail-popup-content-top-right-title {
    width: 100%;
    height: 20px;
    font-size: 16px;
    color: #fff;
    background: url('./png/equipment-title-bg.png') no-repeat;
    background-size: 70px 24px;
    background-position: -5px 0px;
    padding-left: 17px;
    box-sizing: border-box;
  }

  .equipment-detail-popup-content-top-right-status {
    width: 100%;
    height: 42px;
    font-size: 14px;
    color: rgba(11, 229, 229, 1);
    background: url('./png/equipment-status.png') no-repeat;
    background-size: 100% 100%;
    position: relative;
  }

  .equipment-detail-popup-content-top-right-info {
    width: 100%;
    height: 66px;
    display: flex;
    justify-content: space-between;
  }

  .equipment-detail-popup-content-bottom {
    display: flex;
    justify-content: space-between;
  }

  .equipment-detail-popup-content-bottom-left {
    width: 280px;
  }

  .equipment-detail-popup-content-bottom-left-title {
    width: 100%;
    height: 22px;
    font-size: 14px;
    color: #fff;
    background: url('./png/equipment-title-bg.png') no-repeat;
    background-size: 70px 24px;
    background-position: -5px -1px;
    padding-left: 17px;
    box-sizing: border-box;
    margin-bottom: 10px;
  }

  .equipment-detail-popup-content-bottom-left-item {
    width: 100%;
    height: 36px;
    display: flex;
    justify-content: space-between;
    padding: 9px 16px;
    box-sizing: border-box;
    color: #fff;
    font-size: 14px;
  }

  .equipment-detail-popup-content-bottom-left-item-name {
    color: rgba(185, 232, 255, 1);
  }

  .equipment-detail-popup-content-bottom-left-item-bg {
    background: linear-gradient(
      87.3deg,
      rgba(107, 192, 255, 0) 2.25%,
      rgba(107, 192, 255, 0.16) 48.62%,
      rgba(107, 192, 255, 0) 95.17%
    );
  }

  .equipment-detail-popup-content-bottom-right {
    width: 360px;
  }

  .equipment-detail-popup-content-bottom-right-title {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    color: rgba(185, 232, 255, 1);
    margin-bottom: 18px;
  }

  .equipment-detail-popup-content-bottom-right-title-left {
    width: 134px;
    height: 22px;
    font-size: 14px;
    color: #fff;
    background: url('./png/equipment-title-bg.png') no-repeat;
    background-size: 70px 24px;
    background-position: -5px -1px;
    padding-left: 17px;
    box-sizing: border-box;
  }
</style>

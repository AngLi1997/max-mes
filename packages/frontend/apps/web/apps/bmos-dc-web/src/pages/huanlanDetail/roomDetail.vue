<template>
  <div class="room-container">
    <div class="room-detail-popup-top">
      <div>
        <img src="./png/room-icon.png" alt="" width="40px" height="40px" />
      </div>
      <div class="room-detail-popup-top-right">
        <div class="room-detail-popup-top-right-name">{{ roomDetail.name }}</div>
        <div style="display: flex; align-items: center; margin-top: 10px">
          <span class="room-detail-popup-top-right-label">清洁等级：</span>
          <div class="room-detail-popup-top-right-level">
            <div class="room-detail-level-color-box" style="{{bgStyle[roomDetail.cleanLevel] || ''}}"></div>
            &nbsp;
            <span>{{ roomDetail.cleanLevel || '-' }}</span>
          </div>
        </div>
        <div style="margin: 10px 0">
          <span class="room-detail-popup-top-right-label">清洁状态：</span>
          <span style="margin-right: 4px">•</span>
          <span>{{ roomDetail.status?.name || '-' }}</span>
        </div>
        <div>
          <span class="room-detail-popup-top-right-label">清场时限：</span>
          <span>{{ roomDetail.timeLimit }}h</span>
        </div>
      </div>
    </div>
    <div class="room-detail-popup-line"></div>
    <div class="room-detail-popup-bottom">
      <img src="./png/room-info.png" alt="" width="73px" height="18px" style="object-fit: cover" />
      <div class="data-box">
        <div v-for="item in roomDetail.roomEnvPropertyDTOList" :key="item.dataPointName" style="margin-top: 16px">
          <span class="room-detail-popup-top-right-label">{{ item.equipmentDataPropertyName }}：</span>
          <span>{{ item?.value || '-' }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { getParameter, getPlatformFactoryRoomModel } from '@/services';
  import MqttClient from '@/hooks/useMqtt';

  const route = useRoute();
  const _bgStyle = ref({
    A: 'background: #E87EFF',
    B: 'background: #FFE659',
    C: 'background: #DFFFBC',
    D: 'background: #73DAFF',
    一般生产区域: 'background: #D9D9D9',
    '生物阳性区域BSL-2': 'border:1px solid #FB0719',
  });
  const roomDetail = ref<any>({});
  const mqttMap = new Map();
  const mqttUrl = ref('');

  const getMqttUrl = async () => {
    try {
      const { data } = await getParameter('platform.sys.acquisition-address');
      const mqttConfig = JSON.parse(data?.value || '{}').supCon;
      mqttUrl.value = mqttConfig?.mqttAddress || '172.30.1.103:8083';
      return Promise.resolve();
    } catch (error) {
      console.log(error);
      return Promise.reject();
    }
  };

  const myMqttCreate = (env: any) => {
    if (!mqttMap.has(env.equipmentCode)) {
      mqttMap.set(
        env.equipmentCode,
        new MqttClient(
          `ws://${mqttUrl?.value}/mqtt`,
          env.equipmentCode || 'rtdvalue/report',
          (topic, data) => {
            data.RTValue.forEach((item: any) => {
              roomDetail.value.roomEnvPropertyDTOList.forEach((item1: any) => {
                if (item.name === item1.dataPointName) {
                  item1.value = item.value;
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
    if (query.id) {
      const res = await getPlatformFactoryRoomModel(query.id);
      if (res.data) {
        roomDetail.value = res.data;
        await getMqttUrl();
        roomDetail.value.roomEnvPropertyDTOList?.forEach((env: any) => {
          // if (env.equipmentCode === 'mqtt') {
          //   env.dataPointName = 'A000683';
          //   env.equipmentCode = 'FTT2002206';
          // }
          myMqttCreate(env);
        });
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
    max-height: 186px;
    overflow-y: auto;
    &::-webkit-scrollbar {
      width: 0px;
    }
  }
  .room-container {
    width: 320px;
    height: 410px;
    .room-detail-popup-top {
      width: 100%;
      height: 148px;
      padding: 22px 26px;
      box-sizing: border-box;
      display: flex;
      justify-content: space-between;
      color: #fff;
    }

    .room-detail-popup-top-right {
      width: 210px;
      height: 100%;
      font-size: 14px;
    }

    .room-detail-popup-top-right-name {
      font-family: Source Han Sans CN;
      font-size: 16px;
      font-weight: 500;
      line-height: 20px;
      text-align: left;
      text-underline-position: from-font;
      text-decoration-skip-ink: none;
    }

    .room-detail-popup-top-right-label {
      line-height: 18px;
      color: #b9e8ff;
    }

    .room-detail-popup-top-right-level {
      display: flex;
      align-items: center;
    }

    .room-detail-level-color-box {
      width: 12px;
      height: 12px;
      background: #ecffd7;
    }

    .room-detail-popup-bottom {
      width: 100%;
      height: 260px;
      padding: 16px 26px 23px;
      box-sizing: border-box;
      color: #fff;
      font-size: 14px;
    }

    .room-detail-popup-bottom img {
      object-fit: cover;
    }

    .room-detail-popup-line {
      background: linear-gradient(
        90deg,
        rgba(124, 189, 255, 0) 0.89%,
        rgba(124, 189, 255, 0.8) 51.75%,
        rgba(124, 189, 255, 0) 99.11%
      );
      width: 100%;
      height: 1px;
    }
  }
</style>

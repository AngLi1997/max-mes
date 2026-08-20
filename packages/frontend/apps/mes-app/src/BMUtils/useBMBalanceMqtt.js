import { reqEquipmentAppRelease, reqSendHeartBeatApi } from '@/api/common.js';
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { storeToRefs } from 'pinia';
import { onUnmounted, reactive, ref } from 'vue';
import { buildMTSICSCommand, hexToString, hexToUint8Array, stringToHex } from './BMFunc.js';
import BMMqttClient from './BMMqtt';

export const useBMBalanceMqtt = (params) => {
  const systemInfoStore = useSystemInfoStore();
  const { getParameterByCode } = systemInfoStore;
  const balanceInfo = ref({});
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  // MqttTask 用于存储 Mqtt 实例对象
  const mqttTask = ref(null);

  // isBackMessage 是否返回消息
  const isBackMessage = ref(true);
  // 清零消息是否返回
  const clearZeroMessage = ref(false);
  // 去皮消息是否返回
  const removePeelMessage = ref(false);

  const weightInfo = reactive({
    weight: 0,
    weightUnit: 'g',
    open: false,
  });

  // 校验秤具连接
  const checkBalanceConnect = () => {
    if (isBackMessage.value) {
      return true;
    }
    else {
      uni.showToast({
        title: t('秤具连接异常'),
        icon: 'none',
      });
      return false;
    }
  };
  // 获取读数
  const getReadings = () => {
    if (checkBalanceConnect()) {
      isBackMessage.value = false;
      mqttTask.value
      && mqttTask.value.handlePublish(balanceInfo.value.subTopic, balanceInfo.value.read || 'R');
    }
  };

  // 清零
  const clearZero = () => {
    mqttTask.value
    && mqttTask.value.handlePublish(balanceInfo.value.subTopic, balanceInfo.value.clear || 'T');
  };

  // 去皮
  const removePeel = () => {
    mqttTask.value
    && mqttTask.value.handlePublish(balanceInfo.value.subTopic, balanceInfo.value.peel || 'Z');
  };

  // 解析读数
  const solveData = (data) => {
    let value = data;
    if (balanceInfo.value.type === 'Hex') {
      value = hexToString(data);
    }
    try {
      const regexString = balanceInfo.value.value || '-?[0-9]+(\\.[0-9]+)?';
      const regex = new RegExp(regexString, 'g');
      const result = value.match(regex);
      if (result && result[0]) {
        const str = result[0].replace(/\s+/g, '');
        weightInfo.weight = Number(str);
      }
    }
    catch (error) {
      console.log(error);
    }
    // ST NT 000000.0 g
    // console.log('解析读数', data);

    // ST 稳定
    // US 不稳定
    // OL 超载
    // console.log(data.slice(0, 2));

    // NT 净重
    // GS 毛重
    // console.log(data.slice(3, 5));
    // 读数(8位)
    // try {
    //   weightInfo.weight = Number(data.slice(6, 14));
    // }
    // catch (error) {
    //   weightInfo.weight = 0;
    // }
    // // 单位可变(g/kg)
    // weightInfo.weightUnit = data.slice(14, 16);
  };

  const heartBeatTimer = ref(null);
  // 调用心跳接口
  const reqSendHeartBeat = () => {
    if (
      !selectedBalance.value.balanceId
      || !params.value.batchNo
      || !params.value.productName
    ) {
      return false;
    }
    reqSendHeartBeatApi({
      deviceId: selectedBalance.value.balanceId,
      batchNo: params.value.batchNo,
      productName: params.value.productName,
    });
    return true;
  };
  // 开启秤具心跳
  const createHeartBeat = () => {
    const isSuccess = reqSendHeartBeat();
    if (isSuccess) {
      newSetInterval();
    }
    else {
      heartBeatTimer.value = setInterval(() => {
        if (weightInfo.open) {
          const isTrue = reqSendHeartBeat();
          if (isTrue) {
            clearInterval(heartBeatTimer.value);
            heartBeatTimer.value = null;
            newSetInterval();
          }
        }
      }, 100);
    }
  };
  // 新开启setInterval
  const newSetInterval = () => {
    heartBeatTimer.value = setInterval(() => {
      weightInfo.open && reqSendHeartBeat();
    }, 9000);
  };

  // 关闭秤具心跳和 WebSocket
  const closeHeartBeat = () => {
    if (mqttTask.value) {
      mqttTask.value.handleClose();
    }
    if (heartBeatTimer.value) {
      clearInterval(heartBeatTimer.value);
      heartBeatTimer.value = null;
    }
  };

  // 开启mqtt连接
  const createMqtt = () => {
    if (params.value.auto && selectedBalance.value) {
      const res = getParameterByCode('platform.sys.weighing.protocol-type');
      balanceInfo.value = JSON.parse(res.value).find((item) => {
        return item.name === selectedBalance.value.protocolType;
      });
      // balanceInfo.value = {
      //   name: 'mqtt01',
      //   connectionType: 'mqtt',
      //   mqttUrl: '172.30.1.160:8083/mqtt',
      //   pubTopic: '/PubTopic1',
      //   subTopic: '/SubTopic1',
      //   type: 'ASCII',
      //   clear: 'T',
      //   peel: 'Z',
      //   read: 'R',
      //   value: '',
      //   sendType: '',
      // };
      if (balanceInfo.value.sendType === 'unit8Array') {
        balanceInfo.value.read = hexToUint8Array(balanceInfo.value.read);
        balanceInfo.value.clear = hexToUint8Array(balanceInfo.value.clear);
        balanceInfo.value.peel = hexToUint8Array(balanceInfo.value.peel);
      }
      if (balanceInfo.value.sendType === 'MTSICS') {
        balanceInfo.value.read = buildMTSICSCommand(balanceInfo.value.read);
        balanceInfo.value.clear = buildMTSICSCommand(balanceInfo.value.clear);
        balanceInfo.value.peel = buildMTSICSCommand(balanceInfo.value.peel);
      }
      if (balanceInfo.value.sendType === 'Hex') {
        balanceInfo.value.read = stringToHex(balanceInfo.value.read);
        balanceInfo.value.clear = stringToHex(balanceInfo.value.clear);
        balanceInfo.value.peel = stringToHex(balanceInfo.value.peel);
      }
      mqttTask.value = new BMMqttClient(`mqtt://${balanceInfo.value.mqttUrl}`, balanceInfo.value.pubTopic, (topic, data) => {
        if (topic === balanceInfo.value.pubTopic) {
          solveData(data);
          isBackMessage.value = true;
          removePeelMessage.value = true;
          clearZeroMessage.value = true;
        }
      }, {}, () => {
        weightInfo.open = true;
      });
      createHeartBeat();
    }
  };

  onShow(async () => {
    createMqtt();
  });

  onHide(() => {
    closeHeartBeat();
  });
  onUnmounted(() => {
    closeHeartBeat();
    reqEquipmentAppRelease({ id: selectedBalance.value.balanceId });
  });

  return {
    // mqttTask,
    weightInfo,
    selectedBalance,
    isBackMessage,
    removePeelMessage,
    clearZeroMessage,
    getReadings,
    clearZero,
    removePeel,
  };
};

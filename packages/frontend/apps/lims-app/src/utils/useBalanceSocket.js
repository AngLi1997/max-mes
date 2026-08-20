import { reqEquipmentAppRelease, reqSendHeartBeatApi } from '@/api/common.js';
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { storeToRefs } from 'pinia';
import socket from 'plus-websocket';
import { onUnmounted, reactive, ref } from 'vue';

// 十六进制转 Uint8Array
export const hexToUint8Array = (hex) => {
  let byteArray = new Uint8Array(hex.length);
  if (hex.length % 2 !== 0) {
    var sendArray = new Uint8Array((hex.length + 1) / 2);
  }
  else {
    var sendArray = new Uint8Array(hex.length / 2);
  }
  let i;
  byteArray = hex;

  for (i = 0; i < sendArray.length; i++) {
    if (
      (byteArray[i * 2] >= '0' && byteArray[i * 2] <= '9')
      || (byteArray[i * 2] >= 'A' && byteArray[i * 2] <= 'F')
      || (byteArray[i * 2] >= 'a' && byteArray[i * 2] <= 'f')
    ) {
      sendArray[i]
        = Number.parseInt(byteArray[i * 2], 16) * 16
        + Number.parseInt(byteArray[i * 2 + 1], 16);
    }
    else {
      console.log('Error: Invalid hex character');
    }
  }
  return sendArray;
};
export const useBalanceSocket = (params) => {
  const systemInfoStore = useSystemInfoStore();
  const { getParameterByCode } = systemInfoStore;
  const balanceInfo = ref({});
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  // SocketTask 用于存储 WebSocket 实例对象
  const SocketTask = ref(null);

  // isBackMessage 是否返回消息
  const isBackMessage = ref(false);
  // 清零消息是否返回
  const clearZeroMessage = ref(false);
  // 去皮消息是否返回
  const removePeelMessage = ref(false);

  const weightInfo = reactive({
    weight: 0,
    weightUnit: 'g',
    open: false,
  });

  // 获取读数
  const getReadings = () => {
    if (isBackMessage.value) {
      SocketTask.value
      && SocketTask.value.send({
        data: balanceInfo.value.read || 'R',
        success(res) {
          // console.log('getReadings', res);
        },
      });
    }
    else {
      uni.showToast({
        title: t('秤具连接异常'),
        icon: 'none',
      });
    }
  };

  // 清零
  const clearZero = () => {
    SocketTask.value
    && SocketTask.value.send({
      data: balanceInfo.value.clear || 'Z',
      success(res) {
        console.log('clearZero', res);
      },
    });
  };

  // 去皮
  const removePeel = () => {
    SocketTask.value
    && SocketTask.value.send({
      data: balanceInfo.value.peel || 'T',
      success(res) {
        console.log('removePeel', res);
      },
    });
  };

  // 解析读数
  const solveData = (data) => {
    let value = data;
    if (balanceInfo.value.type === 'Hex') {
      value = data
        .split(' ')
        .map((item) => {
          return Number.parseInt(item, 16);
        })
        .join('');
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
    try {
      weightInfo.weight = Number(data.slice(6, 14));
    }
    catch (error) {
      weightInfo.weight = 0;
    }
    // 单位可变(g/kg)
    weightInfo.weightUnit = data.slice(14, 16);
  };

  const heartBeatTimer = ref(null);
  // 调用心跳接口
  const reqSendHeartBeat = () => {
    if (
      !selectedBalance.value.balanceId
      || !params.value.batchNo
      || !params.value.productName
    ) {
      return;
    }
    reqSendHeartBeatApi({
      deviceId: selectedBalance.value.balanceId,
      batchNo: params.value.batchNo,
      productName: params.value.productName,
    });
  };
  // 开启秤具心跳
  const createHeartBeat = () => {
    reqSendHeartBeat();
    heartBeatTimer.value = setInterval(() => {
      weightInfo.open && reqSendHeartBeat();
    }, 9000);
  };

  // 关闭秤具心跳和 WebSocket
  const closeHeartBeat = () => {
    if (SocketTask.value) {
      SocketTask.value.close({
        success(res) {
          console.log('WebSocket 已关闭！');
          SocketTask.value = null;
        },
      });
    }
    if (heartBeatTimer.value) {
      clearInterval(heartBeatTimer.value);
      heartBeatTimer.value = null;
    }
  };

  onShow(async () => {
    // selectedBalance.value = selectedBalance.value || {};
    // selectedBalance.value.websocketAddress = 'Ws://192.168.200.100:8000';
    if (
      params.value.auto
      && selectedBalance.value
      && selectedBalance.value.websocketAddress
    ) {
      const res = getParameterByCode('platform.sys.weighing.protocol-type');
      balanceInfo.value = JSON.parse(res.value).find((item) => {
        return item.name === selectedBalance.value.protocolType;
      });
      if (balanceInfo.value.sendType === 'unit8Array') {
        balanceInfo.value.read = hexToUint8Array(balanceInfo.value.read);
        balanceInfo.value.clear = hexToUint8Array(balanceInfo.value.clear);
        balanceInfo.value.peel = hexToUint8Array(balanceInfo.value.peel);
      }
      SocketTask.value = socket.connectSocket({
        url: selectedBalance.value.websocketAddress,
        complete: () => {
          console.log('connectSocket complete');
        },
        success: () => {
          console.log('connectSocket success');
          isBackMessage.value = true;
        },
        fail: () => {
          uni.showToast({
            title: t('连接失败,请切换到手动称量'),
            icon: 'none',
          });
          console.log('connectSocket fail');
        },
      });
      SocketTask.value.onOpen((res) => {
        weightInfo.open = true;
      });
      SocketTask.value.onMessage((res) => {
        isBackMessage.value = true;
        removePeelMessage.value = true;
        clearZeroMessage.value = true;
        solveData(res.data);
      });
      createHeartBeat();
    }
  });

  onHide(() => {
    closeHeartBeat();
  });
  onUnmounted(() => {
    closeHeartBeat();
    reqEquipmentAppRelease({ id: selectedBalance.value.balanceId });
  });

  return {
    SocketTask,
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

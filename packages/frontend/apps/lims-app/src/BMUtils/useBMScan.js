import { NEVER_SHOW_SCAN_PORT, SCAN_SERIAL_PORT } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { isFunction } from 'lodash-es';
import { onUnmounted, ref } from 'vue';

export function useBMScan({ callback, showSerialPortSelect = { value: false } }) {
  // H5串口扫描
  // #ifdef H5
  // 串口是否打开
  const isOpened = ref(false);

  function onSerialPortData(_event, value) {
    if (isFunction(callback)) {
      callback(value.replace('\r', ''));
    }
  }
  function BMScanCode() {
    const path = getStorageSync(SCAN_SERIAL_PORT)?.path;
    if (!path) {
      return;
    }
    // 串口是否打开
    if (!isOpened.value) {
      openSerialPort();
    }
    // 开始扫描
    setTimeout(() => {
      window?.serialPortAPI?.onSerialPortScan();
    }, 100);
  }

  function closeSerialPort() {
    window?.serialPortAPI?.closeSerialPort();
    isOpened.value = false;
    console.log('closeSerialPort');
  }

  function openSerialPort() {
    const path = getStorageSync(SCAN_SERIAL_PORT)?.path;
    if (!path) {
      const neverShowScanPort = getStorageSync(NEVER_SHOW_SCAN_PORT) || false;
      if (!neverShowScanPort) {
        showSerialPortSelect.value = true;
      }
      return;
    }
    const data = {
      path,
      baudRate: 9600,
      dataBits: 8,
      stopBits: 1,
      parity: 'none',
      flowControl: false,
    };
    window?.serialPortAPI?.openSerialPort(data, onSerialPortData);
    isOpened.value = true;
  }

  onShow(() => {
    openSerialPort();
  });
  onHide(() => {
    closeSerialPort();
  });
  onUnmounted(() => {
    closeSerialPort();
  });
  // #endif
  // 安卓广播扫描
  // #ifdef APP-PLUS
  let main, receiver, filter;
  let _codeQueryTag = false;
  function startScan() {
    main.registerReceiver(receiver, filter);
  }

  function stopScan() {
    main.unregisterReceiver(receiver);
  }

  function initScan() {
    main = plus.android.runtimeMainActivity(); // 获取activity
    const IntentFilter = plus.android.importClass('android.content.IntentFilter');
    filter = new IntentFilter();
    filter.addAction('com.android.serial.BARCODEPORT_RECEIVEDDATA_ACTION'); // 换你的广播动作
    receiver = plus.android.implements(
      'io.dcloud.feature.internal.reflect.BroadcastReceiver',
      {
        onReceive(context, intent) {
          plus.android.importClass(intent);
          const code = intent.getStringExtra('DATA'); // 换你的广播标签
          queryCode(code.replace(/\n/g, ''));
        },
      },
    );
  }

  function queryCode(code) {
    // 防重复
    if (_codeQueryTag)
      return false;
    _codeQueryTag = true;
    setTimeout(() => {
      _codeQueryTag = false;
    }, 150);
    if (isFunction(callback)) {
      callback(code.replace('\r', ''));
    }
  }

  function setBROADCAST() {
    const main2 = plus.android.runtimeMainActivity(); // 获取acitivity
    const Intent = plus.android.importClass('android.content.Intent');
    const intent2 = new Intent('com.android.scanner.service_settings');
    intent2.putExtra('barcode_send_mode', 'BROADCAST');
    main2.sendBroadcast(intent2);
  }

  function setBROADCASTACTION() {
    const main2 = plus.android.runtimeMainActivity(); // 获取acitivity
    const Intent = plus.android.importClass('android.content.Intent');
    const intent2 = new Intent('com.android.scanner.service_settings');
    intent2.putExtra('action_barcode_broadcast', 'com.seuic.scan');
    main2.sendBroadcast(intent2);
  }

  function BMScanCode() {
    const main2 = plus.android.runtimeMainActivity(); // 获取acitivity
    const Intent = plus.android.importClass('android.content.Intent');
    const intent2 = new Intent('com.android.action.keyevent.KEYCODE_KEYCODE_SCAN_F_DOWN');
    main2.sendBroadcast(intent2);
    setAutoStart();
  }

  function setAutoStart() {
    const main2 = plus.android.runtimeMainActivity(); // 获取acitivity
    const Intent = plus.android.importClass('android.content.Intent');
    const intent2 = new Intent('com.android.scanner.service_settings');
    intent2.putExtra('boot_start', true);
    main2.sendBroadcast(intent2);
  }
  function openSerialPort() {
    console.log('android-openSerialPort');
  }

  onShow(() => {
    initScan();
    setBROADCASTACTION();
    setBROADCAST();
    setAutoStart();
    startScan();
  });
  onHide(() => {
    /* 页面退出时一定要卸载监听,否则下次进来时会重复，造成扫一次出2个以上的结果 */
    stopScan();
  });
  // #endif
  return {
    BMScanCode,
    openSerialPort,
  };
}

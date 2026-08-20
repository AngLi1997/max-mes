import { SCAN_SERIAL_PORT } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { ref } from 'vue';

const scanSuccessCallback = ref(null);
function onSerialPortData(_event, value) {
  if (scanSuccessCallback.value) {
    scanSuccessCallback.value({
      result: value.replace('\r', ''),
    });
    scanSuccessCallback.value = null;
  }
}
export function useScan() {
  const timer = ref(null);
  const successCallback = ref(null);
  const failCallback = ref(null);
  const completeCallback = ref(null);
  let main, receiver, filter;
  let _codeQueryTag = false;
  const opened = ref(false);
  const isOpenMessage = ref(false);

  onShow(() => {
    // #ifdef APP-PLUS
    initScan();
    setBROADCASTACTION();
    setBROADCAST();
    setAutoStart();
    startScan();
    // #endif
    // #ifdef H5
    openSerialPort();
    // #endif
  });
  onHide(() => {
    // #ifdef APP-PLUS
    /* 页面退出时一定要卸载监听,否则下次进来时会重复，造成扫一次出2个以上的结果 */
    stopScan();
    // #endif
    // #ifdef H5
    window?.serialPortAPI?.closeSerialPort();
    scanSuccessCallback.value = null;
    // #endif
  });
  // #ifdef APP-PLUS
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
    filter.addAction('com.seuic.scan'); // 换你的广播动作
    receiver = plus.android.implements(
      'io.dcloud.feature.internal.reflect.BroadcastReceiver',
      {
        onReceive(context, intent) {
          plus.android.importClass(intent);
          const code = intent.getStringExtra('scannerdata'); // 换你的广播标签
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
    const id = code;
    if (completeCallback.value) {
      completeCallback.value({ result: code });
    }
    if (timer.value) {
      clearTimeout(timer.value);
      timer.value = null;
      if (successCallback.value) {
        successCallback.value({ result: code });
      }
    }
    uni.$emit('scancodedate', {
      code: id,
    });
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

  function clickScanCode() {
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

  function bmosScanCode({ success, fail, complete }) {
    if (timer.value) {
      return;
    }
    clickScanCode();
    successCallback.value = success || null;
    failCallback.value = fail || null;
    completeCallback.value = complete || null;
    timer.value = setTimeout(() => {
      if (timer.value) {
        clearTimeout(timer.value);
        timer.value = null;
        if (failCallback.value) {
          failCallback.value();
        }
      }
      completeCallback.value && completeCallback.value();
    }, 3000);
  }

  function init() {
    initScan();
    setBROADCASTACTION();
    setBROADCAST();
    setAutoStart();
    startScan();
  }
  // #endif

  // #ifdef H5
  function bmosScanCode({ success }) {
    const path = getStorageSync(SCAN_SERIAL_PORT)?.path;
    if (!path) {
      isOpenMessage.value = true;
      return;
    }
    scanSuccessCallback.value = success || null;
    if (!opened.value) {
      openSerialPort();
      setTimeout(() => {
        window?.serialPortAPI?.onSerialPortScan();
      }, 100);
    }
    else {
      window?.serialPortAPI?.onSerialPortScan();
    }
  }
  function init() {
    console.log('init-h5');
  }
  function stopScan() {
    scanSuccessCallback.value = null;
    console.log('stopScan-h5');
  }
  // #endif

  function openSerialPort() {
    const path = getStorageSync(SCAN_SERIAL_PORT)?.path;
    if (!path) {
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
    opened.value = true;
    window?.serialPortAPI?.openSerialPort(data, onSerialPortData);
  }

  return {
    bmosScanCode,
    init,
    stopScan,
    isOpenMessage,
  };
}

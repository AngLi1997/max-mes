<script setup>
import { clearTimer, getServerTime } from '@/utils/time.js';
import { BMOS_ACCESS_TOKEN, LOCK_SCREEN_TIME } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { init } from '@/utils/useBmosI18n.js';
import { useLocale } from '@/utils/useLocale.js';
import { onHide, onLaunch, onShow } from '@dcloudio/uni-app';

const { loadLanguageAsync, setMessages } = useLocale();

// #ifdef APP-PLUS
// App屏幕锁定监听
function lockScreenMonitoring() {
  let receiver;
  main = plus.android.runtimeMainActivity(); // 获取activity
  // 广播接收
  const ALARM_RECEIVER = 'alarm_receiver';
  receiver = plus.android.implements(
    'io.dcloud.feature.internal.reflect.BroadcastReceiver',
    {
      onReceive(context, intent) {
        // 实现onReceiver回调函数
        const Intent = plus.android.importClass('android.content.Intent');
        console.log(intent.getAction());
        // action = intent.getAction();
        if (intent.getAction() == Intent.ACTION_SCREEN_ON) {
          console.log('开屏');
        }
        else if (intent.getAction() == Intent.ACTION_SCREEN_OFF) {
          console.log('锁屏');
          if (getStorageSync(LOCK_SCREEN_TIME) === 0) {
            return;
          }
          const pages = getCurrentPages();
          const url = pages[pages.length - 1].$page.fullPath;
          if (
            url !== '/pages/login/index'
            && url !== '/pages/lockPage/index'
          ) {
            uni.navigateTo({
              url: '/pages/lockPage/index',
            });
          }
        }
        else if (intent.getAction() == Intent.ACTION_USER_PRESENT) {
          console.log('解锁');
        }
        result.textContent += `\nAction :${intent.getAction()}`;
        main.unregisterReceiver(receiver);
      },
    },
  );
  const IntentFilter = plus.android.importClass(
    'android.content.IntentFilter',
  );
  const Intent = plus.android.importClass('android.content.Intent');
  const filter = new IntentFilter();
  filter.addAction(Intent.ACTION_SCREEN_ON);
  filter.addAction(Intent.ACTION_SCREEN_OFF);
  filter.addAction(Intent.ACTION_USER_PRESENT);
  main.registerReceiver(receiver, filter); // 注册监听
}
// #endif
onShow(() => {
  console.log('App Show');
  const token = getStorageSync(BMOS_ACCESS_TOKEN);
  token && getServerTime();
});
onHide(() => {
  console.log('App Hide');
  clearTimer();
});
onLaunch(() => {
  // 初始化in18n
  init();
  // #ifdef APP-PLUS
  lockScreenMonitoring();
  // #endif
  loadLanguageAsync().then((messages) => {
    const lang = uni.getLocale();
    setMessages(lang, messages);
  });
});
</script>

<style lang="scss">
@import "@/static/iconfont/iconfont.css";
@import "@/static/iconfont/newIconfont.css";
@import "@/static/iconfont/symbolIcon.css";
@import "@/static/commonStyle/uni-component.scss";
@import "@/static/wot/component.scss";
@import '@/static/wot/default.scss';
@font-face {
    font-family: 'Digital Numbers';
    src: url('@/static/fonts/DIGITALNUMBERS.TTF') format('truetype');
}

page {
  background: #f2f3f5;
  height: 100%;
  ::-webkit-scrollbar {
    display: none;
  }
}
</style>

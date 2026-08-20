<template>
  <BMLayout>
    <view class="container">
      <view class="user-box">
        <view class="info">
          <view class="avatar">
            <uv-avatar size="23.45rpx" src="/static/homeIcon/user-avatar.png" />
          </view>
          <view class="name-box">
            <text class="name">
              {{ userInfo.userName }}
            </text>
            <uv-text text="&nbsp;/&nbsp;" color="#545659" size="11.72rpx" />
            <text class="account">
              {{ userInfo.loginName }}
            </text>
          </view>
        </view>
        <view class="logout">
          <uv-text :text="t('退出登录')" color="#FF4C26" size="11.72rpx" @click="logout" />
        </view>
      </view>
      <view class="content">
        <BlockButton :label="t('修改密码')" :bottom-border="false" style="margin-bottom: 14.06rpx;" @row-click="openPwdPopup" />
        <BlockButton :label="t('修改签名密码')" :bottom-border="false" style="margin-bottom: 14.06rpx;" @row-click="openSignModal" />
        <!-- #ifdef H5 -->
        <BlockButton :label="t('扫码串口')" :right-text="serialPortName" :bottom-border="false" style="margin-bottom: 14.06rpx;" @row-click="toScanSerialPort" />
        <!-- #endif -->
        <BlockButton :label="t('手写签名设置')" :bottom-border="false" style="margin-bottom: 14.06rpx;" @row-click="toHandleSign" />
        <BlockButton :label="t('打印设备')" :right-text="deviceName" :bottom-border="false" @row-click="openPrinterPopup" />
        <view class="title">
          <text>{{ t('系统设置') }}</text>
        </view>
        <BlockButton :label="t('主页左侧导航栏设置')" @row-click="tabBarHandle" />
        <BlockButton :label="t('语言切换')" :right-text="langeuageName" @row-click="languageHandle" />
        <!-- #ifdef APP-PLUS -->
        <BlockButton :label="t('清除缓存')" :right-text="cacheSize" @row-click="clearCacheHandle" />
        <!-- #endif -->
        <BlockButton :label="t('检查更新')" :right-text="appVersion" @left-click="appVersionClick" @row-click="appVersionHandel" />
      </view>

      <!-- 退出登录 -->
      <uv-popup ref="popupRef" mode="center" round="11.72rpx">
        <view class="logout-popup">
          <text class="text">
            {{ t('是否退出登录') }}
          </text>
          <view class="button-container">
            <uv-row justify="space-between" gutter="10">
              <uv-col span="6">
                <BmosButton type="default" :text="t('取消')" @click="popupRef.close()" />
              </uv-col>
              <uv-col span="6">
                <BmosButton type="primary" :text="t('确定')" @click="logoutConfirm()" />
              </uv-col>
            </uv-row>
          </view>
        </view>
      </uv-popup>
      <!-- app版本更新 -->
      <!-- <uv-popup ref="appVersionPopupRef" mode="center" round="11.72rpx" :close-on-click-overlay="false">
        <view class="app-version-content">
          <view class="text-content-new">
            <text>{{ t('您已是最新版本') }}</text>
          </view>
          <BmosButton type="primary" :text="t('确定')" @click="appVersionPopupRef.close()" />
        </view>
      </uv-popup> -->
      <BMModal
        v-model="showAppVersionPopup"
        :show-title="false"
        size="small"
        custom-class="tip-popup"
        :close-on-click-modal="false"
        :confirm-text="t('确定')"
        :show-cancel-button="false"
        @confirm="() => { showAppVersionPopup = false }"
      >
        <view class="tip">
          {{ t('您已是最新版本') }}
        </view>
      </BMModal>
      <!-- 修改密码 -->
      <uv-popup ref="popupPwdRef" mode="center" background-color="#fff" round="7.03rpx" :close-on-click-overlay="false">
        <view class="popup-pwd-content">
          <view class="popup-pwd-close">
            <text>{{ t('修改密码') }}</text>
            <uv-icon
              class="close-svg"
              name="guanbi"
              custom-prefix="bmos-icon"
              size="14.07rpx"
              color="#909398"
              @click="closePwdPopup"
            />
          </view>
          <uv-gap height="29.31rpx" bg-color="#fff" />
          <uv-form
            ref="pwdFormRef"
            label-width="140.68rpx"
            label-position="top"
            :model="password"
            :rules="pwdRules"
            :label-style="{ 'clolr': '#303133', 'font-size': '14.07rpx', 'margin-bottom': '3.52rpx' }"
          >
            <uv-form-item custom-style="padding:0;" :label="`${t('新密码')}：`" prop="new1">
              <uv-input v-model="password.new1" :placeholder="t('请输入新密码')" :password="password.pwd1" v-bind="inputAttrs">
                <template #suffix>
                  <uv-icon
                    v-if="password.pwd1"
                    name="yulan-guan"
                    custom-prefix="bmos-icon"
                    size="12.9rpx"
                    color="#A1A5B2"
                    @click="password.pwd1 = false"
                  />
                  <uv-icon
                    v-else
                    name="yulan-kai"
                    custom-prefix="bmos-icon"
                    size="12.9rpx"
                    color="#A1A5B2"
                    @click="password.pwd1 = true"
                  />
                </template>
              </uv-input>
            </uv-form-item>
            <uv-form-item custom-style="padding:0;" :label="`${t('确认密码')}：`" prop="new2">
              <uv-input v-model="password.new2" :placeholder="t('请输入确认密码')" :password="password.pwd2" v-bind="inputAttrs">
                <template #suffix>
                  <uv-icon
                    v-if="password.pwd2"
                    name="yulan-guan"
                    custom-prefix="bmos-icon"
                    size="12.9rpx"
                    color="#A1A5B2"
                    @click="password.pwd2 = false"
                  />
                  <uv-icon
                    v-else
                    name="yulan-kai"
                    custom-prefix="bmos-icon"
                    size="12.9rpx"
                    color="#A1A5B2"
                    @click="password.pwd2 = true"
                  />
                </template>
              </uv-input>
            </uv-form-item>
          </uv-form>
          <uv-row justify="space-between" gutter="10">
            <uv-col span="6">
              <BmosButton type="default" :text="t('取消')" @click="closePwdPopup" />
            </uv-col>
            <uv-col span="6">
              <BmosButton type="primary" :text="t('确定')" @click="submitPwdPopup" />
            </uv-col>
          </uv-row>
        </view>
      </uv-popup>

      <!-- 语言切换 -->
      <uv-popup ref="languagePopupRef" mode="center" round="11.72rpx" :close-on-click-overlay="false">
        <view class="language-content">
          <view class="title">
            <text>{{ t('语言切换') }}</text>
          </view>
          <view class="language-list">
            <view
              v-for="(item, index) in languageListRef"
              :key="index"
              class="language-item"
              :class="{ 'language-item-active': checkedLanguage === item.value }"
              @click="languageItemClick(item.value)"
            >
              <text>{{ item.label }}</text>
              <uv-icon
                v-if="checkedLanguage === item.value"
                name="xuanze"
                custom-prefix="bmos-icon"
                size="14.07rpx"
                color="#3F5DF1"
                class="icon"
              />
            </view>
          </view>
          <uv-row justify="space-between" gutter="10">
            <uv-col span="6">
              <BmosButton type="default" :text="t('取消')" @click="languagePopupRef.close()" />
            </uv-col>
            <uv-col span="6">
              <BmosButton type="primary" :text="t('确定')" @click="languageComfirm" />
            </uv-col>
          </uv-row>
        </view>
      </uv-popup>
      <!-- 打印机 -->
      <BmosPrinter ref="bmosPrinterInstance" @choose-printer-confirm="getDeviceInfo" />

      <BMMessageBox
        v-model="showUpdateModal"
        :title="t('检测到新版本，请及时更新')"
        :confirm-text="t('更新')"
        :show-cancel-button="false"
        :close-on-click-modal="false"
        @confirm="handleUpdateAppVersion"
      />
    </view>
    <BMModal
      v-model="showSignModal"
      :default-padding="false"
      :title="t('修改签名密码')"
      size="medium"
      @confirm="confirmSign"
      @cancel="resetSign"
    >
      <BMForm ref="formRef" v-bind="formProps" />
    </BMModal>
    <BMModal
      v-model="showTabBarModal"
      :title="t('主页导航栏设置')"
      size="medium"
      @confirm="confirmTabBar"
      @cancel="showTabBarModal = false"
    >
      <div class="tabbar-modal-content">
        <div v-for="item in newTabBars" :key="item.id" class="tabbar-modal-item">
          <text class="text">
            {{ item.text }}
          </text>
          <wd-switch v-model="item.show" />
        </div>
      </div>
    </BMModal>
    <!-- #ifdef H5 -->
    <BMScanSerialPort v-model="showScanSerialPort" :show-never-show-btn="false" @confirm="scanSerialConfirm" />
    <!-- #endif -->
  </BMLayout>
</template>

<script setup>
import { postChangePwd, postLogout, updateSignaturePasswordApi } from '@/api/systemApi.js';
import { BMForm, BMLayout, BMMessageBox, BMModal, BMScanSerialPort } from '@/BMComponents';
import BmosButton from '@/components/BmosButton/index.vue';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { useTabbarStore } from '@/stores/tabbar.js';
import { checkUpdateApp, download } from '@/utils/checkUpdateApp.js';
import { encrypt } from '@/utils/encrypt.js';
import { BMOS_ACCESS_TOKEN, DEVICE_PRINTER, SCAN_SERIAL_PORT, USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync, removeStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { languageType, useLocale } from '@/utils/useLocale.js';
import { onShow } from '@dcloudio/uni-app';
import { cloneDeep } from 'lodash-es';
import { storeToRefs } from 'pinia';
import { computed, nextTick, reactive, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import BlockButton from './components/BlockButton.vue';

const systemInfoStore = useSystemInfoStore();
const { showNotify } = useNotify();

const { cacheSize } = storeToRefs(systemInfoStore);
const { formatSize, clearCache, getParameterByCode } = systemInfoStore;
// 获取缓存大小
formatSize();
const popupRef = ref(null);
const bmosPrinterInstance = ref(null);
const userInfo = ref({});
const languageListRef = ref([]);
const currentLanguage = ref('zh-Hans');
const checkedLanguage = ref('zh-Hans');
const langeuageName = computed(() => {
  const language = languageListRef.value.find(item => item.value === currentLanguage.value);
  return language ? language.label : '';
});
const {
  onLocaleChange,
  applicationLocale,
  systemInfo,
} = useLocale();
const clickCount = ref(0);
currentLanguage.value = applicationLocale.value;

const showScanSerialPort = ref(false);

const showSignModal = ref(false);
const formRef = ref();

const formProps = reactive({
  baseColProps: {
    span: 24,
  },
  schemas: [
    {
      field: 'loginPassword',
      component: 'Input',
      label: t('登录密码'),
      required: true,
      componentProps: {
        showPassword: true,
      },
    },
    {
      field: 'signaturePassword1',
      component: 'Input',
      label: t('签名密码'),
      required: true,
      componentProps: {
        showPassword: true,
      },
    },

    {
      field: 'signaturePassword2',
      component: 'Input',
      label: t('确认密码'),
      componentProps: {
        showPassword: true,
        placeholder: t('请再次输入签名密码'),
      },
      dynamicRules: ({ formModel }) => {
        return [
          {
            required: true,
            message: t('请输入确认密码'),
            validator: (val) => {
              if (!val) {
                return Promise.reject(t('请再次输入签名密码'));
              }
              if (formModel.signaturePassword1 !== val) {
                return Promise.reject(t('两次密码不一致'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
  ],
});

const confirmSign = async () => {
  const values = await formRef.value.validate();
  try {
    await updateSignaturePasswordApi({
      loginPassword: encrypt(values.loginPassword),
      signaturePassword: encrypt(values.signaturePassword1),
    });
    showSignModal.value = false;
    showNotify({ type: 'success', message: t('修改签名密码成功') });
  }
  catch (error) {
    error.message && showNotify({ type: 'warning', message: error.message });
  }
};

const resetSign = () => {
  showSignModal.value = false;
};

const openSignModal = () => {
  showSignModal.value = true;
  formRef.value.resetForm();
};
const openPrinterPopup = () => {
  bmosPrinterInstance.value.open();
};

const device = ref(null);
const getDeviceInfo = () => {
  device.value = getStorageSync(DEVICE_PRINTER);
};
const deviceName = computed(() => {
  return device.value ? device.value.name : '';
});

// 串口名称
const serialPortName = ref('');

const appVersion = ref('');

// 设置串口号
const scanSerialConfirm = () => {
  serialPortName.value = getStorageSync(SCAN_SERIAL_PORT)?.friendlyName || '';
};

// 获取版本号
const getAppVersion = async () => {
  try {
    const data = getParameterByCode('platform.sys.version');
    appVersion.value = data.value;
  }
  catch (error) {
    console.log(error);
  }
};
const timer = ref(null);
const appVersionClick = () => {
  clickCount.value++;
  if (clickCount.value >= 5) {
    // #ifdef APP-PLUS
    appVersion.value = systemInfo.value.appWgtVersion;
    // #endif
    // #ifdef H5
    appVersion.value = systemInfo.value.appVersion;
    // #endif
  }
  if (timer.value) {
    clearTimeout(timer.value);
    timer.value = null;
  }
  timer.value = setTimeout(() => {
    clickCount.value = 0;
  }, 1000);
};
getLanguageList();
getAppVersion();

const logout = () => {
  popupRef.value.open();
};

const logoutConfirm = async () => {
  await postLogout();
  popupRef.value.close();
  removeStorageSync(BMOS_ACCESS_TOKEN);
  uni.reLaunch({
    url: '/pages/login/index',
  });
};

const inputAttrs = reactive({
  color: 'rgba(48, 49, 51, 1)',
  fontSize: '12.9rpx',
  placeholderStyle: { 'color': 'rgba(149, 162, 183, 1)', 'font-size': '12.9rpx', 'weight': 400 },
  customStyle: {
    'height': '35.17rpx',
    'box-sizing': 'border-box',
    'border-radius': '7.03rpx',
  },
  onBlur: () => {
    password.new2 && pwdFormRef.value.validateField('new2');
  },
});
const popupPwdRef = ref(null);
const pwdFormRef = ref(null);
const pwdRules = ref({
  new1: [{
    required: true,
    message: t('新密码不能为空'),
    trigger: ['change'],
  }],
  new2: [{
    required: true,
    message: t('确认密码不能为空'),
    trigger: ['change'],
  }, {
    validator: () => {
      // 返回true表校验通过，返回false表示不通过
      if (!password.new2) {
        return true;
      }
      return password.new1 === password.new2;
    },
    message: t('两次密码不一致'),
    trigger: ['blur'],
  }],
});
const password = reactive({
  new1: '',
  new2: '',
  pwd1: true,
  pwd2: true,
});
  // 密码修改弹框
const openPwdPopup = () => {
  popupPwdRef.value.open();
};
const closePwdPopup = () => {
  password.new1 = '';
  password.new2 = '';
  popupPwdRef.value.close();
};
const submitPwdPopup = () => {
  pwdFormRef.value.validate().then(() => {
    postChangePwd({
      password: encrypt(password.new1),
    }).then((res) => {
      if (res.code === 0) {
        closePwdPopup();
        userInfo.value.password = '';
        removeStorageSync(BMOS_ACCESS_TOKEN);
        uni.showToast({
          title: t('修改成功'),
          icon: 'none',
        });
        uni.reLaunch({
          url: '/pages/login/index',
        });
      }
      else {
        uni.showToast({
          title: res.message,
          icon: 'none',
        });
      }
    }).catch((err) => {
      err.message && uni.showToast({
        title: err.message,
        icon: 'none',
      });
    });
  });
};

const showUpdateModal = ref(false);

// app版本更新
const showAppVersionPopup = ref(false);
const appVersionHandel = async () => {
  const res = await checkUpdateApp();
  if (res) {
    showAppVersionPopup.value = true;
  }
  else {
    showUpdateModal.value = true;
  }
};

const handleUpdateAppVersion = () => {
  nextTick(() => {
    showUpdateModal.value = true;
    download();
  });
};

// 语言切换
const languagePopupRef = ref(null);
const languageHandle = async () => {
  checkedLanguage.value = currentLanguage.value;
  languagePopupRef.value.open();
};

// 清除缓存
const clearCacheHandle = () => {
  clearCache();
};

async function getLanguageList() {
  try {
    const res = getParameterByCode('platform.sys.language');
    const data = JSON.parse(res.value);
    languageListRef.value = Object.keys(data).map((item) => {
      return {
        label: t(item),
        value: languageType[data[item]],
      };
    });
  }
  catch (error) {
    console.log(error);
  }
}
const languageItemClick = (value) => {
  checkedLanguage.value = value;
};
const languageComfirm = () => {
  if (checkedLanguage.value !== currentLanguage.value) {
    onLocaleChange({
      code: checkedLanguage.value,
    });
  }
};

// 手写签名
const toHandleSign = () => {
  uni.navigateTo({
    url: '/pages/home/personal/HandleSign',
  });
};

// 扫码串口
const toScanSerialPort = () => {
  showScanSerialPort.value = true;
};

// 主页左侧导航栏设置
const showTabBarModal = ref(false);
const tabBarStore = useTabbarStore();
const { tabBars } = storeToRefs(tabBarStore);
const { updateTabBars } = tabBarStore;
const newTabBars = ref([]);

watch(() => tabBars.value, (newVal) => {
  newTabBars.value = cloneDeep(newVal).filter(item => item.id !== 1 && item.id !== 2);
}, { immediate: true, deep: true });
const tabBarHandle = () => {
  showTabBarModal.value = true;
};
const confirmTabBar = () => {
  updateTabBars(newTabBars.value);
  showTabBarModal.value = false;
};

onShow(() => {
  getDeviceInfo();
  userInfo.value = getStorageSync(USER_INFO);
  scanSerialConfirm();
});
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;

  .logout-popup {
    width: 246.19rpx;
    height: 138.34rpx;
    background-color: #fff;
    padding: 29.89rpx 9.38rpx 9.38rpx;
    box-sizing: border-box;

    .text {
      font-size: 15.24rpx;
      color: #242526;
      text-align: center;
      display: block;
      margin-bottom: 29.89rpx;
    }
  }

  .user-box {
    position: relative;
    background-image: url('@/static/homeIcon/user-info-bg.png');
    background-repeat: no-repeat;
    background-size: 100% 124.27rpx;
    width: 100%;
    height: 124.27rpx;

    .info {
      position: absolute;
      left: 23.45rpx;
      bottom: 35.17rpx;
      display: flex;
      align-items: center;

      .avatar {
        background-color: #fff;
        width: 35.17rpx;
        height: 35.17rpx;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 7.03rpx;
      }

      .name-box {
        display: flex;
        align-items: center;

        .name {
          color: #18191a;
          font-size: 15.24rpx;
        }

        .account {
          color: #545659;
          font-size: 11.72rpx;
        }
      }
    }

    .logout {
      position: absolute;
      top: 32.83rpx;
      right: 23.45rpx;
    }
  }

  .content {
    width: 100%;
    height: calc(100% - 124.27rpx);
    padding: 14.07rpx 9.38rpx;
    box-sizing: border-box;

    .title {
      color: #909398;
      font-size: 11.72rpx;
      padding: 14.07rpx 4.69rpx 4.69rpx;
    }
  }
}

.language-content {
  width: 375.15rpx;
  height: 200.47rpx;
  padding: 9.38rpx;
  box-sizing: border-box;

  .title {
    font-size: 15.24rpx;
    color: #242526;
    text-align: center;
  }

  .language-list {
    width: 100%;
    height: 98.48rpx;
    margin: 9.38rpx 0;
    box-sizing: border-box;
    overflow-y: auto;

    .language-item {
      width: 100%;
      height: 32.82rpx;
      padding: 7.03rpx 9.38rpx;
      box-sizing: border-box;
      color: #4d4d4d;
      font-size: 12.9rpx;
      display: flex;
      justify-content: space-between;
    }

    .language-item-active {
      background-color: #d9e5ff;
      color: #2871ff;
      border-radius: 4.69rpx;
    }
  }
}

.app-version-content {
  width: 246.19rpx;
  height: 138.34rpx;
  padding: 32.83rpx 9.38rpx 9.38rpx;
  box-sizing: border-box;

  .text-content-new {
    color: #242526;
    font-size: 15.24rpx;
    text-align: center;
    margin-bottom: 29.31rpx;
  }
}

.popup-pwd-content {
  width: 375.15rpx;
  height: 294.26rpx;
  padding: 18.76rpx;
  box-sizing: border-box;
  border-radius: 7.03rpx;
  display: flex;
  flex-direction: column;

  .uv-form-item {
    height: 89.1rpx;

    :deep(.uv-form-item__body__right__message) {
      margin-top: 2.93rpx;
      font-size: 10.55rpx;
      color: #ff5633;
    }
  }

  .popup-pwd-close {
    width: 100%;
    height: 14.07rpx;
    position: relative;
    color: #303133;
    font-size: 14.07rpx;

    .close-svg {
      position: absolute;
      right: 0;
      top: 0;
    }
  }
}

.tabbar-modal-content {
  background-color: var(--bmos-bg-form);
  border-radius: 4.69rpx;
  display: flex;
  padding: 0 9.38rpx;
  flex-direction: column;
  align-items: flex-start;
  .tabbar-modal-item {
    border-bottom: 0.59rpx solid var(--bmos-color-border);
    display: flex;
    padding: 10.55rpx 0;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
    .wd-switch.is-checked {
      background: #59bf78;
      border-color: #59bf78;
    }
    .text {
      font-size: 12.89rpx;
    }
  }
  .tabbar-modal-item:last-child {
    border-bottom: none;
  }
}
</style>

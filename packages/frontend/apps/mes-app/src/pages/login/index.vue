<template>
  <view class="content">
    <view class="container">
      <view class="logo-class">
        <uv-image :src="logoRef" width="100%" height="100%" mode="scaleToFill">
          <template #error>
            <uv-image src="/static/loginIcon/login-logo.png" width="100%" height="100%" mode="scaleToFill" />
          </template>
        </uv-image>
      </view>
      <view class="left">
        <view class="image-box">
          <uv-image src="/static/loginIcon/left.png" width="100%" height="100%" mode="scaleToFill" />
        </view>
        <view class="ip-box" @click="openIpConfig">
          <uv-icon name="fuwuqi" custom-prefix="bmos-icon" size="17.58rpx" color="#303133" />
          <text class="ip-input">
            {{ ipConfig || t('服务器地址') }}
          </text>
          <uv-icon name="jiantou-you" custom-prefix="bmos-icon" size="14.07rpx" color="#303133" />
        </view>
      </view>
      <view class="login-box">
        <text class="title">
          {{ t('欢迎登录') }}
        </text>
        <uv-gap height="35.17rpx" bg-color="#fff" />
        <uv-form ref="formRef" label-width="0" label-position="left" :model="userInfo" :rules="rules">
          <uv-form-item custom-style="padding:0;" label="" prop="loginName">
            <uv-input v-model="userInfo.loginName" :placeholder="t('请输入账号')" v-bind="inputAttrs">
              <template #prefix>
                <uv-icon class="icon-svg" name="zhanghao" custom-prefix="bmos-icon" size="12.9rpx" color="#303133" />
              </template>
            </uv-input>
          </uv-form-item>
          <uv-form-item custom-style="padding:0;" label="" prop="password">
            <uv-input
              v-model="userInfo.password" :password="loginPassword" :placeholder="t('请输入密码')"
              v-bind="inputAttrs"
            >
              <template #prefix>
                <uv-icon class="icon-svg" name="mima" custom-prefix="bmos-icon" size="12.9rpx" color="#303133" />
              </template>
              <template #suffix>
                <uv-icon
                  v-if="loginPassword" name="yulan-guan" custom-prefix="bmos-icon" size="12.9rpx" color="#A1A5B2"
                  @click="loginPassword = false"
                />
                <uv-icon
                  v-else name="yulan-kai" custom-prefix="bmos-icon" size="12.9rpx" color="#A1A5B2"
                  @click="loginPassword = true"
                />
              </template>
            </uv-input>
          </uv-form-item>
        </uv-form>
        <uv-gap height="36.34rpx" bg-color="#fff" />
        <uv-button :custom-style="customStylePrimary" @click="loginClick">
          {{ t('登录') }}
        </uv-button>
      </view>
    </view>
  </view>
  <!-- 服务器弹框 -->
  <uv-popup ref="popupRef" mode="center" background-color="#fff" round="7.03rpx">
    <view class="popup-content">
      <view class="popup-close">
        <uv-icon
          class="close-svg" name="guanbi" custom-prefix="bmos-icon" size="14.07rpx" color="#909398"
          @click="closeIpConfig"
        />
      </view>
      <uv-gap height="29.31rpx" bg-color="#fff" />
      <view class="popup-input">
        <uv-form
          ref="ipConfigRef" label-width="140.68rpx" label-position="top" :model="ipConfigInput" :rules="ipRules"
          :label-style="{ 'clolr': '#303133', 'font-size': '14.07rpx', 'margin-bottom': '3.52rpx' }"
        >
          <uv-form-item custom-style="padding:0;" :label="`${t('服务器地址')}：`" prop="ip">
            <uv-input v-model="ipConfigInput.ip" :placeholder="t('请输入IP地址')" v-bind="inputAttrs" />
          </uv-form-item>
        </uv-form>
      </view>
      <view class="popup-buttons">
        <uv-button :custom-style="customStyleInfo" @click="closeIpConfig">
          {{ t('取消') }}
        </uv-button>
        <uv-button :custom-style="customStylePrimarySubmit" @click="submitIpConfig">
          {{ t('确定') }}
        </uv-button>
      </view>
    </view>
  </uv-popup>
  <!-- 第一次登录修改密码弹框 -->
  <uv-popup ref="popupPwdRef" mode="center" background-color="#fff" round="7.03rpx" :close-on-click-overlay="false">
    <view class="popup-pwd-content">
      <view class="popup-pwd-close">
        <text>{{ t('修改密码') }}</text>
        <uv-icon
          class="close-svg" name="guanbi" custom-prefix="bmos-icon" size="14.07rpx" color="#909398"
          @click="closePwdPopup"
        />
      </view>
      <text v-if="showSub" style="margin-top: 9.38rpx;color: var(--bmos-color-text-sub);">
        {{ t('您的密码已过期，请修改密码') }}
      </text>
      <uv-gap height="29.31rpx" bg-color="#fff" />
      <uv-form
        ref="pwdFormRef" label-width="140.68rpx" label-position="top" :model="password" :rules="pwdRules"
        :label-style="{ 'clolr': '#303133', 'font-size': '14.07rpx', 'margin-bottom': '3.52rpx' }"
      >
        <uv-form-item custom-style="padding:0;" :label="`${t('新密码')}：`" prop="new1">
          <uv-input v-model="password.new1" :placeholder="t('请输入新密码')" :password="password.pwd1" v-bind="inputAttrs">
            <template #suffix>
              <uv-icon
                v-if="password.pwd1" name="yulan-guan" custom-prefix="bmos-icon" size="12.9rpx" color="#A1A5B2"
                @click="password.pwd1 = false"
              />
              <uv-icon
                v-else name="yulan-kai" custom-prefix="bmos-icon" size="12.9rpx" color="#A1A5B2"
                @click="password.pwd1 = true"
              />
            </template>
          </uv-input>
        </uv-form-item>
        <uv-form-item custom-style="padding:0;" :label="`${t('确认密码')}：`" prop="new2">
          <uv-input v-model="password.new2" :placeholder="t('请输入确认密码')" :password="password.pwd2" v-bind="inputAttrs">
            <template #suffix>
              <uv-icon
                v-if="password.pwd2" name="yulan-guan" custom-prefix="bmos-icon" size="12.9rpx" color="#A1A5B2"
                @click="password.pwd2 = false"
              />
              <uv-icon
                v-else name="yulan-kai" custom-prefix="bmos-icon" size="12.9rpx" color="#A1A5B2"
                @click="password.pwd2 = true"
              />
            </template>
          </uv-input>
        </uv-form-item>
      </uv-form>
      <view class="popup-pwd-buttons">
        <uv-button :custom-style="customStyleInfo" @click="closePwdPopup">
          {{ t('取消') }}
        </uv-button>
        <uv-button :custom-style="customStylePrimarySubmit" @click="submitPwdPopup">
          {{ t('确定') }}
        </uv-button>
      </view>
    </view>
  </uv-popup>
  <uv-modal
    ref="modalRef" width="300rpx" :title="modalTitle" :content="modalContent" align="center"
    @confirm="confirmModal"
  />
  <BMMessageBox
    v-model="showUpdateModal" :title="t('应用版本与服务器不一致')" :content="getAppVersion()" :confirm-text="t('更新')"
    :close-on-click-modal="false" @cancel="showUpdateModal = false" @confirm="handleUpdateAppVersion"
  />
</template>

<script setup>
import { postChangePwd, postLogin } from '@/api/systemApi.js';
import { BMMessageBox } from '@/BMComponents';
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { checkUpdateApp, download, getAppVersion } from '@/utils/checkUpdateApp.js';
import { encrypt } from '@/utils/encrypt.js';
import { getCustomStyle } from '@/utils/getCustomStyle.js';
import { LOCK_SCREEN_TIME_CODE } from '@/utils/request/config.js';
import { getServerTime } from '@/utils/time.js';
import { BMOS_ACCESS_TOKEN, DEVICE_PRINTER, IP_CONFIG, LOCK_SCREEN_TIME, USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync, removeStorageSync, setStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { setLockTimeout } from '@/utils/useLockScreenTimer.js';
import { onBackPress } from '@dcloudio/uni-app';
import { computed, reactive, ref } from 'vue';

const systemInfoStore = useSystemInfoStore();
const { getAllParameterConfig, getParameterByCode } = systemInfoStore;
onBackPress((options) => {
  console.log('设置锁屏界面不能手势返回', options);
  // 触发返回就会调用此方法，这里实现的是禁用物理返回，顶部导航栏的自定义返回 uni.navigateBack 仍可使用
  if (options.from === 'backbutton') {
    uni.showModal({
      title: t('提示'),
      content: `${t('是否退出app')}?`,
      success(res) {
        if (res.confirm) {
          // 退出当前应用，改方法只在App中生效
          plus.runtime.quit();
        }
        else if (res.cancel) {
          console.log('用户点击取消');
        }
      },
    });
    return true;
  }
  else if (options.from === 'navigateBack') {
    return false;
  }
});
const popupButtonStyle = {
  'width': ' 161.78rpx',
  'height': '35.17rpx',
  'border-radius': '4.69rpx',
  'font-size': ' 14.07rpx',
};
const customStylePrimary = getCustomStyle('primary', {
  width: '100%',
  height: '35.17rpx',
});
const customStylePrimarySubmit = getCustomStyle('primary', { ...popupButtonStyle });
const customStyleInfo = getCustomStyle('default', popupButtonStyle);
const rules = ref({
  loginName: [{
    required: true,
    message: t('账号不能为空'),
    trigger: ['change'],
  }, {
    min: 2,
    max: 18,
    validator: (rule, value) => {
      // 返回true表校验通过，返回false表示不通过
      return /^[A-Z0-9]*$/i.test(value);
    },
    message: t('账号输入有误'),
    trigger: ['change'],
  }],
  password: [{
    required: true,
    message: t('密码不能为空'),
    trigger: ['change'],
  }],
});
const pwdFormRef = ref(null);
const password = reactive({
  new1: '',
  new2: '',
  pwd1: true,
  pwd2: true,
});
const inputAttrs = reactive({
  color: 'rgba(48, 49, 51, 1)',
  fontSize: '12.9rpx',
  placeholderStyle: { 'color': 'rgba(149, 162, 183, 1)', 'font-size': '12.9rpx', 'weight': 400, 'height': '14.06rpx' },
  customStyle: {
    'height': '35.17rpx',
    'box-sizing': 'border-box',
    'border-radius': '7.03rpx',
  },
  onBlur: () => {
    password.new2 && pwdFormRef.value.validateField('new2');
  },
});
const formRef = ref(null);
const popupRef = ref(null);
const popupPwdRef = ref(null);
const modalRef = ref(null);

const loginPassword = ref(true);
const ipConfig = ref('');
const ipConfigInput = reactive({
  ip: '',
});
const ipConfigRef = ref(null);
const remindExpire = ref(false);
const ipRules = ref({
  ip: [{
    required: true,
    message: t('服务器地址不能为空'),
    trigger: ['change'],
  }, {
    validator: (rule, value) => {
      // 返回true表校验通过，返回false表示不通过
      return /^((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?):\d{1,5}$/.test(value);
    },
    message: t('服务器格式错误'),
    trigger: ['change'],
  }],
});

const logoRef = computed(() => {
  return `http://${ipConfig.value}/front-end/app/assets/logo/login-logo.png`;
});

const userInfo = reactive({
  loginName: '',
  password: '',
});

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

const modalTitle = ref('');
const modalContent = ref('');
const showSub = ref(false);
const showUpdateModal = ref(false);

// 密码修改弹框
const openPwdPopup = () => {
  popupPwdRef.value.open();
};
const handleUpdateAppVersion = () => {
  download();
};
const loginClick = async () => {
  const flag = await checkUpdateApp();
  if (!flag) {
    showUpdateModal.value = true;
    return;
  }
  formRef.value.validate().then(() => {
    postLogin({ loginName: userInfo.loginName, terminalType: 1, serviceType: 'MES', password: encrypt(userInfo.password) }).then(async (res) => {
      if (res.code === 0) {
        setStorageSync(USER_INFO, { ...res.data } || {});
        setStorageSync(BMOS_ACCESS_TOKEN, res.data?.token || '');
        switch (res.data.activeStatus) {
          case 0:
            openPwdPopup();
            showSub.value = false;
            userInfo.password = '';
            break;
          case 1:
            try {
              if (res.data.remindExpire) {
                remindExpire.value = true;
                modalTitle.value = t('提示');
                modalContent.value = t('您的密码即将到期，请尽快更换以保持账户安全。');
                modalRef.value.open();
              }
              else {
                // 登录成功获取所有的参数配置
                await getAllParameterConfig();
                // 获取锁屏时间
                const lockTime = getParameterByCode(LOCK_SCREEN_TIME_CODE);
                setStorageSync(LOCK_SCREEN_TIME, Number(lockTime.value));
                setLockTimeout();
                getServerTime();
                uni.navigateTo({
                  url: '/pages/home/index',
                });
              }
            }
            catch (error) {
              uni.showToast({
                title: error.message,
                icon: 'none',
              });
            }
            break;
          case 2:
            openPwdPopup();
            showSub.value = false;
            userInfo.password = '';
            break;
          default:
            break;
        }
      }
      else if (res.code === 8104010) {
        modalTitle.value = t('账号已停用');
        modalContent.value = t('您的账号已被停用，请咨询管理员');
        modalRef.value.open();
      }
      else if (res.code === 8104008) {
        openPwdPopup();
        showSub.value = true;
        userInfo.password = '';
      }
      else {
        uni.showToast({
          title: res.message,
          icon: 'none',
        });
      }
    }).catch((err) => {
      // 非200||401状态提示
      uni.showToast({
        title: err.message || t('登录失败'),
        icon: 'none',
      });
    });
  });
};

// 服务器地址弹框
const openIpConfig = () => {
  ipConfigInput.ip = ipConfig.value;
  popupRef.value.open();
};
const closeIpConfig = () => {
  popupRef.value.close();
};
const submitIpConfig = () => {
  ipConfigRef.value.validate().then(() => {
    if (ipConfig.value !== ipConfigInput.ip) {
      ipConfig.value = ipConfigInput.ip;
      removeStorageSync(DEVICE_PRINTER);
      setStorageSync(IP_CONFIG, ipConfig.value);
    }
    popupRef.value.close();
  });
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
        userInfo.password = '';
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
// modal弹框
const confirmModal = async () => {
  if (remindExpire.value) {
    // 登录成功获取所有的参数配置
    await getAllParameterConfig();
    // 获取锁屏时间
    const lockTime = getParameterByCode(LOCK_SCREEN_TIME_CODE);
    setStorageSync(LOCK_SCREEN_TIME, Number(lockTime.value));
    setLockTimeout();
    getServerTime();
    uni.navigateTo({
      url: '/pages/home/index',
    });
  }
  modalRef.value.close();
  remindExpire.value = false;
};

if (!getStorageSync(IP_CONFIG)) {
  setStorageSync(IP_CONFIG, '172.30.1.160:80');
}
ipConfig.value = getStorageSync(IP_CONFIG) || '';
removeStorageSync(BMOS_ACCESS_TOKEN);
</script>

<style scoped lang="scss">
.content {
  width: 100%;
  height: 100%;
  background-color: #ffffff;
  :deep(.uv-input__content__field-wrapper__field) {
    line-height: 14.06rpx;
  }
}

.container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  background-image: url('@/static/loginIcon/background.png');
  background-size: 661.78rpx 485.35rpx;
  background-position: -22.27rpx -8.21rpx;
  background-repeat: no-repeat;
  box-sizing: border-box;

  .logo-class {
    position: absolute;
    top: 40.45rpx;
    left: 46.89rpx;
    width: 148.89rpx;
    height: 20.52rpx;
  }

  .left {
    width: 457.21rpx;
    height: 100%;
    position: relative;

    .image-box {
      position: absolute;
      top: 127.2rpx;
      left: 92.03rpx;
      width: 317.12rpx;
      height: 226.85rpx;
    }

    .ip-box {
      position: absolute;
      left: 23.45rpx;
      bottom: 23.45rpx;
      display: flex;
      align-items: center;
      font-size: 14.07rpx;
      font-weight: 400;
    }

    .ip-input {
      margin: 0 4.69rpx;
    }
  }

  .login-box {
    width: 260.26rpx;
    height: 351.7rpx;
    padding: 18.76rpx;
    box-sizing: border-box;
    border-radius: 11.72rpx;
    border: 1px solid rgba(211, 216, 247, 1);
    box-shadow:
      10px 8px 12px 0px rgba(0, 0, 0, 0.04),
      -10px -8px 12px 0px rgba(0, 0, 0, 0.05);
    background-color: #ffffff;
    display: flex;
    flex-direction: column;

    .uv-form-item {
      height: 70.34rpx;

      :deep(.uv-form-item__body__right__message) {
        margin-top: 2.93rpx;
        font-size: 10.55rpx;
        color: #ff5633;
      }
    }

    .title {
      color: rgba(48, 49, 51, 1);
      font-size: 18.76rpx;
      font-weight: 400;
      line-height: 26.38rpx;
      text-align: center;
      display: block;
      width: 100%;
    }

    .icon-svg {
      margin-right: 14.07rpx;
    }
  }
}

.popup-content {
  width: 375.15rpx;
  height: 205.16rpx;
  padding: 18.76rpx;
  box-sizing: border-box;
  border-radius: 7.03rpx;
  display: flex;
  flex-direction: column;

  .popup-close {
    width: 100%;
    height: 14.07rpx;
    position: relative;

    .close-svg {
      position: absolute;
      right: 0;
      top: 0;
    }
  }

  .uv-form-item {
    height: 89.1rpx;

    :deep(.uv-form-item__body__right__message) {
      margin-top: 2.93rpx;
      font-size: 10.55rpx;
      color: #ff5633;
    }
  }

  .popup-buttons {
    display: flex;
    justify-content: space-between;
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

  .popup-pwd-buttons {
    display: flex;
    justify-content: space-between;
  }
}
</style>

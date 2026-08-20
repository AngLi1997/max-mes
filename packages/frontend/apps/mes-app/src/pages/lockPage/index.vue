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
      </view>
      <view class="login-box">
        <text class="title">
          {{ t('欢迎回来') }}
        </text>
        <uv-gap height="35.17rpx" bg-color="#fff" />
        <uv-form ref="formRef" label-width="0" label-position="left" :model="userInfo" :rules="rules">
          <text class="loginName">
            {{ user.loginName }}
          </text>
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
        <uv-gap height="52.75rpx" bg-color="#fff" />
        <uv-button :custom-style="customStylePrimary" @click="loginClick">
          {{ t('解锁') }}
        </uv-button>
        <view class="logout-box">
          <text @click="logout">
            {{ t('更换用户') }}
          </text>
        </view>
      </view>
    </view>
  </view>
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
        <uv-form-item custom-style="padding:0;" :label="`${t('新密码')}:`" prop="new1">
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
        <uv-form-item custom-style="padding:0;" :label="`${t('确认密码')}:`" prop="new2">
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
</template>

<script setup>
import { postChangePwd, postLogin, postLogout } from '@/api/systemApi.js';
import { encrypt } from '@/utils/encrypt.js';
import { getCustomStyle } from '@/utils/getCustomStyle.js';
import { getServerTime } from '@/utils/time.js';
import { BMOS_ACCESS_TOKEN, IP_CONFIG, USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync, removeStorageSync, setStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { onBackPress } from '@dcloudio/uni-app';
import { computed, onMounted, reactive, ref } from 'vue';

onBackPress((options) => {
  console.log('设置锁屏界面不能手势返回', options);
  // 触发返回就会调用此方法，这里实现的是禁用物理返回，顶部导航栏的自定义返回 uni.navigateBack 仍可使用
  if (options.from === 'backbutton') {
    return true;
  }
  else if (options.from === 'navigateBack') {
    return false;
  }
});
const password = reactive({
  new1: '',
  new2: '',
  pwd1: true,
  pwd2: true,
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
  password: [{
    required: true,
    message: t('密码不能为空'),
    trigger: ['change'],
  }],
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
});
const formRef = ref(null);
const popupPwdRef = ref(null);
const modalRef = ref(null);
const loginPassword = ref(true);
const ipConfig = ref('');
const logoRef = computed(() => {
  return `http://${ipConfig.value}/front-end/app/assets/logo/login-logo.png`;
});

const user = getStorageSync(USER_INFO) || {};
const userInfo = reactive({
  loginName: user.loginName,
  password: '',
});
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
      return password.new1 === password.new2;
    },
    message: t('两次密码不一致'),
    trigger: ['blur'],
  }],
});

const modalTitle = ref('');
const modalContent = ref('');
const showSub = ref(false);
// modal弹框
const confirmModal = () => {
  modalRef.value.close();
};
// 密码修改弹框
const openPwdPopup = () => {
  popupPwdRef.value.open();
};

const loginClick = async () => {
  formRef.value.validate().then(() => {
    postLogin({ loginName: userInfo.loginName, terminalType: 1, serviceType: 'MES', password: encrypt(userInfo.password) }).then((res) => {
      setStorageSync(USER_INFO, { ...res.data } || {});
      setStorageSync(BMOS_ACCESS_TOKEN, res.data?.token || '');
      if (res.code === 0) {
        switch (res.data.activeStatus) {
          case 0:
            openPwdPopup();
            showSub.value = false;
            break;
          case 1:
            getServerTime();
            uni.navigateBack();
            break;
          case 2:
            openPwdPopup();
            showSub.value = false;
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
      }
      else {
        uni.showToast({
          title: res.message,
          icon: 'error',
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

const logout = async () => {
  await postLogout();
  removeStorageSync(BMOS_ACCESS_TOKEN);
  uni.reLaunch({
    url: '/pages/login/index',
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
          icon: 'error',
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
onMounted(() => {
  ipConfig.value = getStorageSync(IP_CONFIG) || '';
});
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
  background-image: url('/static/loginIcon/background.png');
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

    .ip-svg {
      width: 17.58rpx;
      height: 17.58rpx;
      margin-right: 4.69rpx;
    }

    .ip-input {
      margin-right: 4.69rpx;
    }

    .ip-svg-arrow {
      width: 14.07rpx;
      height: 14.07rpx;
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

    .loginName {
      display: block;
      color: #303133;
      font-size: 18.76rpx;
      margin-bottom: 23.45rpx;
    }

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

    .logout-box {
      padding-top: 17.58rpx;
      color: #2871ff;
      font-size: 12.9rpx;
      text-align: center;
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

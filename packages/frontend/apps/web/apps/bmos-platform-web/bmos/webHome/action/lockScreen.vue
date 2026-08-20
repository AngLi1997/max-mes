<template>
  <!-- 锁屏界面 -->
  <Modal
    v-model:open="open"
    class="Modal"
    width="100%"
    :footer="null"
    :closable="false"
    :closeIcon="''"
    wrap-class-name="full-modal"
    :keyboard="Esc">
    <div class="pc-login">
      <!--左上角logo-->
      <div class="left-top-logo">
        <img :src="getLogoUrl('login-logo.svg')" style="width: 100%" />
      </div>
      <div class="middle">
        <div class="middle_left" :style="middleLeftBgStyle"></div>
        <div class="middle_right">
          <div class="title">{{ t('欢迎回来') }}</div>
          <!-- 用户账号密码输入表单 -->
          <Form ref="formRef" :model="formState" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
            <!-- 锁屏时的用户名称 -->
            <div class="userName">{{ props.userIofo.userName }}</div>
            <Form.Item label="" name="password">
              <BMPasswordInput v-model:value="formState.password" class="inputPassword" @keyup.enter="login">
                <template #prefix>
                  <BMIcons icon="Password" style="width: 21px; height: 22px"></BMIcons>
                </template>
              </BMPasswordInput>
            </Form.Item>
          </Form>
          <Button type="primary" :loading="loading" class="login" @click="login">
            {{ t('解锁') }}
          </Button>
          <div class="loginOut">
            <Button type="link" @click="loginOut">
              {{ t('更换用户') }}
            </Button>
          </div>
        </div>
      </div>
    </div>
  </Modal>
</template>

<script setup lang="ts">
  import { ref, reactive, computed } from 'vue';
  import type { UnwrapRef } from 'vue';
  import type { Rule } from 'ant-design-vue/es/form';
  import { clearLockStatus } from '../../lock/lock';
  import { Modal, message, Form, Button } from 'ant-design-vue';
  import { sso } from '@bmos/messager';
  import { userLogin, logout } from '../../../login/api';
  import { BMIcons } from '@bmos/icons';
  import { getLogoUrl } from '@bmos/utils';
  import { BMPasswordInput } from '@bmos/components';
  import { SERVICE_TYPE, TerminalType } from '../../../login/login/const';
  import { encrypt } from '@bmos/utils';
  import { t } from '@bmos/i18n';

  const emit = defineEmits(['lockChange']);
  const open = ref<boolean>(false);
  const Esc = ref<boolean>(false);

  const { navigatorLoginPage, getUserInfo, setUserToken } = sso;

  const props = defineProps({
    userIofo: {
      type: Object,
      default: () => ({}),
    },
    start: {
      type: Function,
      default: () => () => {},
    },
  });
  interface FormState {
    password: string;
  }

  const middleLeftBgStyle = computed(() => {
    return {
      backgroundImage: `url('${getLogoUrl('login-img.png')}')`,
    };
  });

  const formRef = ref();
  const labelCol = { span: 5 };
  const wrapperCol = { span: 20, offset: 2 };

  const formState: UnwrapRef<FormState> = reactive({
    password: '',
  });
  const showModal = () => {
    open.value = true;
    formState.password = '';
  };
  // 表单校验
  const rules: Record<string, Rule[]> = {
    password: [{ required: true, trigger: 'blur', message: t('密码不能为空') }],
  };

  // 解锁按钮
  const loading = ref<boolean>(false);
  const login = async () => {
    const passwordForm = await formRef.value?.validate();
    const { loginName } = getUserInfo();
    const data = {
      loginName,
      password: encrypt(passwordForm.password),
      serviceType: SERVICE_TYPE,
      terminalType: TerminalType.PC,
    };
    try {
      loading.value = true;
      const res: any = await userLogin(data);
      if (res.code === 0) {
        setUserToken(res.data.token);
        props.start();
        open.value = false;
        clearLockStatus(props.userIofo.userId);
        emit('lockChange');
      }
    } catch (error: any) {
      message.error(error.message);
    } finally {
      loading.value = false;
    }
  };
  // 退出登录按钮
  const loginOut = async () => {
    try {
      const res = await logout();
      if (res.code === 0) {
        clearLockStatus(props.userIofo.userId);
        navigatorLoginPage();
        setUserToken('');
      } else {
        message.error(res.message);
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  defineExpose({ showModal });
</script>

<style lang="less">
  // 大背景
  .pc-login {
    // 左上角logo
    .left-top-logo {
      position: absolute;
      top: 80px;
      left: 100px;
      z-index: 1;
    }
    display: flex;
    align-items: center;
    width: 100%;
    height: 100vh;
    min-width: 1280px;
    min-height: 700px;
    background-color: #fff;
    background-image: url('../../assets/img/bg.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    // 中间区域
    .middle {
      display: flex;
      align-items: center;
      justify-content: space-evenly;
      box-sizing: border-box;
      width: 100%;
      height: 65%;
      // 中左图标
      .middle_left {
        width: 51%;
        height: 100%;
        background-position: center;
        background-size: 100% 100%;
        background-repeat: no-repeat;
      }
      // 右边表单
      .middle_right {
        width: 23%;
        height: 84%;
        min-height: 410px;
        margin-right: 100px;
        border: 1px solid #d3d8f7;
        box-shadow: 0px 0px 40px 1px rgba(10, 58, 153, 0.1);
        border-radius: 16px;
        display: flex;
        flex-direction: column;
        position: relative;
        .title {
          margin-top: 40px;
          // margin-bottom: 60px;
          margin-bottom: 10.5%;
          text-align: center;
          font-size: 30px;
          color: #303133;
        }
        .login {
          width: 84%;
          height: 52px;
          border-radius: 4px;
          font-size: 18px;
          font-weight: 500;
          color: #ffffff;
          position: absolute;
          bottom: 15%;
          left: 8%;
        }
        // 退出登录按钮
        .loginOut {
          width: 100%;
          position: absolute;
          bottom: 4%;
          text-align: center;
        }

        // 输入框点中时
        .plat-input-affix-wrapper-focused {
          border-color: #3c77e2;
        }
        :deep(.plat-input-affix-wrapper > input.plat-input) {
          padding-left: 10px;
        }
        // 校验失败时候的提示文字
        :deep(.plat-form .plat-form-item-explain-error) {
          font-size: 13px;
        }
        // placeholder字体样式 苹方
        :global(.plat-input::-webkit-input-placeholder) {
          color: #95a2b7;
          font-family: 'Sans-Serif' !important;
        }
        // 表单上下间距
        :deep(.plat-form-item) {
          margin-bottom: 20px;
        }
        .inputPassword {
          margin-top: 10%;
        }
        .user-icon {
          width: 21px;
          height: 22px;
        }
        // 去除输入框蓝色背景
        :deep(input:-webkit-autofill) {
          background: transparent;
          transition: background-color 50000s ease-in-out 0s;
          -webkit-text-fill-color: unset;
        }
        .userName {
          color: #18191a;
          font-size: 26px;
          padding-left: 9%;
        }
      }
    }
  }
  .full-modal {
    .plat-modal {
      max-width: 100%;
      height: 100%;
      top: 0;
      padding-bottom: 0;
      margin: 0;
      .plat-modal-content {
        padding: 0px;
        border-radius: 0px;
      }
    }
    .plat-modal-content {
      display: flex;
      flex-direction: column;
      height: 100%;
    }
    .plat-modal-body {
      flex: 1;
      margin: 0px;
    }
  }
</style>

<style lang="less" scoped>
  // 表单输入框样式
  .plat-input-affix-wrapper {
    height: 52px;
    border-radius: 4px;
    border-color: var(--bmos-first-level-border-color);
    font-size: 18px;
    padding-left: 20px;
  }
  :deep(.plat-input-affix-wrapper .anticon.plat-input-password-icon) {
    color: #0d376a;
    font-size: 16px;
  }
  :deep(.plat-input-affix-wrapper .plat-input-prefix) {
    margin-inline-end: 16px;
  }
</style>

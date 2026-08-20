<template>
  <div class="pc-login">
    <!--左上角logo-->
    <div class="left-top-logo">
      <img :src="getLogoUrl('login-logo.svg')" style="width: 100%" />
    </div>
    <!-- 客户端下载 -->
    <div class="download-link" @click="toDownloadPage">
      <span>{{ t('客户端下载') }}</span>
    </div>
    <!-- 右上角切换语言下拉框 -->
    <div class="selectLanguage">
      <Select
        v-model:value="languageValue"
        :placeholder="t('请选择')"
        :bordered="false"
        style="width: 120px"
        @change="ChangeLanguage">
        <template #suffixIcon>
          <BMIcons icon="SelectIcon" style="width: 12px; height: 12px; transform: translateX(-6px)"></BMIcons>
        </template>
        <SelectOption v-for="item in languageList" :key="item.value" :value="item.value">
          <BMIcons icon="EN" style="width: 14px; height: 14px"></BMIcons>
          {{ item.label }}
        </SelectOption>
      </Select>
    </div>
    <div class="middle">
      <div class="middle_left" :style="middleLeftBgStyle"></div>
      <div class="middle_right">
        <div class="title">{{ t('账号登录') }}</div>
        <!-- 用户账号密码输入表单 -->
        <Form ref="formRef" :model="formState" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
          <Form.Item ref="username" label="" name="username">
            <Input
              v-model:value="formState.username"
              autocomplete="off"
              spellcheck="false"
              :placeholder="t('请输入账号')">
              <template #prefix>
                <BMIcons icon="Zhanghao" style="width: 21px; height: 22px"></BMIcons>
              </template>
            </Input>
          </Form.Item>
          <Form.Item label="" name="password">
            <BMPasswordInput v-model:value="formState.password" class="inputPassword" @keyup.enter="login">
              <template #prefix>
                <BMIcons icon="Password" style="width: 21px; height: 22px" />
              </template>
            </BMPasswordInput>
          </Form.Item>
        </Form>
        <Button type="primary" :loading="loading" class="login" @click="login">
          {{ t('登录') }}
        </Button>
      </div>
    </div>
    <!-- 提示修改密码弹窗 -->
    <changePassword
      :id="id"
      ref="changePasswordRef"
      :userId="userId"
      :token="token"
      :titleTip="titleTip"
      :loginName="loginName"></changePassword>
    <!-- 系统激活弹框 -->
    <activationMadal ref="activationMadalRef" :activateVerification="activateVerification"></activationMadal>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, computed } from 'vue';
  import type { UnwrapRef } from 'vue';
  import type { Rule } from 'ant-design-vue/es/form';
  import { userLogin, determinePlatformActived } from '../api';
  import { message } from 'ant-design-vue';
  import { Modal, Select, Form, Input, Button, SelectOption } from 'ant-design-vue';
  import changePassword from './changePassword/changePassword.vue';
  import activationMadal from './activationMadal/activationMadal.vue';
  import { setUser } from '../utils';
  import { SERVICE_TYPE, TerminalType } from './const';
  import { t, changeLanguage, I18nLanguageType } from '@bmos/i18n';
  import { clearLockStatus } from '../../bmos/lock/lock';
  import { BMIcons } from '@bmos/icons';
  import { getParameter } from '../../src/api/Permissions/menuPermissions';
  import { getLogoUrl } from '@bmos/utils';
  import { BMPasswordInput } from '@bmos/components';
  import { encrypt } from '@bmos/utils';
  import dayjs from 'dayjs';

  interface FormState {
    username: string;
    password: string | undefined;
  }

  const middleLeftBgStyle = computed(() => {
    return {
      backgroundImage: `url('${getLogoUrl('login-img.png')}')`,
    };
  });
  const activateVerification = ref<any>();
  const formRef = ref();
  const labelCol = { span: 5 };
  const wrapperCol = { span: 20, offset: 2 };
  const changePasswordRef = ref();
  const activationMadalRef = ref();
  const userId = ref();
  const token = ref('');
  const id = ref();
  const titleTip = ref('');
  const loginName = ref('');
  const props = defineProps({
    lang: {
      type: String,
      default: 'zh_CN',
    },
  });
  const languageValue = ref(props.lang);
  const languageList = ref([{ label: t('简体中文'), value: 'zh_CN' }]);
  const formState: UnwrapRef<FormState> = reactive({
    username: '',
    password: '',
  });
  const emit = defineEmits(['changeLang']);

  // 账号验证 只能英文 数字
  const validatorAccount = async (_rule: any, value: string) => {
    if (!value) {
      return Promise.reject(t('账号不能为空'));
    } else if (
      !/^[a-zA-Z0-9]{2,18}$/.test(value) //2-18位且有数字 字母
    ) {
      return Promise.reject(t('账号输入有误'));
    } else {
      return Promise.resolve();
    }
  };
  // 表单校验
  const rules: Record<string, Rule[]> = {
    username: [{ required: true, validator: validatorAccount, trigger: 'blur' }],
    password: [{ required: true, trigger: 'blur', message: t('密码不能为空') }],
  };
  // 切换语言
  const ChangeLanguage = async (val: any) => {
    changeLanguage(val as I18nLanguageType);
    emit('changeLang', val);
  };
  // 登录按钮
  const loading = ref(false);
  const login = async () => {
    const passwordForm = await formRef.value?.validate();
    try {
      loading.value = true;
      const data = {
        loginName: passwordForm.username,
        password: encrypt(passwordForm.password),
        serviceType: SERVICE_TYPE,
        terminalType: TerminalType.PC,
      };
      const res: any = await userLogin(data);
      if (res.code === 0) {
        switch (res.data.activeStatus) {
          case 0:
            message.error(t('账号未激活，请先激活账号'));
            userId.value = res.data.userId;
            id.value = res.data.id;
            loginName.value = res.data.loginName;
            token.value = res.data.token;
            changePasswordRef.value.showModal();
            formState.password = undefined;
            break;
          case 2:
            message.error(t('您的密码已过有效期,需修改密码'));
            titleTip.value = t('您的密码已过有效期,需修改密码');
            token.value = res.data.token;
            loginName.value = res.data.loginName;
            changePasswordRef.value.showModal();
            formState.password = undefined;
            break;
          case 1:
            if (res.data.remindExpire) {
              Modal.confirm({
                title: t('提示'),
                content: t('您的密码即将到期，请尽快更换以保持账户安全。'),
                okText: t('确定'),
                cancelButtonProps: {
                  style: {
                    display: 'none',
                  },
                },
                keyboard: false,
                onOk() {
                  setUser(res.data);
                  userId.value = res.data.userId;
                  clearLockStatus(userId.value);
                  message.success(t('登录成功'));
                  // 登录成功后跳转到首页, 删除遗留的缓存
                  localStorage.removeItem('currentAppKey');
                  localStorage.removeItem('currentFullPath');
                },
              });
            } else {
              setUser(res.data);
              userId.value = res.data.userId;
              clearLockStatus(userId.value);
              message.success(t('登录成功'));
              // 登录成功后跳转到首页, 删除遗留的缓存
              localStorage.removeItem('currentAppKey');
              localStorage.removeItem('currentFullPath');
            }
            break;
          default:
            break;
        }
      }
    } catch (error: any) {
      message.error(t(error.message));
      // 新加密码过期让修改密码
      if (error.code === 8104008) {
        titleTip.value = t('您的密码已过有效期,需修改密码');
        loginName.value = passwordForm.username;
        changePasswordRef.value.showModal();
        formState.password = undefined;
      }
    } finally {
      loading.value = false;
    }
  };
  // 获取语言下拉框
  // const getLanguageList = async () => {
  //   try {
  //     const res: any = await getParameter('platform.sys.language');
  //     const obj = JSON.parse(res.data.value);
  //     languageList.value = Object.keys(obj).map((item, i) => {
  //       return {
  //         label: t(item),
  //         value: obj[item],
  //       };
  //     });
  //   } catch (error: any) {
  //     // message.error(error.message);
  //   }
  // };

  // 获取哪些系统需要激活校验
  const getActivationList = async () => {
    try {
      const { data } = await getParameter('platform.sys.actived.service');
      const temp = data.value;
      activateVerification.value = temp;
      return Promise.resolve(temp);
    } catch (error: any) {
      // message.error(error.message);
      return Promise.reject(error);
    }
  };
  // 判断平台的授权码是否已过期
  const determineTime = (date: any) => {
    const currentTime = dayjs();
    const backendTime = dayjs(date);
    if (currentTime.isAfter(backendTime)) {
      //(后端返回时间在当前时间之前) 授权码已经过期
      activationMadalRef.value.showActivationMadal();
    }
  };
  // 查平台是否激活
  const determinePlatformActived1 = async () => {
    try {
      const res: any = await determinePlatformActived({});
      // 平台已授权
      if (res.data.active == true) {
        determineTime(res.data.date);
        // activationMadalRef.value.showActivationMadal(); //临时测试弹窗功能
      } else {
        activationMadalRef.value.showActivationMadal();
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 打开下载新标签页
  const toDownloadPage = () => {
    window.open(window.location.origin + '/app/bmos-platform/download/index.html', '_blank');
  };
  onMounted(async () => {
    // getLanguageList();
    await getActivationList();
    determinePlatformActived1();
    sessionStorage.clear();
  });
</script>

<style scoped lang="less">
  // 大背景
  .pc-login {
    // 左上角logo
    .left-top-logo {
      position: absolute;
      top: 80px;
      left: 100px;
      z-index: 1;
    }
    // 语言下拉样式
    :deep(.plat-select-selector) {
      border: 1px solid #fff;
    }

    .selectLanguage {
      border-radius: 4px;
    }
    .selectLanguage:hover {
      background: #f0f1f2;
    }
    :deep(.plat-select-item-option:hover .selectLanguage) {
      background: #f0f1f2;
    }

    display: flex;
    align-items: center;
    width: 100%;
    height: 100vh;
    min-width: 1280px;
    min-height: 700px;
    background-color: #fff;
    background-image: url('../assets/img/bg.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    position: relative;
    .selectLanguage {
      position: absolute;
      top: 24px;
      right: 48px;
    }
    .download-link {
      position: absolute;
      top: 24px;
      right: 200px;
      line-height: 36px;
      cursor: pointer;
      color: var(--bmos-first-level-text-color);
    }
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
        height: 82%;
        min-height: 425px;
        margin-right: 100px;
        border: 1px solid #d3d8f7;
        box-shadow: 0px 0px 40px 1px rgba(10, 58, 153, 0.1);
        border-radius: 16px;
        display: flex;
        flex-direction: column;
        position: relative;
        .title {
          margin-top: 40px;
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
          bottom: 13%;
          left: 8%;
        }

        //  密码小眼睛
        :deep(.plat-input-affix-wrapper .anticon.plat-input-password-icon) {
          color: #0d376a;
          font-size: 16px;
        }
        // 语言下拉框 // 下拉框
        :deep(.plat-select:not(.plat-select-customize-input) .plat-select-selector) {
          border: none;
        }
        :deep(.plat-input-affix-wrapper .plat-input-prefix) {
          margin-inline-end: 16px;
        }

        // 表单上下间距
        :deep(.plat-form-item) {
          margin-bottom: 5px;
        }
        .inputPassword {
          margin-top: 10%;
        }
        .user-icon {
          width: 21px;
          height: 22px;
        }
        .svg-icon {
          cursor: inherit;
        }
        // 去除输入框蓝色背景
        :deep(input:-webkit-autofill) {
          background: transparent;
          transition: background-color 50000s ease-in-out 0s;
          -webkit-text-fill-color: unset;
        }
      }
    }
  }
</style>

<style lang="less">
  .pc-login {
    // 表单输入框样式
    .plat-input-affix-wrapper {
      height: 52px;
      border-radius: 4px;
      border-color: var(--bmos-first-level-border-color);
      font-size: 18px;
      padding-left: 20px;
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

    // 校验错误的时候输入框点击时候的颜色
    :deep(
        .plat-input-affix-wrapper-status-error:not(.plat-input-affix-wrapper-disabled):not(
            .plat-input-affix-wrapper-borderless
          ).plat-input-affix-wrapper
      ) {
      color: red !important;
    }
  }
</style>

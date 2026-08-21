<template>
  <div class="webHome desk">
    <!-- 上部分 -->
    <div class="desk_header">
      <div class="logo-class" @click="backDesk">
        <div v-if="systemValue === DESK_KEY || logoMode === LogoBackgroundMode.WHITE" class="header_left">
          <!-- 白底logo -->
          <img :src="getLogoUrl('Bmos_logo.svg')" style="width: 200px; height: 50px" />
        </div>
        <div v-else style="width: 200px; height: 50px; background: rgb(16, 53, 102)">
          <!-- 蓝底logo -->
          <img :src="getLogoUrl('Bmos_logoBlue.svg')" style="width: 200px; height: 100%" />
        </div>
      </div>
      <div class="header_all_right">
        <div class="header_right">
          <div class="selectMenu">
            <Select
              v-model:value="systemValue"
              :placeholder="t('请选择')"
              :bordered="false"
              style="width: 160px"
              @change="changeSystem">
              <template #suffixIcon>
                <BMIcons
                  icon="Group"
                  style="width: 14px; height: 14px; transform: translateX(-6px); vertical-align: middle"></BMIcons>
              </template>
              <SelectOption
                v-for="item in systemList"
                :id="item.id"
                :key="item.value"
                :value="item.value"
                :label="item.label">
                <div style="display: flex; align-items: center">
                  <BMIcons
                    :icon="MENU_KEY[item.id] ? MENU_KEY[item.id]?.smallIcon : defaultIcon"
                    style="
                      width: 24px;
                      height: 24px;
                      vertical-align: middle;
                      margin-right: 5px;
                      min-width: 24px;
                    "></BMIcons>
                  <span style="overflow: hidden; text-overflow: ellipsis">{{ item.label }}</span>
                </div>
              </SelectOption>
            </Select>
          </div>
          <Divider type="vertical"></Divider>
          <Dropdown
            placement="bottomRight"
            :overlay-style="{
              width: '150px',
            }">
            <div class="message">
              <Badge :count="messageCount" class="message-badge">
                <BMIcons icon="Message_ring" style="width: 24px; height: 24px"></BMIcons>
              </Badge>
            </div>
            <template #overlay>
              <Menu>
                <template v-for="item in messageCountList" :key="item.categoryCode">
                  <MenuItem @click="() => toMessagePage(item)">
                    <div class="message-item">
                      <span class="message-item-code">
                        {{ t(item.categoryCode) }}
                      </span>
                      <Badge
                        :count="item.number"
                        show-zero
                        :number-style="{
                          backgroundColor: '#FFD7CF',
                          color: '#FF5633',
                        }" />
                    </div>
                  </MenuItem>
                </template>
                <MenuItem v-if="!messageCount">
                  <div class="message-empty">
                    <BMIcons icon="NoMessage" style="width: 50px; height: 50px"></BMIcons>
                    <p class="title">{{ t('暂无消息') }}</p>
                  </div>
                </MenuItem>
              </Menu>
            </template>
          </Dropdown>
          <div class="message">
            <Badge :count="notifyCount[NotifyMessageType.ALL]" class="message-badge">
              <BMIcons icon="Message" style="width: 24px; height: 24px" @click="openNotifyModal"></BMIcons>
            </Badge>
          </div>

          <!-- TODO: 消息预警 -->
          <div v-if="false" class="message" @click="showMessageAlert">
            <Badge :count="warningMsgCount" class="message-badge">
              <BMIcons icon="MessageAlert" style="width: 24px; height: 24px"></BMIcons>
            </Badge>
          </div>

          <!-- 右上角用户信息 -->
          <div class="user">
            <Dropdown>
              <div class="info">
                <BMIcons icon="User" style="width: 24px; height: 24px; color: rgb(108, 115, 128)"></BMIcons>
                <span class="userName">
                  {{ userInfo?.userName }}
                </span>
                <BMIcons icon="Group" style="width: 14px; height: 14px; transform: translate(-6px, -1px)"></BMIcons>
              </div>
              <template #overlay>
                <div class="hoverMenu">
                  <div class="userInfo">
                    <div class="headSculpture">
                      <BMIcons icon="HeadSculpture" style="width: 40px; height: 40px"></BMIcons>
                    </div>
                    <div>
                      <div class="loginName">
                        {{ userInfo?.userName }}
                      </div>
                      <div class="accountName">{{ t('账号') }}: {{ userInfo?.loginName }}</div>
                    </div>
                  </div>
                  <div class="action">
                    <div class="line">
                      <Divider type=""></Divider>
                    </div>
                    <div class="actionItem" @click="changePassWord('1')">
                      <BMIcons
                        icon="ActionPassword"
                        style="width: 16px; height: 16px; color: rgb(108, 115, 128)"></BMIcons>
                      <div class="actionTitle">
                        {{ t('密码设置') }}
                      </div>
                    </div>
                    <div class="actionItem" @click="changePassWord('2')">
                      <BMIcons
                        icon="SignPassword"
                        style="width: 16px; height: 16px; color: rgb(108, 115, 128)"></BMIcons>
                      <div class="actionTitle">
                        {{ t('签名密码设置') }}
                      </div>
                    </div>
                    <div class="actionItem" @click="signSet">
                      <BMIcons icon="Sign" style="width: 16px; height: 16px; color: rgb(108, 115, 128)"></BMIcons>
                      <div class="actionTitle">
                        {{ t('签名设置') }}
                      </div>
                    </div>
                    <div class="actionItem" @click="lockScreen">
                      <BMIcons icon="ActionLock" style="width: 16px; height: 16px"></BMIcons>
                      <div class="actionTitle">
                        {{ t('锁屏') }}
                      </div>
                    </div>
                    <div class="actionItem" @click="languageChange">
                      <BMIcons icon="ActionLanguage" style="width: 16px; height: 16px"></BMIcons>
                      <div class="actionTitle">
                        {{ t('语言设置') }}
                      </div>
                    </div>
                    <div class="line">
                      <Divider type=""></Divider>
                    </div>
                    <div class="actionItem" @click="loggingOut">
                      <BMIcons icon="ActionExit" style="width: 16px; height: 16px"></BMIcons>
                      <div class="actionTitle">
                        {{ t('安全退出') }}
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </Dropdown>
          </div>
        </div>
      </div>
    </div>
    <!-- 中间菜单 -->
    <div class="main">
      <div v-if="appSrc" class="main-app">
        <div v-if="appSrc" class="main-app-iframe">
          <iframe
            :id="appRunner"
            width="100%"
            height="100%"
            :onload="load"
            allowfullscreen
            class="main-app__iframe"
            style="display: block"
            :src="appSrc"></iframe>
        </div>
      </div>
      <div v-else class="menu">
        <div v-for="(item, i) in menuList" :key="i" class="itemMenu" @click="chooseMenu(item)">
          <!-- 此svg会模糊,改为用高清图 -->
          <img :src="MENU_KEY[item.id]?.img || DEFAULT_IMG" alt="" class="imageIcon" />
          <div class="title">
            {{ customizeT(item.id) || item.name }}
          </div>
          <div class="name">
            {{ customizeT(item.EN) || item.EN }}
          </div>
        </div>
      </div>
    </div>
    <!-- 提示修改密码弹窗 -->
    <changePassword ref="changePasswordRef" :type="type"></changePassword>
    <!-- 锁屏-->
    <lockScreenModal
      ref="lockScreenRef"
      :user-iofo="userInfo"
      :start="LOCK.start"
      @lockChange="handleLockChange"></lockScreenModal>
    <!-- 切换语言弹窗 -->
    <changeLanguage ref="changeLanguageRef" @changeLang="changeLang"></changeLanguage>
    <!-- 签名设置 -->
    <SignSetModal v-model="signSetOpen"></SignSetModal>
    <!-- 消息中心 -->
    <NotifyModal v-model="notifyModalOpen" :notifyCount @readItem="readItem" @readAll="readAll"></NotifyModal>
    <!-- AI聊天框 -->
    <Popover v-if="AIChatUrl" placement="leftBottom" trigger="click" overlayClassName="chat-popover">
      <template #content>
        <iframe
          :src="AIChatUrl"
          style="width: 100%; height: 100%; min-height: 77vh"
          frameborder="0"
          allow="microphone"></iframe>
      </template>
      <FloatButton
        :style="{
          right: '24px',
        }"></FloatButton>
    </Popover>
  </div>
  <contextHolder />
</template>
<script setup lang="ts">
  import { ref, computed, createVNode, onMounted, onBeforeMount, onUnmounted, reactive, watch } from 'vue';
  import { t, customizeT } from '@bmos/i18n';
  import { handleAppUrl } from '../utils';
  import { DESK_KEY, MENU_KEY, DESK_NAME, DEFAULT_IMG } from './const';
  import changePassword from './action/changePassword.vue';
  import lockScreenModal from './action/lockScreen.vue';
  import changeLanguage from './action/changeLanguage.vue';
  import SignSetModal from './action/SignSetModal.vue';
  import warnningMessage from './action/warnningMessage.vue';
  import { BMIcons } from '@bmos/icons';
  import { Modal, Select, Dropdown, Menu, MenuItem, Badge, Divider, message, SelectOption } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { sso } from '@bmos/messager';
  import { setLockStatus, getLockStatus, clearLockStatus, LockScreen } from '../lock/lock';
  import { getMenuList, getParameter } from '../../src/api/Permissions/menuPermissions';
  import {
    logout,
    determinePlatformActived,
    determineMesActived,
    determineLimsActived,
    determineWmsActived,
    determineBsmsActived,
    determineBimsActived,
    determineLismsActived,
  } from '../../login/api';
  import { reqMessageCount, reqNoticeWarningUnread } from '../api/info';
  import { getLogoUrl, isNullOrUnDef } from '@bmos/utils';
  import dayjs from 'dayjs';
  import { DefaultOptionType, SelectValue } from 'ant-design-vue/es/select';
  import { MessageRouterMap, NotifyMessageType } from './types';
  import { LogoBackgroundMode } from '@bmos/components';
  import NotifyModal from './action/NotifyModal.vue';
  import { useNotify, useToken } from './hooks';

  const emit = defineEmits(['changeLang']);
  const { getUserInfo, navigatorLoginPage, setUserToken, getUserToken } = sso;
  const lockTime = ref(0); //多久不操作会锁屏
  const defaultIcon = ref('Xitong'); //下拉菜单的默认icon
  const currentAppKey = ref('');
  // const systemValue = ref('bmos-desk');
  const changePasswordRef = ref();
  const type = ref<string>('1');
  const lockScreenRef = ref();
  const changeLanguageRef = ref();
  const shortcutKey = ref<string[]>(['Ctrl', 'L']); //快捷键

  const activedApiEnum = {
    // 各个系统对应的激活接口枚举
    '/app/bmos-platform/': determinePlatformActived,
    '/app/bmos-mes/': determineMesActived,
    '/app/bmos-lims/': determineLimsActived,
    '/app/bmos-wms/': determineWmsActived,
    '/app/bmos-bsms/': determineBsmsActived,
    '/app/bmos-bims/': determineBimsActived,
    '/app/bmos-lisms/': determineLismsActived,
  };

  // 用户信息
  const userInfo = ref({ userName: '', loginName: '', userId: '', token: '' });
  const appSrc = ref<string>('');

  const appRunner = ref<string>('app-runner-' + currentAppKey.value);

  const setDocTitle = (title: string) => {
    document.title = title;
  };

  const DESK_KEYs = [{ label: t('BMOS'), value: DESK_KEY, name: 'BMOS', id: 'bmos-desk' }];
  const systemSelectList = ref<any[]>([]);
  const systemList = computed(() => [...DESK_KEYs, ...systemSelectList.value]);

  const getIdByCurrentAppKeyAndSetTitle = async (val: string) => {
    try {
      const item = systemList.value.find((item: any) => item.value === val);
      setDocTitle(customizeT(item?.id) || item.label || 'BMOS');
    } catch (error) {
      //
    }
  };

  watch(
    () => currentAppKey.value,
    (val: string) => {
      appRunner.value = 'app-runner-' + val;
      if (val === DESK_KEY) {
        appSrc.value = '';
      } else {
        if (systemList.value) {
          const item = systemList.value.find((item: any) => item.value === val);
          if (item?.isOutside === 1) {
            return;
          }
        }
        appSrc.value = handleAppUrl(currentAppKey.value);
      }
    },
    {
      immediate: true,
    },
  );

  watch(
    () => appSrc.value,
    (val: string) => {
      if (!val) {
        sessionStorage.removeItem('currentAppKey');
        sessionStorage.removeItem('currentFullPath');
        logoMode.value = LogoBackgroundMode.WHITE;
      } else {
        logoMode.value = LogoBackgroundMode.BLUE;
      }
    },
  );

  const getOutsideIpByCode = async (code: string) => {
    try {
      const { data } = await getParameter('platform.sys.outside_url');
      const outsideJson: any = JSON.parse(data?.value || '{}');
      if (code && outsideJson?.[code]) {
        return Promise.resolve(outsideJson?.[code]);
      } else {
        return Promise.reject();
      }
    } catch (error) {
      return Promise.reject();
    }
  };

  const setAppSrcToOutSide = async (item: any, open: boolean) => {
    //open为true时需单独网页打开
    try {
      const outsideIp = await getOutsideIpByCode(item.id);
      const backUrl: string = item.outsideUrl;
      const backToken = getUserToken(); //token
      let temp: any;
      if (backUrl?.includes('?')) {
        temp = outsideIp + backUrl + `&token=${backToken}`;
      } else {
        temp = outsideIp + backUrl + `?token=${backToken}`;
      }
      if (open) {
        window.open(temp);
      } else {
        appSrc.value = temp;
        setDocTitle(customizeT(item.id) || item.name);
      }
    } catch (error) {
      //
    }
  };

  // 菜单下拉框val
  const systemValue = computed({
    get() {
      return currentAppKey.value || DESK_KEY;
    },
    async set(val) {
      const item = systemList.value.find((item: any) => item.value === val);
      if (item?.isOutside === 1) {
        currentAppKey.value = val;
        setAppSrcToOutSide(item, false);
        return;
      }
      if (item?.isOutside === 2) {
        setAppSrcToOutSide(item, true);
        return;
      }

      if (activedApiEnum[val]) {
        await determineActived(val, 2, activedApiEnum[val]);
      } else {
        currentAppKey.value = val;
        getIdByCurrentAppKeyAndSetTitle(val);
      }
    },
  });

  // 签名设置
  const signSetOpen = ref(false);
  const signSet = () => {
    signSetOpen.value = true;
  };

  const LOCK = reactive({
    start: () => {},
    pause: () => {},
  });
  // 门户页菜单
  const menuList = ref<any>([]);

  // 下拉切换系统
  const changeSystem = (_value: SelectValue, option: DefaultOptionType) => {
    const temp = systemList.value.find((item: any) => item.value === _value);
    if (temp.isOutside !== 2) {
      sessionStorage.removeItem('currentFullPath');
      setDocTitle(option.label || 'BMOS');
    }
  };
  // 中间选择菜单
  const chooseMenu = async (val: any) => {
    if (activedApiEnum[val.key]) {
      await determineActived(val, 1, activedApiEnum[val.key]);
    } else {
      if (val.isOutside != 2) {
        currentAppKey.value = val.key;
        setDocTitle(customizeT(val.id) || val.name);
      }
      systemValue.value = val.key;
    }
  };

  const determineActived = async (val: any, type: any, activedApi: Function) => {
    try {
      const res: any = await activedApi({});
      if (type === 1) {
        if (res.data.active == true) {
          determineTime(res.data.date, val);
        } else {
          return message.error(t('未授权'));
        }
      }
      if (type === 2) {
        if (res.data.active == true) {
          determineTime2(res.data.date, val);
        } else {
          return message.error(t('未授权'));
        }
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 判断授权码是否已过期
  const determineTime = (date: any, val: any) => {
    if (date === 'ALL') {
      // 永久授权
      currentAppKey.value = val.key;
      systemValue.value = val.key;
      setDocTitle(customizeT(val.id) || val.name);
      return;
    }
    const currentTime = dayjs(); //当前时间
    const backendTime = dayjs(date); //后端返回时间
    if (currentTime.isAfter(backendTime)) {
      return message.error(t('授权已过期')); // (后端返回时间在当前时间之前)  授权码已经过期
    } else {
      currentAppKey.value = val.key;
      systemValue.value = val.key;
      setDocTitle(customizeT(val.id) || val.name);
    }
  };
  // 判断授权码是否已过期(下拉框菜单点击时)
  const determineTime2 = (date: any, val: any) => {
    if (date === 'ALL') {
      // 永久授权
      currentAppKey.value = val;
      getIdByCurrentAppKeyAndSetTitle(val);
      return;
    }
    const currentTime = dayjs(); //当前时间
    const backendTime = dayjs(date); //后端返回时间
    if (currentTime.isAfter(backendTime)) {
      return message.error(t('授权已过期')); // (后端返回时间在当前时间之前)  授权码已经过期
    } else {
      currentAppKey.value = val;
      getIdByCurrentAppKeyAndSetTitle(val);
    }
  };

  // 密码设置/签名密码设置
  const changePassWord = (val: string) => {
    type.value = val;
    changePasswordRef.value.showModal();
  };
  // 锁屏
  const lockScreen = () => {
    lockScreenRef.value.showModal();
    setLockStatus(userInfo.value?.userId);
    LOCK.pause();
  };

  // 语言设置按钮
  const languageChange = () => {
    changeLanguageRef.value.showModal();
  };
  // 语言弹框
  const changeLang = (val: string) => {
    emit('changeLang', val);
  };
  // 安全退出
  const loggingOut = () => {
    Modal.confirm({
      title: t('是否退出登录'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      okText: t('确定'),
      cancelText: t('取消'),
      async onOk() {
        try {
          const res = await logout();
          if (res.code === 0) {
            message.success(t('退出成功'));
            clearLockStatus(userInfo.value?.userId);
            navigatorLoginPage();
            setUserToken('');
          } else {
            message.error(res.message);
          }
        } catch (error: any) {
          message.error(error.message);
        }
      },
    });
  };
  // 左上角logo返回桌面
  const backDesk = () => {
    systemValue.value = DESK_KEY;
    appSrc.value = '';
    setDocTitle(t(DESK_NAME));
  };
  onBeforeMount(() => {});
  // 获取门户页菜单
  const getMenulist = async () => {
    try {
      const data = { rootMenuCode: '', terminalType: 0, isFirst: true };
      const res: any = await getMenuList(data);
      menuList.value = [...res.data].map((item: any) => {
        return {
          ...item,
          key: MENU_KEY[item.id] ? MENU_KEY[item.id]?.key : item.id,
          EN: item.alias,
        };
      });
      systemSelectList.value = menuList.value.map((item: any) => {
        return {
          ...item,
          id: item.id,
          label: customizeT(item.id) || item.name,
          value: MENU_KEY[item.id]?.key || item.id,
          name: item.EN,
          isOutside: item.isOutside,
          outsideUrl: item.outsideUrl,
        };
      });
      return Promise.resolve();
    } catch (error) {
      return Promise.reject();
    }
  };
  // 获取锁屏时间
  const getLockScreenTime = async () => {
    try {
      const res: any = await getParameter('platform.sys.web-lock-screen-time');
      lockTime.value = Number(res?.data?.value || 0) * 60 * 1000; //多久不操作会锁屏 转成毫秒
      // 如果设置的时长为0,则永不锁屏
      if (lockTime.value == 0) {
        return;
      }
      const lock = LockScreen(() => lockScreen(), lockTime.value);
      Object.assign(LOCK, lock);
      const status = getLockStatus(userInfo.value?.userId);
      // 若锁屏状态为true
      if (status) {
        lockScreenRef.value?.showModal();
      } else {
        LOCK.start();
      }
    } catch (error) {
      // 锁屏参数属于可选配置，缺失时按不自动锁屏处理。
      lockTime.value = 0;
      console.log(error);
    }
  };

  // 获取键盘锁屏快捷键
  const getLockScreenShortcutKey = async () => {
    try {
      const res: any = await getParameter('platform.sys.web-lock-screen-hotkey');
      const value = res?.data?.value;
      const parsed = value ? JSON.parse(value) : null;
      if (Array.isArray(parsed) && parsed.length >= 2) {
        shortcutKey.value = parsed; //["Ctrl","Q"]
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getWebPollingTime = async () => {
    try {
      const { data } = await getParameter('platform.sys.web-msg-polling-time');
      return Promise.resolve(data?.value || 30);
    } catch (error) {
      return Promise.resolve(30);
    }
  };

  // 监听iframe中的键盘事件
  const load = (e: any) => {
    try {
      e.currentTarget.contentWindow.addEventListener('keydown', (e: any) => {
        e.stopPropagation();
        let firstKey = e[shortcutKey?.value[0].toLowerCase() + 'Key']; //第一个键
        let secondKey = shortcutKey?.value[1]?.charCodeAt(0); //第二个键
        if (firstKey && e.key.charCodeAt(0) === secondKey) {
          lockScreen();
        }
      });
    } catch (error) {
      console.log(error, '---');
    }
  };

  const messageCount = ref<number>(0);
  const messageCountList = ref<
    Array<{
      number: number;
      categoryCode: string;
    }>
  >([]);
  const getMessageCount = async () => {
    try {
      const { data } = await reqMessageCount();
      messageCount.value = data?.reduce((pre: any, cur: any) => {
        return pre + cur.number;
      }, 0);
      messageCountList.value = data;
    } catch (error) {
      //
    }
  };

  const { notifyCount, notifyModalOpen, openNotifyModal, readAll, readItem, contextHolder, handleLockChange } =
    useNotify({
      userInfo,
      navigatorLoginPage,
    });
  const { token } = useToken();

  watch(
    () => token.value,
    () => {
      handleLockChange();
    },
  );

  const changeApp = (key: string, path: string) => {
    currentAppKey.value = key;
    systemValue.value = key;
    appRunner.value = 'app-runner-' + key + new Date().getTime();
    sessionStorage.setItem('currentFullPath', path);
    appSrc.value = handleAppUrl(currentAppKey.value);
  };

  const warnningMessageRef = ref<InstanceType<typeof warnningMessage>>();

  const showMessageAlert = () => {
    warnningMessageRef.value?.showDrawer();
  };

  const warningMsgCount = ref(0);

  const toWarningPage = async (record: any) => {
    // 截取record.categoryCode前三位
    const sysCode = record.categoryCode.substring(0, 3);
    if (MENU_KEY[sysCode].key === systemValue.value) {
      sendMessage2Iframe({ menuId: record.categoryCode, menuKey: MENU_KEY[sysCode].key });
    } else {
      const { key, path } = MessageRouterMap.get(record.categoryCode) || {};
      if (!key || !path) {
        return;
      }
      changeApp(key, path);
    }
  };

  const getWarningMsgCount = async () => {
    try {
      const { data } = await reqNoticeWarningUnread();
      warningMsgCount.value = data ?? 0;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 点击消息跳转
  const toMessagePage = (item: any) => {
    const { categoryCode } = item;
    const { key, path } = MessageRouterMap.get(categoryCode) || {};
    if (!key || !path) {
      return;
    }
    if (currentAppKey.value === key) {
      // 同系统走页面内部跳转
      sendMessage2Iframe({ menuId: categoryCode, menuKey: key });
    } else {
      changeApp(key, path);
    }
  };

  // --------------AI聊天框--------------
  const AIChatUrl = ref('');

  const getAIChatUrl = async () => {
    try {
      const res = await getParameter('platform.sys.AI-url');
      // AI 服务属于可选配置，未配置时接口会返回 data: null。
      AIChatUrl.value = res?.data?.value || '';
    } catch (error) {
      console.log(error);
    }
  };

  // 白底 黑底
  const logoMode = ref<LogoBackgroundMode>(LogoBackgroundMode.WHITE);

  onMounted(async () => {
    try {
      await getMenulist();
      getLockScreenTime();
      getLockScreenShortcutKey();
      userInfo.value = getUserInfo();
      const timer = setInterval(() => {
        const user = getUserInfo();
        if (user) {
          userInfo.value = user;
          clearInterval(timer);
        }
      }, 100);

      // 获取AI聊天框
      await getAIChatUrl();

      // 获取轮询时间
      const pollingTime = await getWebPollingTime();

      // getWarningMsgCount();

      getMessageCount();
      setInterval(
        () => {
          getMessageCount();
        },
        (pollingTime || 30) * 1000,
      );

      window.document.onkeydown = e => {
        e.stopPropagation();
        let firstKey = (e as any)[shortcutKey.value[0].toLowerCase() + 'Key']; //第一个键
        let secondKey = shortcutKey.value[1].charCodeAt(0); //第二个键
        if (firstKey && e.key.charCodeAt(0) === secondKey) {
          lockScreen();
        }
      };

      // 获取当前页面缓存
      const cacheAppKey = sessionStorage.getItem('currentAppKey');
      if (cacheAppKey) {
        systemValue.value = cacheAppKey;
      }

      // 监听此消息返回到桌面
      window.addEventListener('message', (msg: MessageEvent) => {
        if (msg.origin === 'http://172.30.1.30:8080') {
          systemValue.value = DESK_KEY;
          setDocTitle(t(DESK_NAME));
        }
        if (msg.data?.type === 'routerChange' && msg.data?.data?.fullPath) {
          // 缓存当前页面路由
          // 存 sessionStorage
          sessionStorage.setItem('currentFullPath', msg.data.data.fullPath);
        }
        if (msg.data?.type === 'logoColor' && !isNullOrUnDef(msg.data?.data?.color)) {
          logoMode.value = msg.data?.data?.color;
        }
        if (msg.data?.type === 'updateMessageCount') {
          getMessageCount();
        }
      });
      // 监听页面刷新
      window.addEventListener('beforeunload', function (_e: any) {
        // 缓存当前页面
        if (currentAppKey.value) {
          sessionStorage.setItem('currentAppKey', currentAppKey.value);
        } else {
          sessionStorage.removeItem('currentAppKey');
        }
      });
    } catch (error) {
      //
    }
  });

  const sendMessage2Iframe = (params: { menuId: string; menuKey: string }) => {
    let iframeWindow = (document.getElementById(appRunner.value) as HTMLIFrameElement)?.contentWindow;
    if (iframeWindow) {
      iframeWindow.postMessage(params, '*');
    }
  };

  onUnmounted(() => {
    LOCK.pause();
  });
</script>

<style scoped lang="less">
  .webHome {
    width: 100%;
    height: 100%;
    min-width: 820px;
    min-height: 600px;
    background-image: url('../assets/img/webBg.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    position: relative;
    .desk_header {
      width: 100%;
      // height: 5.8%;
      height: 50px;
      padding-right: 2px;
      background-color: #fff;
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-sizing: border-box;
      .logo-class {
        cursor: pointer;
      }
      .header_left {
        width: 11.6%;
        height: 100%;
        position: relative;
      }
      .header_all_right {
        width: calc(100% - 200px);
        height: 100%;
        border-bottom: 1px solid #eee;
        display: flex;
        justify-content: flex-end;
      }
      .header_right {
        height: 100%;
        display: flex;
        align-items: center;
        // 下拉框
        .selectMenu {
          border-radius: 4px;
        }
        .selectMenu:hover {
          background: #f0f1f2;
        }

        :deep(.plat-select-selector) {
          border-radius: 4px;
          border: 1px solid #fff;
        }

        :deep(.plat-select-selector:hover) {
          // border-radius: 20px;
          background: #f0f1f2;
          border: 1px solid #fff;
        }

        :deep(.plat-divider-vertical) {
          height: 1.5em;
          margin: 0 24px;
        }
        :deep(.plat-badge .plat-badge-dot.plat-scroll-number) {
          transform: translate(-1px, -1px);
        }
        :deep(.message-badge) .plat-badge-multiple-words {
          padding: 0 4px;
        }
        .message {
          line-height: 0;
          margin-right: 20px;
          padding: 3px;
          border-radius: 4px;
        }
        // 移入铃铛
        .message:hover {
          background: #f0f1f2;
        }
        //  用户信息
        .user {
          position: relative;

          .info {
            padding: 4px 8px 4px 4px;
            box-sizing: border-box;
            border-radius: 4px;
            display: flex;
            align-items: center;
            cursor: pointer;
            .userName {
              width: 48px;
              display: inline-block;
              font-size: 14px;
              margin-left: 8px;
              margin-top: 1px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }
          .info:hover {
            background: #f0f1f2;
          }
        }
      }
    }
    .main {
      width: 100%;
      height: calc(100% - 50px);
      overflow: auto;
      position: relative;
      padding-left: 3px;
      .menu {
        width: 95%;
        height: 100%;
        margin: 0 auto;
        box-sizing: border-box;
        display: flex;
        flex-wrap: wrap;
        align-content: flex-start;
        .itemMenu {
          width: 16.5%;
          min-width: 245px;
          min-height: 180px;
          margin-left: 4.2%;
          margin-right: 3.9%;
          padding-left: 22px;
          padding-bottom: 22px;
          margin-top: 125px;
          border-radius: 19px;
          cursor: pointer;
          background: linear-gradient(0deg, #e6f0ff, #e6f0ff),
            linear-gradient(246.58deg, rgba(255, 255, 255, 0.79) -13.68%, rgba(255, 255, 255, 0.03) 106.25%);
          position: relative;
          .imageIcon {
            width: 90px;
            height: 90px;
            position: absolute;
            top: -37px;
            left: 12px;
          }
          .title {
            font-size: 24px;
            color: #3c3d40;
            margin-top: 68px;
            font-weight: 500;
          }
          .name {
            font-size: 20px;
            color: #909398;
            margin-top: 10px;
          }
        }
        .itemMenu:hover {
          box-shadow: 0px 4px 16px 2px #00000026;
        }
        .itemMenu:hover .title {
          color: #2871ff;
        }
        .itemMenu:hover .name {
          color: #2871ff;
        }
      }
    }
  }
  // hover时的下拉菜单
  .hoverMenu {
    width: 240px;
    height: 320px;
    background-color: #fff;
    border-radius: 4px;
    position: absolute;
    top: 2px;
    right: 0px;
    .userInfo {
      display: flex;
      height: 60px;
      padding: 12px 0px 8px 12px;
      background-image: url('../assets/img/bgInfo.png');
      .headSculpture {
        margin-right: 8px;
      }
      .loginName {
        font-size: 14px;
      }
      .accountName {
        font-size: 12px;
        color: #909398;
      }
    }
    .action {
      padding: 0px 12px 12px 12px;
      .line {
        margin-bottom: 8px;
      }
      .actionItem {
        width: 216px;
        height: 36px;
        margin-bottom: 3px;
        padding-left: 8px;
        cursor: pointer;
        display: flex;
        align-items: center;
        border-radius: 4px;
        .actionTitle {
          font-size: 14px;
          color: #606266;
          margin-left: 8px;
        }
      }
      .actionItem:hover {
        background: #f4f4f4;
      }
    }
  }
  .main-app {
    position: absolute;
    width: 100%;
    height: 100%;
    overflow: hidden;
    background: #fff;
    z-index: 2;
    top: 0;
    left: 0;
  }
  .main-app-iframe {
    width: 100%;
    height: 100%;
    background: #fff;
  }
  .main-app__iframe {
    border: none;
    background: #fff;
  }

  :deep(.message-item) {
    display: flex;
    justify-content: space-between;
  }
  :deep(.message-empty) {
    display: flex;
    flex-direction: column;
    align-items: center;
    .title {
      color: var(--bmos-fourth-level-text-color);
    }
    .plat-empty-image svg {
      width: 80px;
    }
  }
</style>

<style lang="less">
  .plat-popover.chat-popover .plat-popover-content .plat-popover-inner {
    width: 720px;
  }
</style>

import { getItem } from '@/utils/storage';
import { Recordable } from '@bmos/components';
import { t } from '@bmos/i18n';
import { isArray, isNullOrUnDef } from '@bmos/utils';
import { useWebSocket } from '@vueuse/core';
import { notification } from 'ant-design-vue';
import { CLIENT_ID, NotifyMessageType, NotifyMessageTypeMap } from '../types';

const initNotifyCount = {
  ALL: 0,
  AUDIT: 0,
  WARNING: 0,
  ALARM: 0,
};

export const useNotify = ({ userInfo, navigatorLoginPage }: { userInfo: Ref<any>; navigatorLoginPage: Function }) => {
  const [api, contextHolder] = notification.useNotification();

  const notifyCount = ref<Recordable>({
    ...initNotifyCount,
  });
  // 站内消息通知
  const notifyModalOpen = ref<boolean>(false);
  const openNotifyModal = () => {
    notifyModalOpen.value = true;
  };

  const url = ref<string>(
    `ws://${location.hostname}:60100/api/app/platform/ws/message?bmos-access-token=${getItem(
      'BMOS-ACCESS-TOKEN',
    )}&clientId=${CLIENT_ID.WEB + Math.random()}`,
  );
  // @ts-ignore
  const { data, close, open } = useWebSocket(url, {
    autoReconnect: {
      retries: 3,
      delay: 1000,
    },
  });
  // 监听 data 和 userInfo
  watch([data, userInfo], async () => {
    try {
      await nextTick();
      if (data.value?.code === 401) {
        // 未登录
        navigatorLoginPage();
      }
      const messageData = JSON.parse(data.value);
      if (messageData && messageData.code === 0 && userInfo.value.userId) {
        notifyCount.value = { ...initNotifyCount };
        const { data: message } = messageData;
        const messageArr = message[userInfo.value.userId];
        if (messageArr) {
          messageArr.forEach((item: any) => {
            notifyCount.value[NotifyMessageType.ALL] += item.count;
            if (NotifyMessageTypeMap.get(NotifyMessageType.AUDIT)?.includes(item.messageType)) {
              notifyCount.value[NotifyMessageType.AUDIT] += item.count;
            }
            if (NotifyMessageTypeMap.get(NotifyMessageType.WARNING)?.includes(item.messageType)) {
              notifyCount.value[NotifyMessageType.WARNING] += item.count;
            }
            if (NotifyMessageTypeMap.get(NotifyMessageType.ALARM)?.includes(item.messageType)) {
              notifyCount.value[NotifyMessageType.ALARM] += item.count;
            }
          });
        }
        if (isArray(message)) {
          message.forEach((item: any) => {
            notifyCount.value[NotifyMessageType.ALL] += item.count;
            if (NotifyMessageTypeMap.get(NotifyMessageType.AUDIT)?.includes(item.messageType)) {
              notifyCount.value[NotifyMessageType.AUDIT] += item.count;
            }
            if (NotifyMessageTypeMap.get(NotifyMessageType.WARNING)?.includes(item.messageType)) {
              notifyCount.value[NotifyMessageType.WARNING] += item.count;
            }
            if (NotifyMessageTypeMap.get(NotifyMessageType.ALARM)?.includes(item.messageType)) {
              notifyCount.value[NotifyMessageType.ALARM] += item.count;
            }
          });
        }
        if (!isNullOrUnDef(message.time)) {
          api.info({
            message: t('消息提醒'),
            description: `${message.title}`,
            placement: 'bottomRight',
            onClick: () => {
              notifyModalOpen.value = true;
            },
          });
        }
      }
    } catch (error) {
      console.error(error);
    }
  });

  const handleLockChange = () => {
    close();
    url.value = `ws://${location.hostname}:60100/api/app/platform/ws/message?bmos-access-token=${getItem(
      'BMOS-ACCESS-TOKEN',
    )}&clientId=${CLIENT_ID.WEB + Math.random()}`;
    open();
  };

  const readAll = () => {
    // notifyCount.value = { ...initNotifyCount };
  };
  const readItem = (_item: any) => {
    // notifyCount.value[NotifyMessageType.ALL] =
    //   notifyCount.value[NotifyMessageType.ALL] - item.count > 0
    //     ? notifyCount.value[NotifyMessageType.ALL] - item.count
    //     : 0;
    // const types = [NotifyMessageType.AUDIT, NotifyMessageType.WARNING, NotifyMessageType.ALARM];
    // types.forEach(type => {
    //   if (NotifyMessageTypeMap.get(type)?.includes(item.msgType.value)) {
    //     notifyCount.value[type] = notifyCount.value[type] - 1 > 0 ? notifyCount.value[type] - 1 : 0;
    //   }
    // });
  };

  return {
    notifyCount,
    notifyModalOpen,
    openNotifyModal,
    readAll,
    readItem,
    contextHolder,
    handleLockChange,
  };
};

import request from '../../src/utils/request';

// /api/app/platform/user/status
export const userStatus = (params?: any) => {
  return request({
    url: '/api/app/platform/user/status',
    method: 'get',
    params,
    headers: {
      terminalType: 0,
    },
  });
};

// 获取i18n
export const getI18nConfig = () => {
  return request({
    url: '/api/app/platform/i18n/config',
    method: 'GET',
    headers: {
      'request-resource': 'frontend-web',
    },
  });
};

// 代办任务数量
export const reqMessageCount = () => {
  return request({
    url: '/api/app/platform/message/wait/task/count',
    method: 'get',
  });
};

// ------------------预警信息相关--------------------

/**
 * @description: 预警消息通知分页 /api/app/platform/message/page
 */
export const reqPlasmaNoticePage = (data?: any) => {
  return request({
    url: '/api/app/platform/message/page',
    method: 'post',
    data,
  });
};

/**
 * @description: 预警未读通知数量 /api/app/platform/message/unread/warning/count
 */
export const reqNoticeWarningUnread = () => {
  return request({
    url: '/api/app/platform/message/unread/warning/count',
    method: 'get',
  });
};

/**
 * @description: 编辑-消息标记已读 /api/app/platform/message/read
 */
export const reqPlasmaNoticeRead = (params: any) => {
  return request({
    url: `/api/app/platform/message/read`,
    method: 'post',
    params,
  });
};

/**
 * @description: 编辑-消息全部标记已读 /api/app/platform/notifyMessage/read
 */
export const reqPlasmaNoticeAllRead = (data: any) => {
  return request({
    url: `/api/app/platform/notifyMessage/read`,
    method: 'post',
    data,
  });
};
export const getNotifyMessageList = (data: any) => {
  return request({
    url: '/api/app/platform/notifyMessage/page',
    method: 'POST',
    data,
  });
};

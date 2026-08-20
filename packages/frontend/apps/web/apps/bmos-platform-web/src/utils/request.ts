// import axios from 'axios';
import { handleLogReqHeaders } from '@/services/utils/logHeaders';
import { handleLoginOutReqHeaders } from '@/services/utils/loginOutHeaders';
import { USER_LOCK_REQ_ORDER, createBmosAxios, processFault, userToken } from '@bmos/axios';
import { app, lockScreen, sso } from '@bmos/messager';
import { isFunction } from '@bmos/utils';
import { InternalAxiosRequestConfig } from 'axios';
// import useToken from '../stores/token'

const serive = createBmosAxios({
  baseURL: '',
})
  .feat.use(userToken({ ssoMessenger: sso }))
  .use(processFault({ appMessenger: app }))
  .use([
    {
      reqOrder: USER_LOCK_REQ_ORDER,
      reqInterceptor(req: InternalAxiosRequestConfig) {
        const { lockMessage } = lockScreen;
        if (isFunction(lockMessage) && req.url !== '/api/app/platform/message/wait/task/count') {
          lockMessage();
        }
        return req;
      },
    },
  ])
  .use([
    {
      reqOrder: 5,
      reqInterceptor: handleLogReqHeaders,
    },
  ])
  .use([
    {
      reqOrder: 6,
      reqInterceptor: handleLoginOutReqHeaders,
    },
  ])
  .end();

// serive.defaults.headers['Content-Type'] = 'application/json';

//   return res.data;
// },
// err => {
// err.message && message.error(err.message)
//     return Promise.reject(err);
//   },
// );

//     return res.data
//   },
//   err => {
//     // err.message && message.error(err.message)
//     return Promise.reject(err);
//   },
// );

// serive.interceptors.request.use(
//   (config: any) => {
//     const token =
//       getItem('ISC_SSO_TOKEN') || '42aa78b2-c2ef-41d3-9b96-a32ab8e866f9';
//     config.headers = {
//       'bmos-access-token': token,
//       token: token,
//     };
//     return config;
//   },
//   error => Promise.reject(error),
// );

export default serive; // 导出工具

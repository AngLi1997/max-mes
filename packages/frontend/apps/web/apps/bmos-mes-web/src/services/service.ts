// import axios,{ createBmosAxios, processFault, userToken } from '@bmos/axios';
// import { app, sso } from '@bmos/messager';
import { BMOSAxiosInstance, createBmosAxios, lockPost, processFault, userToken } from '@bmos/axios';
import { app, lockScreen, sso } from '@bmos/messager';
import { handleLogReqHeaders } from './utils/logHeaders';
// import axios from '@bmos/axios';
// const request = createBmosAxios({
//   baseURL: '/api',
// });

const request: BMOSAxiosInstance = createBmosAxios({
  baseURL: '/api',
})
  .feat.use(userToken({ ssoMessenger: sso }))
  .use(processFault({ appMessenger: app }))
  .use(lockPost({ messenger: lockScreen }))
  .use([
    {
      reqOrder: 5,
      reqInterceptor: handleLogReqHeaders,
    },
  ])
  .end();

export default request;

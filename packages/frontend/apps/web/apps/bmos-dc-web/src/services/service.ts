import { BMOSAxiosInstance, createBmosAxios, lockPost, processFault, userToken } from '@bmos/axios';
import { app, lockScreen, sso } from '@bmos/messager';
import { handleLogReqHeaders } from './utils/logHeaders';

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

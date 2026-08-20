import { BMOSAxiosInstance, createBmosAxios, lockPost, processFault, userToken } from '@bmos/axios';
import { app, lockScreen, sso } from '@bmos/messager';

// @ts-ignore
const request: BMOSAxiosInstance = createBmosAxios({
  baseURL: '',
})
  .feat.use(userToken({ ssoMessenger: sso }))
  .use(processFault({ appMessenger: app }))
  .use(lockPost({ messenger: lockScreen }))
  .end();

export default request; // 导出工具

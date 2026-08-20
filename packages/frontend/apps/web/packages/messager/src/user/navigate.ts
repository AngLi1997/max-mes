import { urlSetParams } from '@bmos/utils';
import { SSO_URL, appConfig, redirectQueryName, requestHeaderName, ssoConfig } from '../const';
import { loginNavi } from '../messager';
const { loginMessage, MESSAGE } = loginNavi;
export const navigatorLoginPage = (ssoUrl: string = SSO_URL) => {
  // const envs = utils.getGlobal("ENVS");
  // if (bridged) {
  //   openLogin().then(() => {
  //     location.reload();
  //   });
  // } else if (framed) {
  //   send("navigator:loginPage");
  // } else if (location) {

  // }
  loginMessage();
};

const SSO_BASE_URL = '/app/bmos-platform/bmos/index.html';

const getReturnUrl = () => {
  const { origin } = location;
  return origin + SSO_BASE_URL;
};

export const navigatoFrom = (token: string) => {
  const params = new URLSearchParams(location.search);
  let base = params.get(redirectQueryName);

  let _a: any;
  base = urlSetParams(
    base === null ? _a?.call(appConfig) || getReturnUrl() : base,
    token
      ? {
          [requestHeaderName]: token,
        }
      : {},
  );
  location.replace(base);
};

export const loginOnMessage = (on: boolean = false) => {
  if (on) {
    window.top?.addEventListener('message', e => {
      if (e.data === MESSAGE) {
        let _a: any;
        const base = SSO_URL;
        if (base) {
          const replaceUrl = urlSetParams(base, {
            [redirectQueryName]:
              ((_a = appConfig.ssoReturnUrl) === null ? void 0 : _a?.call(appConfig)) || ssoConfig.returnUrl(),
          });
          location.replace(replaceUrl);
        }
      }
    });
  }
};

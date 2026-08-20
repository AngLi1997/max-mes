import { I18nLanguageEnum } from '@bmos/i18n';

export const handleAppUrl = (app: any): string => {
  if (!app) return app;
  let curApp = location.origin + app;
  const cacheFullPath = sessionStorage.getItem('currentFullPath');
  if (cacheFullPath) {
    curApp = curApp + cacheFullPath;
  }
  if (curApp.includes('?')) {
    return curApp + `&lang=${localStorage.getItem('LANG') || I18nLanguageEnum.ZH_CN}&random=${new Date().getTime()}`;
  }
  return curApp + `?lang=${localStorage.getItem('LANG') || I18nLanguageEnum.ZH_CN}&random=${new Date().getTime()}`;
};
// 获取当前语言方法
export const getCurrentLanguage = () => {
  return localStorage.getItem('LANG') || I18nLanguageEnum.ZH_CN;
};

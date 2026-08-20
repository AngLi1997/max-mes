import { changeLanguage, I18nLanguageType } from '@bmos/i18n';
import { Auth, loginRegist } from '@bmos/messager';
(async () => {
  try {
    loginRegist(true);
    await Auth({});
    changeLanguage((localStorage.getItem('LANG') || 'zh_CN') as I18nLanguageType);
  } catch (error) {
    console.log(error);
  }
  await import('./render');
})();

import { changeLanguage, I18nLanguageEnum, init } from '@bmos/i18n';
import { getResources } from '@bmos/utils';

// 获取 url 中的 query 参数 lang
export const getLang = (): I18nLanguageEnum => {
  const url = location.href;
  const urlObj = new URL(url);
  return (urlObj.searchParams.get('lang') as I18nLanguageEnum) || I18nLanguageEnum.ZH_CN;
};

export const handleLang = async () => {
  const lang = getLang();
  changeLanguage(lang as I18nLanguageEnum);
  const data = await getResources(lang);
  if (data) {
    init({
      lng: lang,
      resources: {
        [lang]: {
          translation: data,
        },
      },
    });
  } else {
    console.error('Failed to load resources');
    init({
      lng: lang,
    });
  }
};

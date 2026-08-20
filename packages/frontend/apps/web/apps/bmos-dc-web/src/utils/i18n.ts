import { getI18nConfig } from '@/services';
import { I18nLanguageEnum, changeLanguage, init } from '@bmos/i18n';
import { getResources, storeResources } from '@bmos/utils';

// 获取 url 中的 query 参数 lang
export const getLang = () => {
  const url = location.href;
  const urlObj = new URL(url);
  const lang = localStorage.getItem('BAIE_LANG');
  return lang || urlObj.searchParams.get('lang') || I18nLanguageEnum.ZH_CN;
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

export const updateLangResource = async (lng: string) => {
  try {
    const { data } = await getI18nConfig();
    await storeResources(lng, data);
    init({
      resources: {
        [lng]: data,
      },
    });
    return Promise.resolve();
  } catch (error) {
    console.error(error);
    return Promise.reject(error);
  }
};

export const setLangResource = async (lng: string) => {
  try {
    let messages = await getResources(lng);
    if (!messages) {
      const { data } = await getI18nConfig();
      messages = data;
      // 缓存到 IndexedDB
      await storeResources(lng, data);
    }
    if (messages) {
      init({
        resources: {
          [lng]: {
            translation: messages,
          },
        },
      });
      return Promise.resolve();
    }
  } catch (error) {
    console.error(error);
    return Promise.reject(error);
  }
};

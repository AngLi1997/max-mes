import { init } from '@bmos/i18n';
import { getResources, storeResources } from '@bmos/utils';
import { getI18nConfig } from '../api/info';

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

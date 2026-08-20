import { getI18nConfigApi } from '@/api';
import { LANGUAGE_MESSAGE } from '@/utils/uniStorage/const.js';
import { getStorageSync, setStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';

const languageType = {
  zh_CN: 'zh-Hans',
  en_US: 'en',
  ru_RU: 'ru',
};

const typeLanguage = {
  'zh-Hans': 'zh_CN',
  'en': 'en_US',
  'ru': 'ru_RU',
};

function useLocale() {
  const systemInfo = ref(null);
  const systemLocale = ref('zh-Hans');
  const applicationLocale = ref('zh-Hans');

  systemInfo.value = uni.getSystemInfoSync();
  systemLocale.value = systemInfo.value.language;
  applicationLocale.value = uni.getLocale();
  const { t, locale, mergeLocaleMessage } = useI18n();
  uni.onLocaleChange((e) => {
    applicationLocale.value = e.locale;
  });

  // 异步获取语言
  async function loadLanguageAsync(refresh = false, lang = uni.getLocale()) {
    try {
      let messages = getStorageSync(LANGUAGE_MESSAGE);
      if (!messages) {
        messages = {};
      }
      if (!messages[lang] || refresh) {
        const res = await getI18nConfigApi(typeLanguage[lang]);
        if (messages && res.data) {
          messages[lang] = res.data;
        }
        else {
          messages = {
            [lang]: res.data,
          };
        }
        setStorageSync(LANGUAGE_MESSAGE, messages);
      }
      return Promise.resolve(messages);
    }
    catch (error) {
      return Promise.reject(error);
    }
  }
  // 设置语言信息
  function setMessages(lang, messages) {
    mergeLocaleMessage(lang, messages[lang]);
  }

  const onLocaleChange = async (e) => {
    const messages = await loadLanguageAsync(true, e.code);
    // #ifdef APP-PLUS
    uni.showModal({
      content: t('应用此设置将重启App'),
      success: (res) => {
        if (res.confirm) {
          setMessages(e.code, messages);
          uni.setLocale(e.code);
        }
      },
    });
    // #endif
    // #ifdef H5
    locale.value = e.code;
    uni.setLocale(e.code);
    setMessages(e.code, messages);
    uni.reLaunch({
      url: '/pages/home/index',
    });
    // #endif
  };
  return {
    onLocaleChange,
    loadLanguageAsync,
    setMessages,
    applicationLocale,
    systemLocale,
    systemInfo,
    t,
  };
}

export { languageType, typeLanguage, useLocale };

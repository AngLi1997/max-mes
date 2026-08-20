<script setup lang="ts">
  import WebHome from './webHome/index.vue';
  import { BMConfigProvider } from '@bmos/components';
  import { watch } from 'vue';
  import { init } from '@bmos/i18n';
  import { reactive, nextTick } from 'vue';
  import { setLangResource, updateLangResource } from './utils';

  const LANG = reactive({
    language: localStorage.getItem('LANG') || 'zh_CN',
    refresh: false,
  });
  // 门户页右上角语言设置
  const changeLanguage = async (val: string) => {
    LANG.language = val;
    // 刷新门户页
    LANG.refresh = false;
    localStorage.setItem('LANG', val);
    await updateLangResource(val);
    await nextTick();
    LANG.refresh = true;
  };

  setLangResource(localStorage.getItem('LANG') || 'zh_CN').finally(() => {
    LANG.refresh = true;
  });

  watch(
    () => LANG.language,
    () => {
      init({
        lng: LANG.language,
      });
    },
    { immediate: true },
  );

  onMounted(() => {
    updateLangResource(LANG.language);
  });
</script>

<template>
  <BMConfigProvider prefixCls="plat" :lang="LANG.language">
    <WebHome v-if="LANG.refresh" @changeLang="changeLanguage"></WebHome>
  </BMConfigProvider>
</template>

<style scoped>
  header {
    max-height: 100vh;
    line-height: 1.5;
  }
</style>

<template>
  <div class="board-container">
    <iframe
      id="board"
      width="100%"
      height="100%"
      allowfullscreen
      class="board__iframe"
      style="display: block"
      :src="boardSrc"></iframe>
  </div>
</template>

<script setup lang="tsx">
  import { ref, onMounted } from 'vue';
  import { I18nLanguageEnum } from '@bmos/i18n';
  import { sso } from '@bmos/messager';
  const { getUserToken } = sso;
  const BoardSrc =
    'http://172.30.1.30:8080/webroot/decision/view/duchamp?viewlet=BMOS%25E5%25A4%25A7%25E5%25B1%258F%25E6%2595%25B0%25E6%258D%25AE%25E7%259C%258B%25E6%259D%25BF.fvs&ref_t=design&ref_c=57448897-e72d-4613-b398-38c40d0aeaea&page=0';
  const boardSrc = ref('');
  // 拼接url
  const montage = () => {
    const lang = localStorage.getItem('LANG') || I18nLanguageEnum.ZH_CN; //lang 语言
    const backUrl = window.location.origin; //ip
    const backToken = getUserToken(); //token
    boardSrc.value = BoardSrc + `&lang=${lang}` + '&is_menu=1' + `&backtoken=${backToken}` + `&backurl=${backUrl}`;
  };
  onMounted(() => {
    montage();
  });
</script>
<style scoped lang="less">
  .board-container {
    width: 100%;
    height: 100%;
  }
</style>

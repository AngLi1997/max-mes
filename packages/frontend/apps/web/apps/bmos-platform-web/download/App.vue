<template>
  <BMConfigProvider prefixCls="plat">
    <div class="download-container">
      <div class="download-content">
        <div class="left-png"><img src="./assets/download-leftsvg.svg" style="width: 100%" /></div>
        <div class="right-box">
          <div class="logo">
            <img :src="getLogoUrl('login-logo.svg')" style="width: 100%" />
          </div>
          <div class="title">
            <img src="./assets/download-title.svg" style="width: 100%" />
          </div>
          <div class="sub-title">
            <img src="./assets/download-subtitle.svg" style="width: 100%" />
          </div>
          <div class="button-box">
            <Button type="primary" block style="height: 60px; margin-bottom: 60px" @click="handleDown('exe')">
              {{t('下载 Windows 版')}}
            </Button>
            <Button type="primary" block style="height: 60px" @click="handleDown('apk')">{{t('下载 Android 版')}}</Button>
          </div>
        </div>
      </div>
    </div>
  </BMConfigProvider>
</template>
<script setup lang="ts">
  import { BMConfigProvider } from '@bmos/components';
  import { Button } from 'ant-design-vue';
  import { getLogoUrl, getIp } from '@bmos/utils';
  import { t } from '@bmos/i18n';
  // 下载
  const handleDown = (type: string) => {
    const link = document.createElement('a');
    link.href = getIp() + 'front-end/download/packages/Bmos-Mes.' + type;
    link.download = 'Bmos-Mes.' + type;
    link.click();
  };
</script>

<style scoped lang="less">
  .download-container {
    width: 100%;
    height: 100vh;
    overflow: auto;
    // -webkit-overflow-scrolling: touch;
    background-image: url('./assets/download-bg.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    .download-content {
      position: relative;
    }
    .left-png {
      width: 459px;
      height: 385px;
      margin-right: 143px;
    }
    .logo {
      width: 235px;
      height: 33px;
      margin-bottom: 75px;
    }
    .title {
      width: 458px;
      height: 50px;
      margin-bottom: 34px;
    }
    .sub-title {
      width: 292px;
      height: 38px;
      margin-bottom: 120px;
    }
    .button-box {
      width: 400px;
    }
  }
  @media screen and (min-width: 1280px) {
    .download-content {
      min-height: 100%;
      display: flex;
      box-sizing: border-box;
      justify-content: center;
      padding: 0 95px 0 125px;
      align-items: center;
    }
  }
  @media screen and (max-width: 1280px) {
    .download-content {
      display: flex;
      min-height: 100%;
      padding: 30px 0 120px;
      box-sizing: border-box;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      .left-png {
        margin-right: 0;
      }
      .right-box {
        display: flex;
        flex-direction: column;
        align-items: center;
        .logo {
          position: absolute;
          top: 80px;
          right: 145px;
        }
      }
    }
  }
</style>

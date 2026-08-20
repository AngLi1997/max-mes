<template>
  <div class="propmp-box">
    <div class="propmp-item">
      <div class="propmp-item-color treat-color"></div>
      <div>{{ t('待分解') }}</div>
    </div>
    <div class="propmp-item">
      <div class="propmp-item-color decomposed-color"></div>
      <div>{{ t('已分解') }}</div>
    </div>
    <div class="propmp-item">
      <div class="propmp-item-color confirmed-color"></div>
      <div>{{ t('已确认') }}</div>
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
</script>

<style scoped lang="less">
  .propmp-box {
    width: 258px;
    height: 44px;
    box-shadow: 0px 0px 10px 0px #00000040;
    background: #ffffff;
    border-radius: 5px;
    padding: 12px, 16px, 12px, 16px;
    position: absolute;
    top: 145px;
    left: 20px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    .propmp-item {
      width: 33%;
      text-align: center;
      display: flex;
      align-items: center;
      justify-content: center;
      .propmp-item-color {
        width: 16px;
        height: 16px;
        border-radius: 3px;
        border: 1px solid;
        margin-right: 5px;
      }
      .treat-color {
        border-color: #ff9a2f;
        background-color: #ffecd8;
      }
      .decomposed-color {
        border-color: #59bf78;
        background-color: #dbf9ef;
      }
      .confirmed-color {
        border-color: #d4d7d9;
        background-color: #ffffff;
      }
    }
  }
</style>

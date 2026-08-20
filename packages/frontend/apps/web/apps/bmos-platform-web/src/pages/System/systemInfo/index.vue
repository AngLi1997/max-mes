<template>
  <div class="container">
    <div class="version">
      <img src="../../../assets/img/shading.png" alt="" />
      {{ t('系统版本号') }}:
      <span>{{ systemVersion }}</span>
    </div>
    <div style="padding-left: 12px">
      <Divider orientation="left" orientation-margin="0px">
        <span class="title">{{ t('系统授权') }}</span>
      </Divider>
      <div>
        <Button type="primary" @click="activeBtn">{{ t('激活') }}</Button>
      </div>
      <div class="content">
        <div v-for="(item, i) in systemList.filter((item: any) => item)" :key="i" class="contentItem">
          <div class="systemName">{{ item?.name }}</div>
          <div>
            <Tag :color="echoTagColor(item?.date)">
              {{ echoStatus(item?.date) }}
            </Tag>
          </div>
          <div v-if="item?.date && item?.date !== 'ALL'" class="expire">
            {{ item?.date }}
            <span>{{ t('到期') }}</span>
          </div>
          <!-- 背景 -->
          <div class="background">
            <img :src="echoImg(item?.name)" alt="" class="imageIcon" />
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 系统激活弹框 -->
  <activationMadal
    ref="activationMadalRef"
    :activateVerification="activateVerification"
    @updatePlatform="updatePlatform"
    @updateMes="updateMes"
    @updateLims="updateLims"
    @updateWms="updateWms"
    @updateBsms="updateBsms"
    @updateBims="updateBims"
    @updateLisms="updateLisms"></activationMadal>
</template>

<script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import { Button, Divider, Tag, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import {
    determinePlatformActived,
    determineMesActived,
    determineLimsActived,
    determineWmsActived,
    determineBsmsActived,
    determineBimsActived,
    determineLismsActived,
  } from '../../../../login/api';
  import activationMadal from './activationMadal/activationMadal.vue';
  import platformImg from '../../../assets/img/activationPlatform.png';
  import mesImg from '../../../assets/img/activationMes.png';
  import limsImg from '../../../assets/img/activationLims.png';
  import wmsImg from '../../../assets/img/activationWms.png';
  import bsmsImg from '../../../assets/img/activationBsms.png';
  import bimsImg from '../../../assets/img/activationBims.png';
  import lismsImg from '../../../assets/img/activationLisms.png';
  import { reqBusinessParameterDetailGET } from '@/api';

  const systemVersion = ref<string>('V2.0.3.2');
  const activationMadalRef = ref();
  const systemList = ref<any>([]);
  const activateVerification = ref<any>();
  // 页面激活按钮
  const activeBtn = () => {
    activationMadalRef.value.showActivationMadal();
  };
  // 点了激活按钮之后更新平台牌牌
  const updatePlatform = async () => {
    await determinePlatformActived1();
  };
  // 点了激活按钮之后更新mes牌牌
  const updateMes = async () => {
    await determineMesActived1();
  };
  // 点了激活按钮之后更新lims牌牌
  const updateLims = async () => {
    await determineLimsActived1();
  };
  // 点了激活按钮之后更新wms牌牌
  const updateWms = async () => {
    await determineWmsActived1();
  };
  // 点了激活按钮之后更新bsms牌牌
  const updateBsms = async () => {
    await determineBsmsActived1();
  };
  // 点了激活按钮之后更新bims牌牌
  const updateBims = async () => {
    await determineBimsActived1();
  };
  // 点了激活按钮之后更新lisms牌牌
  const updateLisms = async () => {
    await determineLismsActived1();
  };

  // 回显标签状态
  const echoStatus = (date: any) => {
    if (date === 'ALL') {
      return t('永久授权');
    }
    if (date && date !== 'ALL') {
      return t('临时授权');
    } else {
      return t('未授权');
    }
  };
  // 回显标签颜色
  const echoTagColor = (date: any) => {
    if (date === 'ALL') {
      return '#59BF78';
    }
    if (date && date !== 'ALL') {
      return '#2871FF';
    } else {
      return '#FF5633';
    }
  };
  // 图片
  const echoImg = (name: any) => {
    switch (name) {
      case t('制药管理平台'):
        return platformImg;
      case t('制造执行系统'):
        return mesImg;
      case t('实验室信息管理系统'):
        return limsImg;
      case t('仓储管理系统'):
        return wmsImg;
      case t('170'):
        return bsmsImg;
      case t('180'):
        return bimsImg;
      case t('210'):
        return lismsImg;
      default:
        return platformImg; // 默认
    }
  };
  // 查系统版本号
  const getSystemVersion1 = async () => {
    try {
      const { data } = await reqBusinessParameterDetailGET('platform.sys.version');
      systemVersion.value = data?.value;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 系统信息中服务的激活校验调整为参数配置控制
  const getActivateCheck = async () => {
    try {
      const { data } = await reqBusinessParameterDetailGET('platform.sys.actived.service');
      const temp = data.value;
      activateVerification.value = temp;
      if (temp.includes('PMP')) {
        determinePlatformActived1();
      }
      if (temp.includes('MES')) {
        determineMesActived1();
      }
      if (temp.includes('LIMS')) {
        determineLimsActived1();
      }
      if (temp.includes('WMS')) {
        determineWmsActived1();
      }
      if (temp.includes('BSMS')) {
        determineBsmsActived1();
      }
      if (temp.includes('BIMS')) {
        determineBimsActived1();
      }
      if (temp.includes('LISMS')) {
        determineLismsActived1();
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 查询平台的权限
  const determinePlatformActived1 = async () => {
    try {
      const res: any = await determinePlatformActived({});
      let obj: any = {};
      if (res.data.active) {
        obj = {
          name: t('制药管理平台'),
          date: res.data.date,
        };
      } else {
        obj = {
          name: t('制药管理平台'),
          date: '',
        };
      }
      systemList.value[0] = obj;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查询mes的权限
  const determineMesActived1 = async () => {
    try {
      const res: any = await determineMesActived({});
      let obj: any = {};
      if (res.data.active) {
        obj = {
          name: t('制造执行系统'),
          date: res.data.date,
        };
      } else {
        obj = {
          name: t('制造执行系统'),
          date: '',
        };
      }
      systemList.value[1] = obj;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查询lims的权限
  const determineLimsActived1 = async () => {
    try {
      const res: any = await determineLimsActived({});
      let obj: any = {};
      if (res.data.active) {
        obj = {
          name: t('实验室信息管理系统'),
          date: res.data.date,
        };
      } else {
        obj = {
          name: t('实验室信息管理系统'),
          date: '',
        };
      }
      systemList.value[2] = obj;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查询wms的权限
  const determineWmsActived1 = async () => {
    try {
      const res: any = await determineWmsActived({});
      let obj: any = {};
      if (res.data.active) {
        obj = {
          name: t('仓储管理系统'),
          date: res.data.date,
        };
      } else {
        obj = {
          name: t('仓储管理系统'),
          date: '',
        };
      }
      systemList.value[3] = obj;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 查询bsms的权限
  const determineBsmsActived1 = async () => {
    try {
      const res: any = await determineBsmsActived({});
      let obj: any = {};
      if (res.data.active) {
        obj = {
          name: t('170'),
          date: res.data.date,
        };
      } else {
        obj = {
          name: t('170'),
          date: '',
        };
      }
      systemList.value[4] = obj;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 查询bims的权限
  const determineBimsActived1 = async () => {
    try {
      const res: any = await determineBimsActived({});
      let obj: any = {};
      if (res.data.active) {
        obj = {
          name: t('180'),
          date: res.data.date,
        };
      } else {
        obj = {
          name: t('180'),
          date: '',
        };
      }
      systemList.value[5] = obj;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 查询集中化lims的权限
  const determineLismsActived1 = async () => {
    try {
      const res: any = await determineLismsActived({});
      let obj: any = {};
      if (res.data.active) {
        obj = {
          name: t('210'),
          date: res.data.date,
        };
      } else {
        obj = {
          name: t('210'),
          date: '',
        };
      }
      systemList.value[6] = obj;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  onMounted(async () => {
    await getSystemVersion1();
    await getActivateCheck();
  });
</script>

<style scoped lang="less">
  .container {
    height: 100%;
    padding: 16px;
    background-color: #fff;
    .version {
      padding: 16px 16px 16px 24px;
      border-radius: 8px;
      font-size: 16px;
      position: relative;
      background: linear-gradient(90deg, #ebf2ff 0%, rgba(230, 244, 255, 0) 100%);
      img {
        position: absolute;
        height: 57px;
        top: 0px;
        left: 0px;
      }
      span {
        font-weight: 700;
      }
    }
    .title {
      font-size: 16px;
      position: relative;
      margin-left: 15px;
      color: #000;
      &::before {
        content: '';
        position: absolute;
        width: 4px;
        height: 16px;
        left: -13px;
        top: 4px;
        border-radius: 2px;
        background-color: #2871ff;
      }
    }
    .content {
      display: flex;
      flex-wrap: wrap;
      align-content: flex-start;
      margin-top: 20px;
      .contentItem {
        width: 220px;
        height: 120px;
        border-radius: 4px;
        padding: 15px 0px 0px 15px;
        margin-right: 80px;
        margin-bottom: 30px;
        background: linear-gradient(180deg, #ebf3ff 19.58%, #fafcff 100%);
        position: relative;
        .systemName {
          font-size: 18px;
          font-weight: 500;
          color: #242526;
          margin-bottom: 10px;
        }
        .expire {
          margin-top: 6px;
          color: #909398;
          z-index: 9;
        }
        // 背景
        .background {
          width: 118px;
          height: 78px;
          background-image: url('../../../assets/img/activationBg.png');
          background-size: cover;
          right: 0px;
          bottom: 0px;
          position: relative;
          position: absolute;
          .imageIcon {
            width: 52px;
            height: 50px;
            position: absolute;
            right: 0px;
            bottom: 0px;
          }
        }
      }
    }
  }
</style>

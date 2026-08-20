<template>
  <div class="left">
    <img
      :src="typeActive === TypeEnum.HUMAN_ALBUMIN ? imagePaths.humanAlbuminActive : imagePaths.humanAlbumin"
      @click="() => changeType(TypeEnum.HUMAN_ALBUMIN)" />
    <img
      :src="typeActive === TypeEnum.IMMUNOGLOBULINS ? imagePaths.immunoglobulinsActive : imagePaths.immunoglobulins"
      @click="() => changeType(TypeEnum.IMMUNOGLOBULINS)" />
    <div class="left-switch-box">
      <span>{{ t('设备透视') }}</span>
      <Switch v-model:checked="equipmentPerspective" @change="equipmentPerspectiveChange" />
    </div>
  </div>
  <div class="right">
    <Right :typeActive="typeActive" @toProcessProgress="toProcessProgress" />
  </div>
  <div class="footer">
    <div class="footer_left">
      <div class="footer_top">
        <template v-for="item in footerTop" :key="item.src">
          <div>
            <div class="footer_monitor">
              <img
                v-if="item.showMonitor"
                :src="
                  activeFooterMonitor === item.src
                    ? imageCommon['/src/assets/baiePng/processMonitorActive.png'].default
                    : imageCommon['/src/assets/baiePng/processMonitor.png'].default
                "
                :style="{
                  width: '32px',
                  height: '36px',
                  cursor: 'pointer',
                }"
                @click="() => clickMonitor(item)" />
            </div>
            <div class="footer_png">
              <img
                :src="activeFooter === item.src ? item.activeSrc : item.src"
                :style="{ cursor: 'pointer' }"
                @click="() => clickFooter(item)" />
            </div>
          </div>
          <div class="arrow-box">
            <BMIcons
              v-if="item.src !== footerTop[footerTop.length - 1].src"
              icon="dcRightRow"
              :style="{
                fontSize: '26px',
                width: '26px',
                height: '26px',
                cursor: 'auto',
              }" />
          </div>
        </template>
      </div>
      <div class="footer_bottom">
        <template v-for="item in footerBottom" :key="item.src">
          <div>
            <div class="footer_monitor">
              <img
                v-if="item.showMonitor"
                :src="
                  activeFooterMonitor === item.src
                    ? imageCommon['/src/assets/baiePng/processMonitorActive.png'].default
                    : imageCommon['/src/assets/baiePng/processMonitor.png'].default
                "
                :style="{
                  width: '32px',
                  height: '36px',
                  cursor: 'pointer',
                }"
                @click="() => clickMonitor(item)" />
            </div>
            <div class="footer_png">
              <img
                :src="activeFooter === item.src ? item.activeSrc : item.src"
                :style="{ cursor: 'pointer' }"
                @click="() => clickFooter(item)" />
            </div>
          </div>
          <div class="arrow-box">
            <BMIcons
              v-if="item.src !== footerBottom[footerBottom.length - 1].src"
              icon="dcRightRow"
              :style="{
                fontSize: '26px',
                width: '26px',
                height: '26px',
                transform: 'rotate(180deg)',
                cursor: 'auto',
              }" />
          </div>
        </template>
      </div>
    </div>
    <div class="footer_right">
      <BMIcons
        icon="dcRightRow"
        :style="{
          fontSize: '26px',
          width: '26px',
          height: '26px',
          transform: 'rotate(90deg)',
          cursor: 'auto',
        }" />
    </div>
  </div>
  <ModalMonitor v-if="activeMonitor" :code="modelId" @close="closeMonitor" />
  <!-- 设备信息 -->
  <EquipmentDetail v-if="showEquipmentDetail" :modelId="modelId" @close="equipmentClose"></EquipmentDetail>
</template>

<script setup lang="ts">
  import { t, currentLng } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  import Right from './Right.vue';
  import { TypeEnum } from './type';
  import ModalMonitor from '../ModalMonitor/index.vue';
  import EquipmentDetail from '../LayeredIntelligentControl/components/EquipmentDetail.vue';

  const emits = defineEmits(['toProcessProgress', 'sendMessage', 'closeProcess']);

  const toProcessProgress = () => {
    emits('toProcessProgress', typeActive.value);
  };

  const images = import.meta.glob('@/assets/baiePng/*/*/*.png', { eager: true });
  const imageCommon = import.meta.glob('@/assets/baiePng/*.png', { eager: true });

  const typeActive = ref<TypeEnum>(TypeEnum.HUMAN_ALBUMIN);

  const activeFooter = ref<string>();
  const equipmentPerspective = ref(false);

  const activeFooterMonitor = ref<string>();
  const activeMonitor = ref<boolean>(false);
  const showEquipmentDetail = ref(false);
  const modelId = ref<string>('');

  // 取消工序选中状态
  const closeActivated = () => {
    emits('sendMessage', {
      type: 'setProcessView',
    });
    activeFooter.value = undefined;
  };

  const changeType = (type: TypeEnum) => {
    typeActive.value = type;
    if (equipmentPerspective.value) {
      return;
    }
    emits('closeProcess');
    closeActivated();
    closeMonitor();
    emits('sendMessage', {
      type: 'changeProcessType',
      value: type === TypeEnum.HUMAN_ALBUMIN ? 0 : 1,
    });
  };

  const imagePaths = computed(() => {
    const lang = currentLng.value;
    return {
      humanAlbumin: images[`/src/assets/baiePng/${lang}/process/humanAlbumin.png`]?.default,
      humanAlbuminActive: images[`/src/assets/baiePng/${lang}/process/humanAlbuminActive.png`]?.default,
      immunoglobulins: images[`/src/assets/baiePng/${lang}/process/immunoglobulins.png`]?.default,
      immunoglobulinsActive: images[`/src/assets/baiePng/${lang}/process/immunoglobulinsActive.png`]?.default,
    };
  });

  // 点击工序图片
  const clickFooter = (item: any) => {
    if (equipmentPerspective.value) {
      return;
    }
    // 如果点击的是同一个图片，则取消工序选中状态
    if (activeFooter.value === item.src) {
      closeActivated();
      return;
    }
    activeFooter.value = item.src;
    emits('sendMessage', {
      type: 'setProcessView',
      id: item.id,
    });
  };

  // 点击监控图片
  const clickMonitor = (item: any) => {
    if (activeFooterMonitor.value === item.src) {
      activeFooterMonitor.value = undefined;
      activeMonitor.value = false;
      return;
    }
    activeFooterMonitor.value = item.src;
    activeMonitor.value = true;
  };

  // 设备弹窗打开
  const openDeviceDetail = (data: any) => {
    console.log('openDeviceDetail', data);
    modelId.value = data.id;
    showEquipmentDetail.value = true;
    activeFooter.value = undefined;
    closeMonitor();
  };

  // 设备弹窗关闭
  const equipmentClose = () => {
    showEquipmentDetail.value = false;
    emits('sendMessage', {
      type: 'closeDetail',
    });
  };

  // 关闭监控
  const closeMonitor = () => {
    activeFooterMonitor.value = undefined;
    activeMonitor.value = false;
  };

  const equipmentPerspectiveChange = (value: boolean) => {
    emits('closeProcess');
    activeFooter.value = undefined;
    closeMonitor();
    equipmentClose();
    emits('sendMessage', {
      type: 'equipmentPerspective',
      value,
      processType: typeActive.value === TypeEnum.HUMAN_ALBUMIN ? 0 : 1,
    });
  };
  const footerTop = computed(() => {
    const lang = currentLng.value;
    const type = typeActive.value === TypeEnum.HUMAN_ALBUMIN ? 'human' : 'group';
    return [
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}1.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active1.png`]?.default,
        showMonitor: true,
        id: type === 'human' ? 'SM_4楼设备_融浆罐（R0101_0' : 'SM_4楼设备_反应罐3___R0301_0',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}2.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active2.png`]?.default,
        showMonitor: typeActive.value === TypeEnum.HUMAN_ALBUMIN,
        id: type === 'human' ? 'SM_4楼设备_反应罐1___R0201_0' : 'SM_4楼设备_反应罐4___R0302_0',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}3.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active3.png`]?.default,
        id: type === 'human' ? 'SM_4楼设备_1_压滤机（X0208）_0' : 'SM_4楼设备_2_压滤机（X0308）_0',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}4.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active4.png`]?.default,
        id: type === 'human' ? 'SM_4楼设备_反应罐2___R0202_1' : 'SM_4楼设备_反应罐4___R0302_1',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}5.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active5.png`]?.default,
        id: type === 'human' ? 'SM_4楼设备_1_压滤机（X0208）_1' : 'SM_4楼设备_2_压滤机（X0308）_2',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}6.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active6.png`]?.default,
        id: type === 'human' ? 'SM_4楼设备_反应罐2___R0202_0' : 'SM_4楼设备_FII滤液暂存罐_配液罐2___R0304_0',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}7.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active7.png`]?.default,
        id: type === 'human' ? 'SM_4楼设备_1_压滤机（X0208）_2' : 'SM_4楼设备_2_压滤机（X0308）_1',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}8.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active8.png`]?.default,
        id: type === 'human' ? 'SM_4楼设备_FV滤液暂存罐_配液罐1___R0204_0' : 'SM_4楼设备_FII溶解罐__R0303_0',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}9.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active9.png`]?.default,
        id: type === 'human' ? 'SM_4楼设备_1_压滤机（X0208）_3' : 'SM_4楼设备_3_压滤机（X0224）_1',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}10.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active10.png`]?.default,
        id: type === 'human' ? 'SM_4楼设备_FV溶解罐__R0203_0' : 'SM_4楼设备_3_压滤机（X0224）_2',
      },
    ];
  });
  const footerBottom = computed(() => {
    const lang = currentLng.value;
    const type = typeActive.value === TypeEnum.HUMAN_ALBUMIN ? 'human' : 'group';
    return [
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}11.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active11.png`]?.default,
        showMonitor: true,
        id: type === 'human' ? 'SM_2楼设备10_多功能全自动装盒机_0' : 'SM_2楼设备10_多功能全自动装盒机_1',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}12.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active12.png`]?.default,
        showMonitor: true,
        id: type === 'human' ? 'SM_3楼设备05_灌装加塞机_M3D05_0' : 'SM_3楼设备05_灌装加塞机_M3D05_1',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}13.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active13.png`]?.default,
        showMonitor: true,
        id: type === 'human' ? 'SM_3楼设备04_灭菌隧道___M3C03_0' : 'SM_3楼设备04_灭菌隧道___M3C03_1',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}14.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active14.png`]?.default,
        showMonitor: true,
        id: type === 'human' ? 'SM_3楼设备01_脉动真空灭菌柜_1' : 'SM_3楼设备01_脉动真空灭菌柜_3',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}15.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active15.png`]?.default,
        showMonitor: true,
        id: type === 'human' ? 'SM_3楼设备01_脉动真空灭菌柜_0' : 'SM_3楼设备01_脉动真空灭菌柜_2',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}16.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active16.png`]?.default,
        showMonitor: true,
        id: type === 'human' ? 'SM_3楼设备_巴氏灭活罐（R0402）_0' : 'SM_3楼设备_低ph灭活罐2_（R0504）_0',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}17.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active17.png`]?.default,
        showMonitor: true,
        id: type === 'human' ? 'SM_3楼设备_0_9%Nacl罐（R0403）_0' : 'SM_3楼设备_静免超滤罐（R0501）_0',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}18.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active18.png`]?.default,
        showMonitor: typeActive.value !== TypeEnum.HUMAN_ALBUMIN,
        id: type === 'human' ? 'SM_4楼设备11_0' : 'SM_3楼设备14_脉动真空灭菌柜_0',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}19.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active19.png`]?.default,
        showMonitor: typeActive.value !== TypeEnum.HUMAN_ALBUMIN,
        id: type === 'human' ? 'SM_4楼设备_3_压滤机（X0224）_0' : 'SM_3楼设备14_脉动真空灭菌柜_1',
      },
      {
        src: images[`/src/assets/baiePng/${lang}/process/${type}20.png`]?.default,
        activeSrc: images[`/src/assets/baiePng/${lang}/process/${type}Active20.png`]?.default,
        showMonitor: typeActive.value !== TypeEnum.HUMAN_ALBUMIN,
        id: type === 'human' ? 'SM_4楼设备_FV溶解罐__R0203_1' : 'SM_3楼设备10_超滤模块（X0506）_0',
      },
    ];
  });

  defineExpose({
    openDeviceDetail,
  });
</script>

<style lang="less" scoped>
  .left {
    display: flex;
    width: 230px;
    height: 262px;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    gap: 50px;
    // 靠左居中
    position: absolute;
    left: 30px;
    top: 50%;
    transform: translateY(-50%);
    .left-switch-box {
      width: 100%;
      padding: 0 10px;
      box-sizing: border-box;
      height: 58px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 14px;
      color: #c3d7e5;
      background-image: url('@/assets/baiePng/switch-bg.png');
      background-size: 100%;
    }
    img {
      cursor: pointer;
      height: 52px;
    }
  }
  .footer {
    height: 198px;
    display: flex;
    padding: 0 30px;
    width: 100%;
    align-items: center;
    position: absolute;
    left: 0;
    bottom: 30px;
  }
  .footer_left {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 100%;
    flex: 1;
  }
  .footer_top,
  .footer_bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: nowrap;
    margin-top: 15px;
  }
  .footer_right {
    background-image: url('@/assets/baiePng/processFooterLine.png');
    height: 100%;
    background-repeat: no-repeat;
    display: flex;
    align-items: center;
    // 让 background-image 高居中 显示
    background-position-y: center;
    justify-content: flex-start;
    margin-top: 36px;
  }
  .footer_monitor {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 36px;
    width: 150px;
    margin-bottom: 4px;
  }
  .footer_png {
    display: flex;
    align-items: center;
  }
  .arrow-box {
    height: 100%;
    box-sizing: border-box;
    padding-bottom: 9px;
    display: flex;
    align-items: end;
  }
  .right {
    width: 480px;
    position: absolute;
    top: 100px;
    right: 0;
  }
</style>

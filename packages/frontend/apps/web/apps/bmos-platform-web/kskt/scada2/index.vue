<template>
  <div class="scada2_wrap">
    <div class="button-box">
      <div class="button1" @click="changeScada"></div>
      <div class="button2"></div>
      <div class="button3" @click="viewDevice"></div>
    </div>
    <div class="bigBg"></div>
    <div class="header">头部</div>
    <div class="largeBorder"></div>
    <div class="topLeft"></div>
    <div class="topRight"></div>
    <div class="downRight"></div>
    <div class="downLeft"></div>
    <div class="container">
      <div class="box grayPublic"><span>排风井</span></div>
      <div class="box1 grayPublic"><span>排风井</span></div>
      <div class="box2 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>L118细胞操作间</div>
        </div>
      </div>
      <div class="box3 bluePublic">
        <div class="content fiveText">
          <div class="small_House"></div>
          <div>微生物限度</div>
        </div>
      </div>
      <div class="box4 bluePublic">
        <div class="content fourText">
          <div class="small_House"></div>
          <div>无菌检测</div>
        </div>
      </div>
      <div class="box5 grayPublic">
        <div class="center first">L115一更</div>
        <div class="center second">L116二更</div>
      </div>
      <div class="box6 bluePublic">
        <div class="content contentWrap fourText">
          <div class="small_House"></div>
          <div>L113传递间</div>
        </div>
      </div>
      <div class="box7 bluePublic">
        <div class="content sixText">
          <div class="small_House"></div>
          <div>L109准备间</div>
        </div>
      </div>
      <div class="box8 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>L114清洗灭菌间</div>
        </div>
        <div class="flex"></div>
      </div>
      <div class="box9 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>L112阳性对照</div>
        </div>
      </div>
      <div class="box10 grayPublic flex1">
        <div class="center">L110一更</div>
        <div class="center">L111二更</div>
      </div>
      <div class="box11 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>L106细胞内霉素检查间</div>
        </div>
      </div>
      <div class="box12 bluePublic">
        <div class="content instrument">
          <div class="small_House"></div>
          <div>L107称量间</div>
        </div>
      </div>
      <div class="box13 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div class="numerator" style="transform: translate(-2px, 1px)">L105仪器检测室</div>
        </div>
      </div>
      <div class="box14 bluePublic right_border">
        <div class="content">
          <div class="small_House"></div>
          <div>L103理化实验室</div>
        </div>
      </div>
      <div class="box16 bluePublic right_border">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>L104样品间</div>
        </div>
      </div>
      <div class="box17 grayPublic"><span>排风井</span></div>
      <div class="box18 grayPublic">
        <div class="center" style="margin-top: 45px">排风井</div>
        <div class="center" style="margin-top: 44px">冷媒</div>
      </div>
      <div class="box19 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>102总更</div>
        </div>
      </div>
      <div class="box20 grayPublic"><span>卫生间 （男/女）</span></div>
      <span class="box21">质检区走廊</span>
      <div class="box22 bluePublic">
        <div class="content fourText">
          <div class="small_House"></div>
          <div>空调机房</div>
        </div>
        <div class="airPressure1">
          <div class="airPressure pointer" @click="toPath(pathList[8])"></div>
          <div class="center">空压机</div>
        </div>
        <div class="lengmei1">
          <div class="lengmei pointer" @click="toPath(pathList[10])"></div>
          <div class="center">6楼冷媒</div>
        </div>
        <div class="lengmei2">
          <div class="lengmei pointer" @click="toPath(pathList[11])"></div>
          <div class="center">顶楼冷媒</div>
        </div>

        <div v-for="item in 5" :key="item" :class="[`machineRoom${item}`]">
          <div class="machineRoom pointer" @click="toPath(pathList[item + 13])"></div>
          <div class="center">空调 -{{ item }}</div>
        </div>

        <div class="machineRoom6">
          <div class="machineRoom pointer" @click="toPath(pathList[19])"></div>
          <div class="center">工艺注射用水</div>
        </div>
        <div class="machineRoom7">
          <div class="machineRoom pointer"></div>
          <div class="center">冷却池</div>
        </div>
      </div>
      <div class="box23 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>121原料间</div>
        </div>
      </div>
      <div class="box24 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>L108培养间</div>
        </div>
      </div>
      <div class="box25 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>145机房</div>
        </div>
      </div>
      <div class="box26 grayPublic"><span>D146男一更</span></div>
      <div class="box27 grayPublic"><span>D147女一更</span></div>
      <div class="box28 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>122粗制间一（解冻、清洗、切制）</div>
        </div>
        <div>
          <div class="blister pointer" style="margin-top: 30px"></div>
          <div class="center">三维混合机</div>
        </div>
      </div>
      <div class="box29 bluePublic">
        <div class="content contentWrap fiveText">
          <div class="small_House"></div>
          <div>D150洁具洗存间</div>
        </div>
      </div>
      <div class="box30 grayPublic"><span>D148男二更</span></div>
      <div class="box31 grayPublic"><span>D149女二更</span></div>
      <div class="box32 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>123缓冲间</div>
        </div>
      </div>
      <div class="box33 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>D157粗制间二（酶解、灭菌）</div>
        </div>
        <div>
          <div class="blister pointer" style="margin-top: 30px"></div>
          <div class="center">三维混合机</div>
        </div>
      </div>
      <div class="box34 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>D156器具暂存间</div>
        </div>
      </div>
      <div class="box35 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>B173准备间</div>
        </div>
      </div>
      <div class="box36 bluePublic">
        <div class="content" style="padding-left: 6px">
          <div class="small_House"></div>
          <div>D158配液间</div>
        </div>
      </div>
      <div class="box38 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>B174生化1</div>
        </div>
        <div style="margin-right: 70px">
          <div class="reactionKettle pointer" @click="toPath(pathList[7])"></div>
          <div class="center">反应釜</div>
        </div>
      </div>
      <div class="box40 grayPublic"><span>传递窗</span></div>
      <div class="box42 bluePublic">
        <div class="content" style="margin-top: 170px">
          <div class="small_House"></div>
          <div>D155清洗准备间</div>
        </div>
        <div style="margin-right: 100px; margin-top: 72px">
          <div class="steam pointer" @click="toPath(pathList[13])"></div>
          <div class="center">蒸汽灭菌柜</div>
        </div>
        <div style="margin-right: -50px; margin-top: -145px">
          <div class="eq2 steam pointer" @click="toPath(pathList[9])"></div>
          <div class="center">干热灭菌柜</div>
        </div>
      </div>
      <div class="box45 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>B169洁物接收间</div>
        </div>
      </div>
      <span class="box46 text">粗制区走廊</span>
      <span class="box47 text">B级洁净走廊三</span>
      <span class="box48 text">B级洁净走廊二</span>
      <span class="box49 text">B级洁净走廊一</span>
      <span class="box50 text">脱D级洁净服</span>
      <span class="box51 text">气锁间</span>
      <span class="box52 text">更洁净衣间</span>
      <span class="box53 text">缓冲间</span>
      <span class="box54 text">D级洁净走廊</span>

      <div class="box56 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>D152物料暂存间</div>
        </div>
      </div>
      <div class="box57 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>C160洁净更衣间</div>
        </div>
      </div>
      <div class="box59 bluePublic">
        <div class="content contentWrap" style="margin-top: 40px">
          <div class="small_House"></div>
          <div>D153D级缓冲间</div>
        </div>
      </div>
      <div class="box60 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>154脱包间</div>
        </div>
      </div>
      <div class="box62 bluePublic">
        <div class="content contentWrap" style="margin-top: 24px">
          <div class="small_House"></div>
          <div>C162包材缓冲间</div>
        </div>
      </div>
      <div class="box63 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>C161预充脱包上瓶间</div>
        </div>
      </div>
      <div class="box64 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>B167预灌装间</div>
        </div>
        <div>
          <div class="preCanned1 pointer" style="margin-top: 60px"></div>
          <div class="center">预灌装机</div>
        </div>
      </div>
      <div class="box65 grayPublic"><div style="word-wrap: break-word">B176退更缓冲间</div></div>
      <div class="box66 grayPublic"><span>B177退更间</span></div>
      <div class="box68 bluePublic">
        <div class="content" style="margin-left: 163px">
          <div class="small_House"></div>
          <div>B172纯化间</div>
        </div>
        <div style="margin-left: 185px">
          <div class="purificationRoom pointer" @click="toPath(pathList[6])"></div>
          <div class="center">超滤</div>
        </div>
      </div>
      <div class="box69 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>B170过滤间</div>
        </div>
        <div>
          <div class="maturationRoom pointer" @click="toPath(pathList[12])"></div>
          <div class="center">稠厚机</div>
        </div>
        <div>
          <div class="highPressure2 pointer" style="margin-top: 20px"></div>
          <div class="center">均质机</div>
        </div>
      </div>
      <div class="box70 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>B168精制间(制剂间)</div>
        </div>
        <div>
          <div class="centrifuge pointer"></div>
          <div class="center">离心机</div>
        </div>
        <div>
          <div class="bubble pointer" style="margin-top: 20px"></div>
          <div class="center">消泡机</div>
        </div>
      </div>
      <div class="box71 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>178接收间</div>
        </div>
      </div>
      <div class="box72 bluePublic"></div>
      <div class="box73 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>制水间</div>
        </div>
        <div class="waterProduction1">
          <div class="waterProduction pointer" @click="toPath(pathList[1])"></div>
          <div class="center">纯化水 -分配</div>
        </div>
        <div class="waterProduction2">
          <div class="waterProduction pointer" @click="toPath(pathList[2])"></div>
          <div class="center">纯化水 -制备储存</div>
        </div>
        <div class="waterProduction3">
          <div class="waterProduction pointer" @click="toPath(pathList[3])"></div>
          <div class="center">注射水 -制备</div>
        </div>
        <div class="waterProduction4">
          <div class="waterProduction pointer" @click="toPath(pathList[4])"></div>
          <div class="center">注射水 -分配</div>
        </div>
        <div class="waterProduction5">
          <div class="waterProduction pointer" @click="toPath(pathList[5])"></div>
          <div class="center">纯蒸汽</div>
        </div>
      </div>
      <div class="box74 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>126废液存放间</div>
        </div>
      </div>
      <div class="box75 bluePublic">
        <div class="content fourText">
          <div class="small_House"></div>
          <div>不合格区</div>
        </div>
      </div>
      <div class="box76 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>危化暂存间</div>
        </div>
      </div>
      <div class="box78 bluePublic">
        <div class="content" style="margin-top: 46.67px">
          <div class="small_House"></div>
          <div>124物料库</div>
        </div>
      </div>
      <div class="box79 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>134灯检间</div>
        </div>
      </div>
      <div class="box80 grayPublic"><div style="word-wrap: break-word">135低温暂存缓冲间</div></div>
      <div class="box81 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>136低温暂存库</div>
        </div>
      </div>
      <div class="box82 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>C131取样分料间</div>
        </div>
      </div>
      <div class="box83 grayPublic"></div>
      <div class="box84 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>C133脱包间</div>
        </div>
      </div>
      <div class="box85 bluePublic">
        <div class="content contentWrap">
          <div class="small_House"></div>
          <div>C132气锁间</div>
        </div>
      </div>
      <div class="box86 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>137外包装间</div>
        </div>
        <div>
          <div class="blister pointer"></div>
          <div class="center">泡罩机</div>
        </div>
        <div>
          <div class="escapeNest pointer" style="margin-top: 20px"></div>
          <div class="center">脱巢机</div>
        </div>
      </div>
      <!-- 右边区域 -->
      <div class="box87 grayPublic"><span>弱电井</span></div>
      <div class="box88 grayPublic"><span>送风井</span></div>
      <div class="box89 grayPublic"><span>送风井</span></div>
      <div class="box90 grayPublic"><span>强电井</span></div>
      <div class="box91 bluePublic"></div>
      <!-- 井背景 -->
      <div class="box92"></div>
      <div class="box93 wellPublic"></div>
      <div class="box94 wellPublic"></div>
      <div class="box95 wellPublic"></div>
      <div class="box96 wellPublic"></div>
      <div class="box97 wellPublic"></div>
      <!-- 备用间 -->
      <div class="box98 grayPublic"><span>142男值班室</span></div>
      <div class="box99 grayPublic"><span>143女值班室</span></div>
      <div class="box100 grayPublic"><span>144综合办公室</span></div>
      <div class="box101 grayPublic"><span>货厅</span></div>
      <!-- 井2 -->
      <div class="box102 wellPublic"></div>
      <div class="box103 wellPublic"></div>
      <div class="box104 wellPublic"></div>
      <div class="box105 wellPublic"></div>
      <div class="box106 wellPublic"></div>
      <div class="box107 wellPublic"></div>
      <div class="box108 grayPublic"><span>水井</span></div>
      <div class="box109 grayPublic">
        <span>货梯</span>
        <span>3#</span>
        <div class="lineone"></div>
        <div class="linetwo"></div>
      </div>
      <div class="box110 grayPublic">
        <span>货梯</span>
        <span>4#</span>
        <div class="lineone"></div>
        <div class="linetwo"></div>
      </div>
      <div class="box111 grayPublic"><span>排烟井</span></div>
      <div class="box112 grayPublic"><span>前室</span></div>
      <div class="box113 grayPublic"><span>送风井</span></div>
      <div class="box114 grayPublic">
        <span style="margin-right: 60px">送风井</span>
        <span>送风井</span>
      </div>
      <div class="box115"></div>
      <span class="box116 text">生产区走廊</span>
      <div class="box117 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>139待检成品库</div>
        </div>
      </div>
      <div class="box118 bluePublic">
        <div class="content">
          <div class="small_House"></div>
          <div>138合格成品库</div>
        </div>
      </div>

      <div
        v-for="item in 32"
        :key="item"
        :class="['video', `no${item}`]"
        title="视频监控"
        @click="openDialog(videoList[item - 1], item)"></div>
    </div>
    <div></div>

    <Modal v-model:open="dialogVisible" :maskClosable="false" width="870px" :footer="null" @cancel="handleClose">
      <div class="outer_wrap" style="width: 850px; height: 500px">
        <videoComponent ref="video" :mode="0" :code="code" />
      </div>
    </Modal>
  </div>
</template>

<script>
  import { Modal } from 'ant-design-vue';
  import videoComponent from '../components/videoComponent.vue';
  import { pathList, videoList } from './equipmentList/list';
  export default {
    components: {
      videoComponent,
      Modal,
    },
    emits: ['change'],
    data() {
      return {
        pathList,
        videoList,
        dialogVisible: false,
        code: '',
      };
    },
    methods: {
      toPath(path) {
        console.log(path, 'path');
        // window.open(path);
      },

      openDialog(code, index) {
        // if (index === 1) {
        //   window.open(
        //     "http://172.16.0.32:38080/os/twin-model/#/preview-model?modelId=1613097679384670210&viewId=1613097679581802498"
        //   );
        //   return;
        // }
        // console.log("---------");
        // console.log(index);
        // if (index === 2) {
        //   window.open(
        //     "http://172.16.0.32:38080/os/twin-model/#/preview-model?modelId=1613097679384670210&viewId=1613101007497785346"
        //   );
        //   return;
        // }
        this.code = code;
        console.log(code, index, 'this.code..');
        this.dialogVisible = true;
        this.$nextTick(() => {
          this.$refs.video.initPlugin();
        });
      },
      handleClose() {
        this.$refs.video.destoryWnd();
        // this.dialogVisible = false;
      },
      changeScada() {
        this.$emit('change');
      },
      // 生产设备
      viewDevice() {
        window.open(
          'http://172.28.100.11:38080/hub-app/system/1860933391017340928/1859051263719858176.html?_=1736301955239',
        );
      },
    },
  };
</script>

<style lang="less" scoped>
  @media only screen and (max-width: 1900px) {
    .scada2_wrap {
      width: 1920px !important;
    }
    .topLeft {
      top: 100px !important;
      left: 16px !important;
    }
    .topRight {
      top: 100px !important;
      right: 15px !important;
    }
    .downRight {
      top: 2378px !important;
      right: 15px !important;
    }
    .downLeft {
      top: 2378px !important;
      left: 16px !important;
    }
  }

  .scada2_wrap {
    background: url('../assets/scada2/bg_color.png') repeat-y center/cover;
    // background: url("../assets/scada2/bigBg.png") repeat-y center/cover;

    width: 100%;
    overflow-x: hidden;
    height: auto;
    position: relative;
    box-sizing: border-box;
    padding: 0 0 55px 0;

    // 右上角按钮
    .button-box {
      position: absolute;
      top: 0;
      right: 0;
      z-index: 999;
      display: flex;
      top: 45px;
      right: 80px;
      .button1 {
        width: 140px;
        height: 46px;
        background: url('../assets/scada2/button1.png') no-repeat center/cover;
        cursor: pointer;
        margin-right: 30px;
      }
      .button2 {
        width: 140px;
        height: 46px;
        background: url('../assets/scada2/button2.png') no-repeat center/cover;
        cursor: pointer;
        margin-right: 30px;
      }
      .button3 {
        width: 140px;
        height: 46px;
        background: url('../assets/scada2/button3.png') no-repeat center/cover;
        cursor: pointer;
      }
    }
    // 大背景
    .bigBg {
      width: 1920px;
      height: 1080px;
      background: url('../assets/scada2/bigBg.png') repeat-y center/cover;
      position: fixed;
      top: 0px;
      left: 0px;
    }
    // 头部
    .header {
      background: url('../assets/scada2/header.png') repeat-y center/cover;
      width: 1878.67px;
      height: 86px;
      position: relative;
      left: 50%;
      transform: translateX(-50%);
    }

    border-radius: 0px;

    .largeBorder {
      background: url('../assets/scada2/largeBorder.png') no-repeat center/cover;
      position: absolute;
      top: 100px;
      width: 1888px;
      height: 2324px;
      // position: relative;
      left: 50%;
      transform: translateX(-50%);
    }
    .topLeft {
      background: url('../assets/scada2/topLeft_border.png') no-repeat center/cover;
      position: absolute;
      top: 100px;
      left: 8px;
      width: 41.5px;
      height: 44.69px;
    }
    .topRight {
      background: url('../assets/scada2/topRight_border.png') no-repeat center/cover;
      position: absolute;
      top: 100px;
      right: 7px;
      width: 41.5px;
      height: 45.69px;
    }
    .downRight {
      background: url('../assets/scada2/downRight_border.png') no-repeat center/cover;
      position: absolute;
      top: 2378px;
      right: 7px;
      width: 41.5px;
      height: 45.69px;
    }
    .downLeft {
      background: url('../assets/scada2/downLeft_border.png') no-repeat center/cover;
      position: absolute;
      top: 2378px;
      left: 8px;
      width: 41.5px;
      height: 45.69px;
    }
    .container {
      // background: url("../assets/scadaImg/kskt-bg.png") no-repeat center/cover;
      // border: 1px solid #fff;
      background: rgba(0, 60, 255, 0.100306);
      box-shadow: inset 0px 0.333333px 41.3333px 19.3333px rgba(22, 173, 255, 0.191024);
      width: 1846px;
      height: 2273px;
      margin-top: 35px;
      position: relative;
      left: 50%;
      transform: translateX(-50%);
      // 灰色透明盒子公共样式
      .grayPublic {
        background: rgba(254, 255, 255, 0.16);
        mix-blend-mode: normal;
        border: 0.333333px solid rgba(151, 151, 151, 0.16);
        font-size: 14px;
        color: #38d2ff;
        position: absolute;
        box-sizing: border-box;
      }
      // 蓝色透明盒公共样式
      .bluePublic {
        background: rgba(80, 125, 222, 0.5);
        mix-blend-mode: normal;
        font-size: 14px;
        color: #38d2ff;
        position: absolute;
        box-sizing: border-box;
      }
      .bluePublic:hover {
        background: linear-gradient(180deg, rgba(80, 125, 222, 0.18) 0%, rgba(80, 125, 222, 0.69) 100%);
      }
      // 井边框公共样式
      .wellPublic {
        //   background: #d8d8d8;
        background: rgba(20, 195, 247, 0.5);
        //   border: 0.333333px solid #979797;
        position: absolute;
      }
      // 细胞操作间类似样式
      .content {
        display: flex;
        align-items: center;
        // height: 24px;
        padding: 3px 0px 5px 8px;
        position: relative;
        background-image: url('../assets/scada2/bgLine.png'), url('../assets/scada2/bgLine2.png'),
          linear-gradient(90.09deg, rgba(0, 84, 183, 0.4) 0.07%, rgba(88, 156, 255, 0) 99.91%);
        background-position:
          -0.9px 0px,
          -0.9px 20px;
        background-repeat: no-repeat;
        > div:nth-child(2) {
          color: #fff;
        }
      }
      //若文字换行
      .contentWrap {
        align-items: flex-start;
        .small_House {
          margin-top: 3px;
        }
      }
      .flex {
        display: flex;
        justify-content: space-around;
        padding: 0px 8px;
      }
      .flex1 {
        display: flex;
        justify-content: space-around;
        align-items: center;
      }
      // 细胞操作间右边房子
      .small_House {
        background: url('../assets/scada2/small_House.png') no-repeat center/cover;
        width: 14px;
        height: 14px;
        margin-right: 3px;
      }
      // 空调类似图片
      .air-conditioning {
        width: 42px;
        height: 36px;
        background: url('../assets/scada2/air-conditioning.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 6px;
      }
      // 无菌隔离器
      .sterility {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/sterility.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 52px;
      }
      // 生物安全柜
      .sterilization {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/sterilization.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 17px;
      }
      // 空调系统
      .machineRoom {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/machineRoom.png') no-repeat center/cover;
        margin: 0 auto;
      }
      // 粗制间
      .roughRoom {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/roughRoom.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 17px;
      }
      // 反应釜
      .reactionKettle {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/reactionKettle.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 83px;
      }
      // 冷媒机
      .refrigerantMachine {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/refrigerantMachine.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 53px;
      }
      // 蒸汽灭菌柜
      .steam {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/steam.png') no-repeat center/cover;
        margin: 0 auto;
      }
      // 臭氧烘干灭菌柜
      .ozone {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/ozone.png') no-repeat center/cover;
        margin: 0 auto;
      }
      // 高压均质机
      .highPressure {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/highPressure.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 49px;
      }
      // 高压均质机2
      .highPressure2 {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/highPressure.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 16.31px;
      }
      // 消泡机
      .bubble {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/steam.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 16.31px;
      }
      // 纯化间
      .purificationRoom {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/purificationRoom.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 81px;
      }
      // 熟化间
      .maturationRoom {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/maturationRoom.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 21px;
      }
      // 离心机
      .centrifuge {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/reactionKettle.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 21px;
      }
      // 泡罩机
      .blister {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/steam.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 61px;
      }
      // 脱巢机
      .escapeNest {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/reactionKettle.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 21px;
      }
      // 制水系统
      .waterProduction {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/waterProduction.png') no-repeat center/cover;
        margin: 0 auto;
      }
      .waterProduction1 {
        position: absolute;
        top: 122.64px;
        right: 10.84px;
      }
      .waterProduction2 {
        position: absolute;
        top: 222.64px;
        right: 104.84px;
      }
      .waterProduction3 {
        position: absolute;
        top: 222.64px;
        right: 10.84px;
      }
      .waterProduction4 {
        position: absolute;
        top: 312.64px;
        right: 119.84px;
      }
      .waterProduction5 {
        position: absolute;
        top: 312.64px;
        right: 20.84px;
      }
      // 预罐装间
      .preCanned1 {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/highPressure.png') no-repeat center/cover;
        margin: 0 auto;
        margin-top: 16.31px;
      }

      // 2-8度冷库
      .staging {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/staging.png') no-repeat center/cover;
        //   margin-top: 33px;
        //   margin-left: 31px;
        margin: 0 auto;
      }

      .center {
        text-align: center;
        // margin-top: 5px;
      }

      .box {
        height: 470.6666564941406px;
        width: 45.33333206176758px;
        line-height: 470.6666564941406px;
        text-align: center;
        top: 20.33px;
        left: 13px;
      }
      .box1 {
        width: 232px;
        height: 45.33px;
        line-height: 45.33px;
        text-align: center;
        top: 20.33px;
        left: 70.67px;
      }
      .box2 {
        height: 104.33333587646484px;
        width: 216px;
        left: 67px;
        top: 77.33px;
      }
      .box3 {
        height: 132.3333282470703px;
        width: 136.6666717529297px;
        left: 67px;
        top: 185px;
      }
      .box4 {
        height: 170px;
        width: 136.6666717529297px;
        left: 67px;
        top: 321px;
      }
      .box5 {
        height: 216.6666717529297px;
        width: 93px;
        top: 76.5px;
        left: 287px;
      }
      .box6 {
        height: 100px;
        width: 93px;
        top: 297px;
        left: 286.67px;
      }
      .box7 {
        height: 338px;
        width: 128px;
        top: 59.33px;
        left: 383.33px;
      }
      .box8 {
        height: 138.3333282470703px;
        width: 187.3333282470703px;
        top: 59.33px;
        left: 515.67px;
      }
      .box9 {
        height: 104.3333282470703px;
        width: 187.3333282470703px;
        top: 201.67px;
        left: 515.67px;
      }
      .box10 {
        height: 87px;
        width: 187.33px;
        top: 310.33px;
        left: 515.67px;
      }
      .box11 {
        height: 121.33px;
        width: 102.67px;
        top: 59.33px;
        left: 707px;
      }
      .box12 {
        height: 212.33px;
        width: 102.67px;
        top: 185px;
        left: 707px;
      }
      .box13 {
        height: 337.67px;
        width: 98px;
        top: 59.33px;
        left: 813.67px;
      }
      .box14 {
        height: 338.67px;
        width: 314.75px;
        top: 59.33px;
        left: 915.67px;
        clip-path: polygon(0% 0px, 100% 0%, 100% 199px, 216px 199px, 216px 100%, 0% 100%);
      }
      // 改为裁剪了即不需要如下辅助盒
      // .box15 {
      //   height: 199px;
      //   width: 99px;
      //   top: 59.33px;
      //   left: 1131.67px;
      // }
      .box16 {
        height: 136px;
        width: 95px;
        top: 262px;
        left: 1135.33px;
      }
      .box17 {
        height: 335px;
        width: 118px;
        line-height: 335px;
        text-align: center;
        top: 61.67px;
        left: 1234.67px;
      }

      .box18 {
        height: 138.33px;
        width: 107.33px;
        top: 61.67px;
        left: 1356.33px;
      }
      .box19 {
        width: 107.33px;
        height: 194.33px;
        top: 204.33px;
        left: 1356.33px;
      }
      .box20 {
        line-height: 60px;
        text-align: center;
        width: 305px;
        height: 138.33px;
        top: 61.67px;
        left: 1467.67px;
      }
      .box21 {
        font-size: 14px;
        color: #fff;
        position: absolute;
        top: 439.33px;
        left: 773.33px;
      }
      .box22 {
        width: 257.33px;
        height: 850px;
        top: 502.33px;
        left: 48.67px;
        position: relative;
      }
      .box23 {
        width: 198.67px;
        height: 138.67px;
        top: 491px;
        left: 404px;
      }
      .box24 {
        width: 118.33px;
        height: 138.67px;
        top: 491px;
        left: 606.67px;
      }
      .box25 {
        width: 336px;
        height: 138.67px;
        top: 491px;
        left: 729px;
      }
      .box26 {
        width: 90.33px;
        height: 138.67px;
        top: 491px;
        left: 1069px;
        text-align: center;
        font-size: 12px;
        span {
          display: inline-block;
          margin-top: 46.33px;
        }
      }
      .box27 {
        width: 90.33px;
        height: 138.67px;
        top: 491px;
        left: 1163.33px;
        text-align: center;
        font-size: 12px;
        span {
          display: inline-block;
          margin-top: 46.33px;
        }
      }
      .box28 {
        width: 419.33px;
        height: 156.67px;
        top: 633.67px;
        left: 404px;
      }
      .box29 {
        width: 115.33px;
        height: 121.67px;
        top: 633.67px;
        left: 950.67px;
      }
      .box30 {
        width: 90.33px;
        height: 121.67px;
        top: 633.67px;
        left: 1069px;
        text-align: center;
        line-height: 90px;
      }
      .box31 {
        width: 90.33px;
        height: 121.67px;
        top: 633.67px;
        left: 1163.33px;
        text-align: center;
        line-height: 90px;
      }
      .box32 {
        width: 74px;
        height: 161px;
        top: 794.33px;
        left: 404px;
      }
      .box33 {
        width: 300.67px;
        height: 161px;
        top: 794.33px;
        left: 482px;
      }
      .box34 {
        width: 163px;
        height: 132.33px;
        top: 823px;
        left: 786.33px;
      }
      .box35 {
        width: 167px;
        height: 101px;
        top: 959.33px;
        left: 404px;
      }
      .box36 {
        width: 143.67px;
        height: 166.83px;
        top: 959.33px;
        left: 575px;
        clip-path: polygon(0% 0px, 100% 0%, 100% 100%, 36px 100%, 36px 101px, 0% 101px);
      }
      .box38 {
        width: 203.33px;
        height: 454.5px;
        top: 1064.33px;
        left: 404px;
        clip-path: polygon(0% 0px, 100% 0%, 100% 376px, 160px 376px, 160px 100%, 0% 100%);
      }
      .box40 {
        width: 72.67px;
        height: 76px;
        top: 1443.67px;
        left: 567.33px;
        text-align: center;
        line-height: 76px;
      }
      .box42 {
        width: 338.67px;
        height: 559.67px;
        top: 960px;
        left: 611.33px;
        clip-path: polygon(
          0% 171px,
          111px 171px,
          111px 0%,
          100% 0%,
          100% 262px,
          202px 262px,
          202px 100%,
          32px 100%,
          32px 481px,
          0% 480px
        );
      }
      .box45 {
        width: 133px;
        height: 294.33px;
        top: 1225.67px;
        left: 816.67px;
      }
      .text {
        font-size: 14px;
        color: #fff;
        position: absolute;
      }
      .box46 {
        top: 1050.33px;
        left: 319.33px;
      }
      .box47 {
        top: 1554.33px;
        left: 472.33px;
      }
      .box48 {
        top: 1554.33px;
        left: 695.33px;
      }
      .box49 {
        top: 1554.33px;
        left: 884.33px;
      }
      .box50 {
        width: 63px;
        top: 1104.33px;
        left: 956.33px;
      }
      .box51 {
        top: 1243.33px;
        left: 952.33px;
      }
      .box52 {
        top: 1328.33px;
        left: 952.33px;
      }
      .box53 {
        top: 1459.33px;
        left: 952.33px;
      }
      .box54 {
        top: 786.33px;
        left: 1134.33px;
      }
      .box55 {
        position: absolute;
        top: 758px;
        left: 1023px;
        font-size: 14px;
        color: #4deaff;
      }
      .box56 {
        width: 132.33px;
        height: 311.67px;
        top: 836px;
        left: 1023px;
      }
      .box57 {
        width: 132.33px;
        height: 71px;
        top: 1150.67px;
        left: 1023px;
      }
      .box59 {
        width: 93.67px;
        height: 144px;
        top: 835px;
        left: 1159.33px;
        clip-path: polygon(0% 0%, 73px 0%, 73px 38px, 100% 38px, 100% 100%, 0% 100%);
      }
      .box60 {
        width: 93.67px;
        height: 140.33px;
        top: 983px;
        left: 1159.33px;
      }
      .box62 {
        width: 93.67px;
        height: 95px;
        top: 1126.65px;
        left: 1159.33px;
        clip-path: polygon(0% 24px, 25px 24px, 25px 0%, 100% 0%, 100% 100%, 0% 100%);
      }
      .box63 {
        width: 230px;
        height: 162px;
        top: 1225.33px;
        left: 1023.33px;
      }
      .box64 {
        width: 230px;
        height: 199px;
        top: 1391.33px;
        left: 1023.33px;
      }
      .box65 {
        width: 77.67px;
        height: 98.67px;
        top: 1594px;
        left: 404px;
        padding-left: 5px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }
      .box66 {
        width: 77.67px;
        height: 98.67px;
        top: 1594px;
        left: 486.33px;
        padding-left: 5px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }
      .box68 {
        width: 398.33px;
        height: 205.67px;
        top: 1594px;
        left: 403.33px;
        clip-path: polygon(0% 103px, 163px 103px, 163px 0%, 100% 0%, 100% 100%, 0% 100%);
      }
      .box69 {
        width: 144px;
        height: 205.67px;
        top: 1594px;
        left: 805.67px;
      }
      .box70 {
        width: 166.33px;
        height: 205.67px;
        top: 1594px;
        left: 953.67px;
      }
      .box71 {
        width: 130px;
        height: 107.33px;
        top: 1594px;
        left: 1123px;
      }
      .box72 {
        width: 129px;
        height: 115.33px;
        top: 1705.33px;
        left: 1124px;
      }
      .box73 {
        width: 257.33px;
        height: 428.33px;
        top: 1356px;
        left: 48.67px;
      }
      .box74 {
        width: 257.33px;
        height: 170.33px;
        top: 1788.33px;
        left: 48.67px;
      }
      .box75 {
        width: 141.67px;
        height: 140.33px;
        top: 1962.67px;
        left: 48.67px;
      }
      .box76 {
        width: 141.67px;
        height: 148.67px;
        top: 2107px;
        left: 48.67px;
      }
      // .box77 {
      //   width: 163px;
      //   height: 41.67px;
      //   top: 1921px;
      //   left: 310.67px;
      // }
      .box78 {
        width: 279.33px;
        height: 337.67px;
        top: 1918px;
        left: 194.67px;
        clip-path: polygon(0% 46px, 115px 46px, 115px 0%, 100% 0%, 100% 100%, 0% 100%);
      }
      .box79 {
        width: 299.33px;
        height: 170.67px;
        top: 1919.67px;
        left: 479.33px;
      }
      .box80 {
        width: 126px;
        height: 107px;
        top: 1918px;
        left: 782.67px;
        padding-left: 5px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }
      .box81 {
        width: 126px;
        height: 225px;
        top: 2030.33px;
        left: 782.33px;
      }
      .box82 {
        width: 112px;
        height: 158px;
        top: 2097.33px;
        left: 666.33px;
      }
      .box83 {
        width: 182.67px;
        height: 65.67px;
        top: 2095.33px;
        left: 479.33px;
      }
      .box84 {
        width: 94px;
        height: 87px;
        top: 2168.33px;
        left: 479.33px;
      }
      .box85 {
        width: 85px;
        height: 87px;
        top: 2168.33px;
        left: 577.33px;
      }
      .box86 {
        width: 342px;
        height: 337px;
        top: 1918.33px;
        left: 912.33px;
      }
      .box87 {
        width: 107.33px;
        height: 155.67px;
        top: 491px;
        left: 1342px;
        text-align: center;
        line-height: 180px;
      }
      .box88 {
        width: 107.33px;
        height: 91.67px;
        top: 650.67px;
        left: 1342px;
        text-align: center;
        line-height: 100px;
      }
      .box89 {
        width: 107.33px;
        height: 193.67px;
        top: 747px;
        left: 1342px;
        text-align: center;
        line-height: 200px;
      }
      .box90 {
        width: 149.33px;
        height: 109.33px;
        top: 495px;
        left: 1662.33px;
        text-align: center;
        line-height: 120px;
      }
      .box91 {
        width: 149.33px;
        height: 134px;
        top: 608.33px;
        left: 1662.33px;
      }
      .box92 {
        width: 289.58px;
        height: 181px;
        //   background: url("../assets/scada2/well.png") repeat-y center/cover;
        background-image: url('../assets/scada2/well.png');
        background-size: 100% 100%;
        position: absolute;
        top: 759.67px;
        left: 1502px;
      }
      .box93 {
        width: 12.45px;
        height: 38.55px;
        top: 738.33px;
        left: 1460.54px;
        z-index: 2;
      }
      .box94 {
        width: 320.9px;
        height: 13.6px;
        top: 742.87px;
        left: 1472.77px;
      }
      .box95 {
        width: 16px;
        height: 198px;
        top: 743px;
        left: 1793.67px;
      }
      .box96 {
        width: 452.33px;
        height: 13.33px;
        top: 942px;
        left: 1357.33px;
      }
      .box97 {
        width: 12.44px;
        height: 22px;
        top: 919.67px;
        left: 1394.65px;
      }
      .box98 {
        width: 118.67px;
        height: 215.67px;
        top: 960px;
        left: 1342px;
        text-align: center;
        line-height: 320px;
      }
      .box99 {
        width: 107.33px;
        height: 215.67px;
        top: 960px;
        left: 1464.67px;
        text-align: center;
        line-height: 320px;
      }
      .box100 {
        width: 233.67px;
        height: 215.67px;
        top: 960px;
        left: 1576px;
        text-align: center;
        line-height: 320px;
      }
      .box101 {
        width: 465.67px;
        height: 192px;
        top: 1179.67px;
        left: 1344px;
        text-align: center;
        line-height: 180px;
      }
      .box102 {
        width: 40px;
        height: 16px;
        top: 1372.67px;
        left: 1344px;
      }
      .box103 {
        width: 16px;
        height: 172.67px;
        top: 1388.67px;
        left: 1344px;
      }
      .box104 {
        width: 159px;
        height: 16px;
        top: 1560.67px;
        left: 1344px;
      }
      .box105 {
        width: 38px;
        height: 16px;
        top: 1372.67px;
        left: 1771px;
      }
      .box106 {
        width: 16px;
        height: 172.67px;
        top: 1388.67px;
        left: 1793px;
      }
      .box107 {
        width: 166px;
        height: 16px;
        top: 1560.67px;
        left: 1643px;
      }
      .box108 {
        width: 134.67px;
        height: 173.33px;
        top: 1387.67px;
        left: 1360.33px;
        text-align: center;
        line-height: 173.33px;
        span {
          margin-left: 20px;
        }
      }
      .box109 {
        width: 98.67px;
        height: 173.33px;
        top: 1387.67px;
        left: 1508.33px;
        > span:nth-child(1) {
          position: absolute;
          top: 41px;
          left: 33px;
        }
        > span:nth-child(2) {
          position: absolute;
          top: 96px;
          left: 29px;
        }
        .lineone {
          width: 1px;
          height: 167.09px;
          background: #17a3c9;
          transform: rotate(151.87deg);
          position: absolute;
          top: 2px;
          left: 48px;
        }
        .linetwo {
          width: 1px;
          height: 167.09px;
          background: #17a3c9;
          transform: rotate(207.87deg);
          position: absolute;
          top: 2px;
          left: 48px;
        }
      }
      .box110 {
        width: 98.67px;
        height: 173.33px;
        top: 1387.67px;
        left: 1660.67px;
        > span:nth-child(1) {
          position: absolute;
          top: 41px;
          left: 33px;
        }
        > span:nth-child(2) {
          position: absolute;
          top: 96px;
          left: 29px;
        }
        .lineone {
          width: 1px;
          height: 167.09px;
          background: #17a3c9;
          transform: rotate(151.87deg);
          position: absolute;
          top: 2px;
          left: 48px;
        }
        .linetwo {
          width: 1px;
          height: 167.09px;
          background: #17a3c9;
          transform: rotate(207.87deg);
          position: absolute;
          top: 2px;
          left: 48px;
        }
      }
      .box111 {
        width: 134.67px;
        height: 64.67px;
        top: 1581.33px;
        left: 1356.33px;
        text-align: center;
        line-height: 64.67px;
      }
      .box112 {
        width: 134.67px;
        height: 155.33px;
        top: 1654.67px;
        left: 1356.33px;
        text-align: center;
        line-height: 70px;
        span {
          margin-right: 14px;
        }
      }
      .box113 {
        width: 86px;
        height: 60.33px;
        top: 1749.67px;
        left: 1502px;
        text-align: center;
        line-height: 60.33px;
      }
      .box114 {
        width: 197.67px;
        height: 60.33px;
        top: 1749.67px;
        left: 1599px;
        text-align: center;
        line-height: 60.33px;
      }
      .box115 {
        width: 248.33px;
        height: 155.22px;
        //   background: url("../assets/scada2/well.png") repeat-y center/cover;
        background-image: url('../assets/scada2/well_two.png');
        background-size: 100% 100%;
        position: absolute;
        top: 1581.33px;
        left: 1548.33px;
      }
      .box116 {
        top: 1861.33px;
        left: 764.33px;
      }
      .box117 {
        width: 442.67px;
        height: 160.33px;
        top: 1829.33px;
        left: 1356.33px;
      }
      .box118 {
        width: 442.67px;
        height: 220px;
        top: 2008.67px;
        left: 1356.33px;
      }

      .twoText {
        width: 66px;
      }
      .threeText {
        width: 80px;
      }
      .threeText2 {
        width: 75px;
      }
      .fourText {
        width: 93px;
      }

      .fiveText {
        width: 108px;
      }
      .fiveText2 {
        width: 103px;
      }
      .fiveText3 {
        width: 98px;
      }
      .fiveText4 {
        width: 143px;
      }
      .sixText {
        width: 122px;
      }
      .texts {
        width: 230px;
      }
      .texts2 {
        width: 190px;
      }

      // 部分特殊样式
      .instrument {
        margin-top: 85.33px;
      }

      .numerator {
        font-size: 12px;
      }
      .right_border {
        border-right: none;
      }
      .left_border {
        border-left: none;
      }
      .first {
        margin-top: 24.67px;
      }
      .second {
        margin-top: 120px;
      }

      // 空压机airPressure
      .airPressure {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/machineRoom.png') no-repeat center/cover;
        margin: 0 auto;
      }
      .airPressure1 {
        position: absolute;
        top: 42.64px;
        left: 16.17px;
      }

      // 冷媒图
      .lengmei {
        width: 55px;
        height: 47px;
        background: url('../assets/scada2/machineRoom.png') no-repeat center/cover;
        margin: 0 auto;
      }
      // 冷媒1
      .lengmei1 {
        position: absolute;
        top: 202.64px;
        right: 6.17px;
      }
      // 冷媒2
      .lengmei2 {
        position: absolute;
        top: 302.64px;
        right: 6.17px;
      }

      // 空调系统*7
      .machineRoom1 {
        position: absolute;
        top: 502.64px;
        right: 6.17px;
      }
      .machineRoom2 {
        position: absolute;
        top: 578px;
        right: 6.17px;
      }
      .machineRoom3 {
        position: absolute;
        top: 661px;
        right: 6.17px;
      }
      .machineRoom4 {
        position: absolute;
        top: 751px;
        right: 6.17px;
      }
      .machineRoom5 {
        position: absolute;
        top: 661px;
        right: 91.5px;
      }
      .machineRoom6 {
        position: absolute;
        top: 751px;
        right: 75.5px;
      }
      .machineRoom7 {
        position: absolute;
        top: 751px;
        right: 176.84px;
      }

      .video {
        background: url('../assets/scada2/camera.png') no-repeat center/cover;
        width: 17px;
        height: 21.67px;
        cursor: pointer;
      }

      .video.no1 {
        position: absolute;
        left: 250px;
        top: 84px;
      }

      .video.no2 {
        position: absolute;
        left: 78px;
        top: 345px;
      }

      .video.no3 {
        position: absolute;
        left: 239px;
        top: 469px;
      }

      .video.no4 {
        position: absolute;
        left: 522px;
        top: 270px;
      }

      .video.no5 {
        position: absolute;
        left: 761px;
        top: 797px;
      }

      .video.no6 {
        position: absolute;
        left: 993px;
        top: 758px;
      }

      .video.no7 {
        position: absolute;
        left: 1232px;
        top: 758px;
      }

      .video.no8 {
        position: absolute;
        left: 1035px;
        top: 1096px;
      }

      .video.no9 {
        position: absolute;
        left: 924px;
        top: 967px;
      }

      .video.no10 {
        position: absolute;
        left: 583px;
        top: 1069px;
      }

      .video.no11 {
        position: absolute;
        left: 745px;
        top: 1131px;
      }

      .video.no12 {
        position: absolute;
        left: 1043px;
        top: 1252px;
      }

      .video.no13 {
        position: absolute;
        top: 1555px;
        left: 1221px;
      }

      .video.no14 {
        position: absolute;
        left: 815px;
        top: 1619px;
      }

      .video.no15 {
        position: absolute;
        left: 965px;
        top: 1619px;
      }

      .video.no16 {
        position: absolute;
        left: 604px;
        top: 1769px;
      }

      .video.no17 {
        position: absolute;
        left: 764px;
        top: 1769px;
      }

      .video.no18 {
        position: absolute;
        left: 918px;
        top: 2200px;
      }

      .video.no19 {
        position: absolute;
        left: 1202px;
        top: 68px;
      }

      .video.no20 {
        position: absolute;
        left: 1451px;
        top: 433px;
      }

      .video.no21 {
        position: absolute;
        left: 1036px;
        top: 601px;
      }

      .video.no22 {
        position: absolute;
        left: 801px;
        top: 636px;
      }

      .video.no23 {
        position: absolute;
        left: 1578px;
        top: 1180px;
      }

      .video.no24 {
        position: absolute;
        left: 1350px;
        top: 1349px;
      }

      .video.no25 {
        position: absolute;
        left: 278px;
        top: 1754px;
      }

      .video.no26 {
        position: absolute;
        left: 278px;
        top: 1926px;
      }

      .video.no27 {
        position: absolute;
        left: 340px;
        top: 1894px;
      }

      .video.no28 {
        position: absolute;
        left: 167px;
        top: 2113px;
      }

      .video.no29 {
        position: absolute;
        left: 449px;
        top: 2118px;
      }

      .video.no30 {
        position: absolute;
        left: 671px;
        top: 2135px;
      }

      .video.no31 {
        position: absolute;
        left: 1329px;
        top: 1892px;
      }

      .video.no32 {
        position: absolute;
        left: 268px;
        top: 1325px;
      }
      .eqp1 {
        background: url('../../../assets/scadaImg/zhis.png') no-repeat center/cover;
        width: 50px;
        height: 50px;
        position: absolute;
        cursor: pointer;
      }

      .eqp1.pos1 {
        left: 252px;
        top: 1366px;
      }
      .eqp1.pos2 {
        left: 183px;
        top: 1698px;
      }
      .eqp1.pos2_ {
        left: 76px;
        top: 1490px;
      }
      .eqp1.pos3 {
        left: 183px;
        top: 1900px;
      }
      .eqp1.pos3_ {
        left: 219px;
        top: 2094px;
      }

      .eqp2 {
        background: url('../../../assets/scadaImg/gr.png') no-repeat center/cover;
        width: 50px;
        height: 50px;
        position: absolute;
        cursor: pointer;
        left: 771px;
        top: 1176px;
      }

      .eqp3 {
        background: url('../../../assets/scadaImg/sr.png') no-repeat center/cover;
        width: 50px;
        height: 50px;
        position: absolute;
        cursor: pointer;
        left: 708px;
        top: 1303px;
      }

      .eqp4 {
        background: url('../../../assets/scadaImg/jz.png') no-repeat center/cover;
        width: 50px;
        height: 50px;
        position: absolute;
        cursor: pointer;
        left: 1010px;
        top: 1721px;
      }

      .eqp5 {
        background: url('../../../assets/scadaImg/cl.png') no-repeat center/cover;
        width: 50px;
        height: 50px;
        position: absolute;
        cursor: pointer;
        left: 659px;
        top: 1648px;
      }
    }
  }
  .pointer {
    // cursor: pointer;
  }
  .scada3 {
    top: -68.67px;
    right: 19px;
    position: absolute;
  }
  .select {
    padding: 10px;
  }
</style>

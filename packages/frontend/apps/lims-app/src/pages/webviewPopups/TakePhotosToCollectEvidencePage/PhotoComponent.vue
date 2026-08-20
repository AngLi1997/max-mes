<!-- eslint-disable vue/no-v-text-v-html-on-component -->
<template>
  <view v-show="ready" class="photo-container">
    <view class="back-button" @click="closeCamera">
      <image class="back-icon" src="./image/back.png" />
    </view>
    <view class="html-box" v-html="videoHtml" />
    <view class="button-box">
      <div class="capture icon_box">
        <wd-icon
          class-prefix="bmos-app-icon"
          name="fanzhuanjingtou"
          size="18.75rpx"
          color="#FFF"
          @click="switchClick"
        />
      </div>
      <image class="capture" src="./image/capture.png" @click="capture" />
    </view>
  </view>
  <view v-show="!ready" class="loading-box">
    <wd-loading />
  </view>
  <uv-popup
    ref="popup"
    mode="center"
    custom-style="width:100%; height: 100%;"
    @change="change"
  >
    <view class="photo-container">
      <view class="canvas-box" v-html="canvasHtml" />
      <view class="button-box">
        <view class="button-item">
          <div class="crop-icon">
            <image class="icon" src="./image/cut.png" @click="imgCropper" />
          </div>
          <text class="text">
            {{ t("裁剪") }}
          </text>
        </view>
        <view class="button-item">
          <image class="icon" src="./image/sure.png" @click="confirm" />
          <text class="text">
            {{ t("确定") }}
          </text>
        </view>
        <view class="button-item">
          <image class="icon" src="./image/retake.png" @click="retake" />
          <text class="text">
            {{ t("重拍") }}
          </text>
        </view>
      </view>
    </view>
  </uv-popup>
  <BMImgCropper v-model:show="showImgCropper" :img-base64="imgBase64" @success="cropperSuccess" />
</template>

<script>
import { BMImgCropper } from '@/BMComponents';
import { uploadRecordItemFile } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';

export default {
  components: {
    BMImgCropper,
  },
  emits: ['success', 'fail', 'close'],
  data() {
    return {
      videoHtml: `<video id="videoer" width="1280" height="800" media-player="audioPlayer" autoplay crossOrigin="anonymous"></video>`,
      canvasHtml: `<canvas id="canvas"></canvas>`,
      showVideo: true,
      videoStream: null,
      ready: false,
      isMain: true,
      imgBase64: '',
      showImgCropper: false,
    };
  },
  mounted() {
    this.checkCamera();
  },
  beforeUnmount() {
    console.log('beforeDestroy');
    this.closeCamera();
  },
  methods: {
    switchClick() {
      this.isMain = !this.isMain;
      this.close();
      this.checkCamera();
    },
    close() {
      if (!this.videoStream)
        return;
      this.videoStream.getTracks().forEach(track => track.stop());
      this.videoStream = null;
    },
    closeCamera() {
      this.close();
      this.$emit('close');
    },
    async checkCamera() {
      const navigator = window.navigator.mediaDevices;
      const devices = await navigator.enumerateDevices();
      console.log('devices', devices);
      if (devices) {
        const stream = await navigator.getUserMedia({
          audio: false,
          // video: {
          //   // width: '100%',
          //   // height: '100%',
          //   // facingMode: { exact: "environment" }, //强制后置摄像头
          //   // facingMode: "user", //前置摄像头
          // }
          video: this.isMain
            ? { facingMode: 'environment', width: { ideal: 4096 }, height: { ideal: 2160 } }
            : { facingMode: 'user', width: { ideal: 4096 }, height: { ideal: 2160 } },
        });
        this.ready = true;
        this.videoStream = stream;
        const videoEl = document.getElementById('videoer');
        if (!videoEl)
          return;
        videoEl.srcObject = stream;
        videoEl.play();
      }
    },
    capture() {
      this.$refs.popup.open();
    },
    photoClose() {
      this.$refs.popup.close();
    },
    confirm() {
      const canvas = document.getElementById('canvas');
      canvas.toBlob(
        (blob) => {
          const file = new File([blob], `${new Date().getTime()}.jpg`);
          uploadRecordItemFile({
            path: file,
            type: 'jpg',
            success: () => {
              this.photoClose();
              this.$emit('success');
            },
            fail: () => {
              console.log('上传失败');
              this.$emit('fail');
            },
          });
        },
        'image/jpeg',
        1,
      );
    },
    imgCropper() {
      try {
        const canvas = document.getElementById('canvas');
        const base64 = canvas.toDataURL();
        this.imgBase64 = base64;
        this.showImgCropper = true;
      }
      catch (error) {
        console.log('error', error);
      }
    },
    cropperSuccess(data) {
      const img = new Image();
      img.src = data;
      img.onload = () => {
        const video = document.getElementById('videoer');
        const canvas = document.getElementById('canvas');
        const width = video.offsetWidth;
        const height = video.offsetHeight;
        canvas.width = 0; // 重置宽度
        canvas.width = width; // 重新设置宽度
        const context = canvas.getContext('2d');
        context.drawImage(img, 0, 0, width, height);
      };
    },
    retake() {
      this.$refs.popup.close();
    },
    getPixelRatio(context) {
      const backingStore = context.backingStorePixelRatio
        || context.webkitBackingStorePixelRatio
        || context.mozBackingStorePixelRatio
        || context.msBackingStorePixelRatio
        || context.oBackingStorePixelRatio
        || context.backingStorePixelRatio || 1;
      return (window.devicePixelRatio || 1) / backingStore;
    },
    change(e) {
      if (e.show) {
        this.$nextTick(() => {
          const canvas = document.getElementById('canvas');
          const video = document.getElementById('videoer');
          const context = canvas.getContext('2d');
          // const ratio = this.getPixelRatio(context);
          const ratio = 1;
          const width = video.offsetWidth;
          const height = video.offsetHeight;
          // 【重要】关闭抗锯齿
          context.mozImageSmoothingEnabled = false;
          context.webkitImageSmoothingEnabled = false;
          context.msImageSmoothingEnabled = false;
          context.imageSmoothingEnabled = false;
          canvas.width = width * ratio;
          canvas.height = height * ratio;
          context.drawImage(video, 0, 0, width * ratio, height * ratio);
          context.scale(ratio, ratio);
          // canvas.width = video.offsetWidth;
          // canvas.height = video.offsetHeight;
          // /* 要跟video的宽高一致 */
          // context.drawImage(video, 0, 0, video.offsetWidth, video.offsetHeight);
        });
      }
    },
    t(text) {
      return t(text);
    },
  },
};
</script>

<style lang="scss" scoped>
.photo-container {
  width: 100%;
  height: 100%;
  display: flex;
  position: relative;

  .back-button {
    position: absolute;
    left: 11.72rpx;
    top: 11.72rpx;
    z-index: 99;

    .back-icon {
      width: 35.17rpx;
      height: 35.17rpx;
    }
  }

  .button-box {
    position: absolute;
    right: 0;
    flex: 1;
    width: 93.79rpx;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    background-color: rgba(48, 49, 51, 0.9);

    .capture {
      width: 42.2rpx;
      height: 42.2rpx;
    }

    .icon_box{
        border: 3px solid #FFF;
        border-radius: 50%;
        margin-bottom: 48.75rpx;
        box-sizing: border-box;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    .button-item {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      text-align: center;
      color: #ffffff;
      font-size: 11.72rpx;
      width: 35.17rpx;
      // height: 56.86rpx;
      .icon {
        width: 35.17rpx;
        height: 35.17rpx;
      }
      .crop-icon {
        width: 25rpx;
        height: 25rpx;
        border: 2px solid #FFF;
        border-radius: 50%;
        padding: 10px;
        .icon {
          width: 24rpx;
          height: 24rpx;
        }
      }
      .text {
        margin-top: 4.69rpx;
      }
    }
  }
}

.loading-box {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.picture-box {
  width: 100%;
  height: 100%;
  display: flex;

  .photo-button-box {
    flex: 1;
    width: 93.79rpx;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(48, 49, 51, 0.9);
  }
}
</style>

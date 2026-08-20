<template>
  <view class="container">
    <view id="canvas-box" class="canvas-box">
      <canvas
        ref="canvasRef"
        canvas-id="canvas"
        class="canvas"
        :disable-scroll="true"
        @touchstart="touchStart"
        @touchmove="touchMove"
        @touchend="touchEnd"
        @mousedown="touchStart"
        @mousemove="touchMove"
        @mouseup="touchEnd"
      />
      <view v-if="!isSigned" class="tip">
        {{ t('在方框内沿文字方向签字') }}
      </view>
      <view class="clear" @click="clear(true)">
        <wd-icon
          class-prefix="bmos-app-icon"
          name="shanchu2"
          size="11.72rpx"
          color="#2871FF"
        />
        <wd-button type="text">
          {{ t('清空') }}
        </wd-button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { IP_CONFIG } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { getCurrentInstance, onMounted, ref, watch } from 'vue';

const props = defineProps({
  signUrl: {
    type: String,
    default: '',
  },
});

const emits = defineEmits(['change']);

const appInstance = getCurrentInstance().proxy;
const canvasInfo = ref({});

const isSigned = ref(false);
let ctx = null;
let isButtonDown = false;
let points = [];
let allPoints = [];

const canvasRef = ref(null);

const setCanvasInfo = () => {
  const query = uni.createSelectorQuery().in(appInstance);
  query
    .select('#canvas-box')
    .boundingClientRect((rect) => {
      canvasInfo.value = rect;
    })
    .exec();
};
  // 初始化画布
function initCanvas() {
  ctx = uni.createCanvasContext('canvas');
  // 设置画笔样式
  ctx.lineWidth = 8;
  ctx.lineCap = 'round';
  ctx.lineJoin = 'round';

  // 获取 canvas 的宽高
  setCanvasInfo();
  if (props.signUrl && props.signUrl.length > 0) {
    drawUrlToCanvas(props.signUrl);
  }
}

watch(() => props.signUrl, (newVal) => {
  if (newVal && newVal.length > 0) {
    drawUrlToCanvas(newVal);
  }
});
// 绘画
function draw(w) {
  const point1 = points[0];
  const point2 = points[1];

  if (!w) {
    allPoints[allPoints.length - 1].push(JSON.parse(JSON.stringify(points)));
  }

  points.shift();
  ctx.moveTo(point1.X, point1.Y);
  ctx.lineTo(point2.X, point2.Y);
  ctx.stroke();
  ctx.draw(true);
  isSigned.value = true;
  emits('change');
}

// 触摸开始，获取到起点
function touchStart() {
  allPoints.push([]);
  ctx.beginPath(); // 每次触摸开始，开启新的路径
  isButtonDown = true;
}

// 触摸移动，获取到路径点
function touchMove(e) {
  if (isButtonDown) {
    let movePoint = {};
    if (e.changedTouches[0].x) {
      movePoint = { X: e.changedTouches[0].x, Y: e.changedTouches[0].y };
    }
    else {
      const { top, left } = canvasInfo.value;
      const X = e.changedTouches[0].pageX - left;
      const Y = e.changedTouches[0].pageY - top;
      movePoint = { X, Y };
    }
    points.push(movePoint); // 存点
    const len = points.length;
    if (len >= 2) {
      draw(); // 绘制路径
    }
  }
}

// 把 base64 画到 canvas 上
function drawBase64ToCanvas(base64) {
  const img = new Image();
  img.src = base64;
  img.onload = () => {
    ctx.drawImage(img);
    ctx.draw(true);
  };
}

const drawUrlToCanvas = async (path) => {
  try {
    const baseUrl = `http://${getStorageSync(IP_CONFIG) || '172.30.1.160:80'}`;

    uni.downloadFile({
      url: `${baseUrl}/${path}`,
      success: (res) => {
        if (res.statusCode === 200) {
          // 获取图片信息
          uni.getImageInfo({
            src: res.tempFilePath,
            success: (imageInfoResult) => {
              // 创建 canvas 上下文
              const { width, height } = canvasInfo.value;
              if (!width || !height) {
                ctx.drawImage(res.tempFilePath, 0, 0, 1249, 612);
                const query = uni.createSelectorQuery().in(appInstance);
                query
                  .select('#canvas-box')
                  .boundingClientRect((rect) => {
                    const curWith = imageInfoResult.width > rect.width ? rect.width : imageInfoResult.width;
                    const curHeight = imageInfoResult.height > rect.height ? rect.height : imageInfoResult.height;
                    ctx.drawImage(res.tempFilePath, 0, 0, curWith, curHeight);
                  })
                  .exec();
              }
              else {
                const curWith = imageInfoResult.width > width ? width : imageInfoResult.width;
                const curHeight = imageInfoResult.height > height ? height : imageInfoResult.height;
                ctx.drawImage(res.tempFilePath, 0, 0, curWith, curHeight);
              }
              ctx.draw(true, () => {
                isSigned.value = true;
              });
            },
          });
        }
      },
      fail: (err) => {
        console.error('下载图片失败:', err);
      },
    });
  }
  catch (error) {
    //
  }
};
  // 触摸结束，将未绘制的点清空防止对后续路径产生干扰
function touchEnd() {
  allPoints = allPoints.filter((e) => {
    return e.length > 0;
  });
  points = [];
  isButtonDown = false;
}

// 清空, 传入true表示清空全部，不传传表示撤回一步
function clear(reset) {
  if (reset)
    allPoints = [];
  const { width, height } = canvasInfo.value;
  ctx.clearRect(0, 0, width, height);
  ctx.draw(true);
  isSigned.value = false;
  emits('change');
}

// 保存
const save = async () => {
  return new Promise((resolve, reject) => {
    if (!isSigned.value) {
      uni.showToast({
        title: t('请签名'),
        icon: 'none',
      });
      return reject(new Error(t('请签名')));
    }
    uni.canvasToTempFilePath({
      canvasId: 'canvas',
      success: (res) => {
        // 获取图片路径
        const { tempFilePath } = res;
        // 保存图片到相册
        // #ifdef H5
        resolve(tempFilePath);
        // #endif

        // #ifndef H5
        if (typeof plus === 'object') {
          plus.io.resolveLocalFileSystemURL(tempFilePath, (entry) => {
            entry.file((file) => {
              const fileReader = new plus.io.FileReader();
              fileReader.onload = function (data) {
                resolve(data.target.result);
              };
              fileReader.readAsDataURL(file);
            });
          });
        }
        // #endif
      },
      fail: () => {
        uni.showToast({
          title: t('转换图片失败'),
          icon: 'none',
        });
        return reject(new Error(t('转换图片失败')));
      },
    });
  });
};

onMounted(() => {
  initCanvas();
});

defineExpose({
  // drawUrl,
  drawBase64ToCanvas,
  save,
  clear,
});
</script>

<style lang="scss" scoped>
.container {
  height: 100%;
}
.canvas-box {
  width: 100%;
  height: 100%;
  .canvas {
    height: 100%;
    width: 100%;
    transition: height 0.3s;
    border: 1px dashed #c2c5cc;
    background: #f2f3f5;
  }
  .tip {
    // 居中
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 11.72rpx;
  }
  .clear {
    position: absolute;
    top: 9.38rpx;
    right: 9.38rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    .wd-button.is-text {
      padding-left: 2.93rpx;
    }
  }
}
</style>

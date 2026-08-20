<template>
  <div
    v-show="open"
    id="Modal_Record_nodelist_box"
    class="Modal_Record_nodelist_box"
    :style="{ top: boxTop + 'px', left: boxLeft + 'px' }">
    <div class="modal_title" @mousedown.stop="titleClick" @mouseup.stop="titleUp" @mouseout="boxOut">
      <div class="title">{{ t('组件选择') }}</div>
      <CloseOutlined class="icon-close" @click="close" />
    </div>
    <div class="modal_body" :style="{ height: `${isRage ? 300 : 700}px` }">
      <slot></slot>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { CloseOutlined } from '@ant-design/icons-vue';
  import { ref, watch } from 'vue';
  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    isRage: {
      type: Boolean,
      default: false,
    },
  });
  const emit = defineEmits(['update:open', 'close']);
  const close = () => {
    emit('update:open', false);
    emit('close');
  };
  watch(
    () => props.open,
    () => {
      boxTop.value = 120;
      boxLeft.value = 460;
    },
  );
  const pageY = ref(0);
  const pageX = ref(0);
  const isClick = ref(false);
  const boxTop = ref(120);
  const boxLeft = ref(460);
  const offsetX = ref(0);
  const offsetY = ref(0);

  const titleClick = (event: any) => {
    pageX.value = event.pageX;
    pageY.value = event.pageY;
    offsetX.value = event.offsetX;
    offsetY.value = event.offsetY;
    isClick.value = true;
  };
  const titleUp = () => {
    isClick.value = false;
  };

  const boxOut = (event: any) => {
    if (isClick.value) {
      boxLeft.value = boxLeft.value + event.pageX - pageX.value;
      boxTop.value = boxTop.value + event.pageY - pageY.value;
      pageX.value = event.pageX;
      pageY.value = event.pageY;
    }
  };
  // 设置监听,弹窗移动
  const setModelMove = () => {
    // 事件委托,绑定到body上
    document.addEventListener('mousemove', function (event) {
      // 检查事件是否来自模态框
      if (isClick.value) {
        boxLeft.value = boxLeft.value + event.pageX - pageX.value;
        boxTop.value = boxTop.value + event.pageY - pageY.value;
        pageX.value = event.pageX;
        pageY.value = event.pageY;
      }
    });
  };
  onMounted(() => {
    setModelMove();
  });
</script>
<style scoped lang="less">
  .Modal_Record_nodelist_box {
    width: 420px;
    position: fixed;
    background-color: white;
    z-index: 999;
    box-sizing: border-box;
    max-height: 95%;
    border-radius: 10px;
    box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.2);
  }
  .modal_title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px;
    &:hover {
      cursor: pointer;
    }
    &:active {
      cursor: move;
    }
    .title {
      color: #18191a;
      font-weight: 600;
      font-size: 16px;
      line-height: 1.5;
      word-wrap: break-word;
      user-select: none; /* 禁止选择文本 */
      -webkit-user-select: none; /* Safari 和 Chrome 的兼容性 */
      -moz-user-select: none; /* Firefox 的兼容性 */
      -ms-user-select: none; /* IE 和 Edge 的兼容性 */
    }
    .icon-close {
      font-size: 16px;
      padding: 3px;
      &:hover {
        color: #18191a;
        background-color: rgba(0, 0, 0, 0.06);
      }
    }
  }
  .modal_body {
    padding: 0 16px;
    overflow: auto;
  }
</style>

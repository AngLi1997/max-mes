<template>
  <div>
    <a-button type="primary" @click="showModal">Open Modal</a-button>
    <a-modal
      v-model:open="open"
      title="Basic Modal"
      @ok="handleOk"
      wrapClassName="modalSizeExtraLarge "
      cancelText="取消"
      okText="确定">
      <p>Some contents...</p>
      <p>Some contents...</p>
      <p>Some contents...</p>
      <p>Some contents...</p>
      <p>Some contents...</p>   
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  const open = ref<boolean>(false);
  const showModal = () => {
    open.value = true;
  };

  const handleOk = (e: MouseEvent) => {
    console.log(e);
    open.value = false;
  };
</script>

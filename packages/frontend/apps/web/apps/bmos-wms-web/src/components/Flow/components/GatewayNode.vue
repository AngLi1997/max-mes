<template>
  <div class="flow-gateway-node">
    <PlusOutlined class="gateway-icon" />
  </div>
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { Node } from '@antv/x6';

  const getNode = inject('getNode') as () => Node;

  const node = ref<any>({});

  onMounted(() => {
    node.value = getNode();
  });
</script>

<style scoped lang="less">
  /* Your CSS code here */
  .flow-gateway-node {
    width: 28px;
    height: 28px;
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    transform: rotate(45deg);
  }
  .gateway-icon {
    color: var(--bmos-primary-color);
    transform: rotate(-45deg);
  }
</style>

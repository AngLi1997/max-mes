<template>
  <div :class="isClick?'flow-item-node_box':''" style="padding: 4px;">
    <div :class="['flow-item-node',node.data?.status?.value]">
      <BMIcon type="Process2" class="item-icon" />
      <span>{{ node.data?.label || '' }}</span>
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { BMIcon } from '@bmos/components';
  import { Node } from '@antv/x6';

  const getNode = inject('getNode') as () => Node;
  const isClick = ref(false)
  const node = ref<any>({});
  onMounted(() => {
    node.value = getNode();
    node.value.on('change:data', ({current}:any) => {
      isClick.value = current.isClick
    })
  });
</script>

<style scoped lang="less">
  /* Your CSS code here */
  .flow-item-node {
    width: 100%;
    height: 100%;
    min-width: 130px;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    line-height: 44px;
    padding: 0 20px;
    color: #242526;
    cursor: pointer;
    background-color: #FFECD8;
    border: 1px solid #FF9A2F;
  }
  .RESOLVE{
    background-color: #E8FCFE;
    border: 1px solid #59BF78;
  }
  .CONFIRM{
    background-color: #fff;
    border: 0;
  }
  
  .flow-item-node_box {
    border: 2px dashed #2871ff;
    border-radius: 10px;
  }
  .item-icon {
    color: #6C7380;
  }
</style>

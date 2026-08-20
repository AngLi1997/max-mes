<template>
  <div class="step3">
    <Image
      src="/src/assets/img/importSuccess.png"
      width="118px"
      height="118px"
      :preview="false"></Image>
    <span>成功导入{{ props.count }}条物料信息</span>
  </div>
</template>

<script setup lang="tsx">
  export interface Props {
    count: number;
  }

  const props = withDefaults(defineProps<Props>(), {
    count: 0,
  });
</script>

<style lang="less" scoped>
  .step3 {
    padding-top: 60px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
</style>

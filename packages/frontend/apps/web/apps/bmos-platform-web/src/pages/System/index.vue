<template>
  <div>contents</div>
  <Button type="primary" @click="DEMO">Click me</Button>
  <iframe src="/app/bmos-platform/demo.html"></iframe>
</template>

<script setup lang="ts">
  const DEMO = ()=>{
    
  }
  onMounted(()=>{
    window.top?.addEventListener('message',(e)=>{
      console.log(e,'addEventListeneraddEventListeneraddEventListener');
      
    },false)
  })

</script>

<style scoped lang="less"></style>

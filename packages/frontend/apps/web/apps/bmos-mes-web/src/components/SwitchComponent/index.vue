<template>
  <div v-if="hasKeepAlive" class="switch-component">
    <keep-alive v-show="currentComponent === 0">
      <component
        :is="list[currentComponent]"
        v-if="force"
        :key="list[currentComponent]"
        :pageParams="pageParams"
        style="height: 100%"></component>
    </keep-alive>
    <component
      :is="list[currentComponent]"
      v-show="currentComponent !== 0"
      v-if="force"
      :pageParams="pageParams"
      style="height: 100%"></component>
  </div>
  <div v-else class="switch-component">
    <component :is="list[currentComponent]" v-if="force" :pageParams="pageParams" style="height: 100%"></component>
  </div>
</template>

<script setup lang="ts">
  import { Recordable } from '@bmos/components';
  import { ref, computed, nextTick, provide, FunctionalComponent, DefineComponent } from 'vue';

  const props = withDefaults(
    defineProps<{
      list?: Array<FunctionalComponent | DefineComponent>;
      hasKeepAlive?: boolean;
    }>(),
    {
      list: () => [],
      hasKeepAlive: false,
    },
  );

  const currentComponent = ref(0);
  const totalStep = computed(() => props.list.length);
  const force = ref<boolean>(true);

  const forcePage = () => {
    force.value = false;
    nextTick(() => {
      force.value = true;
    });
    currentComponent.value = 0;
  };

  const pageParams = ref<Recordable>({});
  const switchNext = (step: number = 1, params?: Recordable) => {
    if (params) {
      pageParams.value = params;
    }
    const total = totalStep.value;
    if (currentComponent.value === total) return;
    const ne = currentComponent.value + step;
    if (ne > total) {
      currentComponent.value = total;
      return;
    }
    currentComponent.value = ne;
  };

  const switchBack = (step: number = 1, params?: Recordable) => {
    if (params) {
      pageParams.value = params;
    }
    if (currentComponent.value === 0) return;
    const total = totalStep.value;
    if (step > total) return;
    const current = currentComponent.value;

    const ne = current - Math.abs(step);
    if (ne < 0) {
      currentComponent.value = 0;
      return;
    }
    currentComponent.value = ne;
  };

  const switchGo = (step: number = 1, params?: Recordable) => {
    if (step === void 0) return forcePage();

    if (typeof step !== 'number') return;

    if (step === 0) return forcePage();
    pageParams.value = {};
    if (step < 0) switchBack(step, params);
    else switchNext(step, params);
  };
  provide('switchGo', switchGo);
</script>

<style scoped lang="less">
  .switch-component {
    height: 100%;
    overflow: hidden;
  }
</style>

<template>
  <div class="flow-basic-node">
    <BMIcon type="Procedure2" />
    <div class="label">{{ labelName }}</div>
    <Divider type="vertical" />
    <Space>
      <BMIcon target="setting" type="Set" class="action-icon" @click.stop="setting" />
      <BMIcon v-if="showNextIcon" type="File" class="action-icon" @click.stop="clickNext" />
    </Space>
  </div>
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { Space, Divider } from 'ant-design-vue';
  import { Cell } from '@antv/x6';
  import { BMIcon, Recordable } from '@bmos/components';
  import { NodeFunctionEnum } from '../types';

  const emit = defineEmits(['setting', 'clickNext']);

  const getNode = inject('getNode') as () => Cell;

  const node = ref<any>({});
  const labelName = ref<string>('');

  const setting = () => {
    emit('setting', node.value as Cell);
  };

  const clickNext = () => {
    emit('clickNext', node.value as Cell);
  };

  const showNextIcon = ref(true);

  const changeShowNextIcon = (formData: Recordable) => {
    if (formData && formData.nodeFunction) {
      if (
        formData.nodeFunction === NodeFunctionEnum.ProcedureShift ||
        formData.nodeFunction === NodeFunctionEnum.ProcessShift ||
        formData.nodeFunction === NodeFunctionEnum.Inspection
      ) {
        showNextIcon.value = false;
      } else {
        showNextIcon.value = true;
      }
    }
  };

  onMounted(() => {
    node.value = getNode();
    labelName.value = node.value.data?.label || '';
    const { formData } = node.value.data;
    changeShowNextIcon(formData);
    node.value.on('change:data', ({ current }: any) => {
      const { label, formData } = current;
      changeShowNextIcon(formData);
      labelName.value = label;
    });
  });
</script>

<style scoped lang="less">
  /* Your CSS code here */
  .flow-basic-node {
    width: 100%;
    height: 100%;
    border-radius: 10px;
    background-color: var(--bmos-primary-color-white);
    display: flex;
    align-items: center;
    justify-content: space-around;
    line-height: 44px;
    padding: 0 10px;
    border: 1px solid var(--bmos-primary-color-white);
    .mes-divider-vertical {
      height: 1.5em;
    }
    .label {
      line-height: 1.3em;
      width: 100px;
      text-align: center;
      flex: 1;
    }
  }

  .action-icon {
    color: var(--bmos-primary-color);
    cursor: pointer;
  }
</style>

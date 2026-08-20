<template>
  <div class="param-config">
    <span class="param-title">{{ title() }}</span>
    <div class="operation-area">
      <Node
        v-if="target.target"
        :target="target.target"
        :showIcon="!show"
        @icon-click="$emit('icon-click', target)"></Node>
      <div v-else-if="hasAdd" class="operation-add-icon">
        <div style="display: flex; column-gap: 22px">
          <SvgIcon
            icon="InnnerAdd"
            :class="[IS_CHECK ? 'innner-check' : '']"
            @click="$emit('add', target, 0)"></SvgIcon>
          <SvgIcon
            v-if="component.componentType != 'SUBMIT_SIGN'"
            icon="OuterAdd"
            @click="$emit('add', target, 1)"></SvgIcon>
        </div>
        <Button v-if="delete" type="link" @click="deleteParam">{{ t('删除参数') }}</Button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import Node from './Node.vue';
  import SvgIcon from '../../../components/svg-icon/index.vue';
  import { storeToRefs } from 'pinia';
  import { QUOTE, SIGN } from '../enum';
  import { useCheckComponent } from '../store/useCheckComponent';
  import { t } from '@bmos/i18n';
  const store = useCheckComponent();
  const { CHECK_STATUS, formulaParses } = storeToRefs(store);
  const { deleteFormulaParam } = store;
  defineEmits(['icon-click', 'add']);
  const props = withDefaults(
    defineProps<{
      target: { key: string; value: string; target: any };
      hasAdd: boolean;
      formulaType: string;
      num: number;
      show: boolean;
      delete: boolean;
      component: any;
    }>(),
    {
      hasAdd: true,
      show: false,
    },
  );

  const IS_CHECK = computed(() => {
    return (
      CHECK_STATUS.value.status && !CHECK_STATUS.value.outside && CHECK_STATUS.value.currentField === props.target.key
    );
  });

  const title = () => {
    if (props.formulaType === QUOTE || props.formulaType === '9' || props.formulaType === '10') {
      return t('参数');
    }
    if (props.formulaType === SIGN) return `${t('参数')}${props.num + 1}`;
    if (!props.target.value) return `${props.target.key}`;
    return `${props.target.key}(${props.target.value})`;
  };

  const deleteParam = () => {
    deleteFormulaParam(props.target.key);
    formulaParses.value &&
      formulaParses.value.forEach((item, index) => {
        item.key = `${t('参数')}${index + 1}`;
      });
  };
</script>

<style scoped lang="less">
  .param-config {
    display: flex;
    column-gap: 6px;
    align-items: unset;
    .param-title {
      display: block;
      width: 88px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      padding-block: 10px;
    }
    .operation-area {
      flex: 1;
    }
  }
  .operation-add-icon {
    display: flex;
    width: 100%;
    column-gap: 22px;
    color: var(--bmos-primary-color);
    align-items: center;
    justify-content: space-between;
  }
</style>

<template>
  <div class="param-config">
    <template v-if="!isEmpty(target)">
      <Node
        :target="target"
        :showIcon="show"
        @icon-click="$emit('deleteClick', target)"
        @node-click="(tg: any) => $emit('nodeClick', tg)"></Node>
    </template>
    <BMIcons
      v-else-if="hasAdd"
      icon="InnnerAdd"
      :class="['operation-add-icon', isCheck ? 'innner-check' : '']"
      @click="$emit('add', target)"></BMIcons>
  </div>
</template>

<script setup lang="ts">
  import { Recordable } from '@bmos/components';
  import Node from './Node.vue';
  import { BMIcons } from '@bmos/icons';
  import { isEmpty } from '@bmos/utils';

  defineEmits(['deleteClick', 'add', 'nodeClick']);
  const props = withDefaults(
    defineProps<{
      target: Recordable;
      hasAdd: boolean;
      show?: boolean;
      checkStatus: any;
      row: Recordable;
    }>(),
    {
      hasAdd: true,
      show: true,
      checkStatus: {
        status: false,
        currentField: void 0,
        behavior: void 0,
      },
      target: () => ({}),
      row: () => ({}),
    },
  );

  const isCheck = computed(() => {
    return (
      props.checkStatus.status &&
      (props.checkStatus.row?.id
        ? props.checkStatus.row.id === props.row.id
        : props.checkStatus.row?.key === props.row?.key)
    );
  });
</script>

<style scoped lang="less">
  .param-config {
    display: flex;
    column-gap: 6px;
    align-items: unset;
    margin: 4px;
    .param-title {
      display: block;
      width: 88px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      padding-block: 10px;
    }
  }
  .operation-add-icon {
    color: var(--bmos-primary-color);
  }
  .innner-check {
    color: #ff9a2f;
    box-shadow: 0px 0px 0px 2px #ff9a2f33;
    border-radius: 4px;
    cursor: url('~@/assets/images/cursor.png') auto;
  }
</style>

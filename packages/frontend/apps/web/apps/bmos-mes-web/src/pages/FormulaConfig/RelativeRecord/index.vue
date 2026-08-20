<template>
  <div class="relatice-record">
    <div class="record-container">
      <div class="record-list-container">
        <BMSearchTree
          v-model:selected-keys="SELECTED_KEYS"
          v-model:expanded-keys="EXPANDED_KEYS"
          :tree-data="treeData"
          :fieldNames="{
            title: 'name',
            key: 'itemId',
          }"
          :showSearch="false"
          :showAllAddIcon="false"
          :showAction="false"
          @select="TREE_SELECT"></BMSearchTree>
      </div>
      <Record
        ref="EDITOR_INSTANCE"
        class="record-content"
        :active-keys="NODE_ACTIVES"
        @node-dbclick="dbClick"
        @node-click="click"></Record>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMSearchTree } from '@bmos/components';
  import { Record } from '../../../components/Record';
  import { t } from '@bmos/i18n';
  import { useTree, useNode } from '../hooks';
  import { useEDITOR } from './hook';
  import { message } from 'ant-design-vue';
  import {
    DATE_COMPONENT,
    NUMBER_COMPONENT,
    TEXT_COMPONENT,
    SELECT_COMPONENT,
  } from '@/components/Record/NodeList/enum';
  import { PropType, ref } from 'vue';
  import { cloneDeep } from '@bmos/utils';

  const treeData = ref<any[]>([]);
  const props = defineProps({
    type: {
      type: String,
      default: '',
    },
    options: {
      type: Array as PropType<Array<any>>,
      default: () => [],
      readonly: true,
    },
    currentComponent: {
      type: Object as PropType<any>,
      default: () => ({}),
    },
    currentFormula: {
      type: Object as PropType<any>,
      default: () => ({}),
    },
    currentSelectRecordItemKeys: {
      type: Array as PropType<Array<string>>,
      default: () => [],
      readonly: true,
    },
  });
  const emit = defineEmits(['confirm']);
  const node = useNode();
  const editor = useEDITOR(node, emit);
  const { CURRENT_COMPONENT } = node;
  const { SELECTED_KEYS, TREE_SELECT, CURRENT_NODE, EXPANDED_KEYS, setCurrent } = useTree(editor, node);
  const { EDITOR_INSTANCE, NODE_ACTIVES, NODE_CLICK, cancelCheck } = editor;

  const getCheckNode = () => {
    return {
      ...CURRENT_COMPONENT.value,
      relevance: `${CURRENT_NODE.value?.name}`,
    };
  };
  /**
   * @description 获取组件类型
   * @param component
   */
  const getComponentType = (component: any) => {
    if (component?.componentType === 'CUSTOM_FIELD') {
      try {
        const detail = JSON.parse(component.componentDetail!);
        return detail?.dataType;
      } catch (error) {
        return component.componentType;
      }
    }
    return component?.componentType;
  };

  // 校验是否选择同一个组件，类型是否匹配
  const checkComponent = (key: string) => {
    if (props.currentComponent.fieldId === key) {
      message.error(t('不能引用自身'));
      return false;
    }
    const componentType = getComponentType(CURRENT_COMPONENT.value);
    if (!componentType) {
      return false;
    }
    switch (props.type) {
      case 'DATE':
        if (![...DATE_COMPONENT, ...SELECT_COMPONENT].includes(componentType)) {
          message.error(t('请选择日期、选择组件'));
          return false;
        }
        break;
      case 'NUMBER':
        if (![...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(componentType)) {
          message.error(t('请选择数值、选择组件'));
          return false;
        }
        break;
      case 'TEXT':
        if (props.currentFormula?.formulaId === '9') {
          if (![...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(componentType)) {
            message.error(t('请选择数值、选择组件'));
            return false;
          }
        } else if (props.currentFormula?.formulaId === '11') {
          if (
            ![...TEXT_COMPONENT, ...NUMBER_COMPONENT, ...DATE_COMPONENT, ...SELECT_COMPONENT].includes(componentType)
          ) {
            message.error(t('请选择文字、数值、日期、选择组件'));
            return false;
          }
        } else {
          if (![...TEXT_COMPONENT, ...SELECT_COMPONENT].includes(componentType)) {
            message.error(t('请选择文字组件'));
            return false;
          }
        }
        break;
      case 'TIME':
        if (![...DATE_COMPONENT, ...SELECT_COMPONENT].includes(componentType)) {
          message.error(t('请选择日期、选择组件'));
          return false;
        }
        break;
      case 'RADIO':
        if (![...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(componentType)) {
          message.error(t('请选择数值、选择组件'));
          return false;
        }
        break;
      case 'CHECKBOX':
        if (![...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(componentType)) {
          message.error(t('请选择数值、选择组件'));
          return false;
        }
        break;
      default:
        break;
    }
    return true;
  };

  const click = (tar: any, key: string, ...args) => {
    NODE_CLICK(tar, key, ...args);
    if (!checkComponent(key)) {
      cancelCheck();
    }
  };
  const dbClick = (tar: any, key: string) => {
    if (!key) return;
    const flag = checkComponent(key);
    if (flag) emit('confirm', getCheckNode());
  };
  /**
   * @description: 递归过滤options中符合条件的值(!props.currentSelectRecordItemKeys.includes(item.itemId))
   * @param {Array} options
   * */
  const filterOptions = (options: Array<any>) => {
    return options.filter(item => {
      if (item.children) {
        item.children = cloneDeep(filterOptions(item.children));
        return item.children.length > 0;
      }
      if (!props.currentSelectRecordItemKeys.includes(item.itemId)) {
        return true;
      }
      return false;
    });
  };

  onMounted(() => {
    treeData.value = filterOptions(cloneDeep(props.options));
    const current = treeData.value?.[0]?.children[0];
    SELECTED_KEYS.value = [current?.itemId];
    EXPANDED_KEYS.value = [treeData.value?.[0]?.itemId];
    setCurrent(current);
  });

  defineExpose({
    getCheckNode,
    checkComponent,
  });
</script>

<style scoped lang="less">
  .relatice-record {
    height: 700px;
    display: flex;
    flex-direction: column;
    .record-container {
      display: flex;
      flex: 1;
      overflow: auto;
    }
    .record-list-container {
      width: 300px;
      height: 100%;
      overflow-y: auto;
      border-block: 1px solid #e6e6e6;
    }
    .record-content {
      flex: 1;
    }
  }
</style>

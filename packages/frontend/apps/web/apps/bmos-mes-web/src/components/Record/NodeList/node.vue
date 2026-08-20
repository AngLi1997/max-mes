<template>
  <div
    class="node-container"
    :class="[
      actived ? 'node-actived' : '',
      rounded ? 'node-container-round' : '',
      bordered ? 'node-container-bordered' : '',
      lookup ? '' : used ? '' : 'node-not-used',
    ]"
    @click.stop="nodeClick">
    <div class="node-content-container">
      <div class="node-content node-content-icon">
        <BMIcons
          :icon="icon"
          :style="{
            verticalAlign: 'initial',
          }" />
      </div>
      <div class="node-content node-content-type" :title="title + getComponentNumber">
        {{ title }}{{ getComponentNumber }}
      </div>
    </div>
    <div v-if="showIcon && !lookup" class="node-operation">
      <FormOutlined v-if="getComponentEdit" class="operation-icon icon-edit" @click.stop="editClick" />
      <BMIcons v-if="getComponentCopy" class="operation-icon icon-copy" icon="COPY" @click.stop="copyClick" />
      <CloseOutlined v-if="getComponentDelete" class="operation-icon icon-delete" @click.stop="iconClick" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { CloseOutlined, FormOutlined } from '@ant-design/icons-vue';
  import { BMIcons } from '@bmos/icons';
  import { nodePropsType, NodeDataType } from './type';
  import {
    BUSINESS_NODE_INFO,
    EQUIPMENT_NODE_INFO,
    ALL_BUSINESS_NODE_INFO,
    CLEAN_NODE_INFO,
    COPY_BUSINESS_NODE_INFO,
    OUTPUT_BUTTON_INFO,
    ALL_DYNAMIC_TABLE_NODE,
    ALL_ADD_DELETE_NODE,
  } from './enum';

  const emit = defineEmits(['node-click', 'icon-click', 'edit-click', 'copy-click']);
  // eslint-disable-next-line vue/no-reserved-props
  const props = withDefaults(defineProps<nodePropsType & { showIcon: boolean; lookup: boolean }>(), {
    title: 'No.1',
    type: t('数字'),
    actived: false,
    rounded: true,
    bordered: true,
    used: false,
    icon: '',
    showIcon: true,
    componentNumber: 0,
    lookup: false,
  });
  // 获取组件的序号
  const getComponentNumber = computed(() => {
    if (BUSINESS_NODE_INFO[props.type]) {
      return '';
    }
    if (EQUIPMENT_NODE_INFO[props.type]) {
      return '';
    }
    if (CLEAN_NODE_INFO[props.type]) {
      return '';
    }
    if (OUTPUT_BUTTON_INFO[props.type]) {
      return '';
    }
    if (ALL_DYNAMIC_TABLE_NODE.includes(props.type)) {
      return `No. ${props.componentNumber} `;
    }
    if (ALL_ADD_DELETE_NODE.includes(props.type)) {
      return `No. ${props.componentNumber} `;
    }
    if (!ALL_BUSINESS_NODE_INFO[props.type] && props.type !== 'CUSTOM_FIELD') {
      return props.componentNumber;
    }
    return `No. ${props.componentNumber} `;
  });
  // 获取组件的编辑权限
  const getComponentEdit = computed(() => {
    return ['RADIO', 'CHECKBOX', 'CUSTOM_FIELD', 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE'].includes(props.type);
  });

  // 获取组件的删除权限
  const getComponentDelete = computed(() => {
    if (OUTPUT_BUTTON_INFO[props.type]) {
      return true;
    }
    if (ALL_ADD_DELETE_NODE.includes(props.type)) {
      return true;
    }
    return !ALL_BUSINESS_NODE_INFO[props.type];
  });

  // 获取可以复制的组件权限
  const getComponentCopy = computed(() => {
    return COPY_BUSINESS_NODE_INFO[props.type];
  });

  const NODE_DATA = computed<NodeDataType>(() => {
    const { title, type, item, actived, nodeKey } = props;
    const clickData: NodeDataType = {
      title,
      type,
      data: item,
      actived,
      key: nodeKey,
    };

    return clickData;
  });

  const nodeClick = () => {
    emit('node-click', NODE_DATA.value);
  };

  const iconClick = () => {
    emit('icon-click', NODE_DATA.value);
  };

  const editClick = () => {
    emit('edit-click', NODE_DATA.value);
  };

  const copyClick = () => {
    emit('copy-click', NODE_DATA.value);
  };
</script>

<style scoped lang="less">
  .node-operation {
    display: flex;
    column-gap: 8px;
    z-index: 1;
    padding: 2px;
    border-radius: 2px;
    .icon-edit {
      display: none;
    }
    .icon-copy {
      display: none;
    }
    .icon-delete {
      display: none;
    }
    .operation-icon:hover {
      background-color: #e1e3e5;
    }
  }

  .node-container {
    .node-content-container {
      width: 100%;
    }
    .node-content-type {
      text-overflow: ellipsis;
      max-width: calc(100% - 29px);
      overflow: hidden;
    }
  }

  .node-container:hover {
    .node-content-container {
      width: 80%;
    }
    .icon-copy {
      display: inline-block;
    }
  }

  .node-content {
    line-height: 1.4;
    font-weight: 400;
  }
  .node-container-bordered {
    border: 1px solid rgba(212, 215, 217, 1);
  }
  .node-container-round {
    border-radius: 4px;
  }
  .node-content-icon {
    font-size: 20px;
    line-height: 1;
  }

  .node-actived {
    border-color: rgba(40, 113, 255, 1);
    background-color: rgba(235, 241, 255, 1);
    .node-content-type {
      color: rgba(40, 113, 255, 1);
    }
    .node-content-title {
      color: rgba(40, 113, 255, 1);
    }
    .node-content-container {
      width: 80%;
    }
    .node-operation {
      .icon-edit {
        display: block;
      }
      .icon-delete {
        display: block;
      }
    }
  }
  .node-not-used {
    position: relative;
    &::before {
      content: '';
      display: block;
      position: absolute;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background-color: rgba(255, 154, 47, 1);
      top: 3px;
      left: 3px;
    }
  }
</style>

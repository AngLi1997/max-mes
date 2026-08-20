<template>
  <div
    class="node-container"
    :class="[
      actived ? 'node-actived' : '',
      rounded ? 'node-container-round' : '',
      bordered ? 'node-container-bordered' : '',
      lookup ? '' : (used ? '' : 'node-not-used'),
    ]"
    @click.stop="nodeClick">
    <div class="node-content-container">
      <span class="node-content node-content-icon">
        <SvgIcon :icon="icon" @click.stop="iconClick" />
      </span>
      <span class="node-content node-content-type">{{ type }}</span>
      <span class="node-content node-content-title">
        No.{{ componentNumber }}
      </span>
    </div>
    <div class="node-opeartion" v-if="showIcon && !lookup">
      <FormOutlined
        @click.stop="editClick"
        v-if="edit"
        class="opeartion-icon icon-edit" />
      <CloseOutlined
        class="opeartion-icon icon-delete"
        @click.stop="iconClick" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { CloseOutlined, FormOutlined } from '@ant-design/icons-vue';
  import SvgIcon from '../../svg_icon/index.vue';
  import { t } from '@bmos/i18n';
  const emit = defineEmits(['node-click', 'icon-click', 'edit-click']);
  import { nodePropsType, NodeDataType } from './type';
  const props = withDefaults(
    defineProps<
      nodePropsType & { showIcon: boolean; lookup: boolean; edit: boolean }
    >(),
    {
      title: 'No.1',
      type: t('数字'),
      actived: false,
      rounded: true,
      bordered: true,
      key: '',
      used: false,
      icon: '',
      showIcon: true,
      componentNumber: 0,
      lookup: false,
      edit: true,
    },
  );

  const cloneObj = (obj: any) => {
    if (!obj) return obj;
    if (typeof cloneObj !== 'object') return obj;
    return JSON.parse(JSON.stringify(obj));
  };

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
</script>

<style scoped lang="less">
  .node-opeartion {
    display: flex;
    column-gap: 8px;
    z-index: 1;
    .icon-edit {
      display: none;
    }
    .icon-delete {
      display: none;
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
    .node-opeartion {
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

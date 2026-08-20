<template>
  <div class="component-node-basic">
    <div class="component-node-title">
      {{ t('基础组件') }}
      <div class="rage-btn" @click="isRageChange">{{ isRage ? t('关闭') : t('批量模式') }}</div>
    </div>
    <div class="component-node-list">
      <NodeComponent
        v-for="(component, type) in NODE_INFO"
        :key="type"
        :data="component"
        :clickType="clickNode?.componentType"
        v-bind="$attrs"
        @node-click="nodeClick"></NodeComponent>
    </div>
    <div v-show="!isRage">
      <div v-if="BUSINESS_NODE_PERMISSION" class="component-node-title">{{ t('物料组件') }}</div>
      <div v-if="BUSINESS_NODE_PERMISSION" class="component-node-list buiness">
        <BusinessNodeComponent
          v-for="(component, type) in BUSINESS_NODE_INFO"
          :key="type"
          :data="component"
          v-bind="$attrs"></BusinessNodeComponent>
      </div>
      <div v-if="EQUIPMENT_NODE_PERMISSION" class="component-node-title">{{ t('设备组件') }}</div>
      <div v-if="EQUIPMENT_NODE_PERMISSION" class="component-node-list buiness">
        <BusinessNodeComponent
          v-for="(component, type) in EQUIPMENT_NODE_INFO"
          :key="type"
          :data="component"
          v-bind="$attrs"></BusinessNodeComponent>
      </div>
      <div v-if="CLEAN_NODE_PERMISSION" class="component-node-title">{{ t('清场组件') }}</div>
      <div v-if="CLEAN_NODE_PERMISSION" class="component-node-list buiness">
        <BusinessNodeComponent
          v-for="(component, type) in CLEAN_NODE_INFO"
          :key="type"
          :data="component"
          v-bind="$attrs"></BusinessNodeComponent>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import NodeComponent from '../NodeComponent/index.vue';
  import BusinessNodeComponent from './components/BusinessNodeComponent.vue';
  import { NODE_INFO, BUSINESS_NODE_INFO, EQUIPMENT_NODE_INFO, CLEAN_NODE_INFO } from '@/components/Record';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { onMounted } from 'vue';
  import { usePermissionStore } from '@/stores/permission';
  const { hasPermission } = usePermissionStore();

  const BUSINESS_NODE_PERMISSION = ref(false);
  const EQUIPMENT_NODE_PERMISSION = ref(false);
  const CLEAN_NODE_PERMISSION = ref(false);
  const props = defineProps({
    isRage: {
      type: Boolean,
      default: false,
    },
    clickNode: {
      type: Object,
      default: () => {},
    },
  });

  const nodeClick = (data: any) => {
    if (!props.isRage) {
      // 未开启批量模式
      emit('node-click', data);
    } else {
      emit('save-node', data);
    }
  };

  const emit = defineEmits(['update:isRage', 'update:clickNode', 'node-click', 'save-node']);

  const isRageChange = () => {
    message.info(props.isRage ? t('您已关闭批量编辑模式') : t('您已进入批量编辑模式'));
    emit('update:isRage', !props.isRage);
    emit('update:clickNode', {});
  };

  onMounted(() => {
    // 判断业务组件有无权限
    for (let key in BUSINESS_NODE_INFO) {
      if (hasPermission(BUSINESS_NODE_INFO[key].permission || '')) {
        BUSINESS_NODE_PERMISSION.value = true;
      }
    }
    // 判断设备组件有无权限
    for (let key in EQUIPMENT_NODE_INFO) {
      if (hasPermission(EQUIPMENT_NODE_INFO[key].permission || '')) {
        EQUIPMENT_NODE_PERMISSION.value = true;
      }
    }
    // 判断清场组件有无权限
    for (let key in CLEAN_NODE_INFO) {
      if (hasPermission(CLEAN_NODE_INFO[key].permission || '')) {
        CLEAN_NODE_PERMISSION.value = true;
      }
    }
  });
</script>

<style scoped lang="less">
  .component-node-basic {
    user-select: none;
  }
  .component-node-title {
    line-height: 1.5;
    border-bottom: 1px solid rgba(225, 227, 229, 1);
    padding: 4px 12px;
    display: flex;
    justify-content: space-between;
  }
  .component-node-list {
    display: flex;
    padding: 16px 0;
    flex-wrap: wrap;
    row-gap: 15px;
  }
  .buiness {
    column-gap: 20px;
    row-gap: 15px;
  }
  .rage-btn {
    color: #2871ff;
    cursor: pointer;
  }
</style>

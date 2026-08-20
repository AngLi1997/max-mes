<template>
  <div class="container">
    <Modal
      :okText="t('确定')"
      :cancelText="t('取消')"
      :open="open"
      :title="t('选择人员')"
      :maskClosable="false"
      destroyOnClose
      @ok="handleOk"
      wrapClassName="modalSizeLarge"
      @cancel="close">
      <div class="modalContent">
        <Shuttle v-bind="$props" ref="shuttleRef"></Shuttle>
      </div>
    </Modal>
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import Shuttle from './components/Shuttle/index.vue';
  import { message, Modal } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  const shuttleRef = ref();

  const props = defineProps({
    openPeople: {
      type: Boolean,
      default: false,
    },
    hasCheckPeople: {
      type: Array,
      default: () => [],
    },
    isView: {
      type: Boolean,
      default: false,
    },
  });

  const emit = defineEmits(['update:openPeople', 'update:people']);

  const close = () => {
    emit('update:openPeople', false);
  };

  const open = computed({
    get: () => {
      return props.openPeople;
    },
    set: val => {
      emit('update:openPeople', val);
    },
  });

  const handleOk = async (e: MouseEvent) => {
    const checkNodes = shuttleRef.value.getCheckNodes();
    try {
      emit('update:people', checkNodes);
    } catch (error: any) {
      error.message && message.error(error.message);
      throw error;
    }
  };
</script>

<style scoped lang="less">
  .modalContent {
    border: 1px solid #e1e3e5;
    height: 100%;
    height: 510px;
  }
  .modal-container {
    width: 100%;
    display: flex;
    height: 100%;
  }

  .topNav {
    background-color: #fafafa;
    padding: 16px;
    height: 48px;
    line-height: 1;
  }

  .bmos-search-tree {
    height: 100%;
    width: 100%;
  }
</style>

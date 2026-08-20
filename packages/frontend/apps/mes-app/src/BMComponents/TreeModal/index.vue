<template>
  <BMModal
    v-model="open"
    size="large"
    :="modalAttrs"
    @confirm="confirm"
    @cancel="cancel"
  >
    <view class="tree-modal-content">
      <BMTree
        v-if="open"
        v-model="value"
        v-model:checkedNodes="checkedNodes"
        :="attrs"
        :mode="mode"
      />
    </view>
  </BMModal>
</template>

<script setup>
  import BMModal from '@/BMComponents/Modal/index.vue';
  import BMTree from '@/BMComponents/Tree/index.vue';
  import { ref, computed, useAttrs, watch } from 'vue';

  const attrs = useAttrs();
  const props = defineProps({
    open: {
      type: Boolean,
      default: false
    },
    modelValue: {
      type: Array,
      default: () => []
    },
    modalAttrs: {
      type: Object,
      default: () => ({})
    },
    required: {
      type: Boolean,
      default: false
    },
    mode: {
      type: String,
      default: 'single'
    }
  });

  const value = ref([]);
  const checkedNodes = ref([]);

  const emit = defineEmits([
    'update:modelValue',
    'update:open',
    'confirm',
    'cancel'
  ]);

  const open = computed({
    get: () => props.open,
    set: (val) => {
      emit('update:open', val);
    }
  });

  const confirm = () => {
    if (props.required && !value.value.length) {
      return;
    }
    open.value = false;
    if (props.mode === 'single') {
      emit('confirm', checkedNodes.value[0] || null);
      emit('update:modelValue', value.value[0] || '');
    } else {
      emit('confirm', checkedNodes.value);
      emit('update:modelValue', value.value);
    }
  };

  const cancel = () => {
    open.value = false;
    emit('cancel');
  };

  watch(
    () => props.open,
    (val) => {
      if (val) {
        if (Array.isArray(props.modelValue)) {
          value.value = [...props.modelValue];
        } else {
          value.value = props.modelValue ? [props.modelValue] : [];
        }
      }
    }
  );
</script>

<style lang="scss" scoped>
.tree-modal-content {
  height: 280.08rpx;
}
</style>

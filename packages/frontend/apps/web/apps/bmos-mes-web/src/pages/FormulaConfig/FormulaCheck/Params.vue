<template>
  <div class="param-config-container">
    <div class="param-config-title">
      <div>
        <span>{{ t('参数配置') }}：</span>
        <Popover>
          <template #content>
            <PopoverContent />
          </template>
          <ExclamationCircleOutlined />
        </Popover>
      </div>
      <Button
        v-if="params.indefiniteParam && !show"
        type="link"
        :icon="h(PlusOutlined)"
        style="padding: 0"
        @click="addFormulaParses">
        {{ t('添加参数') }}
      </Button>
    </div>
    <div v-if="SIGN === formulaType && !show" class="sign-operation">
      <BMIcons
        icon="InnnerAdd"
        :class="[IS_INNER ? 'innner-check' : '']"
        @click="() => $emit('add', SIGN === formulaType, {}, 0)"></BMIcons>
      <BMIcons icon="OuterAdd" @click="() => $emit('add', SIGN === formulaType, {}, 1)"></BMIcons>
    </div>
    <div class="params-container">
      <Item
        v-for="(item, i) in formulaParses"
        :key="i"
        :show="show"
        :hasAdd="!(SIGN === formulaType)"
        :target="item"
        :component="component"
        :formulaType="formulaType"
        :num="i"
        :delete="params.indefiniteParam"
        @icon-click="(...args) => $emit('delete-param', SIGN === formulaType, ...args)"
        @add="(...args) => $emit('add', SIGN === formulaType, ...args, params)" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import Item from './Item.vue';
  import { t } from '@bmos/i18n';
  import { Popover } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { useCheckComponent } from '../store/useCheckComponent';
  import PopoverContent from './Popover/index.vue';
  import { SIGN } from '../enum';
  import { BMIcons } from '@bmos/icons';
  import { storeToRefs } from 'pinia';
  import { h } from 'vue';
  import { PlusOutlined } from '@ant-design/icons-vue';

  defineEmits(['add', 'delete-param']);
  const store = useCheckComponent();
  const { formulaParses, IS_INNER } = storeToRefs(store);
  const { addCheckedComponents } = store;

  // 不定参数添加
  const addFormulaParses = () => {
    addCheckedComponents({
      key: `${t('参数')}${formulaParses.value.length + 1}`,
      value: '',
    });
  };
  defineProps({
    params: {
      type: Object,
      default: () => {},
    },
    component: {
      type: Object,
    },
    formulaType: {
      type: String,
      default: '',
    },
    show: {
      type: Boolean,
      default: false,
    },
  });
</script>

<style scoped lang="less">
  .params-container {
    display: flex;
    flex-direction: column;
    row-gap: 1rem;
    margin-top: 36px;
  }
  .param-config-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .sign-operation {
    padding-block: 16px;
    display: flex;
    column-gap: 22px;
  }
</style>

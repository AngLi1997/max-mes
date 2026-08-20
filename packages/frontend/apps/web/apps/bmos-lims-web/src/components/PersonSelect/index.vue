<template>
  <Modal
    v-model:open="open"
    :title="t('选择人员')"
    :maskClosable="false"
    destroyOnClose
    @ok="handleOk"
    class="person-select-modal"
    wrapClassName="modalSizeLarge">
    <Row class="container">
      <Col :span="12" class="person-list">
        <Row class="person-header">
          <Col :span="24">
            <Space :size="20">
              <span class="person-title">{{ t('人员列表') }}</span>
              <span class="person-number">{{ 100 }}</span>
            </Space>
          </Col>
        </Row>
        <Row class="person-content">
          <Col :span="24">
            <BMSearchTree
              ref="searchTreeRef"
              v-model:checkedKeys="checkedKeys"
              v-model:expandedKeys="expandedKeys"
              @check="checkPerson"
              defaultExpandAll
              :showAllAddIcon="false"
              :showAddChildren="false"
              :showDeleteNode="false"
              :showAction="false"
              checkable
              v-bind="treeProps"></BMSearchTree>
          </Col>
        </Row>
      </Col>
      <Col :span="12">col-12</Col>
    </Row>
  </Modal>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import {
    BMSearchTree,
    SearchTreeInstance,
    SearchTreeProps,
  } from '@bmos/components';
  import { Key } from 'ant-design-vue/es/_util/type';

  const emit = defineEmits(['update:open']);
  const props = withDefaults(
    defineProps<{
      open: boolean;
    }>(),
    {},
  );

  const open = computed({
    get() {
      return props.open;
    },
    set(val) {
      emit('update:open', val);
    },
  });

  // 树方法
  const searchTreeRef = ref<SearchTreeInstance>();
  const treeProps: SearchTreeProps = reactive({
    addChildrenNeedCode: true,
    treeData: [],
  });

  const checkedKeys = ref<string[]>(['']);
  const expandedKeys = ref<string[]>(['all']);

  // 点复选框
  const checkPerson = (checkedKeys: Key[], e: any) => {
    console.log(checkedKeys, e);
  };

  const handleOk = () => {
    open.value = false;
  };
</script>

<style lang="less">
  .person-select-modal {
    .container {
      border-radius: 4px;
      border: 1px solid var(--bmos-second-level-border-color);
    }
    .person-list {
      border-right: 1px solid var(--bmos-second-level-border-color);
      .person-header {
        background-color: var(--bmos-table-td-color);
        line-height: 40px;
        padding-left: var(--bmos-padding-small);
      }
      .person-title {
        font-size: 16px;
      }
      .person-content {
        padding: var(--bmos-padding-small);
      }
    }
  }
</style>

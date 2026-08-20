<template>
  <div class="template-edit">
    <div class="template-edit-opeartion">
      <BreadCrumb :page="t('公式配置')" :implement="route.params.implement"></BreadCrumb>
      <div class="opeartion-container">
        <Button v-hasAuth="120020001000008" @click="toUpdate">
          {{ t('记录编辑') }}
        </Button>
        <Button @click="goBack">{{ t('返回') }}</Button>
      </div>
    </div>
    <div class="template-edit-content">
      <ContentLayout :title="t('记录项')" class="record-content" style="width: 300px" :isIcon="false">
        <BMSearchTree
          v-model:expanded-keys="EXPANDED_KEYS"
          :showSearch="false"
          :showAllAddIcon="false"
          :showAction="false"
          :blockNode="true"
          :selected-keys="SELECTED_KEYS"
          :fieldNames="{
            title: 'name',
            key: 'itemId',
          }"
          :tree-data="TREE_DATA"
          @select="TREE_SELECT"></BMSearchTree>
      </ContentLayout>
      <ContentLayout :title="t('公式')" class="formula-content" :isIcon="false">
        <div style="width: 350px; height: 100%; overflow: hidden">
          <FormulaCheck
            :key="formKey"
            :component="CURRENT_COMPONENT"
            @confirm="SAVE_COMPONENT_FORMULA"
            @delete="deleteParam"
            @add="handleAddParams"
            @before-formula-change="formulaChange"
            @cancel="cancelCheck"
            @clear="clearFormula"></FormulaCheck>
        </div>
      </ContentLayout>
      <Record
        ref="EDITOR_INSTANCE"
        style="flex: 1"
        :formulaId="'122414151'"
        :activeKeys="NODE_ACTIVES"
        @node-click="NODE_CLICK"></Record>
      <Modal
        v-model:open="STATUS.MODAL"
        :title="t('公式引用')"
        style="top: 0px"
        :centered="true"
        :destroyOnClose="true"
        :maskClosable="false"
        :width="1300"
        @ok="() => dbConfirm(MDAAL_RECORD_REF?.getCheckNode())"
        @cancel="() => handleModalCancel(MODAL_TYPE.INST)">
        <RelativeRecord
          ref="MDAAL_RECORD_REF"
          :type="CURRENT_COMPONENT.componentType"
          :currentComponent="CURRENT_COMPONENT"
          :currentFormula="currentFormula"
          :options="TREE_DATA"
          :currentSelectRecordItemKeys="SELECTED_KEYS"
          @confirm="dbConfirm"></RelativeRecord>
      </Modal>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMSearchTree } from '@bmos/components';
  import { Record } from '../../components/Record';
  import { useRouter, useRoute } from 'vue-router';
  import { Button, Modal } from 'ant-design-vue';
  import ContentLayout from '../../components/ContentLayout/index.vue';
  import BreadCrumb from '../TemplateEdit/component/Breadcrumb/index.vue';
  import { useEDITOR, useTree, useNode, ModalConfirm } from './hooks';
  import FormulaCheck from './FormulaCheck/index.vue';
  import RelativeRecord from './RelativeRecord/index.vue';
  import { useChangeStatus } from './store/useChangeStatus';
  import { storeToRefs } from 'pinia';
  import { onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import { MODAL_TYPE } from './enum';
  const route = useRoute();
  const router = useRouter();
  const node = useNode();
  const editor = useEDITOR(node);
  const store = useChangeStatus();
  const { CHANGE_STATUS } = storeToRefs(store);
  const { EDITOR_INSTANCE, NODE_CLICK, NODE_ACTIVES, cancelCheck, SAVE_COMPONENT_FORMULA, deleteParam, formulaChange } =
    editor;

  const {
    SET_INST_NODE_LIST,
    CURRENT_COMPONENT,
    currentFormula,
    handleAddParams,
    handleModalCancel,
    STATUS,
    dbConfirm,
    MDAAL_RECORD_REF,
    formKey,
  } = node;
  const { TREE_DATA, TREE_SELECT, CURRENT_NODE, EXPANDED_KEYS, SELECTED_KEYS, GET_RECORD, clearFormula } = useTree(
    editor,
    node,
  );

  onMounted(() => {
    GET_RECORD(route.params.record_id as string);
  });

  const goBack = () => {
    if (route.params.implement !== '1' && !CHANGE_STATUS.value.status) {
      router.push('/product-config/record-config');
      return;
    }
    if (route.params.implement === '1' && !CHANGE_STATUS.value.status) {
      router.push('/Implement/record-manage');
      return;
    }
    ModalConfirm(() => {
      route.params.implement !== '1'
        ? router.push('/product-config/record-config')
        : router.push('/Implement/record-manage');
    });
  };

  // 跳转至记录编辑
  const toUpdate = () => {
    if (!CHANGE_STATUS.value.status) {
      return router.push({
        name: 'TemplateEdit',
        params: {
          record_id: route.params.record_id,
          record_type: route.params.is_show,
          implement: route.params.implement,
          update: route.params.update,
          recordId: route.params.recordId,
        },
      });
    }
    ModalConfirm(() => {
      router.push({
        name: 'TemplateEdit',
        params: {
          record_id: route.params.record_id,
          record_type: route.params.is_show,
          implement: route.params.implement,
          update: route.params.update,
          recordId: route.params.recordId,
        },
      });
    });
  };

  watch(CURRENT_NODE, val => {
    if (val && val.componentList) {
      return SET_INST_NODE_LIST(val.componentList);
    }
    SET_INST_NODE_LIST([]);
  });
</script>
<style scoped lang="less">
  .template-edit {
    height: inherit;
    display: flex;
  }
  :deep(.ueditor-container) {
    position: relative;
    overflow-x: hidden;
    overflow-y: auto;
    .vue-ueditor {
      .edui-editor {
        width: 100% !important;
        .edui-editor-toolbarbox {
          // top:48px !important;
          position: sticky !important;
          top: 0 !important;
        }
      }
    }
  }
  .record-content {
    border-right: 1px solid rgba(225, 227, 229, 1);
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    :deep(.container-content) {
      flex: 1;
      overflow-y: auto;
    }
  }

  .formula-content {
    display: flex;
    flex-direction: column;
    :deep(.container-content) {
      flex: 1;
      overflow: hidden;
    }
  }
</style>

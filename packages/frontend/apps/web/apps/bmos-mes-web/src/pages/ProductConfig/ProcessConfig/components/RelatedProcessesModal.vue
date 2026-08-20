<template>
  <Modal
    v-model:open="relatedProcessesOpen"
    :title="t('关联工艺')"
    destroyOnClose
    wrap-class-name="modalSizeLarge"
    class="related-processes-modal"
    :maskClosable="false"
    @ok="handleRelatedProcessesOk">
    <Form ref="formRef" :colon="false" :model="relatedForm">
      <div
        v-for="(item, index) in relatedForm.related"
        :key="`${item?.id}${index}${item.relationProcessId ? item.relationProcessId : ''}`"
        class="related-item">
        <FormItem
          :name="['related', index, 'relationProcessId']"
          :label="t('关联工艺')"
          :placeholder="t('请选择关联工艺')">
          <TreeSelect
            v-model:value="item.relationProcessId"
            :tree-data="processTreeData"
            style="width: 260px"
            show-search
            allow-clear
            treeNodeFilterProp="showName"
            :placeholder="t('请选择关联工艺')"
            :field-names="{ label: 'showName', value: 'id' }"></TreeSelect>
        </FormItem>
        <FormItem :name="['related', index, 'materialIds']" :label="t('关联物料')" :placeholder="t('请选择关联物料')">
          <Select
            v-model:value="item.materialIds"
            mode="multiple"
            :options="materialsOptions"
            allow-clear
            :placeholder="t('请选择关联物料')"
            max-tag-count="responsive"
            :field-names="{ label: 'name', value: 'id' }"
            :filter-option="materialsFilterOption"
            style="width: 260px"></Select>
        </FormItem>
        <Tooltip>
          <template #title>{{ t('删除') }}</template>
          <DeleteOutlined
            :class="['delete-icon', index > 0 ? 'show-delete-icon' : '']"
            @click="removeRelated(item, index)" />
        </Tooltip>
      </div>
      <FormItem>
        <span class="add-icon" @click="addRelated">
          <PlusCircleOutlined />
          {{ t('新增') }}
        </span>
      </FormItem>
    </Form>
  </Modal>
</template>

<script setup lang="tsx">
  import { computed, reactive, ref, watch } from 'vue';
  import { Modal, Form, FormItem, Select, message } from 'ant-design-vue';
  import { PlusCircleOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { RelatedForm, RelatedItem } from '../types';
  import { Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import {
    getProcessRelationProcessesMaterialsReq,
    getProcessSaveRelationsReq,
    reqPlatformMaterialPageReq,
    getProcessListTreeReq,
  } from '@/services';
  import { loopSelectableNotValueTree } from '@bmos/utils';

  const emits = defineEmits(['update:relatedProcessesOpen', 'updateRelatedProcesses']);
  const props = defineProps({
    relatedProcessesOpen: {
      type: Boolean,
      default: false,
    },
    rowData: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
  });

  const relatedProcessesOpen = computed<boolean>({
    get() {
      return props.relatedProcessesOpen;
    },
    set(val) {
      emits('update:relatedProcessesOpen', val);
    },
  });

  const relatedForm = reactive<RelatedForm>({
    related: [
      {
        relationProcessId: undefined,
        materialIds: [],
      },
    ],
  });

  const removeRelated = (item: RelatedItem, index: number) => {
    relatedForm.related.splice(index, 1);
  };
  const addRelated = () => {
    relatedForm.related.push({
      id: new Date().getTime().toString(),
      relationProcessId: undefined,
      materialIds: [],
    });
  };

  const processTreeData = ref([]);
  const getProcessTreeData = async () => {
    try {
      const { data } = await getProcessListTreeReq();
      processTreeData.value = loopSelectableNotValueTree(data, 'isFlag', true);
    } catch (error) {}
  };

  const materialsOptions = ref([]);
  const materialsFilterOption = (input: string, option: any) => {
    return option.name.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  };
  const getMaterialsOptions = async () => {
    try {
      const { data } = await reqPlatformMaterialPageReq();
      materialsOptions.value = data.list;
    } catch (error) {}
  };

  const getRelatedProcesses = async () => {
    try {
      const { data } = await getProcessRelationProcessesMaterialsReq(props.rowData.id);
      relatedForm.related = data.length
        ? data
        : [
            {
              relationProcessId: undefined,
              materialIds: [],
            },
          ];
    } catch (error) {}
  };

  // 监听 open, 关闭时清空表单
  watch(
    () => relatedProcessesOpen.value,
    val => {
      if (!val) {
        relatedForm.related = [
          {
            relationProcessId: undefined,
            materialIds: [],
          },
        ];
      } else {
        getProcessTreeData();
        getMaterialsOptions();
        getRelatedProcesses();
      }
    },
  );
  const formRef = ref<any>(null);
  const handleRelatedProcessesOk = async () => {
    try {
      await formRef.value.validate();
      await getProcessSaveRelationsReq(
        props.rowData.id,
        relatedForm.related.map((item: any) => ({
          relationProcessId: item.relationProcessId,
          materialIds: item.materialIds,
        })),
      );
      relatedProcessesOpen.value = false;
      message.success(t('保存关联关系成功'));
      emits('updateRelatedProcesses');
      relatedProcessesOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>

<style lang="less">
  .related-processes-modal {
    .related-item {
      width: 98%;
      background-color: var(--bmos-background-color);
      margin-bottom: var(--bmos-margin-large);
      padding-top: var(--bmos-padding-small);
      padding-left: var(--bmos-padding-small);
      padding-right: var(--bmos-padding-small);
      display: flex;
      align-items: start;
      justify-content: space-between;
      .delete-icon {
        margin-top: 8px;
        visibility: hidden;
        font-size: 20px;
        color: var(--bmos-danger-color);
      }
      .show-delete-icon {
        visibility: visible;
      }
    }
    .add-icon {
      cursor: pointer;
    }
  }
</style>

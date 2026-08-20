<!-- 树节点操作组件 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="ok"></BMModalForm>
</template>

<script setup lang="ts">
import { createVNode, reactive, ref, watch } from 'vue';
import { t } from '@bmos/i18n';
import { BMModalForm, ModalFormInstance, FormProps, ActionListItem, NodeKey } from '@bmos/components';
import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { message, Modal } from 'ant-design-vue';
import { saveCategory, deleteCategory, updateCategory } from '@/services/index';

const props = defineProps({
  treeData: {
    type: Array<any>,
    default: () => [],
  }
})

const emit = defineEmits(['fetchTreeData']);

const treeData = ref(props.treeData);

watch(
  () => props.treeData,
  () => {
    treeData.value = [{
      ...props.treeData[0],
      id: '0',
      key: '0'
    }]
    formProps.schemas[0].componentProps.treeData = treeData.value
  }
)

const formProps = reactive<FormProps>({
  initialValues: {
    parentId: '0',
  },
  schemas: [
    {
      field: 'parentId',
      component: 'TreeSelect',
      label: t('上级分类'),
      required: true,
      componentProps: {
        disabled: true,
        treeData: treeData.value,
        fieldNames: {
          label: 'showName',
          value: 'id',
        },
      },
    },
    {
      field: 'name',
      component: 'Input',
      label: t('分类名称'),
      required: true,
    },
    {
      field: 'code',
      component: 'Input',
      label: t('分类编码'),
      required: true,
      componentProps: ({ formModel }) => {
        return {
          disabled: !!formModel.id,
        };
      },
    },
  ],
});

const title = ref<string>(t('新增分类'));

const addChildrenFn = (node: DataNode) => {
  addItem(node.id);
};

const editNodeFn = (node: DataNode) => {
  actionType.value = 'editNode';
  title.value = t('编辑分类');
  formProps.initialValues = {
    id: node.id,
    name: node.name,
    code: node.code,
    parentId: node.parentId,
  };
  open.value = true;
};

const deleteNodeFn = (node: DataNode) => {
  Modal.confirm({
    title: t('是否删除分类信息'),
    icon: createVNode(ExclamationCircleOutlined),
    closable: true,
    content: t('分类信息删除后无法恢复, 是否删除?'),
    okText: t('确定'),
    cancelText: t('取消'),
    onOk: async () => {
      try {
        await deleteCategory(node.id);
        message.success('删除成功');
        // fetchTreeData();
        emit('fetchTreeData');
      } catch (error) {
        message.error(error.message);
      }
    },
  });
};

const actionType = ref<string>('addChildren');

const actionMap = new Map([
  ['addChildren', addChildrenFn],
  ['editNode', editNodeFn],
  ['deleteNode', deleteNodeFn],
]);
const action = (action: ActionListItem, node: DataNode) => {
  if(action.action === 'ADD') {
    addItem('0');
    return
  }
  const fn = actionMap.get(action.action);
  fn && fn(node);
};

const addItem = (key: NodeKey) => {
  title.value = t('新增分类');
  actionType.value = 'addChildren';
  formProps.initialValues = {
    parentId: key,
  };
  open.value = true;
};

const modalFormRef = ref<ModalFormInstance>();
const open = ref<boolean | undefined>(false);

const request = async (formModal: any) => {
  try {
    const params = {
      ...formModal
    }
    if (actionType.value === 'editNode') {
      return await updateCategory(params);
    } else {
      return await saveCategory(params);
    }
  } catch (error) {
    return Promise.reject(error);
  }
  
}

// 提交
const ok = async () => {
  try {
    await modalFormRef.value?.submit(request);
    message.success(actionType.value === 'editNode' ? t('编辑成功') : t('新增成功'));
    emit('fetchTreeData');
    cancel()
  } catch(error) {
    error.message && message.error(error.message);
  }
}

const cancel = () => {
  open.value = false;
}


defineExpose({
  action,
  cancel,
})
</script>

<style lang="less" scoped>

</style>
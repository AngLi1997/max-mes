<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('分配人员')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @cancelModal="cancel"
    @okModal="ok">
    <div class="assignPersonnel">
      <InputSearch
        v-model:value="searchValue"
        class="bmos-search-tree-input"
        :placeholder="t('请输入')"
        @search="handleSearch"
        @input="handleSearch" />
      <div v-if="treeProps.treeData.length > 0" class="treeClass">
        <Tree
          v-model:checkedKeys="checkedKeys"
          v-model:expandedKeys="expandedKeys"
          :tree-data="treeProps.treeData"
          :field-names="{
            title: 'name',
            key: 'userId',
          }"
          checkable
          @check="check">
          <template #title="{ name }">
            <span v-if="name.indexOf(searchValue) > -1">
              {{ name.substr(0, name.indexOf(searchValue)) }}
              <span style="color: #f50">{{ searchValue }}</span>
              {{ name.substr(name.indexOf(searchValue) + searchValue.length) }}
            </span>
            <span v-else>{{ name }}</span>
          </template>
        </Tree>
      </div>
      <Empty v-else></Empty>
    </div>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { t } from '@bmos/i18n';
  import { reactive, ref } from 'vue';
  import { relateUserSave, assignPerson } from '../../../../api/Permissions/departmentManagement';
  import { message, Tree } from 'ant-design-vue';
  import Empty from '../../../../components/Empty/index.vue';

  const props = defineProps({
    deptId: {
      type: [Number, String],
      default: '',
    },
  });
  // 树搜索输入框
  const searchValue = ref('');
  const checkedKeys = ref<string[]>(['0-0-0']);
  const expandedKeys = ref<string[]>(['0']);
  const emit = defineEmits(['updata', 'upDepartmentdata', 'getTreeDataAction']);
  const cancel = () => {
    checkedKeys.value = [];
    searchValue.value = '';
  };
  const checkeds = ref<any>([]);
  const handleSearch = async () => {
    const data = { deptId: props.deptId, name: searchValue.value };
    const res = await assignPerson(data);
    if (!searchValue.value) {
      // 手动清空时也把'全部'出来
      treeProps.treeData = [
        {
          name: t('全部'),
          userId: '0',
          children: res.data,
        },
      ];
    } else {
      treeProps.treeData = res.data;
    }
  };

  // 分配人员弹框确定按钮
  const ok = async () => {
    try {
      const data = checkeds.value.filter((item: any) => item?.userId != 0); //当选全部的时候不传'全部'节点的id
      await relateUserSave(data);
      message.success(t('操作成功'));
      // 刷新表格
      emit('upDepartmentdata');
      open.value = false;
      checkedKeys.value = [];
      checkeds.value = [];
      searchValue.value = '';
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean | undefined>(false);
  const openModal = () => {
    open.value = true;
  };
  // 树方法
  const treeProps = reactive<any>({
    addChildrenNeedCode: true,
    treeData: [],
  });

  // 点复选框
  const check = (selectedKeys: Key[]) => {
    let checked = selectedKeys.map(item => {
      return {
        deptId: props.deptId,
        userId: item,
      };
    });
    checkeds.value = checked;
  };

  defineExpose({ openModal, treeProps });
</script>
<style lang="less" scoped>
  .assignPersonnel .bmos-search-tree {
    width: 100%;
  }
  :deep(.plat-tree) {
    width: 100%;
  }
  .treeClass {
    height: 400px;
    overflow-y: scroll;
  }
</style>

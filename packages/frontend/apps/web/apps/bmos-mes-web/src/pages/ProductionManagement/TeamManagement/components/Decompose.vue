<template>
  <div class="header">
    <Breadcrumb>
      <BreadcrumbItem>{{ t('班组管理') }}</BreadcrumbItem>
      <BreadcrumbItem v-if="showType == 'create'">
        {{ t('新建班组') }}
      </BreadcrumbItem>
      <BreadcrumbItem v-else>{{ t('编辑') }}</BreadcrumbItem>
    </Breadcrumb>
    <div>
      <Button style="margin-right: 10px" @click="goBack">
        {{ t('返回') }}
      </Button>
      <Button type="primary" @click="handleClickSave">
        {{ t('保存') }}
      </Button>
    </div>
  </div>
  <div class="form-box">
    <BMForm ref="myFormRef" v-bind="formProps"></BMForm>
  </div>
  <div class="main">
    <div class="tree-item">
      <div class="tree-item-header">
        {{ t('人员列表') }}
        <span class="tree-item-num">{{ Object.keys(userList).length }}</span>
      </div>
      <div class="tree-item-box">
        <BMSearchTree
          v-model:checkedKeys="checkedKeys"
          :tree-data="treeData"
          show-icon
          :fieldNames="{ title: 'name' }"
          :expandedKeys="expandedKeys"
          checkable
          @expand="onExpand">
          <template #icon="data">
            <BMIcons v-if="data.deptFlag" icon="Bag" style="width: 16px" />
            <TeamOutlined v-else style="width: 16px" />
          </template>
        </BMSearchTree>
      </div>
    </div>
    <div class="tree-item">
      <div class="tree-item-header tree-people-header">
        <div>
          {{ t('已选择') }}
          <span class="tree-item-num">{{ checkedKeysList.length }}</span>
        </div>
        <Button type="link" @click="clearAllPeople">{{ t('清除') }}</Button>
      </div>
      <div class="tree-item-box">
        <div v-for="item in checkedKeysList" :key="item" class="tree-user-list-item">
          <div>{{ userList[item]?.name }}</div>
          <CloseCircleOutlined @click="clearPeopleById(item)" />
        </div>
      </div>
    </div>
  </div>
  <PermissionDeptModal
    v-model:permissionOpen="permissionDeptModalOpen"
    :resourceId="rowData?.id"
    :saveImmediate="false"
    :isAdd="true"
    :type="false"
    @ok="savePermissionDept" />
</template>
<script lang="tsx" setup>
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMForm, FormProps, BMSearchTree } from '@bmos/components';
  import { TeamOutlined, CloseCircleOutlined } from '@ant-design/icons-vue';
  import { BMIcons } from '@bmos/icons';
  import { platformQueryDeptUserTree, platformQueryDeptUserUnassigned, planTeamSave, planTeamUpdata } from '@/services';
  import PermissionDeptModal from '@/components/PermissionDept/index.vue';

  const emit = defineEmits(['close']);
  const props = defineProps({
    showType: {
      type: String,
      default: '',
    },
    rowData: {
      type: Object,
      default: () => ({}),
    },
  });
  const myFormRef = ref();
  // 表单配置
  const formProps: Ref<FormProps> = ref({
    labelWidth: 110,
    baseColProps: {
      span: 6,
    },
    autoAdvancedLine: 10,
    alwaysShowLines: 6,
    actionColOptions: {
      span: 6,
      style: {
        textAlign: 'center',
      },
    },
    showAdvancedButton: true,
    showActionButtonGroup: false,
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('班组名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        label: t('班组编码'),
        required: true,
      },
      {
        field: 'description',
        component: 'Input',
        label: t('班组描述'),
      },
    ],
  });
  // 树配置
  const treeData = ref<any>([]);
  // 暂存人员
  const userList = ref<any>({});
  // 已选中节点
  const checkedKeys = ref<any>([]);
  // 过滤后的人员节点
  const checkedKeysList = ref<any>([]);
  // 清除所有人员
  const clearAllPeople = () => {
    checkedKeys.value = [];
    expandedKeys.value = [];
  };
  const expandedKeys = ref<any>([]);
  const isClearFlag = ref(false);
  // 清除当前人员
  const clearPeopleById = (id: string) => {
    isClearFlag.value = true;
    checkedKeys.value = checkedKeys.value.filter((item: string) => {
      return item.split('-')?.[1] != id;
    });
    checkedKeysList.value = checkedKeysList.value.filter((item: string) => {
      return item != id;
    });
  };
  const treeOldData = ref<any>({});
  const isRest = ref(false);
  const isClosedList = ref<any>([]);
  onMounted(async () => {
    if (props.showType == 'update') {
      // 编辑 回显数据
      checkedKeysList.value = props.rowData.peoples;
      myFormRef.value.setFieldsValue(props.rowData);
    }
    // 获取树数据
    let { data } = await platformQueryDeptUserTree();
    const { data: unassignedList } = await platformQueryDeptUserUnassigned();
    // 有未分配人员
    let newPart = {
      id: '999',
      name: t('未分配'),
      deptFlag: true,
      parentId: '0',
      children: [] as any,
    };
    unassignedList.list.map((item: any) => {
      newPart.children.push({
        ...item,
        id: item.userId,
        parentId: '999',
      });
    });
    data.unshift(newPart);
    treeData.value = data.map((item: any) => {
      item.children = getChildren(item.children);
      item.key = `${item.parentId}-${item.id}`;
      if (props.showType == 'update') {
        // 编辑 回显节点点击
        checkedKeysList.value = props.rowData.peoples;
      }
      if (!item.deptFlag && userList.value[item.id]) {
        userList.value.push(item);
      }
      if (!item.deptFlag) {
        if (!treeOldData.value[item.id]) {
          treeOldData.value[item.id] = [`${item.parentId}-${item.id}`];
        } else {
          treeOldData.value[item.id].push(`${item.parentId}-${item.id}`);
        }
      }
      return item;
    });
  });

  watch(checkedKeys, () => {
    if (!isClearFlag.value) {
      // 判断是不是右侧删除,避免重新排序
      checkedKeysList.value = [];
    }
    let newCheckedKeys = [] as any;
    if (checkedKeys.value.length < isClosedList.value.length) {
      // 删除了人员
      const removeIdList = isClosedList.value.filter((removeId: any) => {
        if (checkedKeys.value.indexOf(removeId) < 0) {
          return removeId.split('-')?.[1];
        }
      });
      removeIdList.map((removeId: any) => {
        isClosedList.value = isClosedList.value.filter((item: any) => {
          return item.split('-')?.[1] != removeId.split('-')?.[1];
        });
      });
      checkedKeys.value = [...isClosedList.value];
      return;
    }
    checkedKeys.value.map((item: any) => {
      const id = item.split('-')?.[1];
      // 回显右侧已勾选
      if (!isClearFlag.value) {
        if (checkedKeysList.value.indexOf(id) < 0 && userList.value[id]) {
          checkedKeysList.value.push(id);
        }
      }
      // 同步勾选其他相同人员
      treeOldData.value[id]?.map((item: any) => {
        if (newCheckedKeys.indexOf(item) < 0) {
          newCheckedKeys.push(item);
        }
      });
    });
    if (isRest.value) {
      isRest.value = false;
      return;
    }
    checkedKeys.value = [...newCheckedKeys];
    nextTick(() => {
      isClearFlag.value = false;
    });
    isClosedList.value = [...checkedKeys.value];
    isRest.value = true;
  });
  //  点击返回按钮
  const goBack = () => {
    emit('close');
  };
  // 保存班组
  const saveTeam = async (deptIds?: any[]) => {
    if (checkedKeysList.value.length == 0) {
      message.error(t('请选择班组人员'));
      return;
    }
    try {
      await myFormRef.value?.validate();
      if (props.showType != 'update') {
        await planTeamSave({
          description: '',
          ...myFormRef.value.formModel,
          people: checkedKeysList.value,
          deptIds,
        });
      } else {
        await planTeamUpdata({
          id: props.rowData.id,
          ...myFormRef.value.formModel,
          people: checkedKeysList.value,
        });
      }
      message.success(props.showType == 'create' ? t('新增班组人员成功') : t('编辑班组人员成功'));
      goBack();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 权限部门
  const permissionDeptModalOpen = ref<boolean>(false);
  const handleClickSave = () => {
    if (props.showType != 'update') {
      permissionDeptModalOpen.value = true;
    } else {
      saveTeam();
    }
  };
  const savePermissionDept = (deptIds: any[]) => {
    saveTeam(deptIds);
  };
  // 调整数据
  const getChildren = (arr: Array<any>) => {
    if (!arr) {
      return null;
    }
    let res = arr.map((item: any) => {
      item.children = getChildren(item.children);
      item.key = `${item.parentId}-${item.id}`;
      if (item.name.indexOf('-') < 0 && !item.children) {
        item.name = `${item.name}-${item.loginName}`;
      }
      if (!item.deptFlag && !userList.value[item.id]) {
        userList.value[item.id] = item;
      }
      if (props.showType == 'update' && checkedKeysList.value.indexOf(item.id) >= 0) {
        isClosedList.value.push(`${item.parentId}-${item.id}`);
        checkedKeys.value.push(`${item.parentId}-${item.id}`);
      }
      if (!item.deptFlag) {
        if (!treeOldData.value[item.id]) {
          treeOldData.value[item.id] = [`${item.parentId}-${item.id}`];
        } else {
          treeOldData.value[item.id].push(`${item.parentId}-${item.id}`);
        }
      }
      return item;
    });
    return res;
  };
  const onExpand = (keys: any) => {
    expandedKeys.value = keys;
  };
</script>
<style lang="less" scoped>
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px;
    height: 30px;
    margin-bottom: 10px;
  }
  .form-box {
    padding-top: 15px;
    background-color: white;
    margin-bottom: 5px;
  }
  .main {
    height: calc(100% - 110px);
    background-color: white;
    padding: 15px;
    display: flex;
    .tree-item {
      width: 50%;
      height: 100%;
      box-sizing: border-box;
      border: 1px solid #e1e3e5;
      position: relative;
      &:nth-of-type(1) {
        border-right: 0px;
      }
      .tree-item-header {
        height: 40px;
        line-height: 40px;
        padding: 0 20px;
        background-color: #fafafa;
        .tree-item-num {
          margin-left: 20px;
          color: #909398;
        }
      }
      .tree-item-box {
        height: calc(100% - 40px);
        overflow-y: auto;
        box-sizing: border-box;
        padding: 10px 20px;
        .tree-user-list-item {
          margin-top: 15px;
          display: flex;
          align-items: center;
          justify-content: space-between;
        }
      }
      .tree-people-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
      }
    }
  }
</style>

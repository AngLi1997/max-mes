<template>
  <div class="container">
    <div class="searchTree">
      <BMSearchTree
        ref="searchTreeRef"
        v-model:expandedKeys="expandedKeys"
        :selectedKeys="selectedKeys"
        :showAction="false"
        v-bind="treeProps"
        :autoExpandParent="true"
        :showAllAddIcon="false"
        :defaultExpandParent="true"
        @select="select"
        @addItem="addItem">
        <template #action="data">
          <BMIcon
            v-if="data.key === 'all' && (props.inside === '1' ? false : hasPermission('100030002000001'))"
            type="Add"
            class="bmos-tree-label-icon-add"
            @click.stop="() => action('addChildren', data)"></BMIcon>
          <Dropdown
            v-if="
              !(data.key === 'all' && (props.inside === '1' ? false : hasPermission('100030002000001'))) &&
              (props.inside === '1' ? data.clickFlag : true)
            "
            :trigger="['hover']">
            <BMIcon type="More" class="bmos-tree-label-icon-more" @click.stop></BMIcon>
            <template #overlay>
              <Menu>
                <template v-for="item in treeProps.actionList">
                  <template v-if="getActionShow(item.ifShow, data)">
                    <menu-item v-if="item.render" :key="'actionRender' + item.title">
                      <component :is="item.render?.(data)"></component>
                    </menu-item>
                    <menu-item v-else :key="'action' + item.title">
                      <a href="#" @click="() => handleClickAction(item, data)">
                        {{ item.title }}
                      </a>
                    </menu-item>
                  </template>
                </template>
              </Menu>
            </template>
          </Dropdown>
        </template>
      </BMSearchTree>
    </div>
    <!-- 右边部门信息及人员信息-->
    <div class="rightContent">
      <DepartmentTableData
        ref="departmentTableDataRef"
        :departmentInfo="departmentInfo"
        :childrenLength="childrenLength"
        :upDepartmentInfo="upDepartmentInfo"
        :upUpDepartmentInfo="upUpDepartmentInfo"
        :parentCode="parentCodeTable"
        :deptId="deptId"
        :hasParent="hasParent"
        :rootNode="rootNode"
        :inside="inside"
        :firstDeptId="firstDeptId"
        :showTable="showTable"
        @assignPersonnel="assignPersonnel"
        @showButton="showButton"
        @getTreeData="getTreeData"
        @getTreeData2="getTreeData2"></DepartmentTableData>
      <!-- 分配人员弹框 -->
      <AssignPersonnel
        ref="assignPersonnelRef"
        :deptId="deptId"
        @updata="updata"
        @getTreeDataAction="getTreeDataAction"
        @upDepartmentdata="upDepartmentdata"></AssignPersonnel>
      <!-- 左侧树action新增子分类、编辑分类弹框弹框 -->
      <BMModalForm
        ref="modalFormRef"
        v-model:open="open"
        :title="title"
        :formProps="formProps2"
        :cancelText="t('取消')"
        :okText="t('确定')"
        wrapClassName="modalSizeMedium"
        @cancelModal="cancel"
        @okModal="ok"></BMModalForm>
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import {
    BMSearchTree,
    SearchTreeProps,
    SearchTreeInstance,
    ModalFormInstance,
    BMModalForm,
    ActionListItemCustomRenderParams,
    BMIcon,
  } from '@bmos/components';
  import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { t } from '@bmos/i18n';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { reactive, ref, onMounted, createVNode } from 'vue';
  import DepartmentTableData from './departmentTableData/tableData.vue';
  import AssignPersonnel from './assignPersonnel/assignPersonnel.vue';
  import {
    departmentTreeAll, //部门管理树接口
    reqDeptIntervalTree, //部门内部管理树接口
    deleteDepartment,
    editDepartment,
    addDepartment,
    validateDept,
  } from '../../../api/Permissions/departmentManagement';
  import { getParameter } from '../../../api/Permissions/menuPermissions';

  import { Modal, message, MenuItem, Dropdown, Menu } from 'ant-design-vue';
  import { usePermissionStore } from '@/stores/permission';
  const getActionShow = (ifShow: any, data: any) => {
    return searchTreeRef.value?.getActionShow(ifShow, data);
  };
  const handleClickAction = (item: any, data: any) => {
    action(item.action, data);
  };
  const { hasPermission } = usePermissionStore();
  const props = withDefaults(defineProps<{ inside: string }>(), { inside: '2' }); //'1'为部门内部管理,'2'为部门管理
  const rootNode = ref(t('佰墨思')); // 参数配置配的根节点名称
  const deptId = ref(); //部门保存id
  const hasParent = ref(); //该部门是否有parent
  const firstDeptId = ref(); //初始化是展示佰墨思下面的第一级（包括左边表格和部门信息三列）
  const upDepartmentInfo = ref(); //右上角按钮新增该部门
  const upUpDepartmentInfo = ref(); //右上角按钮编辑该部门
  const treeUpDepartmentInfo = ref(); //树下拉新增该部门
  const treeUpUpDepartmentInfo = ref(); //树下拉编辑该部门
  const treeCanDelete = ref(); //判断左边树中是否可删除该部门
  const treeUpInfo = ref(); //存从树而来的上级部门
  const searchTreeRef = ref<SearchTreeInstance>();
  const expandedKeys = ref<string[]>([]);
  const selectedKeys = ref<any[]>(['all']);
  const assignPersonnelRef = ref<any>(null);
  const departmentTableDataRef = ref();
  const parentCode = ref();
  const parentCodeTable = ref();
  const treeProps: SearchTreeProps = reactive({
    addChildrenNeedCode: true,
    showAddChildren: false,
    showDeleteNode: false,
    fieldNames: { title: 'name', key: 'id' },
    treeData: [],
    actionList: [
      {
        title: t('新增部门'),
        action: 'addChildren',
        ifShow: (node: ActionListItemCustomRenderParams) => {
          return props.inside === '1'
            ? node.nodeLevelInTree < 7 && hasPermission('100030006000001')
            : node.nodeLevelInTree < 7 && hasPermission('100030002000001');
        },
      },
      {
        title: t('编辑部门'),
        action: 'editNode',
        ifShow: () => {
          return props.inside === '1' ? hasPermission('100030006000002') : hasPermission('100030002000002');
        },
      },
      {
        title: t('删除部门'),
        action: 'deleteNode',
        ifShow: () => {
          return props.inside === '1' ? hasPermission('100030006000003') : hasPermission('100030002000003');
        },
      },
    ],
  });
  const departmentInfo = ref({ up: '', name: '', remarks: '' });
  const childrenLength = ref();
  // action所需
  const open = ref<boolean | undefined>(false);
  const title = ref<string>('');
  const modalFormRef = ref<ModalFormInstance>();
  const treeDeptId = ref(); //左边树action时所存部门id
  // 部门名称唯一验证
  const validatorUnique = async (_rule: any, value: string) => {
    if (!value) {
      return Promise.reject(t('请输入部门名称'));
    }
    if (!/^.{0,255}$/.test(value)) {
      return Promise.reject(t('部门名称长度不能超过255'));
    } else if (title.value == t('新增部门') || title.value == t('编辑部门')) {
      const res = await validateDept({
        deptName: value,
        id: title.value == t('编辑部门') ? treeDeptId.value : undefined,
      });
      if (res.data == true) {
        return Promise.reject(t('部门名称不能重复!'));
      } else {
        return Promise.resolve();
      }
    } else {
      return Promise.resolve();
    }
  };
  // 新增编辑弹窗formProps
  const formProps2 = reactive<any>({
    initialValues: {},
    labelCol: { span: 5 },
    wrapperCol: { span: 18 },
    schemas: [
      {
        field: 'parentId',
        component: 'Select',
        label: t('上级部门'),
        required: true,
        componentProps: {
          options: [
            {
              label: t('生产部'),
              value: '0',
            },
            {
              label: t('组织部'),
              value: '1',
            },
          ],
          disabled: true,
        },
      },
      {
        field: 'deptName',
        component: 'Input',
        label: t('部门名称'),
        rules: [{ required: true, validator: validatorUnique, trigger: 'blur' }],
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        required: false,
        componentProps: {
          maxlength: 255,
        },
      },
    ],
  })!;

  // 弹窗确定后更新表格(不带参数)
  const updata = () => {
    departmentTableDataRef.value.updata();
  };
  // 弹窗确定后更新表格(带参数)
  const upDepartmentdata = () => {
    departmentTableDataRef.value.upDepartmentdata();
  };
  // 控制右边那些按钮展示
  const showButton = () => {
    hasParent.value = false;
    upDepartmentInfo.value = [];
    parentCodeTable.value = '0';
  };

  // 点整条数据节点
  const select = async (
    selected_Keys: Key[],
    info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
      selectedNodes: DataNode[];
      nativeEvent: MouseEvent;
    },
  ) => {
    if (selected_Keys.length === 0) return;
    if (selectedKeys.value[0] === selected_Keys[0]) {
      return;
    }
    selectedKeys.value = selected_Keys;
    deptId.value = info.node.id === 'all' ? null : info.node.id; //每次点击更新部门id
    hasParent.value = info.node.parent ? true : false; // 点第一级部门时候右边的新增和编辑隐藏
    parentCodeTable.value = info.node.code;
    let upDepartment = info.node.parent?.node.name || '-';
    departmentInfo.value = {
      up: upDepartment,
      name: info.node.name,
      remarks: info.node.remark,
    };
    //新增该部门的下级部门
    upDepartmentInfo.value = [
      {
        label: info.node.name,
        value: info.node.id === 'all' ? '0' : info.node.id,
      },
    ];
    // 编辑该部门
    upUpDepartmentInfo.value = [
      {
        label: info.node.parentName ? info.node.parentName : rootNode.value,
        value: info.node.parentId,
      },
    ];
    //编辑该部门
    childrenLength.value = info.node.children?.length;
    let data = {
      deptId: deptId.value === 'all' ? null : deptId.value,
    };
    departmentTableDataRef.value.tableInstance.fetchData(data);
  };
  // 新增子分类、编辑分类、删除分类  flag字段:true代表有子部门
  const action = (action: string, node: any) => {
    // 新增该部门的下级部门
    treeUpDepartmentInfo.value = [{ label: node.name, value: node.id }];
    // parentCode
    parentCode.value = node.code;
    // 编辑该部门
    treeUpUpDepartmentInfo.value = [
      {
        label: node.parentName ? node.parentName : rootNode.value,
        value: node.parentId,
      },
    ];
    // flag:true代表有子部门,false代表没有子部门
    treeCanDelete.value = !node.flag;
    treeUpInfo.value = node.parentName;
    treeDeptId.value = node.id;
    const departmentInfo = {
      deptName: node.name,
      remark: node.remark,
    };
    if (action === 'addChildren') {
      open.value = true;
      title.value = t('新增部门');
      formProps2.schemas[0].componentProps.options = treeUpDepartmentInfo.value;
      formProps2.initialValues = {
        parentId: treeUpDepartmentInfo.value[0].value,
        deptName: '',
        remark: '',
      };
    }

    if (action === 'editNode') {
      open.value = true;
      title.value = t('编辑部门');
      formProps2.schemas[0].componentProps.options = treeUpUpDepartmentInfo.value;
      formProps2.initialValues = {
        parentId: treeUpUpDepartmentInfo.value[0].value,
        deptName: departmentInfo.deptName,
        remark: departmentInfo.remark,
      };
    }
    if (action === 'deleteNode') {
      Modal.confirm({
        title: t('是否删除部门信息'),
        icon: createVNode(ExclamationCircleOutlined),
        closable: true,
        content: t('部门信息删除后无法恢复，是否删除?'),
        okText: t('确认'),
        cancelText: t('取消'),
        onOk() {
          handleClickDelete();
        },
      });
    }
  };

  // action删除部门确定
  const handleClickDelete = async () => {
    const data = { id: treeDeptId.value };
    try {
      const res: any = await deleteDepartment(data);
      if (res.code === 0) {
        message.success(t('删除成功'));
        if (deptId.value == treeDeptId.value) {
          // 删除同一时候,需要回到初始页面样子
          if (props.inside === '2') {
            hasParent.value = false;
            departmentTableDataRef.value.updata();
            upDepartmentInfo.value = [];
            parentCodeTable.value = '0';
            getTreeData();
          }
          if (props.inside === '1') {
            getTreeData2();
          }
        } else {
          getTreeDataAction();
        }
        return;
      }
      Modal.confirm({
        title: t('无法删除部门信息'),
        icon: createVNode(ExclamationCircleOutlined),
        closable: true,
        content: t(res.message),
        okText: t('确认'),
        cancelText: t('取消'),
        onOk() {},
      });
    } catch (error) {
      Modal.confirm({
        title: t('无法删除部门信息'),
        icon: createVNode(ExclamationCircleOutlined),
        closable: true,
        content: t('该部门下还有用户或子部门信息'),
        okText: t('确认'),
        cancelText: t('取消'),
        onOk() {},
      });
    }
  };

  // 打开分配人员弹窗
  const assignPersonnel = (treeData: any) => {
    assignPersonnelRef.value.openModal();
    assignPersonnelRef.value.treeProps.treeData = treeData;
  };
  // 循环树形结构数据 data, clickFlag这个字段为false的时候部门就不能点击
  const loopTree = (data: any) => {
    return data.map((item: any) => {
      if (item.clickFlag === false) {
        item.disabled = true;
      } else {
        item.disabled = false;
      }
      if (item.children) {
        loopTree(item.children);
      }
      return item;
    });
  };

  // 找第一个clickFlag为true的节点
  const findFirstNode = (treeData: any, flag: any) => {
    for (let i = 0; i < treeData.length; i++) {
      if (treeData[i].clickFlag === flag) {
        return treeData[i];
      }
      if (treeData[i].children && treeData[i].children.length > 0) {
        const foundNode: any = findFirstNode(treeData[i].children, flag);
        if (foundNode) {
          return foundNode;
        }
      }
    }
    return null;
  };

  // 初始化展示树
  const getTreeData = async (infoData: any) => {
    if (!infoData) {
      deptId.value = '';
    }
    departmentInfo.value = infoData || {}; //部门信息展示
    let data = { name: '' };
    const res = props.inside === '1' ? await reqDeptIntervalTree(data) : await departmentTreeAll(data);
    treeProps.treeData = [
      {
        name: rootNode.value,
        key: 'all',
        disabled: true,
        children: loopTree(res.data),
      },
    ];
    treeProps.treeData[0].id = 'all';
    expandedKeys.value = [treeProps.treeData[0]?.id]; //默认展开第一级树
  };
  // 部门内部管理删除当前选中时候的节点
  const getTreeData2 = async () => {
    const res = await reqDeptIntervalTree({ name: '' });
    treeProps.treeData = [
      {
        name: rootNode.value,
        key: 'all',
        id: 'all',
        disabled: true,
        children: loopTree(res.data),
      },
    ];
    const temp = findFirstNode(treeProps.treeData[0]?.children, true);
    hasParent.value = temp ? true : false;
    selectedKeys.value = [temp?.id];
    expandedKeys.value = [temp?.id];
    departmentInfo.value = { up: temp?.parentName || '-', name: temp?.name, remarks: temp?.remarks };

    upDepartmentInfo.value = [
      {
        label: temp?.name,
        value: temp?.id === 'all' ? '0' : temp?.id,
      },
    ];
    // 编辑该部门
    upUpDepartmentInfo.value = [
      {
        label: temp?.parentName ? temp?.parentName : rootNode.value,
        value: temp?.parentId,
      },
    ];
    childrenLength.value = temp?.children?.length;

    deptId.value = temp?.id;
    parentCodeTable.value = temp?.code;

    let data = {
      deptId: deptId.value,
      hiddenTable: treeProps.treeData[0]?.children.length === 0 ? true : false,
    };
    showTable.value = treeProps.treeData[0]?.children.length === 0 ? '0' : '1';
    departmentTableDataRef.value.tableInstance.fetchData(data);
  };

  // 左侧action渲染树
  const getTreeDataAction = async () => {
    let data = { name: '' };
    const res = props.inside === '1' ? await reqDeptIntervalTree(data) : await departmentTreeAll(data);
    treeProps.treeData = [
      {
        name: rootNode.value,
        key: 'all',
        disabled: true,
        children: loopTree(res.data),
      },
    ];
    treeProps.treeData[0].id = 'all';
    expandedKeys.value = [treeProps.treeData[0]?.id]; //默认展开第一级树
  };
  // 重置新增编辑弹框
  const resetting = () => {
    formProps2.initialValues = {};
  };
  // 取消
  const cancel = () => {
    modalFormRef.value?.resetForm();
    resetting();
  };
  // 确定
  const ok = async () => {
    if (title.value == t('新增部门')) {
      try {
        const data = await modalFormRef.value?.validate();
        await addDepartment({
          ...data,
          parentCode: parentCode.value || '0',
          parentId: data?.parentId === 'all' ? '0' : data?.parentId,
        });
        message.success(t('新增成功'));
        // 重新渲染树和部门信息
        open.value = false;
        if (deptId.value == treeDeptId.value) {
          getTreeDataAction();
        } else {
          console.log('不同一');
          getTreeDataAction();
        }
      } catch (error) {
        console.log(error);
      }
    } else if (title.value == t('编辑部门')) {
      try {
        let data = await modalFormRef.value?.validate();
        data = { ...data, id: treeDeptId.value };
        await editDepartment(data);
        open.value = false;
        const infoData = {
          up: treeUpInfo.value,
          name: data.deptName,
          remarks: data.remark,
        };
        // 重新渲染树和部门信息
        message.success(t('编辑成功'));
        if (deptId.value == treeDeptId.value) {
          getTreeData(infoData);
        } else {
          console.log('不同一');
          getTreeDataAction();
        }
      } catch (error: any) {
        error.message && message.error(error.message);
      }
    }
  };
  // '+'号新增
  const addItem = () => {
    parentCode.value = '0';
    open.value = true;
    title.value = t('新增部门');
    formProps2.initialValues = {};
    let data = [{ label: rootNode.value, value: '0' }];
    formProps2.schemas[0].componentProps.options = data;
    formProps2.initialValues.parentId = data[0].value;
  };

  // 获取部门根节点(来源参数配置)
  const getRootNode = async () => {
    try {
      const res: any = await getParameter('platform.sys.client-name');
      rootNode.value = res.data.value;
    } catch (error) {
      console.log(error);
    }
  };
  const showTable = ref<any>(0);
  onMounted(async () => {
    await getRootNode();
    await getTreeData();

    if (props.inside === '1') {
      const temp = findFirstNode(treeProps.treeData[0]?.children, true);
      hasParent.value = temp ? true : false;
      selectedKeys.value = [temp?.id];
      expandedKeys.value = [temp?.id];
      departmentInfo.value = { up: temp?.parentName || '-', name: temp?.name, remarks: temp?.remarks };

      upDepartmentInfo.value = [
        {
          label: temp?.name,
          value: temp?.id === 'all' ? '0' : temp?.id,
        },
      ];
      // 编辑该部门
      upUpDepartmentInfo.value = [
        {
          label: temp?.parentName ? temp?.parentName : rootNode.value,
          value: temp?.parentId,
        },
      ];
      childrenLength.value = temp?.children?.length;

      deptId.value = temp?.id;
      parentCodeTable.value = temp?.code;

      let data = {
        deptId: deptId.value,
        hiddenTable: treeProps.treeData[0]?.children.length === 0 ? true : false,
      };
      departmentTableDataRef.value.tableInstance.fetchData(data);
      showTable.value = treeProps.treeData[0]?.children.length === 0 ? '0' : '1';
    }
  });
</script>
<style scoped lang="less">
  .container {
    display: flex;
    background-color: #fff;
    height: 100%;
  }
  .searchTree {
    width: 265px;
  }
  .bmos-search-tree {
    border-right: 1px solid #e8e8e8;
  }

  .plat-layout {
    ::-webkit-scrollbar {
      display: none;
    }
  }
  .rightContent {
    flex: 1;
    width: calc(100% - 265px);
  }
</style>

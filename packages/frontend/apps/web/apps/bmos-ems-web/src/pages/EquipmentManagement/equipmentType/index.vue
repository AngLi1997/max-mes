<!-- 设备类型 -->
<template>
  <div class="container">
    <div class="searchTree">
      <BMSearchTree
        ref="searchTreeRef"
        v-model:expandedKeys="expandedKeys"
        :selectedKeys="selectedKeys"
        :treeData="treeData"
        :fieldNames="{
          title: 'showName',
          key: 'id',
        }"
        :showAllAddIcon="hasPermission('160010001000001')"
        :action-list="treeActionList"
        @select="select"
        @action="handleTreeAction"
        @addItem="allAddItem"></BMSearchTree>
    </div>
    <!-- 描述列表 -->
    <div v-if="selectedKeys[0] !== 'all'">
      <EquipmentInfo :rowData="rowData"></EquipmentInfo>
    </div>
    <Empty v-else :emptyName="t('请选择具体分类')" />
  </div>
  <!-- 树新增编辑弹框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="treeOpen"
    :title="treeTitle"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="okModal">
    <template #selectA>
      <div class="infoDetails">
        <div v-for="(item, index) in requiredList" :key="index">
          <div>{{ item?.showName }}</div>
          <div>
            <FormItemRest>
              <RadioGroup v-model:value="item.required">
                <Radio :value="false">{{ t('非必填') }}</Radio>
                <Radio :value="true">{{ t('必填') }}</Radio>
              </RadioGroup>
            </FormItemRest>
          </div>
        </div>
      </div>
    </template>
    <template #selectB>
      <div class="useTemplate">
        <div>{{ t('使用日志模版') }}</div>
        <div @click="addTemplate">{{ t('添加') }}</div>
      </div>
      <Form ref="formRef" :colon="false" :model="TemplateForm" labelAlign="left" layout="vertical">
        <div v-for="(item, index) in TemplateForm.useTemplateList" :key="index" class="template-item">
          <div style="width: 90%">
            <FormItem
              :name="['useTemplateList', index, 'operateName']"
              :label="t('操作名称')"
              :required="true"
              class="duration">
              <Input v-model:value="item.operateName"></Input>
            </FormItem>
            <FormItem
              :name="['useTemplateList', index, 'template']"
              :label="t('模板内容')"
              :required="true"
              class="duration">
              <Textarea v-model:value="item.template" :maxlength="1000"></Textarea>
            </FormItem>
          </div>
          <div>
            <DeleteOutlined class="delete-icon" @click="removeTemplate(item, index)" />
          </div>
        </div>
      </Form>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMSearchTree, ActionListItem, BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { reactive, ref, onMounted, createVNode } from 'vue';
  import { Modal, message, RadioGroup, Radio, FormItemRest, Input, Form, FormItem, Textarea } from 'ant-design-vue';
  import {
    getEquipmentTagTree,
    getQueryListDictDown,
    reqEquipmentTag,
    reqUpdateEquipmentType,
    reqDeleteEquipmentType,
  } from '@/services';
  import { loopTree, findItemsById } from './utils';
  import { ExclamationCircleOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { usePermissionStore } from '@/stores/permission';
  import EquipmentInfo from './components/EquipmentInfo.vue';
  import Empty from '@/components/Empty/index.vue';
  const { hasPermission } = usePermissionStore();
  const treeData = ref<any[]>([]);
  const expandedKeys = ref<string[]>(['all']);
  const selectedKeys = ref<any[]>(['all']);
  const treeOpen = ref<boolean>(false);
  const treeTitle = ref<string>(t('新增设备类型'));
  const modalFormRef = ref<any>();
  const infoNameOptions = ref<any>([]); //字典中设备信息下拉框
  const dataNameOptions = ref<any>([]); //字典中设备数据下拉框
  const requiredList = ref<any>([]); //存是否必填list
  const formRef = ref<any>(null);
  const equipmentStatus = ref<any>([
    {
      label: t('灭菌'),
      value: 'DISINFECT_002',
    },
    {
      label: t('清洁'),
      value: 'CLEAN_001',
    },
    {
      label: t('校准'),
      value: 'CALIBRATION_003',
    },
  ]);

  // 模板表单
  const TemplateForm = ref<any>({
    useTemplateList: [],
  });
  const rowData = ref<any>({
    name: t('全部'),
  }); //选择树节点渲染右边描述列表数据

  // 树操作列表
  const treeActionList: ActionListItem[] = [
    {
      title: t('新建子设备类型'),
      action: 'addChildren',
      ifShow: (node: any) => {
        return node.nodeLevelInTree < 7 && hasPermission('160010001000001');
      },
    },
    {
      title: t('编辑设备类型'),
      action: 'editNode',
      ifShow: () => {
        return hasPermission('160010001000002');
      },
    },
    {
      title: t('删除设备类型'),
      action: 'deleteNode',
      ifShow: () => {
        return hasPermission('160010001000003');
      },
    },
  ];

  // 新增编辑弹窗formProps
  const formProps = reactive<any>({
    initialValues: {
      parentId: 'all',
    },
    schemas: [
      {
        field: 'parentId',
        component: 'TreeSelect',
        label: t('上级分类'),
        required: true,
        componentProps: {
          disabled: true,
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          request: async () => {
            const { data } = await getEquipmentTagTree();
            return [
              {
                name: t('全部'),
                showName: t('全部'),
                key: 'all',
                id: 'all',
                children: loopTree(data),
              },
            ];
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('类型名称'),
        required: true,
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'code',
        component: 'Input',
        label: t('类型编码'),
        required: true,
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'equipmentInfo',
        label: t('设备信息'),
        component: 'Select',
        componentProps: () => {
          return {
            mode: 'multiple',
            allowClear: false,
            options: infoNameOptions.value,
            fieldNames: {
              label: 'showName',
              value: 'value',
            },
            filterOption: (input: string, option: any) => {
              return option.showName.toLowerCase().indexOf(input.toLowerCase()) >= 0;
            },
            onChange: (value: any, option: any) => {
              requiredList.value = option?.map((item: any) => {
                return {
                  ...item,
                  required: false, //默认非必填
                };
              });
            },
          };
        },
      },
      // 设备信息必填否
      {
        field: 'infoDetails',
        component: 'Input',
        noLabel: true,
        slot: 'selectA',
        defaultValue: [],
        vIf: () => requiredList.value.length > 0,
      },
      {
        field: 'equipmentData',
        label: t('设备数据'),
        component: 'Select',
        componentProps: () => {
          return {
            mode: 'multiple',
            allowClear: false,
            options: dataNameOptions.value,
            fieldNames: {
              label: 'showName',
              value: 'value',
            },
            filterOption: (input: string, option: any) => {
              return option.showName.toLowerCase().indexOf(input.toLowerCase()) >= 0;
            },
            onChange: () => {},
          };
        },
      },
      {
        field: 'equipmentStatus',
        component: 'CheckboxGroup',
        label: t('设备状态'),
        defaultValue: [],
        componentProps: () => {
          return {
            options: equipmentStatus.value,
            onChange: () => {},
          };
        },
      },
      {
        field: 'description',
        component: 'InputTextArea',
        label: t('描述'),
      },

      {
        field: 'useTemplateList',
        noLabel: true,
        defaultValue: [],
        slot: 'selectB',
        colProps: {
          span: 24,
        },
      },
    ],
  });

  // 点整条数据节点
  const select = async (
    selected_Keys: Key[],
    info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
      selectedNodes: DataNode[];
    },
  ) => {
    if (selected_Keys.length === 0) return;
    if (selectedKeys.value[0] === selected_Keys[0]) {
      return;
    }
    selectedKeys.value = selected_Keys;
    rowData.value = {
      name: info.node.name,
      infoPropertyList: info.node.infoPropertyList
        ?.map((item: any) => item.name + '-' + item.code + '(' + (item.required ? t('必填') : t('非必填')) + ')')
        .join(' , '),
      dataPropertyList: info.node.dataPropertyList?.map((item: any) => item.name + '-' + item.code).join(' , '),
      statusPropertyList: info.node.statusPropertyList?.map((item: any) => item.name).join(' , '),
      description: info.node.description,
      useTemplateList:
        info.node.useTemplateList?.length > 0
          ? info.node.useTemplateList.map((item: any) => {
              return {
                operateName: item.operateName,
                template: item.template,
              };
            })
          : '',
    };
  };

  // '+'号 新增树节点
  const allAddItem = async (node: any) => {
    treeTitle.value = t('新增设备类型');
    formProps.initialValues = {
      parentId: node?.id || 'all',
    };
    formProps.schemas[1].componentProps.disabled = false;
    formProps.schemas[2].componentProps.disabled = false;
    requiredList.value = [];
    TemplateForm.value.useTemplateList = [];
    await getInfoNameOptions();
    await getDataNameOptions();
    treeOpen.value = true;
  };
  // 新增树节点
  const addItem = async (node: any) => {
    treeTitle.value = t('新增设备类型');
    formProps.initialValues = {
      parentId: node?.id || 'all',
      // 设备信息、设备数据自动继承上级设备类型
      equipmentInfo: node?.infoPropertyList?.map((item: any) => item.code),
      equipmentData: node?.dataPropertyList?.map((item: any) => item.code),
    };
    formProps.schemas[1].componentProps.disabled = false;
    formProps.schemas[2].componentProps.disabled = false;

    requiredList.value = node?.infoPropertyList?.map((item: any) => {
      return {
        ...item,
        label: item.name,
        value: item.code,
        showName: item.name + '-' + item.code,
      };
    });
    TemplateForm.value.useTemplateList = [];
    await getInfoNameOptions();
    await getDataNameOptions();
    // 继承的属性不能删除，可再新增新属性；设备状态不继承
    infoNameOptions.value = infoNameOptions.value.map(item => {
      return {
        ...item,
        disabled: node?.infoPropertyList?.map((item2: any) => item2.code)?.includes(item.value),
      };
    });
    dataNameOptions.value = dataNameOptions.value.map(item => {
      return {
        ...item,
        disabled: node?.dataPropertyList?.map((item2: any) => item2.code)?.includes(item.value),
      };
    });
    treeOpen.value = true;
  };
  // 编辑树节点
  const editNodeFn = async (node: any) => {
    treeTitle.value = t('编辑设备类型');
    formProps.initialValues = {
      //回显
      parentId: node.parentId === '0' ? 'all' : node.parentId,
      id: node.id,
      name: node.name,
      code: node.code,
      equipmentInfo: node.infoPropertyList?.map((item: any) => item.code),
      equipmentData: node.dataPropertyList?.map((item: any) => item.code),
      equipmentStatus: node.statusPropertyList?.map((item: any) => item.code),
      description: node.description,
    };
    requiredList.value = node.infoPropertyList.map((item: any) => {
      return {
        ...item,
        label: item.name,
        value: item.code,
        showName: item.name + '-' + item.code,
      };
    });
    TemplateForm.value.useTemplateList = node.useTemplateList;
    formProps.schemas[1].componentProps.disabled = true;
    formProps.schemas[2].componentProps.disabled = true;
    await getInfoNameOptions();
    await getDataNameOptions();
    treeOpen.value = true;
  };
  // 删除树节点
  const deleteNodeFn = (node: any) => {
    Modal.confirm({
      title: t('是否删除该信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('信息删除后无法恢复，是否删除'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          await reqDeleteEquipmentType({ id: node.id });
          message.success(t('删除成功'));
          getTreeData();
          if (node.selected) {
            selectedKeys.value = ['all'];
            rowData.value = {};
          }
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
    });
  };
  // 新增子分类、编辑分类、删除分类
  const handleTreeAction = (action: ActionListItem, node: any) => {
    if (action.action === 'addChildren') {
      addItem(node);
    }
    if (action.action === 'editNode') {
      editNodeFn(node);
    }
    if (action.action === 'deleteNode') {
      deleteNodeFn(node);
    }
  };
  // 获取设备信息下拉框总数据
  const getInfoNameOptions = async () => {
    try {
      const { data } = await getQueryListDictDown({ dictId: '160010002001' });
      infoNameOptions.value = data?.map((item: any) => {
        return {
          ...item,
          showName: item.label + '-' + item.value,
        };
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 获取设备数据下拉框总数据
  const getDataNameOptions = async () => {
    try {
      const { data } = await getQueryListDictDown({ dictId: '160010002002' });
      dataNameOptions.value = data?.map((item: any) => {
        return {
          ...item,
          showName: item.label + '-' + item.value,
        };
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 添加模板
  const addTemplate = () => {
    TemplateForm.value.useTemplateList.push({ operateName: '', template: '' });
  };
  // 删除模板
  const removeTemplate = (item: any, index: any) => {
    TemplateForm.value.useTemplateList.splice(index, 1);
  };

  // 弹框确定
  const okModal = (instance: any) => {
    instance.validate().then(async (params: any) => {
      await formRef.value?.validate();
      const data: any = { ...params };
      data.parentId = data.parentId === 'all' ? '0' : data.parentId;
      //设备信息
      const infoTemp = requiredList.value.map((item: any) => {
        return {
          code: item.value,
          name: item.label,
          required: item.required,
          propertyType: item?.propertyType,
        };
      });

      // 设备状态
      const statusTemp = equipmentStatus.value
        ?.filter((item: any) => data.equipmentStatus?.includes(item.value))
        ?.map((item2: any) => {
          return {
            code: item2.value,
            name: item2.label,
            required: true,
          };
        });
      // 设备数据
      const dataTemp = dataNameOptions.value
        ?.filter((item: any) => data.equipmentData?.includes(item.value))
        ?.map((item2: any) => {
          return {
            code: item2.value,
            name: item2.label,
            // required: true,
          };
        });

      try {
        if (data.id) {
          // 编辑保存
          const data1 = {
            id: data.id,
            description: data?.description,
            infoPropertyList: infoTemp,
            statusPropertyList: statusTemp,
            dataPropertyList: dataTemp,
            useTemplateList: TemplateForm.value.useTemplateList,
          };
          await reqUpdateEquipmentType(data1);
          await getTreeData();
          // 若编辑的为当前选中,重新更新描述列表
          if (data.id === selectedKeys.value[0]) {
            const temp = findItemsById(treeData.value, data.id);
            rowData.value = {
              name: temp.name,
              infoPropertyList: temp.infoPropertyList
                ?.map(
                  (item: any) => item.name + '-' + item.code + '(' + (item.required ? t('必填') : t('非必填')) + ')',
                )
                .join(' , '),
              dataPropertyList: temp.dataPropertyList?.map((item: any) => item.name + '-' + item.code).join(' , '),
              statusPropertyList: temp.statusPropertyList?.map((item: any) => item.name).join(' , '),
              description: temp.description,
              useTemplateList:
                temp.useTemplateList?.length > 0
                  ? temp.useTemplateList.map((item: any) => {
                      return {
                        operateName: item.operateName,
                        template: item.template,
                      };
                    })
                  : '',
            };
          }
        } else {
          // 新增保存
          const { code, name, parentId } = data;
          const data2 = {
            code,
            name,
            parentId,
            description: data?.description,
            infoPropertyList: infoTemp,
            statusPropertyList: statusTemp,
            dataPropertyList: dataTemp,
            useTemplateList: TemplateForm.value.useTemplateList,
          };
          await reqEquipmentTag(data2);
          await getTreeData();
        }
        message.success(t(data.id ? t('编辑成功') : t('新增成功')));
        treeOpen.value = false;
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 获取树数据
  const getTreeData = async () => {
    try {
      const { data } = await getEquipmentTagTree();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          key: 'all',
          showName: t('全部'),
          children: loopTree(data),
        },
      ];
      return Promise.resolve();
    } catch (error) {
      return Promise.reject();
    }
  };
  onMounted(() => {
    getTreeData();
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
    width: 265px;
  }
  .infoDetails {
    width: 100%;
    // height: 50px;
    padding: 7px 10px 1px 10px;
    box-sizing: border-box;
    border-radius: 4px;
    background-color: #f2f3f5;
    > div {
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;
    }
  }
  .useTemplate {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    > div:nth-child(1) {
      margin-left: -15px;
    }
    > div:nth-child(2) {
      cursor: pointer;
      color: #2871ff;
    }
  }
  .template-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 10px 1px 10px;
    margin-bottom: 15px;
    box-sizing: border-box;
    border-radius: 4px;
    background-color: #f2f3f5;
  }

  .delete-icon {
    margin-top: -28px;
    // visibility: hidden;
    font-size: 20px;
    color: var(--bmos-danger-color);
  }
  :deep(.ems-empty-description) {
    color: #909398;
  }
</style>

<template>
  <div class="template-edit">
    <div class="template-edit-opeartion">
      <BreadCrumb
        :implement="(route.params.implement as string)"
        :page="IS_SHOW ? t('记录查看') : t('记录编辑')"></BreadCrumb>
      <div class="opeartion-container">
        <Button v-if="!IS_SHOW" v-hasAuth="120020001000009" @click="toConfig">
          {{ t('公式配置') }}
        </Button>
        <Button v-if="!IS_SHOW" @click="togglePattern">
          {{ t('版式切换') }}
        </Button>
        <Button @click="goBack">{{ t('返回') }}</Button>
        <Button v-if="!IS_SHOW" type="primary" :loading="saveLoading" @click="saveTemplate(false)">
          {{ t('保存') }}
        </Button>
        <Button v-if="!IS_SHOW" type="primary" :loading="saveLoading" @click="saveTemplate(true)">
          {{ t('完成') }}
        </Button>
      </div>
    </div>
    <div class="template-edit-content">
      <spin :spinning="spinClink" class="sole-flex">
        <ContentLayout
          :title="t('记录项')"
          class="record-content"
          :isIcon="!IS_SHOW"
          :style="{
            width: IS_SHOW ? '260px' : '300px',
            minWidth: '260px',
          }">
          <template #opeartionIcon>
            <BMModalForm
              v-if="!IS_SHOW"
              :formProps="FORM_ITEMS"
              wrapClassName="Modal_Record_nodelist_box"
              :submit="handleModalSubmit"
              :title="t('新增记录项')">
              <template #RECORD_UPLOAD="{ formModel }">
                <Upload
                  :before-upload="beforeUpload"
                  :customRequest="customRequest"
                  accept=".docx"
                  :file-list="formModel['fileList']"
                  @change="fileUpload($event, formModel)">
                  <Button>
                    <upload-outlined></upload-outlined>
                    {{ t('上传文件') }}
                  </Button>
                  <template #itemRender="{ file }">
                    <Flex justify="space-between" style="padding-right: 11px; box-sizing: border-box">
                      <Space>
                        <LoadingOutlined v-if="file.status === 'uploading'"></LoadingOutlined>
                        <BMIcons
                          v-if="file.status === 'done'"
                          icon="Success"
                          style="width: 14px; height: 14px; color: #59bf78"></BMIcons>
                        <span :style="file.status === 'error' ? 'color: red' : ''">
                          {{ file.name }}
                        </span>
                      </Space>
                      <CloseCircleFilled
                        style="color: var(--bmos-fourth-level-text-color); font-size: 12px"
                        class="cursor-common"
                        @click="
                          () => {
                            formModel['fileList'].splice(formModel['fileList'].indexOf(file), 1);
                          }
                        " />
                    </Flex>
                  </template>
                </Upload>
              </template>
              <template #trigger>
                <PlusOutlined class="cursor-common"></PlusOutlined>
              </template>
            </BMModalForm>
          </template>
          <div class="tree_box">
            <BMSearchTree
              ref="searchMethod"
              v-model:expanded-keys="EXPANDED_KEYS"
              :selected-keys="SELECTED_KEYS"
              :showSearch="false"
              :showAllAddIcon="false"
              :showAction="!IS_SHOW"
              :blockNode="true"
              :draggable="true"
              :autoExpandParent="true"
              :actionList="ACTION_LIST"
              :fieldNames="{
                title: 'name',
                key: 'itemId',
              }"
              :tree-data="TREE_DATA"
              @drop="onDrop"
              @select="isSaveRecord"></BMSearchTree>
          </div>
        </ContentLayout>
        <div class="record_shadow_box">
          <div v-if="SELECTED_KEYS.length == 0 && !IS_SHOW" class="record_shadow"></div>
          <ContentLayout
            :title="t('实例')"
            :iconClick="() => iconClick()"
            :isIcon="!IS_SHOW"
            :style="{
              width: '300px',
            }"
            class="component-content">
            <template #ortherIcon>
              <Tooltip>
                <template #title>
                  <span>{{ openFlag ? t('全部展开') : t('全部收起') }}</span>
                </template>
                <img class="retract" :src="openFlag ? open : retract" alt="" @click="ortherIconClick()" />
              </Tooltip>
            </template>
            <NodeList
              ref="nodelistRef"
              v-model:activeKeys="INST_ACTIVE_KEYS"
              :nodeList="(INST_NODE_LIST as any)"
              :icon="!IS_SHOW"
              @iconClick="deleteClick"
              @node-click="nodeClick"
              @edit-click="nodeEdit"
              @copy-click="copyClick"></NodeList>
            <Modal
              :title="`${t('新增')}${MODAL_ENUM_T[current_node?.type]}${t('组件')}`"
              :open="STATUS.EDIT"
              :destroyOnClose="true"
              :maskClosable="false"
              wrapClassName="Modal_Record_nodelist_box"
              @ok="() => editOk(current_node)"
              @cancel="
                () => {
                  STATUS.EDIT = false;
                  clickNode = {};
                }
              ">
              <ComponentProperty
                ref="BMFormRef"
                :items="current_node.items"
                :type="current_node.type"></ComponentProperty>
            </Modal>
            <!--新增自定义字段弹窗-->
            <BMModalForm
              ref="customFieldModalFormRef"
              v-model:open="openCustomField"
              :title="t('新建自定义字段')"
              :formProps="customFieldFormProps"
              wrapClassName="modalSizeMedium"
              :submit="customFieldSubmit"></BMModalForm>
          </ContentLayout>
          <TEditor
            v-if="!IS_SHOW"
            ref="EDITOR_INSTANCE"
            v-model:contentValue="contentValue"
            v-model:ctrlDown="ctrlDown"
            v-model:isClick="isClick"
            style="flex: 1"
            :isRage="isRage"
            @rendered="() => (spinClink = false)"
            @content-click="handleUEditorClick"
            @delete-content="contrastNode"
            @addTemplate="addTemplate" />
          <Record
            v-else
            ref="RECORD_INSTANCE"
            v-model:activeKeys="NODE_ACTIVE_KEYS"
            style="flex: 1"
            @node-click="recordNodeClick"></Record>
          <ContentLayout
            v-if="IS_SHOW"
            :title="t('公式')"
            class="record-content"
            :isIcon="false"
            style="width: 260px; min-width: 260px">
            <Formula :key="formKey" :component="currentComponent" :show="true"></Formula>
          </ContentLayout>
          <NodeModal v-model:open="STATUS.MODAL" :isRage="isRage" @close="NodeModalClose">
            <NodeComponentList
              v-model:isRage="isRage"
              v-model:clickNode="clickNode"
              @node-click="NodeComponentClick"
              @save-node="saveClickNode"
              @business-node-click="BusinessNodeComponentClick"></NodeComponentList>
          </NodeModal>
        </div>
      </spin>
    </div>
    <Modal
      v-model:open="saveModel"
      :title="t('提示')"
      :okText="t('保存')"
      :confirm-loading="confirmLoading"
      :width="320"
      @ok="saveModel = false">
      <div>{{ t('是否保存当前记录') }}</div>
    </Modal>
    <BMModalForm
      ref="modalFormRef"
      v-model:open="STATUS.COPY"
      :title="t('复制')"
      :formProps="copyFormProps"
      :cancelText="t('取消')"
      :okText="t('确定')"
      wrapClassName="modalSizeMedium"
      :submit="copySubmit"
      @cancel="STATUS.COPY = false"></BMModalForm>
    <EquipmentDataAcquisitionDynamicTable
      v-model:open="equipmentDataAcquisitionDynamicTableOpen"
      :current-node="current_node"
      @updateDetail="equipmentDataAcquisitionDynamicTableSubmit" />
  </div>
</template>

<script setup lang="tsx">
  import { BMSearchTree, BMModalForm, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
  import { UploadOutlined, PlusOutlined, LoadingOutlined, CloseCircleFilled } from '@ant-design/icons-vue';
  import { cloneDeep } from '@bmos/utils';
  import { useRouter, useRoute } from 'vue-router';
  import { NodeList, NodeDataType, Record, NODE } from '@/components/Record';
  import { Button, Upload, Modal, message, UploadChangeParam, Space, Spin, Flex, Tooltip } from 'ant-design-vue';
  import ContentLayout from '@/components/ContentLayout/index.vue';
  import { useEDITOR, useNodeList, useTree, useModelForm, customRequest, useDynamicTable } from './hooks';
  import { MODAL_ENUM_T } from './enum';
  import { t } from '@bmos/i18n';
  import { reactive, onMounted } from 'vue';
  import NodeComponentList from './component/NodeComponentList/index.vue';
  import BreadCrumb from './component/Breadcrumb/index.vue';
  import { dictListDictCode, recordItemSingleEdit } from '@/services';
  import { FILE_STAUTS } from '../RecordConfig/enum';
  import { BMIcons } from '@bmos/icons';
  import ComponentProperty from './component/ComponentProperty/index.vue';
  import { beforeUpload } from '@/utils';
  import {
    ALL_NODE_INFO,
    ALL_BUTTON_INFO,
    CUSTOM_FIELD,
    ALL_DYNAMIC_TABLE_NODE,
  } from '@/components/Record/NodeList/enum';
  import Formula from '@/pages/FormulaConfig/FormulaCheck/index.vue';
  import retract from '@/assets/images/retract.png';
  import open from '@/assets/images/open.png';
  import NodeModal from './component/Modal/index.vue';
  import TEditor from '@/components/TEditor/index.vue';
  import EquipmentDataAcquisitionDynamicTable from './component/Modal/EquipmentDataAcquisitionDynamicTable.vue';

  const router = useRouter();
  const route = useRoute();
  const isClick = ref(false);
  const editor = useEDITOR(isClick);
  const current_node = ref<any>();
  const BMFormRef = ref();
  const {
    contentValue,
    EDITOR_INSTANCE,
    RECORD_INSTANCE,
    IS_SHOW,
    NODE_ACTIVE_KEYS,
    togglePattern,
    spinClink,
    pageConfig,
  } = editor;
  // let IS_SAVE = false;
  const saveModel = ref(false);
  const confirmLoading = ref(false);

  const useNode = useNodeList(editor);

  const saveLoading = ref(false);
  const {
    INST_NODE_LIST,
    contrastNode,
    NODE_CLICK,
    ADD_NODE,
    ADD_CUSTOM_FIELD_NODE,
    DELETE_LIST_NODE,
    INST_ACTIVE_KEYS,
    nodeEditClick,
    MODAL_NODE,
    addBusinessNode,
    addBusinessGroup,
    copyBusinessGroup,
    ueditorWrapClick,
    NODE_ID,
    getNumber,
    setCurrentComponent,
    currentComponent,
    recordNodeClick,
    FATHER_NODE_CLICK,
    openFlag,
    formKey,
    ctrlDown,
  } = useNode;
  const use_Tree = useTree(editor, useNode, saveTemplate, NODE_ID);
  const {
    TREE_DATA,
    TREE_SELECT,
    EXPANDED_KEYS,
    SELECTED_KEYS,
    INIT_TREE_DATA,
    CURRENT_NODE,
    ACTION_LIST,
    onDrop,
    isRage,
    clickNode,
    searchMethod,
    RecordData,
    addTemplate,
  } = use_Tree;

  const { FORM_ITEMS, handleModalSubmit } = useModelForm(use_Tree, useNode, editor, saveTemplate);

  const STATUS = reactive<Record<string, boolean>>({
    OPEN: false,
    MODAL: false,
    EDIT: false,
    COPY: false,
  });
  const nodelistRef = ref();
  const modeDom = ref();
  const clickCopyNode = ref();

  const iconClick = () => {
    if (!CURRENT_NODE.value) {
      return message.error(t('当前没有记录项目，请先新建记录项目'));
    }
    STATUS.MODAL = true;
  };

  const deleteClick = (data: NodeDataType) => {
    isClick.value = true;
    Modal.confirm({
      content: t('组件删除后无法恢复，是否删除'),
      title: t('是否删除该组件'),
      onOk() {
        try {
          DELETE_LIST_NODE(data);
        } catch (error) {
          message.error(t('删除组件失败，请稍后重试'));
        }
      },
    });
  };

  const {
    equipmentDataAcquisitionDynamicTableOpen,
    openEquipmentDataAcquisitionDynamicTableModal,
    equipmentDataAcquisitionDynamicTableSubmit,
    addDynamicTableInEditor,
  } = useDynamicTable({
    current_node,
    useNode,
    editor,
  });

  const nodeEdit = (data: NodeDataType) => {
    isClick.value = true;
    if (MODAL_NODE.includes(data.type)) {
      const items = data.data?.componentDetail ? JSON.parse(data.data?.componentDetail) : '';
      current_node.value = { ...data.data, items, type: data.type };
      STATUS.EDIT = true;
    }
    if (data.type === 'CUSTOM_FIELD') {
      openCustomFieldModalForm(data);
    }
    if (data.type === 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE') {
      openEquipmentDataAcquisitionDynamicTableModal(data);
    }
  };

  const editOk = async (node: NodeDataType | any) => {
    try {
      const res = await BMFormRef.value?.validate();
      if (node.fieldId === void 0) {
        const data = { ...node, componentDetail: res };
        if (!isRage.value) {
          ADD_NODE(data);
        } else {
          clickNode.value = data;
        }
      } else {
        const data = { ...node, componentDetail: res };
        const flag = nodeEditClick(data);
        if (flag === void 0) return (STATUS.EDIT = false);
        if (!flag) return message.error(t('修改失败，请稍后重试'));
      }
      STATUS.EDIT = false;
    } catch (error) {
      console.log(error);
    }
  };
  const NodeModalClose = () => {
    if (isRage.value == true) {
      isRage.value = false;
      clickNode.value = {};
      message.info(t('您已关闭批量编辑模式'));
    }
  };

  const NodeComponentClick = (data: NODE & { fieldId: string; componentNumber: number }) => {
    isClick.value = true;
    if (MODAL_NODE.includes(data.componentType!)) {
      current_node.value = { ...data, items: [], type: data.componentType };
      STATUS.EDIT = true;
      return;
    }
    ADD_NODE(data);
  };

  const saveClickNode = (data: any) => {
    // 单选等组件需要配置
    if (MODAL_NODE.includes(data.componentType!)) {
      current_node.value = { ...data, items: [], type: data.componentType };
      STATUS.EDIT = true;
      return;
    }
    clickNode.value = { ...data, items: [], type: data.componentType };
  };

  const BusinessNodeComponentClick = (data: NODE & { fieldId: string; componentNumber: number }) => {
    addBusinessNode(data);
  };
  // 新增自定义字段弹窗
  const customFieldModalFormRef = ref<any>();
  const openCustomField = ref(false);
  const customFieldFormProps = reactive<FormProps>({
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'dataType',
        component: 'Select',
        label: t('数据类型'),
        required: true,
        componentProps: {
          options: [
            {
              label: t('文字'),
              value: 'TEXT',
            },
            {
              label: t('数值'),
              value: 'NUMBER',
            },
            {
              label: t('日期'),
              value: 'DATE',
            },
          ],
        },
      },
      {
        field: 'dataSources',
        component: 'Select',
        label: t('数据来源'),
        required: true,
        componentProps: () => {
          return {
            options: [],
            onChange: (value: string) => {
              dictListDictCode({
                code: value,
              }).then((res: any) => {
                customFieldModalFormRef.value?.formRef?.setFormModel('fieldName', undefined);
                customFieldModalFormRef.value?.formRef?.updateSchema({
                  field: 'fieldName',
                  componentProps: {
                    options: res.data.map((item: any) => {
                      return {
                        ...item,
                        showName: `${item.label}-${item.value}`,
                      };
                    }),
                  },
                });
              });
            },
          };
        },
      },
      {
        field: 'fieldName',
        component: 'Select',
        label: t('字段数据'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'value',
            },
            onChange: (_value: any, data: any) => {
              formModel['fieldName'] = data.label;
              formModel['fieldData'] = data.value;
            },
          };
        },
      },
    ],
  });
  // 复制组件表单
  const copyFormProps = reactive<FormProps>({
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'copyNum',
        component: 'Input',
        label: t('复制次数'),
        dynamicRules: () => {
          return [
            {
              required: true,
              trigger: 'blur',
              validator: (_rule: any, value: any) => {
                if (!value) return Promise.reject(t('请输入复制次数'));
                const reg = /^-?\d+$/;
                if (Number(value) <= 0 || !reg.test(value)) {
                  return Promise.reject(t('只能输入正数'));
                }
                if (Number(value) > 10) {
                  return Promise.reject(t('复制次数不能大于10次'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
  });

  // 获取父级组件类型(根据子组件fieldId)
  const getParentComponentType = (fieldId: any, treeData: any) => {
    let parent = null;
    function getParent(id: any, list: any, obj: any) {
      list.forEach((item: any) => {
        if (item.fieldId === id) {
          parent = obj;
          return;
        }
        if (item.children && item.children.length > 0) {
          getParent(id, item.children, item);
        }
      });
    }

    getParent(fieldId, treeData, null);
    if (parent) {
      // @ts-ignore
      return parent.componentType;
    }
    return '';
  };

  const openCustomFieldModalForm = (data: any) => {
    current_node.value = data.data;
    // 获取自定义字段组件/添加自定义字段按钮的父级组件componentType
    const parentComponentType = getParentComponentType(data.data.fieldId, INST_NODE_LIST.value);
    let dataSourcesOptions = [];
    if (parentComponentType === 'EQUIPMENT_INFO') {
      dataSourcesOptions = [
        {
          label: t('设备信息自定义字段'),
          value: 'DeviceInformationFields',
        },
      ];
    } else if (parentComponentType === 'EQUIPMENT_DATA_ACQUISITION_GROUP') {
      dataSourcesOptions = [
        {
          label: t('设备数据自定义字段'),
          value: 'DeviceDataFields',
        },
      ];
    } else if (parentComponentType === 'BATCH_INSPECTION_RESULTS') {
      dataSourcesOptions = [
        {
          label: t('检验结果自定义字段'),
          value: 'InspectionResultCustomFields',
        },
      ];
    } else {
      dataSourcesOptions = [
        {
          label: t('物料信息自定义字段'),
          value: 'MaterialCustomFields',
        },
        {
          label: t('物料批次自定义字段'),
          value: 'MaterialBatchCustomFields',
        },
        {
          label: t('物料件自定义字段'),
          value: 'MaterialPieceCustomFields',
        },
      ];
    }
    openCustomField.value = true;
    setTimeout(() => {
      customFieldModalFormRef.value?.formRef?.updateSchema({
        field: 'dataSources',
        componentProps: {
          options: dataSourcesOptions,
        },
      });
    }, 0);
    if (current_node.value.componentType === 'CUSTOM_FIELD') {
      const componentDetail = JSON.parse(current_node.value.componentDetail);
      setTimeout(() => {
        customFieldModalFormRef.value?.formRef?.setFormModels(componentDetail);
        dictListDictCode({
          code: componentDetail.dataSources,
        }).then((res: any) => {
          customFieldModalFormRef.value?.formRef?.updateSchema({
            field: 'fieldName',
            componentProps: {
              options: res.data.map((item: any) => {
                return {
                  ...item,
                  showName: `${item.label}-${item.value}`,
                };
              }),
            },
          });
        });
      }, 100);
    }
  };

  const customFieldSubmit = async (formValues: Recordable) => {
    const Node: any = {
      ...CUSTOM_FIELD,
      componentDetail: formValues,
    };
    ADD_CUSTOM_FIELD_NODE(Node, current_node.value);
    openCustomField.value = false;
  };

  // 获取父元素距离父元素的距离
  const getAddOffsetTop = (doc: any) => {
    let offsetTop = 0;
    offsetTop = doc.offsetTop;
    if (doc.offsetParent.className.indexOf('formula-container') < 0) {
      offsetTop = offsetTop + getAddOffsetTop(doc.offsetParent);
    }
    return offsetTop;
  };

  // 判断是否是基础组件，是基础组件才调用NODE_CLICK
  const nodeClick = (key: any, ActiveKey: any, data: any) => {
    isClick.value = true;
    if (IS_SHOW.value) {
      setCurrentComponent(key);
      // 获取节点滚动条距离
      const element = document.getElementsByName(key)[0];
      if (!element) {
        return;
      }
      const fatherElement = document.getElementsByClassName('formula-container')[0];
      if (!fatherElement) {
        return;
      }
      let offsetTop = element.offsetTop;
      if (element?.offsetParent && element.offsetParent.className.indexOf('formula-container') < 0) {
        offsetTop += getAddOffsetTop(element.offsetParent);
      }
      if (offsetTop < 200) {
        fatherElement.scrollTop = 0;
      } else {
        fatherElement.scrollTop = offsetTop - 20;
      }
    }
    if (ALL_NODE_INFO[data.type]) {
      NODE_CLICK(key, ActiveKey, data);
    }
    if (ALL_DYNAMIC_TABLE_NODE.includes(data.type)) {
      addDynamicTableInEditor(ActiveKey, data);
    }
    // 点击按钮节点，添加组
    if (ALL_BUTTON_INFO.includes(data.type)) {
      // 点击自定义字段按钮单独处理
      if (data.type === 'CUSTOM_FIELD_BUTTON') {
        openCustomFieldModalForm(data);
        return;
      }
      if (data.type === 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE_BUTTON') {
        openEquipmentDataAcquisitionDynamicTableModal(data);
        return;
      }
      addBusinessGroup(data);
    }
    // 点击业务组件分组
    if (!ALL_NODE_INFO[data.type] && !ALL_BUTTON_INFO.includes(data.type)) {
      if (data.data.children) {
        FATHER_NODE_CLICK(getChildren(data.data.children));
      }
    }
  };
  // 复制组件
  const copyClick = (data: any, copyTypeButton: any) => {
    isClick.value = true;
    clickCopyNode.value = {
      data,
      copyTypeButton,
    };
    STATUS.COPY = true;
    // copyBusinessGroup(data, copyTypeButton);
  };
  const copySubmit = async (data: any) => {
    const num = data.copyNum;
    for (let i = 0; i < num; i++) {
      await copyBusinessGroup(clickCopyNode.value.data, clickCopyNode.value.copyTypeButton);
    }
    STATUS.COPY = false;
  };

  const getChildren = (arr: any) => {
    let list: any = [];
    arr?.map((item: any) => {
      if (ALL_BUTTON_INFO.includes(item.type)) {
        return;
      }
      if (item.children && item.children.length > 0) {
        list.push(...getChildren(item.children));
      } else {
        list.push(item);
      }
    });
    return list;
  };

  const fileUpload = (info: UploadChangeParam, model: any) => {
    let resFileList = [...info.fileList];
    resFileList = resFileList.slice(-2);
    resFileList = resFileList.filter(file => {
      if (file.response) {
        file.url = file.response.url;
      }
      return file.status !== FILE_STAUTS.ERROR && file.status !== void 0;
    });
    const file = resFileList.pop();

    model['fileList'] = file ? [file] : [];
    // model['name'] = resFileList[0].name?.replace('.docx','')
  };

  // 过滤按钮
  const filterBusinessButtonNode = (componentList: any) => {
    componentList.forEach((item: any) => {
      if (item.children && item.children.length > 0) {
        item.children = item.children.filter((child: any) => {
          return !ALL_BUTTON_INFO.includes(child.componentType);
        });
        filterBusinessButtonNode(item.children);
      }
    });
  };

  // 设置父组件的used属性,递归设置,当子孙有一个used为true,则父节点used为true
  const setParentComponentUsed = (componentList: any) => {
    let flag = false;
    componentList.forEach((item: any) => {
      if (item.children && item.children.length > 0) {
        item.used = setParentComponentUsed(item.children);
      }
      if (item.used) {
        flag = true;
      }
    });
    return flag;
  };

  async function saveTemplate(isback: boolean = false, isAdd: boolean = false) {
    if (saveLoading.value) {
      message.error(t('保存中,请不要重复保存'));
      return;
    }
    if (TREE_DATA.value[0].children.length === 0 && isAdd) {
      // 首次添加记录项,不需要保存
      return;
    }
    const data: any[] = RecordData.itemList;
    if (TREE_DATA.value[0].children.length === 0) {
      Modal.confirm({
        title: t('提示'),
        content: t('无记录项需要保存'),
        class: 'save-confirm',
        footer() {
          return (
            <div class='save-confirm-btn'>
              <Button onClick={() => Modal.destroyAll()}>{t('取消')}</Button>
              <Button
                type='primary'
                onClick={() => {
                  if (isback) {
                    // 返回
                    router_go();
                  }
                  Modal.destroyAll();
                }}>
                {t('确认')}
              </Button>
            </div>
          );
        },
        async onOk() {
          if (isback) {
            // 返回
            router_go();
            Modal.destroyAll();
          }
        },
      });
      return;
    }
    data.map((item: any) => {
      EDITOR_INSTANCE.value.deleteHeader();
      let content = EDITOR_INSTANCE.value.getAllContent();
      item.componentList = INST_NODE_LIST.value.filter(() => true);
      item.maxNumber = getNumber();
      item.pageConfig = JSON.stringify(pageConfig.value) || item.pageConfig;
      if (!content) {
        item.fileContent = '';
        return;
      }
      content = content?.split('<hr class="fhhr" style="margin:5px 0;" size="1.000"/>')?.join('');
      // 提出页眉页脚,单独存入字段,content中不存入页眉页脚
      let newContent = '';
      const removeHeaderArr = content.split('<!-- remove_header_flag -->'); //根据flag拆分成3段
      if (removeHeaderArr.length == 3) {
        // 没有进入过该记录项,不做变更,有值说明进入过
        newContent = removeHeaderArr[0] || '';
        const removeFooterArr = removeHeaderArr[2]?.split('<!-- remove_footer_flag -->');
        newContent = newContent + removeFooterArr[0] + (removeFooterArr[2] || '');
        // 保存页眉页脚
        // 防止为null
        if (!!item.docxHeader && !!item.docxHeader.headerPrimary) {
          item.docxHeader.headerPrimary.content = removeHeaderArr[1];
        } else if (removeHeaderArr[1]) {
          item.docxHeader = {
            headerFirst: null,
            headerPrimary: {
              content: removeHeaderArr[1],
              pageCodeHorizontalAlignment: 1,
            },
            headerEven: null,
            linkToPrevious: false,
          };
        }
        if (!!item.docxFooter && !!item.docxFooter.footerPrimary) {
          item.docxFooter.footerPrimary.content = removeFooterArr[1];
        } else if (removeFooterArr[1]) {
          item.docxFooter = {
            footerFirst: null,
            footerPrimary: {
              content: removeFooterArr[1],
              pageCodeHorizontalAlignment: 1,
            },
            footerEven: null,
            linkToPrevious: false,
          };
        }
      } else {
        newContent = content;
      }
      item.fileContent = newContent;
    });
    const hide = message.loading(t('保存中'), 0);
    try {
      saveLoading.value = true;
      const arr: any[] = cloneDeep(data);
      arr.forEach(item => {
        if (item.componentList && item.componentList.length > 0) {
          // 提交时过滤生产BOM信息的按钮
          filterBusinessButtonNode(item.componentList);
          // 提交时设置父组件的used属性
          item.used = setParentComponentUsed(item.componentList);
        }
      });
      // 编辑/新增/记录管理共用一个接口
      const res = await recordItemSingleEdit(arr[0]);
      if (res.code === 0 && isback) {
        // 返回
        router_go();
      }
      if (res.code === 0 && !isback) {
        // 刷新页面
        const selectData = TREE_DATA.value[0].children.find((item: any) => item.itemId == SELECTED_KEYS.value[0]);
        if (selectData) {
          const node = {
            node: {
              eventKey: selectData.itemId,
              dataRef: selectData,
            },
          };
          TREE_SELECT([], node);
        } else {
          INIT_TREE_DATA();
        }
        message.success(t('保存成功'));
      }
    } catch (error: any) {
      saveLoading.value = false;
      throw message.error(error.message);
    } finally {
      hide();
      saveLoading.value = false;
    }
  }

  const handleUEditorClick = (id: string) => {
    if (id) {
      ueditorWrapClick(id, ctrlDown.value);
    }
  };

  const router_go = () => {
    Modal.destroyAll();
    if (route.params.implement === '1') {
      router.push('/Implement/record-manage');
    } else {
      router.push('/product-config/record-config');
    }
  };

  // 设置监听,弹窗移动
  const setModelMove = () => {
    // 事件委托,绑定到body上
    const element = document.getElementsByTagName('body')[0];
    element.addEventListener('mousedown', function (event: any) {
      // 检查事件是否来自模态框
      if (typeof event.target?.className == 'string' && event.target?.className.indexOf('mes-modal-title') >= 0) {
        const dom = event.target?.closest('.mes-modal');
        dom.setAttribute('isClick', true);
        // 需要减去padding 16px
        dom.setAttribute('etop', event.offsetY + 16);
        dom.setAttribute('eleft', event.offsetX + 16);
        dom.style.top = event.pageY - event.offsetY - 16 + 'px';
        dom.style.left = event.pageX - event.offsetX - 16 + 'px';
        dom.style.position = 'fixed';
        // 保存点击正在移动的dom
        modeDom.value = dom;
      }
    });
    element.addEventListener('mouseup', function (event: any) {
      // 检查事件是否来自模态框
      if (typeof event.target?.className == 'string' && event.target?.className.indexOf('mes-modal-title') >= 0) {
        const dom = event.target?.closest('.mes-modal');
        dom.setAttribute('isClick', false);
      }
    });
    element.addEventListener('mousemove', function (event: any) {
      // 检查事件是否来自模态框
      if (typeof event.target?.className == 'string' && event.target?.className.indexOf('mes-modal-title') >= 0) {
        const dom = event.target?.closest('.mes-modal');
        if (dom.getAttribute('isClick') == 'true') {
          dom.style.top = event.pageY - dom.getAttribute('etop') * 1 + 'px';
          dom.style.left = event.pageX - dom.getAttribute('eleft') * 1 + 'px';
        }
      } else if (!!modeDom.value && modeDom.value.getAttribute('isClick') == 'true') {
        // 防止鼠标移动过快,移出标签监听不到
        modeDom.value.style.top = event.pageY - modeDom.value.getAttribute('etop') * 1 + 'px';
        modeDom.value.style.left = event.pageX - modeDom.value.getAttribute('eleft') * 1 + 'px';
      }
    });
  };

  const goBack = () => {
    if (!IS_SHOW.value) {
      Modal.confirm({
        title: t('是否保存修改记录'),
        content: t('是否对记录的修改进行保存'),
        class: 'save-confirm',
        footer() {
          return (
            <div class='save-confirm-btn'>
              <Button onClick={() => Modal.destroyAll()}>{t('取消')}</Button>
              <Button onClick={() => router_go()}>{t('不保存')}</Button>
              <Button type='primary' loading={saveLoading.value} onClick={() => saveTemplate(true)}>
                {t('保存')}
              </Button>
            </div>
          );
        },
        async onOk() {
          try {
            await saveTemplate(true);
            router_go();
          } catch (error) {}
        },
      });
      return;
    }
    router_go();
  };

  // 是否保存当前批记录
  const isSaveRecord = (keys: KEY[], node: any) => {
    if (keys.length == 0) {
      SELECTED_KEYS.value = [node.node.eventKey];
      return;
    }
    // 编辑时切换记录项提示保存
    if (!IS_SHOW.value) {
      // 没有点击过,直接跳转记录项
      if (!isClick.value) {
        TREE_SELECT(keys, node);
        isClick.value = false;
        return;
      }
      Modal.confirm({
        title: t('是否保存修改记录'),
        content: t('是否对记录的修改进行保存'),
        class: 'save-confirm',
        footer() {
          return (
            <div class='save-confirm-btn'>
              <Button onClick={() => Modal.destroyAll()}>{t('取消')}</Button>
              <Button
                onClick={() => {
                  Modal.destroyAll();
                  TREE_SELECT(keys, node);
                  isClick.value = false;
                }}>
                {t('不保存')}
              </Button>
              <Button
                type='primary'
                loading={saveLoading.value}
                onClick={async () => {
                  await saveTemplate(false);
                  Modal.destroyAll();
                  TREE_SELECT(keys, node);
                  isClick.value = false;
                }}>
                {t('保存')}
              </Button>
            </div>
          );
        },
      });
      return;
    }
    // 查看时直接切换记录项
    TREE_SELECT(keys, node);
  };

  // 跳转公式配置弹窗
  const toConfig = () => {
    Modal.confirm({
      title: t('是否保存修改记录'),
      content: t('是否对记录的修改进行保存'),
      class: 'save-confirm',
      footer() {
        return (
          <div class='save-confirm-btn'>
            <Button onClick={() => Modal.destroyAll()}>{t('取消')}</Button>
            <Button onClick={() => router_go_formula()}>{t('不保存')}</Button>
            <Button
              type='primary'
              loading={saveLoading.value}
              onClick={async () => {
                await saveTemplate(false);
                router_go_formula();
              }}>
              {t('保存')}
            </Button>
          </div>
        );
      },
    });
  };
  const router_go_formula = () => {
    Modal.destroyAll();
    router.push({
      name: 'formula-config',
      params: {
        record_id: router.currentRoute.value.params.record_id,
        is_show: router.currentRoute.value.params.record_type,
        implement: route.params.implement,
        update: route.params.update,
        recordId: route.params.recordId,
      },
    });
  };
  const ortherIconClick = () => {
    openFlag.value = !openFlag.value;
    if (openFlag.value) {
      nodelistRef.value.retractAll();
    } else {
      nodelistRef.value.openAll();
    }
  };
  onMounted(() => {
    if (IS_SHOW.value) {
      INIT_TREE_DATA();
    }
    // setKeyDown();
    setModelMove();
  });
</script>
<style lang="less">
  .Modal_Record_nodelist_box {
    .mes-modal-title {
      width: 100%;
      cursor: pointer;
      user-select: none;
    }
  }
</style>
<style scoped lang="less">
  :deep(.mes-spin-nested-loading) {
    width: 100%;
  }
  :deep(.mes-spin-container) {
    display: flex;
    flex-direction: row;
  }
  :deep(.ueditor-container) {
    height: 100%;
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
          z-index: 99999999999;
        }
      }
    }
  }
  .record-content {
    border-right: 1px solid rgba(225, 227, 229, 1);
    box-sizing: border-box;
  }
  :deep(.container-content) {
    height: calc(100% - 41px);
    // max-height: 94%;
    padding: 12px;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .component-content {
    border-right: 1px solid #eeeeee;
    box-sizing: border-box;
  }
  .save-confirm {
    display: flex;
    justify-content: flex-end;
    column-gap: 16px;
  }
  .retract {
    margin-right: 10px;
    cursor: pointer;
    width: 18px;
    &:hover {
      background: #f2f3f4;
    }
  }
  .template-edit-content {
    :deep(.mes-tree-treenode-selected) {
      border-radius: 4px;
    }
  }
  .record_shadow_box {
    width: 100%;
    height: 100%;
    position: relative;
    display: flex;
    .record_shadow {
      position: absolute;
      width: 100%;
      height: 100%;
      right: 0;
      top: 0;
      background-color: rgba(0, 0, 0, 0.2);
      z-index: 1000;
    }
  }
  .tree_box {
    position: relative;
    height: 100%;
  }
</style>

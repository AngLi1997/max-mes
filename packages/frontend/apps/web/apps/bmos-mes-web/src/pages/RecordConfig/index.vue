<template>
  <BMPageComponent
    :selectedKeys="TREEFIELD.selectedKeys"
    :rowKeys="['recordId', 'versionId']"
    :treeData="TREE_DATA"
    :showAllAddIcon="implement !== '1' && hasPermission('120020001000001')"
    :showAction="implement !== '1'"
    :fieldNames="fieldNames"
    :titles="[t('模板信息'), t('版本信息')]"
    :treeField="treeField"
    :tableFields="tableFields"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 18,
        },
      },
    ]"
    :actionList="ACTION_LIST"
    :requests="[reqRecordList, getTemplateVersionList]"
    :columns="[recordColumn, versionColumn]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <BMModalForm
        v-model:open="STATUS.OPEN"
        :title="t(MODAL_TITLE[STATUS.FORM])"
        :submit="async (...args) => handleModalSubmit(insts, ...args)">
        <template #footer>
          <div class="steps-action">
            <Button v-if="current == 0 || !!STATUS.FORM" @click="handleModalCancel">
              {{ t('取消') }}
            </Button>
            <Button v-if="current == 1 && !STATUS.FORM" style="margin-left: 8px" @click="downloadFile">
              {{ t('下载') }}
            </Button>
            <Button v-if="current > 0 && !STATUS.FORM" style="margin-left: 8px" @click="current--">
              {{ t('上一步') }}
            </Button>
            <Button
              v-if="(current == 1 && !STATUS.FORM) || (typeIsFile && current == 0)"
              type="primary"
              @click="nextStepClick">
              {{ t('下一步') }}
            </Button>
            <Button
              v-if="current == 2 || (!typeIsFile && current == 0)"
              type="primary"
              :loading="addRecordLoading"
              @click="addRecordSubmit">
              {{ t('确定') }}
            </Button>
          </div>
        </template>
        <Steps
          v-if="!STATUS.FORM"
          :current="current"
          :items="[
            {
              title: t('新增方式'),
            },
            {
              title: t('格式检测'),
            },
            {
              title: t('记录信息'),
            },
          ]"
          size="small"></Steps>
        <div v-show="current == 0 || current == 2" class="steps_form_box">
          <!-- 新增方式表单/记录信息表单 -->
          <BMForm
            ref="myFormRef"
            style="padding-right: 16px"
            v-bind="{
            initialValues: formDefaultValue!,
            schemas: ADD_RECORD as FormProps['schemas'],
            labelWidth:120,
            showActionButtonGroup: false
          }">
            <template #UPLOAD_RECORD="{ formModel }">
              <Upload
                accept=".docx"
                :maxCount="1"
                :before-upload="beforeUpload"
                :customRequest="customRequest"
                :file-list="formModel['fileList']"
                @change="fileUploadChange($event, formModel)">
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
                        style="width: 14px; color: #59bf78"></BMIcons>
                      <span :style="file.status === 'error' ? 'color: red' : ''">
                        {{ file.name }}
                      </span>
                    </Space>
                    <CloseCircleFilled
                      style="color: var(--bmos-fourth-level-text-color); font-size: 12px"
                      class="cursor-common"
                      @click="() => formModel['fileList'].splice(formModel['fileList'].indexOf(file), 1)" />
                  </Flex>
                </template>
              </Upload>
            </template>
            <template #VERSION_METHOD="{ formModel }">
              <RadioGroup
                :options="RadioGroupOptions"
                option-type="button"
                :value="formModel['method']"
                @change="RadioGroupChange($event, formModel)"></RadioGroup>
            </template>
            <template #VERSION_UPLOAD="{ formModel }">
              <Upload
                :before-upload="beforeUpload"
                accept=".docx"
                :file-list="formModel['fileList']"
                :customRequest="customRequest"
                @change="fileUploadChange($event, formModel)">
                <Button>
                  <upload-outlined></upload-outlined>
                  {{ t('上传文件') }}
                </Button>
                <template #itemRender="{ file }">
                  <Space>
                    <LoadingOutlined v-if="file.status === 'uploading'"></LoadingOutlined>
                    <BMIcons
                      v-if="file.status === 'success'"
                      icon="Success"
                      style="width: 14px; color: #59bf78"></BMIcons>
                    <span :style="file.status === 'error' ? 'color: red' : ''">
                      {{ file.name }}
                    </span>
                  </Space>
                </template>
              </Upload>
            </template>
            <template #DEPART="{ formModel }">
              <div class="depart-modal-tree">
                <ModalBtn :submit="() => departMentSubmit(formModel, 'deptIds')" :title="t('部门授权')">
                  <DepartMent ref="departMent" :checks="formModel['deptIds']" :type="false" :isAdd="true"></DepartMent>
                  <template #trigger>
                    <Button :icon="DEPART_ICON(formModel, 'deptIds')" class="depart-btn">
                      {{ t('选择部门') }}
                    </Button>
                  </template>
                </ModalBtn>
              </div>
            </template>
          </BMForm>
        </div>
        <!-- 格式检测表单 -->
        <div v-show="current == 1" class="steps_form_box">
          <BMTable
            ref="tableInstance"
            :columns="columns"
            :show-tool-bar="false"
            :show-search-border="false"
            :dataSource="tableData"
            row-key="id"
            showIndex
            :pagination="false"
            :search="false"
            :scroll="{ x: 520, y: 1000 }"></BMTable>
        </div>
      </BMModalForm>
      <Button v-if="implement !== '1'" v-hasAuth="120020001000004" type="primary" @click="() => addRecord(treeNode)">
        {{ t('新增记录') }}
      </Button>
    </template>
    <template #tableHeaderToolbar1="{ currentNodes, instance }">
      <HistoryModal v-model:historyOpen="historyOpen" :businessId="secondRowData?.versionId" />
      <Button
        v-if="implement !== '1'"
        v-hasAuth="120020001000007"
        @click="handleToolBarClick(formula.VERSION, currentNodes, instance)">
        {{ t('新增版本') }}
      </Button>
      <Button
        v-hasAuth="implement === '1' ? 120060001001001 : 120020001000008"
        @click="handleToolBarClick(formula.EDIT, currentNodes)">
        {{ t('记录编辑') }}
      </Button>
      <Button
        v-hasAuth="implement === '1' ? 120060001001002 : 120020001000009"
        @click="handleToolBarClick(formula.FORMULA, currentNodes)">
        {{ t('公式配置') }}
      </Button>
    </template>
  </BMPageComponent>

  <BMModalForm
    :title="MODALTITLE"
    :formProps="FORM_ITEMS"
    :open="STATUS.CATETORY"
    :submit="CATEGORYHANDLE"
    @cancelModal="() => (STATUS.CATETORY = false)"></BMModalForm>
</template>

<script setup lang="tsx">
  import { Button, RadioGroup, Upload, message, Space, Flex, Steps } from 'ant-design-vue';
  import { ref, h } from 'vue';
  import { UploadOutlined, LoadingOutlined, CloseCircleFilled } from '@ant-design/icons-vue';
  import { formula, METHOD, STATE, MODAL_TITLE } from './enum';
  import { BMModalForm, ActionListItem, FormProps, BMPageComponent, BMForm, BMTable } from '@bmos/components';
  import { BMIcons } from '@bmos/icons';
  import { OPERATION } from '../TemplateEdit/enum';
  import { reqRecordList, recordCheckoutSaveRecord, reqRecordManageList } from '../../services';
  import { t } from '@bmos/i18n';
  import ModalBtn from '../../components/ModalBtn/index.vue';
  import { useRouter } from 'vue-router';
  import { useCategory, useTree, useModalForm, useColumns } from './hooks';
  import { customRequest } from '../../hooks';
  import DepartMent from '../../components/DepartMent/index.vue';
  import { beforeUpload } from '../../utils';
  import { usePermissionStore } from '@/stores/permission';
  import HistoryModal from '@/components/History/index.vue';
  import { recordItemEdit } from '@/services';
  const props = withDefaults(
    defineProps<{
      implement?: string; //1实施玩
    }>(),
    {
      implement: '2',
    },
  );
  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  const insts: any[] = [];

  const USETREE = useTree();
  const currentRow = ref();
  const departMent = ref();
  const current = ref<number>(0);
  const addRecordLoading = ref(false);

  const { TREE_DATA, TREE_ACTION, FORM_ITEMS, ACTION_LIST, treeField, fieldNames, MODALTITLE, TREEFIELD } = USETREE;

  const { MODAL_SUBMIT } = useCategory(USETREE);
  const {
    fileUploadChange,
    RadioGroupOptions,
    addRecord,
    ADD_RECORD,
    RadioGroupChange,
    handleModalSubmit,
    formDefaultValue,
    STATUS,
    tableData,
    columns,
    myFormRef,
    typeIsFile,
    downloadFile,
  } = useModalForm(currentRow, USETREE, props, current);

  const { recordColumn, versionColumn, tableFields, historyOpen, secondRowData } = useColumns({ props });

  const nextStepClick = async () => {
    if (current.value == 0) {
      // 新增方式
      await myFormRef.value.validate();
      const params = await myFormRef.value?.validate();
      console.log('============params', params);
      current.value++;
      return;
    }
    if (current.value == 1) {
      // 格式检测
      current.value++;
    }
  };

  const addRecordSubmit = async () => {
    try {
      addRecordLoading.value = true;
      await myFormRef.value.validate();
      const params = await myFormRef.value?.validate();
      await handleModalSubmit([], params);
    } catch (error) {
      console.log('error: ', error);
    } finally {
      addRecordLoading.value = false;
    }
  };

  const vertifyNodeStatus = (node: any) => {
    if (!node) {
      message.error(t('请先选择记录'));
      return;
    }
    if (props.implement !== '1' && Number(node.state.value) !== STATE.EDIT) {
      message.error(t('请选择编辑版本'));
      return false;
    }
    if (props.implement === '1' && Object.keys(node).length === 0) {
      message.error(t('请选择版本'));
      return false;
    }
    return true;
  };

  const handleToolBarClick = (type: number, node: any, inst?: any) => {
    //记录编辑
    if (type === formula.EDIT) {
      if (!vertifyNodeStatus(node[1])) return;
      // 记录编辑,编辑时要记录历史
      if (props.implement !== '1') {
        recordItemEdit({ recordVersionId: node[1]?.versionId });
      }

      router.push({
        name: 'TemplateEdit',
        params: {
          record_id: node[1]?.versionId,
          record_type: OPERATION.EDIT,
          implement: props.implement,
          update: 1,
          recordId: node[1]?.recordId,
        },
      });
      return;
    }
    if (type === formula.FORMULA) {
      //公式配置
      if (!vertifyNodeStatus(node[1])) return;
      router.push({
        name: 'formula-config',
        params: {
          record_id: node[1]?.versionId,
          is_show: OPERATION.EDIT,
          implement: props.implement,
          update: 0,
          recordId: node[1]?.recordId,
        },
      });
      return;
    }
    if (type === formula.VERSION) {
      current.value = 2;
      STATUS.METHOD = METHOD.COPY;
    }
    recordCheckoutSaveRecord({ recordId: node[0].recordId })
      .then(() => {
        currentRow.value = node[0];
        if (!insts[1]) insts[1] = inst;
        formDefaultValue.value.version = node[0]?.versionId;

        STATUS.FORM = 1;
        STATUS.OPEN = true;
      })
      .catch((err: any) => {
        message.error(err.message);
      });
    // AddRecord.value = VersionFormProps.value;
  };

  const handleModalCancel = () => {
    STATUS.METHOD = METHOD.COPY;
    STATUS.OPEN = false;
  };

  const handleTreeAction = (action: ActionListItem, node: any) => {
    TREE_ACTION(action, node);
    STATUS.CATETORY = true;
  };

  const CATEGORYHANDLE = async (val: any) => {
    const res = await MODAL_SUBMIT(val);
    if (res) STATUS.CATETORY = false;
  };

  const departMentSubmit = (model: any, field: string) => {
    const keys = departMent.value.getSelectKeys();
    model[field] = keys;
    return Promise.resolve(true);
  };

  const DEPART_ICON = (model: any, field: string) => {
    const style_icon = {
      width: '16px',
      height: '16px',
      verticalAlign: 'sub',
    };
    if (!model[field] || model[field]?.length === 0) {
      return h(BMIcons, {
        icon: 'Depart',
        style: style_icon,
      });
    } else {
      return h(BMIcons, {
        icon: 'Success',
        style: style_icon,
      });
    }
  };

  // 查版本信息
  const getTemplateVersionList = async (params: API.RecordListRecordReq) => {
    if (!params.recordId) return { data: [] };
    return props.implement !== '1' ? await reqRecordList(params) : await reqRecordManageList(params);
  };
</script>

<style scoped lang="less">
  .depart-btn {
    display: inline-flex;
    column-gap: 8px;
    align-items: center;
  }
  .steps_form_box {
    padding-top: 16px;
    min-height: 360px;
    :deep(.mes-radio-button-wrapper) {
      margin-right: 20px;
      border-radius: 4px;
      border-inline-start-width: 1px;
    }
    :deep(.mes-radio-button-wrapper:not(:first-child)::before) {
      display: none;
    }
  }
</style>

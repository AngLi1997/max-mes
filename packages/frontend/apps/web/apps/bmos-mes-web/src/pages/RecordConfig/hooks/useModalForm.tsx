import { OPERATION } from '@/pages/TemplateEdit/enum';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { fileStreamDownload } from '@bmos/utils';
import type { RadioChangeEvent, RadioGroupProps } from 'ant-design-vue';
import { RadioButton, RadioGroup, message } from 'ant-design-vue';
import { computed, nextTick, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { getVersionListByRecord, recordCopyVersion, recordDownloadByUrl, recordSaveRecord } from '../../../services';
import { FILE_STAUTS, METHOD } from '../enum';
import { UseModalFormType } from '../type';

export const useModalForm = (currentRow: any, useTree: any, props: any, current: any): UseModalFormType => {
  const router = useRouter();
  const { TREE_DATA } = useTree;
  const formDefaultValue = ref<FormProps['initialValues']>({
    method: METHOD.COPY,
    fileList: [],
    name: '',
    addType: true,
  })!;
  const myFormRef = ref();
  const tableData = ref([]);
  const originFilePath = ref('');

  const fileUploadChange = (info: any, model: any): void => {
    let resFileList = [...info.fileList];
    resFileList = resFileList.slice(-2);
    resFileList = resFileList.filter(file => {
      if (file.response) {
        file.url = file.response.url;
        tableData.value = file.response.formatResults || [];
        originFilePath.value = file.response.originFilePath;
      }
      return file.status !== FILE_STAUTS.ERROR && file.status !== undefined;
    });
    const file = resFileList.pop();
    model['fileList'] = file ? [file] : [];
    // model['name'] = resFileList.pop()?.name?.replace('.docx', '');
  };
  const downloadFile = async () => {
    if (!originFilePath.value) {
      return;
    }
    try {
      const res = await recordDownloadByUrl(originFilePath.value);
      fileStreamDownload(res, 'download.docx');
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const RadioGroupOptions: RadioGroupProps['options'] = [
    {
      label: t('复制已有版本'),
      value: METHOD.COPY,
    },
    {
      label: t('重新上传记录'),
      value: METHOD.NEW,
    },
  ];

  const STATUS = reactive({
    OPEN: false,
    METHOD: METHOD.COPY,
    CATETORY: false,
    FORM: 0,
  });

  const addRecord = (treeNode: any) => {
    currentRow.value = {};
    STATUS.FORM = 0;
    STATUS.METHOD = METHOD.NEW;
    let categoryId = '';
    categoryId = treeNode.id === 'all' ? void 0 : treeNode.id;
    formDefaultValue.value && (formDefaultValue.value.categoryId = categoryId);
    current.value = 0;
    typeIsFile.value = true;
    nextTick(() => (STATUS.OPEN = true));
  };

  const getSelectData = async () => {
    try {
      const res = await getVersionListByRecord(currentRow.value.recordId);
      return res.data;
    } catch (error: any) {
      message.error(error.message);
      return [];
    }
  };
  const typeIsFile = ref(true);
  const addTypeChange = (value: any) => {
    typeIsFile.value = value;
  };

  const ADD_RECORD = computed(() => {
    const fileList = {
      field: 'fileList',
      component: 'Input',
      label: t('上传记录'),
      slot: 'UPLOAD_RECORD',
      required: false,
      colProps: {
        span: 24,
      },
    };
    const version = {
      field: 'version',
      vIf: () => !typeIsFile.value || current.value == 2,
      component: 'Input',
      label: t('版本号'),
      required: true,
      colProps: {
        span: 24,
      },
    };
    const remark = {
      field: 'remark',
      vIf: () => !typeIsFile.value || current.value == 2,
      component: 'InputTextArea',
      label: t('备注'),
      colProps: {
        span: 24,
      },
    };
    if (!STATUS.FORM) {
      return [
        {
          label: t('新增方式'),
          vIf: () => current.value == 0,
          field: 'addType',
          defaultValue: false,
          required: true,
          colProps: {
            span: 24,
          },
          component: ({ formModel }: any) => {
            return (
              <>
                <RadioGroup v-model:value={formModel['addType']} onChange={() => addTypeChange(formModel['addType'])}>
                  <RadioButton value={true}>{t('上传word记录')}</RadioButton>
                  <RadioButton value={false} class='waring-false'>
                    {t('编辑器自行创建')}
                  </RadioButton>
                </RadioGroup>
              </>
            );
          },
        },
        {
          field: 'fileList',
          vIf: () => typeIsFile.value && current.value == 0,
          component: 'Input',
          required: true,
          label: t('上传记录'),
          slot: 'UPLOAD_RECORD',
          colProps: {
            span: 24,
          },
        },
        {
          field: 'categoryId',
          vIf: () => !typeIsFile.value || current.value == 2,
          component: 'TreeSelect',
          label: t('分类'),
          required: true,
          colProps: {
            span: 24,
          },
          componentProps: {
            treeData: TREE_DATA.value[0].itemList,
            fieldNames: {
              children: 'itemList',
              label: 'name',
              value: 'id',
            },
          },
        },
        {
          field: 'name',
          vIf: () => !typeIsFile.value || current.value == 2,
          component: 'Input',
          label: t('记录名称'),
          required: true,
          colProps: {
            span: 24,
          },
        },
        version,
        {
          field: 'deptIds',
          vIf: () => !typeIsFile.value || current.value == 2,
          label: t('部门授权'),
          required: true,
          slot: 'DEPART',
          colProps: {
            span: 24,
          },
          dynamicRules: ({ formModel }: any) => {
            return [
              {
                required: true,
                validator: () => {
                  if (!formModel['deptIds'] || formModel['deptIds']?.length === 0) {
                    return Promise.reject(t('请选择部门授权'));
                  }
                  return Promise.resolve();
                },
                trigger: 'change',
              },
            ];
          },
        },
        remark,
      ];
    }
    const schema = [
      version,
      {
        field: 'method',
        component: 'Input',
        label: t('方式'),
        required: true,
        colProps: {
          span: 24,
        },
        slot: 'VERSION_METHOD',
      },
    ];
    return [
      ...schema,
      {
        vIf: () => STATUS.METHOD === METHOD.COPY,
        field: 'versionOldId',
        component: 'Select',
        label: t('复制已有版本'),
        required: true,
        colProps: {
          span: 24,
        },
        componentProps: {
          request: async () => await getSelectData(),
          fieldNames: {
            label: 'version',
            value: 'versionId',
            options: 'options',
          },
        },
      },
      {
        vIf: () => STATUS.METHOD === METHOD.NEW,
        ...fileList,
      },
      remark,
    ];
  });

  const RadioGroupChange = (e: RadioChangeEvent, model?: any) => {
    STATUS.METHOD = e.target.value;
    model['method'] = e.target.value;
  };

  const handleModalSubmit = async (insts: any[], val: any) => {
    const { name, categoryId, remark, version, fileList = [], versionOldId, deptIds = [] } = val;
    const fileTarget = fileList[fileList.length - 1];
    if (STATUS.FORM === 1 && STATUS.METHOD === METHOD.COPY) {
      const data = {
        versionOldId,
        version,
        remark,
        fileContent: fileTarget?.response?.filePath,
        recordId: currentRow.value?.recordId,
      };

      try {
        const res = await recordCopyVersion(data);
        STATUS.OPEN = false;
        router.push({
          name: 'TemplateEdit',
          params: {
            record_id: res.data,
            record_type: OPERATION.EDIT,
            implement: props.implement,
            update: 0,
            recordId: currentRow.value?.recordId,
          },
        });
      } catch (error: any) {
        message.error(error.message);
      }
      return;
    }
    try {
      const data = {
        name: name ? name : currentRow.value?.name,
        remark,
        version,
        categoryId: categoryId ? categoryId : currentRow.value?.categoryId,
        items: fileTarget?.response?.itemVO,
        recordId: currentRow.value?.recordId,
        deptIds,
      };
      const res = await recordSaveRecord(data);
      STATUS.OPEN = false;
      router.push({
        name: 'TemplateEdit',
        params: {
          record_id: res.data.recordVersionId,
          record_type: OPERATION.EDIT,
          implement: props.implement,
          update: 0,
          recordId: res.data.recordId,
        },
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const columns: TableColumn[] = [
    {
      title: t('格式类型'),
      dataIndex: 'formatType',
      resizable: true,
      width: 200,
      customRender: ({ record }) => <div>{record.formatType.label}</div>,
      hideInSearch: true,
    },
    {
      title: t('总数'),
      dataIndex: 'total',
      resizable: true,
      width: 80,
      hideInSearch: true,
    },
    {
      title: t('自动优化'),
      dataIndex: 'handleCount',
      resizable: true,
      width: 90,
      customRender: ({ record }) => <div style='color: #59BF78;'>{record.handleCount}</div>,
      hideInSearch: true,
    },
    {
      title: t('未优化'),
      dataIndex: 'unHandleCount',
      resizable: true,
      width: 80,
      customRender: ({ record }) => <div style='color: #FF5633;'>{record.unHandleCount}</div>,
      hideInSearch: true,
    },
  ];

  return {
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
    typeIsFile,
    myFormRef,
    downloadFile,
  };
};

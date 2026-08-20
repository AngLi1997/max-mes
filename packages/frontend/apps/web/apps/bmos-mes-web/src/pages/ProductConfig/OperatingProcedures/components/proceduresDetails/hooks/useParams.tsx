import { getOperateListCategory } from '@/services';
import { FormProps, Recordable } from '@bmos/components';
import { UploadParams, modalStatus } from '../../../enum';
export const useParams = () => {
  //是否求改表单
  const isForm = ref<boolean>(false);
  //是否上传PDF
  const isBeforeUpload = ref<boolean>(true);
  //当前状态
  const isState = ref<modalStatus>();
  //上级id
  const operateId = ref<string>('');
  //是否提交显示部门管理
  const isModalOpen = ref<boolean>(false);
  //提交参数
  const submitParams = ref<Object>({});
  //是否打开部门管理
  const permissionModalOpen = ref<boolean>(false);
  //表单ref
  const submitForm = ref(null);
  //上级树
  const treeFormProps = ref<Recordable>();
  //禁用状态
  const isDisableds: any[] = [modalStatus.Edit, modalStatus.Copy];
  //查询表单
  const fromProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        field: 'name',
        label: t('文件名称'),
        component: 'Input',
        required: true,
        componentProps: () => {
          return {
            disabled: isDisableds.includes(isState.value),
            onChange: () => {
              isForm.value = true;
            },
          };
        },
      },
      {
        field: 'code',
        label: t('文件编号'),
        component: 'Input',
        required: true,
        componentProps: () => {
          return {
            disabled: isDisableds.includes(isState.value),
            onChange: () => {
              isForm.value = true;
            },
          };
        },
      },
      {
        field: 'categoryId',
        label: t('所属分类'),
        component: 'TreeSelect',
        required: true,
        componentProps: () => {
          return {
            disabled: isDisableds.includes(isState.value),
            // treeData: treeFormProps.value,
            fieldNames: {
              children: 'children',
              label: 'name',
              value: 'id',
            },
            virtual: false,
            height: 200,
            request: async () => {
              const res: any = await getOperateListCategory();
              return res.data;
            },
            onChange: () => {
              isForm.value = true;
            },
          };
        },
      },
      {
        field: 'version',
        label: t('版本号'),
        component: 'Input',
        required: true,
        componentProps: () => {
          return {
            onChange: () => {
              isForm.value = true;
            },
          };
        },
      },
      {
        field: 'fileEffectDate',
        label: t('文件生效日期'),
        component: 'DatePicker',
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
            placeholder: t('请选择日期'),
            onChange: () => {
              isForm.value = true;
            },
          };
        },
      },
      {
        field: 'remark',
        label: t('版本描述'),
        component: 'Input',
        componentProps: () => {
          return {
            onChange: () => {
              isForm.value = true;
            },
          };
        },
      },
    ],
    labelWidth: 80,
    baseColProps: {
      span: 8,
    },
    autoAdvancedLine: 3,
    alwaysShowLines: 3,
    showActionButtonGroup: false,
  });
  //上传文件
  const fileList = reactive<UploadParams>({
    type: '',
    file: '',
    streamingMedia: {},
  });
  return {
    isForm,
    isState,
    isBeforeUpload,
    operateId,
    isModalOpen,
    fileList,
    fromProps,
    treeFormProps,
    submitForm,
    permissionModalOpen,
    submitParams,
  };
};

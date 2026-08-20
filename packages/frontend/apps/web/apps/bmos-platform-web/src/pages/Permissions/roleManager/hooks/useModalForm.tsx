import { getRoleName, postRoleSave, postRoleUpdate } from '@/api/Permissions/roleManagement';
import type { FormProps } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { modalStatus } from '../enum';
export const useModalForm = (useTree: any) => {
  const { treeData, pageRoleManager } = useTree;
  //弹窗开关
  const modalInstance = reactive({
    addStorage: false,
  });
  //弹窗类型
  const formModalType = ref<string>(modalStatus.Add);
  //弹窗初始化数据
  const formDefaultValue = ref<FormProps['initialValues'] | any>({
    roleTypeId: '',
    position: '',
  });
  //弹窗fom表单
  const addFomUse = computed(() => {
    const schemas = [
      {
        field: 'roleTypeId',
        component: 'TreeSelect',
        label: t('分类'),
        required: true,
        componentProps: {
          disabled: formModalType.value == modalStatus.Edit ? true : false,
          treeData: treeData?.value[0]?.children,
          fieldNames: {
            children: 'children',
            label: 'roleTypeName',
            value: 'id',
          },
        },
      },
      {
        field: 'roleName',
        component: 'Input',
        label: t('角色名称'),
        required: true,
      },
      {
        field: 'description',
        component: 'InputTextArea',
        label: t('描述'),
        componentProps: {
          maxlength: 255,
        },
      },
    ];
    return {
      schemas,
      disabled: false,
    };
  });
  //新增数据处理
  const addProcessing = (fromData: any) => {
    let roleTypeId = '';
    roleTypeId = fromData.id === 'all' ? undefined : fromData.id;
    formDefaultValue.value = { roleTypeId: roleTypeId };
    modalInstance.addStorage = true;
  };
  //新增
  const handleModalAdd = async (fromModel: any) => {
    try {
      const res = await postRoleSave(fromModel);
      if (res.code === 0) message.success(t('新增成功'));
      pageRoleManager.value?.fetchData();
      modalInstance.addStorage = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  //编辑
  const handleModalEdit = async (fromModel: any) => {
    try {
      const res = await postRoleUpdate(fromModel);
      if (res.code === 0) message.success(t('编辑成功'));
      pageRoleManager.value?.fetchData();
      modalInstance.addStorage = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  //确认
  const handleModalSubmit = async (fromModel: any) => {
    const res = await getRoleName(fromModel);
    if (res.data) return message.error('角色名称已存在');
    switch (formModalType.value) {
      case modalStatus.Add:
        await handleModalAdd(fromModel);
        break;
      case modalStatus.Edit:
        await handleModalEdit(fromModel);
        break;
      case modalStatus.View:
        modalInstance.addStorage = false;
        break;
    }
  };
  // 查询和编辑数据处理
  const processing = (fromData: any) => {
    formDefaultValue.value = { ...fromData };
    modalInstance.addStorage = true;
  };
  //展示处理
  const storageAdd = (treeNode: any, isType: any) => {
    formModalType.value = isType;
    switch (formModalType.value) {
      case modalStatus.Add:
        addProcessing(treeNode);
        break;
      case modalStatus.Edit:
        //处理数据
        processing(treeNode);
        break;
      case modalStatus.View:
        //显示隐藏
        addFomUse.value.disabled = true;
        //处理数据
        processing(treeNode);
        break;
    }
  };
  return {
    addFomUse,
    formModalType,
    modalInstance,
    formDefaultValue,
    storageAdd,
    handleModalSubmit,
  };
};

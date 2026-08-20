import {
  reqPlatformInfoGET,
  reqPlatformTagDefineListAllTypeIdGET,
  reqPlatformTagInstanceCreatePOST,
  reqPlatformTagInstanceEditPOST,
  reqPlatformTagInstanceInfoGET,
  reqPlatformTagSceneListByTypeIdGET,
} from '@/api';

import { getChinese } from '@/pages/System/tagConfig/utils';

import { FormProps, Recordable, RenderCallbackParams } from '@bmos/components';

import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

import { t } from '@bmos/i18n';

import { message, Modal } from 'ant-design-vue';

import { ActionType, modalStatus, User, UseTableParams } from '../../../types';

import TagForm from '../components/TagForm.vue';

export const useFrom = ({ emits }: UseTableParams) => {
  //REF类型
  const TagQueryForm = ref();
  //REF类型
  const TagInitialStyle = ref();
  //标签类型树
  const tagType = ref<Recordable>();
  //业务场景树
  const businessScenario = ref<object[]>([]);
  //标签样式树
  const tagStyle = ref<any[]>([]);
  //标签样式内容
  const tagStyleContent = ref();
  //标签选择数据
  const valueInfo = ref<object[]>([]);
  //标签列表ID
  const tagListId = ref<string>();
  //标签类型请求
  const tagTypeGET = (tree: Recordable, tagTypeId: string) => {
    tagType.value = tree[0]?.children;

    // TagQueryForm.value?.updateSchema({ field: 'tagTypeId', componentProps: { options: tree[0]?.children } });
    if (tagTypeId) {
      fromProps.initialValues!.tagTypeId = tagTypeId;
      businessScenarioGET(tagTypeId);
    }
  };
  //业务场景请求
  const businessScenarioGET = async (value: string) => {
    try {
      const res = await reqPlatformTagSceneListByTypeIdGET({ typeId: value });
      businessScenario.value = res.data;
      TagQueryForm.value?.updateSchema({ field: 'tagSceneId', componentProps: { options: res.data } });
    } catch (errors) {
      businessScenario.value = [];
    }
  };
  //标签样式请求
  const tagStyleGET = async () => {
    try {
      const res = await reqPlatformTagDefineListAllTypeIdGET();
      tagStyle.value = res.data;
      TagQueryForm.value?.updateSchema({
        field: 'tagDefineId',
        componentProps: { options: res.data },
      });
    } catch (errors) {
      tagStyle.value = [];
    }
  };
  //标签选择数据请求
  const valueInfoGET = async (id: string) => {
    try {
      const res = await reqPlatformInfoGET({ tagSceneId: id });
      valueInfo.value = res.data?.availableFields;
      // TagQueryForm.value?.updateSchema({
      //   field: 'tagDefineId',
      //   componentProps: { options: res.data?.availableFields },
      // });
    } catch (errors) {
      valueInfo.value = [];
    }
  };
  //查询表单
  const fromProps = reactive<FormProps>({
    initialValues: {
      tagTypeId: null,
    },
    schemas: [
      {
        //标签名称
        field: 'tagName',
        label: t('标签名称'),
        component: 'Input',
        required: true,
      },
      {
        //标签类型
        field: 'tagTypeId',
        label: t('标签类型'),
        component: 'Select',
        required: true,
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: tagType,
            fieldNames: {
              label: 'tagTypeName',
              value: 'id',
            },
            onChange: (value: string) => {
              formInstance.setFieldsValue({
                tagSceneId: null,
              });
              if (value) businessScenarioGET(value);
            },
          };
        },
      },
      {
        //业务场景
        field: 'tagSceneId',
        label: t('业务场景'),
        component: 'Select',
        required: true,
        componentProps: {
          options: [],
          fieldNames: {
            label: 'tagSceneName',
            value: 'id',
          },
          onChange: (value: string) => {
            valueInfoGET(value);
          },
        },
      },
      {
        //标签样式
        field: 'tagDefineId',
        label: t('标签样式'),
        component: 'Select',
        required: true,
        componentProps: {
          options: [],
          fieldNames: {
            label: 'tagStyle',
            value: 'id',
          },
        },
      },
    ],
  });

  //标签字段
  const toProps = reactive<FormProps>({
    schemas: [
      {
        field: 'component',
        component: 'Divider',
        label: t('标签字段'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'ValidateForm',
        noLabel: true,
        required: true,
        colProps: {
          span: 24,
        },
        defaultValue: [],
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <TagForm
              selects={formModel.ValidateForm}
              dropInfo={valueInfo.value}
              onUpdate:modify={(modify: User[]) => {
                modifyHtml(modify);
              }}
            />
          );
        },
      },
    ],
  });
  //获取HTML全部ID
  const getAllId = (html: string) => {
    let tempElement = document.createElement('div');
    tempElement.innerHTML = html;
    const elements = tempElement.querySelectorAll('[id]');
    return Array.from(elements)
      .filter(item => item.id.includes('field'))
      .map((element: any) => {
        return { defineField: element.id, label: getChinese(element?.outerText)?.join(''), dataSourceField: null };
      });
  };
  //修改HTML文本
  const modifyHtml = (useFrom: User[]) => {
    useFrom.map((item: any) => {
      let elements = document.getElementById(item.defineField);
      const value: any[] = valueInfo.value.filter((valueInfo: any) => valueInfo.field === item.dataSourceField);
      if (item.defineField === 'title') {
        elements!.innerHTML = `${item?.label ? item.label : ''}`;
      } else {
        elements!.innerHTML = `${item?.label ? item.label : ''}${value.length > 0 ? value[0]?.exampleValue : ''}`;
      }
    });
  };
  const stateType = async (state: modalStatus, row: Recordable) => {
    switch (state) {
      case modalStatus.Add:
        await tagStyleGET();
        // await valueInfoGET(tagStyle.value[0].id);
        tagStyleContent.value = tagStyle.value[0].previewHtml;
        TagQueryForm.value.setFormModel('tagDefineId', tagStyle.value[0].id);
        TagInitialStyle.value.setFormModel('ValidateForm', getAllId(tagStyleContent.value));
        break;
      case modalStatus.Edit:
        await queryDetails(row);
        break;
      case modalStatus.View:
        await queryDetails(row);
        break;
    }
  };
  //查询详情
  const queryDetails = async (query: Recordable) => {
    const { id } = query;
    try {
      const res = await reqPlatformTagInstanceInfoGET({ id });
      const { tagName, tagSceneId, tagTypeId, tagDefineId, previewHtml, configFields, availableFields } = res.data;
      tagListId.value = id;
      await businessScenarioGET(tagTypeId);
      await tagStyleGET();
      valueInfo.value = availableFields;
      tagStyleContent.value = previewHtml;
      TagQueryForm.value.setFormModel('tagDefineId', tagDefineId);
      TagInitialStyle.value.setFormModel('ValidateForm', configFields);
      TagQueryForm.value.setFieldsValue({ tagName, tagSceneId, tagTypeId, tagDefineId });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //新增
  const addTag = async (formList: any) => {
    try {
      const toList = await TagInitialStyle.value.validate();
      const param = {
        ...formList,
        fields: toList?.ValidateForm,
      };
      await reqPlatformTagInstanceCreatePOST(param);
      back();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //编辑
  const editTag = async (formList: any) => {
    try {
      const toList = await TagInitialStyle.value.validate();
      const param = {
        ...formList,
        fields: toList?.ValidateForm,
        id: tagListId.value,
      };
      await reqPlatformTagInstanceEditPOST(param);
      back();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //返回
  const back = () => {
    emits('back');
  };
  const actionType: ActionType = {
    [modalStatus.Add]: addTag,
    [modalStatus.Edit]: editTag,
  };
  const save = async (state: modalStatus) => {
    try {
      const formList = await TagQueryForm.value.validate();
      Modal.confirm({
        title: t('提示'),
        icon: h(ExclamationCircleOutlined),
        content: t('是否保存标签信息？'),
        onOk: () => {
          actionType[state](formList);
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  return {
    fromProps,
    toProps,
    TagQueryForm,
    tagStyleContent,
    TagInitialStyle,
    tagTypeGET,
    back,
    stateType,
    save,
  };
};

import { t } from '@bmos/i18n';
import { useRoute } from 'vue-router';
import { recordItemSingleSave } from '../../../services';

export const useModelForm = (use_Tree: any, useNode: any, editor: any, saveTemplate: Function) => {
  const route = useRoute();
  const { GET_RECORD, SELECTED_KEYS, EXPANDED_KEYS, TREE_SELECT, getTreeData } = use_Tree;
  const FORM_ITEMS = {
    initialValues: {},
    labelWidth: 100,
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('记录项名称'),
        required: true,
      },
      {
        field: 'fileList',
        component: 'Input',
        label: t('上传记录'),
        slot: 'RECORD_UPLOAD',
      },
    ],
  };

  const ADD_RECORD = async (model: any) => {
    const data = {
      ...model.fileList?.pop()?.response,
      ...model,
      recordVersionId: route.params?.record_id,
    };
    await saveTemplate(false, true);
    return recordItemSingleSave(data).then(async (res: any) => {
      if (res.code === 0) {
        SELECTED_KEYS.value = [res.data.itemId];
        EXPANDED_KEYS.value = [route.params?.record_id];
        await getTreeData();
        TREE_SELECT(SELECTED_KEYS.value, {
          node: {
            eventKey: res.data.itemId,
            dataRef: {
              id: res.data.id,
            },
          },
        });
        // 定位到新增的记录项
        const scrollFather = document.getElementsByClassName('template-edit-content');
        const scroll = scrollFather[0].querySelector('.mes-tree') as any;
        const height = scroll.querySelector('.mes-tree-list');
        setTimeout(() => {
          scroll.scrollTop = height?.clientHeight + 50;
        }, 400);
        return Promise.resolve(true);
      }
      return Promise.resolve(false);
    });
  };

  const handleModalSubmit = (model: any) => {
    return ADD_RECORD(model);
  };

  const handleModalCancel = () => {};

  return { FORM_ITEMS, ADD_RECORD, handleModalSubmit, handleModalCancel };
};

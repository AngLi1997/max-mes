import { message } from 'ant-design-vue';
import {
  recordDeleteCategory,
  recordSaveCategory,
  recordUpdateCategory,
} from '../../../services';
import { UseCategoryType } from '../type';

const NEED_ID = ['ADD_CATETORY'];

export type ACTION_TYPE = Record<string, Function>;
export const useCategory = (TreeInit: any): UseCategoryType => {
  const { CURRENT, INIT_TREE_DATA } = TreeInit;
  const DELETE_CATETORY = async (val: any, key?: KEY) => {
    try {
      debugger
      await recordDeleteCategory(val);
      message.success(t('删除成功'))
      return true
    } catch (error: any) {
      message.error(error.message);
      return false;
    }
  };
  const ADD_CATETORY = async (val: any) => {
    try {
      await recordSaveCategory(val);
      message.success(t('新增成功'))
      return true;
    } catch (error: any) {
      message.error(error.message);
      return false;
    }
  };
  const EDIT_CATETORY = async (val: any) => {
    try {
      await recordUpdateCategory(val);
      message.success(t('编辑成功'))
      return true;
    } catch (error: any) {
      message.error(error.message);
      return false;
    }
  };
  const ADD = async (val: any) => {
    return await ADD_CATETORY(val);
  };

  const ACTION: ACTION_TYPE = {
    DELETE_CATETORY,
    ADD_CATETORY,
    EDIT_CATETORY,
    ADD,
  };

  const HANDLE_ACTION = async (type: string, key: KEY) => {
    const res = await ACTION[type](key);
    if (res) INIT_TREE_DATA();
  };

  const MODAL_SUBMIT = async (val: any) => {
    const { action, node } = CURRENT.value;
    const data = { ...val, id: node?.data?.id };

    if (NEED_ID.includes(action.action)) {
      data.parentId = node.data.id;
    }

    const res = await ACTION[action.action](data);
    res && INIT_TREE_DATA();
    return res;
  };

  return { HANDLE_ACTION, MODAL_SUBMIT };
};

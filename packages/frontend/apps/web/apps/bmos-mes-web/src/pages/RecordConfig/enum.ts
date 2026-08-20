export enum formula {
  VERSION,
  EDIT,
  FORMULA,
}

export enum METHOD {
  COPY,
  NEW,
}

export enum STATE {
  EDIT = 1,
  REVIEW,
  CONFIRM,
  INVALID,
}

export const STATE_STATUS = ['', 'primary', 'warning', 'success', ''];

export enum V_OP {
  SHOW = 1,
  HISTORY = 2,
  REVIEW = 3,
  INVALID = 4,
  REVIEW_SCHEDULE = 5,
}

export enum FILE_STAUTS {
  ERROR = 'error',
}

export const MODAL_TITLE: Record<string | number, string> = {
  0: '新增记录',
  1: '新增版本',
};

export const DEFAULT_KEY = 'all';

export const ADD_CATETORY = 'ADD_CATETORY';
export const EDIT_CATETORY = 'EDIT_CATETORY';
export const CATETORY_DELETE = 'DELETE_CATETORY';

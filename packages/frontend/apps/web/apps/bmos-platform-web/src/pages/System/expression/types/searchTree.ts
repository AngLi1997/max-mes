export enum ActionList {
  AddClassification = 'AddClassification',
  EditClassification = 'EditClassification',
  DeleteClassification = 'DeleteClassification',
}
export type ActionListType = keyof typeof ActionList;

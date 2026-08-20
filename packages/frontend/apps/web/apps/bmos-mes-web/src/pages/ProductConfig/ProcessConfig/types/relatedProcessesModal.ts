// 关联的每一项
export interface RelatedItem {
  id?: string;
  relationProcessId: string | undefined;
  materialIds: string[];
}

export interface RelatedForm {
  related: RelatedItem[];
}

import { KEY } from '@/components/Layout/type';

export type nodePropsType = {
  title?: string;
  type: string;
  actived: boolean;
  used?: boolean;
  icon?: string;
  rounded?: boolean;
  bordered?: boolean;
  item: ComponentNode & { canEdit: boolean | undefined };
  nodeKey: KEY;
  componentNumber?: number | string;
};

export type NodeDataType = Pick<nodePropsType, 'title' | 'actived' | 'type' | 'key'> & { data: NodeType };

export interface OptionsType extends Record<string, any> {
  name: string;
  key: string;
  type: string;
}

export interface NodeType extends ComponentNode {
  recordItemId?: KEY;
}

export interface ComponentNode extends API.ListRecordItemResComponentListVo {}

export interface ComponentNodeList {
  basic: ComponentNode[];
  business: ComponentNode[];
}

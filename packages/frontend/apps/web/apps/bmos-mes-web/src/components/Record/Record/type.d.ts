export type Formula = String;

export interface formulaNode {
  id: string;
  node_type: string;
}

export interface configType {
  readonly?: boolean;
}

export interface RecordPropsType {
  formulaId?: string;
  config?: configType;
  activeKeys?: Array<KEY>;
  multiple?: boolean;
  processId?: string;
  processVersion?: string;
  productPlanId?: string;
  node: any;
  index?: number;
  getApi: any;
  params: any;
}

export type emits = ['error', 'rendered', 'node-click', 'update:activeKeys', 'node-dbclick'];

export interface WeighingRequirement {
  id: string;
  productName: string;
  productCode: string;
  batchNo: string;
  productionBom: string;
  planProductionTime: string;
  weighingCenter: string;
  status: 'pending' | 'inProgress' | 'completed' | 'cancelled';
}

export interface WeighingRequirementParams {
  batchNo?: string;
  productName?: string;
  productCode?: string;
  productionBom?: string;
  pageNum?: number;
  pageSize?: number;
}

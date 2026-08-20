import { Obj } from './type';
import {
  BatchRecordsConfigurationEnum,
  BatchRecordsManagementEnum,
  BatchRecordsReviewEnum,
} from './urlEnum/BatchRecords';
import {
  BatchReleaseConfigurationEnum,
  BatchReleaseManagementEnum,
  BatchReleaseReviewEnum,
} from './urlEnum/BatchRelease';
import BatchReleaseAbstract from './urlEnum/BatchReleaseAbstract';
import { DataSetManageEnum } from './urlEnum/DataSet';
import { PleaseCheckConfigEnum } from './urlEnum/InspectionManage';
import {
  FormulaApprovalEnum,
  FormulaConfigurationEnum,
  OperatingApprovalEnum,
  PlanTemplateEnum,
  ProcessApprovalEnum,
  RecordReviewEnum,
  WeighingCenterEnum,
} from './urlEnum/ProductConfig';
import {
  MaterialManageEnum,
  PlanCalendarEnum,
  PlanManagementEnum,
  StorageRoomEnum,
  TemporaryStorageManageEnum,
  WeighingRequirements,
  WeighingTaskEnum,
  WeighingWorkOrderPlan,
} from './urlEnum/ProductionManagement';
import AuditEnum from './urlEnum/auditEnum';
import exceptionManagement from './urlEnum/exception';
import instructionConfirmation from './urlEnum/instructionConfirmation';
import instructionListDecomposition from './urlEnum/instructionListDecomposition';
import MaterialHeadersEnum from './urlEnum/materialEnum';
import MaterialTraceabilityConfigurationEnum from './urlEnum/materialTraceabilityConfiguration';
import NoRulesHeadersEnum from './urlEnum/noRules';
import OperatingProcedures from './urlEnum/operatingProcedures';
import PlanApprovalHeadersEnum from './urlEnum/planApproval';
import processConfigEnum from './urlEnum/processConfigEnum';
import ProductHistory from './urlEnum/productHistory';
import ProductionPlanHeadersEnum from './urlEnum/productionPlan';
import RecordHeadersEnum from './urlEnum/recordEnum';
import TareHeadersEnum from './urlEnum/tare';
import teaManagement from './urlEnum/teaManagement';

export const HeadersEnum: Record<string, Record<string, Obj>> = {
  ...RecordHeadersEnum,
  ...processConfigEnum,
  ...MaterialHeadersEnum,
  ...ProductionPlanHeadersEnum,
  ...AuditEnum,
  ...NoRulesHeadersEnum,
  ...instructionConfirmation,
  ...instructionListDecomposition,
  ...teaManagement,
  ...ProductHistory,
  ...TemporaryStorageManageEnum,
  ...StorageRoomEnum,
  ...MaterialManageEnum,
  ...OperatingProcedures,
  ...PlanApprovalHeadersEnum,
  ...DataSetManageEnum,
  ...BatchRecordsConfigurationEnum,
  ...BatchRecordsManagementEnum,
  ...BatchRecordsReviewEnum,
  ...BatchReleaseConfigurationEnum,
  ...BatchReleaseManagementEnum,
  ...BatchReleaseReviewEnum,
  ...exceptionManagement,
  ...BatchReleaseAbstract,
  ...TareHeadersEnum,
  ...MaterialTraceabilityConfigurationEnum,
  ...FormulaConfigurationEnum,
  ...FormulaApprovalEnum,
  ...OperatingApprovalEnum,
  ...RecordReviewEnum,
  ...ProcessApprovalEnum,
  ...PlanTemplateEnum,
  ...WeighingCenterEnum,
  ...WeighingTaskEnum,
  ...PlanManagementEnum,
  ...PlanCalendarEnum,
  ...PleaseCheckConfigEnum,
  ...WeighingRequirements,
  ...WeighingWorkOrderPlan,
};

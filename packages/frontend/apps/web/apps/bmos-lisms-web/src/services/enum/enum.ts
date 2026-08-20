import { Obj } from './type';
import ConfigurationManagementEnum from './urlEnum/configurationManagement'; // 配置管理
import {
  ExecutionManagementEnum,
  InspectionDataAuditEnum,
  TestingDataCenterEnum,
} from './urlEnum/inspectionManagement'; // 检验数据
import LaboratoryResourceEnum from './urlEnum/laboratoryResource';
import MaterialManagementEnum from './urlEnum/materialManagement'; // 物料管理
import ReportManagementEnum from './urlEnum/reportManagement'; // 报告管理
import SpecimenManagementEnum from './urlEnum/specimenManagement'; // 标本管理

export const HeadersEnum: Record<string, Record<string, Obj>> = {
  ...ConfigurationManagementEnum,
  ...MaterialManagementEnum,
  ...ReportManagementEnum,
  ...SpecimenManagementEnum,
  ...LaboratoryResourceEnum,
  // 新增
  ...TestingDataCenterEnum,
  ...InspectionDataAuditEnum,
  ...ExecutionManagementEnum,
};

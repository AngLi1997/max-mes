import CodeRuleEnum from './urlEnum/codeRule';
import DepartmentManagementHeadersEnum from './urlEnum/departmentManagement';

import DictManagementHeadersEnum from './urlEnum/dictManagement';

import formulaConfigurationHeadersEnum from './urlEnum/formulaConfiguration';

import MaterialPlatformHeadersEnum from './urlEnum/materialPlatform';

import MenuPermissionsHeadersEnum from './urlEnum/menuPermissions';

import parameterConfigurationHeadersEnum from './urlEnum/parameterConfiguration';

import PermissionAdmitHeadersEnum from './urlEnum/permissionAdmit';

import RoleManagementHeadersEnum from './urlEnum/roleManagement';

import TagConfig from './urlEnum/tagConfig';

import UnitsManagementHeadersEnum from './urlEnum/unitsManagement';

import UserManagementHeadersEnum from './urlEnum/userManagement';

import DepartmentInsideManagementHeadersEnum from './urlEnum/departmentInsideManagement';

export const HeadersEnum = {
  ...UserManagementHeadersEnum,
  ...MaterialPlatformHeadersEnum,
  ...DepartmentManagementHeadersEnum,
  ...RoleManagementHeadersEnum,
  ...PermissionAdmitHeadersEnum,
  ...MenuPermissionsHeadersEnum,
  ...DepartmentInsideManagementHeadersEnum,
  ...UnitsManagementHeadersEnum,
  ...DictManagementHeadersEnum,
  ...CodeRuleEnum,
  ...parameterConfigurationHeadersEnum,
  ...formulaConfigurationHeadersEnum,
  ...TagConfig,
};

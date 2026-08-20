import { Obj } from './type';
import TestArticleEnum from './urlEnum/testArticleEnum'
import AnalysisItemEnum from './urlEnum/analysisItemEnum'
import InspectionItemEnum from './urlEnum/inspectionItemEnum'
import ExperimentalPackageEnum from './urlEnum/experimentalPackage';
import InspectionManageEnum from './urlEnum/inspectionManageEnum'

export const HeadersEnum: Record<string, Record<string, Obj>> = {
  ...TestArticleEnum,
  ...AnalysisItemEnum,
  ...InspectionItemEnum,
  ...ExperimentalPackageEnum,
  ...InspectionManageEnum,
};

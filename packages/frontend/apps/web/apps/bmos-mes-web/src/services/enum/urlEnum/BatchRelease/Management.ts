import { OperationType } from '../../const';
import { log } from '../../type';
// 批签发管理
const BatchReleaseManagementEnum: Record<string, log> = {
  '120040003': {
    '/app/mes/lotRelease/manage/generate': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.add,
          business: data.again ? '重新生成' : '生成批签发',
        };
      },
    },
    '/app/mes/lotRelease/manage/scrap': {
      type: OperationType.edit,
      business: '作废',
    },
    '/app/mes/lotRelease/manage/submit': {
      type: OperationType.audit,
      business: '提交审核',
    },
    '/app/mes/lotRelease/manage/download': {
      type: OperationType.export,
      business: '下载批签发',
    },
    '/app/mes/lotRelease/manage/updateExcelFile': {
      type: OperationType.edit,
      business: '上传',
    },
    '/app/mes/lotRelease/manage/downloadByUrl': {
      type: OperationType.export,
      business: '下载',
    },
  },
};

export { BatchReleaseManagementEnum };

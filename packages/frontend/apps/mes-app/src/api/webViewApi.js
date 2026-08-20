import request from '@/utils/request/request.js';

// 获取html内容
export const getHtmlApi = params =>
  request.get('/api/app/mes/record/query/record/item', params);

// 获取组件配置项
export const getComponentsApi = params =>
  request.get('/api/app/mes/procedure/step/record/item', params);

// 获取所有组件的值
export const getRecordDataApi = params =>
  request.get('/api/app/mes/execute/item/latest/data', params);

// 查询组件历史值
export const getFieldDataListApi = params =>
  request.get('/api/app/mes/execute/field/data/list', params);

// 修订记录项的数据
export const postModifyExecuteDataApi = data =>
  request.post('/api/app/mes/execute/modify', data);

// 修改记录项的数据
export const postUpdateExecuteDataApi = data =>
  request.post('/api/app/mes/execute/update', data);

// 保存一页记录的数据
export const saveRecordDataApi = data =>
  request.post('/api/app/mes/execute/batch/save', data);

// 保存单个组件的值
export const postSaveFieldDataApi = data =>
  request.post('/api/app/mes/execute/save', data);

// 复制记录项
export const postCopyRecordItemApi = data =>
  request.post('/api/app/mes/execute/copy/recordItem', data);

// 作废记录项
export const postDiscardRecordItemApi = data =>
  request.put('/api/app/mes/execute/discard', data);

// 查询复制的记录项
export const getCopyRecordItemListApi = params =>
  request.get('/api/app/mes/execute/copyVersion/list', params);

// 锁定步骤
export const postLockStepApi = data =>
  request.put('/api/app/mes/execute/lock/step', data);

// 解锁步骤
export const postUnLockStepApi = data =>
  request.put('/api/app/mes/execute/unLock/step', data);

// 当前步骤的执行班组下的人员
export const getStepGroupUserApi = params =>
  request.get('/api/app/mes/procedure/step/group/users', params);

// 完成当前任务
export const postCompleteStepApi = data =>
  request.post('/api/app/mes/flow/complete/task', data);

// 完成执行实例任务
export const postCompleteExecuteApi = data =>
  request.post('/api/app/mes/flow/complete/execution', data);

// 查询记录项附件
export const getRecordItemFileApi = params =>
  request.get('/api/app/mes/execute/attachment/list', params);

// 上传记录项附件
export const postUploadRecordItemFileApi = data =>
  request.post('/api/app/mes/execute/attachment/upload', data);

// 获取服务器时间
export const getServerTimeApi = () =>
  request.get('/api/app/mes/execute/server/time');

// 业务组件数据批量保存
export const postBatchSaveBusinessApi = data =>
  request.post('/api/app/mes/execute/business/save/batch', data);

// 校验是否触发过业务组件
export const getBusinessTriggerApi = params =>
  request.get('/api/app/mes/execute/business/saved', params);

// 激活任务/步骤 /api/app/mes/flow/active/step
export const reqActiveStepApi = data =>
  request.post('/api/app/mes/flow/active/step', data);

// 计算时间差 /api/app/mes/execute/calculate/date
export const reqCalculateDateApi = params =>
  request.get('/api/app/mes/execute/calculate/date', params);

// 查询拍照组件记录
export const getPhotoHisListApi = params =>
  request.get('/api/app/mes/execute/picture/list', params);

// 查询字典下拉框 /api/app/mes/platform/query/list/dict/down
export const reqDictDownApi = params =>
  request.get('/api/app/mes/platform/query/list/dict/down', params);

// 查看工序、工艺 /api/app/mes/execute/procedure/view
export const reqProcedureViewApi = params =>
  request.get('/api/app/mes/execute/procedure/view', params);

// 查看工序:工步存在的班次列表 /api/app/mes/execute/stepVersionList
export const reqStepVersionListApi = params =>
  request.get('/api/app/mes/execute/stepVersionList', params);

// 查询关联工艺树
export const getProcessListTree = params =>
  request.get('/api/app/mes/process/list/tree', params);

// 公式试算 /api/app/mes/execute/calculation/preview
export const reqFormulaCalculateApi = data =>
  request.post('/api/app/mes/execute/calculation/preview', data);

// 获取组件实例详情 // /api/app/mes/components/getInstanceByProps
export const getInstanceByProps = data =>
  request.post('/api/app/mes/components/getInstanceByProps', data);

// 根据组件实例id获取工位信息 ///api/app/mes/station/getStationIdListByComponentInstanceId
export const getStationIdListByComponentInstanceIdApi = params =>
  request.get('/api/app/mes/station/getStationIdListByComponentInstanceId', params);

// 获取所有的单位列表 /api/app/platform/unit/getAllUnit
export const getAllUnitApi = () =>
  request.get('/api/app/platform/unit/getAllUnit');

// 拍照取证附件添加备注
export const executeAttachmentAddRemark = data =>
  request.post('/api/app/mes/execute/attachment/addRemark', data);

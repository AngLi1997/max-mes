import request from '@/utils/request/request.js';

// 根据组件实例id获取物料投入列表
export const getInputListList = params =>
  request.get('/api/app/mes/weigh/centre/input/getInputList', params);

// 投料
export const inputWeighCentre = data =>
  request.post('/api/app/mes/weigh/centre/input/input', data, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料投入',
    },
  });

// 扫描物料件号查询物料件信息（校验配料单信息）
export const weighFinishInput = data =>
  request.post(`/api/app/mes/weigh/centre/input/finishInput?componentInstanceId=${data.componentInstanceId}`, {});

// 获取配液投入组件实例(获取当前组件绑定的配液单)
export const getInputInstance = params =>
  request.get('/api/app/mes/mobile/preparation/input/instance', params);

// 获取未投入的配液单列表
export const queryPendingInputPlanList = params =>
  request.get('/api/app/mes/mobile/preparation/input/queryPendingInputPlanList', params);

// 绑定配液单
export const preparationInputBind = data =>
  request.post('/api/app/mes/mobile/preparation/input/bind', data);

// 根据配液投入组件实例id查询当前配液投入组件绑定的配液单下的投料列表
export const getQueryInputList = params =>
  request.get('/api/app/mes/mobile/preparation/input/queryInputList', params);

// 扫描配液投入确认的物料件信息(附带校验)
export const scanPreparationInputMaterial = data =>
  request.get('/api/app/mes/tag/scan/scanPreparationInputMaterial', data);

// 扫描配液投入确认的设备信息(附带校验)
export const preparationInputContainerCode = data =>
  request.get('/api/app/mes/tag/scan/preparationInputContainerCode', data);

// 进行配液投入（回填批记录
export const preparationInputOperate = data =>
  request.post('/api/app/mes/mobile/preparation/input/operate', data, {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '配液投入',
    },
  });

// 完成配液投入
export const preparationInputComplete = data =>
  request.post('/api/app/mes/mobile/preparation/input/complete', data);

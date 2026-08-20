import { OperationType } from '../const';
import { log } from '../type';

const MaterialHeadersEnum: Record<string, log> = {
  '120010001': {
    '/app/mes/product/material/category/save': {
      type: OperationType.add,
      business: '新增原辅包分类',
    },
    '/app/mes/product/material/category/update': {
      type: OperationType.edit,
      business: '编辑原辅包分类',
    },
    '/app/mes/product/material/category/delete': {
      type: OperationType.delete,
      business: '删除原辅包分类',
    },
    '/app/mes/product/material/save': {
      type: OperationType.add,
      business: '新增原辅包',
    },
    '/app/mes/product/material/update': {
      type: OperationType.edit,
      business: '编辑原辅包',
    },
    '/app/mes/product/material/changeStatus': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.status ? '启用原辅包' : '停用原辅包',
        };
      },
    },
    '/app/mes/product/material/delete': {
      type: OperationType.delete,
      business: '删除原辅包',
    },
    '/app/mes/product/material/sync': {
      type: OperationType.add,
      business: '同步原辅包',
    },
  },
  '120010002': {
    '/app/mes/product/material/category/save': {
      type: OperationType.add,
      business: '新增中间品分类',
    },
    '/app/mes/product/material/category/update': {
      type: OperationType.edit,
      business: '编辑中间品分类',
    },
    '/app/mes/product/material/category/delete': {
      type: OperationType.delete,
      business: '删除中间品分类',
    },
    '/app/mes/product/material/save': {
      type: OperationType.add,
      business: '新增中间品',
    },
    '/app/mes/product/material/update': {
      type: OperationType.edit,
      business: '编辑中间品',
    },
    '/app/mes/product/material/changeStatus': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.status ? '启用中间品' : '停用中间品',
        };
      },
    },
    '/app/mes/product/material/delete': {
      type: OperationType.delete,
      business: '删除中间品',
    },
    '/app/mes/product/material/sync': {
      type: OperationType.add,
      business: '同步中间品',
    },
  },
  '120010003': {
    '/app/mes/product/material/category/save': {
      type: OperationType.add,
      business: '新增产品分类',
    },
    '/app/mes/product/material/category/update': {
      type: OperationType.edit,
      business: '编辑产品分类',
    },
    '/app/mes/product/material/category/delete': {
      type: OperationType.delete,
      business: '删除产品分类',
    },
    '/app/mes/product/material/save': {
      type: OperationType.add,
      business: '新增产品',
    },
    '/app/mes/product/material/update': {
      type: OperationType.edit,
      business: '编辑产品',
    },
    '/app/mes/product/material/changeStatus': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.status ? '启用产品' : '停用产品',
        };
      },
    },
    '/app/mes/product/material/delete': {
      type: OperationType.delete,
      business: '删除产品',
    },
    '/app/mes/product/material/save/batchRecord': {
      type: OperationType.relevance,
      business: '产品绑定记录',
    },
    '/app/mes/product/material/sync': {
      type: OperationType.add,
      business: '同步产品',
    },
  },
};

export default MaterialHeadersEnum;

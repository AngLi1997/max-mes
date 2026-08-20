const { workerData, parentPort } = require('worker_threads')
const { Project } = require('ts-morph');
const { cacheMap,schemasList,keys,pathList } = workerData

// import {
//   accessPath,
//   getNameByPath,
//   handleZH,
//   objectEach,
//   objectKeys,
//   Uppercase,
// } from './utils';
const REGEX = /Result«(?:List«)?(.*?)»/;
let TYPE_START = '{\n';
let TYPE_END = '}\n';
const GET = 'get';
const ARRAY = 'array';
const BODY_KEY = 'application/json';
const INTERFACE = 'interface ';
const TYPE = 'type ';
const INTEGER = 'integer';
const STRING = 'string';
const NUMBER = 'number';
const BASE_TYPE = [
  'boolean',
  'Boolean',
  'string',
  'String',
  'number',
  'Number',
  'object',
  'Object',
  'array',
  'Array',
  'string,int',
];
const REGE = /.*(Id|id)$/;
let writer

 const accessPath = path => {
  if (path === void 0) throw 'the parameter must be a String';
  if (path === '') return path;
  path = decodeURIComponent(path);
  const keys = path.split('/');

  return keys.pop();
};

const Uppercase = (str)=>{
  if(!str)return ''
  return str.charAt(0).toUpperCase() + str.slice(1);
}

const capitalizeFirstLetter = string => {
  if(!string)return string
  return string.charAt(0).toUpperCase() + string.slice(1);
};

const handleString = (str)=>{
  const strs = str.trim().split('-')
  if(strs.length===1)return capitalizeFirstLetter(strs[0])
  return strs.reduce((prev,cur)=>{
    return prev + capitalizeFirstLetter(cur)
  },'')
}

const handleZH = (str) => {
  str = str.split('«').pop() || ''
  return str.replace(
    /[\u4e00-\u9fa5\u3000-\u303f\uff00-\uff0f\uff1a\uff1b-\uff20\uff3b-\uff40\uff5b-\uff65\uffe0-\uffee！？。，：:]/g,
    '',
  );
};

const objectKeys = obj => {
  if (!obj || JSON.stringify(obj) === '{}') return [];
  return Object.keys(obj);
};

const objectEach = (obj, iterator) => {
  for (const [key, val] of Object.entries(obj)) {
    if (iterator.call(obj, val, key) === false) break;
  }
};

 const getNameByPath = (
  path,
  num = 3,
  REGEX= /[{}]/g,
) => {
  if (!path) throw 'the path is required';

  const num_1 = (path.match(/{/g) || []).length;
  if (num_1 > num) num = num_1 + 1;

  //处理参数位置
  if (num_1 > 0 && path[path.length - 1] !== '}') path = handlePath(path);

  path = path.replace(REGEX, '');
  if (!path) return '';

  let paths = path.split('/');
  let name = '';

  if (paths.length > num) {
    paths = paths.slice(0 - num);
  }

  name = paths.reduce((prev, cur, index) => {
    cur = handleString(cur)
    if (paths.length === index + num_1) {
      return prev + 'By' + cur;
    }

    return prev + cur;
  }, name);

  return name;
};

/**
 * 处理特殊类型情况
 * @param type 类型
 * @returns
 */
const vertifyType = (type, k) => {
  if (REGE.test(k)) return STRING;
  if (type === INTEGER) return NUMBER;
  return type;
};

/**
 * 通过key获取数据
 * @param key
 * @returns
 */
const findSchemaByKey = key => {
  if (!key === undefined) return;
  if (Reflect.has(schemasList, key)) return schemasList[key];
  return;
};

/**
 * 生成注释
 * @param content 注释内容
 * @returns
 */
const generateComment = content => {
  if (!writer) return;
  writer.writeLine('/*');
  writer.writeLine('* ' + content || '注释');
  writer.writeLine('*/');
};


/**
 * 类型生成
 * @param schemakey 内容对象
 * @param comment 注释
 * @returns
 */
const generateType = (schemakey, comment = '注释') => {
  if (!schemakey) return '';

  let key = '';
  const formatKey = REGEX.exec(schemakey.name);
  if (formatKey) {
    key = handleZH(formatKey[1]);
  }
  if (!key) key = handleZH(schemakey.name);
  // 判断是否是基本类型
  if (BASE_TYPE.includes(key)) return;
  // 是否有缓存
  if (cacheMap.has(key.trim())) return;
  // console.log(key, cacheMap.has(key), 'key');

  // 根据key获取schema
  const {
    type,
    properties = {},
    required,
  } = findSchemaByKey(schemakey.key) || {};
  const schemakeys = [];

  // console.log('name',name,key,schemakey,'key');
  // 注释
  generateComment(comment);
  // interface
  writer.writeLine(INTERFACE + key + TYPE_START);

  const pros = Object.keys(properties);

  for (let index = 0; index < pros.length; index++) {
    const k = pros[index];
    const v = properties[k];
    const IS_REQUIRED = required ? required.includes(k) : true;
    let keyType = vertifyType(v.type, k) + ';';
    if (v.type === ARRAY) {
      if (v.items.type !== void 0) {
        keyType = 'Array<' + v.items.type + '>;';
      } else {
        // 列表元素key
        let schema_key = accessPath(v.items.$ref || '');
        // 列表元素名称
        const name = key + Uppercase(k) + 'Vo';
        schemakeys.push({ key: schema_key, name });
        keyType = 'Array<' + handleZH(name) + '>;';
      }
    }
    generateComment(v.description || '');
    writer.writeLine(k + (IS_REQUIRED ? ':' : '?:') + keyType);
  }

  writer.writeLine(TYPE_END);
  cacheMap.add(key.trim());

  if (schemakeys.length > 0) {
    for (let i = 0; i < schemakeys.length; i++) {
      const item = schemakeys[i];
      generateType(item, item.key);
    }
  }
};

/**
 * GET parameter处理
 * @param {*} val
 * @param {*} writer
 * @returns
 */
const generateparamType = (val, k = '注释') => {
  const { parameters = [], operationId = '' } = val || {};
  generateComment(k + ' request');
  // writer.writeLine(INTERFACE + capitalizeFirstLetter(operationId) + TYPE_START);
  writer.writeLine(INTERFACE + getNameByPath(k) + 'Req' + TYPE_START);

  if (parameters.length !== 0) {
    for (const item of parameters) {
      generateComment(item?.description || '');
      const isRequired = item.required ? '' : '?';
      const property = item.name + isRequired + ':';
      writer.writeLine(
        property + vertifyType(item.schema.type, item.name) + ';',
      );
    }
  }

  writer.writeLine(TYPE_END);
};

/**
 * POST请求
 * @param {*} val
 * @param {*} method
 * @returns
 */
const generateReqType = (val, method, k) => {
  if (!val) return;
  if (method === GET) {
    generateparamType(val[method], k);
  } else {
    const { content = {} } = val[method]?.requestBody || {};
    const refkey = content[BODY_KEY]?.schema?.$ref || '';
    // 获取schema对应key
    const schemakey = accessPath(refkey);
    generateType(
      { key: schemakey, name: getNameByPath(k) + 'Req' },
      k + ' response',
    );
  }

  generateResType(val, method, k);
};

/**
 * 响应数据
 * @param {*} val
 * @param {*} method
 */
const generateResType = async (val, method, k) => {
  if (!val) return;
  const { responses } = val[method] || {};
  const { content } = responses['200'] || {};
  const schema = content[BODY_KEY]?.schema || {};

  // 获取schema对应key
  const schemakey = accessPath(schema['$ref'] || '');
  // 根据路径生成名称
  const name = getNameByPath(k) + 'Res';

  if (!schemakey) {
    if (cacheMap.has(name)) return;

    if (JSON.stringify(schema.properties || {}) !== '{}') {
      writer.writeLine(`${TYPE}${name} = {`);

      objectEach(schema.properties, (v, k) => {
        writer.writeLine(`${v}:${v.type} `);
      });
      writer.writeLine('}');
    } else {
      writer.writeLine(`${TYPE}${name} = ${schema.type} `);
    }

    cacheMap.add(name);
    return;
  }
  generateType({ key: schemakey, name }, k + ' response');
};


const start = ()=>{
  const project = new Project();
  writer = project.createWriter();
  for (const k of keys) {
    const v = pathList[k]
    const methods = objectKeys(v);
    const method = methods.length === 1 ? methods[0] : void 0;
    if (method !== void 0) {
      // 生成请求数据类型
      generateReqType(v, method, k);
      // 生成响应数据类型
      // generateResType(v, method, k);
    }
  }
  return { length: keys.length,content: writer.toString() }
}
parentPort.postMessage(start())
// import axios from '@bmos/axios';
const axios = require('axios');
const path = require('path');
const root = path.resolve(__dirname, '..', '..');
const config = require(path.resolve(root, 'bmosapi.json'));
const { createFileContent } = require('./project');
// const PATH_SRC = '/openApi';
// const FILE_NAME = 'typings.d.ts';

// const { Worker } = require('worker_threads');
// import { getNameByPath } from './utils';
// const TSCONFIG_PATH = path.resolve(root, 'tsconfig.src.json');
let afterHandle = [];
const refMap = new Map();

const capitalizeFirstLetter = string => {
  if (!string) return string;
  return string.charAt(0).toUpperCase() + string.slice(1);
};

const handleString = str => {
  const strs = str.trim().split('-');
  if (strs.length === 1) return capitalizeFirstLetter(strs[0]);
  return strs.reduce((prev, cur) => {
    return prev + capitalizeFirstLetter(cur);
  }, '');
};

// 通过路径获取名称
const getNameByPath = (path, num = 3, REGEX = /[{}]/g) => {
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
    cur = handleString(cur);
    if (paths.length === index + num_1) {
      return prev + 'By' + cur;
    }

    return prev + cur;
  }, name);

  return name;
};

const NAME_SPACE = 'declare namespace API {\n';

let TYPE_END = '}\n';

let schemasList;

const getRef = refStr => {
  if (!refStr || typeof refStr !== 'string') return '';
  const $ref = refStr.split('/').pop();
  if (!$ref) return '';
  const ref = decodeURIComponent($ref);
  return ref.trim();
};

const handleType = type => {
  return type === 'integer' ? 'number' : type;
};

const getPropByRef = (ref, parent) => {
  if (!ref) return;
  if (!schemasList[ref]) return [];
  const { required, properties = {} } = schemasList[ref] || {};
  if (!properties) return;
  const schemas = [];
  try {
    Object.keys(properties)?.forEach(item => {
      if (!item) return;
      const property = properties[item];
      if (!property) return;
      const proty = {
        name: item,
        description: property.description,
        type: handleType(property?.type) || 'string',
      };
      required ? (proty['required'] = required.includes(item)) : null;
      if (property?.type === 'array') {
        const items = property.items || {};
        const _ref = getRef(items['$ref']);
        if (!_ref) {
          proty['type'] = `Array<${handleType(items.type)}>`;
        } else {
          if (refMap.has(_ref)) {
            proty['type'] = `Array<${refMap.get(_ref)}>`;
          } else {
            const propertyVoName =
              parent.name + capitalizeFirstLetter(item) + 'Vo';
            proty['type'] = `Array<${propertyVoName}>`;
            afterHandle.push({
              name: propertyVoName,
              ref: _ref,
              description: propertyVoName,
            });
          }
        }
      }
      schemas.push(proty);
    });
  } catch (error) {
    console.log(error, 'ggggg');
  } finally {
    return schemas;
  }
};

const createParameters = (parameters, parent) => {
  if (parameters.length === 2) return [];
  const params = parameters.slice(0, parameters.length - 2);
  const interface = {
    name: parent.name + 'Req',
    description: parent.description,
    summary: parent.summary,
    properties: [],
  };
  interface.properties = params.map(item => {
    const { name, description, required, schema } = item;
    return {
      name,
      description,
      required,
      type: handleType(schema?.type) || 'string',
    };
  });
  return interface
};

const createBody = (requestBody, parent) => {
  if (!requestBody) return;
  const { schema } = requestBody.content?.['application/json'] || {};
  if (!schema) return;
  const inName = parent.name + 'Req'
  const interface = {
    name: inName,
    description: parent.description,
    summary: parent.summary,
    properties: null,
    type: '',
  };

  if (schema?.type) {
    interface.type = schema.type;

    if (schema?.type === 'array') {
      const ref = getRef(schema.items['$ref']);
      if (!ref) {
        interface.type = `Array<${handleType(schema.type)}>`;
      } else {
        if (refMap.has(ref)) {
          interface.type = `Array<${refMap.get(ref)}>`;
          return;
        }
        const nameVo = inName + 'Vo';
        interface.type = `Array<${nameVo}>`;
        afterHandle.push({
          name: nameVo,
          ref: ref,
          description: nameVo,
        });
      }
    }
  }

  if (schema['$ref']) {
    const ref = getRef(schema['$ref']);
    if (refMap.has(ref)) {
      interface['type'] = refMap.get(ref);
    } else {
      interface['properties'] =
        getPropByRef(ref, { ...parent, name: inName }) || [];
      refMap.set(ref, inName);
    }
  }

  return interface;
};

const createResponse = (path, parent) => {
  const app = path.content?.['application/json'] || {};
  const ref = getRef(app.schema?.['$ref']);
  if (!schemasList[ref]) return;
  const sche = schemasList[ref];
  const res = sche.properties;
  const interfaceName = parent.name + 'Res';
  if (res?.data?.type !== 'object' && res?.data?.type !== 'array') {
    return {
      name: interfaceName,
      description: res.data?.description,
      type: handleType(res?.data?.type) || 'string',
    };
  }

  const ref_1 = getRef(res.data.items?.['$ref']);
  const hasMap = refMap.has(ref_1);
  if (hasMap) {
    return {
      name: interfaceName,
      description: res?.data.description,
      type: refMap.get(ref_1),
    };
  }
  const propers = getPropByRef(ref_1, { ...parent, name: interfaceName });
  refMap.set(ref_1, interfaceName);
  return {
    name: interfaceName,
    description: res?.data.description,
    type: res?.data.type,
    properties: propers,
  };
};

const handleafterHandle = arr => {
  afterHandle = [];
  afterHandle.length = 0;
  return arr.map(item => {
    const defaultObj = {
      name: item.name,
      description: item.description,
      type: 'object',
    };

    if (refMap.has(item.ref)) {
      return {
        ...defaultObj,
        type: refMap.get(item.ref),
      };
    }
    const propers = getPropByRef(item.ref, item);
    refMap.set(item.ref, item.name);
    return {
      ...defaultObj,
      properties: propers,
    };
  });
};

const handlePathsJson = paths => {
  if (!paths) return;

  const keys = Object.keys(paths);
  const mapp = [];
  let index = 0;
  while (index < keys.length) {
    const path = keys[index];
    const element = paths[path];

    if (element) {
      Object.keys(element).forEach(method => {
        const name = getNameByPath(path);
        const operation = element[method];
        const parameters = operation.parameters || [];
        const requestBody = operation.requestBody || {};
        const responses = operation.responses || {};
        const parent = {
          name,
          description: path,
          summary: operation.summary,
        };
        let params;
        if (method === 'get') {
          console.log(parameters,'parameters');
          params = createParameters(parameters, parent);
        } else {
          params = createBody(requestBody, parent);
        }
        const response = createResponse(responses['200'], parent);
        mapp.push(params, response);
      });
    }
    index++;
  }
  while (afterHandle.length > 0) {
    const handles = handleafterHandle([...afterHandle]);
    mapp.push(...handles);
  }
  // console.log(mapp,'mapp');
  return mapp.filter(item => item);
};

/**
 * 获取api文档
 * @param path  文档地址
 */
const getAPI_Doc = async path => {
  try {
    const { data, status } = await axios.get(path);

    if (status !== 200 || !data) {
      throw 'The Write Operation Failed';
    }
    const { components, info, paths, tags } = data;
    schemasList = components.schemas;
    pathList = handlePathsJson(paths);
    dirList = tags;
    API_info = info;
    createFileContent(pathList);
  } catch (error) {
    throw error;
  }
};

// 类型生成入口
const createFileSource = async () => {};

/**
 * 入口
 */
const startApi = async () => {
  if (!config.target) {
    throw 'the attribute of target is required';
  }

  // 创建ts项目实例

  //获取api文档
  await getAPI_Doc(config.target);
  // // 创建write实例
  // writer = project.createWriter();
  // if (!writer) throw 'error';
  // // 生成namespace API
  // writer.write(NAME_SPACE);
  // // 生成内容
  // await createFileSource();
  // // 生成namespace闭合标签
  // writer.write(TYPE_END);
  // //创建类型文件
  // const sourceFile = project.createSourceFile(file, writer.toString());
  // // 格式化类型文件
  // sourceFile.formatText();
  // // await project.save();
  // // 本地生成文件
  // fs.writeFileSync(file, sourceFile.getText());
  // // 清除缓存
  // cacheMap.clear();
  // schemasList.clear()
};

// export default startApi;
exports.startApi = startApi;

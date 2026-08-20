const { Project, VariableDeclarationKind, ts } = require('ts-morph');
const fs = require('node:fs');
const path = require('path');
const root = path.resolve(__dirname, '..', '..');
const TSCONFIG_PATH = path.resolve(root, 'tsconfig.src.json');
const config = require(path.resolve(root, 'bmosapi.json'));
const PATH_SRC = '/openApi';
const FILE_NAME = 'typings.d.ts';
const { deleteFile } = require('./file');

const createBody = () => {};

const createParameters = () => {};

const createResponses = () => {};

const createFileContent = async objMap => {
  if (!objMap) return;
  // // 文件名称
  const filename = config.filename ? config.filename : FILE_NAME;
  const src_path = config.path ? config.path : PATH_SRC;
  // // 绝对路径
  const file = path.resolve(root, src_path + '/' + filename);
  await deleteFile(file);
  const project = new Project({
    tsConfigFilePath: TSCONFIG_PATH,
  });
  let sourceFile = project.createSourceFile(file);
  sourceFile.addStatements(`
      declare namespace API {
        interface Data<T>{
          code:number;
          data:T;
          message:string;
        }
      }
    `);

  // 获取命名空间声明
  const namespaceDeclaration = sourceFile.getFirstDescendantByKind(
    ts.SyntaxKind.ModuleDeclaration,
  );

  let addInstance;

  objMap.forEach(element => {
    if (!element.properties) {
      if (!element.type || !element.name) return;
      addInstance = namespaceDeclaration.addTypeAlias({
        name: element.name,
        type: element.type,
        docs: [element.description || ''],
        hasQuestionToken: element.required ? false : true,
      });
    } else {
      addInstance = namespaceDeclaration.addInterface({
        name: element.name,
        docs: [element.description || ''],
        properties:
          element.properties?.map(item => {
            return {
              name: item.name,
              type: item.type,
              docs: [item?.description || ''],
              hasQuestionToken: item.required ? false : true,
            };
          }) || [],
      });
    }
  });
  sourceFile.formatText();
  sourceFile.saveSync();
};

exports.createFileContent = createFileContent;

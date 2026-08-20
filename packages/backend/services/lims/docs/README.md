# BMOS LIMS2 文档目录

## 📐 架构文档 (architecture/)

- [CLAUDE.md](architecture/CLAUDE.md) - 开发指南和代码规范
- [ELN_LIMS_INTEGRATION.md](architecture/ELN_LIMS_INTEGRATION.md) - ELN与LIMS数据交互关系
- [DIRECTORY_OPTIMIZATION_PROPOSAL.md](architecture/DIRECTORY_OPTIMIZATION_PROPOSAL.md) - 目录结构优化方案

## 🔌 API文档 (api/)

- [API_DOCUMENTATION.md](api/API_DOCUMENTATION.md) - API接口文档
- [INSPECTION_SCHEME_DROPDOWN_API.md](api/INSPECTION_SCHEME_DROPDOWN_API.md) - 检验方案下拉API

## 💻 实现文档 (implementation/)

- [INSPECTION_ENTRY_IMPLEMENTATION.md](implementation/INSPECTION_ENTRY_IMPLEMENTATION.md) - 检验录入实现说明
- [ANALYSIS_ITEM_HIERARCHY_LIST_IMPLEMENTATION.md](implementation/ANALYSIS_ITEM_HIERARCHY_LIST_IMPLEMENTATION.md) - 分析项层级列表实现
- [ASSIGNABLE_USERS_QUERY_IMPLEMENTATION.md](implementation/ASSIGNABLE_USERS_QUERY_IMPLEMENTATION.md) - 可分配用户查询实现
- [MAPPER_REFACTOR_SUMMARY.md](implementation/MAPPER_REFACTOR_SUMMARY.md) - Mapper重构总结

## 🔧 技术文档 (technical/)

- [PDF_IMPLEMENTATION_GUIDE.md](technical/PDF_IMPLEMENTATION_GUIDE.md) - PDF生成实现指南
- [PDF_DEPENDENCIES.md](technical/PDF_DEPENDENCIES.md) - PDF相关依赖说明
- [PLATFORM_CODE_RULE_INTEGRATION.md](technical/PLATFORM_CODE_RULE_INTEGRATION.md) - 平台编码规则集成
- [INSPECTION_EXECUTION_LIST.md](technical/INSPECTION_EXECUTION_LIST.md) - 检验执行列表
- [INSPECTION_SCHEME_SNAPSHOT.md](technical/INSPECTION_SCHEME_SNAPSHOT.md) - 检验方案快照
- [RECORD_MODULE_SPEC.md](technical/RECORD_MODULE_SPEC.md) - 记录模块规范

---

## 文档规范

### 文档分类说明

- **architecture/**: 系统架构、设计模式、开发规范
- **api/**: REST API接口文档、接口设计说明
- **implementation/**: 具体功能实现说明、实现细节
- **technical/**: 技术方案、集成指南、技术选型

### 文档命名规范

- 使用大写字母和下划线：`FEATURE_NAME.md`
- 描述性命名，见名知意
- 避免使用缩写（除非是公认的技术术语）

### 文档更新规范

- 新增文档后及时更新本索引
- 文档内容变更时更新修改日期
- 废弃文档移至 `archive/` 目录

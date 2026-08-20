# 通过检品ID查询检验方案下拉数据功能实现

## 功能描述
实现了通过检品ID查询检验方案下拉数据的功能。该功能可以根据指定的检品ID，查询出所有关联的检验方案，并返回适合下拉选择器使用的数据格式。

## 实现的文件和功能

### 1. 新增DTO
**文件**: `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/scheme/dto/response/InspectionSchemeDropdownDTO.java`

下拉数据专用DTO，包含以下字段：
- `id`: 检验方案ID
- `name`: 检验方案名称
- `code`: 方案编码
- `activeVersionNo`: 当前生效版本号
- `activeVersionId`: 当前生效版本ID
- `materialId`: 物料ID
- `materialCode`: 物料编码
- `materialName`: 物料名称
- `packageId`: 实验包ID
- `packageCode`: 实验包编码
- `packageName`: 实验包名称
- `displayName`: 显示名称（方案名称 - 版本号）

### 2. 服务接口扩展
**文件**: `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/scheme/service/InspectionSchemeService.java`

新增方法：
```java
/**
 * 通过检品ID查询检验方案下拉数据
 *
 * @param materialId 检品ID
 * @return 检验方案下拉数据列表
 */
List<InspectionSchemeDropdownDTO> getInspectionSchemeDropdownByMaterialId(Long materialId);
```

### 3. 服务实现
**文件**: `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/scheme/service/impl/InspectionSchemeServiceImpl.java`

实现了主要的业务逻辑：
1. **查询检验方案**: 根据检品ID查询所有关联的检验方案
2. **过滤条件**: 只查询未删除的检验方案
3. **排序**: 按创建时间倒序排列
4. **关联查询**: 查询检验方案的生效版本、物料信息、实验包信息
5. **数据转换**: 转换为下拉数据格式

### 4. Web接口
**文件**: `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/scheme/InspectionSchemeController.java`

新增API接口：
```java
@GetMapping("/dropdown/material/{materialId}")
@ApiOperation("通过检品ID查询检验方案下拉数据")
public ResponseInfo<List<InspectionSchemeDropdownDTO>> getInspectionSchemeDropdownByMaterialId(@PathVariable Long materialId)
```

## API使用说明

### 请求
```
GET /inspection-scheme/dropdown/material/{materialId}
```

### 参数
- `materialId` (路径参数): 检品ID，必填

### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "检验方案A",
      "code": "SCHEME001",
      "activeVersionNo": "V1.0",
      "materialId": 101,
      "materialCode": "MAT001",
      "materialName": "检品名称A",
      "packageId": 201,
      "packageCode": "PKG001",
      "packageName": "实验包A",
      "displayName": "检验方案A - V1.0"
    },
    {
      "id": 2,
      "name": "检验方案B",
      "code": "SCHEME002",
      "activeVersionNo": "V2.1",
      "materialId": 101,
      "materialCode": "MAT001",
      "materialName": "检品名称A",
      "packageId": 202,
      "packageCode": "PKG002",
      "packageName": "实验包B",
      "displayName": "检验方案B - V2.1"
    }
  ]
}
```

## 核心逻辑

### 1. 查询逻辑
```sql
SELECT * FROM lm_inspection_scheme 
WHERE material_id = ? 
  AND is_deleted = 0 
ORDER BY create_time DESC
```

### 2. 数据关联
- **检验方案版本**: 通过 `InspectionSchemeVersionMapper.getActiveVersion()` 查询当前生效版本
- **物料信息**: 通过 `MaterialMapper.selectById()` 查询物料详情
- **实验包信息**: 通过 `InspectPackageMapper.selectById()` 查询实验包详情

### 3. 显示名称生成
- 如果有生效版本：`方案名称 - 版本号`
- 如果没有生效版本：`方案名称`

## 数据库关系

```
lm_inspection_scheme (检验方案表)
    ├── material_id → lm_inspect_material (检品表)
    ├── package_id → lm_inspect_package (实验包表)  
    └── id → lm_inspection_scheme_version (检验方案版本表)
```

## 使用场景

这个接口适用于以下业务场景：
1. **检验单创建**: 选择检品后，显示可用的检验方案
2. **检验计划制定**: 根据检品选择对应的检验方案
3. **方案关联查询**: 查看某个检品关联的所有检验方案
4. **下拉选择器**: 为前端下拉选择器提供数据源

## 特点

- **高效查询**: 通过索引优化查询性能
- **完整信息**: 包含方案、版本、物料、实验包的完整信息
- **友好显示**: 提供格式化的显示名称
- **数据完整性**: 自动关联查询相关表的详细信息
- **排序优化**: 按创建时间倒序，最新的方案排在前面

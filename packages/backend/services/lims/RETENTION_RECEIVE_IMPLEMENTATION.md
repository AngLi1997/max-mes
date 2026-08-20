# 留样接收功能实现总结

## 功能概述

实现留样接收列表查询功能，用于展示已取样但未接收的留样样品，支持分页查询和多条件筛选。

**设计原则**: 复用现有样品领取的DTO和VO，保持数据结构的一致性，仅在SQL层面控制留样样品的筛选逻辑。

## 需求说明

1. **数据来源**: 展示已取样未接收，且检验项目是留样的样品
   - 样品状态：`sampled = 1`（已取样）
   - 样品状态：`received = 0`（未接收）
   - 检验项目ID：`2000000000000000001`（留样检验项目）
   - 排除作废样品：`discarded = 0`

2. **排序规则**:
   - 取样时间倒序（最新取样的在前）
   - 同样的时间按照样品编号排序

3. **筛选条件**:
   - 样品编号
   - 检验单号
   - 检品名称
   - 批次号

4. **批量接收**: 复用现有的样品批量接收接口 `/sample-receive/samples/batch-receive`

## 实现方案

### 1. 数据层 (Mapper)

#### 新增查询方法
**文件**: `SampleMapper.java`

```java
/**
 * 分页查询留样接收列表
 * @param queryDTO 查询条件
 * @return 留样接收列表
 */
List<SampleCollectionListDTO> selectRetentionReceivePageList(SampleCollectionPageQueryDTO queryDTO);
```

#### SQL实现
**文件**: `SampleMapper.xml`

```xml
<select id="selectRetentionReceivePageList" resultType="com.bmos.lims2.server.inspect.sample.dto.SampleCollectionListDTO">
    SELECT
        s.id,
        s.sample_no AS sampleNo,
        s.sample_name AS sampleName,
        s.inspection_order_id AS inspectionOrderId,
        io.order_no AS orderNo,
        s.inspect_item_id AS inspectItemId,
        ii.code AS inspectItemCode,
        ii.name AS inspectItemName,
        m.name AS materialName,
        m.code AS materialCode,
        m.specification AS materialSpec,
        io.batch_no AS batchNo,
        s.quantity,
        s.unit_id AS unitId,
        u.name AS unitName,
        s.receive_time AS receiveTime,
        s.receiver_name AS receiverName,
        io.create_time AS requestTime,
        io.create_by AS requestUserId,
        s.create_time AS createTime,
        s.tag_printed AS tagPrinted
    FROM lm_sample s
    LEFT JOIN lm_inspection_order io ON s.inspection_order_id = io.id
    LEFT JOIN lm_inspect_material m ON io.material_id = m.id
    LEFT JOIN lm_inspect_item ii ON s.inspect_item_id = ii.id
    LEFT JOIN lm_unit u ON s.unit_id = u.id
    WHERE s.is_deleted = 0
      AND s.sampled = 1
      AND s.received = 0
      AND s.discarded = 0
      AND s.inspect_item_id = 2000000000000000001
    <if test="sampleNo != null and sampleNo != ''">
        AND s.sample_no LIKE CONCAT('%', #{sampleNo}, '%')
    </if>
    <if test="orderNo != null and orderNo != ''">
        AND io.order_no LIKE CONCAT('%', #{orderNo}, '%')
    </if>
    <if test="materialName != null and materialName != ''">
        AND m.name LIKE CONCAT('%', #{materialName}, '%')
    </if>
    <if test="batchNo != null and batchNo != ''">
        AND io.batch_no LIKE CONCAT('%', #{batchNo}, '%')
    </if>
    ORDER BY s.sampling_time DESC, s.sample_no ASC
</select>
```

**关键特性**:
- 固定筛选留样检验项目：`inspect_item_id = 2000000000000000001`
- 固定筛选已取样未接收：`sampled = 1 AND received = 0`
- 排除作废样品：`discarded = 0`
- 关联查询：检验单、物料、检验项目、单位信息
- 动态条件：样品编号、检验单号、物料名称、批次号
- 排序：取样时间倒序，样品编号升序
- **返回类型**: 复用 `SampleCollectionListDTO`

### 2. 服务层 (Service)

#### 复用的DTO

**SampleCollectionPageQueryDTO** (查询参数 - 复用):
```java
package com.bmos.lims2.server.inspect.sample.dto;

@Getter
@Setter
@ApiModel(value = "样品领取列表分页查询参数")
public class SampleCollectionPageQueryDTO extends BasePage {
    private Long inspectItemId;                 // 检验项目ID
    private String inspectItemName;             // 检验项目名称
    private String sampleNo;                    // 样品编号
    private String orderNo;                     // 检验单号
    private String materialName;                // 检品名称
    private List<Long> materialIds;             // 检品ID列表
    private String batchNo;                     // 批次号
    private LocalDateTime inspectionRequestTimeStart;  // 请验开始时间
    private LocalDateTime inspectionRequestTimeEnd;    // 请验结束时间
}
```

**SampleCollectionListDTO** (返回数据 - 复用):
```java
package com.bmos.lims2.server.inspect.sample.dto;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("样品领取列表数据对象")
public class SampleCollectionListDTO {
    private Long id;                            // 样品ID
    private String sampleNo;                    // 样品编号
    private String sampleName;                  // 样品名称
    private Long inspectionOrderId;             // 检验单ID
    private String orderNo;                     // 检验单号
    private Long inspectItemId;                 // 检验项目ID
    private String inspectItemCode;             // 检验项目编码
    private String inspectItemName;             // 检验项目名称
    private String materialName;                // 检品名称
    private String materialCode;                // 检品编码
    private String materialSpec;                // 检品规格
    private String batchNo;                     // 批次号
    private String quantity;                    // 样品数量
    private Long unitId;                        // 单位ID
    private String unitName;                    // 单位名称
    private LocalDateTime requestTime;          // 请验时间
    private String requestUserId;               // 请验人ID
    private String requestUserName;             // 请验人名称
    private LocalDateTime receiveTime;          // 接收时间
    private String receiverName;                // 接收人
    private LocalDateTime createTime;           // 创建时间
    private Boolean tagPrinted;                 // 标签是否已打印

    public String getRequestUserName() {
        return UserUtils.getUsername(requestUserId + "");
    }
}
```

#### Service接口
**文件**: `SampleReceiveService.java`

```java
/**
 * 分页查询留样接收列表
 * @param queryDTO 查询条件
 * @return 留样接收列表分页数据
 */
CommonPage<SampleCollectionListDTO> getRetentionReceivePageList(SampleCollectionPageQueryDTO queryDTO);
```

#### Service实现
**文件**: `SampleReceiveServiceImpl.java`

```java
@Override
public CommonPage<SampleCollectionListDTO> getRetentionReceivePageList(SampleCollectionPageQueryDTO queryDTO) {
    // 设置分页参数
    PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

    // 查询留样接收列表
    List<SampleCollectionListDTO> list = sampleMapper.selectRetentionReceivePageList(queryDTO);

    // 返回分页数据
    return CommonPage.convertPage(list);
}
```

### 3. Web层 (Controller)

#### 复用的VO

**SampleCollectionPageReqVO** (请求VO - 复用):
```java
package com.bmos.lims2.web.inspect.sample.vo.req;

@Getter
@Setter
@ApiModel(value = "样品领取列表分页查询参数")
public class SampleCollectionPageReqVO extends BasePage {
    private Long inspectItemId;                 // 检验项目ID
    private String inspectItemName;             // 检验项目名称
    private String sampleNo;                    // 样品编号
    private String orderNo;                     // 检验单号
    private String materialName;                // 检品名称
    private List<Long> materialIds;             // 检品ID列表
    private String batchNo;                     // 批次号
    private LocalDateTime inspectionRequestTimeStart;  // 请验开始时间
    private LocalDateTime inspectionRequestTimeEnd;    // 请验结束时间
}
```

**SampleCollectionListRespVO** (响应VO - 复用):
```java
package com.bmos.lims2.web.inspect.sample.vo.resp;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("样品领取列表响应对象")
public class SampleCollectionListRespVO {
    private Long id;                            // 样品ID
    private String sampleNo;                    // 样品编号
    private String sampleName;                  // 样品名称
    private Long inspectionOrderId;             // 检验单ID
    private String orderNo;                     // 检验单号
    private Long inspectItemId;                 // 检验项目ID
    private String inspectItemCode;             // 检验项目编码
    private String inspectItemName;             // 检验项目名称
    private String materialName;                // 检品名称
    private String materialCode;                // 检品编码
    private String materialSpec;                // 检品规格
    private String batchNo;                     // 批次号
    private String quantity;                    // 样品数量
    private Long unitId;                        // 单位ID
    private String unitName;                    // 单位名称
    private LocalDateTime requestTime;          // 请验时间
    private String requestUserId;               // 请验人ID
    private String requestUserName;             // 请验人名称
    private LocalDateTime receiveTime;          // 接收时间
    private String receiverName;                // 接收人
    private LocalDateTime createTime;           // 创建时间
    private Boolean tagPrinted;                 // 标签是否已打印

    public String getRequestUserName() {
        return UserUtils.getUsername(requestUserId + "");
    }
}
```

#### 复用的数据转换器
**文件**: `SampleCollectionWebConverter.java` (已存在，直接复用)

```java
@Mapper(componentModel = "spring")
public interface SampleCollectionWebConverter {
    SampleCollectionWebConverter INSTANCE = Mappers.getMapper(SampleCollectionWebConverter.class);

    /**
     * PageReqVO转PageQueryDTO
     */
    SampleCollectionPageQueryDTO voToPageQueryDTO(SampleCollectionPageReqVO reqVO);

    /**
     * CollectionListDTO转CollectionListRespVO
     */
    SampleCollectionListRespVO dtoToRespVO(SampleCollectionListDTO dto);

    /**
     * CollectionListDTO列表转CollectionListRespVO列表
     */
    List<SampleCollectionListRespVO> dtoListToRespVOList(List<SampleCollectionListDTO> dtoList);
}
```

#### Controller接口
**文件**: `SampleReceiveController.java`

```java
@ApiOperation("分页查询留样接收列表")
@PostMapping("/retention/page")
public ResponseInfo<CommonPage<SampleCollectionListRespVO>> getRetentionReceivePage(
        @RequestBody @Valid SampleCollectionPageReqVO reqVO) {

    // 转换参数
    SampleCollectionPageQueryDTO queryDTO = SampleCollectionWebConverter.INSTANCE.voToPageQueryDTO(reqVO);

    // 查询留样接收列表
    CommonPage<SampleCollectionListDTO> pageData = sampleReceiveService.getRetentionReceivePageList(queryDTO);

    // 转换结果
    List<SampleCollectionListRespVO> respList = SampleCollectionWebConverter.INSTANCE
            .dtoListToRespVOList(pageData.getList());

    CommonPage<SampleCollectionListRespVO> result = new CommonPage<>();
    result.setTotal(pageData.getTotal());
    result.setPageNum(pageData.getPageNum());
    result.setPageSize(pageData.getPageSize());
    result.setList(respList);

    return ResponseInfo.success(result);
}
```

## API接口文档

### 1. 分页查询留样接收列表

**接口路径**: `POST /sample-receive/retention/page`

**请求参数** (复用 `SampleCollectionPageReqVO`):
```json
{
  "sampleNo": "string",        // 样品编号（模糊查询，可选）
  "orderNo": "string",         // 检验单号（模糊查询，可选）
  "materialName": "string",    // 检品名称（模糊查询，可选）
  "batchNo": "string",         // 批次号（模糊查询，可选）
  "inspectItemId": 2000000000000000001,  // 可传留样检验项目ID（可选）
  "pageNum": 1,                // 页码（必填）
  "pageSize": 10               // 每页大小（必填）
}
```

**响应数据** (复用 `SampleCollectionListRespVO`):
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "list": [
      {
        "id": 123,
        "sampleNo": "YP202602060001",
        "sampleName": "留样样品",
        "inspectionOrderId": 456,
        "orderNo": "QY202602060001",
        "inspectItemId": 2000000000000000001,
        "inspectItemCode": "SAMPLE_RETENTION",
        "inspectItemName": "留样",
        "materialName": "测试物料",
        "materialCode": "MAT001",
        "materialSpec": "规格型号",
        "batchNo": "BATCH001",
        "quantity": "10",
        "unitId": 1,
        "unitName": "kg",
        "requestTime": "2026-02-06 09:00:00",
        "requestUserId": "100001",
        "requestUserName": "李四",
        "receiveTime": null,
        "receiverName": null,
        "createTime": "2026-02-06 10:00:00",
        "tagPrinted": true
      }
    ]
  }
}
```

### 2. 批量接收留样样品

**接口路径**: `POST /sample-receive/samples/batch-receive`

**说明**: 复用现有的样品批量接收接口，留样样品的接收与普通样品接收逻辑一致。

**请求参数**:
```json
{
  "sampleIds": [123, 456, 789]  // 样品ID列表
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

## 修改文件清单

### Server层（3个文件修改）
1. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/mapper/SampleMapper.java` - 新增方法声明
2. `bmos-lims2-server/src/main/resources/mapper/inspect/order/SampleMapper.xml` - 新增SQL查询
3. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/receive/service/SampleReceiveService.java` - 新增接口方法
4. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/receive/service/impl/SampleReceiveServiceImpl.java` - 新增实现方法

### Web层（1个文件修改）
5. `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/receive/SampleReceiveController.java` - 新增Controller接口

### 文档
6. `RETENTION_RECEIVE_IMPLEMENTATION.md` - 实现总结文档

**特别说明**:
- ✅ **无需创建新的DTO/VO** - 全部复用 `SampleCollection` 相关类
- ✅ **无需创建新的Converter** - 复用 `SampleCollectionWebConverter`
- ✅ **保持数据结构一致性** - 前端可以使用相同的组件和逻辑

## 编译验证

项目已通过编译验证，无语法错误。

```
BUILD SUCCESS
Total time: 38.274 s
```

## 功能特性

### 1. 数据筛选
- ✅ 只展示已取样未接收的留样样品
- ✅ 自动关联检验单、物料、检验项目、单位信息
- ✅ 支持多条件模糊查询
- ✅ 排除已作废的样品

### 2. 排序规则
- ✅ 取样时间倒序（最新的在前）
- ✅ 相同时间按样品编号升序

### 3. 分页支持
- ✅ 使用 PageHelper 实现分页
- ✅ 返回总数、页码、页大小等分页信息

### 4. 数据完整性
- ✅ 包含样品基本信息
- ✅ 包含检验单信息
- ✅ 包含物料信息
- ✅ 包含检验项目信息（留样）
- ✅ 包含请验人员和时间信息

### 5. 接收功能
- ✅ 复用现有批量接收接口
- ✅ 统一的样品接收流程
- ✅ 完整的状态转换和时间记录

### 6. 代码复用
- ✅ 复用 `SampleCollectionPageQueryDTO` 查询参数
- ✅ 复用 `SampleCollectionListDTO` 返回数据
- ✅ 复用 `SampleCollectionPageReqVO` 请求VO
- ✅ 复用 `SampleCollectionListRespVO` 响应VO
- ✅ 复用 `SampleCollectionWebConverter` 数据转换器
- ✅ 最小化代码改动，保持系统一致性

## 前端对接说明

### 1. 列表查询

```javascript
// 请求示例 - 使用与样品领取相同的数据结构
const response = await axios.post('/sample-receive/retention/page', {
  sampleNo: 'YP2026',        // 可选
  orderNo: 'QY2026',         // 可选
  materialName: '测试',      // 可选
  batchNo: 'BATCH',          // 可选
  pageNum: 1,                // 必填
  pageSize: 10               // 必填
});

// 响应数据结构与样品领取列表完全一致
const { total, pageNum, pageSize, list } = response.data.data;

// 注意：inspectItemId 固定为 2000000000000000001 (留样检验项目)
// inspectItemName 固定为 "留样"
```

### 2. 批量接收

```javascript
// 选中样品ID列表
const selectedIds = [123, 456, 789];

// 批量接收 - 使用与普通样品相同的接口
await axios.post('/sample-receive/samples/batch-receive', {
  sampleIds: selectedIds
});
```

### 3. 前端展示字段建议

由于复用了 `SampleCollectionListRespVO`，前端可以根据需要选择展示以下字段：

**必要字段**:
- 样品编号 (`sampleNo`)
- 批次号 (`batchNo`)
- 检品名称 (`materialName`)
- 检品编码 (`materialCode`)
- 规格 (`materialSpec`)
- 样品数量 (`quantity` + `unitName`)
- 标签打印状态 (`tagPrinted`)
- 操作列（接收按钮）

**可选字段**:
- 检验项目名称 (`inspectItemName`) - 固定显示"留样"
- 请验时间 (`requestTime`)
- 请验人 (`requestUserName`)
- 创建时间 (`createTime`)

**筛选条件**:
- 样品编号
- 检验单号
- 检品名称
- 批次号

### 4. 前端组件复用

由于使用了相同的数据结构，前端可以复用样品领取的以下内容：
- ✅ 表格组件
- ✅ 筛选表单组件
- ✅ 分页组件
- ✅ 数据转换逻辑
- ✅ TypeScript 类型定义

## 测试建议

### 1. 功能测试
- [ ] 查询留样接收列表，验证只返回已取样未接收的留样样品
- [ ] 测试各个筛选条件的模糊查询功能
- [ ] 验证排序规则（取样时间倒序，样品编号升序）
- [ ] 测试分页功能

### 2. 边界测试
- [ ] 无数据时的展示
- [ ] 筛选条件为空时的查询
- [ ] 特殊字符的模糊查询
- [ ] 大数据量的分页性能

### 3. 集成测试
- [ ] 接收留样样品后，列表中该样品消失
- [ ] 新取样的留样样品出现在列表中
- [ ] 验证与现有样品接收流程的兼容性
- [ ] 验证数据结构与样品领取列表的一致性

## 注意事项

1. **留样检验项目ID**: 固定为 `2000000000000000001`，在SQL中硬编码
2. **样品状态**: 只查询 `sampled=1 AND received=0` 的样品
3. **排除作废**: 自动排除 `discarded=1` 的样品
4. **批量接收**: 使用现有接口，无需单独实现
5. **数据复用**: 完全复用样品领取的DTO/VO，保持系统一致性
6. **前端复用**: 前端可以复用样品领取的组件和逻辑
7. **分页性能**: 建议在 `lm_sample` 表的 `inspect_item_id`, `sampled`, `received`, `sampling_time` 字段上建立组合索引

## 后续优化建议

1. **索引优化**:
   ```sql
   CREATE INDEX idx_sample_retention_receive
   ON lm_sample(inspect_item_id, sampled, received, discarded, sampling_time DESC);
   ```

2. **缓存优化**: 考虑对单位名称等字典数据进行缓存

3. **扩展功能**:
   - 支持按取样时间范围筛选
   - 支持批量打印标签
   - 支持导出留样接收记录

## 版本历史

### v1.1 - 2026-02-06
- 优化实现，复用现有的 `SampleCollection` 相关DTO和VO
- 删除自定义的 `RetentionReceive` 相关类
- 使用现有的 `SampleCollectionWebConverter` 转换器
- 保持与样品领取功能的数据结构一致性

### v1.0 - 2026-02-06
- 初始实现留样接收列表查询功能
- 支持多条件筛选和分页
- 复用现有批量接收接口


1. **数据来源**: 展示已取样未接收，且检验项目是留样的样品
   - 样品状态：`sampled = 1`（已取样）
   - 样品状态：`received = 0`（未接收）
   - 检验项目ID：`2000000000000000001`（留样检验项目）
   - 排除作废样品：`discarded = 0`

2. **排序规则**:
   - 取样时间倒序（最新取样的在前）
   - 同样的时间按照样品编号排序

3. **筛选条件**:
   - 样品编号
   - 检验单号
   - 检品名称
   - 检品编码
   - 批次号

4. **批量接收**: 复用现有的样品批量接收接口 `/sample-receive/samples/batch-receive`

## 实现方案

### 1. 数据层 (Mapper)

#### 新增查询方法
**文件**: `SampleMapper.java`

```java
/**
 * 分页查询留样接收列表
 * @param queryDTO 查询条件
 * @return 留样接收列表
 */
List<com.bmos.lims2.server.inspect.receive.dto.RetentionReceiveListDTO> selectRetentionReceivePageList(
        com.bmos.lims2.server.inspect.receive.dto.RetentionReceivePageQueryDTO queryDTO);
```

#### SQL实现
**文件**: `SampleMapper.xml`

```xml
<select id="selectRetentionReceivePageList" resultType="com.bmos.lims2.server.inspect.receive.dto.RetentionReceiveListDTO">
    SELECT
        s.id,
        s.sample_no AS sampleNo,
        s.sample_name AS sampleName,
        s.inspection_order_id AS inspectionOrderId,
        io.order_no AS orderNo,
        m.name AS materialName,
        m.code AS materialCode,
        m.specification AS materialSpec,
        io.batch_no AS batchNo,
        s.quantity,
        s.unit_id AS unitId,
        u.name AS unitName,
        s.sampling_time AS samplingTime,
        s.sampler_name AS samplerName,
        io.create_time AS requestTime,
        io.create_by AS requestUserId,
        s.tag_printed AS tagPrinted,
        s.remark
    FROM lm_sample s
    LEFT JOIN lm_inspection_order io ON s.inspection_order_id = io.id
    LEFT JOIN lm_inspect_material m ON io.material_id = m.id
    LEFT JOIN lm_unit u ON s.unit_id = u.id
    WHERE s.is_deleted = 0
      AND s.sampled = 1
      AND s.received = 0
      AND s.discarded = 0
      AND s.inspect_item_id = 2000000000000000001
    <if test="sampleNo != null and sampleNo != ''">
        AND s.sample_no LIKE CONCAT('%', #{sampleNo}, '%')
    </if>
    <if test="orderNo != null and orderNo != ''">
        AND io.order_no LIKE CONCAT('%', #{orderNo}, '%')
    </if>
    <if test="materialName != null and materialName != ''">
        AND m.name LIKE CONCAT('%', #{materialName}, '%')
    </if>
    <if test="materialCode != null and materialCode != ''">
        AND m.code LIKE CONCAT('%', #{materialCode}, '%')
    </if>
    <if test="batchNo != null and batchNo != ''">
        AND io.batch_no LIKE CONCAT('%', #{batchNo}, '%')
    </if>
    ORDER BY s.sampling_time DESC, s.sample_no ASC
</select>
```

**关键特性**:
- 固定筛选留样检验项目：`inspect_item_id = 2000000000000000001`
- 固定筛选已取样未接收：`sampled = 1 AND received = 0`
- 排除作废样品：`discarded = 0`
- 关联查询：检验单、物料、单位信息
- 动态条件：样品编号、检验单号、物料名称、物料编码、批次号
- 排序：取样时间倒序，样品编号升序

### 2. 服务层 (Service)

#### DTO定义

**RetentionReceivePageQueryDTO** (查询参数):
```java
package com.bmos.lims2.server.inspect.receive.dto;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("留样接收分页查询参数")
public class RetentionReceivePageQueryDTO {
    private String sampleNo;        // 样品编号
    private String orderNo;         // 检验单号
    private String materialName;    // 检品名称
    private String materialCode;    // 检品编码
    private String batchNo;         // 批次号
    private Integer pageNum;        // 页码
    private Integer pageSize;       // 每页大小
}
```

**RetentionReceiveListDTO** (返回数据):
```java
package com.bmos.lims2.server.inspect.receive.dto;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("留样接收列表数据对象")
public class RetentionReceiveListDTO {
    private Long id;                        // 样品ID
    private String sampleNo;                // 样品编号
    private String sampleName;              // 样品名称
    private Long inspectionOrderId;         // 检验单ID
    private String orderNo;                 // 检验单号
    private String materialName;            // 检品名称
    private String materialCode;            // 检品编码
    private String materialSpec;            // 检品规格
    private String batchNo;                 // 批次号
    private String quantity;                // 样品数量
    private Long unitId;                    // 单位ID
    private String unitName;                // 单位名称
    private LocalDateTime samplingTime;     // 取样时间
    private String samplerName;             // 取样人
    private LocalDateTime requestTime;      // 请验时间
    private String requestUserId;           // 请验人ID
    private String requestUserName;         // 请验人名称
    private Boolean tagPrinted;             // 标签是否已打印
    private String remark;                  // 备注

    public String getRequestUserName() {
        return UserUtils.getUsername(requestUserId + "");
    }
}
```

#### Service接口
**文件**: `SampleReceiveService.java`

```java
/**
 * 分页查询留样接收列表
 * @param queryDTO 查询条件
 * @return 留样接收列表分页数据
 */
CommonPage<RetentionReceiveListDTO> getRetentionReceivePageList(RetentionReceivePageQueryDTO queryDTO);
```

#### Service实现
**文件**: `SampleReceiveServiceImpl.java`

```java
@Override
public CommonPage<RetentionReceiveListDTO> getRetentionReceivePageList(RetentionReceivePageQueryDTO queryDTO) {
    // 设置分页参数
    PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

    // 查询留样接收列表
    List<RetentionReceiveListDTO> list = sampleMapper.selectRetentionReceivePageList(queryDTO);

    // 返回分页数据
    return CommonPage.convertPage(list);
}
```

### 3. Web层 (Controller)

#### 请求VO
**文件**: `RetentionReceivePageReqVO.java`

```java
package com.bmos.lims2.web.inspect.receive.vo.req;

@Data
@ApiModel("留样接收分页查询请求")
public class RetentionReceivePageReqVO {
    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty(value = "页码", required = true, example = "1")
    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    @ApiModelProperty(value = "每页大小", required = true, example = "10")
    @NotNull(message = "每页大小不能为空")
    private Integer pageSize;
}
```

#### 响应VO
**文件**: `RetentionReceiveListRespVO.java`

```java
package com.bmos.lims2.web.inspect.receive.vo.resp;

@Data
@ApiModel("留样接收列表响应")
public class RetentionReceiveListRespVO {
    @ApiModelProperty("样品ID")
    private Long id;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("取样人")
    private String samplerName;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("请验人名称")
    private String requestUserName;

    @ApiModelProperty("标签是否已打印")
    private Boolean tagPrinted;

    @ApiModelProperty("备注")
    private String remark;
}
```

#### 数据转换器
**文件**: `RetentionReceiveWebConverter.java`

```java
package com.bmos.lims2.web.inspect.receive.converter;

@Mapper(componentModel = "spring")
public interface RetentionReceiveWebConverter {

    RetentionReceiveWebConverter INSTANCE = Mappers.getMapper(RetentionReceiveWebConverter.class);

    /**
     * 请求VO转查询DTO
     */
    RetentionReceivePageQueryDTO reqVoToQueryDto(RetentionReceivePageReqVO reqVO);

    /**
     * DTO转响应VO
     */
    RetentionReceiveListRespVO dtoToRespVo(RetentionReceiveListDTO dto);

    /**
     * DTO列表转响应VO列表
     */
    List<RetentionReceiveListRespVO> dtoListToRespVoList(List<RetentionReceiveListDTO> dtoList);
}
```

#### Controller接口
**文件**: `SampleReceiveController.java`

```java
@ApiOperation("分页查询留样接收列表")
@PostMapping("/retention/page")
public ResponseInfo<CommonPage<RetentionReceiveListRespVO>> getRetentionReceivePage(
        @RequestBody @Valid RetentionReceivePageReqVO reqVO) {

    // 转换参数
    RetentionReceivePageQueryDTO queryDTO = RetentionReceiveWebConverter.INSTANCE.reqVoToQueryDto(reqVO);

    // 查询留样接收列表
    CommonPage<RetentionReceiveListDTO> pageData = sampleReceiveService.getRetentionReceivePageList(queryDTO);

    // 转换结果
    List<RetentionReceiveListRespVO> respList = RetentionReceiveWebConverter.INSTANCE
            .dtoListToRespVoList(pageData.getList());

    CommonPage<RetentionReceiveListRespVO> result = new CommonPage<>();
    result.setTotal(pageData.getTotal());
    result.setPageNum(pageData.getPageNum());
    result.setPageSize(pageData.getPageSize());
    result.setList(respList);

    return ResponseInfo.success(result);
}
```

## API接口文档

### 1. 分页查询留样接收列表

**接口路径**: `POST /sample-receive/retention/page`

**请求参数**:
```json
{
  "sampleNo": "string",        // 样品编号（模糊查询，可选）
  "orderNo": "string",         // 检验单号（模糊查询，可选）
  "materialName": "string",    // 检品名称（模糊查询，可选）
  "materialCode": "string",    // 检品编码（模糊查询，可选）
  "batchNo": "string",         // 批次号（模糊查询，可选）
  "pageNum": 1,                // 页码（必填）
  "pageSize": 10               // 每页大小（必填）
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "list": [
      {
        "id": 123,
        "sampleNo": "YP202602060001",
        "sampleName": "留样样品",
        "inspectionOrderId": 456,
        "orderNo": "QY202602060001",
        "materialName": "测试物料",
        "materialCode": "MAT001",
        "materialSpec": "规格型号",
        "batchNo": "BATCH001",
        "quantity": "10",
        "unitId": 1,
        "unitName": "kg",
        "samplingTime": "2026-02-06 10:00:00",
        "samplerName": "张三",
        "requestTime": "2026-02-06 09:00:00",
        "requestUserId": "100001",
        "requestUserName": "李四",
        "tagPrinted": true,
        "remark": "备注信息"
      }
    ]
  }
}
```

### 2. 批量接收留样样品

**接口路径**: `POST /sample-receive/samples/batch-receive`

**说明**: 复用现有的样品批量接收接口，留样样品的接收与普通样品接收逻辑一致。

**请求参数**:
```json
{
  "sampleIds": [123, 456, 789]  // 样品ID列表
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

## 修改文件清单

### Server层
1. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/receive/dto/RetentionReceivePageQueryDTO.java` - 新增
2. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/receive/dto/RetentionReceiveListDTO.java` - 新增
3. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/mapper/SampleMapper.java` - 新增方法
4. `bmos-lims2-server/src/main/resources/mapper/inspect/order/SampleMapper.xml` - 新增SQL
5. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/receive/service/SampleReceiveService.java` - 新增方法
6. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/receive/service/impl/SampleReceiveServiceImpl.java` - 新增实现

### Web层
7. `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/receive/vo/req/RetentionReceivePageReqVO.java` - 新增
8. `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/receive/vo/resp/RetentionReceiveListRespVO.java` - 新增
9. `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/receive/converter/RetentionReceiveWebConverter.java` - 新增
10. `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/receive/SampleReceiveController.java` - 新增接口

### 文档
11. `RETENTION_RECEIVE_IMPLEMENTATION.md` - 实现总结文档

## 编译验证

项目已通过编译验证，无语法错误。

```
BUILD SUCCESS
Total time: 11.836 s
```

## 功能特性

### 1. 数据筛选
- ✅ 只展示已取样未接收的留样样品
- ✅ 自动关联检验单、物料、单位信息
- ✅ 支持多条件模糊查询
- ✅ 排除已作废的样品

### 2. 排序规则
- ✅ 取样时间倒序（最新的在前）
- ✅ 相同时间按样品编号升序

### 3. 分页支持
- ✅ 使用 PageHelper 实现分页
- ✅ 返回总数、页码、页大小等分页信息

### 4. 数据完整性
- ✅ 包含样品基本信息
- ✅ 包含检验单信息
- ✅ 包含物料信息
- ✅ 包含取样人员和时间信息
- ✅ 包含请验人员和时间信息

### 5. 接收功能
- ✅ 复用现有批量接收接口
- ✅ 统一的样品接收流程
- ✅ 完整的状态转换和时间记录

## 前端对接说明

### 1. 列表查询

```javascript
// 请求示例
const response = await axios.post('/sample-receive/retention/page', {
  sampleNo: 'YP2026',        // 可选
  orderNo: 'QY2026',         // 可选
  materialName: '测试',      // 可选
  materialCode: 'MAT',       // 可选
  batchNo: 'BATCH',          // 可选
  pageNum: 1,                // 必填
  pageSize: 10               // 必填
});

// 响应数据
const { total, pageNum, pageSize, list } = response.data.data;
```

### 2. 批量接收

```javascript
// 选中样品ID列表
const selectedIds = [123, 456, 789];

// 批量接收
await axios.post('/sample-receive/samples/batch-receive', {
  sampleIds: selectedIds
});
```

### 3. 前端展示字段建议

**列表表格**:
- 样品编号 (`sampleNo`)
- 批次号 (`batchNo`)
- 检品名称 (`materialName`)
- 检品编码 (`materialCode`)
- 规格 (`materialSpec`)
- 样品数量 (`quantity` + `unitName`)
- 取样时间 (`samplingTime`)
- 取样人 (`samplerName`)
- 标签打印状态 (`tagPrinted`)
- 操作列（接收按钮）

**筛选条件**:
- 样品编号
- 检验单号
- 检品名称
- 检品编码
- 批次号

## 测试建议

### 1. 功能测试
- [ ] 查询留样接收列表，验证只返回已取样未接收的留样样品
- [ ] 测试各个筛选条件的模糊查询功能
- [ ] 验证排序规则（取样时间倒序，样品编号升序）
- [ ] 测试分页功能

### 2. 边界测试
- [ ] 无数据时的展示
- [ ] 筛选条件为空时的查询
- [ ] 特殊字符的模糊查询
- [ ] 大数据量的分页性能

### 3. 集成测试
- [ ] 接收留样样品后，列表中该样品消失
- [ ] 新取样的留样样品出现在列表中
- [ ] 验证与现有样品接收流程的兼容性

## 注意事项

1. **留样检验项目ID**: 固定为 `2000000000000000001`，在SQL中硬编码
2. **样品状态**: 只查询 `sampled=1 AND received=0` 的样品
3. **排除作废**: 自动排除 `discarded=1` 的样品
4. **批量接收**: 使用现有接口，无需单独实现
5. **分页性能**: 建议在 `lm_sample` 表的 `inspect_item_id`, `sampled`, `received`, `sampling_time` 字段上建立组合索引

## 后续优化建议

1. **索引优化**:
   ```sql
   CREATE INDEX idx_sample_retention_receive
   ON lm_sample(inspect_item_id, sampled, received, discarded, sampling_time DESC);
   ```

2. **缓存优化**: 考虑对单位名称等字典数据进行缓存

3. **扩展功能**:
   - 支持按取样时间范围筛选
   - 支持批量打印标签
   - 支持导出留样接收记录

## 版本历史

### v1.0 - 2026-02-06
- 初始实现留样接收列表查询功能
- 支持多条件筛选和分页
- 复用现有批量接收接口

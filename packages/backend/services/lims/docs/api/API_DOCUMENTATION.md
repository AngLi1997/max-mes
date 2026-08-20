# 实验包完整配置查询接口

## 接口描述
通过实验包ID查询实验包下所有的检项、检项所包含的分析项、分析项所包含的数据点信息的接口。

## 接口地址
```
GET /api/app/lims2/inspect/package/full-config/{packageId}
```

## 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| packageId | Long | 是 | 实验包ID |

## 响应结果
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "packageId": 1,
    "packageCode": "PKG001",
    "packageName": "实验包名称",
    "remark": "实验包描述",
    "inspectionItems": [
      {
        "inspectItemId": 1,
        "inspectItemCode": "ITEM001",
        "inspectItemName": "检验项目名称",
        "isRequired": true,
        "sort": 1,
        "remark": "检验项目备注",
        "inspectionParameters": [
          {
            "parameterId": 1,
            "parameterCode": "PARAM001",
            "parameterName": "分析项名称",
            "standardRule": "标准规定",
            "isReportable": true,
            "isExecutable": true,
            "dataPoints": [
              {
                "dataPointId": 1,
                "name": "数据点名称",
                "pointType": "NUMBER",
                "trendLineConfig": "{}",
                "options": "{}",
                "reportDisplay": true,
                "finalExpression": "判定表达式",
                "judgments": [
                  {
                    "judgmentType": "RANGE",
                    "minValue": 0.0,
                    "maxValue": 100.0,
                    "standardValue": "50.0",
                    "expression": "x >= 0 && x <= 100"
                  }
                ]
              }
            ]
          }
        ]
      }
    ]
  }
}
```

## 数据结构说明

### InspectPackageFullConfigRespVO
| 字段名 | 类型 | 说明 |
|--------|------|------|
| packageId | Long | 实验包ID |
| packageCode | String | 实验包编码 |
| packageName | String | 实验包名称 |
| remark | String | 实验包描述 |
| inspectionItems | List<InspectionItemVO> | 检验项目列表 |

### InspectionItemVO
| 字段名 | 类型 | 说明 |
|--------|------|------|
| inspectItemId | Long | 检验项目ID |
| inspectItemCode | String | 检验项目编码 |
| inspectItemName | String | 检验项目名称 |
| isRequired | Boolean | 是否必检 |
| sort | Integer | 排序 |
| remark | String | 备注 |
| inspectionParameters | List<AnalysisItemVO> | 分析项列表 |

### AnalysisItemVO
| 字段名 | 类型 | 说明 |
|--------|------|------|
| parameterId | Long | 分析项ID |
| parameterCode | String | 分析项编码 |
| parameterName | String | 分析项名称 |
| standardRule | String | 标准规定 |
| isReportable | Boolean | 是否报告项 |
| isExecutable | Boolean | 是否可执行 |
| dataPoints | List<DataPointVO> | 数据点列表 |

### DataPointVO
| 字段名 | 类型 | 说明 |
|--------|------|------|
| dataPointId | Long | 原始数据点ID |
| name | String | 数据点名称 |
| pointType | String | 数据点类型（NUMBER/TEXT/OPTION） |
| trendLineConfig | String | 趋势线配置(JSON) |
| options | String | 选项配置(JSON) |
| reportDisplay | Boolean | 是否报告显示 |
| finalExpression | String | 最终判定表达式 |
| judgments | List<JudgmentVO> | 判定配置列表 |

### JudgmentVO
| 字段名 | 类型 | 说明 |
|--------|------|------|
| judgmentType | String | 判定类型（RANGE/EQUAL） |
| minValue | BigDecimal | 最小值 |
| maxValue | BigDecimal | 最大值 |
| standardValue | String | 标准值 |
| expression | String | 判定表达式 |

## 数据关系说明
- 实验包 → 检验方案 → 检验方案版本（生效状态）→ 检验项目配置 → 分析项配置 → 数据点配置 → 判定配置
- 查询逻辑基于生效的检验方案版本（status = 'ACTIVE'）
- 支持嵌套数据结构，一次查询获取完整的层级关系

## 错误码说明
| 错误码 | 说明 |
|--------|------|
| 81_00_0002 | 数据不存在（实验包不存在） |

## 使用示例
```bash
# 查询实验包ID为1的完整配置信息
curl -X GET "http://localhost:60800/api/app/lims2/inspect/package/full-config/1"
```

## 注意事项
1. 该接口只返回生效状态的检验方案版本配置
2. 如果实验包没有关联任何检验方案，将返回基础实验包信息，inspectionItems为空数组
3. 数据按照检验项目排序、分析项ID、数据点ID、判定ID进行排序
4. 支持MyBatis的嵌套结果映射，性能较优

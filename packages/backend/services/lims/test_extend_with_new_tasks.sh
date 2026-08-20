#!/bin/bash

BASE_URL="http://localhost:61001/api/app/lims2"
TOKEN="1ce4e933-5ec4-4d3d-be64-fa13c80b1f9d"

echo "=== 留样延期自动生成观察任务测试 ==="
echo ""

# 1. 查询留样样品列表，获取一个已接收的样品
echo "1. 查询留样样品列表..."
SAMPLE_RESPONSE=$(curl -s -X POST "${BASE_URL}/retention-sample-manage/page" \
  -H "Bmos-Access-Token: ${TOKEN}" \
  -H "Token: ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"pageNum":1,"pageSize":1}')

echo "Response: ${SAMPLE_RESPONSE}" | python -m json.tool 2>/dev/null || echo "${SAMPLE_RESPONSE}"
echo ""

# 提取样品ID和当前留样期限
SAMPLE_ID=$(echo "${SAMPLE_RESPONSE}" | grep -o '"id":"[0-9]*"' | head -1 | grep -o '[0-9]*')
CURRENT_EXPIRY=$(echo "${SAMPLE_RESPONSE}" | grep -o '"retentionExpiryDate":"[^"]*"' | head -1 | cut -d'"' -f4)

echo "样品ID: ${SAMPLE_ID}"
echo "当前留样期限: ${CURRENT_EXPIRY}"
echo ""

if [ -z "${SAMPLE_ID}" ]; then
  echo "未找到留样样品，退出测试"
  exit 1
fi

# 2. 查询该样品的观察任务
echo "2. 查询样品的观察任务..."
TASK_RESPONSE=$(curl -s -X POST "${BASE_URL}/retention-observation/task/page" \
  -H "Bmos-Access-Token: ${TOKEN}" \
  -H "Token: ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d "{\"pageNum\":1,\"pageSize\":100,\"sampleNo\":\"$(echo ${SAMPLE_RESPONSE} | grep -o '\"sampleNo\":\"[^\"]*\"' | head -1 | cut -d'\"' -f4)\"}")

echo "观察任务列表: ${TASK_RESPONSE}" | python -m json.tool 2>/dev/null || echo "${TASK_RESPONSE}"
echo ""

TASK_COUNT_BEFORE=$(echo "${TASK_RESPONSE}" | grep -o '"total":[0-9]*' | grep -o '[0-9]*')
echo "延期前观察任务数量: ${TASK_COUNT_BEFORE}"
echo ""

# 3. 计算新的延期日期（增加2年）
if [ ! -z "${CURRENT_EXPIRY}" ]; then
  CURRENT_YEAR=$(echo "${CURRENT_EXPIRY}" | cut -d'-' -f1)
  NEW_YEAR=$((CURRENT_YEAR + 2))
  NEW_EXPIRY="${NEW_YEAR}-$(echo ${CURRENT_EXPIRY} | cut -d'-' -f2-)"
  echo "新的留样期限: ${NEW_EXPIRY}"
  echo ""
  
  # 4. 执行延期操作
  echo "3. 执行延期操作..."
  EXTEND_RESPONSE=$(curl -s -X POST "${BASE_URL}/retention-sample-manage/${SAMPLE_ID}/extend" \
    -H "Bmos-Access-Token: ${TOKEN}" \
    -H "Token: ${TOKEN}" \
    -H "Content-Type: application/json" \
    -d "{\"newExpiryDate\":\"${NEW_EXPIRY}\"}")
  
  echo "延期结果: ${EXTEND_RESPONSE}" | python -m json.tool 2>/dev/null || echo "${EXTEND_RESPONSE}"
  echo ""
  
  # 5. 再次查询观察任务
  echo "4. 再次查询观察任务..."
  sleep 1
  TASK_RESPONSE_AFTER=$(curl -s -X POST "${BASE_URL}/retention-observation/task/page" \
    -H "Bmos-Access-Token: ${TOKEN}" \
    -H "Token: ${TOKEN}" \
    -H "Content-Type: application/json" \
    -d "{\"pageNum\":1,\"pageSize\":100,\"sampleNo\":\"$(echo ${SAMPLE_RESPONSE} | grep -o '\"sampleNo\":\"[^\"]*\"' | head -1 | cut -d'\"' -f4)\"}")
  
  echo "延期后观察任务列表: ${TASK_RESPONSE_AFTER}" | python -m json.tool 2>/dev/null || echo "${TASK_RESPONSE_AFTER}"
  echo ""
  
  TASK_COUNT_AFTER=$(echo "${TASK_RESPONSE_AFTER}" | grep -o '"total":[0-9]*' | grep -o '[0-9]*')
  echo "延期后观察任务数量: ${TASK_COUNT_AFTER}"
  echo ""
  
  # 6. 验证结果
  NEW_TASKS=$((TASK_COUNT_AFTER - TASK_COUNT_BEFORE))
  echo "=== 测试结果 ==="
  echo "延期前任务数量: ${TASK_COUNT_BEFORE}"
  echo "延期后任务数量: ${TASK_COUNT_AFTER}"
  echo "新增任务数量: ${NEW_TASKS}"
  
  if [ ${NEW_TASKS} -gt 0 ]; then
    echo "✅ 测试通过：延期后自动生成了 ${NEW_TASKS} 个新的观察任务"
  else
    echo "⚠️ 测试结果：延期后未生成新的观察任务（可能年数未增加）"
  fi
else
  echo "无法获取当前留样期限，跳过延期测试"
fi


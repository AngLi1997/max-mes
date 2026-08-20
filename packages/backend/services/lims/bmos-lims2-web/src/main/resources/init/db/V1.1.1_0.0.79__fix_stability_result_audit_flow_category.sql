-- 修正稳定性结果审核内置流程分类归属到稳定性配置
-- 参考稳定性方案审批内置流程分类配置（120020003 -> parent 12002003）

-- 1) 修正分类树中父节点tree_code，缺失时追加稳定性结果审核子分类编码
update bmos_lims2.lm_flow_audit_category
set tree_code = case
                    when find_in_set('120020040', replace(ifnull(tree_code, ''), '，', ',')) > 0 then tree_code
                    when ifnull(tree_code, '') = '' then '12002003,120020040'
                    else concat(tree_code, ',120020040')
    end,
    update_time = now(),
    update_by = '1'
where code = '12002003'
  and is_deleted = 0;

-- 2) 保证稳定性结果审核流程分类编码归属正确（与分类定义一致）
update bmos_lims2.lm_flow_audit
set category_code = '120020040',
    update_time = now(),
    update_by = '1'
where code = '120020040'
  and is_deleted = 0;

-- 3) 保证部署分类编码一致
update bmos_lims2.audit_deployment
set category = '120020040',
    update_time = now(),
    update_by = '1'
where name = '稳定性结果审核内置流程';

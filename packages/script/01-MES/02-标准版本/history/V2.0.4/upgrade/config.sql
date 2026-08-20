# 系统是否需要授权 by lilong 240403
INSERT INTO bmos_platform.bp_business_parameter (id, code, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100020, 'platform.sys.license.isRequired', 'true', 'STRING', 'BUSINESS', '平台', '系统是否需要授权', 100200, '', 1, '1', '1', now(), now(), 0);
# 更新用户密码字符集 by zhangruoyu 240409
UPDATE bmos_platform.bp_business_parameter SET value = '{
  "lowerCase": true,
  "upperCase": true,
  "digit": true,
  "specialCharacters": []
}' WHERE id = 100011;

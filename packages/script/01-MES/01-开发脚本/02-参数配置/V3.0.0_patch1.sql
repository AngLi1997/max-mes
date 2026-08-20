UPDATE bmos_platform.bp_business_parameter SET value = '{"190":"http://172.30.1.160:3000","900":"http://www.thingjs.com","220010001":"http://172.30.1.160:3000"}' WHERE id = 100016;

INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (100035, 'platform.dc.baie.modelUrl', '白俄3D模型地址', 'https://www.thingjs.com/s/e99f06b66498234f53172d00?params=105b0f77fd24654d4eebc434e9', 'STRING', 'BUSINESS', '平台', '白俄3D模型地址', 100350, '', 1, null, null, now(), now(), 0);


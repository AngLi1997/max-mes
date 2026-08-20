update bmos_platform.bp_message_template set content_template = '生产批号：#{#batchNo}
异常节点：#{#abnormalNode}' where id = 4 or id = 3;
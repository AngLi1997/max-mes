update bm_batch_record_item set page_starting_number =1 where page_starting_number is null;
update bm_batch_record_item set docx_header = null where docx_header = 'null';
update bm_batch_record_item set docx_footer = null where docx_footer = 'null';
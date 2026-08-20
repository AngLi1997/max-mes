update bm_ingredient_weigh_batch_process
set reuse = (select reusable from bm_procedure_step_model where id = bm_ingredient_weigh_batch_process.procedure_step_model_id)
where 1 = 1;

update bm_ingredient_weigh_process
set reuse = (select reusable from bm_procedure_step_model where id = bm_ingredient_weigh_process.procedure_step_model_id)
where 1 = 1;

update bm_output_weigh_process
set reuse = (select reusable from bm_procedure_step_model where id = bm_output_weigh_process.procedure_step_model_id)
where 1 = 1;
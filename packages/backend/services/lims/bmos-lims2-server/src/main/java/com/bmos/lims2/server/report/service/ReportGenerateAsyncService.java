package com.bmos.lims2.server.report.service;

public interface ReportGenerateAsyncService {

    void executeValidation(Long validationTaskId);

    void executeGenerate(Long generateTaskId);
}



package com.bmos.lims2.server.inspect.query.service;

import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.sample.ledger.entity.SampleLedger;
import com.bmos.lims2.server.report.entity.ReportGenerateTask;
import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;

import java.util.List;

/**
 * @Description: 检验查询-详情聚合DTO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:30
 */
public class InspectionDetailDTO {
    private InspectionOrderDTO order;
    private StatusFlags flags;
    private List<SampleLedger> sampleLedgers;
    private List<ReportGenerateTask> reports;
    private List<InspectionEntryRecordDTO> entryRecords;

    public InspectionOrderDTO getOrder() {
        return order;
    }

    public void setOrder(InspectionOrderDTO order) {
        this.order = order;
    }

    public StatusFlags getFlags() {
        return flags;
    }

    public void setFlags(StatusFlags flags) {
        this.flags = flags;
    }

    public List<SampleLedger> getSampleLedgers() {
        return sampleLedgers;
    }

    public void setSampleLedgers(List<SampleLedger> sampleLedgers) {
        this.sampleLedgers = sampleLedgers;
    }

    public List<ReportGenerateTask> getReports() {
        return reports;
    }

    public void setReports(List<ReportGenerateTask> reports) {
        this.reports = reports;
    }

    public List<InspectionEntryRecordDTO> getEntryRecords() {
        return entryRecords;
    }

    public void setEntryRecords(List<InspectionEntryRecordDTO> entryRecords) {
        this.entryRecords = entryRecords;
    }

    public static class StatusFlags {
        private boolean requested;
        private boolean sampled;
        private boolean inspected;
        private boolean reported;

        public boolean isRequested() {
            return requested;
        }

        public void setRequested(boolean requested) {
            this.requested = requested;
        }

        public boolean isSampled() {
            return sampled;
        }

        public void setSampled(boolean sampled) {
            this.sampled = sampled;
        }

        public boolean isInspected() {
            return inspected;
        }

        public void setInspected(boolean inspected) {
            this.inspected = inspected;
        }

        public boolean isReported() {
            return reported;
        }

        public void setReported(boolean reported) {
            this.reported = reported;
        }
    }
}



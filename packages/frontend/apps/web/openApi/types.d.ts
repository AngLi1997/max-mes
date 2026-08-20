declare namespace API {
    /*
    * /audit/create response
    */
    interface AuditCreate {

        /*
        * 
        */
        applyRemark: string;
        /*
        * 
        */
        auditType: string;
        /*
        * 
        */
        bizIds: Array<string>;
        /*
        * 
        */
        bizInfos: Array<bizInfosVo>;
        /*
        * 
        */
        bizValue: string;
    }

    /*
    * AuditBizInfo
    */
    interface bizInfosVo {

        /*
        * 
        */
        bizId: string;
        /*
        * 
        */
        bizValue: string;
    }

    /*
    * /audit/execute response
    */
    interface AuditExecute {

        /*
        * 
        */
        auditIds: Array<string>;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditType: string;
        /*
        * 
        */
        reviewRemark: string;
        /*
        * 
        */
        sourceModeCode: string;
        /*
        * 
        */
        sourceOperation: string;
        /*
        * 
        */
        sourcePageCode: string;
    }

    /*
    * /config/delete request
    */
    interface DeleteUsingGET {

        /*
        * 枚举唯一id
        */
        paramId?: string;
    }

    /*
    * /config/delete响应数据
    */
    interface ConfigDelete {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: boolean;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /config/general-config-one request
    */
    interface GeneralConfigOneUsingGET {

        /*
        * 枚举唯一id
        */
        paramId?: string;
    }

    /*
    * /config/general-config-one响应数据
    */
    interface ConfigGeneralConfigOne {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /config/group-general-config request
    */
    interface GroupGeneralConfigUsingGET {

        /*
        * 排序方式
        */
        dir?: string;
        /*
        * 
        */
        meunType?: string;
        /*
        * 排序字段
        */
        orderBy?: string;
    }

    /*
    * /config/group-general-config响应数据
    */
    interface ConfigGroupGeneralConfig {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * ConfigDetailsResponse
    */
    interface dataVo {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 是否删除
        */
        deleted: number;
        /*
        * 描述
        */
        description: string;
        /*
        * 
        */
        detailList: Array<detailListVo>;
        /*
        * 值
        */
        enumsType: string;
        /*
        * 值
        */
        enumsValue: string;
        /*
        * ip
        */
        ip: string;
        /*
        * 修约规则
        */
        label: string;
        /*
        * 菜单标识
        */
        menuIdentify: string;
        /*
        * 枚举唯一标识
        */
        paramId: string;
        /*
        * 
        */
        remark: string;
        /*
        * 排序号
        */
        sort: number;
        /*
        * 启动禁用
        */
        status: string;
        /*
        * 领用库编号
        */
        substNo: string;
        /*
        * 单位
        */
        unit: string;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
        /*
        * 位数
        */
        value: string;
    }

    /*
    * ConfigParameterDetail
    */
    interface detailListVo {

        /*
        * 
        */
        label: string;
        /*
        * 
        */
        name: string;
        /*
        * 
        */
        value: string;
    }

    /*
    * /config/list-adtevak-config request
    */
    interface ListAdtevakConfigUsingGET {

        /*
        * 排序方式
        */
        dir?: string;
        /*
        * 
        */
        meunType?: string;
        /*
        * 排序字段
        */
        orderBy?: string;
    }

    /*
    * /config/list-adtevak-config响应数据
    */
    interface ConfigListAdtevakConfig {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /config/list-inspect-item request
    */
    interface ListInspectItemUsingGET {

        /*
        * 排序方式
        */
        dir?: string;
        /*
        * 检验方式
        */
        inspectMethod?: string;
        /*
        * 检验类型
        */
        inspectType?: string;
        /*
        * 检验项目名称
        */
        itemName?: string;
        /*
        * 检验项目编号
        */
        itemNo?: string;
        /*
        * 项目类型
        */
        itemType?: string;
        /*
        * 是否复检
        */
        relnspect?: string;
    }

    /*
    * /config/list-inspect-item响应数据
    */
    interface ConfigListInspectItem {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /config/list-meun request
    */
    interface MeunListUsingGET {

        /*
        * 排序方式
        */
        dir?: string;
        /*
        * 
        */
        meunType?: string;
        /*
        * 排序字段
        */
        orderBy?: string;
    }

    /*
    * /config/list-meun响应数据
    */
    interface ConfigListMeun {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /config/save response
    */
    interface ConfigSave {

        /*
        * 描述
        */
        description: string;
        /*
        * 枚举类型
        */
        enumsType: string;
        /*
        * 枚举值
        */
        enumsValue: string;
        /*
        * 修约规则
        */
        label: string;
        /*
        * 菜单标识
        */
        menuIdentify: string;
        /*
        * 枚举唯一标识
        */
        paramId: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 排序号
        */
        sort: number;
        /*
        * 启用禁用
        */
        status: string;
        /*
        * 领用库编号
        */
        substNo: string;
        /*
        * 单位
        */
        unit: string;
        /*
        * 保留小数
        */
        value: string;
    }

    /*
    * /config/update response
    */
    interface ConfigUpdate {

        /*
        * 描述
        */
        description: string;
        /*
        * 枚举类型
        */
        enumsType: string;
        /*
        * 枚举值
        */
        enumsValue: string;
        /*
        * 修约规则
        */
        label: string;
        /*
        * 菜单标识
        */
        menuIdentify: string;
        /*
        * 枚举唯一标识
        */
        paramId: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 排序号
        */
        sort: number;
        /*
        * 启用禁用
        */
        status: string;
        /*
        * 领用库编号
        */
        substNo: string;
        /*
        * 单位
        */
        unit: string;
        /*
        * 保留小数
        */
        value: string;
    }

    /*
    * /config/update-adtevak-config response
    */
    interface ConfigUpdateAdtevakConfig {

        /*
        * 地址
        */
        address: string;
        /*
        * 血浆编号
        */
        adtevakCode: string;
        /*
        * 中心名称
        */
        centreName: string;
        /*
        * ip
        */
        ip: string;
        /*
        * 菜单标识
        */
        menuIdentify: string;
        /*
        * 简称
        */
        shorterName: string;
        /*
        * 启用禁用
        */
        status: number;
        /*
        * 电话
        */
        telNumber: string;
        /*
        * 链接地址
        */
        url: string;
    }

    /*
    * /config/update-inspect-item response
    */
    interface ConfigUpdateInspectItem {

        /*
        * 默认设备
        */
        defaultInstrument: string;
        /*
        * 检验项目描述
        */
        description: string;
        /*
        * 检验方式
        */
        inspectMethod: string;
        /*
        * 检验类型
        */
        inspectType: string;
        /*
        * 检验项目名称
        */
        itemName: string;
        /*
        * 检验项目编号
        */
        itemNo: string;
        /*
        * 项目类型
        */
        itemType: string;
        /*
        * 是否复检
        */
        relnspect: string;
        /*
        * 备注
        */
        remark: string;
    }

    /*
    * /config/update-inspect-rule response
    */
    interface ConfigUpdateInspectRule {

        /*
        * 
        */
        details: undefined;
        /*
        * 检验项目名称
        */
        itemName: string;
        /*
        * 检验项目编号
        */
        itemNo: string;
        /*
        * 
        */
        ruleDetail: undefined;
        /*
        * 标准规定
        */
        standard: string;
    }

    /*
    * /config/update-sort response
    */
    interface ConfigUpdateSort {

    }

    /*
    * /inspect/task/create response
    */
    interface TaskCreate {

        /*
        * 标本列表
        */
        samples: Array<samplesVo>;
    }

    /*
    * Sample
    */
    interface samplesVo {

        /*
        * 血浆外观
        */
        appearance: string;
        /*
        * 申请人
        */
        applicant: string;
        /*
        * 申请时间
        */
        applicantTime: string;
        /*
        * 申请备注
        */
        applyRemark: string;
        /*
        * 审核ID
        */
        auditId: string;
        /*
        * 审核结果
        */
        auditResult: string;
        /*
        * 审核类型
        */
        auditType: string;
        /*
        * 检验批号
        */
        batchNo: string;
        /*
        * 关联业务单id
        */
        bizId: string;
        /*
        * 献浆者血型
        */
        bloodType: string;
        /*
        * 标本箱号
        */
        boxId: string;
        /*
        * 采浆日期,前端展示
        */
        donorDate: string;
        /*
        * 免疫类型: NORMAL 普通,HA 甲肝,HB 乙肝,RABIES 狂犬,TETANUS 破伤风,ANTHRAX 炭疽
        */
        immunityType: string;
        /*
        * 检验项目 返回多个code用英文逗号隔开
        */
        inspectionItems: string;
        /*
        * 是否补样: TRUE 是,FALSE 否
        */
        needSupplement: string;
        /*
        * 
        */
        nonSuffixPlasmaNo: string;
        /*
        * 浆站来源编本编号，前端展示
        */
        orgSampleNo: string;
        /*
        * 来源单位系统名字
        */
        originOrgName: string;
        /*
        * 献浆者姓名
        */
        plasmaDonorName: string;
        /*
        * 献浆者编号
        */
        plasmaDonorNo: string;
        /*
        * 血浆编号
        */
        plasmaNo: string;
        /*
        * 标本接收状态: RECEIVING 待接收,RECEIVED 已接收,REJECTING 拒收待审核,REJECTED 已拒收 RECEIVE_AUDITING 接收待审核
        */
        receiveStatus: string;
        /*
        * 拒收原因名称
        */
        refuseReasonName: string;
        /*
        * 审核备注
        */
        reviewRemark: string;
        /*
        * 审核人
        */
        reviewer: string;
        /*
        * 审核时间
        */
        reviewerTime: string;
        /*
        * 标本分类:PLASMA_SPECIMEN 血浆标本,SERUM_SPECIMEN 血清标本
        */
        sampleClassification: string;
        /*
        * 标本编号（业务主键）,不展示
        */
        sampleNo: string;
        /*
        * 标本类型: ORG_TEST_SAMPLE 集中化检测标本,COMPANY_TEST_SAMPLE 公司复检标本,FOLLOW_UP_TEST_SAMPLE 回访检测标本
        */
        sampleType: string;
        /*
        * 血清标本阳性类型
        */
        serumType: string;
        /*
        * 献浆者性别
        */
        sex: string;
        /*
        * 审核状态
        */
        status: string;
        /*
        * 检品状态:NORMAL 正常, REJECT 拒收,OVERTIME 超期
        */
        testArticleStatus: string;
        /*
        * 运输状态: FREEZING 冷冻(≤-10℃),REFRIGERATE 冷藏(2-8℃)
        */
        transportStatus: string;
    }

    /*
    * /inspect/task/edit response
    */
    interface TaskEdit {

        /*
        * 检验项目编号
        */
        inspectItemCodes: Array<string>;
        /*
        * 标本编号
        */
        sampleNo: string;
    }

    /*
    * /inventory/labwarehouse/create response
    */
    interface LabwarehouseCreate {

        /*
        * 
        */
        description: string;
        /*
        * 
        */
        remark: string;
        /*
        * 
        */
        warehouseName: string;
        /*
        * 
        */
        warehouseNo: string;
    }

    /*
    * /inventory/labwarehouse/list request
    */
    interface MaterialListUsingGET {

    }

    /*
    * /inventory/labwarehouse/list响应数据
    */
    interface LabwarehouseList {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /inventory/materialwarehouse/create response
    */
    interface MaterialwarehouseCreate {

        /*
        * 
        */
        description: string;
        /*
        * 
        */
        remark: string;
        /*
        * 
        */
        warehouseName: string;
        /*
        * 
        */
        warehouseNo: string;
    }

    /*
    * /inventory/materialwarehouse/list request
    */
    interface LabListUsingGET {

    }

    /*
    * /inventory/materialwarehouse/list响应数据
    */
    interface MaterialwarehouseList {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /inventory/warehouse/update request
    */
    interface UpdateUsingGET {

    }

    /*
    * /inventory/warehouse/update响应数据
    */
    interface WarehouseUpdate {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: boolean;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/loginRecord/queryByPage response
    */
    interface LoginRecordQueryByPage {

        /*
        * 开始时间
        */
        createdAt: string;
        /*
        * 
        */
        dir: string;
        /*
        * 
        */
        orderBy: string;
        /*
        * 
        */
        pageNum: number;
        /*
        * 
        */
        pageSize: number;
    }

    /*
    * /report response
    */
    interface Report {

        /*
        * 
        */
        dataPublishTime: string;
        /*
        * 
        */
        dataPublisher: string;
        /*
        * 
        */
        digitalSign: string;
        /*
        * 
        */
        publishCount: number;
        /*
        * 
        */
        publishRemark: string;
        /*
        * 
        */
        qualifiedCount: number;
        /*
        * 
        */
        receiveBy: string;
        /*
        * 
        */
        receiveTime: string;
        /*
        * 
        */
        receivedCount: number;
        /*
        * 
        */
        reportBy: string;
        /*
        * 
        */
        reportTime: string;
        /*
        * 
        */
        sampleBatchNo: string;
        /*
        * 
        */
        status: string;
        /*
        * 
        */
        templateId: string;
        /*
        * 
        */
        transferCount: number;
        /*
        * 
        */
        transferFrom: string;
        /*
        * 
        */
        unqualifiedCount: number;
    }

    /*
    * /report/page request
    */
    interface PageQueryReportsUsingGET {

        /*
        * 
        */
        auditResult?: string;
        /*
        * 
        */
        auditStatus?: string;
        /*
        * 
        */
        auditTimeEnd?: string;
        /*
        * 
        */
        auditTimeStart?: string;
        /*
        * 
        */
        dataPublishTimeEnd?: string;
        /*
        * 
        */
        dataPublishTimeStart?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 动态文档Id
        */
        docId?: string;
        /*
        * 献浆者编号
        */
        donorNo?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 标本编号
        */
        orgSampleNo?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 
        */
        plasmaNo?: string;
        /*
        * 
        */
        receiveTimeEnd?: string;
        /*
        * 
        */
        receiveTimeStart?: string;
        /*
        * 
        */
        reportTimeEnd?: string;
        /*
        * 
        */
        reportTimeStart?: string;
        /*
        * 标本批次编号
        */
        sampleBatchNo?: string;
        /*
        * 
        */
        slurryTimeEnd?: string;
        /*
        * 
        */
        slurryTimeStart?: string;
        /*
        * 
        */
        status?: string;
        /*
        * 来源单位
        */
        transferFrom?: string;
    }

    /*
    * /report/page响应数据
    */
    interface ReportPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/import response
    */
    interface SampleImport {

    }

    /*
    * /sample/importOut/sample response
    */
    interface ImportOutSample {

        /*
        * 标本批号
        */
        batchNo: string;
    }

    /*
    * /sample/receive/apply response
    */
    interface ReceiveApply {

        /*
        * 申请备注
        */
        applyRemark: string;
        /*
        * 标本批号集合
        */
        batchNoList: Array<string>;
        /*
        * 运输最高温度
        */
        maxTemperature: number;
        /*
        * 运输最低温度
        */
        minTemperature: number;
        /*
        * 运输状态 FREEZING 冷冻(≤-10℃),REFRIGERATE 冷藏(2-8℃)
        */
        transportStatus: string;
    }

    /*
    * /sample/receive/get/batchNo request
    */
    interface QuerySampleDetailByBatchNoUsingGET_1 {

        /*
        * 标本批号
        */
        batchNo: string;
    }

    /*
    * /sample/receive/get/batchNo响应数据
    */
    interface GetBatchNo {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/receive/get/detail request
    */
    interface ReceiveGetDetailUsingGET {

        /*
        * 标本批号
        */
        batchNo: string;
    }

    /*
    * /sample/receive/get/detail响应数据
    */
    interface GetDetail {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/receive/query request
    */
    interface ReceiveQueryUsingGET {

        /*
        * 标本批号
        */
        batchNo: string;
    }

    /*
    * /sample/receive/query响应数据
    */
    interface ReceiveQuery {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/receive/two/page request
    */
    interface ReceiveApplyTwoPageUsingGET {

        /*
        * 一级列表标本接收审核id
        */
        auditId?: string;
        /*
        * 审核结果
        */
        auditResult?: string;
        /*
        * 检验批号
        */
        batchNo?: string;
        /*
        * 批接收状态
        */
        batchReceiveStatus?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 检验进程
        */
        inspectionProcess?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 浆站来源编本编号
        */
        orgSampleNo?: string;
        /*
        * 来源单位系统编码
        */
        originOrgCode?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 献浆者姓名
        */
        plasmaDonorName?: string;
        /*
        * 献浆者编号
        */
        plasmaDonorNo?: string;
        /*
        * 血浆编号
        */
        plasmaNo?: string;
        /*
        * 接收日期止
        */
        receiveDateEnd?: string;
        /*
        * 接收日期起
        */
        receiveDateStart?: string;
        /*
        * 标本分类
        */
        sampleClassification?: string;
        /*
        * 标本类型
        */
        sampleType?: string;
        /*
        * 采浆日期止
        */
        slurryDateEnd?: string;
        /*
        * 采浆日期起
        */
        slurryDateStart?: string;
        /*
        * 审核状态
        */
        status?: string;
        /*
        * 检品状态
        */
        testArticleStatus?: string;
        /*
        * 送检日期止
        */
        transferDateEnd?: string;
        /*
        * 送检日期起
        */
        transferDateStart?: string;
        /*
        * 运输状态
        */
        transportStatus?: string;
    }

    /*
    * /sample/receive/two/page响应数据
    */
    interface TwoPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/reject/apply response
    */
    interface RejectApply {

        /*
        * 申请备注
        */
        applyRemark: string;
        /*
        * 是否补样 TRUE 是,FALSE 否
        */
        needSupplement: string;
        /*
        * 拒收原因
        */
        refuseReason: string;
        /*
        * 业务主键集合
        */
        sampleNo: Array<string>;
    }

    /*
    * /sample/reject/apply/page request
    */
    interface RejectApplyPageUsingGET {

        /*
        * 审核ID
        */
        auditId?: string;
        /*
        * 审核结果:APPROVE 批准,REJECT 拒绝
        */
        auditResult?: string;
        /*
        * 检验批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 是否补样:TRUE 是,FALSE 否
        */
        needSupplement?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 标本编号
        */
        orgSampleNo?: string;
        /*
        * 来源单位系统编码
        */
        originOrgCode?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 献浆者姓名
        */
        plasmaDonorName?: string;
        /*
        * 献浆者编号
        */
        plasmaDonorNo?: string;
        /*
        * 标本接收状态:RECEIVING 待接收,RECEIVED 已接收,REJECTING 拒收待审核,REJECTED 已拒收
        */
        receiveStatus?: string;
        /*
        * 拒收原因
        */
        refuseReason?: string;
        /*
        * 拒绝日期止
        */
        rejectDateEnd?: string;
        /*
        * 拒绝日期起
        */
        rejectDateStart?: string;
        /*
        * 标本分类: PLASMA_SPECIMEN 血浆标本,SERUM_SPECIMEN 血清标本
        */
        sampleClassification?: string;
        /*
        * 标本类型: ORG_TEST_SAMPLE 集中化检测标本,COMPANY_TEST_SAMPLE 公司复检标本,FOLLOW_UP_TEST_SAMPLE 回访检测标本
        */
        sampleType?: string;
        /*
        * 采浆日期止
        */
        slurryDateEnd?: string;
        /*
        * 采浆日期起
        */
        slurryDateStart?: string;
        /*
        * 审核状态:TO_AUDIT 待审核,AUDITED 已审核
        */
        status?: string;
        /*
        * 检品状态: NORMAL 正常,REJECT 拒收,OVERTIME 超期
        */
        testArticleStatus?: string;
    }

    /*
    * /sample/reject/apply/page响应数据
    */
    interface ApplyPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/reject/audit/page request
    */
    interface RejectAuditPageUsingGET {

        /*
        * 审核ID
        */
        auditId?: string;
        /*
        * 审核结果:APPROVE 批准,REJECT 拒绝
        */
        auditResult?: string;
        /*
        * 检验批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 是否补样:TRUE 是,FALSE 否
        */
        needSupplement?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 标本编号
        */
        orgSampleNo?: string;
        /*
        * 来源单位系统编码
        */
        originOrgCode?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 献浆者姓名
        */
        plasmaDonorName?: string;
        /*
        * 献浆者编号
        */
        plasmaDonorNo?: string;
        /*
        * 标本接收状态:RECEIVING 待接收,RECEIVED 已接收,REJECTING 拒收待审核,REJECTED 已拒收
        */
        receiveStatus?: string;
        /*
        * 拒收原因
        */
        refuseReason?: string;
        /*
        * 拒绝日期止
        */
        rejectDateEnd?: string;
        /*
        * 拒绝日期起
        */
        rejectDateStart?: string;
        /*
        * 标本分类: PLASMA_SPECIMEN 血浆标本,SERUM_SPECIMEN 血清标本
        */
        sampleClassification?: string;
        /*
        * 标本类型: ORG_TEST_SAMPLE 集中化检测标本,COMPANY_TEST_SAMPLE 公司复检标本,FOLLOW_UP_TEST_SAMPLE 回访检测标本
        */
        sampleType?: string;
        /*
        * 采浆日期止
        */
        slurryDateEnd?: string;
        /*
        * 采浆日期起
        */
        slurryDateStart?: string;
        /*
        * 审核状态:TO_AUDIT 待审核,AUDITED 已审核
        */
        status?: string;
        /*
        * 检品状态: NORMAL 正常,REJECT 拒收,OVERTIME 超期
        */
        testArticleStatus?: string;
    }

    /*
    * /sample/reject/audit/page响应数据
    */
    interface AuditPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/reject/get/remark request
    */
    interface RejectQueryRemarkUsingGET {

        /*
        * 批号
        */
        batchNo: string;
        /*
        * 接收状态
        */
        receiveStatus: string;
        /*
        * 业务主键
        */
        sampleNo: string;
    }

    /*
    * /sample/reject/get/remark响应数据
    */
    interface GetRemark {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/reject/get/sampleNo request
    */
    interface RejectQueryDetailUsingGET {

        /*
        * 业务主键
        */
        sampleNo?: string;
    }

    /*
    * /sample/reject/get/sampleNo响应数据
    */
    interface GetSampleNo {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /systemManagement/dataBackup/backUp response
    */
    interface DataBackupBackUp {

    }

    /*
    * /systemManagement/dataBackup/download request
    */
    interface DownloadUsingGET_1 {

        /*
        * id
        */
        id?: string;
    }

    type DataBackupDownloadRes = object
    /*
    * /systemManagement/dataBackup/queryByPage response
    */
    interface DataBackupQueryByPage {

        /*
        * 
        */
        dir: string;
        /*
        * 
        */
        orderBy: string;
        /*
        * 
        */
        pageNum: number;
        /*
        * 
        */
        pageSize: number;
    }

    /*
    * /systemManagement/dataBackup/{id} response
    */
    interface DataBackupById {

    }

    /*
    * /systemManagement/securitySettings/queryByList} response
    */
    interface SecuritySettingsQueryByList {

    }

    /*
    * /templates/search response
    */
    interface TemplatesSearch {

        /*
        * 
        */
        fileId: string;
        /*
        * 
        */
        name: string;
        /*
        * 
        */
        standardNumber: string;
        /*
        * 
        */
        templateNo: string;
    }

    /*
    * /templates/{templateId}/templateVersions/{versionNumber}/file request
    */
    interface GetTemplateFileBodyUsingGET {

        /*
        * templateId
        */
        templateId: string;
        /*
        * versionNumber
        */
        versionNumber: number;
    }

    /*
    * /templates/{templateId}/templateVersions/{versionNumber}/file响应数据
    */
    interface ByTemplateIdVersionNumber {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /log/list-operation-log-page request
    */
    interface ListOperationLogPageUsingGET {

        /*
        * 操作人
        */
        createBy?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 结束时间
        */
        endTime?: string;
        /*
        * 事件操作
        */
        operationEvent?: string;
        /*
        * 功能模块
        */
        operationMode?: string;
        /*
        * 页面
        */
        operationPage?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 开始时间
        */
        startTime?: string;
    }

    /*
    * /log/list-operation-log-page响应数据
    */
    interface LogListOperationLogPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /log/list-audit-log-page request
    */
    interface ListAuditLogPageUsingGET {

        /*
        * 审核id
        */
        auditId?: string;
        /*
        * 功能模块
        */
        auditMode?: string;
        /*
        * 页面
        */
        auditPage?: string;
        /*
        * 审核结果
        */
        auditResult?: string;
        /*
        * 操作人
        */
        createBy?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 结束时间
        */
        endTime?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 开始时间
        */
        startTime?: string;
    }

    /*
    * /log/list-audit-log-page响应数据
    */
    interface LogListAuditLogPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/receive/oneApply/page request
    */
    interface ReceiveApplyOnePageUsingGET {

        /*
        * 一级列表标本接收审核id
        */
        auditId?: string;
        /*
        * 审核结果
        */
        auditResult?: string;
        /*
        * 检验批号
        */
        batchNo?: string;
        /*
        * 批接收状态
        */
        batchReceiveStatus?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 检验进程
        */
        inspectionProcess?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 浆站来源编本编号
        */
        orgSampleNo?: string;
        /*
        * 来源单位系统编码
        */
        originOrgCode?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 献浆者姓名
        */
        plasmaDonorName?: string;
        /*
        * 献浆者编号
        */
        plasmaDonorNo?: string;
        /*
        * 血浆编号
        */
        plasmaNo?: string;
        /*
        * 接收日期止
        */
        receiveDateEnd?: string;
        /*
        * 接收日期起
        */
        receiveDateStart?: string;
        /*
        * 标本分类
        */
        sampleClassification?: string;
        /*
        * 标本类型
        */
        sampleType?: string;
        /*
        * 采浆日期止
        */
        slurryDateEnd?: string;
        /*
        * 采浆日期起
        */
        slurryDateStart?: string;
        /*
        * 审核状态
        */
        status?: string;
        /*
        * 检品状态
        */
        testArticleStatus?: string;
        /*
        * 送检日期止
        */
        transferDateEnd?: string;
        /*
        * 送检日期起
        */
        transferDateStart?: string;
        /*
        * 运输状态
        */
        transportStatus?: string;
    }

    /*
    * /sample/receive/oneApply/page响应数据
    */
    interface OneApplyPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/receive/oneAudit/page request
    */
    interface ReceiveAuditOnePageUsingGET {

        /*
        * 一级列表标本接收审核id
        */
        auditId?: string;
        /*
        * 审核结果
        */
        auditResult?: string;
        /*
        * 检验批号
        */
        batchNo?: string;
        /*
        * 批接收状态
        */
        batchReceiveStatus?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 检验进程
        */
        inspectionProcess?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 浆站来源编本编号
        */
        orgSampleNo?: string;
        /*
        * 来源单位系统编码
        */
        originOrgCode?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 献浆者姓名
        */
        plasmaDonorName?: string;
        /*
        * 献浆者编号
        */
        plasmaDonorNo?: string;
        /*
        * 血浆编号
        */
        plasmaNo?: string;
        /*
        * 接收日期止
        */
        receiveDateEnd?: string;
        /*
        * 接收日期起
        */
        receiveDateStart?: string;
        /*
        * 标本分类
        */
        sampleClassification?: string;
        /*
        * 标本类型
        */
        sampleType?: string;
        /*
        * 采浆日期止
        */
        slurryDateEnd?: string;
        /*
        * 采浆日期起
        */
        slurryDateStart?: string;
        /*
        * 审核状态
        */
        status?: string;
        /*
        * 检品状态
        */
        testArticleStatus?: string;
        /*
        * 送检日期止
        */
        transferDateEnd?: string;
        /*
        * 送检日期起
        */
        transferDateStart?: string;
        /*
        * 运输状态
        */
        transportStatus?: string;
    }

    /*
    * /sample/receive/oneAudit/page响应数据
    */
    interface OneAuditPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /log/list-inspect-log-page request
    */
    interface ListInspectLogPageUsingGET {

        /*
        * 复核日期止
        */
        checkDateEnd?: string;
        /*
        * 复核日期起
        */
        checkDateStart?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 检验任务状态:TO_UPLOAD待上传数据/TO_CHECK待复核/CHECKED已复核
        */
        inspectDataStatus?: string;
        /*
        * 检验日期止
        */
        inspectDateEnd?: string;
        /*
        * 检验日期起
        */
        inspectDateStart?: string;
        /*
        * 检验项目编号
        */
        inspectItemCode?: string;
        /*
        * 检验结果:QUALIFIED合格/UNQUALIFIED不合格
        */
        inspectResult?: string;
        /*
        * 检验次数：INITIAL_INSPECT初检/RE_INSPECT复检
        */
        inspectTimes?: string;
        /*
        * 检验人
        */
        inspector?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 浆站标本编号
        */
        orgSampleNo?: string;
        /*
        * 来源单位
        */
        originOrgCode?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 质控品批号
        */
        qcBatchNo?: string;
        /*
        * 试剂批号
        */
        reagentBatchNo?: string;
        /*
        * 标本批号
        */
        sampleBatchNo?: string;
        /*
        * 标本编号
        */
        sampleNo?: string;
        /*
        * 检品状态:NORMAL正常/REJECT拒收/OVERTIME超期
        */
        testArticleStatus?: string;
    }

    /*
    * /log/list-inspect-log-page响应数据
    */
    interface LogListInspectLogPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/signatureRecord/checkExists request
    */
    interface CheckExistsUsingGET {

    }

    /*
    * /personalCenter/signatureRecord/checkExists响应数据
    */
    interface SignatureRecordCheckExists {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: string;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/signatureRecord/fileList request
    */
    interface GetFileListUsingGET {

    }

    /*
    * /personalCenter/signatureRecord/fileList响应数据
    */
    interface SignatureRecordFileList {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<string>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/signatureRecord/pushRecord response
    */
    interface SignatureRecordPushRecord {

        /*
        * IP地址
        */
        ip: string;
        /*
        * 报告ID
        */
        reportId: string;
        /*
        * 报告名
        */
        reportName: string;
        /*
        * 报告编号
        */
        reportNo: string;
        /*
        * 操作日期
        */
        reportTime: string;
        /*
        * 使用角色
        */
        roleName: string;
        /*
        * 签名ID
        */
        signatureId: string;
        /*
        * 用户
        */
        userName: string;
    }

    /*
    * /personalCenter/signatureRecord/queryPage response
    */
    interface SignatureRecordQueryPage {

        /*
        * 
        */
        dir: string;
        /*
        * 文件名
        */
        fileName: string;
        /*
        * 
        */
        orderBy: string;
        /*
        * 
        */
        pageNum: number;
        /*
        * 
        */
        pageSize: number;
        /*
        * 报告ID
        */
        reportId: string;
        /*
        * 操作时间
        */
        reportTime: string;
        /*
        * 签名ID
        */
        signatureId: string;
        /*
        * 操作人
        */
        userName: string;
    }

    /*
    * /personalCenter/signatureSettings/detail/{id} request
    */
    interface ViewSignatureUsingGET {

        /*
        * 
        */
        id: string;
        /*
        * id
        */
        id: string;
    }

    /*
    * /personalCenter/signatureSettings/detail/{id}响应数据
    */
    interface DetailById {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/signatureSettings/queryPage response
    */
    interface SignatureSettingsQueryPage {

        /*
        * 
        */
        dir: string;
        /*
        * 
        */
        orderBy: string;
        /*
        * 
        */
        pageNum: number;
        /*
        * 
        */
        pageSize: number;
    }

    /*
    * /personalCenter/signatureSettings/save response
    */
    interface SignatureSettingsSave {

        /*
        * 签名base64
        */
        signatureBase64: string;
        /*
        * 签名ID
        */
        signatureId: string;
    }

    /*
    * /personalCenter/signatureSettings/upload response
    */
    interface SignatureSettingsUpload {

    }

    /*
    * /api/centralized-lims/sample/station/sync response
    */
    interface StationSync {

    }

    /*
    * /systemManagement/securitySettings/query} response
    */
    interface SecuritySettingsQuery {

    }

    /*
    * /log/list-rounding-log-page request
    */
    interface ListRoundingLogPageUsingGET {

        /*
        * 保留小数
        */
        digits?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 结束时间
        */
        endTime?: string;
        /*
        * 字段名
        */
        fieldName?: string;
        /*
        * 页面
        */
        inspectItemCode?: string;
        /*
        * 功能模块
        */
        model?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 修约规则
        */
        roundingRule?: string;
        /*
        * 开始时间
        */
        startTime?: string;
    }

    /*
    * /log/list-rounding-log-page响应数据
    */
    interface LogListRoundingLogPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/signaturePassword/save response
    */
    interface SignaturePasswordSave {

    }

    /*
    * /personalCenter/signaturePassword/verif response
    */
    interface SignaturePasswordVerif {

    }

    /*
    * /laboratory/warehouse/page/audit request
    */
    interface AuditWarehousePageUsingGET {

        /*
        * 审核日期止
        */
        auditDataEnd?: string;
        /*
        * 审核日期起
        */
        auditDataStart?: string;
        /*
        * 审核id
        */
        auditId?: string;
        /*
        * 审核结果 APPROVE:批准,REJECT:拒绝
        */
        auditResult?: string;
        /*
        * 审核状态 TO_AUDIT:待审核,AUDITED:已审核
        */
        auditStatus?: string;
        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 有效日期止
        */
        expireDataEnd?: string;
        /*
        * 有效日期起
        */
        expireDataStart?: string;
        /*
        * 关键物料品类 试剂 REAGENT 质控品 QUALITY_CONTROL 其他 OTHER
        */
        keyMaterialCategory?: string;
        /*
        * 关键物料类型
        */
        keyMaterialTypeId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 使用原因
        */
        reasonId?: string;
        /*
        * 使用类型 OUT_CONSUME 物料消耗,OUT_SCRAP 物料报废
        */
        recordSource: string;
        /*
        * 登记日期止
        */
        registrantDataEnd?: string;
        /*
        * 登记日期起
        */
        registrantDataStart?: string;
        /*
        * 物料规格
        */
        specificationId?: string;
        /*
        * 供应商Id
        */
        supplierName?: string;
        /*
        * 领用库
        */
        targetWarehouseId: string;
    }

    /*
    * /laboratory/warehouse/page/audit响应数据
    */
    interface PageAudit {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/warehouse/page/in request
    */
    interface InWarehousePageUsingGET {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 有效日期止
        */
        expireDataEnd?: string;
        /*
        * 有效日期起
        */
        expireDataStart?: string;
        /*
        * 关键物料品类 试剂 REAGENT 质控品 QUALITY_CONTROL 其他 OTHER
        */
        keyMaterialCategory?: string;
        /*
        * 关键物料类型
        */
        keyMaterialTypeId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 接收日期止
        */
        receiveDataEnd?: string;
        /*
        * 接收日期起
        */
        receiveDataStart?: string;
        /*
        * 物料规格
        */
        specificationId?: string;
        /*
        * 供应商
        */
        supplierName?: string;
        /*
        * 领用库
        */
        targetWarehouseId: string;
    }

    /*
    * /laboratory/warehouse/page/in响应数据
    */
    interface PageIn {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/warehouse/page/manage request
    */
    interface WarehouseManagePageUsingGET {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 有效日期止
        */
        expireDataEnd?: string;
        /*
        * 有效日期起
        */
        expireDataStart?: string;
        /*
        * 是否只看库存大于0的数据 TRUE:开关打开,FALSE:开关关闭
        */
        flag: string;
        /*
        * 关键物料品类 REAGENT:试剂,QUALITY_CONTROL:质控品,OTHER:其他
        */
        keyMaterialCategory?: string;
        /*
        * 关键物料类型
        */
        keyMaterialTypeId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL:关键物料,NORMAL_MATERIAL:普通物料
        */
        materialType?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 物料规格
        */
        specificationId?: string;
        /*
        * 供应商
        */
        supplierName?: string;
        /*
        * 领用库
        */
        targetWarehouseId: string;
        /*
        * 物料单位
        */
        unitId?: string;
        /*
        * 
        */
        validFlag?: string;
    }

    /*
    * /laboratory/warehouse/page/manage响应数据
    */
    interface PageManage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /log/combo-box-list request
    */
    interface AuditComboBoxUsingGET {

    }

    /*
    * /log/combo-box-list响应数据
    */
    interface LogComboBoxList {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/warehouse/apply response
    */
    interface WarehouseApply {

        /*
        * 唯一标识
        */
        identify: string;
        /*
        * 使用原因
        */
        reasonId: string;
        /*
        * 使用类型 OUT_CONSUME 物料消耗,OUT_SCRAP 物料报废 
        */
        recordSource: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 使用数量
        */
        useCount: number;
    }

    /*
    * /laboratory/warehouse/page/manage/item request
    */
    interface WarehouseManageItemPageUsingGET {

        /*
        * 
        */
        dir?: string;
        /*
        * 唯一标识
        */
        identify: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
    }

    /*
    * /laboratory/warehouse/page/manage/item响应数据
    */
    interface ManageItem {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/warehouse/query/remark request
    */
    interface QueryRemarkUsingGET {

        /*
        * 
        */
        dir?: string;
        /*
        * 唯一标识
        */
        identify: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
    }

    /*
    * /laboratory/warehouse/query/remark响应数据
    */
    interface QueryRemark {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/instrument/delete response
    */
    interface InstrumentDelete {

        /*
        * 唯一标志集合
        */
        identifyList: Array<string>;
    }

    /*
    * /laboratory/instrument/page request
    */
    interface PageUsingGET {

        /*
        * 
        */
        dir?: string;
        /*
        * 设备名称
        */
        instrumentName?: string;
        /*
        * 设备编号
        */
        instrumentNo?: string;
        /*
        * 设备厂家
        */
        manufacturer?: string;
        /*
        * 设备型号
        */
        model?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 设备类型
        */
        type?: string;
    }

    /*
    * /laboratory/instrument/page响应数据
    */
    interface InstrumentPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/instrument/query request
    */
    interface QueryUsingGET {

    }

    /*
    * /laboratory/instrument/query响应数据
    */
    interface InstrumentQuery {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/instrument/query/identify request
    */
    interface UpdateActiveUsingGET {

        /*
        * 唯一标志
        */
        identify: string;
    }

    /*
    * /laboratory/instrument/query/identify响应数据
    */
    interface QueryIdentify {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/instrument/save response
    */
    interface InstrumentSave {

        /*
        * 是否启用 开启：Y，关闭：N
        */
        active: string;
        /*
        * 设备名称
        */
        instrumentName: string;
        /*
        * 设备编号
        */
        instrumentNo: string;
        /*
        * 设备厂家
        */
        manufacturer: string;
        /*
        * 设备型号
        */
        model: string;
        /*
        * 联系电话
        */
        phone: string;
        /*
        * 负责人
        */
        principal: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 点检日期
        */
        spotCheckedDate: string;
        /*
        * 设备类型
        */
        type: string;
    }

    /*
    * /laboratory/instrument/update response
    */
    interface InstrumentUpdate {

        /*
        * 是否启用 开启：Y，关闭：N
        */
        active: string;
        /*
        * 唯一标志
        */
        identify: string;
        /*
        * 设备名称
        */
        instrumentName: string;
        /*
        * 设备编号
        */
        instrumentNo: string;
        /*
        * 设备厂家
        */
        manufacturer: string;
        /*
        * 设备型号
        */
        model: string;
        /*
        * 联系电话
        */
        phone: string;
        /*
        * 负责人
        */
        principal: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 点检日期
        */
        spotCheckedDate: string;
        /*
        * 设备类型
        */
        type: string;
    }

    /*
    * /laboratory/instrument/update/active response
    */
    interface UpdateActive {

        /*
        * 是否启用 开启：Y，关闭：N
        */
        active: string;
        /*
        * 唯一标志
        */
        identify: string;
    }

    /*
    * /log/list-signature-log-page request
    */
    interface ListSignatureLogPageUsingGET {

        /*
        * 操作人
        */
        createBy?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 结束时间
        */
        endTime?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 报告ID
        */
        reportId?: string;
        /*
        * 文件名
        */
        reportName?: string;
        /*
        * 电子签名id
        */
        signatureId?: string;
        /*
        * 开始时间
        */
        startTime?: string;
    }

    /*
    * /log/list-signature-log-page响应数据
    */
    interface LogListSignatureLogPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /log/list-file-log-page request
    */
    interface ListFileLogPageUsingGET {

        /*
        * 操作人
        */
        createBy?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 结束时间
        */
        endTime?: string;
        /*
        * 文件id
        */
        fileId?: string;
        /*
        * 文件名称
        */
        fileName?: string;
        /*
        * 文件模板id
        */
        fileTemplateId?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 开始时间
        */
        startTime?: string;
    }

    /*
    * /log/list-file-log-page响应数据
    */
    interface LogListFileLogPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /log/list-login-log-page request
    */
    interface ListLoginLogPageUsingGET {

        /*
        * 操作人
        */
        createBy?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 结束时间
        */
        endTime?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 开始时间
        */
        startTime?: string;
        /*
        * 操作项目
        */
        type?: string;
    }

    /*
    * /log/list-login-log-page响应数据
    */
    interface LogListLoginLogPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /api/centralized-lims/bloodStation/saveAdtevakData response
    */
    interface BloodStationSaveAdtevakData {

    }

    type BloodStationSaveAdtevakDataRes = object
    /*
    * /report/{sampleBatchNo} request
    */
    interface GetReportUsingGET {

        /*
        * sampleBatchNo
        */
        sampleBatchNo: string;
    }

    /*
    * /report/{sampleBatchNo}响应数据
    */
    interface ReportBySampleBatchNo {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /report/{sampleBatchNo}/latest request
    */
    interface GetReportLatestVersionUsingGET {

        /*
        * sampleBatchNo
        */
        sampleBatchNo: string;
    }

    /*
    * /report/{sampleBatchNo}/latest响应数据
    */
    interface LatestBySampleBatchNo {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /data/count response
    */
    interface DataCount {

        /*
        * 统计维度, SIGNLE/BATCH, 默认SINGLE
        */
        dimension: string;
        /*
        * 统计指标, TO_RECEIVE/RECEIVED/INSPECT_SUBMISSION/TO_INSPECT/INSPECTED/TO_PUBLISH_DATA/TO_PUBLISH_REPORT/TO_AUDIT
        */
        indicator: string;
        /*
        * 是否展示环比趋势, true/false
        */
        showRatio: boolean;
        /*
        * 统计周期, WHOLE/MONTH/DAY, 默认WHOLE
        */
        statisticalCycle: string;
        /*
        * 数据统计时间范围, CUR_DAY/CUR_WEEK/CUR_MONTH/CUR_YEAR/LAST_DAY
        */
        statisticalTime: string;
    }

    /*
    * /inspect/datapub/list response
    */
    interface DatapubList {

        /*
        * 审核日期止
        */
        auditDateEnd: string;
        /*
        * 审核日期起
        */
        auditDateStart: string;
        /*
        * 审核结果:APPROVE批准/REJECT拒绝
        */
        auditResult: string;
        /*
        * 审核状态:TO_AUDIT待审核/AUDITED已审核
        */
        auditStatus: string;
        /*
        * 
        */
        dir: string;
        /*
        * 献浆日期止
        */
        donorDateEnd: string;
        /*
        * 献浆日期起
        */
        donorDateStart: string;
        /*
        * 献浆者姓名
        */
        donorName: string;
        /*
        * 献浆者编号
        */
        donorNo: string;
        /*
        * 是否获取标本详情信息
        */
        fetchSampleDetail: boolean;
        /*
        * 检验结果:QUALIFIED合格/UNQUALIFIED不合格
        */
        inspectResult: string;
        /*
        * 
        */
        orderBy: string;
        /*
        * 浆站标本编号
        */
        orgSampleNo: string;
        /*
        * 来源单位
        */
        originOrgCode: string;
        /*
        * 
        */
        pageNum: number;
        /*
        * 
        */
        pageSize: number;
        /*
        * 发布日期止
        */
        publishDateEnd: string;
        /*
        * 发布日期起
        */
        publishDateStart: string;
        /*
        * 发布状态:TO_PUBLISH待发布/TO_AUDIT待审核/PUBLISHED已发布
        */
        publishStatus: string;
        /*
        * 标本批号
        */
        sampleBatchNo: string;
        /*
        * 标本分类
        */
        sampleClassification: string;
        /*
        * 标本编号
        */
        sampleNo: string;
        /*
        * 标本类型
        */
        sampleType: string;
        /*
        * 检品状态: NORMAL正常/REJECT拒收/OVERTIME超期
        */
        testArticleStatus: string;
        /*
        * 不合格项目
        */
        unqualifiedItem: string;
    }

    /*
    * /systemManagement/securitySettings/queryPasswordChangeCycle request
    */
    interface QueryPasswordChangeCycleUsingGET {

    }

    /*
    * /systemManagement/securitySettings/queryPasswordChangeCycle响应数据
    */
    interface SecuritySettingsQueryPasswordChangeCycle {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: boolean;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /config/select-combo-box request
    */
    interface SelectComboBoxUsingGET {

        /*
        * 是否删除 0:未删除，1:删除
        */
        deleted?: number;
        /*
        * 菜单标识
        */
        menuIdentify?: string;
        /*
        * 是否启用 0:未启用，1:启用
        */
        status?: string;
    }

    /*
    * /config/select-combo-box响应数据
    */
    interface ConfigSelectComboBox {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /templates/{templateId} request
    */
    interface GetCurrentEffectiveTemplateVersionUsingGET {

        /*
        * templateId
        */
        templateId: string;
    }

    /*
    * /templates/{templateId}响应数据
    */
    interface TemplatesByTemplateId {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /fileUpload/download/{fileName} response
    */
    interface DownloadByFileName {

    }

    type DownloadByFileNameRes = object
    /*
    * /fileUpload/fileUpload response
    */
    interface FileUploadFileUpload {

    }

    /*
    * /personalCenter/messageNotifications/pushMessages response
    */
    interface MessageNotificationsPushMessages {

        /*
        * 
        */
        auditInfo: undefined;
        /*
        * 消息类型
        */
        messageType: string;
        /*
        * 权限功能id
        */
        permissionFeatureId: string;
        /*
        * 标题
        */
        title: string;
        /*
        * 跳转Url
        */
        url: string;
        /*
        * 
        */
        warningInfo: undefined;
    }

    /*
    * /personalCenter/messageNotifications/queryByKey request
    */
    interface QueryByKeyUsingGET {

        /*
        * key
        */
        key: string;
    }

    /*
    * /personalCenter/messageNotifications/queryByKey响应数据
    */
    interface MessageNotificationsQueryByKey {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/messageNotifications/queryByPage response
    */
    interface MessageNotificationsQueryByPage {

        /*
        * 
        */
        dir: string;
        /*
        * 1通知2告警3异常
        */
        messageType: number;
        /*
        * 
        */
        orderBy: string;
        /*
        * 
        */
        pageNum: number;
        /*
        * 
        */
        pageSize: number;
        /*
        * 状态0未读1已读
        */
        readFlag: number;
        /*
        * 日期
        */
        sendTime: string;
    }

    /*
    * /personalCenter/messageNotifications/readMessage response
    */
    interface MessageNotificationsReadMessage {

        /*
        * 消息唯一标识数组
        */
        ids: Array<string>;
    }

    /*
    * /personalCenter/messageNotifications/unReadCount request
    */
    interface UnReadCountUsingGET {

    }

    /*
    * /personalCenter/messageNotifications/unReadCount响应数据
    */
    interface MessageNotificationsUnReadCount {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: object;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/signaturePassword/exist request
    */
    interface SignaturePasswordExistUsingGET {

    }

    /*
    * /personalCenter/signaturePassword/exist响应数据
    */
    interface SignaturePasswordExist {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: boolean;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/signaturePassword/save/{password} response
    */
    interface SaveByPassword {

    }

    /*
    * /personalCenter/signaturePassword/verif/{password} response
    */
    interface VerifByPassword {

    }

    /*
    * /systemManagement/userManagement/queryByList request
    */
    interface QueryByListUsingGET_1 {

    }

    /*
    * /systemManagement/userManagement/queryByList响应数据
    */
    interface UserManagementQueryByList {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/warehouse/get/inventory request
    */
    interface GetInventoryUsingGET {

        /*
        * 是否删除 0:未删除，1:删除
        */
        deleted?: number;
        /*
        * 菜单标识
        */
        menuIdentify?: string;
        /*
        * 是否启用 0:未启用，1:启用
        */
        status?: string;
    }

    /*
    * /laboratory/warehouse/get/inventory响应数据
    */
    interface GetInventory {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/basic/inStock/remark/{identify} request
    */
    interface InStockRemarkUsingGET {

        /*
        * 物料唯一标识
        */
        identify: string;
    }

    /*
    * /material/basic/inStock/remark/{identify}响应数据
    */
    interface RemarkByIdentify {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/basic/material/page request
    */
    interface PageMaterialBaseInfoUsingGET {

        /*
        * 
        */
        dir?: string;
        /*
        * 英文简称
        */
        enShortName?: string;
        /*
        * 关键物料类型 引用配置页面内容
        */
        keyMaterialTypeId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 供应商
        */
        supplierName?: string;
        /*
        * 物料单位 配置表
        */
        unitId?: string;
    }

    /*
    * /material/basic/material/page响应数据
    */
    interface MaterialPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/basic/material/{identify} response
    */
    interface MaterialByIdentify {

    }

    /*
    * /material/basic/supplier/list request
    */
    interface ListSupplierUsingGET {

        /*
        * 物料标识
        */
        materialIdentify?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
    }

    /*
    * /material/basic/supplier/list响应数据
    */
    interface SupplierList {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/basic/supplier/page request
    */
    interface PageSupplierUsingGET {

        /*
        * 
        */
        dir?: string;
        /*
        * 英文简称
        */
        enShortName?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 审计要求 TRUE 是 FALSE 否
        */
        requireAudit?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
        /*
        * 供应商编号
        */
        supplierNo?: string;
        /*
        * 供应商类型，配置表数据
        */
        supplierTypeId?: string;
    }

    /*
    * /material/basic/supplier/page响应数据
    */
    interface SupplierPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/basic/supplier/{identify} response
    */
    interface SupplierByIdentify {

    }

    /*
    * /material/inventory/inStock/first/page request
    */
    interface InStockFirstPageUsingGET {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 库存大于0 TRUE 或者 FALSE
        */
        existsInventory?: string;
        /*
        * 有效日期止
        */
        expireDateDown?: string;
        /*
        * 有效日期起
        */
        expireDateUp?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 入库日期止
        */
        inWarehouseTimeDown?: string;
        /*
        * 入库日期起
        */
        inWarehouseTimeUp?: string;
        /*
        * 关键物料品类 试剂 REAGENT 质控品 QUALITY_CONTROL 其他 OTHER
        */
        keyMaterialCategory?: string;
        /*
        * 关键物料类型 引用配置页面内容
        */
        keyMaterialTypeId?: string;
        /*
        * 物料实例标识--二级列表查询必传
        */
        materialInstanceIdentify?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 生产日期止
        */
        productionDateDown?: string;
        /*
        * 生产日期起
        */
        productionDateUp?: string;
        /*
        * 物料规格,配置表数据
        */
        specificationId?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
        /*
        * 物料单位 配置表
        */
        unitId?: string;
        /*
        * 仓库地址,配置表数据
        */
        warehouseAddressId?: string;
        /*
        * 仓储区域 待检区WAITING 合格区 PASS 不合格区 NOPASS
        */
        warehouseArea?: string;
    }

    /*
    * /material/inventory/inStock/first/page响应数据
    */
    interface FirstPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/inventory/inStock/second/page request
    */
    interface InStockSecondPageUsingGET {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 库存大于0 TRUE 或者 FALSE
        */
        existsInventory?: string;
        /*
        * 有效日期止
        */
        expireDateDown?: string;
        /*
        * 有效日期起
        */
        expireDateUp?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 入库日期止
        */
        inWarehouseTimeDown?: string;
        /*
        * 入库日期起
        */
        inWarehouseTimeUp?: string;
        /*
        * 关键物料品类 试剂 REAGENT 质控品 QUALITY_CONTROL 其他 OTHER
        */
        keyMaterialCategory?: string;
        /*
        * 关键物料类型 引用配置页面内容
        */
        keyMaterialTypeId?: string;
        /*
        * 物料实例标识--二级列表查询必传
        */
        materialInstanceIdentify?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 生产日期止
        */
        productionDateDown?: string;
        /*
        * 生产日期起
        */
        productionDateUp?: string;
        /*
        * 物料规格,配置表数据
        */
        specificationId?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
        /*
        * 物料单位 配置表
        */
        unitId?: string;
        /*
        * 仓库地址,配置表数据
        */
        warehouseAddressId?: string;
        /*
        * 仓储区域 待检区WAITING 合格区 PASS 不合格区 NOPASS
        */
        warehouseArea?: string;
    }

    /*
    * /material/inventory/inStock/second/page响应数据
    */
    interface SecondPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/inventory/receive response
    */
    interface InventoryReceive {

        /*
        * 物料批号
        */
        batchNo: string;
        /*
        * 有效日期
        */
        expireDate: string;
        /*
        * 物料唯一标识
        */
        materialIdentify: string;
        /*
        * 是否抽检 TRUE 是 FALSE 否
        */
        needSpotCheck: string;
        /*
        * 生产日期
        */
        productionDate: string;
        /*
        * 质控品含量
        */
        qualityControlNumerical: number;
        /*
        * 入库数量
        */
        quantity: number;
        /*
        * 接收结果，验收合格 PASS 验收不合格 NO_PASS
        */
        receiveResult: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 规格,配置表数据
        */
        specificationId: string;
        /*
        * 仓库地址,配置表数据
        */
        warehouseAddressId: string;
        /*
        * 仓储区域 待检区WAITING 合格区 PASS 不合格区 NOPASS
        */
        warehouseArea: string;
    }

    /*
    * /material/inventory/receive/cancel response
    */
    interface ReceiveCancel {

        /*
        * 接收标识列表
        */
        identifiers: Array<string>;
        /*
        * 入库备注
        */
        remark: string;
    }

    /*
    * /material/inventory/receive/detail/{identify} request
    */
    interface ReceiveDetailUsingGET {

        /*
        * 物料接收标识
        */
        identify: string;
    }

    /*
    * /material/inventory/receive/detail/{identify}响应数据
    */
    interface DetailByIdentify {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/inventory/receive/edit response
    */
    interface ReceiveEdit {

        /*
        * 物料批号
        */
        batchNo: string;
        /*
        * 有效日期
        */
        expireDate: string;
        /*
        * 接收标识
        */
        identify: string;
        /*
        * 物料唯一标识
        */
        materialIdentify: string;
        /*
        * 是否抽检 TRUE 是 FALSE 否
        */
        needSpotCheck: string;
        /*
        * 生产日期
        */
        productionDate: string;
        /*
        * 质控品含量
        */
        qualityControlNumerical: number;
        /*
        * 入库数量
        */
        quantity: string;
        /*
        * 接收结果，验收合格 PASS 验收不合格 NO_PASS
        */
        receiveResult: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 规格,配置表数据
        */
        specificationId: string;
        /*
        * 仓库地址,配置表数据
        */
        warehouseAddressId: string;
        /*
        * 仓储区域 待检区WAITING 合格区 PASS 不合格区 NOPASS
        */
        warehouseArea: string;
    }

    /*
    * /material/inventory/receive/page request
    */
    interface ReceivePageUsingGET {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 有效日期止
        */
        expireDateDown?: string;
        /*
        * 有效日期起
        */
        expireDateUp?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 入库日期止
        */
        inWarehouseTimeDown?: string;
        /*
        * 入库日期起
        */
        inWarehouseTimeUp?: string;
        /*
        * 关键物料类型 引用配置页面内容
        */
        keyMaterialTypeId?: string;
        /*
        * 物料实例ID
        */
        materialInstanceId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 是否抽检 TRUE 是 FALSE 否
        */
        needSpotCheck?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 生产日期止
        */
        productionDateDown?: string;
        /*
        * 生产日期起
        */
        productionDateUp?: string;
        /*
        * 规格,配置表数据
        */
        specificationId?: string;
        /*
        * 入库状态 WAITING_STORAGE 待入库 STORAGED 已入库
        */
        storageStatus?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
        /*
        * 物料单位 配置表
        */
        unitId?: string;
        /*
        * 仓库地址,配置表数据
        */
        warehouseAddressId?: string;
        /*
        * 仓储区域 待检区WAITING 合格区 PASS 不合格区 NOPASS
        */
        warehouseArea?: string;
    }

    /*
    * /material/inventory/receive/page响应数据
    */
    interface ReceivePage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/inventory/receive/remark/{identify} request
    */
    interface ReceiveRemarkUsingGET {

        /*
        * 物料接收标识
        */
        identify: string;
    }

    /*
    * /material/inventory/storage response
    */
    interface InventoryStorage {

        /*
        * 接收标识列表
        */
        identifiers: Array<string>;
        /*
        * 入库备注
        */
        remark: string;
    }

    /*
    * /material/use/discharged response
    */
    interface UseDischarged {

        /*
        * 放行文件列表
        */
        passFiles: Array<passFilesVo>;
        /*
        * 放行备注
        */
        passRemark: string;
        /*
        * 放行结果
        */
        passResult: string;
        /*
        * 领用单标识
        */
        useFormIdentify: string;
    }

    /*
    * LocalFileDTO
    */
    interface passFilesVo {

        /*
        * 文件名
        */
        fileName: string;
        /*
        * 文件大小
        */
        fileSize: string;
        /*
        * 标识/下载地址
        */
        identify: string;
    }

    /*
    * /material/use/discharged/detail/{userFormIdentify} request
    */
    interface DischargedDetailUsingGET {

        /*
        * 物料领用唯一标识
        */
        userFormIdentify: string;
    }

    /*
    * /material/use/discharged/detail/{userFormIdentify}响应数据
    */
    interface DetailByUserFormIdentify {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/use/download/{userFormIdentify} request
    */
    interface DischargedDetailUsingGET_1 {

        /*
        * 物料领用唯一标识
        */
        userFormIdentify: string;
    }

    type DownloadByUserFormIdentifyRes = object
    /*
    * /material/use/out response
    */
    interface UseOut {

        /*
        * 备注
        */
        outRemark: string;
        /*
        * 接收人
        */
        receiver: string;
        /*
        * 用户领用单标识
        */
        useIdentifies: Array<string>;
    }

    /*
    * /material/use/out/page request
    */
    interface OutPageUsingGET {

        /*
        * 申请日期止
        */
        applicantTimeDown?: string;
        /*
        * 申请日期起
        */
        applicantTimeUp?: string;
        /*
        * 审核日期止
        */
        auditDateDown?: string;
        /*
        * 审核日期起
        */
        auditDateUp?: string;
        /*
        * 审核ID
        */
        auditId?: string;
        /*
        * 审核结果 待定
        */
        auditResult?: string;
        /*
        * 审核状态 TO_AUDIT 待审核  AUDITED 已审核
        */
        auditStatus?: string;
        /*
        * 审核类型 MATERIAL_SPOT_CHECK 抽检 MATERIAL_USE 领用 MATERIAL_SCRAP 报废 MATERIAL_RETURN 退货
        */
        auditType?: string;
        /*
        * 批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 关键物料类型
        */
        keyMaterialTypeId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 出库状态 WAITING_DELIVERY 待出库 DELIVERY 已出库
        */
        outStatus?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 放行结果 PASS 准予放行 NO_PASS 不予放行
        */
        passResult?: string;
        /*
        * 领用原因
        */
        reasonId?: string;
        /*
        * 物料规格
        */
        specificationId?: string;
        /*
        * 抽检状态  WAIT_SUBMIT 待提交 SUBMITED 已提交
        */
        status?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
        /*
        * 领用库
        */
        targetWarehouseId?: string;
        /*
        * 使用类别
        */
        useType?: string;
        /*
        * 仓库地址
        */
        warehouseAddressId?: string;
        /*
        * 仓库区域
        */
        warehouseArea?: string;
    }

    /*
    * /material/use/out/page响应数据
    */
    interface OutPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/use/outReturn response
    */
    interface UseOutReturn {

        /*
        * 入库单号
        */
        inWarehouseNo: string;
        /*
        * 物料实例唯一标识
        */
        materialInstanceIdentify: string;
        /*
        * 退货原因 关联配置表
        */
        reasonId: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 退货数量
        */
        useCount: number;
    }

    /*
    * /material/use/outScrap response
    */
    interface UseOutScrap {

        /*
        * 入库单号
        */
        inWarehouseNo: string;
        /*
        * 物料实例唯一标识
        */
        materialInstanceIdentify: string;
        /*
        * 报废原因 关联配置表
        */
        reasonId: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 报废数量
        */
        useCount: number;
        /*
        * 物料领用区域
        */
        warehouseArea: string;
    }

    /*
    * /material/use/outSpotCheck response
    */
    interface UseOutSpotCheck {

        /*
        * 入库单号
        */
        inWarehouseNo: string;
        /*
        * 物料实例唯一标识
        */
        materialInstanceIdentify: string;
    }

    /*
    * /material/use/outSpotCheck/history request
    */
    interface OutSpotCheckHistoryUsingGET {

        /*
        * 物料批号
        */
        batchNo: string;
        /*
        * 物料唯一标识
        */
        materialIdentify: string;
        /*
        * 规格,配置表数据
        */
        specificationId: string;
        /*
        * 
        */
        validSpecificationId?: string;
    }

    /*
    * /material/use/outSpotCheck/history响应数据
    */
    interface OutSpotCheckHistory {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: string;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/use/outSpotCheck/page request
    */
    interface OutSpotCheckPageUsingGET {

        /*
        * 申请日期止
        */
        applicantTimeDown?: string;
        /*
        * 申请日期起
        */
        applicantTimeUp?: string;
        /*
        * 审核日期止
        */
        auditDateDown?: string;
        /*
        * 审核日期起
        */
        auditDateUp?: string;
        /*
        * 审核ID
        */
        auditId?: string;
        /*
        * 审核结果 待定
        */
        auditResult?: string;
        /*
        * 审核状态 TO_AUDIT 待审核  AUDITED 已审核
        */
        auditStatus?: string;
        /*
        * 审核类型 MATERIAL_SPOT_CHECK 抽检 MATERIAL_USE 领用 MATERIAL_SCRAP 报废 MATERIAL_RETURN 退货
        */
        auditType?: string;
        /*
        * 批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 关键物料类型
        */
        keyMaterialTypeId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 出库状态 WAITING_DELIVERY 待出库 DELIVERY 已出库
        */
        outStatus?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 放行结果 PASS 准予放行 NO_PASS 不予放行
        */
        passResult?: string;
        /*
        * 领用原因
        */
        reasonId?: string;
        /*
        * 物料规格
        */
        specificationId?: string;
        /*
        * 抽检状态  WAIT_SUBMIT 待提交 SUBMITED 已提交
        */
        status?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
        /*
        * 领用库
        */
        targetWarehouseId?: string;
        /*
        * 使用类别
        */
        useType?: string;
        /*
        * 仓库地址
        */
        warehouseAddressId?: string;
        /*
        * 仓库区域
        */
        warehouseArea?: string;
    }

    /*
    * /material/use/outSpotCheck/page响应数据
    */
    interface OutSpotCheckPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/use/outSpotCheck/remark/{userIdentify} response
    */
    interface RemarkByUserIdentify {

    }

    /*
    * /material/use/outSpotCheck/revert response
    */
    interface OutSpotCheckRevert {

        /*
        * 物料领用单标识列表
        */
        identifiers: Array<string>;
    }

    /*
    * /material/use/outSpotCheck/submit response
    */
    interface OutSpotCheckSubmit {

        /*
        * 物料领用单标识列表
        */
        identifiers: Array<string>;
        /*
        * 数量
        */
        quality: number;
        /*
        * 备注
        */
        remark: string;
    }

    /*
    * /material/use/outSpotCheckAudit/page request
    */
    interface OutSpotCheckAuditPageUsingGET {

        /*
        * 申请日期止
        */
        applicantTimeDown?: string;
        /*
        * 申请日期起
        */
        applicantTimeUp?: string;
        /*
        * 审核日期止
        */
        auditDateDown?: string;
        /*
        * 审核日期起
        */
        auditDateUp?: string;
        /*
        * 审核ID
        */
        auditId?: string;
        /*
        * 审核结果 待定
        */
        auditResult?: string;
        /*
        * 审核状态 TO_AUDIT 待审核  AUDITED 已审核
        */
        auditStatus?: string;
        /*
        * 审核类型 MATERIAL_SPOT_CHECK 抽检 MATERIAL_USE 领用 MATERIAL_SCRAP 报废 MATERIAL_RETURN 退货
        */
        auditType?: string;
        /*
        * 批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 关键物料类型
        */
        keyMaterialTypeId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 出库状态 WAITING_DELIVERY 待出库 DELIVERY 已出库
        */
        outStatus?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 放行结果 PASS 准予放行 NO_PASS 不予放行
        */
        passResult?: string;
        /*
        * 领用原因
        */
        reasonId?: string;
        /*
        * 物料规格
        */
        specificationId?: string;
        /*
        * 抽检状态  WAIT_SUBMIT 待提交 SUBMITED 已提交
        */
        status?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
        /*
        * 领用库
        */
        targetWarehouseId?: string;
        /*
        * 使用类别
        */
        useType?: string;
        /*
        * 仓库地址
        */
        warehouseAddressId?: string;
        /*
        * 仓库区域
        */
        warehouseArea?: string;
    }

    /*
    * /material/use/outSpotCheckAudit/page响应数据
    */
    interface OutSpotCheckAuditPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/use/outUse response
    */
    interface UseOutUse {

        /*
        * 物料实例唯一标识
        */
        materialInstanceIdentify: string;
        /*
        * 领用原因 关联配置表
        */
        reasonId: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 领用库 关联配置表
        */
        targetWarehouseId: string;
        /*
        * 领用数量
        */
        useCount: number;
    }

    /*
    * /material/use/spotCheckPass/page request
    */
    interface SpotCheckPassPageUsingGET {

        /*
        * 审核日期止
        */
        auditDateDown?: string;
        /*
        * 审核日期起
        */
        auditDateUp?: string;
        /*
        * 审核状态 TO_AUDIT 待审核  AUDITED 已审核
        */
        auditStatus?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 入库日期止
        */
        inWarehouseTimeDown?: string;
        /*
        * 入库日期起
        */
        inWarehouseTimeUp?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 放行结果 PASS 准予放行 NO_PASS 不予放行
        */
        passResult?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
    }

    /*
    * /material/use/spotCheckPass/page响应数据
    */
    interface SpotCheckPassPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/use/spotCheckPass/remark/{useFormIdentify} request
    */
    interface SpotCheckPassRemarkUsingGET {

        /*
        * 物料领用唯一标识
        */
        useFormIdentify: string;
    }

    /*
    * /material/use/spotCheckPass/remark/{useFormIdentify}响应数据
    */
    interface RemarkByUseFormIdentify {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/use/validOutUse request
    */
    interface ValidOutUseUsingGET {

        /*
        * 物料实例唯一标识
        */
        materialInstanceIdentify: string;
    }

    /*
    * /material/use/validOutUse响应数据
    */
    interface UseValidOutUse {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/warn/expired/page request
    */
    interface PageExpiredUsingGET {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 剩余日期大于等于0 True或 False
        */
        existsExpired?: string;
        /*
        * 库存大于0 True或 False
        */
        existsInventory?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 供应商名称
        */
        supplierName?: string;
    }

    /*
    * /material/warn/expired/page响应数据
    */
    interface ExpiredPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /inspect/fourenzyme/read response
    */
    interface FourenzymeRead {

        /*
        * 
        */
        boardNo: string;
        /*
        * 
        */
        file: string;
        /*
        * 
        */
        fileData: Array<fileDataVo>;
        /*
        * 
        */
        fileName: string;
        /*
        * 
        */
        inControl: string;
        /*
        * 
        */
        inspectItemCode: string;
        /*
        * 
        */
        instrument: string;
        /*
        * 
        */
        qualityControlInfo: Array<qualityControlInfoVo>;
        /*
        * 
        */
        reagentId: string;
        /*
        * 
        */
        sourceModeCode: string;
        /*
        * 
        */
        sourceOperation: string;
        /*
        * 
        */
        sourcePageCode: string;
    }

    /*
    * FourEnzymeFileModel
    */
    interface fileDataVo {

        /*
        * 
        */
        dataType: string;
        /*
        * 
        */
        holeNo: string;
        /*
        * 
        */
        inspectOriginValue: string;
        /*
        * 
        */
        odValue: string;
        /*
        * 
        */
        qcName: string;
        /*
        * 
        */
        qcOriginValue: string;
        /*
        * 
        */
        ratio: string;
        /*
        * 
        */
        sampleNo: string;
    }

    /*
    * QualityControlInfo
    */
    interface qualityControlInfoVo {

        /*
        * 质控品批号(不用传)
        */
        batchNo: string;
        /*
        * 质控品含量(不用传)
        */
        content: string;
        /*
        * 质控品id
        */
        id: string;
        /*
        * 质控品类型(非必传,多个质控品时必传)
        */
        type: string;
    }

    /*
    * /inspect/proteinelec/read response
    */
    interface ProteinelecRead {

    }

    /*
    * /material/use/discharged/update response
    */
    interface DischargedUpdate {

        /*
        * 放行文件列表
        */
        passFiles: Array<passFilesVo>;
        /*
        * 领用单标识
        */
        useFormIdentify: string;
    }

    /*
    * /material/warn/inventory/page request
    */
    interface PageInventoryUsingGET {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 剩余日期大于等于0 True或 False
        */
        existsExpired?: string;
        /*
        * 库存大于0 True或 False
        */
        existsInventory?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 供应商名称
        */
        supplierName?: string;
    }

    /*
    * /material/warn/inventory/page响应数据
    */
    interface InventoryPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/signaturePassword/saveOrUpdate response
    */
    interface SignaturePasswordSaveOrUpdate {

        /*
        * 
        */
        password: string;
        /*
        * 
        */
        signaturePassword: string;
    }

    /*
    * /material/warn/supplier/page request
    */
    interface PageSupplierUsingGET_1 {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 剩余日期大于等于0 True或 False
        */
        existsExpired?: string;
        /*
        * 库存大于0 True或 False
        */
        existsInventory?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 供应商名称
        */
        supplierName?: string;
    }

    /*
    * /config/selectValueByCode request
    */
    interface SelectValueByCodeUsingGET {

    }

    /*
    * /config/selectValueByCode响应数据
    */
    interface ConfigSelectValueByCode {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: string;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /inspect/alt/read response
    */
    interface AltRead {

    }

    /*
    * /inspect/protein/read response
    */
    interface ProteinRead {

    }

    /*
    * /material/basic/material/detail/{identify} request
    */
    interface DetailMaterialUsingGET {

        /*
        * identify
        */
        identify: string;
    }

    /*
    * /material/basic/supplier/detail request
    */
    interface DetailSupplierUsingGET {

        /*
        * 物料标识
        */
        materialIdentify?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
    }

    /*
    * /material/basic/supplier/detail响应数据
    */
    interface SupplierDetail {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/basic/supplier/remark/{identify} request
    */
    interface SupplierRemarkUsingGET {

        /*
        * identify
        */
        identify: string;
    }

    /*
    * /material/inventory/batch/{materialInstanceIdentify} request
    */
    interface BatchUsingGET {

        /*
        * materialInstanceIdentify
        */
        materialInstanceIdentify: string;
    }

    /*
    * /material/inventory/batch/{materialInstanceIdentify}响应数据
    */
    interface BatchByMaterialInstanceIdentify {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/inventory/receive/cord/page request
    */
    interface ReceiveCordPageUsingGET {

        /*
        * 物料批号
        */
        batchNo?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 有效日期止
        */
        expireDateDown?: string;
        /*
        * 有效日期起
        */
        expireDateUp?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 入库日期止
        */
        inWarehouseTimeDown?: string;
        /*
        * 入库日期起
        */
        inWarehouseTimeUp?: string;
        /*
        * 关键物料类型 引用配置页面内容
        */
        keyMaterialTypeId?: string;
        /*
        * 物料实例ID
        */
        materialInstanceId?: string;
        /*
        * 物料名称
        */
        materialName?: string;
        /*
        * 物料编号
        */
        materialNo?: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType?: string;
        /*
        * 是否抽检 TRUE 是 FALSE 否
        */
        needSpotCheck?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 生产日期止
        */
        productionDateDown?: string;
        /*
        * 生产日期起
        */
        productionDateUp?: string;
        /*
        * 规格,配置表数据
        */
        specificationId?: string;
        /*
        * 入库状态 WAITING_STORAGE 待入库 STORAGED 已入库
        */
        storageStatus?: string;
        /*
        * 供应商名称
        */
        supplierName?: string;
        /*
        * 物料单位 配置表
        */
        unitId?: string;
        /*
        * 仓库地址,配置表数据
        */
        warehouseAddressId?: string;
        /*
        * 仓储区域 待检区WAITING 合格区 PASS 不合格区 NOPASS
        */
        warehouseArea?: string;
    }

    /*
    * /material/inventory/receive/cord/page响应数据
    */
    interface CordPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /personalCenter/loginRecord/queryYearsList response
    */
    interface LoginRecordQueryYearsList {

    }

    /*
    * /material/inventory/batch/edit response
    */
    interface BatchEdit {

        /*
        * 物料实例标识
        */
        identify: string;
        /*
        * 质控品含量
        */
        qualityControlNumerical: number;
    }

    /*
    * /inventory/materialwarehouse/statistics request
    */
    interface MaterialStatisticsUsingGET {

    }

    /*
    * /inventory/materialwarehouse/statistics响应数据
    */
    interface MaterialwarehouseStatistics {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /report/body request
    */
    interface GetReportLatestVersionUsingGET {

        /*
        * sampleBatchNo
        */
        sampleBatchNo: string;
        /*
        * versionNumber
        */
        versionNumber?: number;
    }

    /*
    * /report/body响应数据
    */
    interface ReportBody {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /report/info request
    */
    interface GetReportUsingGET {

        /*
        * sampleBatchNo
        */
        sampleBatchNo: string;
    }

    /*
    * /report/info响应数据
    */
    interface ReportInfo {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /templates/file request
    */
    interface GetCurrentEffectiveTemplateVersionUsingGET {

        /*
        * templateNo
        */
        templateNo: string;
        /*
        * versionNumber
        */
        versionNumber?: number;
    }

    /*
    * /templates/file响应数据
    */
    interface TemplatesFile {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /config/query/inspect request
    */
    interface QueryForInspectUsingGET {

    }

    /*
    * /config/query/inspect响应数据
    */
    interface QueryInspect {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: string;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/instrument/query/inspect request
    */
    interface QueryForInspectUsingGET_1 {

    }

    /*
    * /templates/all request
    */
    interface QueryOnePageAllTemplatesUsingGET {

        /*
        * 
        */
        auditResult?: string;
        /*
        * 
        */
        auditStatus?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 
        */
        fileId?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 
        */
        standardNumber?: string;
        /*
        * 
        */
        templateName?: string;
        /*
        * 
        */
        templateNo?: string;
    }

    /*
    * /templates/all响应数据
    */
    interface TemplatesAll {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /laboratory/warehouse/query/key request
    */
    interface QueryKeyUsingGET {

        /*
        * 试剂 REAGENT；质控品 QUALITY_CONTROL
        */
        key?: string;
        /*
        * 领用库名字
        */
        library?: string;
    }

    /*
    * /laboratory/warehouse/query/key响应数据
    */
    interface QueryKey {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /material/inventory/inStock/storageCard request
    */
    interface StorageCardUsingGET {

        /*
        * 
        */
        dir?: string;
        /*
        * 入库单号
        */
        inWarehouseNo?: string;
        /*
        * 
        */
        orderBy?: string;
    }

    /*
    * /material/inventory/inStock/storageCard响应数据
    */
    interface InStockStorageCard {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: Array<dataVo>;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/download/{batchCode} response
    */
    interface DownloadByBatchCode {

    }

    type DownloadByBatchCodeRes = object
    /*
    * /test/inspect/allinone response
    */
    interface InspectAllinone {

        /*
        * 
        */
        inspectItemCodes: Array<string>;
        /*
        * 
        */
        realSample: boolean;
        /*
        * 
        */
        sampleBatchNo: string;
        /*
        * 
        */
        sampleNos: Array<string>;
    }

    /*
    * /api/centralized-lims/bloodStation/upload response
    */
    interface BloodStationUpload {

    }

    type BloodStationUploadRes = object
    /*
    * /report/file request
    */
    interface DownloadUsingGET {

        /*
        * fileType
        */
        fileType: string;
        /*
        * params
        */
        params: string;
    }

    /*
    * /report/file响应数据
    */
    interface ReportFile {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: string;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/test/data request
    */
    interface StationSyncUsingGET {

        /*
        * batchNo
        */
        batchNo?: string;
    }

    /*
    * /sample/test/data响应数据
    */
    interface TestData {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /test/inspect/audit response
    */
    interface InspectAudit {

        /*
        * 
        */
        inspectItemCodes: Array<string>;
        /*
        * 
        */
        realSample: boolean;
        /*
        * 
        */
        sampleBatchNo: string;
        /*
        * 
        */
        sampleNos: Array<string>;
    }

    /*
    * /test/inspect/create response
    */
    interface InspectCreate {

        /*
        * 
        */
        inspectItemCodes: Array<string>;
        /*
        * 
        */
        realSample: boolean;
        /*
        * 
        */
        sampleBatchNo: string;
        /*
        * 
        */
        sampleNos: Array<string>;
    }

    /*
    * /test/inspect/publish response
    */
    interface InspectPublish {

        /*
        * 
        */
        inspectItemCodes: Array<string>;
        /*
        * 
        */
        realSample: boolean;
        /*
        * 
        */
        sampleBatchNo: string;
        /*
        * 
        */
        sampleNos: Array<string>;
    }

    /*
    * /api/centralized-lims/user/token/get response
    */
    interface TokenGet {

    }

    type TokenGetRes = object
    /*
    * /log/operationLogSave response
    */
    interface LogOperationLogSave {

        /*
        * 事件操作
        */
        operationEvent: string;
        /*
        * 功能模块
        */
        operationMode: string;
        /*
        * 页面
        */
        operationPage: string;
    }

    /*
    * /config/getValueByParamId request
    */
    interface GetValueByParamIdUsingGET {

    }

    /*
    * /config/getValueByParamId响应数据
    */
    interface ConfigGetValueByParamId {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: string;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/receive/get/detail/audit request
    */
    interface ReceiveGetDetailByAuditIdUsingGET {

        /*
        * 审核id
        */
        auditId: string;
        /*
        * 标本批号
        */
        batchNo: string;
    }

    /*
    * /sample/receive/get/detail/audit响应数据
    */
    interface DetailAudit {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/receive/twoAudit/page request
    */
    interface ReceiveAuditTwoPageUsingGET {

        /*
        * 一级列表标本接收审核id
        */
        auditId?: string;
        /*
        * 审核结果
        */
        auditResult?: string;
        /*
        * 检验批号
        */
        batchNo?: string;
        /*
        * 批接收状态
        */
        batchReceiveStatus?: string;
        /*
        * 
        */
        dir?: string;
        /*
        * 检验进程
        */
        inspectionProcess?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 浆站来源编本编号
        */
        orgSampleNo?: string;
        /*
        * 来源单位系统编码
        */
        originOrgCode?: string;
        /*
        * 
        */
        pageNum?: number;
        /*
        * 
        */
        pageSize?: number;
        /*
        * 献浆者姓名
        */
        plasmaDonorName?: string;
        /*
        * 献浆者编号
        */
        plasmaDonorNo?: string;
        /*
        * 血浆编号
        */
        plasmaNo?: string;
        /*
        * 接收日期止
        */
        receiveDateEnd?: string;
        /*
        * 接收日期起
        */
        receiveDateStart?: string;
        /*
        * 标本分类
        */
        sampleClassification?: string;
        /*
        * 标本类型
        */
        sampleType?: string;
        /*
        * 采浆日期止
        */
        slurryDateEnd?: string;
        /*
        * 采浆日期起
        */
        slurryDateStart?: string;
        /*
        * 审核状态
        */
        status?: string;
        /*
        * 检品状态
        */
        testArticleStatus?: string;
        /*
        * 送检日期止
        */
        transferDateEnd?: string;
        /*
        * 送检日期起
        */
        transferDateStart?: string;
        /*
        * 运输状态
        */
        transportStatus?: string;
    }

    /*
    * /sample/receive/twoAudit/page响应数据
    */
    interface TwoAuditPage {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /test/inspect/createTask response
    */
    interface InspectCreateTask {

        /*
        * 
        */
        inspectItemCodes: Array<string>;
        /*
        * 
        */
        realSample: boolean;
        /*
        * 
        */
        sampleBatchNo: string;
        /*
        * 
        */
        sampleNos: Array<string>;
    }

    /*
    * /sample/receive/get/audit/{auditId} request
    */
    interface QuerySampleDetailByBatchNoUsingGET {

        /*
        * auditId
        */
        auditId: string;
    }

    /*
    * /sample/receive/get/audit/{auditId}响应数据
    */
    interface AuditByAuditId {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/query/inspectItems request
    */
    interface QueryInspectItemsUsingGET {

        /*
        * sampleNo
        */
        sampleNo: string;
    }

    /*
    * /sample/query/inspectItems响应数据
    */
    interface QueryInspectItems {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/receive/get/audit request
    */
    interface QuerySampleDetailByBatchNoUsingGET {

        /*
        * auditId
        */
        auditId: string;
    }

    /*
    * /sample/receive/get/audit响应数据
    */
    interface GetAudit {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /sample/job/send response
    */
    interface JobSend {

    }

    /*
    * /personalCenter/signaturePassword/verify response
    */
    interface SignaturePasswordVerify {

        /*
        * 
        */
        password: string;
        /*
        * 
        */
        signaturePassword: string;
    }

    /*
    * /audit/tasks request
    */
    interface CreateUsingGET {

    }

    /*
    * /audit/tasks响应数据
    */
    interface AuditTasks {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: undefined;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

    /*
    * /inspect/singledata/unchecked response
    */
    interface SingledataUnchecked {

        /*
        * 
        */
        dir: string;
        /*
        * 检验项目
        */
        inspectItemCode: string;
        /*
        * 
        */
        orderBy: string;
        /*
        * 
        */
        pageNum: number;
        /*
        * 
        */
        pageSize: number;
    }

    /*
    * /test/inspect/task/compensate request
    */
    interface ReadProteinElecUsingGET {

        /*
        * sampleBatchNo
        */
        sampleBatchNo: string;
    }

    /*
    * /test/inspect/task/compensate响应数据
    */
    interface TaskCompensate {

        /*
        * 
        */
        code: number;
        /*
        * 
        */
        data: string;
        /*
        * 
        */
        message: string;
        /*
        * 
        */
        name: string;
    }

}

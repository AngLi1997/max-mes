declare namespace API {
    /*
    * /audit/create response
    */
    interface CreateAuditRequest {

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
        bizInfos: Array<AuditBizInfo>;
        /*
        * 
        */
        bizValue: string;
    }

    /*
    * AuditBizInfo
    */
    interface AuditBizInfo {

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
    * /audit/create响应数据
    */
    interface CreateAuditResponse {

        /*
        * 
        */
        bizAuditIdMap: object;
    }

    /*
    * /audit/execute response
    */
    interface ExecuteAuditRequest {

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
    interface ConfigDetailsResponse {

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
        detailList: Array<ConfigParameterDetail>;
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
    interface ConfigParameterDetail {

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
    interface ConfigDetailsResponse {

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
        detailList: Array<ConfigParameterDetail>;
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
    interface ConfigParameterDetail {

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
    interface AdtevakConfigResponse {

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
        * 操作人
        */
        createBy: string;
        /*
        * 操作时间
        */
        createTime: string;
        /*
        * ip
        */
        ip: string;
        /*
        * 菜单标识
        */
        menuIdentify: string;
        /*
        * 浆站密匙
        */
        secretKey: string;
        /*
        * 简称英文
        */
        shorterCode: string;
        /*
        * 简称
        */
        shorterName: string;
        /*
        * 是否删除
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
    interface InspectItemResponse {

        /*
        * 默认设备
        */
        defaultInstrument: string;
        /*
        * 默认设备编号
        */
        defaultInstrumentNo: string;
        /*
        * 检验项目描述
        */
        description: string;
        /*
        * 通用标准
        */
        details: Array<PassStandardDetails>;
        /*
        * 检验方式
        */
        inspectMethod: string;
        /*
        * 检验方式id
        */
        inspectMethodId: string;
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
        * 通过标准
        */
        passStandard: string;
        /*
        * 是否复检
        */
        relnspect: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 修约规则
        */
        roundingRule: string;
        /*
        * 修约规则
        */
        ruleDetail: Array<RoundingRuleDetail>;
        /*
        * 标准规定
        */
        standard: string;
        /*
        * 检验标准修改人
        */
        standardBy: string;
        /*
        * 检验标准修改时间
        */
        standardTime: string;
        /*
        * 修改人
        */
        updateBy: string;
        /*
        * 修改时间
        */
        updateTime: string;
    }

    /*
    * PassStandardDetails
    */
    interface PassStandardDetails {

        /*
        * 操作一
        */
        labelOne: string;
        /*
        * 操作二
        */
        labelTwo: string;
        /*
        * 判断字段名称
        */
        name: string;
        /*
        * 文本操作符
        */
        textOne: string;
        /*
        * 文本值
        */
        textValueOne: string;
        /*
        * 值
        */
        valueOne: string;
        /*
        * 值
        */
        valueTwo: string;
    }

    /*
    * RoundingRuleDetail
    */
    interface RoundingRuleDetail {

        /*
        * 保留位数
        */
        digits: string;
        /*
        * 名称
        */
        name: string;
        /*
        * 修约规则
        */
        ruleCode: string;
        /*
        * 修约规则值
        */
        ruleValue: string;
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
    interface GeneralMeunResponse {

        /*
        * 
        */
        list: Array<GeneralMeunResponse>;
        /*
        * 
        */
        menuIdentify: string;
        /*
        * 
        */
        meunName: string;
    }

    /*
    * /config/save response
    */
    interface SaveGeneralConfigRequest {

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
    interface SaveGeneralConfigRequest {

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
    interface UpdateAdtevakRequest {

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
    interface SaveItemRequest {

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
    interface SaveItemRuleRequest {

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
    * /inspect/task/create response
    */
    interface CreateInspectTaskRequest {

        /*
        * 标本列表
        */
        samples: Array<Sample>;
    }

    /*
    * Sample
    */
    interface Sample {

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
    interface UpdateInspectTaskRequest {

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
    interface CreateWarehouseRequest {

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
    interface ListWarehouseResponse {

        /*
        * 
        */
        result: Array<Warehouse>;
        /*
        * 
        */
        total: number;
    }

    /*
    * Warehouse
    */
    interface Warehouse {

        /*
        * 
        */
        active: string;
        /*
        * 
        */
        description: string;
        /*
        * 
        */
        ip: string;
        /*
        * 
        */
        name: string;
        /*
        * 
        */
        remark: string;
        /*
        * 
        */
        type: string;
        /*
        * 
        */
        warehouseNo: string;
    }

    /*
    * /inventory/materialwarehouse/create response
    */
    interface CreateWarehouseRequest {

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
    interface ListWarehouseResponse {

        /*
        * 
        */
        result: Array<Warehouse>;
        /*
        * 
        */
        total: number;
    }

    /*
    * Warehouse
    */
    interface Warehouse {

        /*
        * 
        */
        active: string;
        /*
        * 
        */
        description: string;
        /*
        * 
        */
        ip: string;
        /*
        * 
        */
        name: string;
        /*
        * 
        */
        remark: string;
        /*
        * 
        */
        type: string;
        /*
        * 
        */
        warehouseNo: string;
    }

    /*
    * /inventory/warehouse/update request
    */
    interface UpdateUsingGET {

    }

    /*
    * /personalCenter/loginRecord/queryByPage response
    */
    interface LoginRecordPageRequest {

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
    * /personalCenter/loginRecord/queryByPage响应数据
    */
    interface LoginRecordResponse {

        /*
        * 
        */
        result: Array<LoginRecord>;
        /*
        * 
        */
        total: number;
    }

    /*
    * LoginRecord
    */
    interface LoginRecord {

        /*
        * 
        */
        createdAt: string;
        /*
        * 
        */
        ip: string;
        /*
        * 
        */
        loginName: string;
        /*
        * 
        */
        result: string;
        /*
        * 
        */
        tenantId: string;
        /*
        * 
        */
        token: string;
        /*
        * 
        */
        type: string;
        /*
        * 
        */
        updatedAt: string;
        /*
        * 
        */
        userId: string;
    }

    /*
    * /report response
    */
    interface InspectReportCreateRequest {

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
    * /report响应数据
    */
    interface InspectReportResponse {

        /*
        * 
        */
        auditBy: string;
        /*
        * 
        */
        auditId: string;
        /*
        * 
        */
        auditRemark: string;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditStatus: string;
        /*
        * 
        */
        auditTime: string;
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
        docId: string;
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
    interface ReportListResponse {

        /*
        * 
        */
        result: Array<InspectReportResponse>;
        /*
        * 
        */
        total: number;
    }

    /*
    * InspectReportResponse
    */
    interface InspectReportResponse {

        /*
        * 
        */
        auditBy: string;
        /*
        * 
        */
        auditId: string;
        /*
        * 
        */
        auditRemark: string;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditStatus: string;
        /*
        * 
        */
        auditTime: string;
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
        docId: string;
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
    * /sample/import响应数据
    */
    interface Result {

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
    * /sample/importOut/sample response
    */
    interface 查询标本拒收详情 {

        /*
        * 标本批号
        */
        batchNo: string;
    }

    /*
    * /sample/receive/apply response
    */
    interface ReceiveApplyRequest {

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
    * /sample/receive/apply响应数据
    */
    interface Void {

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
    interface 标本拒收详情-包括审核单信息{

        /*
        * 
        */
        sample: undefined;
        /*
        * 标本拒收信息集合
        */
        sampleList: Array<Sample>;
    }

    /*
    * Sample
    */
    interface Sample {

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
    interface 标本数量详情 {

        /*
        * 批号
        */
        batchNo: string;
        /*
        * 接收数量
        */
        receiveCount: number;
        /*
        * 接收血浆标本
        */
        receivePlasmaSpecimen: number;
        /*
        * 接收血清标本
        */
        receiveSerumSpecimen: number;
        /*
        * 拒收数量
        */
        rejectCount: number;
        /*
        * 拒收血浆标本
        */
        rejectPlasmaSpecimen: number;
        /*
        * 拒收血清标本
        */
        rejectSerumSpecimen: number;
        /*
        * 请验数量
        */
        transferCount: number;
        /*
        * 请验血浆标本
        */
        transferPlasmaSpecimen: number;
        /*
        * 请验血清标本
        */
        transferSerumSpecimen: number;
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
    interface QuerySampleBatchResponse {

        /*
        * 
        */
        sampleBatch: undefined;
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
    interface B3和B4分页查询模型 {

        /*
        * 
        */
        result: Array<Sample>;
        /*
        * 
        */
        total: number;
    }

    /*
    * Sample
    */
    interface Sample {

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
    * /sample/reject/apply response
    */
    interface RejectApplyRequest {

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
    interface B3和B4分页查询模型 {

        /*
        * 
        */
        result: Array<Sample>;
        /*
        * 
        */
        total: number;
    }

    /*
    * Sample
    */
    interface Sample {

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
    interface B3和B4分页查询模型 {

        /*
        * 
        */
        result: Array<Sample>;
        /*
        * 
        */
        total: number;
    }

    /*
    * Sample
    */
    interface Sample {

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
    interface 标本备注信息：B3和B4{

        /*
        * 请验备注
        */
        applyRemark: string;
        /*
        * 接收备注
        */
        receiveRemark: string;
        /*
        * 拒收申请备注
        */
        rejectApplyRemark: string;
        /*
        * 拒收审核备注
        */
        rejectAuditRemark: string;
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
    interface 标本拒收详情-包括审核单信息{

        /*
        * 
        */
        sample: undefined;
        /*
        * 标本拒收信息集合
        */
        sampleList: Array<Sample>;
    }

    /*
    * Sample
    */
    interface Sample {

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
    * /systemManagement/dataBackup/download request
    */
    interface DownloadUsingGET_1 {

        /*
        * id
        */
        id?: string;
    }

    type downloadUsingGET_1Res = object
    /*
    * /systemManagement/dataBackup/queryByPage response
    */
    interface DataBackupRequest {

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
    * /systemManagement/dataBackup/queryByPage响应数据
    */
    interface DataBackupResponse {

        /*
        * 
        */
        result: Array<DataBackupInfo>;
        /*
        * 
        */
        total: number;
    }

    /*
    * DataBackupInfo
    */
    interface DataBackupInfo {

        /*
        * 备份结果 1成功，0失败，2未知
        */
        backupStatus: string;
        /*
        * 创建人
        */
        createBy: string;
        /*
        * 创建时间
        */
        createTime: string;
        /*
        * 服务器存放地址
        */
        filePath: string;
        /*
        * 文件大小
        */
        fileSize: number;
        /*
        * 下载地址
        */
        fileUrl: string;
        /*
        * 主键id
        */
        id: string;
        /*
        * ip
        */
        ip: string;
        /*
        * 操作人id
        */
        operateBy: string;
        /*
        * 操作日期
        */
        operateDate: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 更新人
        */
        updateBy: string;
        /*
        * 更新时间
        */
        updateTime: string;
    }

    /*
    * /systemManagement/securitySettings/queryByList}响应数据
    */
    interface SecuritySettingsResponse {

        /*
        * 创建人
        */
        createBy: string;
        /*
        * 创建时间
        */
        createTime: string;
        /*
        * ID
        */
        id: number;
        /*
        * IP
        */
        ip: string;
        /*
        * 操作人
        */
        operateBy: string;
        /*
        * 操作时间
        */
        operateTime: string;
        /*
        * 参数
        */
        parameters: string;
        /*
        * 项目名
        */
        projectName: string;
        /*
        * 安全项目ID
        */
        securityProjectId: string;
        /*
        * 单位
        */
        unit: string;
        /*
        * 更新人
        */
        updateBy: string;
        /*
        * 更新时间
        */
        updateTime: string;
    }

    /*
    * /templates/search response
    */
    interface DocTemplateListRequest {

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
    * /templates/search响应数据
    */
    interface DocTemplateListResponse {

        /*
        * 
        */
        result: Array<DocTemplateResponse>;
        /*
        * 
        */
        total: number;
    }

    /*
    * DocTemplateResponse
    */
    interface DocTemplateResponse {

        /*
        * 
        */
        auditBy: string;
        /*
        * 
        */
        auditId: string;
        /*
        * 
        */
        auditRemark: string;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditStatus: string;
        /*
        * 
        */
        auditTime: string;
        /*
        * 
        */
        buildNumber: string;
        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createRemark: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        effectiveDate: string;
        /*
        * 
        */
        expireDate: string;
        /*
        * 
        */
        fileId: string;
        /*
        * 
        */
        fileType: string;
        /*
        * 
        */
        standardNumber: string;
        /*
        * 
        */
        status: string;
        /*
        * 
        */
        templateName: string;
        /*
        * 
        */
        templateNo: string;
        /*
        * 
        */
        versionNumber: number;
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
    interface DocFile {

        /*
        * 
        */
        body: string;
        /*
        * 
        */
        fileId: string;
        /*
        * 
        */
        type: string;
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
    interface ListOperationLogResponse {

        /*
        * 
        */
        result: Array<OperationLogVO>;
        /*
        * 
        */
        total: number;
    }

    /*
    * OperationLogVO
    */
    interface OperationLogVO {

        /*
        * 操作人
        */
        createBy: string;
        /*
        * 操作日期
        */
        createTime: string;
        /*
        * id
        */
        id: number;
        /*
        * 
        */
        ip: string;
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
    interface ListAuditLogResponse {

        /*
        * 
        */
        result: Array<AuditLogVO>;
        /*
        * 
        */
        total: number;
    }

    /*
    * AuditLogVO
    */
    interface AuditLogVO {

        /*
        * 审核id
        */
        auditId: string;
        /*
        * 功能模块
        */
        auditMode: string;
        /*
        * 页面
        */
        auditPage: string;
        /*
        * 审核结果
        */
        auditResult: string;
        /*
        * 操作人
        */
        createBy: string;
        /*
        * 操作日期
        */
        createTime: string;
        /*
        * 主键id
        */
        id: number;
        /*
        * ip
        */
        ip: string;
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
    interface B1、B2分页查询{

        /*
        * 
        */
        result: Array<SampleBatch>;
        /*
        * 
        */
        total: number;
    }

    /*
    * SampleBatch
    */
    interface SampleBatch {

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
        * 流程id
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
        * 标本批号（业务主键）
        */
        batchNo: string;
        /*
        * 批接收状态:RECEIVING 待接收,AUDITING 待审核,RECEIVED 已接收
        */
        batchReceiveStatus: string;
        /*
        * 浆站承运人
        */
        carrierBy: string;
        /*
        * 浆站承运人电话
        */
        carrierPhone: string;
        /*
        * 检验进程:SAMPLE_RECEIVING 标本待接收,INSPECTION_PENDING 检验待执行,INSPECTION_PENDED 检验执行中,DATA_ISSUING 数据待签发,REPORT_ISSUING 报告待签发,REPORT_ISSUED 报告已签发
        */
        inspectionProcess: string;
        /*
        * 运输最高温度
        */
        maxTemperature: string;
        /*
        * 运输最低温度
        */
        minTemperature: string;
        /*
        * 来源单位系统名称
        */
        originOrgName: string;
        /*
        * 请验备注
        */
        originRemark: string;
        /*
        * 浆站装箱人
        */
        packBy: string;
        /*
        * 浆站装箱人电话
        */
        packPhone: string;
        /*
        * 接收数量
        */
        receiveCount: number;
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
        * 浆站标本外观检查:APPARENT_OK 外观完好,CLEAR_LABEL 标签清晰,ALL 两者都有
        */
        sampleAppearance: string;
        /*
        * 标本类型: ORG_TEST_SAMPLE 集中化检测标本,COMPANY_TEST_SAMPLE 公司复检标本,FOLLOW_UP_TEST_SAMPLE 回访检测标本
        */
        sampleType: string;
        /*
        * 审核状态
        */
        status: string;
        /*
        * 送检人
        */
        transferBy: string;
        /*
        * 请验数量
        */
        transferCount: number;
        /*
        * 送检日期
        */
        transferDate: string;
        /*
        * 运输状态:FREEZING 冷冻(≤-10℃),REFRIGERATE 冷藏(2-8℃)
        */
        transportStatus: string;
        /*
        * 浆站运输温度标准:POSITIVE 2~8℃,NEGATIVE -10℃以下
        */
        transportTemperatureStandard: string;
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
    interface B1、B2分页查询{

        /*
        * 
        */
        result: Array<SampleBatch>;
        /*
        * 
        */
        total: number;
    }

    /*
    * SampleBatch
    */
    interface SampleBatch {

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
        * 流程id
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
        * 标本批号（业务主键）
        */
        batchNo: string;
        /*
        * 批接收状态:RECEIVING 待接收,AUDITING 待审核,RECEIVED 已接收
        */
        batchReceiveStatus: string;
        /*
        * 浆站承运人
        */
        carrierBy: string;
        /*
        * 浆站承运人电话
        */
        carrierPhone: string;
        /*
        * 检验进程:SAMPLE_RECEIVING 标本待接收,INSPECTION_PENDING 检验待执行,INSPECTION_PENDED 检验执行中,DATA_ISSUING 数据待签发,REPORT_ISSUING 报告待签发,REPORT_ISSUED 报告已签发
        */
        inspectionProcess: string;
        /*
        * 运输最高温度
        */
        maxTemperature: string;
        /*
        * 运输最低温度
        */
        minTemperature: string;
        /*
        * 来源单位系统名称
        */
        originOrgName: string;
        /*
        * 请验备注
        */
        originRemark: string;
        /*
        * 浆站装箱人
        */
        packBy: string;
        /*
        * 浆站装箱人电话
        */
        packPhone: string;
        /*
        * 接收数量
        */
        receiveCount: number;
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
        * 浆站标本外观检查:APPARENT_OK 外观完好,CLEAR_LABEL 标签清晰,ALL 两者都有
        */
        sampleAppearance: string;
        /*
        * 标本类型: ORG_TEST_SAMPLE 集中化检测标本,COMPANY_TEST_SAMPLE 公司复检标本,FOLLOW_UP_TEST_SAMPLE 回访检测标本
        */
        sampleType: string;
        /*
        * 审核状态
        */
        status: string;
        /*
        * 送检人
        */
        transferBy: string;
        /*
        * 请验数量
        */
        transferCount: number;
        /*
        * 送检日期
        */
        transferDate: string;
        /*
        * 运输状态:FREEZING 冷冻(≤-10℃),REFRIGERATE 冷藏(2-8℃)
        */
        transportStatus: string;
        /*
        * 浆站运输温度标准:POSITIVE 2~8℃,NEGATIVE -10℃以下
        */
        transportTemperatureStandard: string;
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
    interface ListInspectDataResponse {

        /*
        * 
        */
        result: Array<InspectDataVO>;
        /*
        * 
        */
        total: number;
    }

    /*
    * InspectDataVO
    */
    interface InspectDataVO {

        /*
        * 复核人
        */
        checkBy: string;
        /*
        * 复核日期
        */
        checkTime: string;
        /*
        * 
        */
        extraInfo: undefined;
        /*
        * 
        */
        id: string;
        /*
        * 是否在控:Y/N
        */
        inControl: string;
        /*
        * 检验项目编号
        */
        inspectItemCode: string;
        /*
        * 检验项目名称
        */
        inspectItemName: string;
        /*
        * 检验结果
        */
        inspectResult: string;
        /*
        * 检验日期
        */
        inspectTime: string;
        /*
        * 检验次数
        */
        inspectTimes: string;
        /*
        * 检验值
        */
        inspectValue: string;
        /*
        * 检验人
        */
        inspector: string;
        /*
        * 检验设备
        */
        instrument: string;
        /*
        * 浆站标本编号
        */
        orgSampleNo: string;
        /*
        * 来源单位
        */
        originOrgCode: string;
        /*
        * 发布状态
        */
        publishStatus: string;
        /*
        * 质控品批号
        */
        qcBatchNo: string;
        /*
        * 质控品含量
        */
        qcContent: string;
        /*
        * 内质控值
        */
        qcValue: string;
        /*
        * 质控品信息
        */
        qualityControlInfo: Array<QualityControlInfo>;
        /*
        * 试剂批号
        */
        reagentBatchNo: string;
        /*
        * 标本批号
        */
        sampleBatchNo: string;
        /*
        * 标本编号
        */
        sampleNo: string;
        /*
        * 检品状态
        */
        testArticleStatus: string;
    }

    /*
    * QualityControlInfo
    */
    interface QualityControlInfo {

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
    * /personalCenter/signatureRecord/checkExists request
    */
    interface CheckExistsUsingGET {

    }

    /*
    * /personalCenter/signatureRecord/fileList request
    */
    interface GetFileListUsingGET {

    }

    /*
    * /personalCenter/signatureRecord/pushRecord response
    */
    interface SignatureRecordPushRequest {

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
    * /personalCenter/signatureRecord/pushRecord响应数据
    */
    interface Void {

    }

    /*
    * /personalCenter/signatureRecord/queryPage response
    */
    interface SignatureRecordRequest {

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
    * /personalCenter/signatureRecord/queryPage响应数据
    */
    interface SignatureRecordResponse {

        /*
        * 
        */
        result: Array<SignatureRecord>;
        /*
        * 
        */
        total: number;
    }

    /*
    * SignatureRecord
    */
    interface SignatureRecord {

        /*
        * 创建人
        */
        createBy: string;
        /*
        * 创建时间
        */
        createTime: string;
        /*
        * 主键id
        */
        id: string;
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
        * 更新人
        */
        updateBy: string;
        /*
        * 更新时间
        */
        updateTime: string;
        /*
        * 用户
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
    interface SignatureInfoResponse {

        /*
        * 生效日期
        */
        effectiveDate: string;
        /*
        * 失效日期
        */
        expirationDate: string;
        /*
        * ip
        */
        ip: string;
        /*
        * 签名id
        */
        signatureId: string;
        /*
        * 状态
        */
        status: string;
        /*
        * 图片地址url
        */
        url: string;
    }

    /*
    * /personalCenter/signatureSettings/queryPage response
    */
    interface SignatureInfoRequest {

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
    * /personalCenter/signatureSettings/queryPage响应数据
    */
    interface SignatureInfoPageResponse {

        /*
        * 
        */
        result: Array<SignatureInfo>;
        /*
        * 
        */
        total: number;
    }

    /*
    * SignatureInfo
    */
    interface SignatureInfo {

        /*
        * 创建人
        */
        createBy: string;
        /*
        * 创建时间
        */
        createTime: string;
        /*
        * 生效日期
        */
        effectiveDate: string;
        /*
        * 失效日期
        */
        expirationDate: string;
        /*
        * 主键id
        */
        id: string;
        /*
        * ip
        */
        ip: string;
        /*
        * 签名id
        */
        signatureId: string;
        /*
        * 状态
        */
        signatureStatus: string;
        /*
        * 更新人
        */
        updateBy: string;
        /*
        * 更新时间
        */
        updateTime: string;
        /*
        * 图片地址url
        */
        url: string;
    }

    /*
    * /personalCenter/signatureSettings/save response
    */
    interface SignatureInfoUploadRequest {

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
    * /personalCenter/signatureSettings/save响应数据
    */
    interface Void {

    }

    /*
    * /personalCenter/signatureSettings/upload响应数据
    */
    interface SignatureInfoUploadResponse {

        /*
        * 签名base64
        */
        signatureBase64: string;
        /*
        * 签名ID
        */
        signatureId: string;
        /*
        * 签名图片名称
        */
        signatureName: string;
    }

    /*
    * /systemManagement/securitySettings/query}响应数据
    */
    interface SecuritySettingsResponse {

        /*
        * 创建人
        */
        createBy: string;
        /*
        * 创建时间
        */
        createTime: string;
        /*
        * ID
        */
        id: number;
        /*
        * IP
        */
        ip: string;
        /*
        * 操作人
        */
        operateBy: string;
        /*
        * 操作时间
        */
        operateTime: string;
        /*
        * 参数
        */
        parameters: string;
        /*
        * 项目名
        */
        projectName: string;
        /*
        * 安全项目ID
        */
        securityProjectId: string;
        /*
        * 单位
        */
        unit: string;
        /*
        * 更新人
        */
        updateBy: string;
        /*
        * 更新时间
        */
        updateTime: string;
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
    interface ListRoundingResponse {

        /*
        * 
        */
        result: Array<RoundingLogVO>;
        /*
        * 
        */
        total: number;
    }

    /*
    * RoundingLogVO
    */
    interface RoundingLogVO {

        /*
        * 操作日期
        */
        createTime: string;
        /*
        * 保留小数
        */
        digits: string;
        /*
        * 字段名
        */
        fieldName: string;
        /*
        * 主键id
        */
        id: string;
        /*
        * 页面Code
        */
        inspectItemCode: string;
        /*
        * 页面
        */
        inspectItemName: string;
        /*
        * 原始数据
        */
        inspectOriginValue: string;
        /*
        * 功能模块
        */
        model: string;
        /*
        * 修约编码
        */
        roundingCode: string;
        /*
        * 修约规则名称
        */
        roundingName: string;
        /*
        * 映射修约规则
        */
        roundingRule: string;
        /*
        * 修约数据
        */
        roundingValue: number;
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
    interface E1-3：领用库消耗审核页面查询{

        /*
        * 
        */
        result: Array < E1 - 3、E1 - 4、E1 - 5、E1 - 6 领用库页面 >;
        /*
        * 
        */
        total: number;
    }

    /*
    * E1-3、E1-4、E1-5、E1-6 领用库页面
    */
    interface E1-3、E1 - 4、E1 - 5、E1 - 6 领用库页面{

        /*
        * 审核人
        */
        auditBy: string;
        /*
        * 审核结果
        */
        auditResult: string;
        /*
        * 审核状态
        */
        auditStatus: string;
        /*
        * 审核日期
        */
        auditTime: string;
        /*
        * 物料批号
        */
        batchNo: string;
        /*
        * 有效日期
        */
        expireDate: string;
        /*
        * 唯一标识(审核id)
        */
        identify: string;
        /*
        * 关键物料品类 试剂 REAGENT 质控品 QUALITY_CONTROL 其他 OTHER
        */
        keyMaterialCategory: string;
        /*
        * 关键物料类型 引用配置页面内容
        */
        keyMaterialTypeId: string;
        /*
        * 关键物料类型名称
        */
        keyMaterialTypeName: string;
        /*
        * 物料名称
        */
        materialName: string;
        /*
        * 物料编号
        */
        materialNo: string;
        /*
        * 使用原因
        */
        reasonId: string;
        /*
        * 使用原因名称 配置表
        */
        reasonName: string;
        /*
        * 登记人
        */
        registrant: string;
        /*
        * 登记日期
        */
        registrantTime: string;
        /*
        * 规格,配置表数据
        */
        specificationId: string;
        /*
        * 规格名称
        */
        specificationName: string;
        /*
        * 供应商名称
        */
        supplierName: string;
        /*
        * 使用数量
        */
        useCount: number;
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
    interface E2-1：入库查询页面{

        /*
        * 
        */
        result: Array < E1 - 2 领用库入库查询 >;
        /*
        * 
        */
        total: number;
    }

    /*
    * E1-2 领用库入库查询
    */
    interface E1-2 领用库入库查询{

        /*
        * 物料批号
        */
        batchNo: string;
        /*
        * 有效日期
        */
        expireDate: string;
        /*
        * 唯一标识
        */
        identify: string;
        /*
        * 入库数量
        */
        inWarehouseCount: number;
        /*
        * 关键物料品类 试剂 REAGENT 质控品 QUALITY_CONTROL 其他 OTHER
        */
        keyMaterialCategory: string;
        /*
        * 关键物料类型名称
        */
        keyMaterialTypeName: string;
        /*
        * 物料名称
        */
        materialName: string;
        /*
        * 物料编号
        */
        materialNo: string;
        /*
        * 物料库出库备注
        */
        materialOutRemark: string;
        /*
        * 物料备注
        */
        materialRemark: string;
        /*
        * 接收人
        */
        receiver: string;
        /*
        * 接收日期
        */
        receiverTime: string;
        /*
        * 规格名称
        */
        specificationName: string;
        /*
        * 供应商名称
        */
        supplierName: string;
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
    interface E1-1：领用库库存管理页面 一级{

        /*
        * 
        */
        result: Array < E1 - 1页面库存管理 % 2C一级页面 >;
        /*
        * 
        */
        total: number;
    }

    /*
    * E1-1页面库存管理%2C一级页面
    */
    interface E1-1页面库存管理 % 2C一级页面{

    }

    /*
    * /log/combo-box-list request
    */
    interface AuditComboBoxUsingGET {

    }

    /*
    * /log/combo-box-list响应数据
    */
    interface ComboBoxResponse {

        /*
        * 
        */
        inspect: Array<EnumList>;
        /*
        * 
        */
        inspectTimes: Array<EnumList>;
        /*
        * 
        */
        results: Array<EnumList>;
        /*
        * 
        */
        type: Array<EnumList>;
    }

    /*
    * EnumList
    */
    interface EnumList {

        /*
        * 
        */
        label: string;
        /*
        * 
        */
        value: string;
    }

    /*
    * /laboratory/warehouse/apply response
    */
    interface 物料报废、消耗申请Request{

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
    interface E1-1：领用库库存管理页面 二级{

        /*
        * 
        */
        result: Array < E1 - 1页面库存管理 % 2C二级页面 >;
        /*
        * 
        */
        total: number;
    }

    /*
    * E1-1页面库存管理%2C二级页面
    */
    interface E1-1页面库存管理 % 2C二级页面{

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
    interface 备注 {

        /*
        * 审核备注
        */
        auditRemark: string;
        /*
        * 消耗备注或者报废备注
        */
        useRemark: string;
    }

    /*
    * /laboratory/instrument/delete response
    */
    interface DeleteInstrumentRequest {

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
    interface ListInstrumentResponse {

        /*
        * 
        */
        result: Array<Instrument>;
        /*
        * 
        */
        total: number;
    }

    /*
    * Instrument
    */
    interface Instrument {

        /*
        * 是否启用 开启：Y，关闭：N
        */
        active: string;
        /*
        * 标志
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
        /*
        * 设备类型
        */
        typeName: string;
        /*
        * 操作人
        */
        updateBy: string;
        /*
        * 操作日期
        */
        updateTime: string;
    }

    /*
    * /laboratory/instrument/query request
    */
    interface QueryUsingGET {

    }

    /*
    * /laboratory/instrument/query响应数据
    */
    interface QueryInstrumentResponse {

        /*
        * 
        */
        instrument: undefined;
        /*
        * 
        */
        instrumentList: Array<Instrument>;
    }

    /*
    * Instrument
    */
    interface Instrument {

        /*
        * 是否启用 开启：Y，关闭：N
        */
        active: string;
        /*
        * 标志
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
        /*
        * 设备类型
        */
        typeName: string;
        /*
        * 操作人
        */
        updateBy: string;
        /*
        * 操作日期
        */
        updateTime: string;
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
    interface QueryInstrumentResponse {

        /*
        * 
        */
        instrument: undefined;
        /*
        * 
        */
        instrumentList: Array<Instrument>;
    }

    /*
    * Instrument
    */
    interface Instrument {

        /*
        * 是否启用 开启：Y，关闭：N
        */
        active: string;
        /*
        * 标志
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
        /*
        * 设备类型
        */
        typeName: string;
        /*
        * 操作人
        */
        updateBy: string;
        /*
        * 操作日期
        */
        updateTime: string;
    }

    /*
    * /laboratory/instrument/save response
    */
    interface E2% 3A保存仪器管理{

    }

    /*
    * /laboratory/instrument/update response
    */
    interface UpdateInstrumentRequest {

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
    interface UpdateActiveRequest {

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
    interface SignatureRecordResponse {

        /*
        * 
        */
        result: Array<SignatureRecord>;
        /*
        * 
        */
        total: number;
    }

    /*
    * SignatureRecord
    */
    interface SignatureRecord {

        /*
        * 创建人
        */
        createBy: string;
        /*
        * 创建时间
        */
        createTime: string;
        /*
        * 主键id
        */
        id: string;
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
        * 更新人
        */
        updateBy: string;
        /*
        * 更新时间
        */
        updateTime: string;
        /*
        * 用户
        */
        userName: string;
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
    interface ListFileLogResponse {

        /*
        * 
        */
        result: Array<FileTemplateLogVO>;
        /*
        * 
        */
        total: number;
    }

    /*
    * FileTemplateLogVO
    */
    interface FileTemplateLogVO {

        /*
        * 操作人
        */
        createBy: string;
        /*
        * 操作日期
        */
        createTime: string;
        /*
        * 文件id
        */
        fileId: string;
        /*
        * 文件名称
        */
        fileName: string;
        /*
        * 文件模板id
        */
        fileTemplateId: string;
        /*
        * 主键id
        */
        id: string;
        /*
        * ip地址
        */
        ip: string;
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
    interface ListLoginResponse {

        /*
        * 
        */
        result: Array<LoginPageResultVO>;
        /*
        * 
        */
        total: number;
    }

    /*
    * LoginPageResultVO
    */
    interface LoginPageResultVO {

        /*
        * 
        */
        createdAt: string;
        /*
        * 
        */
        id: string;
        /*
        * 
        */
        ip: string;
        /*
        * 
        */
        loginName: string;
        /*
        * 
        */
        result: string;
        /*
        * 
        */
        tenantId: string;
        /*
        * 
        */
        token: string;
        /*
        * 
        */
        type: string;
        /*
        * 
        */
        updatedAt: string;
        /*
        * 
        */
        userId: string;
    }

    type undefinedRes = object
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
    interface InspectReportResponse {

        /*
        * 
        */
        auditBy: string;
        /*
        * 
        */
        auditId: string;
        /*
        * 
        */
        auditRemark: string;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditStatus: string;
        /*
        * 
        */
        auditTime: string;
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
        docId: string;
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
    interface InspectReportResponse {

        /*
        * 
        */
        auditBy: string;
        /*
        * 
        */
        auditId: string;
        /*
        * 
        */
        auditRemark: string;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditStatus: string;
        /*
        * 
        */
        auditTime: string;
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
        docId: string;
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
    * /data/count response
    */
    interface CountInspectTaskRequest {

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
    * /data/count响应数据
    */
    interface CountInspectTaskResponse {

        /*
        * 统计起始时间
        */
        result: Array<StatisticalResult>;
    }

    /*
    * StatisticalResult
    */
    interface StatisticalResult {

        /*
        * 统计标签
        */
        label: string;
        /*
        * 与上一周期的环比
        */
        ratio: string;
        /*
        * 统计值
        */
        value: number;
    }

    /*
    * /inspect/datapub/list response
    */
    interface ListInspectDataPubRequest {

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
    * /inspect/datapub/list响应数据
    */
    interface ListInspectDataPubResponse {

        /*
        * 
        */
        result: Array<InspectTaskVO>;
        /*
        * 
        */
        total: number;
    }

    /*
    * InspectTaskVO
    */
    interface InspectTaskVO {

        /*
        * 审核人
        */
        auditBy: string;
        /*
        * 审核备注
        */
        auditRemark: string;
        /*
        * 审核结果
        */
        auditResult: string;
        /*
        * 审核状态
        */
        auditStatus: string;
        /*
        * 审核日期
        */
        auditTime: string;
        /*
        * 献浆者血型
        */
        donorBloodType: string;
        /*
        * 献浆者姓名
        */
        donorName: string;
        /*
        * 献浆者编号
        */
        donorNo: string;
        /*
        * 献浆者性别
        */
        donorSex: string;
        /*
        * 献浆时间
        */
        donorTime: string;
        /*
        * 固定检验项目
        */
        fixedInspectItems: Array<InspectItemInfo>;
        /*
        * 免疫类型
        */
        immunityType: string;
        /*
        * 单项检验数据信息
        */
        inspectItemList: Array<TaskItemInfo>;
        /*
        * 检验结论
        */
        inspectResult: string;
        /*
        * 检验状态
        */
        inspectStatus: string;
        /*
        * 浆站标本编号
        */
        orgSampleNo: string;
        /*
        * 来源单位
        */
        originOrgCode: string;
        /*
        * 血浆外观
        */
        plasmaAppearance: string;
        /*
        * 血浆编号
        */
        plasmaNo: string;
        /*
        * 审核ID
        */
        publishId: string;
        /*
        * 发布备注
        */
        publishRemark: string;
        /*
        * 发布状态
        */
        publishStatus: string;
        /*
        * 发布日期
        */
        publishTime: string;
        /*
        * 发布人
        */
        publisher: string;
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
        * 特殊检验项目
        */
        specialInspectItems: Array<InspectItemInfo>;
        /*
        * 检品状态
        */
        testArticleStatus: string;
    }

    /*
    * InspectItemInfo
    */
    interface InspectItemInfo {

        /*
        * 检验项目编号
        */
        code: string;
        /*
        * 检验项目名称
        */
        name: string;
        /*
        * 是否选中
        */
        selected: boolean;
    }

    /*
    * TaskItemInfo
    */
    interface TaskItemInfo {

        /*
        * 复核人
        */
        checkBy: string;
        /*
        * 检验项目编号
        */
        code: string;
        /*
        * 检验状态
        */
        inspectStatus: string;
        /*
        * 检验人
        */
        inspector: string;
        /*
        * 质控品批号
        */
        qcBatchNo: string;
        /*
        * 质控品信息
        */
        qcInfo: Array<QualityControlInfo>;
        /*
        * 试剂批号
        */
        reagentBatchNo: string;
        /*
        * 检验结果
        */
        result: string;
        /*
        * 是否为不合格
        */
        unqualified: boolean;
    }

    /*
    * QualityControlInfo
    */
    interface QualityControlInfo {

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
    * /systemManagement/securitySettings/queryPasswordChangeCycle request
    */
    interface QueryPasswordChangeCycleUsingGET {

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
    interface EnumList {

        /*
        * 
        */
        label: string;
        /*
        * 
        */
        value: string;
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
    interface DocTemplateVersionResponse {

        /*
        * 
        */
        auditBy: string;
        /*
        * 
        */
        auditId: string;
        /*
        * 
        */
        auditRemark: string;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditStatus: string;
        /*
        * 
        */
        auditTime: string;
        /*
        * 
        */
        buildNumber: string;
        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createRemark: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        effectiveDate: string;
        /*
        * 
        */
        expireDate: string;
        /*
        * 
        */
        fileId: string;
        /*
        * 
        */
        standardNumber: string;
        /*
        * 
        */
        status: string;
        /*
        * 
        */
        templateNo: string;
        /*
        * 
        */
        versionNumber: number;
    }

    type downLoadUsingPOSTRes = object
    /*
    * /fileUpload/fileUpload响应数据
    */
    interface LocalFileDTO {

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
    * /personalCenter/messageNotifications/pushMessages response
    */
    interface PushMessagesRequest {

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
    * /personalCenter/messageNotifications/pushMessages响应数据
    */
    interface Void {

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
    interface MessageNotificationsInfo {

        /*
        * 应用名称
        */
        appName: string;
        /*
        * 内容
        */
        content: string;
        /*
        * 接收时间
        */
        createdAt: string;
        /*
        * 发送者登录名
        */
        loginName: string;
        /*
        * 消息唯一id
        */
        msgId: string;
        /*
        * 0未读1已读
        */
        readFlag: number;
        /*
        * 0应用1用户
        */
        source: string;
        /*
        * 标题
        */
        title: string;
        /*
        * 1通知2告警3异常
        */
        type: number;
        /*
        * 消息跳转路径
        */
        url: string;
    }

    /*
    * /personalCenter/messageNotifications/queryByPage response
    */
    interface MessageNotificationsRequest {

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
    * /personalCenter/messageNotifications/queryByPage响应数据
    */
    interface MessageNotificationsResponse {

        /*
        * 
        */
        result: Array<MessageNotificationsInfo>;
        /*
        * 
        */
        total: number;
    }

    /*
    * MessageNotificationsInfo
    */
    interface MessageNotificationsInfo {

        /*
        * 应用名称
        */
        appName: string;
        /*
        * 内容
        */
        content: string;
        /*
        * 接收时间
        */
        createdAt: string;
        /*
        * 发送者登录名
        */
        loginName: string;
        /*
        * 消息唯一id
        */
        msgId: string;
        /*
        * 0未读1已读
        */
        readFlag: number;
        /*
        * 0应用1用户
        */
        source: string;
        /*
        * 标题
        */
        title: string;
        /*
        * 1通知2告警3异常
        */
        type: number;
        /*
        * 消息跳转路径
        */
        url: string;
    }

    /*
    * /personalCenter/messageNotifications/readMessage response
    */
    interface MessageQueryRequest {

        /*
        * 消息唯一标识数组
        */
        ids: Array<string>;
    }

    /*
    * /personalCenter/messageNotifications/readMessage响应数据
    */
    interface Void {

    }

    /*
    * /personalCenter/messageNotifications/unReadCount request
    */
    interface UnReadCountUsingGET {

    }

    /*
    * /personalCenter/messageNotifications/unReadCount响应数据
    */
    interface Map«string % 2Cint{

    }

    /*
    * /personalCenter/signaturePassword/exist request
    */
    interface SignaturePasswordExistUsingGET {

    }

    /*
    * /systemManagement/userManagement/queryByList request
    */
    interface QueryByListUsingGET_1 {

    }

    /*
    * /systemManagement/userManagement/queryByList响应数据
    */
    interface UserManagementResponse {

        /*
        * 
        */
        classify: string;
        /*
        * 
        */
        createdAt: string;
        /*
        * 
        */
        creator: string;
        /*
        * 
        */
        isMultiSessionAllowed: string;
        /*
        * 
        */
        loginName: string;
        /*
        * 
        */
        loginStatus: string;
        /*
        * 
        */
        nickname: string;
        /*
        * 
        */
        sign: string;
        /*
        * 
        */
        status: string;
        /*
        * 
        */
        tenantId: string;
        /*
        * 
        */
        updatedAt: string;
        /*
        * 
        */
        userId: string;
        /*
        * 
        */
        userType: string;
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
    interface EnumList {

        /*
        * 
        */
        label: string;
        /*
        * 
        */
        value: string;
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
    interface RemarkResponse% 3A物料备注Response{

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
    interface PageMaterialBaseInfoResponse% 3A物料基础信息分页查询列表{

    }

    /*
    * /material/basic/material/{identify}响应数据
    */
    interface Void {

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
    interface ListSupplierResponse {

        /*
        * 供应商标识
        */
        identify: string;
        /*
        * 供应商名称
        */
        supplierName: string;
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
    interface PageSupplierResponse% 3A供应分页查询列表{

    }

    /*
    * /material/basic/supplier/{identify}响应数据
    */
    interface Void {

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
    interface PageReceiveMaterialResponse% 3AF4 - 物料库存管理一级分页查询列表{

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
    interface PageSecondInstockMaterialResponse% 3AF4 - 物料库存管理二级分页查询列表{

    }

    /*
    * /material/inventory/receive response
    */
    interface ReceiveMaterialRequest% 3A物料接收Request{

    }

    /*
    * /material/inventory/receive响应数据
    */
    interface ReceiveMaterialResponse% 3A物料接收Response{

    }

    /*
    * /material/inventory/receive/cancel response
    */
    interface StorageMaterialRequest% 3A物料入库Request{

    }

    /*
    * /material/inventory/receive/cancel响应数据
    */
    interface Void {

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
    interface ReceiveMaterialDetailResponse% 3A物料接收详情Request{

    }

    /*
    * /material/inventory/receive/edit response
    */
    interface UpdateReceiveMaterialRequest% 3A物料接收接收Request{

    }

    /*
    * /material/inventory/receive/edit响应数据
    */
    interface ReceiveMaterialResponse% 3A物料接收Response{

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
    interface PageReceiveMaterialResponse% 3AF3 - 物料入库分页查询列表{

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
    * /material/inventory/receive/remark/{identify}响应数据
    */
    interface RemarkResponse% 3A物料备注Response{

    }

    /*
    * /material/inventory/storage response
    */
    interface StorageMaterialRequest% 3A物料入库Request{

    }

    /*
    * /material/inventory/storage响应数据
    */
    interface Void {

    }

    /*
    * /material/use/discharged response
    */
    interface DischargedRequest% 3A物料抽检放行{

    }

    /*
    * /material/use/discharged响应数据
    */
    interface Void {

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
    interface LocalFileDTO {

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
    * /material/use/download/{userFormIdentify} request
    */
    interface DischargedDetailUsingGET_1 {

        /*
        * 物料领用唯一标识
        */
        userFormIdentify: string;
    }

    type dischargedDetailUsingGET_1Res = object
    /*
    * /material/use/out response
    */
    interface OutRequest% 3A物料出库Request{

    }

    /*
    * /material/use/out响应数据
    */
    interface Void {

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
    interface PageOutSpotCheckResponse% 3A物料抽检分页查询Response{

    }

    /*
    * /material/use/outReturn response
    */
    interface OutUseRequest% 3A物料退货Request{

    }

    /*
    * /material/use/outReturn响应数据
    */
    interface Void {

    }

    /*
    * /material/use/outScrap response
    */
    interface OutUseRequest% 3A物料报废Request{

    }

    /*
    * /material/use/outScrap响应数据
    */
    interface Void {

    }

    /*
    * /material/use/outSpotCheck response
    */
    interface OutSpotCheckRequest% 3A物料抽检Request{

    }

    /*
    * /material/use/outSpotCheck响应数据
    */
    interface Void {

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
    interface PageOutSpotCheckResponse% 3A物料抽检分页查询Response{

    }

    /*
    * /material/use/outSpotCheck/remark/{userIdentify}响应数据
    */
    interface RemarkResponse% 3A物料备注Response{

    }

    /*
    * /material/use/outSpotCheck/revert response
    */
    interface UseSubmitRequest% 3A物料撤销抽检Request{

    }

    /*
    * /material/use/outSpotCheck/revert响应数据
    */
    interface Void {

    }

    /*
    * /material/use/outSpotCheck/submit response
    */
    interface UseSubmitRequest% 3A物料提交抽检 - 撤销抽检Request{

    }

    /*
    * /material/use/outSpotCheck/submit响应数据
    */
    interface Void {

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
    interface PageOutSpotCheckResponse% 3A物料抽检分页查询Response{

    }

    /*
    * /material/use/outUse response
    */
    interface OutUseRequest% 3A物料领用Request{

    }

    /*
    * /material/use/outUse响应数据
    */
    interface Void {

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
    interface PageOutResponse% 3A物料抽检分页查询Response{

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
    interface RemarkResponse% 3A物料备注Response{

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
    interface OutUseResponse% 3A物料领用Response{

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
    interface PageWarnResponse% 3A物料库预警分页结果{

    }

    /*
    * /inspect/fourenzyme/read response
    */
    interface UploadFileDataRequest {

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
        fileData: Array<FourEnzymeFileModel>;
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
        qualityControlInfo: Array<QualityControlInfo>;
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
    interface FourEnzymeFileModel {

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
    interface QualityControlInfo {

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
    * /material/use/discharged/update response
    */
    interface UpdateDischargedRequest% 3A物料抽检放行编辑提交{

    }

    /*
    * /material/use/discharged/update响应数据
    */
    interface Void {

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
    interface PageWarnResponse% 3A物料库预警分页结果{

    }

    /*
    * /personalCenter/signaturePassword/saveOrUpdate response
    */
    interface SignaturePasswordRequest {

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
    * /material/warn/supplier/page响应数据
    */
    interface PageSupplierWarnResponse% 3A物料库供应商分页结果{

    }

    /*
    * /config/selectValueByCode request
    */
    interface SelectValueByCodeUsingGET {

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
    * /material/basic/material/detail/{identify}响应数据
    */
    interface MaterialBaseInfoBO {

        /*
        * 英文简称
        */
        enShortName: string;
        /*
        * 物料标识
        */
        identify: string;
        /*
        * 关键物料品类 试剂 REAGENT 质控品 QUALITY_CONTROL 其他 OTHER
        */
        keyMaterialCategory: string;
        /*
        * 关键物料类型 引用配置页面内容
        */
        keyMaterialTypeId: string;
        /*
        * 关键物料类型名称
        */
        keyMaterialTypeName: string;
        /*
        * 物料名称
        */
        materialName: string;
        /*
        * 物料编号
        */
        materialNo: string;
        /*
        * 物料类型 CORE_MATERIAL 关键物料 NORMAL_MATERIAL 普通物料
        */
        materialType: string;
        /*
        * 最小库存数量
        */
        minInventory: number;
        /*
        * 备注
        */
        remark: string;
        /*
        * 供应商--关联标识
        */
        supplierIdentify: string;
        /*
        * 供应商名称
        */
        supplierName: string;
        /*
        * 物料单位 配置表
        */
        unitId: string;
        /*
        * 物料单位名称
        */
        unitName: string;
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
    interface SupplierBO {

        /*
        * 地址
        */
        address: string;
        /*
        * 中文简称
        */
        cnShortName: string;
        /*
        * 负责人
        */
        contactPerson: string;
        /*
        * 联系方式
        */
        contactPhone: string;
        /*
        * 英文简称
        */
        enShortName: string;
        /*
        * 有效日期
        */
        expireDate: string;
        /*
        * 唯一标识
        */
        identify: string;
        /*
        * 物料标识
        */
        materialIdentify: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 审计要求对象包装，显示requireAudit.label {'label':label,'value':value}
        */
        requireAudit: string;
        /*
        * 供应商名称
        */
        supplierName: string;
        /*
        * 供应商编号
        */
        supplierNo: string;
        /*
        * 供应商类型 关联数据 配置表获取
        */
        supplierTypeId: string;
        /*
        * 供应商类型名称 关联数据 配置表获取
        */
        supplierTypeName: string;
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
    interface MaterialInstanceDetailBO {

        /*
        * 可用库存量
        */
        availableStock: number;
        /*
        * 批号
        */
        batchNo: string;
        /*
        * 有效日期
        */
        expireDate: string;
        /*
        * 冻结总量
        */
        freezeInventory: number;
        /*
        * 
        */
        identify: string;
        /*
        * 结存总量
        */
        inventory: number;
        /*
        * 生产日期
        */
        productionDate: string;
        /*
        * 质控品含量
        */
        qualityControlNumerical: string;
        /*
        * 规格 关联配置表
        */
        specificationId: string;
        /*
        * 规格 关联配置表
        */
        specificationName: string;
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
    interface PageReceiveMaterialResponse% 3AF3 - 物料入库分页查询列表{

    }

    /*
    * /sample/station/sync response
    */
    interface 浆站数据同步模型 {

        /*
        * 标本批号
        */
        batchNo: string;
        /*
        * 浆站承运人
        */
        carrierBy: string;
        /*
        * 浆站承运人电话
        */
        carrierPhone: string;
        /*
        * 标本信息集合
        */
        list: Array<浆站同步数据>;
        /*
        * 来源单位编码
        */
        originOrgCode: string;
        /*
        * 请验备注
        */
        originRemark: string;
        /*
        * 浆站装箱人
        */
        packBy: string;
        /*
        * 浆站装箱人电话
        */
        packPhone: string;
        /*
        * 浆站标本外观检查 APPARENT_OK:外观完好,CLEAR_LABEL:标签清晰,ALL:外观完好和标签清晰
        */
        sampleAppearance: string;
        /*
        * 送检人
        */
        transferBy: string;
        /*
        * 请验数量
        */
        transferCount: number;
        /*
        * 送检日期
        */
        transferDate: string;
        /*
        * 浆站运输温度标准 POSITIVE:2~8℃,NEGATIVE:-10℃以下
        */
        transportTemperatureStandard: string;
    }

    /*
    * 浆站同步数据
    */
    interface 浆站同步数据 {

        /*
        * 血浆外观
        */
        appearance: string;
        /*
        * 献浆者血型
        */
        bloodType: string;
        /*
        * 标本箱号
        */
        boxId: string;
        /*
        * 免疫类型 NORMAL:普通,HA:甲肝,HB:乙肝,RABIES:狂犬,TETANUS:破伤风,ANTHRAX:炭疽
        */
        immunityType: string;
        /*
        * 检验项目，多个检验项目用英文逗号隔开 IP001:蛋白质含量,IP002:ALT,IP003:HBsAg,IP004:抗-HCV,IP005:抗-HIV,IP006:抗-TP,IP010:蛋白电泳
        */
        inspectionItems: string;
        /*
        * 浆站来源标本编号
        */
        orgSampleNo: string;
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
        * 标本分类 PLASMA_SPECIMEN:血浆标本,SERUM_SPECIMEN:血清标本
        */
        sampleClassification: string;
        /*
        * 标本类型 ORG_TEST_SAMPLE:集中化检测标本,COMPANY_TEST_SAMPLE:公司复检标本,FOLLOW_UP_TEST_SAMPLE:回访检测标本
        */
        sampleType: string;
        /*
        * 血清标本阳性类型 多个使用英文逗号隔开 HBsAg、抗-HCV、抗-HIV、抗-TP
        */
        serumType: string;
        /*
        * 献浆者性别
        */
        sex: string;
        /*
        * 采浆日期
        */
        slurryDate: string;
    }

    /*
    * /material/inventory/batch/edit response
    */
    interface InstanceUpdateDTO {

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
    * /material/inventory/batch/edit响应数据
    */
    interface Void {

    }

    /*
    * /inventory/materialwarehouse/statistics request
    */
    interface MaterialStatisticsUsingGET {

    }

    /*
    * /inventory/materialwarehouse/statistics响应数据
    */
    interface 物料库库存统计BO {

        /*
        * 物料名称
        */
        materialType: string;
        /*
        * 物料数量
        */
        num: number;
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
    interface InspectReportBodyResponse {

        /*
        * 
        */
        body: string;
        /*
        * 
        */
        docId: string;
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
    interface InspectReportResponse {

        /*
        * 
        */
        auditBy: string;
        /*
        * 
        */
        auditId: string;
        /*
        * 
        */
        auditRemark: string;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditStatus: string;
        /*
        * 
        */
        auditTime: string;
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
        docId: string;
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
    interface DocFileResponse {

        /*
        * 
        */
        body: string;
        /*
        * 
        */
        fileId: string;
        /*
        * 
        */
        type: string;
    }

    /*
    * /config/query/inspect request
    */
    interface QueryForInspectUsingGET {

    }

    /*
    * /laboratory/instrument/query/inspect request
    */
    interface QueryForInspectUsingGET_1 {

    }

    /*
    * /laboratory/instrument/query/inspect响应数据
    */
    interface EnumList {

        /*
        * 
        */
        label: string;
        /*
        * 
        */
        value: string;
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
    interface DocTemplateListResponse {

        /*
        * 
        */
        result: Array<DocTemplateResponse>;
        /*
        * 
        */
        total: number;
    }

    /*
    * DocTemplateResponse
    */
    interface DocTemplateResponse {

        /*
        * 
        */
        auditBy: string;
        /*
        * 
        */
        auditId: string;
        /*
        * 
        */
        auditRemark: string;
        /*
        * 
        */
        auditResult: string;
        /*
        * 
        */
        auditStatus: string;
        /*
        * 
        */
        auditTime: string;
        /*
        * 
        */
        buildNumber: string;
        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createRemark: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        effectiveDate: string;
        /*
        * 
        */
        expireDate: string;
        /*
        * 
        */
        fileId: string;
        /*
        * 
        */
        fileType: string;
        /*
        * 
        */
        standardNumber: string;
        /*
        * 
        */
        status: string;
        /*
        * 
        */
        templateName: string;
        /*
        * 
        */
        templateNo: string;
        /*
        * 
        */
        versionNumber: number;
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
    interface EnumList {

        /*
        * 
        */
        label: string;
        /*
        * 
        */
        value: string;
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
    interface StorageCardResponse% 3A货位卡Response{

    }

    type downLoadUsingPOST_1Res = object
    /*
    * /test/inspect/allinone response
    */
    interface TestInspectRequest {

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

    type undefinedRes = object
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
    interface 集中化检测标本送检交接记录 {

        /*
        * 
        */
        apparentOk: string;
        /*
        * 
        */
        auditSignature: string;
        /*
        * 
        */
        batchNo: string;
        /*
        * 
        */
        carrier: string;
        /*
        * 
        */
        clearLabel: string;
        /*
        * 
        */
        code: string;
        /*
        * 
        */
        flagFalse: string;
        /*
        * 
        */
        flagTrue: string;
        /*
        * 
        */
        hbsAgCount: string;
        /*
        * 
        */
        hcvCount: string;
        /*
        * 
        */
        hivCount: string;
        /*
        * 
        */
        maxTemperature: string;
        /*
        * 
        */
        minTemperature: string;
        /*
        * 
        */
        negative: string;
        /*
        * 
        */
        orgName: string;
        /*
        * 
        */
        pack: string;
        /*
        * 
        */
        plasmaSpecimen: string;
        /*
        * 
        */
        positive: string;
        /*
        * 
        */
        receiveCount: string;
        /*
        * 
        */
        receiveSignature: string;
        /*
        * 
        */
        rejectCount: string;
        /*
        * 
        */
        rejectMessage: string;
        /*
        * 
        */
        reviewerTime: string;
        /*
        * 
        */
        serumSpecimen: string;
        /*
        * 
        */
        tpCount: string;
        /*
        * 
        */
        transferCount: string;
        /*
        * 
        */
        transferDate: string;
    }

    /*
    * /test/inspect/audit response
    */
    interface TestInspectRequest {

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
    interface TestInspectRequest {

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
    interface TestInspectRequest {

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

    type undefinedRes = object
    /*
    * /log/operationLogSave response
    */
    interface 保存操作日志 {

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
    interface 标本数量详情 {

        /*
        * 批号
        */
        batchNo: string;
        /*
        * 接收数量
        */
        receiveCount: number;
        /*
        * 接收血浆标本
        */
        receivePlasmaSpecimen: number;
        /*
        * 接收血清标本
        */
        receiveSerumSpecimen: number;
        /*
        * 拒收数量
        */
        rejectCount: number;
        /*
        * 拒收血浆标本
        */
        rejectPlasmaSpecimen: number;
        /*
        * 拒收血清标本
        */
        rejectSerumSpecimen: number;
        /*
        * 请验数量
        */
        transferCount: number;
        /*
        * 请验血浆标本
        */
        transferPlasmaSpecimen: number;
        /*
        * 请验血清标本
        */
        transferSerumSpecimen: number;
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
    interface B3和B4分页查询模型 {

        /*
        * 
        */
        result: Array<Sample>;
        /*
        * 
        */
        total: number;
    }

    /*
    * Sample
    */
    interface Sample {

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
    * /test/inspect/createTask response
    */
    interface TestInspectRequest {

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
    interface 标本拒收详情-包括审核单信息{

        /*
        * 
        */
        sample: undefined;
        /*
        * 标本拒收信息集合
        */
        sampleList: Array<Sample>;
    }

    /*
    * Sample
    */
    interface Sample {

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
    interface QueryInspectionItemsResponse {

        /*
        * 固定检验项目
        */
        fixedInspectItems: Array<InspectItemInfo>;
        /*
        * 特殊检验项目
        */
        specialInspectItems: Array<InspectItemInfo>;
    }

    /*
    * InspectItemInfo
    */
    interface InspectItemInfo {

        /*
        * 检验项目编号
        */
        code: string;
        /*
        * 检验项目名称
        */
        name: string;
        /*
        * 是否选中
        */
        selected: boolean;
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
    interface 标本拒收详情-包括审核单信息{

        /*
        * 
        */
        sample: undefined;
        /*
        * 标本拒收信息集合
        */
        sampleList: Array<Sample>;
    }

    /*
    * Sample
    */
    interface Sample {

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
    * /sample/job/send响应数据
    */
    interface Void {

    }

    /*
    * /personalCenter/signaturePassword/verify response
    */
    interface SignaturePasswordRequest {

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
    interface ListAuditTaskResponse {

        /*
        * 待审核任务列表
        */
        result: Array<AuditFormCount>;
    }

    /*
    * AuditFormCount
    */
    interface AuditFormCount {

        /*
        * 页面链接
        */
        auditPagePath: string;
        /*
        * 审核类型
        */
        auditType: string;
        /*
        * 模块
        */
        module: string;
        /*
        * 待审核数量
        */
        num: number;
        /*
        * 页面
        */
        page: string;
    }

    /*
    * /inspect/singledata/unchecked response
    */
    interface ListUncheckedDataRequest {

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
    * /inspect/singledata/unchecked响应数据
    */
    interface ListUncheckedDataResponse {

        /*
        * 
        */
        result: Array<InspectDataCount>;
        /*
        * 
        */
        total: number;
    }

    /*
    * InspectDataCount
    */
    interface InspectDataCount {

        /*
        * 检验项目编号
        */
        inspectItemCode: string;
        /*
        * 检验项目名称
        */
        inspectItemName: string;
        /*
        * 待检验数量
        */
        num: number;
        /*
        * 来源单位
        */
        originOrgCode: string;
        /*
        * 页面链接
        */
        pagePath: string;
        /*
        * 接收人
        */
        receiveBy: string;
        /*
        * 接收日期
        */
        receiveTime: string;
        /*
        * 标本批号
        */
        sampleBatchNo: string;
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

}

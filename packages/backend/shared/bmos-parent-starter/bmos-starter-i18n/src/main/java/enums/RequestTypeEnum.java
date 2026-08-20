package enums;

public enum RequestTypeEnum {
    BACKEND("backend"),
    FRONTEND_WEB("frontend-web"),
    FRONTEND_APP("frontend-app"),
    ;
    private String code;

    RequestTypeEnum(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public static RequestTypeEnum getRequestTypeEnumByCode(String code){
        for(RequestTypeEnum requestTypeEnum : RequestTypeEnum.values()){
            if(requestTypeEnum.getCode().equals(code)){
                return requestTypeEnum;
            }
        }
        return null;
    }

}

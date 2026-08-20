declare namespace API {
    /*
    * /api/bmos-mes/dept/assign-person request
    */
    interface AssignPersonUsingGET {

        /*
        * 部门id
        */
        deptId?: string;
        /*
        * 查询字样
        */
        name?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        orderSort?: string;
        /*
        * 页码，从 1 开始
        */
        pageNum: number;
    }

    /*
    * /api/bmos-mes/dept/assign-person response
    */
    interface DeptAssignPersonRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: Array<dataVo>;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * 未分配用户VO
    */
    interface dataVo {

        /*
        * 
        */
        name: string;
        /*
        * 
        */
        userId: string;
    }

    /*
    * /api/bmos-mes/dept/relate-user-data request
    */
    interface RelateUserDataUsingGET {

        /*
        * 部门id
        */
        deptId?: string;
        /*
        * 部门名称
        */
        deptName?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        orderSort?: string;
        /*
        * 页码，从 1 开始
        */
        pageNum: number;
        /*
        * 每页条数，最大值为 100
        */
        pageSize: number;
        /*
        * 用户id
        */
        userId?: string;
    }

    /*
    * /api/bmos-mes/dept/relate-user-data response
    */
    interface DeptRelateUserDataRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: undefined;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/dept/relate-user-del response
    */
    interface DeptRelateUserDel {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 部门id
        */
        deptId: string;
        /*
        * 
        */
        id: number;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
        /*
        * 用户id
        */
        userId: string;
    }

    /*
    * /api/bmos-mes/dept/relate-user-del response
    */
    interface DeptRelateUserDelRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/dept/relate-user-delAll response
    */
    interface DeptRelateUserDelAll {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 部门id
        */
        deptId: string;
        /*
        * 
        */
        id: number;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
        /*
        * 用户id
        */
        userId: string;
    }

    /*
    * /api/bmos-mes/dept/relate-user-delAll response
    */
    interface DeptRelateUserDelAllRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/dept/relate-user-save response
    */
    interface DeptRelateUserSave {

    }

    /*
    * /api/bmos-mes/dept/relate-user-save response
    */
    interface DeptRelateUserSaveRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/dept/save response
    */
    interface DeptSave {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 部门代码
        */
        deptCode: string;
        /*
        * 部门名称
        */
        deptName: string;
        /*
        * 
        */
        id: number;
        /*
        * 上级部门id
        */
        parentId: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
    }

    /*
    * /api/bmos-mes/dept/save response
    */
    interface DeptSaveRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/dept/tree request
    */
    interface GetTreeUsingGET {

        /*
        * 部门id
        */
        deptId?: string;
        /*
        * 部门名称
        */
        deptName?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        orderSort?: string;
        /*
        * 页码，从 1 开始
        */
        pageNum: number;
        /*
        * 每页条数，最大值为 100
        */
        pageSize: number;
        /*
        * 用户id
        */
        userId?: string;
    }

    /*
    * /api/bmos-mes/dept/tree response
    */
    interface DeptTreeRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: Array<dataVo>;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/dept/update response
    */
    interface DeptUpdate {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 部门代码
        */
        deptCode: string;
        /*
        * 部门名称
        */
        deptName: string;
        /*
        * 
        */
        id: number;
        /*
        * 上级部门id
        */
        parentId: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
    }

    /*
    * /api/bmos-mes/dept/update response
    */
    interface DeptUpdateRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/menu/tree request
    */
    interface GetDataUsingGET {

    }

    /*
    * /api/bmos-mes/menu/tree response
    */
    interface MenuTreeRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: Array<dataVo>;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/role/get-role request
    */
    interface GetRoleUsingGET {

        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        orderSort?: string;
        /*
        * 页码，从 1 开始
        */
        pageNum: number;
        /*
        * 每页条数，最大值为 100
        */
        pageSize: number;
        /*
        * 角色类型id
        */
        roleTypeId?: string;
    }

    /*
    * /api/bmos-mes/role/get-role response
    */
    interface RoleGetRoleRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: undefined;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/role/relate-menu-save response
    */
    interface RoleRelateMenuSave {

    }

    /*
    * /api/bmos-mes/role/relate-menu-save response
    */
    interface RoleRelateMenuSaveRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/role/save-role response
    */
    interface RoleSaveRole {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 描述
        */
        description: string;
        /*
        * 
        */
        id: number;
        /*
        * 角色名称
        */
        roleName: string;
        /*
        * 角色分类id
        */
        roleTypeId: number;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
    }

    /*
    * /api/bmos-mes/role/save-role response
    */
    interface RoleSaveRoleRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/role/save-type response
    */
    interface RoleSaveType {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 
        */
        id: number;
        /*
        * 父级id
        */
        parentId: number;
        /*
        * 角色分类名称
        */
        roleTypeName: string;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
    }

    /*
    * /api/bmos-mes/role/save-type response
    */
    interface RoleSaveTypeRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/role/tree request
    */
    interface GetTreeUsingGET_1 {

        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        orderSort?: string;
        /*
        * 页码，从 1 开始
        */
        pageNum: number;
        /*
        * 每页条数，最大值为 100
        */
        pageSize: number;
        /*
        * 角色类型id
        */
        roleTypeId?: string;
    }

    /*
    * /api/bmos-mes/role/tree response
    */
    interface RoleTreeRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: Array<dataVo>;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/role/update-role response
    */
    interface RoleUpdateRole {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 描述
        */
        description: string;
        /*
        * 
        */
        id: number;
        /*
        * 角色名称
        */
        roleName: string;
        /*
        * 角色分类id
        */
        roleTypeId: number;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
    }

    /*
    * /api/bmos-mes/role/update-role response
    */
    interface RoleUpdateRoleRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/role/update-type response
    */
    interface RoleUpdateType {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 
        */
        id: number;
        /*
        * 父级id
        */
        parentId: number;
        /*
        * 角色分类名称
        */
        roleTypeName: string;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
    }

    /*
    * /api/bmos-mes/role/update-type response
    */
    interface RoleUpdateTypeRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/user/export request
    */
    interface ExportDataUsingGET {

    }

    /*
    * /api/bmos-mes/user/export response
    */
    interface UserExportRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/user/import request
    */
    interface ImportDataUsingGET {

    }

    /*
    * /api/bmos-mes/user/import response
    */
    interface UserImportRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/user/page request
    */
    interface GetPageUsingGET {

        /*
        * 性别
        */
        gender?: number;
        /*
        * id
        */
        id?: string;
        /*
        * 用户账号（模糊查询）
        */
        loginName?: string;
        /*
        * 
        */
        orderBy?: string;
        /*
        * 
        */
        orderSort?: string;
        /*
        * 页码，从 1 开始
        */
        pageNum: number;
        /*
        * 每页条数，最大值为 100
        */
        pageSize: number;
        /*
        * 手机号（模糊查询）
        */
        phone?: string;
        /*
        * 启停
        */
        state?: number;
        /*
        * 状态
        */
        status?: number;
    }

    /*
    * /api/bmos-mes/user/page response
    */
    interface UserPageRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: undefined;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/user/relate-role-data request
    */
    interface RelateRoleDataUsingGET {

    }

    /*
    * /api/bmos-mes/user/relate-role-data response
    */
    interface UserRelateRoleDataRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: Array<dataVo>;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/user/relate-role-save response
    */
    interface UserRelateRoleSave {

    }

    /*
    * /api/bmos-mes/user/relate-role-save response
    */
    interface UserRelateRoleSaveRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/user/save response
    */
    interface UserSave {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 邮箱
        */
        email: string;
        /*
        * 性别
        */
        gender: number;
        /*
        * 
        */
        id: number;
        /*
        * 用户账号
        */
        loginName: string;
        /*
        * 旧密码
        */
        oldPassword: string;
        /*
        * 密码
        */
        password: string;
        /*
        * 手机号
        */
        phone: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 启停
        */
        state: number;
        /*
        * 状态
        */
        status: number;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
        /*
        * 用户名称
        */
        userName: string;
    }

    /*
    * /api/bmos-mes/user/save response
    */
    interface UserSaveRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/user/unassigned request
    */
    interface GetUnassignedUsingGET {

    }

    /*
    * /api/bmos-mes/user/unassigned response
    */
    interface UserUnassignedRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 数据内容
        */
        data: Array<dataVo>;
        /*
        * 返回信息
        */
        message: string;
    }

    /*
    * /api/bmos-mes/user/update response
    */
    interface UserUpdate {

        /*
        * 
        */
        createBy: string;
        /*
        * 
        */
        createTime: string;
        /*
        * 
        */
        deleted: boolean;
        /*
        * 邮箱
        */
        email: string;
        /*
        * 性别
        */
        gender: number;
        /*
        * 
        */
        id: number;
        /*
        * 用户账号
        */
        loginName: string;
        /*
        * 旧密码
        */
        oldPassword: string;
        /*
        * 密码
        */
        password: string;
        /*
        * 手机号
        */
        phone: string;
        /*
        * 备注
        */
        remark: string;
        /*
        * 启停
        */
        state: number;
        /*
        * 状态
        */
        status: number;
        /*
        * 
        */
        updateBy: string;
        /*
        * 
        */
        updateTime: string;
        /*
        * 用户名称
        */
        userName: string;
    }

    /*
    * /api/bmos-mes/user/update response
    */
    interface UserUpdateRes {

        /*
        * 编码
        */
        code: number;
        /*
        * 返回信息
        */
        message: string;
    }

}

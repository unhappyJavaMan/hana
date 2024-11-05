package com.hana.service.Common;

public class FunctionPath {
    public static class AUTH {
        public static final String login = "/api/auth/login";
        public static final String logout = "/api/auth/logout";
    }
    public static class user {
        public static final String create = "/api/users/create";
        public static final String delete = "/api/users/delete";
        public static final String update = "/api/users/update";
        public static final String getAll = "/api/users/getAll";
        public static final String getUserById = "/api/users/getUserById";
        public static final String changePassword = "/api/users/changePassword";
        public static final String getUserInfo = "/api/users/getUserInfo";
    }
    public static class systemRole {
        public static final String create = "/api/systemRole/create";
        public static final String getAll  = "/api/systemRole/getAll";
        public static final String getById  = "/api/systemRole/getById";
        public static final String update  = "/api/systemRole/update";
        public static final String delete  = "/api/systemRole/delete";
    }
    public static class customer {
        public static final String create = "/api/customer/create";
        public static final String getAll  = "/api/customer/getAll";
        public static final String getById  = "/api/customer/getById";
        public static final String getCustomerByUserId  = "/api/customer/getCustomerByUserId";
        public static final String createCustomerByUserId = "/api/customer/createCustomerByUserId";
        public static final String updateCustomerStatusByUser = "/api/customer/updateCustomerStatusByUser";
    }
    public static class appointment {
        public static final String create = "/api/appointment/create";
        public static final String getByCustomerId  = "/api/appointment/getByCustomerId";
    }
}

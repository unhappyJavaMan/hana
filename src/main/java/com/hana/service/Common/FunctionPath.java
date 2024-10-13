package com.hana.service.Common;

public class FunctionPath {
    public static class AUTH {
        public static final String login = "/api/auth/login";
        public static final String logout = "/api/auth/logout";
    }
    public static class user {
        public static final String create = "/api/members/create";
        public static final String delete = "/api/members/delete";
        public static final String update = "/api/members/update";
        public static final String getAll = "/api/members/getAll";
        public static final String getUserById = "/api/members/getUserById";
        public static final String changePassword = "/api/members/changePassword";
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
    }
    public static class appointment {
        public static final String create = "/api/appointment/create";
        public static final String getByCustomerId  = "/api/appointment/getByCustomerId";
    }
}

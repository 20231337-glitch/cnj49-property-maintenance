package com.cnj49.propertymaintenance.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Phan quyen theo vai tro: STAFF tao/theo doi yeu cau, MANAGER duyet va quan ly, chi ADMIN duoc xoa.
 * Chi kiem tra tang bao mat: bi chan -> 403; duoc phep -> moi ma khac 403 (ke ca loi nghiep vu/khong tim thay).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RoleAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    private ResultActions call(String method, String url) throws Exception {
        return "POST".equals(method)
                ? mockMvc.perform(post(url).with(csrf()))
                : mockMvc.perform(get(url));
    }

    private void assertAllowed(String method, String url) throws Exception {
        int code = call(method, url).andReturn().getResponse().getStatus();
        assertThat(code).as(method + " " + url).isNotEqualTo(403);
    }

    @ParameterizedTest
    @WithMockUser(username = "staff", roles = "STAFF")
    @CsvSource({
            "POST, /maintenance/1/quotations/1/approve",
            "POST, /maintenance/1/quotations/1/reject",
            "GET,  /workorders/create",
            "POST, /workorders",
            "POST, /workorders/1/cancel",
            "GET,  /inspections/create",
            "POST, /inspections",
            "POST, /maintenance/1/close",
            "POST, /maintenance/1/cancel",
            "POST, /maintenance/1/status",
            "GET,  /expenses/create",
            "POST, /expenses",
            "GET,  /expenses/1/edit",
            "GET,  /properties/create",
            "POST, /properties",
            "GET,  /properties/1/edit",
            "POST, /properties/1",
            "GET,  /units/create",
            "POST, /units",
            "GET,  /contractors/create",
            "POST, /contractors",
            "GET,  /categories/create",
            "POST, /categories",
            "POST, /maintenance/1/delete",
            "POST, /maintenance/1/quotations/1/delete",
    })
    void staff_isDeniedManagementActions(String method, String url) throws Exception {
        call(method, url).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @WithMockUser(username = "staff", roles = "STAFF")
    @CsvSource({
            "GET,  /dashboard",
            "GET,  /reports",
            "GET,  /properties",
            "GET,  /maintenance",
            "GET,  /maintenance/create",
            "POST, /maintenance",
            "POST, /maintenance/1",
            "GET,  /maintenance/1/quotations/create",
            "POST, /maintenance/1/quotations",
            "POST, /maintenance/1/quotations/1",
            "POST, /workorders/1/start",
            "POST, /workorders/1/pause",
            "POST, /workorders/1/resume",
            "POST, /workorders/1/complete",
    })
    void staff_canCreateAndFollowRequests(String method, String url) throws Exception {
        assertAllowed(method, url);
    }

    @ParameterizedTest
    @WithMockUser(username = "manager", roles = "MANAGER")
    @CsvSource({
            "POST, /maintenance/1/quotations/1/approve",
            "POST, /maintenance/1/quotations/1/reject",
            "GET,  /workorders/create",
            "POST, /workorders/1/cancel",
            "GET,  /inspections/create",
            "POST, /maintenance/1/close",
            "GET,  /expenses/create",
            "GET,  /properties/create",
            "GET,  /properties/1/edit",
            "GET,  /contractors/create",
            "GET,  /categories/create",
    })
    void manager_canApproveInspectAndManageMasterData(String method, String url) throws Exception {
        assertAllowed(method, url);
    }

    @ParameterizedTest
    @WithMockUser(username = "manager", roles = "MANAGER")
    @CsvSource({
            "/properties/1/delete",
            "/units/1/delete",
            "/contractors/1/delete",
            "/categories/1/delete",
            "/expenses/1/delete",
            "/maintenance/1/delete",
            "/maintenance/1/quotations/1/delete",
    })
    void manager_cannotDelete(String url) throws Exception {
        call("POST", url).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @WithMockUser(username = "admin", roles = "ADMIN")
    @CsvSource({
            "/properties/1/delete",
            "/contractors/1/delete",
            "/maintenance/1/delete",
            "/maintenance/1/quotations/1/delete",
    })
    void admin_canDelete(String url) throws Exception {
        assertAllowed("POST", url);
    }

    @Test
    @WithMockUser(username = "staff", roles = "STAFF")
    void accessDeniedPage_rendersForbiddenMessage() throws Exception {
        mockMvc.perform(get("/errors/403"))
                .andExpect(content().string(containsString("Không có quyền truy cập")));
    }
}

package com.example.bs.security;

import com.example.bs.tools.Jwt;
import com.example.bs.tools.interceptor.Login;
import com.example.bs.tools.interceptor.Per;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginInterceptorSecurityTest {

    private Login loginInterceptor;

    @BeforeEach
    void setUp() {
        loginInterceptor = new Login();
    }

    @Test
    void shouldRejectWhenTokenMissing() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/bs/selkucun");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        HandlerMethod handler = new HandlerMethod(new DummyController(), "normalApi");

        boolean pass = loginInterceptor.preHandle(req, resp, handler);

        assertFalse(pass);
        assertTrue(resp.getStatus() == 401);
        assertTrue(resp.getContentAsString().contains("Not_Login"));
    }

    @Test
    void shouldRejectWhenTokenInvalid() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/bs/selkucun");
        req.addHeader("token", "bad-token");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        HandlerMethod handler = new HandlerMethod(new DummyController(), "normalApi");

        boolean pass = loginInterceptor.preHandle(req, resp, handler);

        assertFalse(pass);
        assertTrue(resp.getStatus() == 401);
        assertTrue(resp.getContentAsString().contains("Not_Login"));
    }

    @Test
    void shouldRejectWhenPermissionInsufficient() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/bs/seluser");
        req.addHeader("token", makeToken(0));
        MockHttpServletResponse resp = new MockHttpServletResponse();
        HandlerMethod handler = new HandlerMethod(new DummyController(), "managerApi");

        boolean pass = loginInterceptor.preHandle(req, resp, handler);

        assertFalse(pass);
        assertTrue(resp.getStatus() == 403);
        assertTrue(resp.getContentAsString().contains("No_Permission"));
    }

    @Test
    void shouldPassWhenPermissionEnough() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/bs/seluser");
        req.addHeader("token", makeToken(2));
        MockHttpServletResponse resp = new MockHttpServletResponse();
        HandlerMethod handler = new HandlerMethod(new DummyController(), "managerApi");

        boolean pass = loginInterceptor.preHandle(req, resp, handler);

        assertTrue(pass);
    }

    @Test
    void shouldPassWhenStaticResourceRequest() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/uploads/avatar.jpg");
        MockHttpServletResponse resp = new MockHttpServletResponse();
        Object nonHandlerMethod = new Object();

        boolean pass = loginInterceptor.preHandle(req, resp, nonHandlerMethod);

        assertTrue(pass);
    }

    private String makeToken(int per) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "tester");
        claims.put("per", per);
        return Jwt.generateJwt(claims);
    }

    static class DummyController {
        public void normalApi() {
        }

        @Per(1)
        public void managerApi() {
        }
    }
}

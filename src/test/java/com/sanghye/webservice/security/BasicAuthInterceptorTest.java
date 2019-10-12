package com.sanghye.webservice.security;

import com.sanghye.webservice.domain.User;
import com.sanghye.webservice.service.UserService;
import com.sanghye.webservice.support.test.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;


import java.util.Base64;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BasicAuthInterceptorTest extends BaseTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private JwtAuthInterceptor basicAuthInterceptor;

    @Test
    public void preHandle_로그인_성공() throws Exception {
        String userId = "userId";
        String password = "password";
        MockHttpServletRequest request = basicAuthHttpRequest(userId, password);
        User loginUser = new User(userId, "password", "name", "javajigi@slipp.net");
        when(userService.checkLoginUser(userId, password)).thenReturn(loginUser);

        basicAuthInterceptor.preHandle(request, null, null);
        softly.assertThat(request.getSession().getAttribute(HttpSessionUtils.USER_SESSION_KEY)).isEqualTo(loginUser);
    }

    private MockHttpServletRequest basicAuthHttpRequest(String userId, String password) {
        String encodedBasicAuth = Base64.getEncoder()
                .encodeToString(String.format("%s:%s", userId, password).getBytes());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic " + encodedBasicAuth);
        return request;
    }
}

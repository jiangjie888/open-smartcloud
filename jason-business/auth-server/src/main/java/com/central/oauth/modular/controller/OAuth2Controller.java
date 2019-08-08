package com.central.oauth.modular.controller;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import com.central.core.model.constants.SecurityConstants;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.utils.ResponseUtil;
import com.central.core.utils.SpringUtil;
import com.central.oauth.core.mobile.MobileAuthenticationToken;
import com.central.oauth.core.openid.OpenIdAuthenticationToken;
import com.central.oauth.modular.service.impl.RedisClientDetailsService;
import com.central.oauth2.common.util.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
///import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter
/**
 * OAuth2相关操作
 */
@Api(tags = "OAuth2相关操作")
@Slf4j
@RestController
public class OAuth2Controller {
    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private AuthenticationManager authenticationManager;


    @ResponseBody
    @ApiOperation(value = "用户名密码获取token")
    @RequestMapping(value = { SecurityConstants.PASSWORD_LOGIN_PRO_URL }, method = { RequestMethod.POST }, produces="application/json;charset=UTF-8")
    public void getUserTokenInfo(@RequestParam String username,@RequestParam String password,HttpServletRequest request, HttpServletResponse response) throws IOException {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        writerToken(request, response, token, "用户名或密码错误");
    }

    @ApiOperation(value = "openId获取token")
    @PostMapping(SecurityConstants.OPENID_TOKEN_URL)
    public void getTokenByOpenId(String openId,HttpServletRequest request, HttpServletResponse response) throws IOException {
        OpenIdAuthenticationToken token = new OpenIdAuthenticationToken(openId);
        writerToken(request, response, token, "openId错误");
    }

    @ApiOperation(value = "mobile获取token")
    @PostMapping(SecurityConstants.MOBILE_TOKEN_URL)
    public void getTokenByMobile(String mobile,String password,HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobileAuthenticationToken token = new MobileAuthenticationToken(mobile, password);
        writerToken(request, response, token, "手机号或密码错误");
    }

/*
    @ApiOperation(value = "access_token刷新token")
    @PostMapping(value = "/oauth/refresh/token", params = "access_token")
    public void refreshTokenInfo(String access_token ,HttpServletRequest request, HttpServletResponse response) {

        //拿到当前用户信息
        try {
            Authentication user = SecurityContextHolder.getContext()
                    .getAuthentication();

            if(user!=null){
                if(user instanceof OAuth2Authentication){
                    Authentication athentication = (Authentication)user;
                    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails() ;
                }

            }
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
            OAuth2Authentication  auth =(OAuth2Authentication ) user ;
            RedisClientDetailsService clientDetailsService = SpringUtil.getBean(RedisClientDetailsService.class);


            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(auth.getOAuth2Request().getClientId());


            AuthorizationServerTokenServices authorizationServerTokenServices = SpringUtil
                    .getBean("defaultAuthorizationServerTokenServices", AuthorizationServerTokenServices.class);
            OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);


            RefreshTokenGranter refreshTokenGranter = new RefreshTokenGranter( authorizationServerTokenServices, clientDetailsService, requestFactory );

            Map<String, String> map = new HashMap<>();
            map.put("grant_type", "refresh_token");
            map.put("refresh_token", accessToken.getRefreshToken().getValue());
            TokenRequest tokenRequest = new TokenRequest(map, auth.getOAuth2Request().getClientId(), auth.getOAuth2Request().getScope()  , "refresh_token");


            OAuth2AccessToken oAuth2AccessToken = refreshTokenGranter.grant("refresh_token",
                    tokenRequest);

            tokenStore.removeAccessToken(accessToken);


            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(oAuth2AccessToken));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            Map<String, String> rsp = new HashMap<>();
            rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
            rsp.put("rsp_msg", e.getMessage());

            try {
                response.getWriter().write(objectMapper.writeValueAsString(rsp));
                response.getWriter().flush();
                response.getWriter().close();
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }



    }
    */
/**
     * 移除access_token和refresh_token
     * @param access_token
     *//*

    @ApiOperation(value = "移除token")
    @PostMapping(value = SecurityConstants.LOGOUT_URL, params = "access_token")
    public void removeToken(String access_token) {

        //拿到当前用户信息
        Authentication user = SecurityContextHolder.getContext()
                .getAuthentication();

        if(user!=null){
            if(user instanceof OAuth2Authentication){
                Authentication athentication = (Authentication)user;
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails() ;
            }

        }
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
        if (accessToken != null) {
            // 移除access_token
            tokenStore.removeAccessToken(accessToken);

            // 移除refresh_token
            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }

        }
    }
*/



    private void writerToken(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token
            , String badCredenbtialsMsg) throws IOException {
        try {
            final String[] clientInfos = AuthUtils.extractClient(request);
            String clientId = clientInfos[0];
            String clientSecret = clientInfos[1];

            ClientDetails clientDetails = getClient(clientId, clientSecret, null);
            TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "customer");
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            oAuth2Authentication.setAuthenticated(true);

            ResponseUtil.responseWriter(null,response,objectMapper.writeValueAsString(oAuth2AccessToken),HttpStatus.OK.value());
            //writerObj(response, oAuth2AccessToken);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            exceptionHandler(response, badCredenbtialsMsg);
        } catch (Exception e) {
            exceptionHandler(response, e);
        }
    }

    private void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        log.error("exceptionHandler-error:", e);
        exceptionHandler(response, e.getMessage());
    }

    private void exceptionHandler(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseUtil.responseWriter(null,response,msg,HttpStatus.UNAUTHORIZED.value());
        //writerObj(response, ResponseData.error(msg));
    }

   /* private void writerObj(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (
                Writer writer = response.getWriter()
        ) {
            writer.write(objectMapper.writeValueAsString(obj));
            writer.flush();
        }
    }*/

    private ClientDetails getClient(String clientId, String clientSecret, RedisClientDetailsService clientDetailsService) {
        if (clientDetailsService == null) {
            clientDetailsService = SpringUtil.getBean(RedisClientDetailsService.class);
        }
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }
        return clientDetails;
    }
}

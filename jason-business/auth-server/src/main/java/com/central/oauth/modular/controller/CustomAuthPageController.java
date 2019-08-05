package com.central.oauth.modular.controller;


import com.central.core.model.constants.SecurityConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  处理登录和授权的控制器
 */
@Api(tags = "自定义授权模式API")
@Controller
@SessionAttributes({"authorizationRequest"})
public class CustomAuthPageController {



    @Autowired
    private SecurityProperties securityProperties;


    /*@GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("OAUTH_LOGIN_PRO_URL",SecurityConstants.OAUTH_LOGIN_PRO_URL);
        return "login";
    }*/

    /**
     * 自定义授权页面，注意：一定要在类上加@SessionAttributes({"authorizationRequest"})
     */
    /*@RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request.getAttribute("scopes"));
        List<String> scopeList = new ArrayList<>();
        if (scopes != null) {
            scopeList.addAll(scopes.keySet());
        }
        model.put("scopeList", scopeList);
        return "grant";
    }*/
    @ApiOperation(value = "返回确认授权页面")
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("approve");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scopes",authorizationRequest.getScope());
        return view;
    }



/*    @ApiOperation(value = "确认授权或是拒绝")
    @RequestMapping(value = {"/oauth/authorize"},method = {RequestMethod.POST},params = {"user_oauth_approval"})
    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal) {
        if (!(principal instanceof Authentication)) {
            sessionStatus.setComplete();
            throw new InsufficientAuthenticationException("User must be authenticated with Spring Security before authorizing an access token.");
        } else {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest)model.get("authorizationRequest");
            if (authorizationRequest == null) {
                sessionStatus.setComplete();
                throw new InvalidRequestException("Cannot approve uninitialized authorization request.");
            } else {
                View var8;
                try {
                    //AuthorizationEndpoint authorizationEndpoint = new AuthorizationEndpoint();
                    Set<String> responseTypes = authorizationRequest.getResponseTypes();
                    authorizationRequest.setApprovalParameters(approvalParameters);
                    authorizationRequest = this.userApprovalHandler.updateAfterApproval(authorizationRequest, (Authentication)principal);
                    boolean approved = this.userApprovalHandler.isApproved(authorizationRequest, (Authentication)principal);
                    authorizationRequest.setApproved(approved);
                    if (authorizationRequest.getRedirectUri() == null) {
                        sessionStatus.setComplete();
                        throw new InvalidRequestException("Cannot approve request when no redirect URI is provided.");
                    }

                    if (!authorizationRequest.isApproved()) {
                        RedirectView var12 = new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, new UserDeniedAuthorizationException("User denied access"), responseTypes.contains("token")), false, true, false);
                        return var12;
                    }

                    if (!responseTypes.contains("token")) {
                        var8 = this.getAuthorizationCodeResponse(authorizationRequest, (Authentication)principal);
                        return var8;
                    }

                    var8 = this.getImplicitGrantResponse(authorizationRequest).getView();
                } finally {
                    sessionStatus.setComplete();
                }

                return var8;
            }
        }
    }*/
}
package com.central.oauth.modular.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.central.core.model.common.AuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oauth_client_details")
public class Client extends Model<Client> {
   private static final long serialVersionUID = -8185413579135897885L;

   @TableId(value = "id", type = IdType.AUTO)
   private Long id;
   @TableField(value = "client_id")
   private String clientId;

   @TableField(value = "resource_ids")
   private String resourceIds = "";

   @TableField(value = "client_secret")
   private String clientSecret;

   @TableField(value = "client_secret_str")
   private String clientSecretStr;

   @TableField(value = "scope")
   private String scope = "all";

   @TableField(value = "authorized_grant_types")
   private String authorizedGrantTypes = "authorization_code,password,refresh_token,client_credentials";

   @TableField(value = "web_server_redirect_uri")
   private String webServerRedirectUri;

   @TableField(value = "authorities")
   private String authorities = "";

   @TableField(value = "access_token_validity")
   private Integer accessTokenValiditySeconds = 18000;

   @TableField(value = "refresh_token_validity")
   private Integer refreshTokenValiditySeconds = 28800;

   @TableField(value = "additional_information")
   private String additionalInformation = "{}";

   @TableField(value = "autoapprove")
   private String autoapprove = "true";
}
-- ----------------------------
-- Table structure for log_common_log
-- ----------------------------
DROP TABLE IF EXISTS `log_common_log`;
CREATE TABLE `log_common_log`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `APP_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用编码',
  `LEVEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志级别 info，error，warn，debug',
  `CLASS_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类名',
  `METHOD_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打日志的方法的名称',
  `IP` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '远程访问IP地址',
  `ACCOUNT_ID` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户账号id',
  `REQUEST_NO` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志号',
  `URL` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求地址 ',
  `REQUEST_DATA` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求的数据内容',
  `LOG_CONTENT` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志详情',
  `CREATE_TIMESTAMP` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `_APP_CODE`(`APP_CODE`) USING BTREE,
  INDEX `_LEVEL`(`LEVEL`) USING BTREE,
  INDEX `_REQUEST_NO`(`REQUEST_NO`) USING BTREE,
  INDEX `_CREATE_TIME`(`CREATE_TIMESTAMP`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log_trace_log
-- ----------------------------
DROP TABLE IF EXISTS `log_trace_log`;
CREATE TABLE `log_trace_log`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `APP_CODE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用编码',
  `IP` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `SERVLET_PATH` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求路径',
  `RPC_PHASE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'rpc调用类型，\n    G1,     //网关发送请求\n\n    G2,     //接收网关请求（切controller）\n\n    P1,     //调用端发送请求（切consumer）\n\n    P2,     //被调用端接收到请求（切provider）\n\n    P3,     //被调用端发送响应成功\n\n    P4,     //调用端接收到响应成功\n\n    EP3,    //被调用端发送响应失败\n\n    EP4,    //调用端接收到响应失败\n\n    G3,     //控制器响应网关成功\n\n    G4,     //网关接收到成功请求\n\n    EG3,    //控制器接收到错误响应\n\n    EG4,    //网关接收到错误响应',
  `TRACE_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一请求号',
  `SPAN_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点id',
  `PARENT_SPAN_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点父id',
  `CONTENT` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志内容',
  `CREATE_TIMESTAMP` bigint(20) NULL DEFAULT NULL COMMENT '生成时间戳',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `_APP_CODE`(`APP_CODE`) USING BTREE,
  INDEX `_RPC_PHASE`(`RPC_PHASE`) USING BTREE,
  INDEX `_CREATE_TIME`(`CREATE_TIMESTAMP`) USING BTREE,
  INDEX `_TRACE_ID`(`TRACE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

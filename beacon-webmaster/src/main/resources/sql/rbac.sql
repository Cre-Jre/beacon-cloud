-- ----------------------------
-- Table structure for `sms_user`
-- ----------------------------
DROP TABLE IF EXISTS `sms_user`;
CREATE TABLE `sms_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '用户登录密码',
  `salt` varchar(32) NOT NULL COMMENT '认证的盐',
  `nickname` varchar(32) NOT NULL COMMENT '昵称',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认系统时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认系统时间',
  `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-未删除 ， 1-已删除',
  `extend1` varchar(32) DEFAULT NULL COMMENT '备用字段1',
  `extend2` varchar(32) DEFAULT NULL COMMENT '备用字段2',
  `extend3` varchar(32) DEFAULT NULL COMMENT '备用字段3',
  `extend4` varchar(32) DEFAULT NULL COMMENT '备用字段4',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of sms_user
-- ----------------------------
INSERT INTO `sms_user` VALUES ('1', 'admin', 'b39dc5da02d002e6ac581e5bb929d2e5', '09a8424ed5bf4373af6530fec2b29c0f', '老郑', '2023-07-28 08:25:16', '0', '2023-07-28 08:25:16', '0', '0', null, null, null, null);
-- ----------------------------
-- Table structure for `sms_role`
-- ----------------------------
DROP TABLE IF EXISTS `sms_role`;
CREATE TABLE `sms_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '角色名',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认系统时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认系统时间',
  `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-未删除 ， 1-已删除',
  `extend1` varchar(32) DEFAULT NULL COMMENT '备用字段1',
  `extend2` varchar(32) DEFAULT NULL COMMENT '备用字段2',
  `extend3` varchar(32) DEFAULT NULL COMMENT '备用字段3',
  `extend4` varchar(32) DEFAULT NULL COMMENT '备用字段4',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of sms_role
-- ----------------------------
INSERT INTO `sms_role` VALUES ('1', '管理员', '2023-07-28 08:25:33', '1', '2023-07-28 08:25:33', '1', '0', null, null, null, null);
-- ----------------------------
-- Table structure for `sms_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sms_menu`;
CREATE TABLE `sms_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '菜单名',
  `parent_id` bigint(20) NOT NULL COMMENT '父菜单id',
  `url` varchar(256) DEFAULT NULL COMMENT '跳转的连接地址',
  `icon` varchar(64) DEFAULT NULL COMMENT '按钮的小图标',
  `type` int(11) NOT NULL COMMENT '菜单的类型',
  `sort` int(11) DEFAULT NULL COMMENT '菜单排序规则',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认系统时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认系统时间',
  `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-未删除 ， 1-已删除',
  `extend1` varchar(32) DEFAULT NULL COMMENT '备用字段1',
  `extend2` varchar(32) DEFAULT NULL COMMENT '备用字段2',
  `extend3` varchar(32) DEFAULT NULL COMMENT '备用字段3',
  `extend4` varchar(32) DEFAULT NULL COMMENT '备用字段4',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of sms_menu
-- ----------------------------
INSERT INTO `sms_menu` VALUES ('1', '权限管理', '0', null, 'fa fa-cog', '0', '1000', '2023-07-28 08:26:44', '1', '2023-07-28 08:26:44', '1', '0', null, null, null, null);
INSERT INTO `sms_menu` VALUES ('2', '用户管理', '1', 'sys/user.html', 'fa fa-user', '1', '1000', '2023-07-28 08:27:08', '1', '2023-07-28 08:27:08', '1', '0', null, null, null, null);
INSERT INTO `sms_menu` VALUES ('3', '角色管理', '1', 'sys/role.html', 'fa fa-user-secret', '1', '1000', '2023-07-28 08:27:41', '1', '2023-07-28 08:27:41', '1', '0', null, null, null, null);
INSERT INTO `sms_menu` VALUES ('4', '菜单管理', '1', 'sys/menu.html', 'fa fa-th-list', '1', '1000', '2023-07-28 08:27:54', '1', '2023-07-28 08:27:54', '1', '0', null, null, null, null);
INSERT INTO `sms_menu` VALUES ('5', '查看', '2', null, null, '2', '1000', '2023-07-28 08:28:37', '1', '2023-07-28 08:28:37', '1', '0', null, null, null, null);
INSERT INTO `sms_menu` VALUES ('6', '新增', '2', null, null, '2', '1000', '2023-07-28 08:28:54', '1', '2023-07-28 08:29:36', '1', '0', null, null, null, null);
INSERT INTO `sms_menu` VALUES ('7', '修改', '2', null, null, '2', '1000', '2023-07-28 08:29:20', '1', '2023-07-28 08:29:36', '1', '0', null, null, null, null);
INSERT INTO `sms_menu` VALUES ('8', '删除', '2', null, null, '2', '1000', '2023-07-28 08:29:34', '1', '2023-07-28 08:29:37', '1', '0', null, null, null, null);
-- ----------------------------
-- Table structure for `sms_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sms_user_role`;
CREATE TABLE `sms_user_role` (
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `role_id` bigint(11) NOT NULL COMMENT '角色id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认系统时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认系统时间',
  `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-未删除 ， 1-已删除',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sms_user_role
-- ----------------------------
INSERT INTO `sms_user_role` VALUES ('1', '1', '2023-07-28 08:25:41', '1', '2023-07-28 08:25:41', '1', '0');
-- ----------------------------
-- Table structure for `sms_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sms_role_menu`;
CREATE TABLE `sms_role_menu` (
  `role_id` bigint(11) NOT NULL COMMENT '角色id',
  `menu_id` bigint(11) NOT NULL COMMENT '菜单id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认系统时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认系统时间',
  `update_id` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-未删除 ， 1-已删除',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sms_role_menu
-- ----------------------------
INSERT INTO `sms_role_menu` VALUES ('1', '1', '2023-07-28 08:30:34', null, '2023-07-28 08:30:34', null, '0');
INSERT INTO `sms_role_menu` VALUES ('1', '2', '2023-07-28 08:30:38', null, '2023-07-28 08:30:38', null, '0');
INSERT INTO `sms_role_menu` VALUES ('1', '3', '2023-07-28 08:30:40', null, '2023-07-28 08:30:40', null, '0');
INSERT INTO `sms_role_menu` VALUES ('1', '4', '2023-07-28 08:30:44', null, '2023-07-28 08:30:44', null, '0');
INSERT INTO `sms_role_menu` VALUES ('1', '5', '2023-07-28 08:30:46', null, '2023-07-28 08:30:46', null, '0');
INSERT INTO `sms_role_menu` VALUES ('1', '6', '2023-07-28 08:30:48', null, '2023-07-28 08:30:48', null, '0');
INSERT INTO `sms_role_menu` VALUES ('1', '7', '2023-07-28 08:30:50', null, '2023-07-28 08:30:50', null, '0');
INSERT INTO `sms_role_menu` VALUES ('1', '8', '2023-07-28 08:30:50', null, '2023-07-28 08:30:50', null, '0');

#广告表
create table ad (
id int(11) not null auto_increment comment '主键',
title varchar(50) default null comment '标题',
img_file_name varchar(100) default null comment '图片文件名',
link varchar(200) default null comment '',
weight int(11) default null comment '',
primary key (id)
)engine=InnoDB default charset=utf8;

#字典表
create table dic (
type varchar(50) not null comment '类型',
code varchar(50) not null comment '编码',
name varchar(50) default null comment '名称',
weight int(11) default null comment '权重',
primary key (type,code)
) engine=InnoDB default charset=utf8;

#商户表
CREATE TABLE `business` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `subtitle` varchar(50) DEFAULT NULL COMMENT '子标题',
  `img_file_name` varchar(100) DEFAULT NULL COMMENT '图片名称',
  `price` int(11) DEFAULT NULL COMMENT '商品价格',
  `distance` int(11) DEFAULT NULL COMMENT '距离',
  `number` int(11) DEFAULT NULL COMMENT '已售数量',
  `star` int(11) DEFAULT NULL COMMENT '星级',
  `commentNum` int(11) DEFAULT '0',
  `desc` varchar(200) DEFAULT NULL COMMENT '商品描述',
  `city` varchar(50) DEFAULT NULL COMMENT '城市编码',
  `category` varchar(50) DEFAULT NULL COMMENT '商品类别编码',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#会员表
create table user (
id int(11) not null auto_increment comment '主键',
phone bigint(13) default null comment '手机号',
`name` varchar(16) default null comment '用户名',
`password` char(32) default null comment '密码',
createDate datetime default null,
lastLoginTime timestamp not null default current_timestamp on update current_timestamp,
primary key (id),
unique key phone_unique (phone),
unique key name_unique (`name`)
) engine=innodb default charset=utf8;

#订单表
create table orders(
id int(11) not null auto_increment comment '主键',
business_id int(11) not null comment '商户id',
user_id int(11) not null comment '用户id',
num int(11) default null comment '购买数量',
comment_state int(1) default null comment '评论状态 0：未评论 2：已评论',
#总共位数11位,2位小数,9位整数
price decimal(11,2) default null comment '价格',
createDate timestamp not null default current_timestamp on update current_timestamp,
primary key (id),
#创建外键，同时创建索引
index fk_orders_business_idx (business_id),
index fk_orders_user_idx (user_id),
constraint fk_orders_business foreign key (business_id) references business (id),
constraint fk_orders_user foreign key (user_id) references user (id)
) engine=innodb default charset=utf8;

#同步表
create table syntime(
id int(11) not null auto_increment comment '主键',
`type` varchar(25) default null comment '同步的数据类型，用英文单词表示',
updateTime datetime default null comment '更新时间',
primary key (id)
) engine=innodb default charset=utf8;

#评论表
create table `comment`(
id int(11) not null auto_increment comment '主键',
order_id int(11) default null comment '订单id',
content varchar(255) default null comment '评论内容',
star int(1) default null comment '星级 1-5',
createTime timestamp not null default current_timestamp on update current_timestamp,
primary key (id),
#创建外键，同时创建索引
index fk_comment_order_idx (order_id),
constraint fk_comment_order foreign key (order_id) references orders (id)
) engine=innodb default charset=utf8;

#后台用户表
create table sys_user(
id int(11) not null auto_increment comment '主键',
group_id int(11) default null comment '用户组id',
username varchar(50) default null comment '用户名',
`password` varchar(32) default null comment '密码',
nickName varchar(50) default null comment '别名',
update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
#create_date timestamp default null comment '创建时间',
create_time datetime DEFAULT NULL,
primary key (id),
unique key username_unique (username),
key fk_sys_group_id (group_id),
constraint fk_sys_group_id foreign key(group_id) references sys_group(id)
) engine=innodb default charset=utf8;

#用户组
create table sys_group(
id int(11) not null auto_increment comment '主键',
`name` varchar(50) default null comment '组名',
primary key (id),
unique key unique_name (`name`)
)engine=innodb default charset=utf8;

#菜单
drop table if exists sys_menu;
create table sys_menu (
id int(11) not null auto_increment comment '主键',
`name` varchar(50) default null comment '组名',
url varchar(200) default null comment '访问地址',
parent_id int(11) default 0 comment '父菜单ID',
order_num int(2) default null comment '排序数字',
primary key (id)
) engine=innodb default charset=utf8;

#菜单下的操作
CREATE TABLE `sys_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单id',
  `name` varchar(50) DEFAULT NULL COMMENT '操作名',
  `url` varchar(200) DEFAULT NULL COMMENT '访问地址',
  `method` varchar(50) DEFAULT NULL COMMENT '操作',
  PRIMARY KEY (`id`),
  key fk_menu_id (menu_id),
  constraint fk_menu_id foreign key(menu_id) references sys_menu(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#组和菜单之间是多对对关系,所以冗余一张表,记录组和菜单之间的关系
create table sys_group_menu (
id int(11) not null auto_increment comment '主键',
group_id int(11) default null comment '组id',
menu_id int(11) default null comment '菜单id',
primary key (id)
) engine=innodb default charset=utf8;

#在给用户组分配菜单时，并不是菜单下的所有动作都会被分配，所以光记录“sys_group_menu”不够，还需要记录“用户组--动作”关系
CREATE TABLE `sys_group_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_id` int(11) DEFAULT NULL COMMENT '用户组id',
  `action_id` int(11) DEFAULT NULL COMMENT '操作id',
  PRIMARY KEY (`id`),
  KEY `fk_group_id` (`group_id`),
  KEY `fk_action_id` (`action_id`),
  CONSTRAINT `fk_action_id` FOREIGN KEY (`action_id`) REFERENCES `sys_action` (`id`),
  CONSTRAINT `fk_group_id` FOREIGN KEY (`group_id`) REFERENCES `sys_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#销量统计
CREATE TABLE `sales_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_name` varchar(50) DEFAULT NULL COMMENT '分类统计的类名',
  `hour` varchar(2) DEFAULT NULL COMMENT '时间',
  `sales_num` int(11) DEFAULT NULL COMMENT '销量',
  `type` varchar(50) DEFAULT NULL COMMENT '类别名：统计的是什么东西',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '同步时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
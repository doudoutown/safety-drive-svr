CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `age` tinyint(4) NOT NULL DEFAULT '0',
  `driving_age` tinyint(4) NOT NULL DEFAULT '0',
  `gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0女 1男',
  `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

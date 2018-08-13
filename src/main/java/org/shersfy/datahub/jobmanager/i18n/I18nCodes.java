package org.shersfy.datahub.jobmanager.i18n;

/**
 * I18n Message codes
 */
public interface I18nCodes {
	// I18n API message codes start
	/**token不存在**/
	public int TOKEN_NOT_EXIST = 1001;
	/**token失效**/
	public int TOKEN_DISABLED  = 1002;
	/**token过期**/
	public int TOKEN_EXPIRED   = 1003;
	/**创建token的用户信息有误**/
	public int TOKEN_CREATOR_ERROR		= 1004;
	/**权限拒绝, token没有权限访问**/
	public int TOKEN_PERMISSION_DENIED	= 1005;
	/**未开放API**/
	public int TOKEN_NOT_OPENED_API		= 1006;
	/**调用次数过多**/
	public int TOKEN_CALL_TOO_MUCH		= 1007;
	/**token已失效, 请登录datahub web页面刷新token, 可能token用户已被移除项目%s**/
	public int TOKEN_INVALID 			= 1008;
	// I18n API message codes end

	// I18n message codes start
	/** 系统错误 **/
	public String MSGT0000E000000 = "MSG.T0000.E000000";
	/** 系统错误: 系统内部错误, 请联系管理员 **/
	public String MSGT0000E000001 = "MSG.T0000.E000001";
	/** 系统错误: 域名解析异常, 未知域名 %s **/
	public String MSGT0000E000002 = "MSG.T0000.E000002";
	/** 无效参数值 **/
	public String MSGT0001E000000 = "MSG.T0001.E000000";
	/** 无效参数值: %s不能为空 **/
	public String MSGT0001E000001 = "MSG.T0001.E000001";
	/** 无效参数值: %s不能为负数 **/
	public String MSGT0001E000002 = "MSG.T0001.E000002";
	/** 无效参数值: 参数%s格式错误 **/
	public String MSGT0001E000003 = "MSG.T0001.E000003";
	/** 无效参数值: 参数%s数量为%s, 参数%s数量为%s, 数量不相等 **/
	public String MSGT0001E000004 = "MSG.T0001.E000004";
	/** 无效参数值: 参数%s数量不能为0 **/
	public String MSGT0001E000005 = "MSG.T0001.E000005";
	/** 无效参数值: 参数%s最大长度为%s, 最小长度为%s **/
	public String MSGT0001E000006 = "MSG.T0001.E000006";
	/** 无效参数值: 参数%s只能是数字 **/
	public String MSGT0001E000007 = "MSG.T0001.E000007";
	/** 无效参数值: 参数%s最小值为'%s', 但是输入为 '%s' **/
	public String MSGT0001E000008 = "MSG.T0001.E000008";
	/** 无效参数值: 参数%s最大值为'%s', 但是输入为 '%s' **/
	public String MSGT0001E000009 = "MSG.T0001.E000009";
	/** 无效参数值: 两个参数不能同时为空, %s和%s不能同时为空 **/
	public String MSGT0001E000010 = "MSG.T0001.E000010";
	/** 无效参数值: Hive配置未绑定有效的HDFS连接, 请检查Hive配置 **/
	public String MSGT0001E000011 = "MSG.T0001.E000011";
	/** 无效参数值: 参数%s位数不等于%s **/
	public String MSGT0001E000012 = "MSG.T0001.E000012";
	/** 无效参数值: 已超过最大支持数量: 单个任务数据源表数量(%s)已超过最大支持数, 最大值=%s, 当前值=%s **/
	public String MSGT0001E000013 = "MSG.T0001.E000013";
	/** 无效参数值: 参数%s无效, 非法的表名, 字母数字下划线组成 **/
	public String MSGT0001E000014 = "MSG.T0001.E000014";
	/** 无效参数值: 参数%s无效, 建表语句语法错误, 以CREATE开始\n%s **/
	public String MSGT0001E000015 = "MSG.T0001.E000015";
	/** 无效参数值: 参数%s无效, 建表语句语法错误, CREATE附近错误\n%s **/
	public String MSGT0001E000016 = "MSG.T0001.E000016";
	/** 无效参数值: 参数%s无效, 建表语句语法错误, TABLE附近错误\n%s **/
	public String MSGT0001E000017 = "MSG.T0001.E000017";
	/** 无效参数值: 参数%s无效, 输入的表名[%s]和建表语句DDL表名[%s]不一致 **/
	public String MSGT0001E000018 = "MSG.T0001.E000018";
	/** 无效参数值: 参数%s无效, 正则表达式格式错误'%s' **/
	public String MSGT0001E000019 = "MSG.T0001.E000019";
	/** 无效参数值: 参数%s无效, 亚马逊S3区域不存在 '%s'**/
	public String MSGT0001E000020 = "MSG.T0001.E000020";
	/** 无效参数值: 参数不一致, %s和%s不一致 **/
	public String MSGT0001E000021 = "MSG.T0001.E000021";
	/** 环境配置错误 **/
	public String MSGT0002E000000 = "MSG.T0002.E000000";
	/** 环境配置错误: LEAP系统hive连接URL有更新, 请配置最新的URL信息\nnew=%s\nold=%s **/
	public String MSGT0002E000001 = "MSG.T0002.E000001";
	/** 环境配置错误: LEAP Hadoop集群kerberos认证失败, 请检查配置 **/
	public String MSGT0002E000002 = "MSG.T0002.E000002";
	/** 环境配置错误: 文件不存在 %s **/
	public String MSGT0002E000003 = "MSG.T0002.E000003";
	/** 环境配置错误: krb5.conf文件内容无效, 文件存在, 且文件大小不超过10M, 且包含内容"[libdefaults]", "[realms]", "[domain_realm]"\n%s **/
	public String MSGT0002E000004 = "MSG.T0002.E000004";
	/** 环境配置错误: LEAP HDFS未授权目录[%s], 请先创建目录 **/
	public String MSGT0002E000005 = "MSG.T0002.E000005";
	/** 环境配置错误: Kerberos认证模式下, Hive URL未指定principal参数\n%s **/
	public String MSGT0002E000006 = "MSG.T0002.E000006";
	/** 环境配置错误: Hive JDBC URL连接串HA模式缺少"namespace.hive"参数 **/
	public String MSGT0002E000007 = "MSG.T0002.E000007";
	/** 环境配置错误: URL格式错误: %s **/
	public String MSGT0002E000008 = "MSG.T0002.E000008";
	/** 环境配置错误: 保存SMS服务信息错误, 请检查配置 **/
	public String MSGT0002E000009 = "MSG.T0002.E000009";
	/** 环境配置错误: 发送短信错误, 请检查配置 **/
	public String MSGT0002E000010 = "MSG.T0002.E000010";
	/** DB连接错误 **/
	public String MSGT0003E000000 = "MSG.T0003.E000000";
	/** DB连接错误: 连接名已存在 %s ,请修改为其它值 **/
	public String MSGT0003E000001 = "MSG.T0003.E000001";
	/** DB连接错误: 用户名或密码错误 **/
	public String MSGT0003E000002 = "MSG.T0003.E000002";
	/** DB连接错误: 未知的数据库 %s **/
	public String MSGT0003E000003 = "MSG.T0003.E000003";
	/** DB连接错误: 通过端口[%s]连接到[%s]的TCP/IP连接失败。错误：拒绝连接 **/
	public String MSGT0003E000004 = "MSG.T0003.E000004";
	/** DB连接错误: 不支持该数据库类型 %s **/
	public String MSGT0003E000005 = "MSG.T0003.E000005";
	/** DB连接错误: 无效的JDBC URL '%s' 连接到%s **/
	public String MSGT0003E000006 = "MSG.T0003.E000006";
	/** 创建表错误 **/
	public String MSGT0004E000000 = "MSG.T0004.E000000";
	/** 创建表错误: 创建表失败\n%s **/
	public String MSGT0004E000001 = "MSG.T0004.E000001";
	/** 创建表错误: 抱歉，您没有该库%s的建表权限 **/
	public String MSGT0004E000002 = "MSG.T0004.E000002";
	/** 创建表错误: 数量不等, 处理结果数量为%s, 目标表数量为%s **/
	public String MSGT0004E000003 = "MSG.T0004.E000003";
	/** 创建表错误: **/
	public String MSGT0004E000004 = "MSG.T0004.E000004";
	/** 创建表错误: **/
	public String MSGT0004E000005 = "MSG.T0004.E000005";
	/** 获取数据库错误 **/
	public String MSGT0005E000000 = "MSG.T0005.E000000";
	/** 获取数据库错误: **/
	public String MSGT0005E000001 = "MSG.T0005.E000001";
	/** 获取数据库错误: **/
	public String MSGT0005E000002 = "MSG.T0005.E000002";
	/** 获取数据库错误: **/
	public String MSGT0005E000003 = "MSG.T0005.E000003";
	/** 获取数据库错误: **/
	public String MSGT0005E000004 = "MSG.T0005.E000004";
	/** 获取数据库错误: **/
	public String MSGT0005E000005 = "MSG.T0005.E000005";
	/** 获取表错误 **/
	public String MSGT0006E000000 = "MSG.T0006.E000000";
	/** 获取表错误: **/
	public String MSGT0006E000001 = "MSG.T0006.E000001";
	/** 获取表错误: **/
	public String MSGT0006E000002 = "MSG.T0006.E000002";
	/** 获取表错误: **/
	public String MSGT0006E000003 = "MSG.T0006.E000003";
	/** 获取表错误: **/
	public String MSGT0006E000004 = "MSG.T0006.E000004";
	/** 获取表错误: **/
	public String MSGT0006E000005 = "MSG.T0006.E000005";
	/** 获取字段列错误 **/
	public String MSGT0007E000000 = "MSG.T0007.E000000";
	/** 获取字段列错误: **/
	public String MSGT0007E000001 = "MSG.T0007.E000001";
	/** 获取字段列错误: **/
	public String MSGT0007E000002 = "MSG.T0007.E000002";
	/** 获取字段列错误: **/
	public String MSGT0007E000003 = "MSG.T0007.E000003";
	/** 获取字段列错误: **/
	public String MSGT0007E000004 = "MSG.T0007.E000004";
	/** 获取字段列错误: **/
	public String MSGT0007E000005 = "MSG.T0007.E000005";
	/** 获取DDL错误 **/
	public String MSGT0008E000000 = "MSG.T0008.E000000";
	/** 获取DDL错误: 源表%s, 目标表%s **/
	public String MSGT0008E000001 = "MSG.T0008.E000001";
	/** 获取DDL错误: 数量不等, 源表数量为%s, 目标表数量为%s **/
	public String MSGT0008E000002 = "MSG.T0008.E000002";
	/** 获取DDL错误: **/
	public String MSGT0008E000003 = "MSG.T0008.E000003";
	/** 获取DDL错误: **/
	public String MSGT0008E000004 = "MSG.T0008.E000004";
	/** 获取DDL错误: **/
	public String MSGT0008E000005 = "MSG.T0008.E000005";
	/** 获取DDL错误: **/
	public String MSGT0008E000006 = "MSG.T0008.E000006";
	/** 获取分区错误 **/
	public String MSGT0009E000000 = "MSG.T0009.E000000";
	/** 获取分区错误: 表不存在 %s **/
	public String MSGT0009E000001 = "MSG.T0009.E000001";
	/** 获取分区错误: **/
	public String MSGT0009E000002 = "MSG.T0009.E000002";
	/** 获取分区错误: **/
	public String MSGT0009E000003 = "MSG.T0009.E000003";
	/** 获取分区错误: **/
	public String MSGT0009E000004 = "MSG.T0009.E000004";
	/** 获取分区错误: **/
	public String MSGT0009E000005 = "MSG.T0009.E000005";
	/** 检索数据错误 **/
	public String MSGT0010E000000 = "MSG.T0010.E000000";
	/** 检索数据错误: **/
	public String MSGT0010E000001 = "MSG.T0010.E000001";
	/** Hive连接错误 **/
	public String MSGT0011E000000 = "MSG.T0011.E000000";
	/** Hive连接错误: **/
	public String MSGT0011E000001 = "MSG.T0011.E000001";
	/** Hive连接错误: **/
	public String MSGT0011E000002 = "MSG.T0011.E000002";
	/** Hive连接错误: **/
	public String MSGT0011E000003 = "MSG.T0011.E000003";
	/** Hive连接错误: **/
	public String MSGT0011E000004 = "MSG.T0011.E000004";
	/** Hive连接错误: **/
	public String MSGT0011E000005 = "MSG.T0011.E000005";
	/** SparkMPP连接错误 **/
	public String MSGT0012E000000 = "MSG.T0012.E000000";
	/** SparkMPP连接错误: **/
	public String MSGT0012E000001 = "MSG.T0012.E000001";
	/** SparkMPP连接错误: **/
	public String MSGT0012E000002 = "MSG.T0012.E000002";
	/** SparkMPP连接错误: **/
	public String MSGT0012E000003 = "MSG.T0012.E000003";
	/** SparkMPP连接错误: **/
	public String MSGT0012E000004 = "MSG.T0012.E000004";
	/** SparkMPP连接错误: **/
	public String MSGT0012E000005 = "MSG.T0012.E000005";
	/** HDFS连接错误 **/
	public String MSGT0013E000000 = "MSG.T0013.E000000";
	/** HDFS连接错误: 连接名已存在 %s ,请修改为其它值 **/
	public String MSGT0013E000001 = "MSG.T0013.E000001";
	/** HDFS连接错误: krb5.conf文件内容无效, 文件存在, 且文件大小不超过10M, 且包含内容"[libdefaults]", "[realms]", "[domain_realm]"\n%s **/
	public String MSGT0013E000002 = "MSG.T0013.E000002";
	/** HDFS连接错误: 尝试连接失败 %s **/
	public String MSGT0013E000003 = "MSG.T0013.E000003";
	/** HDFS连接错误: 持久化HdfsInfo对象到数据库失败 **/
	public String MSGT0013E000004 = "MSG.T0013.E000004";
	/** HDFS连接错误: 权限不足, 拒绝访问: user=%s, access=READ_WRITE, owner=%s, path="%s" **/
	public String MSGT0013E000005 = "MSG.T0013.E000005";
	/** HDFS连接错误: **/
	public String MSGT0013E000006 = "MSG.T0013.E000006";
	/** FTP连接错误 **/
	public String MSGT0014E000000 = "MSG.T0014.E000000";
	/** FTP连接错误: 获取FTP文件列表错误 **/
	public String MSGT0014E000001 = "MSG.T0014.E000001";
	/** FTP连接错误: 连接名已存在 %s ,请修改为其它值 **/
	public String MSGT0014E000002 = "MSG.T0014.E000002";
	/** FTP连接错误: **/
	public String MSGT0014E000003 = "MSG.T0014.E000003";
	/** FTP连接错误: **/
	public String MSGT0014E000004 = "MSG.T0014.E000004";
	/** FTP连接错误: **/
	public String MSGT0014E000005 = "MSG.T0014.E000005";
	/** S3连接错误 **/
	public String MSGT0015E000000 = "MSG.T0015.E000000";
	/** S3连接错误: **/
	public String MSGT0015E000001 = "MSG.T0015.E000001";
	/** 客户端错误 **/
	public String MSGT0016E000000 = "MSG.T0016.E000000";
	/** 客户端错误: 客户端未启动, 请启动客户端 [%s] **/
	public String MSGT0016E000001 = "MSG.T0016.E000001";
	/** 客户端错误: **/
	public String MSGT0016E000002 = "MSG.T0016.E000002";
	/** 客户端错误: **/
	public String MSGT0016E000003 = "MSG.T0016.E000003";
	/** 客户端错误: **/
	public String MSGT0016E000004 = "MSG.T0016.E000004";
	/** 客户端错误: **/
	public String MSGT0016E000005 = "MSG.T0016.E000005";
	/** 同一集群验证错误 **/
	public String MSGT0017E000000 = "MSG.T0017.E000000";
	/** 同一集群验证错误: 该hdfs用户"%s"没有权限验证集群信息, 请确认hive和它绑定的hdfs是同一个集群 **/
	public String MSGT0017E000001 = "MSG.T0017.E000001";
	/** 同一集群验证错误: 绑定的HDFS连接信息[%s]和Hive连接信息可能不是同一集群, 请检查配置\nHive访问地址%s 不在Hadoop集群列表中[\n%s] **/
	public String MSGT0017E000002 = "MSG.T0017.E000002";
	/** 同一集群验证错误: **/
	public String MSGT0017E000003 = "MSG.T0017.E000003";
	/** 同一集群验证错误: **/
	public String MSGT0017E000004 = "MSG.T0017.E000004";
	/** 同一集群验证错误: **/
	public String MSGT0017E000005 = "MSG.T0017.E000005";
	/** 记录不存在: %s记录不存在[id=%s] **/
	public String MSGT0018I000001 = "MSG.T0018.I000001";
	/** CSV文件上传错误 **/
	public String MSGT0018E000000 = "MSG.T0018.E000000";
	/** CSV文件上传错误: 上传文件中不包含任何*.txt|*.csv文件 **/
	public String MSGT0018E000001 = "MSG.T0018.E000001";
	/** CSV文件上传错误: 不支持的文件格式 [*.%s] **/
	public String MSGT0018E000002 = "MSG.T0018.E000002";
	/** CSV文件上传错误: **/
	public String MSGT0018E000003 = "MSG.T0018.E000003";
	/** CSV文件上传错误: **/
	public String MSGT0018E000004 = "MSG.T0018.E000004";
	/** CSV文件上传错误: **/
	public String MSGT0018E000005 = "MSG.T0018.E000005";
	/** 文件不存在: 文件不存在 %s **/
	public String MSGT0019E000001 = "MSG.T0019.E000001";
	/** 获取分隔符错误 **/
	public String MSGT0020E000000 = "MSG.T0020.E000000";
	/** SQL文件解析错误 **/
	public String MSGT0021E000000 = "MSG.T0021.E000000";
	/** SQL文件解析错误: SQL文件解析内容为空 **/
	public String MSGT0021E000001 = "MSG.T0021.E000001";
	/** 上传错误 **/
	public String MSGT0022E000000 = "MSG.T0022.E000000";
	/** 上传错误: hdfs文件加载到hive失败 %s **/
	public String MSGT0022E000001 = "MSG.T0022.E000001";
	/** 上传错误: 文件上传失败, 接收到的文件大小%s和实际大小%s不相等 **/
	public String MSGT0022E000002 = "MSG.T0022.E000002";
	/** 文件合并错误 **/
	public String MSGT0023E000000 = "MSG.T0023.E000000";
	/** 文件合并错误: 文件md5验证失败, 客户端md5=%s, 服务器md5=%s **/
	public String MSGT0023E000001 = "MSG.T0023.E000001";
	/** 登录错误 **/
	public String MSGT0024E000000 = "MSG.T0024.E000000";
	/** 登录错误: 用户不存在 **/
	public String MSGT0024E000001 = "MSG.T0024.E000001";
	/** 登录错误: 密码错误 **/
	public String MSGT0024E000002 = "MSG.T0024.E000002";
	/** 重置错误 **/
	public String MSGT0025E000000 = "MSG.T0025.E000000";
	/** 重置错误: 用户不存在 **/
	public String MSGT0025E000001 = "MSG.T0025.E000001";
	/** 重置错误: 密码不能为空 **/
	public String MSGT0025E000002 = "MSG.T0025.E000002";
	/** 注册错误 **/
	public String MSGT0026E000000 = "MSG.T0026.E000000";
	/** 注册错误: **/
	public String MSGT0026E000001 = "MSG.T0026.E000001";
	/** 任务保存错误 **/
	public String MSGT0027E000000 = "MSG.T0027.E000000";
	/** 任务保存错误: 该任务正在执行中，请稍后重试[%s] **/
	public String MSGT0027E000001 = "MSG.T0027.E000001";
	/** 任务保存错误: 任务已过时\ncron=%s, \n有效时间 %s ~ %s **/
	public String MSGT0027E000002 = "MSG.T0027.E000002";
	/** 任务保存错误: 添加任务到调度器失败[%s] **/
	public String MSGT0027E000003 = "MSG.T0027.E000003";
	/** 任务保存错误: 文件不存在 %s **/
	public String MSGT0027E000004 = "MSG.T0027.E000004";
	/** 任务保存错误: 不支持的目标类型 %s**/
	public String MSGT0027E000005 = "MSG.T0027.E000005";
	/** 任务保存错误: **/
	public String MSGT0027E000006 = "MSG.T0027.E000006";
	/** 任务保存错误: **/
	public String MSGT0027E000007 = "MSG.T0027.E000007";
	/** 任务保存错误: **/
	public String MSGT0027E000008 = "MSG.T0027.E000008";
	/** 任务保存错误: **/
	public String MSGT0027E000009 = "MSG.T0027.E000009";
	/** 任务保存错误: **/
	public String MSGT0027E000010 = "MSG.T0027.E000010";
	/** 任务调度错误 **/
	public String MSGT0028E000000 = "MSG.T0028.E000000";
	/** 任务调度错误: **/
	public String MSGT0028E000001 = "MSG.T0028.E000001";
	/** 激活任务: 任务 %s 已被停止 **/
	public String MSGT0029I000001 = "MSG.T0029.I000001";
	/** 停止任务错误 **/
	public String MSGT0029E000000 = "MSG.T0029.E000000";
	/** 停止任务错误: 该任务正在调度中，请先稍后重试 **/
	public String MSGT0029E000001 = "MSG.T0029.E000001";
	/** 激活任务: 任务 %s 已被激活 **/
	public String MSGT0030I000001 = "MSG.T0030.I000001";
	/** 激活任务错误 **/
	public String MSGT0030E000000 = "MSG.T0030.E000000";
	/** 激活任务错误: **/
	public String MSGT0030E000001 = "MSG.T0030.E000001";
	/** 权限错误 **/
	public String MSGT0031E000000 = "MSG.T0031.E000000";
	/** 权限错误: 您不是该项目成员, 操作无效 **/
	public String MSGT0031E000001 = "MSG.T0031.E000001";
	/** 权限错误: 你不是系统管理员, 操作无效 **/
	public String MSGT0031E000002 = "MSG.T0031.E000002";
	/** 权限错误: 请求被拒绝, 只接受post请求 **/
	public String MSGT0031E000003 = "MSG.T0031.E000003";
	/** 权限错误: 权限拒绝, 文件或目录不能被删除, %s 不是以 %s 开始的 **/
	public String MSGT0031E000004 = "MSG.T0031.E000004";
	/** 权限错误: 权限拒绝, 目录不能被删除 %s **/
	public String MSGT0031E000005 = "MSG.T0031.E000005";
	/** 删除错误 **/
	public String MSGT0032E000000 = "MSG.T0032.E000000";
	/** 删除错误: LEAP系统配置不能删除 **/
	public String MSGT0032E000001 = "MSG.T0032.E000001";
	/** 删除错误: **/
	public String MSGT0032E000002 = "MSG.T0032.E000002";
	/** 下载错误 **/
	public String MSGT0033E000000 = "MSG.T0033.E000000";
	/** 下载错误: **/
	public String MSGT0033E000001 = "MSG.T0033.E000001";
	/** 保存报警规则错误 **/
	public String MSGT0034E000000 = "MSG.T0034.E000000";
	/** Excel文件上传错误 **/
	public String MSGT0035E000000 = "MSG.T0035.E000000";
	/** Excel文件上传错误: **/
	public String MSGT0035E000001 = "MSG.T0035.E000001";
	// I18n message codes end
}

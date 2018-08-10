package org.shersfy.datahub.jobmanager.constant;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 常量
 *
 */
public class Const {

	private Const(){
	}

	/**系统异常信息**/
	public static final String SYS_ERR_MSG 	= "sys_error";

	public static final String CLIENT_REQ_WEBSOCK 	= "/websock";
	public static final String CLIENT_REQ 			= "/client/";
	
	/**hdfs默认用户访问根目录/user/%s**/
	public static final String HDFS_HOME_DEFAULT 	= "/user/%s";
	// cookie key
	public static final String LOGIN_SSO_IDINFO = "sso_idinfo";
	public static final String LOGIN_USER_KEY 	= "loginUser";
	public static final String LOGIN_ID_INFO 	= "IdInfo";
	public static final String I18N		 		= "i18n";
	public static final String COOKIE_KEY 		= "leapId";
	public static final String COOKIE_LANG 		= "lang";
	public static final String COOKIE_LANG_TIME = "timestamp";
	
	/**请求响应类型--json**/
	public static final String RESPONSE_CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

	/**登录用户管理员角色**/
	public static final String LOGIN_USER_ADMIN 	= "leapid.admin";
	/**登录用户项目管理员角色**/
	public static final String LOGIN_USER_PM 		= "leapid.pm";
	/**登录用户项目成员角色**/
	public static final String LOGIN_USER_MEMBER 	= "leapid.member";

	/**leap系统环境配置DB连接名**/
	public static final String LEAP_SYSTEM_DB 		= "LEAP_SYSTEM_DB";
	/**leap系统环境配置hdfs连接名**/
	public static final String LEAP_SYSTEM_HDFS 	= "LEAP_SYSTEM_HDFS";
	/**leap系统环境配置hive连接名**/
	public static final String LEAP_SYSTEM_HIVE 	= "LEAP_SYSTEM_HIVE";

	// current app
	public static final String APP_NAME_COOKIE_KEY = "appName";

	public static final int TICKET_TYPE_FORM = 0;
	public static final int TICKET_TYPE_TOKEN = 1;

	public static final byte UPLOAD_STATUS_SUCCESS = 1;
	public static final byte UPLOAD_STATUS_FAIL = 0;

	/**断点续传阀值**/
	public static final Long BLOCK_THRESHOLD = 10 * 1024 * 1024L;
	/**客户端上传的非dump文件**/
	public static final int NOT_DUMP_FILE = 1;
	/**客户端上传的dump文件**/
	public static final int DUMP_FILE = 2;

	public static final String BASE_PATH 	= "basePath";
	public static final String RETURN_URL 	= "returnUrl";

	public static final String URL_KEY 				= "url";
	public static final String APP_ID_KEY 			= "appId";
	public static final String APP_USER_ID_KEY 		= "userId";
	
	/**Hive查询超时(秒)**/
	public static final String HIVE_QUERY_TIMEOUT 			= "hive.server2.query.timeout";
	/**客户端请求配置-连接超时**/
	public static final String CONFIG_CONN_TIMEOUT_KEY 		= "configConnTimeout";
	/**客户端请求配置-socket超时**/
	public static final String CONFIG_SOCKET_TIMEOUT_KEY 	= "configSocketTimeout";
	/**HDFS环境变量**/
	public static final String HADOOP_USER_NAME 	= "HADOOP_USER_NAME";
	/**字母数字下划线组成, 数字不能开头**/
	public static final String PATTERN_LEGAL1 	= "[a-zA-Z_][a-zA-Z0-9_]+";
	/**字母数字下划线组成**/
	public static final String PATTERN_LEGAL2 	= "[a-zA-Z0-9_]+";

	/**code=-2 tip=ssh登录异常**/
	public static final int SSH_ERROR	= -2;
	/**系统换行符**/
	public static final String LINE_SEP 	= System.getProperty("line.separator", "\n");
	/**系统换行符匹配**/
	public static final String LINE_SEP_REG = "\r\n|\n|\r";
	public static final String COLUMN_SEP 	= "\u0001";
	/**文件锁定超时**/
	public static final long FILE_LOCK_TIMEOUT 	= 5*60*1000;
	/**最小时间1900-01-01 0:0:0.0**/
	public static final long MIN_DATE = -2209017600000L;
	/**最大时间9999-12-31 23:59:59.999**/
	public static final long MAX_DATE = 253402271999999L;
	/** 打印日志时间间隔 n(秒)x1000 **/
	public static final long PRINTLOG_TIME = 30 * 1000;
	/** 默认cron表达式 **/
	public static final String CRON_DEFAULT = "* * * * * ?";

	/**年份类型: yyyy**/
	public static final String yyyy		= "yyyy";
	/**年月类型: yyyyMM**/
	public static final String yyyyMM 	= "yyyy/MM";
	/**日期统一格式:yyyy/MM/dd**/
	public static final String yyyyMMdd 		= "yyyy/MM/dd";
	/**文件夹时间戳格式**/
	public static final String FOLDER_TIMESTAMP = "yyyyMMddHHmmssSSS";
	/**数据转string日期格式**/
	public static final String FORMAT_DATE		= "yyyy-MM-dd";
	/**数据转string日期时间格式**/
	public static final String FORMAT_DATETIME	= "yyyy-MM-dd HH:mm:ss";
	/**数据转string日期时间格式**/
	public static final String FORMAT_TIMESTAMP	= "yyyy-MM-dd HH:mm:ss.SSS";
	/**日期时间统一格式:yyyy/MM/dd HH:mm:ss**/
	public static final String yyyyMMddHHmmss 	= "yyyy/MM/dd HH:mm:ss";
	/**时间统一格式:HH:mm:ss**/
	public static final String HHmmss 			= "HH:mm:ss";
	/**浮点型小数位默认保留30位数**/
	public static final String DEC_FORMAT 		= "#.##############################";
	/**hive数据库类型**/
	public static final long DB_TYPE_HIVE 		= 31;
	/**spark mpp数据类型**/
	public static final long DB_TYPE_SPARKMPP   = 62;

	/**客户端重连时间**/
	public static final long CLIENT_RECONNECT_TIME = 1 * 60 * 1000;
	/**请求超时时间**/
	public static final long REQUEST_TIMEOUT	= 1 * 60 * 1000;
	/**超时时间30秒**/
	public static final long CONNECT_TIMEOUT	= 30 * 1000;
	/**缓存行数计算权值**/
	public static final int DEFAULT_BLOCK_WEIGHT = 5000;
	/**blank**/
	public static final String BLANK = "blank";

	/**Oracle中nvarchar2类型**/
	public static final String TYPE_NVARCHAR2 	= "NVARCHAR2";
	/**Oracle中nchar类型**/
	public static final String TYPE_NCHAR	 	= "NCHAR";
	/**Oracle中nclob类型**/
	public static final String TYPE_NCLOB	 	= "NCLOB";
	/**Oracle中rowid类型**/
	public static final String TYPE_ROWID		= "ROWID";
	/**Oracle中tiemstamp类型**/
	public static final String TYPE_TIMESTAMP	= "TIMESTAMP";
	/**Oracle中raw类型**/
	public static final String TYPE_RAW			= "RAW";
	/**Mssql中hierarchyid类型**/
	public static final String TYPE_HIERARCHYID	= "HIERARCHYID";
	
	/**短信服务电话号码参数**/
	public static final String SMS_PHONE_NUMBERS = "${phone_numbers}";
	/**短信服务短信内容参数**/
	public static final String SMS_CONTENT 		 = "${content}";

	/**where关键字**/
	public static final String WHERE_STR	 	= "WHERE ";
	/**and关键字**/
	public static final String AND_STR	 		= " AND ";
	/**err_data分隔符**/
	public static final String ERROR_DATA_SEPARATOR = "|";
	
	/**数据库数据插入类型**/
	public static enum InsertType{
		/**正常插入**/
		Normal,
		/**更新插入**/
		UpdateInsert,
		/**删除插入**/
		DeleteInsert;

		public static InsertType valueOf(Integer index){
			if(index == null){
				return Normal;
			}
			switch (index) {
			case 1:
				return UpdateInsert;
			case 2:
				return DeleteInsert;
			default:
				return Normal;
			}
		}

		public int index(){
			return this.ordinal();
		}
	}
	
	/**任务类型或数据源类型**/
	public static enum JobTypes{
		/**空类型**/
		Dummy("dummy"),
		/**本地上传**/
		LocalUpload("local"),
		/**数据库上传**/
		DatabaseMove("db"),
		/**客户端上传**/
		ClientUpload("client"),
		/**dump上传**/
		DumpUpload("dump"),
		/**S3上传**/
		S3Upload("s3"),
		/**阿里云关系型数据库**/
		AliyunRDS("alirds"),
		/**亚马逊关系型数据库**/
		AmazonRDS("awsrds"),
		/**Hadoop分布式文件系统**/
		HDFS("hdfs"),
		/**hive数据库**/
		Hive("hive"),
		/**csv文件上传**/
		CsvUpload("csv"),
		/**excel文件上传**/
		ExcelUpload("excel"),
		/**Ftp文件上传**/
		FtpUpload("ftp"),
		/**HiveSpark**/
		HiveSpark("spark");

		private String alias;

		JobTypes(String alias){
			this.alias = alias;
		}

		public static JobTypes valueOf(Integer index){
			if(index == null){
				return Dummy;
			}
			switch (index) {
			case 1:
				return LocalUpload;
			case 2:
				return DatabaseMove;
			case 3:
				return ClientUpload;
			case 4:
				return DumpUpload;
			case 5:
				return S3Upload;
			case 6:
				return AliyunRDS;
			case 7:
				return AmazonRDS;
			case 8:
				return HDFS;
			case 9:
				return Hive;
			case 10:
				return CsvUpload;
			case 11:
				return ExcelUpload;
			case 12:
				return FtpUpload;
			case 13:
				return HiveSpark;
			default:
				return Dummy;
			}
		}

		public int index(){
			return this.ordinal();
		}

		/**
		 * 获取别名
		 * 
		 * @author PengYang
		 * @date 2016-09-04
		 * 
		 * @param type
		 * @return String
		 */
		public String alias(){
			return this.alias;
		}
	}

	/**任务状态**/
	public static enum JobStatus{
		/**空状态**/
		Dummy,
		/**调度中。有效期内**/
		Scheduling,
		/**调度结束。有效期失效**/
		Scheduled,
		/**调度暂停, 有效期内点击【暂停】操作移除调度, 点击【激活】重新加入调度，超过有效期点【激活】变为调度结束**/
		Paused;

		public static JobStatus valueOf(Integer index){
			if(index == null){
				return Dummy;
			}
			switch (index) {
			case 1:
				return Scheduling;
			case 2:
				return Scheduled;
			case 3:
				return Paused;
			default:
				return Dummy;
			}
		}

		public int index(){
			return this.ordinal();
		}
	}

	/**任务日志状态**/
	public static enum JobLogStatus{
		/**空状态**/
		Dummy,
		/**执行中。 一次任务正在执行**/
		Executing,
		/**成功。一次任务执行成功结束**/
		Successful,
		/**失败。一次任务执行异常结束**/
		Failed;

		public static JobLogStatus valueOf(Integer index){
			if(index == null){
				return Dummy;
			}
			switch (index) {
			case 1:
				return Executing;
			case 2:
				return Successful;
			case 3:
				return Failed;
			default:
				return Dummy;
			}
		}

		public int index(){
			return this.ordinal();
		}
	}

	/**任务运行周期类型**/
	public static enum JobPeriod{
		/**周期类型-立刻执行**/
		PeriodOnceImmed,
		/**周期类型-定时**/
		PeriodOnceOntime,
		/**周期类型-周期性**/
		PeriodCircle;

		public static JobPeriod valueOf(Integer index){
			if(index == null){
				return PeriodOnceImmed;
			}
			switch (index) {
			case 1:
				return PeriodOnceOntime;
			case 2:
				return PeriodCircle;
			default:
				return PeriodOnceImmed;
			}
		}

		public int index(){
			return this.ordinal();
		}
	}

	/**访问应用类型**/
	public static enum AppType{
		/**空类型**/
		Dummy(""),
		/**浏览器**/
		Browser("BS"),
		/**客户端**/
		Client("CS");

		private String alias;

		AppType(String alias){
			this.alias = alias;
		}

		public static AppType valueOf(Integer index){
			if(index == null){
				return Dummy;
			}
			switch (index) {
			case 1:
				return Browser;
			case 2:
				return Client;
			default:
				return Dummy;
			}
		}

		public int index(){
			return this.ordinal();
		}

		/**
		 * 获取别名
		 * 
		 * @author PengYang
		 * @date 2016-09-04
		 * 
		 * @param type
		 * @return String
		 */
		public String alias(){
			return this.alias;
		}
	}

	/**简单cron表达式解析周期类型**/
	public static enum CronType{
		/**立刻执行一次**/
		OnceImmediately,
		/**定时一次**/
		Onece,
		/**每秒钟**/
		PerSecond,
		/**每分钟**/
		PerMinute,
		/**每小时**/
		PerHour,
		/**每天**/
		PerDay,
		/**每周**/
		PerWeek,
		/**每月**/
		PerMonth,
		/**每年**/
		PerYear,
		/**cron表达式**/
		CronExpression;

		private int offset = 1;//增加值

		/****/
		public static CronType valueOfByCron(String cron){
			if(cron == null){
				return null;
			}
			cron = cron.trim();
			String[] a = cron.split(" ");
			//秒
			if(a.length>0 && a[0].contains("/")){
				String[] ss = a[0].split("/");
				if(ss.length>0){
					PerSecond.setOffset(Integer.valueOf(ss[1]));
				}
				return PerSecond;
			}
			//分
			if(a.length>1 && a[1].contains("/")){
				String[] ss = a[1].split("/");
				if(ss.length>0){
					PerMinute.setOffset(Integer.valueOf(ss[1]));
				}
				return PerMinute;
			}
			//时
			if(a.length>2 && a[2].contains("/")){
				String[] ss = a[2].split("/");
				if(ss.length>0){
					PerHour.setOffset(Integer.valueOf(ss[1]));
				}
				return PerHour;
			}
			//日
			if(a.length>3 && a[3].contains("/")){
				String[] ss = a[3].split("/");
				if(ss.length>0){
					PerDay.setOffset(Integer.valueOf(ss[1]));
				}
				return PerDay;
			}
			//月
			if(a.length>4 && a[4].contains("/")){
				String[] ss = a[4].split("/");
				if(ss.length>0){
					PerMonth.setOffset(Integer.valueOf(ss[1]));
				}
				return PerMonth;
			}
			//周
			if(a.length>5){
				if(a[5].contains("/")){
					String[] ss = a[5].split("/");
					if(ss.length>0){
						PerWeek.setOffset(Integer.valueOf(ss[1]));
					}
					return PerWeek;
				}

				if(!("?".equals(a[5].trim()) || "*".equals(a[5].trim()))){
					return PerWeek;
				}
			}
			//年
			if(a.length>6 && a[6].contains("/")){
				String[] ss = a[6].split("/");
				if(ss.length>0){
					PerYear.setOffset(Integer.valueOf(ss[1]));
				}
				return PerYear;
			}

			return CronExpression;
		}

		public int index(){
			return this.ordinal();
		}

		public int offset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		@Override
		public String toString(){
			return "{\"index\":\""+this.index()+"\",\"period\":\""+this.name()
			+"\",\"offset\":\""+this.offset+"\"}";
		}
	}

	/**
	 * 从excel获取单元格日期时间格式解析, 统一日期时间格式<br/>
	 * 返回统一的日期时间格式<br/>
	 * 
	 * @author PengYang
	 * @date 2017-03-30
	 * 
	 * @param xlsFormat 来自excel的单元日期格式
	 * @return String
	 */
	public static String toDateFormatFromXls(String xlsFormat){

		if(StringUtils.isBlank(xlsFormat)){
			return yyyyMMddHHmmss;
		}
		
		if(StringUtils.containsIgnoreCase(xlsFormat, "[DBNum")){
			xlsFormat = xlsFormat.substring(xlsFormat.lastIndexOf("]")+"]".length());
		}
		if(StringUtils.containsIgnoreCase(xlsFormat, "[$-")){
			xlsFormat = xlsFormat.substring(xlsFormat.lastIndexOf("]")+"]".length());
		}
		// 年份yy
		boolean yy = StringUtils.containsIgnoreCase(xlsFormat, "yy");
		yy = yy && !StringUtils.containsIgnoreCase(xlsFormat, "m");
		yy = yy && !StringUtils.containsIgnoreCase(xlsFormat, "d");
		yy = yy || xlsFormat.matches("[y]{1,}");
		// 年月yy&&m
		boolean ym = StringUtils.containsIgnoreCase(xlsFormat, "yy");
		ym = ym && StringUtils.containsIgnoreCase(xlsFormat, "m");
		ym = ym && !StringUtils.containsIgnoreCase(xlsFormat, "d");
		ym = ym || xlsFormat.matches("[m]{1,}");
		// 日期yy&&m&d
		boolean ymd = StringUtils.containsIgnoreCase(xlsFormat, "yy");
		ymd = ymd && StringUtils.containsIgnoreCase(xlsFormat, "m");
		ymd = ymd && StringUtils.containsIgnoreCase(xlsFormat, "d");
		// d...型
		ymd = ymd || xlsFormat.matches("[d]{1,}");
		// aaa...型
		ymd = ymd || xlsFormat.matches("[a]{3,}");
		// [h]型, 取消24小时限制
		ymd = ymd || StringUtils.containsIgnoreCase(xlsFormat, "[h]");
		
		// 月日m&d
		boolean md = StringUtils.containsIgnoreCase(xlsFormat, "m");
		md = md && StringUtils.containsIgnoreCase(xlsFormat, "d");
		md = md && !StringUtils.containsIgnoreCase(xlsFormat, "yy");
		// 时分秒
		boolean hms = StringUtils.containsIgnoreCase(xlsFormat, "h");
		hms = hms && StringUtils.containsIgnoreCase(xlsFormat, "m");
		hms = hms && StringUtils.containsIgnoreCase(xlsFormat, "s");
		// 时分
		boolean hm = StringUtils.containsIgnoreCase(xlsFormat, "h");
		hm = hm && StringUtils.containsIgnoreCase(xlsFormat, "m");
		hm = hm && !StringUtils.containsIgnoreCase(xlsFormat, "s");
		// 分秒
		boolean ms = StringUtils.containsIgnoreCase(xlsFormat, "m");
		ms = ms && StringUtils.containsIgnoreCase(xlsFormat, "s");
		ms = ms && !StringUtils.containsIgnoreCase(xlsFormat, "h");

		// 年月日时分秒
		boolean yMdHms1 = (ymd&&hms);
		yMdHms1 = yMdHms1 || (ymd&&hm);
		yMdHms1 = yMdHms1 || (ymd&&ms);

		boolean yMdHms2 = (ym&&hms);
		yMdHms2 = yMdHms2 || (ym&&hm);
		yMdHms2 = yMdHms2 || (ym&&ms);

		boolean yMdHms3 = (md&&hms);
		yMdHms3 = yMdHms3 || (md&&hm);
		yMdHms3 = yMdHms3 || (md&&ms);

		// y年...型
		if(yy){
			return yyyy;
		}
		// M月...型
		else if(ym){
			return yyyyMM;
		}
		// 年月日时分秒
		else if(yMdHms1 || yMdHms2 || yMdHms3){
			xlsFormat = yyyyMMddHHmmss;
		}
		// 年月日
		else if(ymd || md){
			xlsFormat = yyyyMMdd;
		}
		// 时分秒
		else if(hms || hm || ms){
			xlsFormat = HHmmss;
		}


		return xlsFormat;
	}

	/**资源访问权限**/
	public static enum AccessAuth{
		/**没有任务权限**/
		None(""),
		/**读权限**/
		Read("r"),
		/**写权限**/
		Write("w"),
		/**执行权限**/
		Excute("x"),
		/**读写权限**/
		ReadWrite("rw"),
		/**读写权限**/
		ReadWriteExcute("rwx");

		private String alias;

		AccessAuth(String alias){
			this.alias = alias;
		}

		public static AccessAuth valueOf(Integer index){
			if(index == null){
				return None;
			}
			switch (index) {
			case 0:
				return None;
			case 1:
				return Read;
			case 2:
				return Write;
			case 3:
				return Excute;
			case 4:
				return ReadWrite;
			case 5:
				return ReadWriteExcute;
			default:
				return None;
			}
		}

		public static AccessAuth valueOfName(String name){
			if(StringUtils.isBlank(name)){
				return None;
			}
			else if(Read.alias.equalsIgnoreCase(name)){
				return Read;
			}
			else if(Write.alias.equalsIgnoreCase(name)){
				return Write;
			}
			else if(Excute.alias.equalsIgnoreCase(name)){
				return Excute;
			}
			else if(ReadWrite.alias.equalsIgnoreCase(name)){
				return ReadWrite;
			}
			else if(ReadWriteExcute.alias.equalsIgnoreCase(name)){
				return ReadWriteExcute;
			}
			else{
				return None;
			}
		}

		public int index(){
			return this.ordinal();
		}

		/**
		 * 获取别名
		 *
		 * @author PengYang
		 * @date 2016-09-04
		 *
		 * @param type
		 * @return String
		 */
		public String alias(){
			return this.alias;
		}
	}

	/**hdfs获取文件类型**/
	public static enum FileType{
		/**只获取文件**/
		File,
		/**只获取目录**/
		Directory,
		/**全都获取**/
		All;
		public static FileType indexOf(Integer index){
			if(index == null){
				return All;
			}
			switch (index) {
			case 1:
				return File;
			case 2:
				return Directory;
			case 3:
				return All;
			default:
				return All;
			}
		}

		public int index(){
			return this.ordinal()+1;
		}
	}
	/**
	 * sqoop连接器类型
	 * @author PengYang
	 * @date 2017-06-11
	 *
	 * @copyright Copyright Lenovo Corporation 2017 All Rights Reserved.
	 */
	public static enum SqoopConnectorType{

		GenericJdbcConnector("generic-jdbc-connector"),
		HdfsConnector("hdfs-connector");

		private String alias;

		SqoopConnectorType(String alias){
			this.alias = alias;
		}

		public String alias() {
			return this.alias;
		}

	}
	/**
	 * 客户端状态
	 * @author PengYang
	 * @date 2017-06-28
	 *
	 * @copyright Copyright Lenovo Corporation 2017 All Rights Reserved.
	 */
	public static enum ClientStatus{
		/**客户端程序离线(未启动)**/
		Offline,
		/**客户端程序在线(已启动)**/
		Online,
		/**客户端程序--空闲(没有任务在执行)**/
		Idle,
		/**客户端程序--忙碌(有任务在执行)**/
		Busy;
		public static ClientStatus indexOf(Integer index){
			if(index == null){
				return Offline;
			}
			switch (index) {
			case 0:
				return Offline;
			case 1:
				return Online;
			case 2:
				return Idle;
			case 3:
				return Busy;
			default:
				return Online;
			}
		}
		public int index(){
			return this.ordinal();
		}
	}
	
	public static enum DBSrcType{
		/**表**/
		TABLE,
		/**视图**/
		VIEW,
		/**SQL语句**/
		SQL;
		
		public static DBSrcType indexOf(int index){
			switch (index) {
			case 1:
				return TABLE;
			case 2:
				return VIEW;
			case 3:
				return SQL;

			default:
				break;
			}
			return TABLE;
		}
		
		public int index(){
			return this.ordinal()+1;
		}
	}
	/**
	 * 源文件处理方式
	 *
	 * @copyright Copyright Lenovo Corporation 2017 All Rights Reserved.
	 */
	public static enum SrcFileHandleType{
		
		None,
		Rename,
		Delete,
		Move;
		
		private String format;
		
		SrcFileHandleType(){}
		
		public int index(){
			return this.ordinal()+1;
		}
		
		public static SrcFileHandleType indexOf(int index){
			switch (index) {
			case 1:
				return None;
			case 2:
				return Rename;
			case 3:
				return Delete;
			case 4:
				return Move;

			default:
				break;
			}
			return None;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}
	}
	/**
	 * 目标文件重名处理方式
	 *
	 * @copyright Copyright Lenovo Corporation 2017 All Rights Reserved.
	 */
	public static enum TarFileHandleType{
		
		Overwrite,
		Append,
		Skip,
		Rename;
		
		private String format;
		
		public static TarFileHandleType indexOf(int index){
			switch (index) {
			case 1:
				return Overwrite;
			case 2:
				return Append;
			case 3:
				return Skip;
			case 4:
				return Rename;

			default:
				break;
			}
			return Overwrite;
		}
		
		public int index(){
			return this.ordinal()+1;
		}
		
		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}
	}	
	
	/***
	 * 文件名重命名类型
	 *
	 * @copyright Copyright Lenovo Corporation 2017 All Rights Reserved.
	 */
	public static enum RenameType{
		
		None(""),
		Number("_1"),
		Timestamp("_yyyyMMdd_HHmmss");
		
		private String format;
		
		RenameType (){
		}
		RenameType (String format){
			this.format = format;
		}
		
		public static RenameType indexOf(int index){
			switch (index) {
			case 1:
				return None;
			case 2:
				return Number;
			case 3:
				return Timestamp;
			default:
				break;
			}
			return None;
		}
		
		public int index(){
			return this.ordinal()+1;
		}
		
		public String getFormat() {
			return format;
		}
		public void setFormat(String format) {
			this.format = format;
		}
	}
	
	/**FTP协议类型**/
	public static enum FTPProtocolType{

		/**简单文件传输协议**/
		FTP,
		/**SSH文件传输协议**/
		SFTP;

		public static FTPProtocolType indexOf(int index){
			switch (index) {
			case 1:
				return FTP;
			case 2:
				return SFTP;
			}
			return FTP;
		}
		
		public int index(){
			return this.ordinal()+1;
		}
	}
	
	/**系统内置功能函数**/
	public static enum SystemFunctions{
		/**系统当前年份(自定义格式)**/
		CURRENT_YEAR("current_year"),
		/**系统当前月份(自定义格式)**/
		CURRENT_MONTH("current_month"),
		/**系统当天日期(自定义格式)**/
		CURRENT_DATE("current_date"),
		/**系统当天时间0点0分0秒(自定义格式)**/
		CURRENT_TIME0("current_time0"),
		/**系统当天时间23点59分59秒(自定义格式)**/
		CURRENT_TIME23("current_time23"),
		/**DB系统当前小时0分0秒0毫秒(自定义格式)**/
		DB_SYSTEM_HH_0("db_system_hh_0"),
		/**DB系统当前小时59分59秒999毫秒(自定义格式)**/
		DB_SYSTEM_HH_59("db_system_hh_59"),
		/**DB系统当前时间(自定义格式)**/
		DB_SYSTEM_TIME("db_system_time");
		
		private String name;
		private String format;
		
		SystemFunctions(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String getFormat() {
			return format;
		}
		public void setFormat(String format) {
			this.format = format;
		}
	}
	/**Hadoop认证类型**/
	public static enum HadoopAuthTypes{
		SIMPLE("simple"),
		KERBEROS("kerberos"),
		SENTRY("sentry");
		
		private JSONObject json;
		private String alias;
		
		HadoopAuthTypes(String alias){
			this.alias = alias;
			this.json = new JSONObject();
		}
		
		public static HadoopAuthTypes indexOf(int index){
			switch (index) {
			case 1:
				return SIMPLE;
			case 2:
				return KERBEROS;
			case 3:
				return SENTRY;
			default:
				return SIMPLE;
			}
		}
		
		public String alias() {
			return alias;
		}
		
		public int index(){
			return this.ordinal()+1;
		}
		
		@Override
		public String toString() {
			json.put("index", this.index());
			json.put("alias", this.alias);
			json.put("name", this.name());
			return json.toJSONString();
		}
		
	}
	
	/**Hive表存储格式类型**/
	public static enum HiveTableFormat {
		
		text("TextInputFormat"),
		
		orc("OrcInputFormat"),
		
		parquet("MapredParquetInputFormat"),
		
		rcfile("RCFileInputFormat"),
		
		sequencefile("SequenceFileInputFormat");
		
		private String alias;
		
		HiveTableFormat(String alias){
			this.alias = alias;
		}
		
		public static HiveTableFormat indexOf(Integer index) {
			if (index == null) {
				return text;
			}
			switch (index) {
			case 0:
				return text;
			case 1:
				return orc;
			case 2:
				return parquet;
			case 3:
				return rcfile;
			case 4:
				return sequencefile;
			default:
				return text;
			}
		}
		
		public int index() {
			return this.ordinal();
		}

		public String alias() {
			return alias;
		}

	}
	
	/**短信供应商类型**/
	public static enum SMSProviderType {
		Others,
		Aliyun;
		
		public static SMSProviderType indexOf(Integer index) {
			if (index == null) {
				return Others;
			}
			switch (index) {
			case 1:
				return Aliyun;
			default:
				return Others;
			}
		}
		
		public int index() {
			return this.ordinal();
		}
	}

	/**报警规则**/
	public static enum AlarmRules {
		//任务失败
		Fail,
		//任务超时
		TimeOut,
		//客户端离线
		OffLine;

		public static AlarmRules indexOf(Integer index) {
			if (index == null) {
				return Fail;
			}
			switch (index) {
				case 0:
					return Fail;
				case 1:
					return TimeOut;
				case 2:
					return OffLine;
				default:
					return Fail;
			}
		}

		public int index() {
			return this.ordinal();
		}
	}

	/**报警任务类型**/
	public static enum AlarmJobType {
		//迁移任务
		Job,
		//客户端
		Client;


		public static AlarmJobType indexOf(Integer index) {
			if (index == null) {
				return Job;
			}
			switch (index) {
				case 0:
					return Job;
				case 1:
					return Client;
				default:
					return Job;
			}
		}

		public int index() {
			return this.ordinal();
		}
	}

	/**客户端报警类型**/
	public static enum AlarmType {
		//客户端任务
		ClientJob,
		//客户端状态
		ClientStatus;


		public static AlarmType indexOf(Integer index) {
			if (index == null) {
				return ClientJob;
			}
			switch (index) {
				case 0:
					return ClientJob;
				case 1:
					return ClientStatus;
				default:
					return ClientJob;
			}
		}

		public int index() {
			return this.ordinal();
		}
	}

	/**报警规则状态**/
	public static enum AlarmRuleStatus {
		//禁用
		Disable,
		//已启用
		Enable;


		public static AlarmRuleStatus indexOf(Integer index) {
			if (index == null) {
				return Disable;
			}
			switch (index) {
				case 0:
					return Disable;
				case 1:
					return Enable;
				default:
					return Disable;
			}
		}

		public int index() {
			return this.ordinal();
		}
	}

	/**报警通知类型**/
	public static enum NotifyType {
		//短信
		SMS,
		//邮件
		Email;


		public static NotifyType indexOf(Integer index) {
			if (index == null) {
				return SMS;
			}
			switch (index) {
				case 0:
					return SMS;
				case 1:
					return Email;
				default:
					return SMS;
			}
		}

		public int index() {
			return this.ordinal();
		}
	}

}

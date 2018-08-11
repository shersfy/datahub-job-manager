package org.shersfy.datahub.jobmanager.constant;


/**
 * 常量
 *
 */
public class Const {
    private Const(){
    }
    
    public static final String BASE_PATH    = "base_path";
    // cookie key
    public static final String LOGIN_USER_KEY   = "login_user";
    public static final String I18N             = "i18n";
    
    /** 默认cron表达式 **/
    public static final String CRON_DEFAULT = "* * * * * ?";
    
    /**任务状态**/
    public static enum JobStatus{
        /**空状态**/
        Dummy,
        /**调度中, 正在执行**/
        Scheduling,
        /**调度结束, 已移除调度器**/
        Scheduled,
        /**正常等待状态**/
        Normal;

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
                return Normal;
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
        /**执行中。 一次任务正在执行**/
        Executing,
        /**成功。一次任务执行成功结束**/
        Successful,
        /**失败。一次任务执行异常结束**/
        Failed;

        public static JobLogStatus valueOf(Integer index){
            if(index == null){
                return Executing;
            }
            switch (index) {
            case 1:
                return Successful;
            case 2:
                return Failed;
            default:
                return Executing;
            }
        }

        public int index(){
            return this.ordinal();
        }
    }
    
    /**任务类型或数据源类型**/
    public static enum JobType{
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

        JobType(String alias){
            this.alias = alias;
        }

        public static JobType valueOf(Integer index){
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
        
        public static JobType valueOfAlias(String alias){
            if(alias == null){
                alias = "";
            }
            switch (alias.toLowerCase()) {
                case "local":
                    return LocalUpload;
                
                case "db":
                    return DatabaseMove;
                
                case "client":
                    return ClientUpload;
                
                case "dump":
                    return DumpUpload;
                
                case "s3":
                    return S3Upload;
                    
                case "alirds":
                    return AliyunRDS;
                
                case "awsrds":
                    return AmazonRDS;
                
                case "hdfs":
                    return HDFS;
                
                case "hive":
                    return Hive;
                
                case "csv":
                    return CsvUpload;
                
                case "excel":
                    return ExcelUpload;
                
                case "ftp":
                    return FtpUpload;
                
                case "spark":
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
         * @param type
         * @return String
         */
        public String alias(){
            return this.alias;
        }
    }
    
    /**任务运行周期类型**/
    public static enum JobPeriodType{
        /**周期类型-立刻执行**/
        PeriodOnceImmed,
        /**周期类型-定时**/
        PeriodOnceOntime,
        /**周期类型-周期性**/
        PeriodCircle;

        public static JobPeriodType valueOf(Integer index){
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
    
}

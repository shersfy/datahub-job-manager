package org.shersfy.datahub.jobmanager.job;

import org.quartz.JobExecutionContext;
import org.shersfy.datahub.commons.exception.DatahubException;
import org.shersfy.datahub.jobmanager.constant.Const.JobType;
import org.shersfy.datahub.jobmanager.model.JobInfo;

/**
 * 调用其它服务组件API, 下发任务配置并执行任务
 * 
 * 2018年8月11日
 */
public class DispatcherJob extends BaseJob{

    @Override
    public void dispatch(JobExecutionContext context) throws DatahubException {
        JobInfo job = getJob();
        String config = job.getConfig();
        // 调用服务分发任务
        LOGGER.info(config);
        JobType type = JobType.valueOf(job.getJobType());
        switch (type) {
            case LocalUpload:
                break;
            case DatabaseMove:
            case AliyunRDS:
            case AmazonRDS:
                break;
            case ClientUpload:
                break;
            case DumpUpload:
                break;
            case S3Upload:
                break;
            case FtpUpload:
                break;
            case CsvUpload:
                break;
            case ExcelUpload:
                break;
            case HDFS:
                break;
            case Hive:
                break;
            default:
                break;
        }
        
    }
    
}

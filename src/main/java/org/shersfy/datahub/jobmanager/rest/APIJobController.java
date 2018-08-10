package com.lenovo.datahub.web.api;

import java.io.File;
import java.sql.Types;
import java.util.*;
import java.util.function.Predicate;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.lenovo.datahub.domain.vo.*;
import com.lenovo.datahub.job.param.*;
import com.lenovo.datahub.meta.ColumnMeta;
import com.lenovo.datahub.meta.DataFileMeta;
import com.lenovo.datahub.meta.FtpFileMeta;
import com.lenovo.datahub.meta.S3ObjectMeta;
import com.lenovo.datahub.meta.TableMeta;
import com.lenovo.datahub.meta.TableType;
import com.lenovo.datahub.service.CsvInfoService;
import com.lenovo.datahub.service.DbInfoService;
import com.lenovo.datahub.web.api.form.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.datahub.bean.Result;
import com.lenovo.datahub.bean.ResultMsg;
import com.lenovo.datahub.client.conf.Partition;
import com.lenovo.datahub.common.Const;
import com.lenovo.datahub.common.Const.AppType;
import com.lenovo.datahub.common.Const.DBSrcType;
import com.lenovo.datahub.common.Const.JobGroup;
import com.lenovo.datahub.domain.JobInfo;
import com.lenovo.datahub.excel.SheetMeta;
import com.lenovo.datahub.hadoop.HiveUtil;
import com.lenovo.datahub.service.ExcelInfoService;
import com.lenovo.datahub.service.FileInfoSerivce;
import com.lenovo.datahub.service.FtpInfoService;
import com.lenovo.datahub.service.HdfsInfoService;
import com.lenovo.datahub.service.JobInfoService;
import com.lenovo.datahub.service.S3InfoService;
import com.lenovo.datahub.utils.DateUtil;
import com.lenovo.datahub.web.api.vo.JobInfoAPI;
import com.lenovo.datahub.web.controller.DumpController;
import com.lenovo.datahub.web.controller.JobController;
import com.lenovo.datahub.web.controller.form.JobInfoForm;

/**
 * 任务管理
 */
@Controller
@RequestMapping("/api/job")
public class APIJobController extends APIController {
	
	@Resource
	private JobInfoService jobInfoService;
	@Resource
	private ExcelInfoService excelInfoService;
	@Resource
	private CsvInfoService csvInfoService;
	@Resource
	private FtpInfoService ftpInfoService;
	@Resource
	private S3InfoService s3InfoService;
    @Resource
    private FileInfoSerivce fileInfoSerivce;
    @Resource
    private HdfsInfoService hdfsInfoService;
    @Resource
	private DbInfoService dbInfoService;
    @Resource
    private JobController jobController;
    @Resource
    private DumpController dumpController;

	/**任务--查看任务详情*/
	@RequestMapping(value="/v1/detail", method=RequestMethod.GET)
	@ResponseBody
	public Result getJobDetail(@Valid APIBaseDetailForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		Long id = Long.parseLong(form.getId());
		res = checkNullAndPermission(jobInfoService, id, form.getTokenUser());
		if(res.getCode() != SUCESS){
			return res;
		}
		
		Map<Long, ProjectVo> map = new HashMap<>();
		map.put(form.getTokenUser().getPid(), form.getTokenUser().getProject());
		
		JobInfo po   = (JobInfo) res.getModel();
		JobInfoVo vo = jobInfoService.getDetail(id, map, getI18n());
		JobInfoAPI detail = jobInfoService.poToAPI(po, vo, form.getTokenUser());
		
		res.setModel(detail);
		
		return res;
	}
	
	/**任务--查看任务详情*/
	@RequestMapping(value="/v1/list", method=RequestMethod.GET)
	@ResponseBody
	public Result getJobList(@Valid APIJobListForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		JobInfo where = new JobInfo();
		where.setJobName(form.getLikeName());
		where.setPid(form.getTokenUser().getPid());
		if(form.getFromType()!=null){
			where.setGroupNo(Integer.parseInt(form.getFromType()));
		}
		
		Page<JobInfo> page = new Page<>();
		page.setCurrentPage(form.getPageNo());
		page.setPageSize(form.getPageSize());
		
		List<JobInfo> data = jobInfoService.findPage(where, page).getData();
		res.setModel(data);
		
		return res;
	}
	
	/**任务管理--保存数据库迁移任务*/
	@RequestMapping(value="/v1/db2hdfs/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitDb2HdfsJob(@Valid APIJobDb2HdfsForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		// id!=null更新(验证权限)
		if(StringUtils.isNotBlank(form.getId())){
			Long id = Long.parseLong(form.getId());
			res = checkNullAndPermission(jobInfoService, id, form.getTokenUser());
			if(res.getCode() != SUCESS){
				return res;
			}
		}
		
		// 参数tables为空
		if(form.getFromTables().length==0){
			String proxyUser = form.getTokenUser().getProject().getProxyUser();
			TableType[] types= new TableType[]{TableType.TABLE};
			String catalog   = form.getFromCatalog();
			String schema    = form.getFromSchema();
			Long dbId        = Long.valueOf(form.getFromId());
			res = dbInfoService.getTables(dbId, catalog, schema, types, proxyUser);
			if(res.getCode() != SUCESS){
				return super.formatMsg(res);
			}
			@SuppressWarnings("unchecked")
			List<TableMeta> list = (List<TableMeta>) res.getModel();
			TableMeta[] tables   = list.toArray(new TableMeta[list.size()]);
			
			form.initFromJson(tables);
			form.initToHdfsJson(tables);
		}
		
		if(form.getFromTables().length==0){
			res.setCode(FAIL);
			res.setI18nMsg(new ResultMsg(MSGT0001E000005, "fromTables"));
			return super.formatMsg(res);
		}

		String fromJson   = form.getFromJson();
		String toHdfsJson = form.getToHdfsJson();
		String toDbJson   = "[]";
		String toHiveJson = "[]";
		int mtype = DBSrcType.TABLE.index();
		
		JobInfoForm job = this.initJobInfoForm(form);
		res = jobController.saveDbJob(job, bundle, fromJson, toDbJson, toHdfsJson, toHiveJson, mtype);
		return super.formatMsg(res);
	}
	
	/**任务管理--保存数据库迁移任务*/
	@RequestMapping(value="/v1/db2hive/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitDb2HiveJob(@Valid APIJobDb2HiveForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		// id!=null更新(验证权限)
		if(StringUtils.isNotBlank(form.getId())){
			Long id = Long.parseLong(form.getId());
			res = checkNullAndPermission(jobInfoService, id, form.getTokenUser());
			if(res.getCode() != SUCESS){
				return res;
			}
		}
		
		Long fromId = Long.valueOf(form.getFromId());
		Long toId   = Long.valueOf(form.getToId());
		
		String proxyUser = form.getTokenUser().getProject().getProxyUser();
		
		// 参数tables为空
		if(form.getFromTables().length==0){
			TableType[] types= new TableType[]{TableType.TABLE};
			String catalog   = form.getFromCatalog();
			String schema    = form.getFromSchema();
			res = dbInfoService.getTables(fromId, catalog, schema, types, proxyUser);
			if(res.getCode() != SUCESS){
				return super.formatMsg(res);
			}
			
			@SuppressWarnings("unchecked")
			List<TableMeta> list = (List<TableMeta>) res.getModel();
			TableMeta[] fromTbls = list.toArray(new TableMeta[list.size()]);
			TableMeta[] toTbls   = new TableMeta[fromTbls.length];
			
			// create table if table not exists 
			for(int i=0; i<fromTbls.length; i++){
				
				toTbls[i] = new TableMeta();
				toTbls[i].setCatalog(null);
				toTbls[i].setSchema(StringUtils.isNotBlank(fromTbls[i].getSchema())?fromTbls[i].getSchema():fromTbls[i].getCatalog());
				toTbls[i].setName(fromTbls[i].getName());
				if(dbInfoService.existByTable(toId, toTbls[i])){
					continue;
				}
				
				// generate to table ddl
				res = dbInfoService.getDDL(fromId, fromTbls[i], toId, toTbls[i]);
				if(res.getCode() != SUCESS){
					return super.formatMsg(res);
				}
				// create to table
				toTbls[i] = (TableMeta) res.getModel();
				res = dbInfoService.createTable(toId, toTbls[i].getDdl(), toTbls[i], getLoginUser().getUserName(), proxyUser);
				if(res.getCode() != SUCESS){
					return super.formatMsg(res);
				}
			}
			
			form.initFromJson(fromTbls);
			form.initToHiveJson(toTbls);
		}
		
		if(form.getFromTables().length==0){
			res.setCode(FAIL);
			res.setI18nMsg(new ResultMsg(MSGT0001E000005, "fromTables"));
			return super.formatMsg(res);
		}

		String fromJson   = form.getFromJson();
		String toHiveJson = form.getToHiveJson();
		String toHdfsJson = "[]";
		String toDbJson   = "[]";
		int mtype = DBSrcType.TABLE.index();
		
		JobInfoForm job = this.initJobInfoForm(form);
		res = jobController.saveDbJob(job, bundle, fromJson, toDbJson, toHdfsJson, toHiveJson, mtype);
		
		return super.formatMsg(res);
	}
	
	/**任务管理--提交执行自定义SQL导入hdfs任务*/
	@RequestMapping(value="/v1/sql2hdfs/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitSql2HdfsJob(@Valid APIJobSql2HdfsForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		// id!=null更新(验证权限)
		if(StringUtils.isNotBlank(form.getId())){
			Long id = Long.parseLong(form.getId());
			res = checkNullAndPermission(jobInfoService, id, form.getTokenUser());
			if(res.getCode() != SUCESS){
				return res;
			}
		}
		
		String fromJson   = form.getFromJson();
		String toHdfsJson = form.getToHdfsJson();
		String toDbJson   = "[]";
		String toHiveJson = "[]";
		int mtype = DBSrcType.SQL.index();
		
		JobInfoForm job = this.initJobInfoForm(form);
		res = jobController.saveDbJob(job, bundle, fromJson, toDbJson, toHdfsJson, toHiveJson, mtype);
		return super.formatMsg(res);
	}
	
	/**任务管理--提交执行自定义SQL导入hdfs任务*/
	@RequestMapping(value="/v1/sql2hive/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitSql2HiveJob(@Valid APIJobSql2HiveForm form, BindingResult bundle){

		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		// id!=null更新(验证权限)
		if(StringUtils.isNotBlank(form.getId())){
			Long id = Long.parseLong(form.getId());
			res = checkNullAndPermission(jobInfoService, id, form.getTokenUser());
			if(res.getCode() != SUCESS){
				return res;
			}
		}
		
		Long fromId = Long.valueOf(form.getFromId());
		Long toId   = Long.valueOf(form.getToId());
		
		String proxyUser = form.getTokenUser().getProject().getProxyUser();
		
		TableMeta toTable = new TableMeta(null, form.getToSchema(), form.getToTable());
		if(!dbInfoService.existByTable(toId, toTable)){
			// 1.获取建表DDL
			res = dbInfoService.getColumns(fromId, form.getFromSql());
			if(res.getCode() != SUCESS){
				return super.formatMsg(res);
			}
			@SuppressWarnings("unchecked")
			List<ColumnMeta> cols = (List<ColumnMeta>) res.getModel();
			
			res = dbInfoService.getDDLByCols(toId, toTable, cols);
			if(res.getCode() != SUCESS){
				return super.formatMsg(res);
			}
			
			// 2.创建表
			toTable = (TableMeta) res.getModel();
			res = dbInfoService.createTable(toId, toTable.getDdl(), toTable, getLoginUser().getUserName(), proxyUser);
			if(res.getCode() != SUCESS){
				return super.formatMsg(res);
			}
		}
		
		// 3.提交任务
		String fromJson   = form.getFromJson();
		String toHiveJson = form.getToHiveJson();
		String toHdfsJson = "[]";
		String toDbJson   = "[]";
		int mtype = DBSrcType.SQL.index();
		
		JobInfoForm job = this.initJobInfoForm(form);
		res = jobController.saveDbJob(job, bundle, fromJson, toDbJson, toHdfsJson, toHiveJson, mtype);
		
		return super.formatMsg(res);
	}
	
	/**任务管理--保存数据库迁移任务*/
	@RequestMapping(value="/v1/db2db/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitDb2DbJob(@Valid APIForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}

		// 1.id=null新建
		// 2.id!=null更新(验证权限)
		
		JobInfo where = new JobInfo();
		jobInfoService.saveJob(where);
		
		return res;
	}
	
	/**任务管理--提交HDFS迁移到HDFS任务*/
	@RequestMapping(value="/v1/hdfs2hdfs/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitHdfs2HdfsJob(@Valid APIForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		// 1.id=null新建
		// 2.id!=null更新(验证权限)
		
		JobInfo where = new JobInfo();
		jobInfoService.saveJob(where);
		
		return res;
	}
	/**任务管理--提交HDFS迁移到Hive任务*/
	@RequestMapping(value="/v1/hdfs2hive/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitHdfs2HiveJob(@Valid APIForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		// 1.id=null新建
		// 2.id!=null更新(验证权限)
		
		JobInfo where = new JobInfo();
		jobInfoService.saveJob(where);
		
		return res;
	}
	/**任务管理--保存本地任意文件迁移任务*/
	@RequestMapping(value="/v1/files/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitFilesJob1(@Valid APIJobFileForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}

        res = fileInfoSerivce.uploadFile("local",form.getFile());
		if(res.getCode() != SUCESS){
            return super.formatMsg(res);
        }

        // 添加本地上传任务
        LocalUploadParam params = new LocalUploadParam();
        params.setAppUser(form.getTokenUser().getProject().getProxyUser());

        DataFileMeta file = (DataFileMeta)res.getModel();

		List<String> fileNames = Arrays.asList(new String[]{file.getPath()});
        List<HdfsParams> toHdfsList = JSON.parseArray(form.getToHdfsJson(), HdfsParams.class);

        List<FileUploadParam> fromList = new ArrayList<>();
        for(String name :fileNames){
            File localFile = new File(name);
            FileUploadParam fp = new FileUploadParam();
            fp.setFilePath(localFile.getPath());
            fp.setName(localFile.getName());
            fp.setSize(localFile.length());
            fromList.add(fp);
        }
        
        Long toId = Long.parseLong(form.getToId());
        for(HdfsParams param: toHdfsList){
        	param.setHid(Long.valueOf(form.getToId()));
			res = hdfsInfoService.hasReadWrite(param.getHid(), FilenameUtils.getFullPath(param.getFileName()), params.getAppUser());
			if(res.getCode()!=SUCESS){
				return super.formatMsg(res);
			}
			param.setHid(toId);
		}
        
        params.setFromList(fromList);
        params.setToHdfsList(toHdfsList);

        JobInfo info = initJobInfo(form);
        info.setId(null);
        info.setOtherParams(params.toString());
        info.setGroupNo(JobGroup.LocalUpload.index());

        res = jobInfoService.saveJob(info);
        return super.formatMsg(res);
	}
	
	/**任务管理--保存批量本地任意文件迁移任务*/
	@RequestMapping(value="/v2/files/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitFilesJob2(@Valid APIJobFilesForm form, BindingResult bundle){

		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}

		List<FileUploadParam> fromList = new ArrayList<>();
		for(MultipartFile file :form.getFiles()){
			res = fileInfoSerivce.uploadFile("local", file);
			if(res.getCode() != SUCESS){
				return super.formatMsg(res);
			}

			DataFileMeta df = (DataFileMeta)res.getModel();
			File localFile = new File(df.getPath());
			FileUploadParam fp = new FileUploadParam();
			fp.setFilePath(localFile.getPath());
			fp.setName(localFile.getName());
			fp.setSize(localFile.length());
			fromList.add(fp);
		}

		// 添加本地上传任务
		LocalUploadParam params = new LocalUploadParam();
		params.setAppUser(form.getTokenUser().getProject().getProxyUser());

		List<HdfsParams> toHdfsList = JSON.parseArray(form.getToHdfsJson(), HdfsParams.class);
		Long toId = Long.parseLong(form.getToId());
		for(HdfsParams param: toHdfsList){
			param.setHid(Long.valueOf(form.getToId()));
			res = hdfsInfoService.hasReadWrite(param.getHid(), FilenameUtils.getFullPath(param.getFileName()), params.getAppUser());
			if(res.getCode()!=SUCESS){
				return super.formatMsg(res);
			}
			param.setHid(toId);
		}

		params.setFromList(fromList);
		params.setToHdfsList(toHdfsList);

		JobInfo info = initJobInfo(form);
		info.setId(null);
		info.setOtherParams(params.toString());
		info.setGroupNo(JobGroup.LocalUpload.index());

		res = jobInfoService.saveJob(info);
		return super.formatMsg(res);
	}

	/**任务管理--保存excel文件迁移任务*/
	@RequestMapping(value="/v1/csv/params/demo", method=RequestMethod.GET)
	@ResponseBody
	public Result getSubmitCsvJobDemo(@Valid APIForm form, BindingResult bundle){
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}

		JSONObject demo = new JSONObject();

		List<JSONObject> toHdfsList  = new ArrayList<>();
		List<JSONObject> toHiveList  = new ArrayList<>();

		JSONObject hdfsParams = new JSONObject();
		hdfsParams.put("fileName", "/hdfs/demo/test.csv");
		toHdfsList.add(hdfsParams);

		JSONObject hiveParams = new JSONObject();

		hiveParams.put("schema", "db_demo");
		hiveParams.put("tableName", "tb_test");

		List<Partition> parts = new ArrayList<Partition>();
		parts.add(new Partition("year", "2018"));
		parts.add(new Partition("month", "01"));
		parts.add(new Partition("date", "31"));
		for(Partition p :parts){
			p.setName(null);
		}

		hiveParams.put("partition", parts);
		toHiveList.add(hiveParams);

		demo.put("toHdfsJson", toHdfsList);
		demo.put("toHiveJson", toHiveList);

		res.setModel(demo);

		return res;
	}

	/**任务管理--保存csv文件迁移任务*/
	@RequestMapping(value="/v1/csv/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitCsvJob1(@Valid APIJobCsvForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}

		res = csvInfoService.uploadAndUnzip(form.getFile(), "csv", new String[] {"csv", "txt"});
		if(res.getCode() != SUCESS){
			return super.formatMsg(res);
		}

		CsvParams params = new CsvParams();
		params.setAppUser(form.getTokenUser().getProject().getProxyUser());
		List<CsvInfoVo> returnList = JSON.parseArray(JSON.toJSONString(res.getModel()), CsvInfoVo.class);
		List<CsvInfoVo> fromList  = new ArrayList<>();
		
		List<HdfsParams> toHdfsList  = JSON.parseArray(form.getToHdfsJson(), HdfsParams.class);
		List<HiveParams> toHiveList  = JSON.parseArray(form.getToHiveJson(), HiveParams.class);

		Long toId = Long.parseLong(form.getToId());
		for (CsvInfoVo vo:returnList) {
			CsvInfoVo newvo = new CsvInfoVo();
			newvo.setSrcPath(vo.getSrcPath());
			newvo.setFilePath(vo.getFilePath());
			newvo.setTableName(vo.getTableName());
			newvo.setSize(vo.getSize());
			newvo.setColumnSep(form.getColumnSep());
			newvo.setDataLineNo(form.getDataLineNo()==null?1L:Long.parseLong(form.getDataLineNo()));
			fromList.add(newvo);
		}
		
		for(HdfsParams param: toHdfsList){
			param.setHid(toId);
			res = hdfsInfoService.hasReadWrite(param.getHid(), FilenameUtils.getFullPath(param.getFileName()), params.getAppUser());
			if(res.getCode()!=SUCESS){
				return super.formatMsg(res);
			}
		}
		
		for(HiveParams param: toHiveList){
			param.setDbId(toId);
			param.setPartition(HiveUtil.getPartitionStr(param.getPartition()));
		}
		
		params.setFromList(fromList);
		params.setToHdfsList(toHdfsList);
		params.setToHiveList(toHiveList);

		JobInfo info = initJobInfo(form);
		info.setId(null);
		info.setOtherParams(params.toString());
		info.setGroupNo(JobGroup.CsvUpload.index());

		res = jobInfoService.saveJob(info);
		return super.formatMsg(res);
	}



	/**任务管理--保存批量csv文件迁移任务*/
	@RequestMapping(value="/v2/csv/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitCsvJob2(@Valid APIJobCsvForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		// 填充
		// 填充
	
		return super.formatMsg(res);
	}
	
	
	/**任务管理--保存excel文件迁移任务*/
	@RequestMapping(value="/v1/excel/params/demo", method=RequestMethod.GET)
	@ResponseBody
	public Result getSubmitExcelJobDemo(@Valid APIForm form, BindingResult bundle){
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		JSONObject demo = new JSONObject();
		
		List<JSONObject> toHdfsList  = new ArrayList<>();
		List<JSONObject> toHiveList  = new ArrayList<>();
		
		JSONObject hdfsParams = new JSONObject();
		hdfsParams.put("fileName", "/hdfs/demo/excel.xls");
		toHdfsList.add(hdfsParams);
		
		JSONObject mappings   = new JSONObject();
		JSONObject hiveParams = new JSONObject();
		
		hiveParams.put("schema", "db_demo");
		hiveParams.put("tableName", "tb_sheet");
		
		List<Partition> parts = new ArrayList<Partition>();
		parts.add(new Partition("year", "2018"));
		parts.add(new Partition("month", "01"));
		parts.add(new Partition("date", "31"));
		for(Partition p :parts){
			p.setName(null);
		}
		
		hiveParams.put("partition", parts);
		
		mappings.put("0", hiveParams);
		mappings.put("1", hiveParams.clone());
		
		toHiveList.add(mappings);
		
		demo.put("toHdfsJson", toHdfsList);
		demo.put("toHiveJson", toHiveList);
		
		res.setModel(demo);
		
		return res;
	}
	
	
	/**任务管理--保存excel文件迁移任务*/
	@RequestMapping(value="/v1/excel/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitExcelJob(@Valid APIJobExcelForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		res = excelInfoService.uploadAndUnzip(form.getFile(), "excel", new String[] {"xlsx", "xls"});
		if(res.getCode() != SUCESS){
			return super.formatMsg(res);
		}
		
		ExcelParams params = new ExcelParams();
		params.setAppUser(form.getTokenUser().getProject().getProxyUser());
		
		List<ExcelInfoVo> returnList = JSON.parseArray(JSON.toJSONString(res.getModel()), ExcelInfoVo.class);
		List<ExcelInfoVo> fromList   = new ArrayList<>();
		List<HdfsParams> toHdfsList  = JSON.parseArray(form.getToHdfsJson(), HdfsParams.class);
		List<JSONObject> toHiveList  = JSON.parseArray(form.getToHiveJson(), JSONObject.class);
		
		Long toId = Long.parseLong(form.getToId());
		for(ExcelInfoVo vo :returnList){
			ExcelInfoVo newvo = new ExcelInfoVo();
			newvo.setSrcPath(vo.getSrcPath());
			newvo.setFilePath(vo.getFilePath());
			newvo.setSize(vo.getSize());
			newvo.setTableName(vo.getTableName());
			newvo.setTotalRowSize(vo.getTotalRowSize());
			newvo.setSheets(vo.getSheets());
			fromList.add(newvo);
		}
		
		for(HdfsParams param :toHdfsList){
			param.setHid(toId);
			res = hdfsInfoService.hasReadWrite(param.getHid(), FilenameUtils.getFullPath(param.getFileName()), params.getAppUser());
			if(res.getCode()!=SUCESS){
				return super.formatMsg(res);
			}
			param.setHid(toId);
		}
		
		params.setFromList(fromList);
		params.setToHdfsList(toHdfsList);
		
		for(int i=0; i<toHiveList.size(); i++){
			final int index = i;
			fromList.get(i).getSheets().removeIf(new Predicate<SheetMeta>() {
				@Override
				public boolean test(SheetMeta t) {
					return !toHiveList.get(index).keySet().contains(String.valueOf(t.getIndex()));
				}
			});
			// sheetIndex--hiveParam
			Map<Integer, HiveParams> sheetMap = new HashMap<>();
			for(Object key :toHiveList.get(i).keySet()){
				String indexStr = String.valueOf(key);
				Integer sheetIndex 	= Integer.valueOf(indexStr);
				HiveParams param 	= JSON.parseObject(toHiveList.get(i).getString(indexStr), HiveParams.class);
				param.setDbId(toId);
				param.setPartition(HiveUtil.getPartitionStr(param.getPartition()));
				sheetMap.put(sheetIndex, param);
			}
			params.getToHiveList().add(sheetMap);
		}
		
		JobInfo info = initJobInfo(form);
		info.setId(null);
		info.setOtherParams(params.toString());
		info.setGroupNo(JobGroup.ExcelUpload.index());
		
		res = jobInfoService.saveJob(info);
		return super.formatMsg(res);
	}
	
	/**任务管理--提交insert SQL文件导入hdfs任务*/
	@RequestMapping(value="/v1/sqlfile2hdfs/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitSqlFile2HdfsJob(@Valid APIJobSqlFiles2HdfsForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		// 1. 上传并解析文件
		res = dumpController.upload(form.getFile(), form.getToColumnSep());
		if(res.getCode()!=SUCESS){
			return res;
		}
		
		@SuppressWarnings("unchecked")
		List<DumpInfoVo> txtFiles = (List<DumpInfoVo>) res.getModel();
		
		String fromJson 	= form.initFromJson(txtFiles);
		String toHdfsJson 	= form.initToHdfsJson(txtFiles);
		
		// 2. 提交上传任务
		DumpParams params = new DumpParams();
		params.setAppUser(form.getTokenUser().getProject().getProxyUser());
		
		List<DumpInfoVo> fromList	= JSON.parseArray(fromJson, DumpInfoVo.class);
		List<HdfsParams> toHdfsList = JSON.parseArray(toHdfsJson, HdfsParams.class);
		
		params.setFromList(fromList!=null ?fromList :params.getFromList());
		params.setToHdfsList(toHdfsList!=null ?toHdfsList :params.getToHdfsList());
		
		params.setFromType(AppType.Browser);
		params.setUser(getLoginUser());
		
//		params.setBak(isBak);
//		params.setBakDir(bakDir);
		params.setBak(false);
		params.setBakDir(null);
		
		JobInfo info = initJobInfo(form);
		info.setOtherParams(params.toString());
		info.setGroupNo(JobGroup.DumpUpload.index());
		res = jobInfoService.saveJob(info);
		
		return super.formatMsg(res);
	}
	
	/**任务管理--提交insert SQL文件导入hive任务<br/>
	 * 不支持导入到hive分区表(可以导入hdfs, 再导入Hive)<br/>
	 */
	@RequestMapping(value="/v1/sqlfile2hive/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitSqlFile2HiveJob(@Valid APIJobSqlFiles2HiveForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		String proxyUser = form.getTokenUser().getProject().getProxyUser();
		Long toId = Long.parseLong(form.getToId());
		
		// 1. 上传并解析文件
		res = dumpController.upload(form.getFile(), null);
		if(res.getCode()!=SUCESS){
			return res;
		}

		@SuppressWarnings("unchecked")
		List<DumpInfoVo> txtFiles = (List<DumpInfoVo>) res.getModel();
		String fromJson   = form.initFromJson(txtFiles);
		String toHiveJson = form.initToHiveJson(txtFiles);
		
		// 2. hive表不存在创建Hive表
		for(DumpInfoVo txt : txtFiles){
			TableMeta table = new TableMeta(null, form.getToSchema(), txt.getTableName());
			if(dbInfoService.existByTable(toId, table)){
				continue;
			}
			List<ColumnMeta> cols = txt.getHeader();
			if(cols.isEmpty()){
				cols = getDefaultColumns(Long.valueOf(txt.getCols()).intValue());
			}
			res = dbInfoService.getDDLByCols(toId, table, cols);
			if(res.getCode()!=SUCESS){
				return super.formatMsg(res);
			}
			table = (TableMeta) res.getModel();
			res = dbInfoService.createTable(toId, table.getDdl(), table, form.getTokenUser().getUserName(), proxyUser);
			if(res.getCode()!=SUCESS){
				return super.formatMsg(res);
			}
		}
		
		// 3. 提交上传任务
		DumpParams params = new DumpParams();
		params.setAppUser(proxyUser);
		List<DumpInfoVo> fromList	= JSON.parseArray(fromJson, DumpInfoVo.class);
		List<HiveParams> toHiveList = JSON.parseArray(toHiveJson, HiveParams.class);
		
		params.setFromList(fromList);
		params.setToHiveList(toHiveList);
		
		params.setFromType(AppType.Browser);
		params.setUser(getLoginUser());
		
//		params.setBak(isBak);
//		params.setBakDir(bakDir);
		params.setBak(false);
		params.setBakDir(null);
		
		JobInfo info = initJobInfo(form);
		info.setOtherParams(params.toString());
		info.setGroupNo(JobGroup.DumpUpload.index());
		
		res = jobInfoService.saveJob(info);
		
		return super.formatMsg(res);
	}
	
	/**任务管理--保存亚马逊S3数据对象迁移任务*/
	@RequestMapping(value="/v1/s3tohdfs/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitS3Job(@Valid APIJobS3ToHdfsForm form, BindingResult bundle){

		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		// id=null新建
		// id!=null更新(验证权限)
		if(StringUtils.isNotBlank(form.getId())){
			Long id = Long.parseLong(form.getId());
			res = checkNullAndPermission(jobInfoService, id, form.getTokenUser());
			if(res.getCode() != SUCESS){
				return res;
			}
		}

		Long fromId = Long.parseLong(form.getFromId());
		// 1. 获取源对象信息
		res = s3InfoService.listObjects(fromId, form.getFromBucketName(), form.getFromKeysPrefix());
		if(res.getCode() != SUCESS){
			return super.formatMsg(res);
		}

		@SuppressWarnings("unchecked")
		List<S3ObjectMeta> list = (List<S3ObjectMeta>) res.getModel();
		S3ObjectMeta[] objs = list.toArray(new S3ObjectMeta[list.size()]);
		
		form.initFromJson(objs);
		form.initToHdfsJson(objs);

		String fromJson 	= form.getFromJson();
		String toHdfsJson 	= form.getToHdfsJson();

		JobInfoForm job = this.initJobInfoForm(form);
		res = jobController.saveS3Job(job, bundle, fromJson, toHdfsJson);

		return super.formatMsg(res);
	}
	
	/**任务管理--提交FTP文件或目录导入HDFS任务*/
	@RequestMapping(value="/v1/ftp2hdfs/submit", method=RequestMethod.POST)
	@ResponseBody
	public Result submitFTPJob(@Valid APIJobFtp2HdfsForm form, BindingResult bundle){
		
		Result res = check(form, bundle);
		if(res.getCode() != SUCESS){
			return res;
		}
		
		// id=null新建
		// id!=null更新(验证权限)
		if(StringUtils.isNotBlank(form.getId())){
			Long id = Long.parseLong(form.getId());
			res = checkNullAndPermission(jobInfoService, id, form.getTokenUser());
			if(res.getCode() != SUCESS){
				return res;
			}
		}
		
		Long fromId = Long.parseLong(form.getFromId());
		// 1. 获取ftp源对象信息
		res = ftpInfoService.getFileInfo(fromId, form.getFromPath());
		if(res.getCode() != SUCESS){
			return super.formatMsg(res);
		}
		
		FtpFileMeta ftpFile = (FtpFileMeta) res.getModel();
		form.initFromJson(ftpFile);
		form.initToHdfsJson(ftpFile);
		
		String fromJson 	= form.getFromJson();
		String toHdfsJson 	= form.getToHdfsJson();
		
		JobInfoForm job = this.initJobInfoForm(form);
		res = jobController.saveFtpJob(job, bundle, fromJson, toHdfsJson);
		
		return super.formatMsg(res);
	}
	
	private JobInfo initJobInfo(APIJobInfoForm form){
		JobInfo info = new JobInfo();
		if(form.getId()!=null){
			info.setId(Long.parseLong(form.getId()));
		}
		info.setPid(form.getTokenUser().getPid());
		info.setJobName("API_"+form.getJobName());
		info.setNote(form.getJobName());

		//from
		if(form.getFromId()!=null){
			info.setFromId(Long.parseLong(form.getFromId()));
		}

		//to
		info.setToId(Long.parseLong(form.getToId()));
		info.setToType(Integer.parseInt(form.getToType()));

		info.setCronExpression(form.getCronExpression());
		info.setPeriodType(Integer.parseInt(form.getPeriodType()));
		info.setUserId(form.getTokenUser().getId());
		info.setUserName(form.getTokenUser().getUserName());

		return info;
	}
	
	private JobInfoForm initJobInfoForm(APIJobInfoForm form){
		JobInfoForm info = new JobInfoForm();
		if(StringUtils.isNotBlank(form.getId())){
			info.setId(Long.parseLong(form.getId()));
		}
		info.setPid(form.getTokenUser().getPid());
		info.setJobName("API_"+form.getJobName());
		info.setNote(form.getJobName());
		if(StringUtils.isNotBlank(form.getFromType())){
			info.setGroupNo(Integer.parseInt(form.getFromType()));
		}

		//from
		if(form.getFromId()!=null){
			info.setFromId(Long.parseLong(form.getFromId()));
		}

		//to
		info.setToId(Long.parseLong(form.getToId()));
		info.setToType(Integer.parseInt(form.getToType()));

		info.setCronExpression(form.getCronExpression());
		info.setPeriodType(Integer.parseInt(form.getPeriodType()));
		
		info.setStartTimeStr(DateUtil.format(form.getStartTime(), Const.FORMAT_DATE));
		info.setEndTimeStr(DateUtil.format(form.getEndTime(), Const.FORMAT_DATE));

		return info;
	}

	private List<ColumnMeta> getDefaultColumns(int cols){
		List<ColumnMeta> list = new ArrayList<ColumnMeta>();
		for(int i=1; i<=cols; i++){
			ColumnMeta cm = new ColumnMeta("field"+i);
			cm.setDataType(Types.LONGVARCHAR);
			cm.setTypeName("LONGVARCHAR");
			cm.setColumnSize(255);
			list.add(cm);
		}
		return list;
	}
	
}

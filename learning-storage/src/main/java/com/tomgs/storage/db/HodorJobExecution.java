package com.tomgs.storage.db;

import java.util.Date;
import lombok.Data;

/**
 * hodor_job_execution table entity
 *
 * @author tomgs
 * @since 2021/3/18
 */
@Data
public class HodorJobExecution {

    private String requestId;

    private String groupName;

    private String jobName;

    private String parameters;

    private String schedulerTag;

    private String clientHostname;

    private String clientIp;

    private Date startTime;

    private Date completeTime;

    /** @see org.draomara.hodor.model.executor.JobExecuteStatus */
    private Integer status;

    private String comments;

    private String result;

}

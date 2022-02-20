package com.zxj.demo.model;

import com.google.common.base.Preconditions;
import lombok.Data;

/**
 * @author snow-zen
 */
@Data
public class AsyncTaskResult {

    private final String taskId;
    private final int maxProcess;
    private final int curProcess;
    private final AsyncTaskState state;
    private final Object result;

    public AsyncTaskResult(AsyncTask task) {
        Preconditions.checkNotNull(task);

        this.taskId = task.getTaskId();
        this.maxProcess = task.getMaxProcess();
        this.curProcess = task.getCurProcess();
        this.result = task.getResult();
        this.state = task.getState();
    }

}

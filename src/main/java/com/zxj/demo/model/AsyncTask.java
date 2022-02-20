package com.zxj.demo.model;

/**
 * @author snow-zen
 */
public abstract class AsyncTask implements Runnable {

    private static final int DEFAULT_MAX_PROCESS = 100;

    private final String taskId;

    private int maxProcess = DEFAULT_MAX_PROCESS;
    private int curProcess;
    private Object result;
    private AsyncTaskState state = AsyncTaskState.CREATE;

    public AsyncTask(String taskId) {
        this.taskId = taskId;
    }

    public AsyncTask(String taskId, int maxProcess) {
        this.taskId = taskId;
        this.maxProcess = maxProcess;
    }

    protected void setCurProcess(int val) {
        if (val < 0.0 || val > maxProcess) {
            throw new IllegalArgumentException("process val error");
        }
        this.curProcess = val;
        System.out.println("处理中: " + val);

        if (val > 0.0 && state == AsyncTaskState.CREATE) {
            state = AsyncTaskState.PROCESS;
        }
    }

    protected void setResult(Object result) {
        this.result = result;
        this.state = AsyncTaskState.COMPLETION;
    }

    public String getTaskId() {
        return taskId;
    }

    public int getCurProcess() {
        return curProcess;
    }

    public int getMaxProcess() {
        return maxProcess;
    }

    public Object getResult() {
        return result;
    }

    public void fail() {
        this.state = AsyncTaskState.FAIL;
        this.curProcess = 0;
        this.result = null;
    }

    public AsyncTaskState getState() {
        return state;
    }
}

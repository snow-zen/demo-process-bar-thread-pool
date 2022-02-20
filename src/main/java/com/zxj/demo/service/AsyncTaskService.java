package com.zxj.demo.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zxj.demo.model.AsyncTask;
import com.zxj.demo.model.AsyncTaskResult;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author snow-zen
 */
@Service
public class AsyncTaskService implements DisposableBean {

    private final AsyncTaskPoolExecutor exec = new AsyncTaskPoolExecutor();

    public void execute(AsyncTask task) {
        exec.execute(task);
    }

    public AsyncTaskResult getStateResult(String taskId) {
        return exec.getTaskResult(taskId);
    }

    @Override
    public void destroy() {
        exec.shutdown();
    }

    private static class AsyncTaskPoolExecutor extends ThreadPoolExecutor {

        private final Map<String, AsyncTask> commitTaskMap = new ConcurrentHashMap<>();
        private final Cache<String, AsyncTaskResult> endTaskResultMap;

        public AsyncTaskPoolExecutor() {
            super(1, Runtime.getRuntime().availableProcessors() + 1,
                    5, TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>(256));
            endTaskResultMap = CacheBuilder.newBuilder()
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    .build();
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);

            if (r instanceof AsyncTask) {
                AsyncTask task = (AsyncTask) r;
                commitTaskMap.put(task.getTaskId(), task);
            }
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);

            if (r instanceof AsyncTask) {
                AsyncTask task = (AsyncTask) r;
                String taskId = task.getTaskId();

                if (t != null) {
                    task.fail();
                }

                commitTaskMap.remove(taskId);
                endTaskResultMap.put(taskId, new AsyncTaskResult(task));
            }
        }

        public AsyncTaskResult getTaskResult(String taskId) {
            AsyncTask task = commitTaskMap.get(taskId);

            if (task == null) {
                AsyncTaskResult result = endTaskResultMap.getIfPresent(taskId);

                if (result == null) {
                    throw new IllegalArgumentException("无效的 taskId");
                }
                return result;
            } else {
                return new AsyncTaskResult(task);
            }
        }

    }
}

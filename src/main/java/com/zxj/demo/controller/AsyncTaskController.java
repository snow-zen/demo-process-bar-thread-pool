package com.zxj.demo.controller;

import com.zxj.demo.model.AsyncTaskResult;
import com.zxj.demo.model.FibonacciTask;
import com.zxj.demo.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author snow-zen
 */
@RestController
@RequestMapping("/task")
public class AsyncTaskController {

    @Autowired
    private AsyncTaskService asyncTaskService;

    @PostMapping
    public String createTask() {
        String taskId = UUID.randomUUID().toString();
        asyncTaskService.execute(new FibonacciTask(taskId, 1000));
        return taskId;
    }

    @GetMapping("/{taskId}/state")
    public AsyncTaskResult getTaskState(@PathVariable String taskId) {
        return asyncTaskService.getStateResult(taskId);
    }
}

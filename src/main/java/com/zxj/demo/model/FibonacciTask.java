package com.zxj.demo.model;

import lombok.SneakyThrows;

/**
 * @author snow-zen
 */
public class FibonacciTask extends AsyncTask {

    private final int cnt;

    public FibonacciTask(String taskId, int cnt) {
        super(taskId, cnt);
        this.cnt = cnt;
    }

    @SneakyThrows
    @Override
    public void run() {
        if (cnt < 2) {
            setResult(cnt);
            return;
        }
//        TimeUnit.SECONDS.sleep(10);

        int a = 0, b = 0, r = 1;
        for (int i = 2; i <= cnt; ++i) {
            a = b;
            b = r;
            r = a + b;

//            TimeUnit.SECONDS.sleep(1);
            setCurProcess(i);
        }
        setResult(r);
    }

}

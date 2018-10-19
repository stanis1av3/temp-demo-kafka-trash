package com.example.demo.util.csv;

import com.example.demo.transport.StackOverFlowSurvey;
import com.example.demo.util.csv.CsvGenerator;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by user on 14.10.2018.
 */
public class CsvRunner {
    public static void main(String[] args) throws InterruptedException {

    }

    public BlockingQueue<StackOverFlowSurvey> getSurveyData() throws InterruptedException {

        Executor executor = new ConcurrentTaskExecutor();

        CsvGenerator generator = new CsvGenerator();
        executor.execute(generator);
        Thread.sleep(3000);
        return generator.getQueue();
    }
}

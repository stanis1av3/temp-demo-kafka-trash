package com.example.demo.util.csv;

import com.example.demo.transport.StackOverFlowSurvey;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by user on 14.10.2018.
 */
public class CsvGenerator implements Runnable {
    public static void main(String[] args) throws IllegalAccessException {

//        String fileName = "c:/java/survey_results_schema.csv";
//        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
//            stream.forEach(s-> {
//                String firstString = s.split(",")[0].substring(0,1);
//                String restString = s.split(",")[0].substring(1);
//                System.out.println("private String "+ firstString.toLowerCase()+restString+";");
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        Field f0 =  StackOverFlowSurvey.class.getDeclaredFields()[0];
//        StackOverFlowSurvey survey = new StackOverFlowSurvey();

//        f0.setAccessible(true);
//        f0.set(survey, "hello");
//        System.out.println(survey.getColumn());


    }

    BlockingQueue<StackOverFlowSurvey> queue;

    public BlockingQueue<StackOverFlowSurvey> getQueue() {
        return queue;
    }

    public CsvGenerator() {
        this.queue = new ArrayBlockingQueue<StackOverFlowSurvey>(200);
    }

    public void gatherOverflowData() {
        String dataFileName = "c:/java/survey_results_public.csv";

        List<StackOverFlowSurvey> surveyList = new ArrayList<>();


        try (Stream<String> stream = Files.lines(Paths.get(dataFileName))) {
            AtomicInteger i = new AtomicInteger();
            AtomicInteger counter = new AtomicInteger();
            stream.forEach(s -> {
                counter.incrementAndGet();
                StackOverFlowSurvey survey = new StackOverFlowSurvey();
                Stream.of(StackOverFlowSurvey.class.getDeclaredFields()).forEach(f ->
                        {

                            f.setAccessible(true);

                            try {
                                f.set(survey, s.split(",")[i.get() - 1]);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (ArrayIndexOutOfBoundsException aioue) {
                            }
                            i.incrementAndGet();

                        }
                );
                surveyList.add(survey);

                try {
                    queue.put(survey);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(counter.get()==200){

                    System.out.println("added record: " + counter);
                    System.out.println("Waiting for 10 seconds to continue!!");
                    try {
                        Thread.sleep(10000);
                        counter.set(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                i.set(0);


            });

            System.out.println(surveyList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.gatherOverflowData();
    }
}

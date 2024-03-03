package com.example.springbatchdbtofile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBatchDbToFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchDbToFileApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(){

        return  new CommandLineRunner() {
            @Autowired
            Job job;
            @Autowired
            JobLauncher jobLauncher;
            @Override
            public void run(String... args) throws Exception {

                var jobParameters = new JobParametersBuilder()
                        .addString("time", String.valueOf(System.currentTimeMillis()))
                        .toJobParameters();

                jobLauncher.run(job,jobParameters);
            }
        };
    }

}

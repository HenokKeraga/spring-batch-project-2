package com.example.springbatchdbtofile.config;

import com.example.springbatchdbtofile.model.Student;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    final JobRepository jobRepository;
    final PlatformTransactionManager platformTransactionManager;
    final DataSource dataSource;

    @Bean
    public Job job(Step step) {

        return new JobBuilder("job", jobRepository)
                .start(step)
                .build();

    }

    @Bean
    public Step step(ItemReader<Student> itemReader, ItemWriter<Student> itemWriter) {

        return new StepBuilder("step", jobRepository)
                .<Student, Student>chunk(3, platformTransactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemReader<Student> itemReader() {

        return new JdbcCursorItemReaderBuilder<Student>()
                .name("itemReader")
                .dataSource(dataSource)
                .sql("SELECT id ,name ,department,age FROM student")
                .beanRowMapper(Student.class)
                .build();
    }

    @Bean
    public ItemWriter<Student> itemWriter() {
        return new FlatFileItemWriterBuilder<Student>()
                .name("itemWriter")
                .resource(new FileSystemResource("stu.csv"))
                .lineSeparator("\r\n")
                .delimited().delimiter(",")
                .names("Id","Name","Department","Age")
                .build();
    }


}

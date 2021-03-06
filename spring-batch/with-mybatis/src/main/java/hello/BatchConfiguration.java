package hello;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // ページ単位での取得
    @Bean
    public MyBatisPagingItemReader<Person> reader(SqlSessionFactory sqlSessionFactory) {
        MyBatisPagingItemReader<Person> reader = new MyBatisPagingItemReader<>();
        reader.setSqlSessionFactory(sqlSessionFactory);

        // firstNameで検索。
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("firstName", "Jill");
        reader.setParameterValues(parameter);
        reader.setQueryId(PersonMapper.class.getName() + ".selectPage");

        // 全件取得。15件取得できるのでsetPageSizeの動作を確認したい時はこっち。
//        reader.setQueryId(PersonMapper.class.getName() + ".selectPageAll");

        reader.setPageSize(5);
        return reader;
    }

    // カーソルでの取得
//    @Bean
//    public MyBatisCursorItemReader<Person> reader(SqlSessionFactory sqlSessionFactory) {
//        MyBatisCursorItemReader<Person> reader = new MyBatisCursorItemReader<>();
//        reader.setSqlSessionFactory(sqlSessionFactory);
//        reader.setQueryId(PersonMapper.class.getName() + ".selectAll");
//
//        return reader;
//    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public MyBatisBatchItemWriter<Person> writer(SqlSessionFactory sqlSessionFactory) {
        MyBatisBatchItemWriter<Person> writer = new MyBatisBatchItemWriter<>();
        writer.setSqlSessionFactory(sqlSessionFactory);
        writer.setStatementId(PersonMapper.class.getName() + ".insert");
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, SqlSessionFactory sqlSessionFactory) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1(sqlSessionFactory))
                .end()
                .build();
    }

    @Bean
    public Step step1(SqlSessionFactory sqlSessionFactory) {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(5)
                .reader(reader(sqlSessionFactory))
                .processor(processor())
                .writer(writer(sqlSessionFactory))
                .build();
    }
    // end::jobstep[]
}
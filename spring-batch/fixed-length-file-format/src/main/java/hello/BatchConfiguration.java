package hello;

import com.sun.tools.corba.se.idl.constExpr.Or;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public OrderMapper orderMapper;

    @Bean
    public FlatFileItemReader<Order> reader() {
        FlatFileItemReader<Order> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("sample-data.txt"));
        reader.setLineMapper(defaultLineMapper());
        return reader;
    }

    @Bean
    public FlatFileItemWriter<Order> writer() {
        FlatFileItemWriter<Order> writer = new FlatFileItemWriter<>();
        writer.setName("itemWriter");
        Path path = Paths.get("fixed-length-file-format/src/main/resources/output.txt");
        writer.setResource(new FileSystemResource(path.toString()));

        BeanWrapperFieldExtractor<Order> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"isin", "quantity", "price", "customer"});
        fieldExtractor.afterPropertiesSet();

        FormatterLineAggregator<Order> lineAggregator = new FormatterLineAggregator<>();
        lineAggregator.setFormat("%12s%3s%5s%9s");
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);
        return writer;
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Order, Order>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public FixedLengthTokenizer fixedLengthTokenizer() {
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();

        String[] names = {"isin", "quantity", "price", "customer"};
        tokenizer.setNames(names);

        Range[] ranges = {new Range(1, 12), new Range(13, 15), new Range(16, 20), new Range(21, 29)};
        tokenizer.setColumns(ranges);

        return tokenizer;
    }

    @Bean
    public DefaultLineMapper<Order> defaultLineMapper() {
        DefaultLineMapper<Order> orderDefaultLineMapper = new DefaultLineMapper<>();
        orderDefaultLineMapper.setFieldSetMapper(orderMapper);
        orderDefaultLineMapper.setLineTokenizer(fixedLengthTokenizer());

        return orderDefaultLineMapper;
    }

}
package hello;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
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

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<FlatFile> reader() {
        FlatFileItemReader<FlatFile> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper((FlatFile::new));
        return reader;
    }

    @Bean
    public FlatFileItemWriter<FlatFile> writer() {
        FlatFileItemWriter<FlatFile> writer = new FlatFileItemWriter<>();
        writer.setName("itemWriter");
        // 最初ClassPathResourceでインスタンス作成してハマった。
        // 次にFileSystemResourceで"src/main/resources/output.txt"を渡してインスタンス作成したが、これもFileがありません。でエラー。
        // 次にFileSystemResourceで"file://src/main/resources/output.txt"のように接頭辞"file:"をつけてみたら、作成できたがroot直下に「file:」というディレクトリができてしまってそこに作成された。
        // 最後に今の形で最初にPathのインスタンスを作成して渡したら綺麗に動いた。理由はわからない。
        Path path = Paths.get("output-file/src/main/resources/output.txt");
        writer.setResource(new FileSystemResource(path.toString()));
        writer.setLineAggregator(FlatFile::getLine);
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
                .<FlatFile, FlatFile>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }
    // end::jobstep[]
}
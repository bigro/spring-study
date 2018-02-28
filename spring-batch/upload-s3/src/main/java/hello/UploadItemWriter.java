package hello;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class UploadItemWriter<Person> implements ItemWriter<Person> {

    @Autowired
    AmazonS3 amazonS3;

    @Value("${cloud.aws.bucket}")
    String bucketName;

    @Override
    public void write(List<? extends Person> items) throws Exception {
        String path = getClass().getClassLoader().getResource("sample-data.csv").getPath();
        File file = new File(path);

        try {
            amazonS3.putObject(bucketName, file.getName(), file);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }
}

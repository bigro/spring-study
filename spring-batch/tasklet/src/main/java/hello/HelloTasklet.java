package hello;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

public class HelloTasklet implements Tasklet, InitializingBean {

    private String hello;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        System.out.println(hello);
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(hello)) {
            throw new IllegalArgumentException();
        }
    }

    public HelloTasklet(String hello) {
        this.hello = hello;
    }
}

package cn.pcshao.graduaction.task.hadoop;

import org.apache.hadoop.mapreduce.Job;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author pcshao.cn
 * @date 2019-2-22
 */
public class HTaskTypeFactoryTest {

    /**
     * 测试本地MapReduce
     */
    @Test
    public void testWordCount() throws InterruptedException, IOException, ClassNotFoundException {
        Job facJob = HTaskTypeFactory.getFacJob(WordCount.Map.class, WordCount.Reduce.class,
                "E:\\Hado\\localtest\\input.txt", "E:\\Hado\\localtest\\out\\" + "test");
        facJob.waitForCompletion(true);
    }

}

package cn.pcshao.graduaction.task.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Hadoop任务处理调度对外API总线工厂
 *  需要和其他示例注入wordCount程序置于同一目录
 * @author pcshao.cn
 * @date 2019-02-21
 */
public class HTaskTypeFactory {

    static Logger logger = LoggerFactory.getLogger(HTaskTypeFactory.class);

    private Map<String, Object> hTaskTypes;

    static{
        //TODO 后期改造成扫描taskType到map集合
    }

    public Object getHTaskType(String name){
        return hTaskTypes.get(name);
    }

    //TEST
    public static void startWordCountHTask(String outputPath) {
        Configuration conf = new Configuration();

        Job job = null;
        try {
            job = Job.getInstance(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        job.setMapperClass(WordCount.Map.class);
        job.setReducerClass(WordCount.Reduce.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        try {
            FileInputFormat.setInputPaths(job, "E:\\Hado\\localtest\\input.txt");
        } catch (IOException e) {
//            logger.info("HTask输入路径错误");
            e.printStackTrace();
        }
        FileOutputFormat.setOutputPath(job, new Path("E:\\Hado\\localtest\\HTaskOUT\\"+ outputPath));

//        FileInputFormat.setInputPaths(job, "hdfs://hadoop0:9000/input");
//        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop0:9000/output"));

        try {
            job.waitForCompletion(true);
        } catch (IOException e) {
            logger.info("HTask 输出路径问题，多是已存在输出目录");
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.info("HTask InterruptedException");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.info("HTask执行类找不到");
            e.printStackTrace();
        }
    }

}

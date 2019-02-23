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

    public static Job getFacJob(Class mapper, Class reducer, Class mapOutputKeyClass, Class mapOutputKeyValueClass, Class outputKeyClass, Class outputValueClass, String inputPath, String outputPath) {
        Configuration conf = new Configuration();

        Job job = null;
        try {
            job = Job.getInstance(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        job.setMapperClass(mapper);
        job.setReducerClass(reducer);
        job.setMapOutputKeyClass(mapOutputKeyClass);
        job.setMapOutputValueClass(mapOutputKeyValueClass);
        job.setOutputKeyClass(outputKeyClass);
        job.setOutputValueClass(outputValueClass);

        try {
            FileInputFormat.setInputPaths(job, inputPath);
        } catch (IOException e) {
            logger.info("HTask输入路径错误");
            e.printStackTrace();
        }
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

//        FileInputFormat.setInputPaths(job, "hdfs://hadoop0:9000/input");
//        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop0:9000/output"));

        return job;
        /*try {
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
        }*/
    }

    public static Job getFacJob(Class mapper, Class reducer, String inputPath, String outputPath){
        return getFacJob(mapper, reducer, Text.class, IntWritable.class, Text.class, LongWritable.class, inputPath, outputPath);
    }

}

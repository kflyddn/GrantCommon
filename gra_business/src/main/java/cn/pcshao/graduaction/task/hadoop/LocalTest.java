package cn.pcshao.graduaction.task.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class LocalTest {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);

        job.setMapperClass(WordCount.Map.class);
        job.setReducerClass(WordCount.Reduce.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

//        FileInputFormat.setInputPaths(job, "E:\\Hado\\localtest\\input.txt");
//        FileOutputFormat.setOutputPath(job, new Path("E:\\Hado\\localtest\\out"));

        FileInputFormat.setInputPaths(job, "hdfs://hadoop0:9000/input");
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop0:9000/output"));

        job.waitForCompletion(true);
    }
}

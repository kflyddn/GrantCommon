package cn.pcshao.graduaction.task.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * @author pcshao.cn
 * @date 2019-01-25
 */
public class HelloWorld {

    public static void main(String[] args) {
        JobConf conf = new JobConf(HelloWorld.class);
        conf.setJobName("wordCount app 中文 test");

        //设置输出键值类型
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);
        //设置mapper和reducer的实现类
        conf.setMapperClass(Map.class);
        conf.setReducerClass(Reduce.class);
        //设置输入输出格式
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        //指定输入输出路径
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        try {
            JobClient.runJob(conf);
        } catch (IOException e) {
            e.printStackTrace();
       }
    }

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>{
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(LongWritable longWritable, Text text, OutputCollector<Text, IntWritable> outputCollector, Reporter reporter) throws IOException {
            String line = text.toString(); //读入Text
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()){
                word.set(tokenizer.nextToken());
                outputCollector.collect(word, one); //输出collector
            }
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>{
        @Override
        public void reduce(Text text, Iterator<IntWritable> iterator, OutputCollector<Text, IntWritable> outputCollector, Reporter reporter) throws IOException {
            int sum = 0;
            //遍历累加计数
            while (iterator.hasNext()){
                sum += iterator.next().get();
            }
            outputCollector.collect(text, new IntWritable(sum));
        }
    }
}

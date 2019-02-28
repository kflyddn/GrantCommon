package cn.pcshao.graduaction.task;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pcshao.cn
 * @date 2019/1/11
 */
@Component
public class HadoopTestMapper extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
        // 实例化作业对象，设置作业名称、Mapper和Reduce类
        Job job = new Job(getConf(), "Q1SumDeptSalary");
        job.setJobName("Q1SumDeptSalary");
        job.setJarByClass(HadoopTestMapper.class);
        job.setMapperClass(MapClass.class);
        job.setReducerClass(Reduce.class);
        // 设置输入格式类
        job.setInputFormatClass(TextInputFormat.class);
        // 设置输出格式
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        String[] otherArgs = new GenericOptionsParser(job.getConfiguration(), strings).getRemainingArgs();
        DistributedCache.addCacheFile(new Path(otherArgs[0]).toUri(),
                job.getConfiguration());
        FileInputFormat.addInputPath(job, new Path(otherArgs[1]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));
        job.waitForCompletion(true);
        return job.isSuccessful() ? 0 : 1;
    }

    public static class MapClass extends Mapper<LongWritable, Text, Text, Text> {
        // 用于缓存 dept文件中的数据
        private Map<String, String> deptMap = new HashMap<>();
        private String[] kv;

        // 此方法会在Map方法执行之前执行且执行一次
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            BufferedReader in = null;
            try {
                // 从当前作业中获取要缓存的文件
                Path[] paths = DistributedCache.getLocalCacheFiles(context.getConfiguration());
                String deptIdName = null;
                for (Path path : paths) {
                    // 对部门文件字段进行拆分并缓存到deptMap中
                    if (path.toString().contains("dept")) {
                        in = new BufferedReader(new FileReader(path.toString()));
                        while (null != (deptIdName = in.readLine())) {
                            // 对部门文件字段进行拆分并缓存到deptMap中
                            // 其中Map中key为部门编号，value为所在部门名称
                            deptMap.put(deptIdName.split(",")[0], deptIdName.split(",")[1]);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 对员工文件字段进行拆分
            kv = value.toString().split(",");
            // map join: 在map阶段过滤掉不需要的数据，输出key为部门名称和value为员工工资
            if (deptMap.containsKey(kv[7])) {
                if (null != kv[5] && !"".equals(kv[5].toString())) {
                    context.write(new Text(deptMap.get(kv[7].trim())), new
                            Text(kv[5].trim()));
                }
            }
        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, LongWritable> {
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException,
                InterruptedException {
            // 对同一部门的员工工资进行求和
            long sumSalary = 0;
            for (Text val : values) {
                sumSalary += Long.parseLong(val.toString());
            }
            // 输出key为部门名称和value为该部门员工工资总和
            context.write(key, new LongWritable(sumSalary));
        }
    }

    /**
     * 主方法，执行入口
     * @param args 输入参数 要处理的HDFS文件路径
     */
    public static void main(String[] args) throws Exception {
//        int res = ToolRunner.run(new Configuration(), new HadoopTestMapper(), args);
        int res = ToolRunner.run(new HadoopTestMapper(), args);
        System.exit(res);
    }

    //@Scheduled(cron = "0/1 * * * * *")
    public void testHadoop() throws Exception {
        String[] filePath = new String[3];
        filePath[0] = "D:\\PCSHAO\\IDEAworkspace\\GrantCommon\\gra_business\\src\\main\\resources\\hadoop\\dept";
        filePath[1] = "D:\\PCSHAO\\IDEAworkspace\\GrantCommon\\gra_business\\src\\main\\resources\\hadoop\\emp";
        filePath[2] = "D:\\PCSHAO\\IDEAworkspace\\GrantCommon\\gra_business\\src\\main\\resources\\hadoop\\out";
        int res = ToolRunner.run(new Configuration(), new HadoopTestMapper(), filePath);
        System.exit(res);
    }
}

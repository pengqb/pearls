package com.hundsun.hsccbp.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ArchivesDpMapReduce {
	private static Logger log = Logger.getLogger(ArchivesDpMapReduce.class);

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			JSONArray jArray = null;
			try {
				jArray = JSONArray.parseArray(value.toString());
			} catch (Exception e) {
				log.error("数据不是合法的json", e);
				return;
			}
			for(Object valueJson : jArray){
				String ep_id = ((JSONObject)valueJson).getString("partitionKey").split("_")[1];
				word.set(ep_id);
				context.write(word, one);
			}
		}
	}

	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}
	
	/**
	 * 测试参数
	 * args[0]: hdfs://localhost:19000/archivesDp
     * args[1]:hdfs://localhost:19000/archivesdpResult
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//String[] fileArray = { "d:/tools/hadoop/hadoop-2.7.3/archivesDp/10.0.0.5_archivesdp_20161208020437.txt", "d:/tools/hadoop/hadoop-2.7.3/test2.txt" };
		String[] fileArray = { "d:/tools/hadoop/hadoop-2.7.3/archivesDp/*.txt", "hdfs://localhost:19000/workCountOut1" };
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, fileArray).getRemainingArgs();
		if (otherArgs.length < 2) {
			System.err.println("Usage: EP_Count <in> [<in>...] <out>");
			System.exit(2);
		}
		Job job = Job.getInstance(conf, "EP Count");
		job.setJarByClass(ArchivesDpMapReduce.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		for (int i = 0; i < otherArgs.length - 1; ++i) {
			FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
		}
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}

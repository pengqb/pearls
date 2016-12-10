package com.hundsun.hsccbp.hadoop;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class ArchivesDpTopSort {
	static int topn = 5;// 取排名前5的设备

	// 每行记录是一个整数。将Text文本转换为IntWritable类型，作为map的key
	public static class Map extends Mapper<Object, Text, IntWritable, Text> {
		// 输出key 词频
		IntWritable outKey = new IntWritable();
		Text outValue = new Text();

		// 实现map函数
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				String element = itr.nextToken();
				if (Pattern.matches("\\d+", element)) {
					outKey.set(Integer.parseInt(element));
				} else {
					outValue.set(element);
				}
			}
			context.write(outKey, outValue);
		}
	}

	// reduce之前hadoop框架会进行shuffle和排序，因此直接输出key即可。
	public static class Reduce extends
			Reducer<IntWritable, Text, Text, IntWritable> {
		// 要获得前K个频率最高的词
		// 因为reduce前已经排序了，所以只需要取最后k个元素即可
		private static ArrayBlockingQueue<Line> queue = new ArrayBlockingQueue<>(topn);

		// 实现reduce函数
		public void reduce(IntWritable key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			for (Text value : values) {
				if (queue.size() >= topn) {
					queue.take();
				}
				queue.offer(new Line(value.toString(), key.get()));
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			Iterator<Line> it = queue.iterator();
			while (it.hasNext()) {
				Line line = it.next();
				context.write(new Text(line.getKey()),
						new IntWritable(line.getValue()));
			}
		}
	}

	/**
	 * args[0]: 5 
	 * args[1]:hdfs://localhost:19000/archivesdpResult
	 * args[2]:hdfs://localhost:19000/archivesdpSortResult
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String[] fileArray = {
				"hdfs://localhost:19000/workCountOut1/part-r-00000",
				"hdfs://localhost:19000/topkey" };
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, fileArray)
				.getRemainingArgs();
		conf.set("topKout", otherArgs[otherArgs.length - 1]);
		if (otherArgs.length < 2) {
			System.err.println("Usage: Data Sort <in> <out>");
			System.exit(2);
		}
		System.out.println(otherArgs[0]);
		System.out.println(otherArgs[1]);
		Job job = Job.getInstance(conf, "Data Sort");
		job.setJarByClass(ArchivesDpTopSort.class);

		// 设置Map和Reduce处理类
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		// 设置Map输出类型

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);

		// 设置输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		MultipleOutputs.addNamedOutput(job, "topKMOS", TextOutputFormat.class,
				Text.class, Text.class);
		// 设置输入和输出目录

		FileInputFormat.addInputPath(job, new Path(
				otherArgs[otherArgs.length - 2]));

		FileOutputFormat.setOutputPath(job, new Path(
				otherArgs[otherArgs.length - 1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

package com.vela.spark;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import scala.Tuple2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ArchivesDpSortLambda {

	public static class FlatMap implements FlatMapFunction<String, String> {
		@Override
		public Iterator<String> call(String value) throws Exception {
			JSONArray jArray = null;
			try {
				jArray = JSONArray.parseArray(value);
			} catch (Exception e) {
				// log.error("数据不是合法的json", e);
				 //return;
			}
//			List<String> list = new ArrayList<>();
//			jArray.forEach(l -> list.add(((JSONObject) l).getString(
//					"partitionKey").split("_")[1]));
			List<String> list = jArray.stream().map(l -> ((JSONObject)l).getString(
					"partitionKey").split("_")[1]).collect(Collectors.toList());
			return list.iterator();
		}
	}

	private static void writeTo(List<Tuple2<String, Integer>> output) {
		for (Tuple2<?, ?> tuple : output) {
			System.out.println(tuple._1 + "\t" + tuple._2);
		}
	}

	public static void main(String... args) {
		String file = "d:/tools/hadoop/hadoop-2.7.3/archivesDp/*.txt";
		// master is a Spark, Mesos or YARN cluster URL, or a special “local”
		// string to run in local mode.
		SparkConf conf = new SparkConf().setAppName("Simple Application")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(file).cache();
		// JavaRDD<String> words = lines.flatMap(line -> { return
		// Arrays.asList(line.split(" ")).iterator();});
		JavaRDD<String> words = lines.flatMap(new FlatMap());

		JavaPairRDD<String, Integer> ones = words
				.mapToPair(str -> new Tuple2<String, Integer>(str, 1));

		JavaPairRDD<String, Integer> counts = ones.reduceByKey((a, b) -> a + b);

		List<Tuple2<String, Integer>> output = counts.collect();
		List<Tuple2<String, Integer>> arrayList = new ArrayList<Tuple2<String, Integer>>(
				output);
		// sort statistics result by value
		arrayList.sort(Comparator.comparing(t -> t._2));
		writeTo(arrayList);
		System.exit(0);
	}
}

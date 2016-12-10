package com.vela.spark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ArchivesDpSort {

	public static class FlatMap implements FlatMapFunction<String, String> {
		@Override
		public Iterator<String> call(String value) throws Exception {
			JSONArray jArray = null;
			try {
				jArray = JSONArray.parseArray(value);
			} catch (Exception e) {
				// log.error("数据不是合法的json", e);
				// return;
			}
			List<String> list = new ArrayList<String>();
			for (Object valueJson : jArray) {
				String ep_id = ((JSONObject) valueJson).getString(
						"partitionKey").split("_")[1];
				list.add(ep_id);
			}
			return list.iterator();
		}
	}

	private static void writeTo(List<Tuple2<String, Integer>> output) {
		for (Tuple2<?, ?> tuple : output) {
			System.out.println(tuple._1 + "\t" + tuple._2);
		}
	}

	public static void main(String[] args) {
		String file = "d:/tools/hadoop/hadoop-2.7.3/archivesDp/*.txt";
		// master is a Spark, Mesos or YARN cluster URL, or a special “local”
		// string to run in local mode.
		SparkConf conf = new SparkConf().setAppName("Simple Application")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(file).cache();
		JavaRDD<String> words = lines.flatMap(new FlatMap());

		JavaPairRDD<String, Integer> ones = words
				.mapToPair(new PairFunction<String, String, Integer>() {
					@Override
					public Tuple2<String, Integer> call(String arg0)
							throws Exception {
						return new Tuple2<String, Integer>(arg0, 1);
					}
				});

		JavaPairRDD<String, Integer> counts = ones
				.reduceByKey(new Function2<Integer, Integer, Integer>() {
					@Override
					public Integer call(Integer arg0, Integer arg1)
							throws Exception {
						return arg0 + arg1;
					}
				});

		List<Tuple2<String, Integer>> output = counts.collect();
		List<Tuple2<String, Integer>> arrayList = new ArrayList<Tuple2<String, Integer>>(
				output);
		// sort statistics result by value
		Collections.sort(arrayList, new Comparator<Tuple2<String, Integer>>() {
			@Override
			public int compare(Tuple2<String, Integer> t1,
					Tuple2<String, Integer> t2) {
				if (t1._2 < t2._2) {
					return 1;
				} else if (t1._2 > t2._2) {
					return -1;
				}
				return 0;
			}
		});
		writeTo(arrayList);
		System.exit(0);
	}
}

package com.vela.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class QuickStart {

	public static void main(String[] args) {
		//定义本地文件
		//String file = "D:\\tools\\hadoop\\hadoop-2.7.3\\mysql.sql"; 
		
		//定义hdfs文件
		//String file = "hdfs://localhost:19000/workCount/mysql.sql";
		
		//support running on directories, compressed files, and wildcards as well. For example, 
		//you can use textFile("/my/directory"), textFile("/my/directory/*.txt"), and textFile("/my/directory/*.gz").
		//String file = "hdfs://localhost:19000/workCount/*";
		String file = "D:\\tools\\hadoop\\hadoop-2.7.3\\test.txt"; 
		//master is a Spark, Mesos or YARN cluster URL, or a special “local” string to run in local mode. 
	    SparkConf conf = new SparkConf().setAppName("Simple Application").setMaster("local");
	    JavaSparkContext sc = new JavaSparkContext(conf);
	    JavaRDD<String> data = sc.textFile(file).cache();
	    long numAs = data.filter(line -> line.contains("a")).count();
	    long numBs = data.filter(line -> line.contains("b")).count();
	    System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
	    sc.stop();
	    sc.close();
	}

}

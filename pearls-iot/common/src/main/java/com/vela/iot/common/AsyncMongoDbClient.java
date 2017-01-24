package com.vela.iot.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;

/**
 * Mongodb 客户端
 */
public class AsyncMongoDbClient {

	static Logger log = LoggerFactory.getLogger(AsyncMongoDbClient.class);
	public static MongoClient mongo = null;
	public static MongoDatabase database;
	static String MONGODB_COLLECTION_GW = "gw";

	static SingleResultCallback<Document> callbackPrintDocuments = new SingleResultCallback<Document>() {
		@Override
		public void onResult(final Document document, final Throwable t) {
			log.debug(document.toJson());
		}
	};

	static SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
		@Override
		public void onResult(final Void result, final Throwable t) {
			log.debug("Operation Finished!");
		}
	};

	static SingleResultCallback<BulkWriteResult> printBatchResult = new SingleResultCallback<BulkWriteResult>() {
		@Override
		public void onResult(final BulkWriteResult result, final Throwable t) {
			log.debug(result.toString());
		}
	};

	public static void insertData(Map<String, Object> map) {
		try {
			MongoCollection<Document> dbCollection = database
					.getCollection(MONGODB_COLLECTION_GW);
			Document doc = new Document(map);
			dbCollection.insertOne(doc, callbackWhenFinished);
		} catch (Exception e) {
			log.error("error:", e);
		}
	}

	public static void updateData() {
		try {
			MongoCollection<Document> dbCollection = database
					.getCollection(MONGODB_COLLECTION_GW);
			dbCollection
					.updateOne(Filters.eq("_id", new ObjectId(
							"588156495586fa222db504d6")), Updates.combine(
							Updates.set("method", "activeasync"),
							Updates.set("hashcode", 125777)),
							(result, t) -> log.debug(""
									+ result.getModifiedCount()));
		} catch (Exception e) {
			log.error("error:", e);
		}
	}

	public static void bulkUpdateData() {
		MongoCollection<Document> dbCollection = database
				.getCollection(MONGODB_COLLECTION_GW);
		dbCollection.bulkWrite(Arrays.asList(
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activeasyncbulk"),
						Updates.set("hashcode", 125777)))),
				new BulkWriteOptions().ordered(false), printBatchResult);
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "1");
		map.put("method", "active");
		AsyncMongoDbClient.insertData(map);
	}
}

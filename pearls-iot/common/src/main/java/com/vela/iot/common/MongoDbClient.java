package com.vela.iot.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;

/**
 * Mongodb 客户端
 */
public class MongoDbClient {

	static Logger log = LoggerFactory.getLogger(MongoDbClient.class);

	public static MongoClient mongo = null;
	public static MongoDatabase database;
	static String MONGODB_COLLECTION_GW = "gw";

	public static void insertData(Map<String, Object> map) {
		try {
			MongoCollection<Document> dbCollection = database
					.getCollection(MONGODB_COLLECTION_GW);
			Document doc = new Document(map);
			dbCollection.insertOne(doc);
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
							Updates.set("method", "activesync"),
							Updates.set("hashcode", 125777)));
		} catch (Exception e) {
			log.error("error:", e);
		}
	}

	public static void bulkUpdateData() {
		MongoCollection<Document> dbCollection = database
				.getCollection(MONGODB_COLLECTION_GW);
		BulkWriteResult result = dbCollection.bulkWrite(Arrays.asList(
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777))),
				new UpdateOneModel<>(Filters.eq("_id", new ObjectId(
						"588156495586fa222db504d6")), Updates.combine(
						Updates.set("method", "activesyncbulk"),
						Updates.set("hashcode", 125777)))),
				new BulkWriteOptions().ordered(false));
		log.debug(result.toString());
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "1");
		map.put("method", "active");
		MongoDbClient.insertData(map);
	}
}

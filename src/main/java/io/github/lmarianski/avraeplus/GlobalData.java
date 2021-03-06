package io.github.lmarianski.avraeplus;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.*;

public class GlobalData {

    private MongoCollection<Document> globalData;

    private Document statsFilter = new Document("type", "stats");
    private Document statsDoc;

    public static long DAY_MS = 86400000;

    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    public GlobalData() {
        globalData = Main.db.getCollection("globalData");

        statsDoc = findOrCreate(statsFilter);

        if (!statsDoc.containsKey("hitsThisMonth")) {
            statsDoc.append("hitsThisMonth", 0);
        }
        Main.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Date lastSwap = statsDoc.getDate("lastSwap");

                if (lastSwap == null || calendar.get(Calendar.MONTH) > lastSwap.getMonth()) {
                    int hits = statsDoc.getInteger("hitsThisMonth");
                    statsDoc.append("hitsLastMonth", hits);
                    statsDoc.append("hitsThisMonth", 0);
                    statsDoc.append("lastSwap", new Date(calendar.getTimeInMillis()));
                    update();
                }
            }
        }, 0, DAY_MS);
    }

    public Document findOrCreate(Document doc) {
        if (globalData.countDocuments(doc) == 0) {
            globalData.insertOne(doc);
            return doc;
        } else {
            return globalData.find(doc).first();
        }
    }

    public synchronized void update() {
//        globalData.insertMany(Arrays.asList(
//                statsDoc
//        ));
        globalData.findOneAndReplace(statsFilter, statsDoc);
    }

    public int getHitsThisMonth() {
        return statsDoc.getInteger("hitsThisMonth");
    }
    public int getHitsLastMonth() {
        return statsDoc.getInteger("hitsLastMonth");
    }

    public synchronized void incrementHits() {
        int hits = statsDoc.getInteger("hitsThisMonth");
        statsDoc.append("hitsThisMonth", hits+1);
        update();
    }

}

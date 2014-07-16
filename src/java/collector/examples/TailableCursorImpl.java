package collector.examples;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.bson.types.ObjectId;

import com.mongodb.*;

/**
 * Java + MongoDB Hello world Example
 *
 */
public class TailableCursorImpl {
    public static void main(String[] args) {

        try {

            //Oplog is already a capped collection, no need to create collection (or set to capped)

            /**** Connect to MongoDB ****/
            // Since 2.10.0, uses MongoClient
            MongoClient mongo = new MongoClient("localhost", 27017);
            /**** Get database ****/
            DB db = mongo.getDB("local");
            /**** Get collection / table from 'testdb' ****/
            DBCollection table = db.getCollection("oplog.rs");

            final AtomicBoolean readRunning = new AtomicBoolean(true);
            final AtomicBoolean writeRunning = new AtomicBoolean(true);

            final AtomicLong writeCounter = new AtomicLong(0);
            final AtomicLong readCounter = new AtomicLong(0);

            final ArrayList<Thread> writeThreads = new ArrayList<Thread>();
            final ArrayList<Thread> readThreads = new ArrayList<Thread>();

            for (int idx=0; idx < 10; idx++) {
                final Thread writeThread = new Thread(new Writer(mongo, writeRunning, writeCounter));
                final Thread readThread = new Thread(new Reader(mongo, readRunning, readCounter));
                writeThread.start();
                readThread.start();
                writeThreads.add(writeThread);
                readThreads.add(readThread);
            }

            for (final Thread readThread : readThreads) readThread.interrupt();
            for (final Thread writeThread : writeThreads) writeThread.interrupt();

            System.out.println("----- write count: " + writeCounter.get());
            System.out.println("----- read count: " + readCounter.get());


        /*    DBCursor cursor = table.find();
            System.out.println(cursor.getOptions());
            while (cursor.hasNext()) {
                cursor.next();

            }
            System.out.println(cursor.size());*/


            /**** Done ****/
            System.out.println("Done");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }

    }

    private static class Reader implements Runnable {
        @Override
        public void run() {
            final HashSet<ObjectId> seenIds = new HashSet<ObjectId>();
            long lastTimestamp = 0;

            while (_running.get()) {
                try {
                    _mongo.getDB("local").requestStart();
                    final DBCursor cur = createCursor(lastTimestamp);
                    try {
                        while (cur.hasNext() && _running.get()) {
                            final BasicDBObject doc = (BasicDBObject)cur.next();
                            final ObjectId docId = doc.getObjectId("_id");
                            lastTimestamp = doc.getLong("ts");
                            if (seenIds.contains(docId)) System.out.println("------ duplicate id found: " + docId);
                            seenIds.add(docId);
                            _counter.incrementAndGet();
                        }
                    } finally {
                        try { if (cur != null) cur.close(); } catch (final Throwable t) { /* nada */ }
                        _mongo.getDB("local").requestDone();
                    }

                    try { Thread.sleep(100); } catch (final InterruptedException ie) { break; }
                } catch (final Throwable t) { t.printStackTrace(); }
            }
        }

        private DBCursor createCursor(final long pLast) {
            final DBCollection col = _mongo.getDB("local").getCollection("oplog.rs");

            if (pLast == 0)
            { return col.find().sort(new BasicDBObject("$natural", 1)).addOption(Bytes.QUERYOPTION_TAILABLE).addOption(Bytes.QUERYOPTION_AWAITDATA); }

            final BasicDBObject query = new BasicDBObject("ts", new BasicDBObject("$gt", pLast));
            return col.find(query).sort(new BasicDBObject("$natural", 1)).addOption(Bytes.QUERYOPTION_TAILABLE).addOption(Bytes.QUERYOPTION_AWAITDATA);
        }

        private Reader(final Mongo pMongo, final AtomicBoolean pRunning, final AtomicLong pCounter)
        { _mongo = pMongo; _running = pRunning; _counter = pCounter; }

        private final Mongo _mongo;

        private final AtomicBoolean _running;
        private final AtomicLong _counter;
    }

    /**
     * The thread that is writing to the capped collection.
     */
    private static class Writer implements Runnable {
        @Override
        public void run() {
            while (_running.get()) {
                final ObjectId docId = ObjectId.get();
                final BasicDBObject doc = new BasicDBObject("_id", docId);
                final long count = _counter.incrementAndGet();
                doc.put("count", count);
                doc.put("ts", System.currentTimeMillis());
                _mongo.getDB("local").getCollection("oplog.rs").insert(doc);
            }
        }

        private Writer(final Mongo pMongo, final AtomicBoolean pRunning, final AtomicLong pCounter)
        { _mongo = pMongo; _running = pRunning; _counter = pCounter; }

        private final Mongo _mongo;
        private final AtomicBoolean _running;
        private final AtomicLong _counter;
    }
}
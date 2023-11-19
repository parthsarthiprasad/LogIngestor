package com.parthsarthiprasad.LogInjectorDyte.service.lucene;

import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import java.io.IOException;
import java.time.LocalDate;
import com.parthsarthiprasad.LogInjectorDyte.model.LogList;

public class LuceneService {


    public static void main(String[] args) throws IOException {

        Weld weld = new Weld();
        WeldContainer container = weld.initialize();

        Directory index = new ByteBuffersDirectory();
        LuceneIndexer indexer = container.select(LuceneIndexer.class).get();
        LuceneSearcher searcher = container.select(LuceneSearcher.class).get();

        indexer.index(index);

        System.out.println("\nFind all logs");
        System.out.println("----------------------------------------------");
        searcher.findAll(index).forEach(LuceneService::findLogList);

        System.out.println("\nFind shopping lists by parentResourceId");
        System.out.println("----------------------------------------------");
        searcher.findByLogParentResouceId(index,  "server-0987").forEach(LuceneService::findLogList);


        System.out.println("\nFind shopping lists by commit");
        System.out.println("----------------------------------------------");
        searcher.findByLogCommit(index,  "5e5342f").forEach(LuceneService::findLogList);

        System.out.println("\nFind shopping lists by spanId");
        System.out.println("----------------------------------------------");
        searcher.findByLogSpanId(index,  "span-456").forEach(LuceneService::findLogList);

        System.out.println("\nFind shopping lists by traceId");
        System.out.println("----------------------------------------------");
        searcher.findByLogTraceId(index,  "abc-xyz-123").forEach(LuceneService::findLogList);

        System.out.println("\nFind shopping lists by timestamp");
        System.out.println("----------------------------------------------");
        searcher.findByLogTimestamp(index, "2023-09-15T08:00:00Z").forEach(LuceneService::findLogList);

        System.out.println("\nFind shopping lists by resourceId");
        System.out.println("----------------------------------------------");
        searcher.findByLogResouceId(index, "server-1234").forEach(LuceneService::findLogList);

        System.out.println("\nFind shopping lists by message");
        System.out.println("----------------------------------------------");
        searcher.findByLogMessage(index, "Failed to connect to DB").forEach(LuceneService::findLogList);

        System.out.println("\nFind shopping lists by level");
        System.out.println("----------------------------------------------");
        searcher.findByLogLevel(index, "Milk").forEach(LuceneService::findLogList);

        System.out.println("\nFind logs in  date range");
        System.out.println("----------------------------------------------");
        searcher.findByDateRange(index, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 11, 22)).forEach(Application::findLogList);

        container.shutdown();
    }

    private static void findLogList(LogList logList) {

        System.out.println("level: " + logList.getLevel());
        System.out.println("message: " + logList.getMessage());
        System.out.println("resourceId: " + logList.getResourceId());
        System.out.println("timestamp: " + logList.getTimestamp());
        System.out.println("traceId: " + logList.getTraceId());
        System.out.println("spanId: " + logList.getSpanId());
        System.out.println("commit: " + logList.getCommit());
        System.out.println("parentResourceId: " + logList.getMetadata().getParentResourceId());
//        System.out.println("message: " + logList.getName());
//        System.out.println("date: " + logList.getDate().format(DateTimeFormatter.ISO_DATE));
//        System.out.println("metadata: " + logList.getMetadata().stream().collect(Collectors.joining(", ")));
    }

}

package com.parthsarthiprasad.tradewind.service.lucene;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parthsarthiprasad.tradewind.model.LogList;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Component that indexes JSON data with Apache Lucene.
 *
 * @author parthsarthiprasad
 */
@ApplicationScoped
public class LuceneIndexer {

    @Inject
    private ObjectMapper mapper;

    /**
     * Parse JSON documents and index content.
     *
     * @param index
     * @throws IOException
     */
    public void index(Directory index) throws IOException {


        for (int i = 0; i < 5; i++) {

            String fileName = "/data/shopping-list-" + (i + 1) + ".json";
            InputStream stream = this.getClass().getResourceAsStream(fileName);
//            ShoppingList shoppingList = mapper.readValue(stream, ShoppingList.class);
            LogList logentity = mapper.readValue(stream, LogList.class);

        }

    }

    public void indexLogList(Directory index) throws IOException {

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(index, config);

        for (int i = 0; i < 5; i++) {

            String fileName = "/data/shopping-list-" + (i + 1) + ".json";
            InputStream stream = this.getClass().getResourceAsStream(fileName);
//            ShoppingList shoppingList = mapper.readValue(stream, ShoppingList.class);
            LogList logentity = mapper.readValue(stream, LogList.class);

            Document document = toDocument(logentity);

            indexWriter.addDocument(document);
            indexWriter.commit();
        }

        indexWriter.close();
    }


    /**
     * Create a Lucene {@link Document} instance from a {@link LogList} instance.
     *
     * @param loglist
     * @return
     */

        /*
     {
	"level": "error",
	"message": "Failed to connect to DB",
    "resourceId": "server-1234",
	"timestamp": "2023-09-15T08:00:00Z",
	"traceId": "abc-xyz-123",
    "spanId": "span-456",
    "commit": "5e5342f",
    "metadata": {
        "parentResourceId": "server-0987"
    }
}
     */
    private Document toDocument(LogList logList) {

        Document document = new Document();
        document.add(new StringField(DocumentFields.LEVEL_FIELD, logList.getLevel(), Field.Store.YES));
        document.add(new StringField(DocumentFields.MESSAGE_FIELD, logList.getMessage(), Field.Store.YES));
        document.add(new StringField(DocumentFields.RESOURCE_ID_FIELD, logList.getResourceId(), Field.Store.YES));
        document.add(new StringField(DocumentFields.TIMESTAMP_FIELD, logList.getTimestamp(), Field.Store.YES));
        document.add(new StringField(DocumentFields.TRACE_ID_FIELD, logList.getTraceId(), Field.Store.YES));
        document.add(new StringField(DocumentFields.SPAN_ID_FIELD, logList.getSpanId(), Field.Store.YES));
        document.add(new StringField(DocumentFields.COMMIT_FIELD, logList.getCommit(), Field.Store.YES));
        document.add(new StringField(DocumentFields.SPAN_ID_FIELD, logList.getSpanId(), Field.Store.YES));
        document.add(new StringField(DocumentFields.PARENT_RESOURCE_ID_FIELD, logList.getMetadata().getParentResourceId(), Field.Store.YES));

//        document.add(new StringField(DocumentFields.NAME_FIELD, shoppingList.getName(), Field.Store.YES));
//        document.add(new SortedDocValuesField(DocumentFields.NAME_FIELD, new BytesRef(shoppingList.getName())));
//
//        document.add(new StringField(DocumentFields.DATE_FIELD,
//                DateTools.dateToString(DateUtils.toDate(shoppingList.getDate()), DateTools.Resolution.DAY), Field.Store.YES));
//
//        shoppingList.getItems().forEach(item -> document.add(new StringField(DocumentFields.ITEM_FIELD, item, Field.Store.YES)));

//        document.add(new StringField(DocumentFields.FILE_NAME_FIELD, shoppingList.getFileName(), Field.Store.YES));

        return document;
    }
}

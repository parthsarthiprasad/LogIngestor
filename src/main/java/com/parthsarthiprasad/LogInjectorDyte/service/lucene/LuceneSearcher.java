package com.parthsarthiprasad.LogInjectorDyte.service.lucene;

import com.parthsarthiprasad.LogInjectorDyte.model.LogList;
import com.parthsarthiprasad.LogInjectorDyte.model.Metadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static com.parthsarthiprasad.LogInjectorDyte.util.DateUtils.toDate;
//import static com.parthsarthiprasad.example.util.DateUtils.toDate;


/**
 * Component that searches an index with Apache Lucene.
 *
 * @author parthsarthiprasad
 */
@ApplicationScoped
public class LuceneSearcher {

    /**
     * Find all documents.
     *
     * @param index
     * @return
     * @throws IOException
     */
    public List<LogList> findAll(Directory index) throws IOException {
        Query query = new MatchAllDocsQuery();
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }

    /**
     * Search documents by log level
     *
     * @param index
     * @param level
     * @return
     * @throws IOException
     */
    public List<LogList> findByLogLevel(Directory index, String level) throws IOException {
        Query query = new TermQuery(new Term(DocumentFields.LEVEL_FIELD, level));
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }
    /**
     * Search documents by log message
     *
     * @param index
     * @param message
     * @return
     * @throws IOException
     */
    public List<LogList> findByLogMessage(Directory index, String message) throws IOException {
        Query query = new TermQuery(new Term(DocumentFields.MESSAGE_FIELD, message));
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }
    /**
     * Search documents by log resouceID
     *
     * @param index
     * @param resouceId
     * @return
     * @throws IOException
     */
    public List<LogList> findByLogResouceId(Directory index, String resouceId) throws IOException {
        Query query = new TermQuery(new Term(DocumentFields.RESOURCE_ID_FIELD, resouceId));
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }
    /**
     * Search documents by log timestamp
     *
     * @param index
     * @param Timestamp
     * @return
     * @throws IOException
     */
    public List<LogList> findByLogTimestamp(Directory index, String Timestamp) throws IOException {
        Query query = new TermQuery(new Term(DocumentFields.TIMESTAMP_FIELD, Timestamp));
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }
    /**
     * Search documents by log traceId
     *
     * @param index
     * @param logTraceId
     * @return
     * @throws IOException
     */
    public List<LogList> findByLogTraceId(Directory index, String logTraceId) throws IOException {
        Query query = new TermQuery(new Term(DocumentFields.TRACE_ID_FIELD, logTraceId));
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }
    /**
     * Search documents by log spanId
     *
     * @param index
     * @param spanID
     * @return
     * @throws IOException
     */
    public List<LogList> findByLogSpanId(Directory index, String spanID) throws IOException {
        Query query = new TermQuery(new Term(DocumentFields.SPAN_ID_FIELD, spanID));
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }
    /**
     * Search documents by log commit
     *
     * @param index
     * @param commit
     * @return
     * @throws IOException
     */
    public List<LogList> findByLogCommit(Directory index, String commit) throws IOException {
        Query query = new TermQuery(new Term(DocumentFields.COMMIT_FIELD, commit));
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }
    /**
     * Search documents by log level
     *
     * @param index
     * @param parentResouceId
     * @return
     * @throws IOException
     */
    public List<LogList> findByLogParentResouceId(Directory index, String parentResouceId) throws IOException {
        Query query = new TermQuery(new Term(DocumentFields.PARENT_RESOURCE_ID_FIELD, parentResouceId));
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }

    /**
     * Search documents by a range of date.
     *
     * @param index
     * @param lowerValue
     * @param upperValue
     * @return
     * @throws IOException
     */
    public List<LogList> findByDateRange(Directory index, LocalDate lowerValue, LocalDate upperValue) throws IOException {

        String lowerValueAsString = DateTools.dateToString(toDate(lowerValue), DateTools.Resolution.DAY);
        String upperValueAsString = DateTools.dateToString(toDate(upperValue), DateTools.Resolution.DAY);

        Query query = new TermRangeQuery(DocumentFields.TIMESTAMP_FIELD, new BytesRef(lowerValueAsString), new BytesRef(upperValueAsString), true, true);
        List<Document> documents = executeQuery(index, query, Integer.MAX_VALUE);
        return documents.stream().map(this::toLogList).collect(Collectors.toList());
    }

    /**
     * Execute a query.
     *
     * @param index
     * @param query
     * @param maxResults
     * @return
     * @throws IOException
     */
    private List<Document> executeQuery(Directory index, Query query, Integer maxResults) throws IOException {

        Sort sort = new Sort(new SortField(DocumentFields.TIMESTAMP_FIELD, SortField.Type.STRING));

        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs topDocs = searcher.search(query, maxResults);


        return Arrays.stream(topDocs.scoreDocs)
                .map(scoreDoc -> toDocument(scoreDoc, searcher))
                .collect(Collectors.toList());
    }

    /**
     * Get a Lucene {@link Document} from a Lucene {@link ScoreDoc}.
     *
     * @param scoreDoc
     * @param searcher
     * @return
     */
    private Document toDocument(ScoreDoc scoreDoc, IndexSearcher searcher) {
        try {
            return searcher.doc(scoreDoc.doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a {@link LogList} instance from a Lucene {@link Document}.
     *
     * @param document
     * @return
     */
    private LogList toLogList(Document document) {

        LogList logList = new LogList();
//        logList.setName(document.get(DocumentFields.NAME_FIELD));

//        try {
//            Date date = DateTools.stringToDate(document.get(DocumentFields.DATE_FIELD));
//            LocalDate localDate = toLocalDate(date);
//            shoppingList.setDate(localDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        logList.setCommit(document.get(DocumentFields.COMMIT_FIELD));
        logList.setLevel(document.get(DocumentFields.LEVEL_FIELD));
        logList.setMessage(document.get(DocumentFields.MESSAGE_FIELD));
        logList.setResourceId(document.get(DocumentFields.RESOURCE_ID_FIELD));
        logList.setTimestamp(document.get(DocumentFields.TIMESTAMP_FIELD));
        logList.setTraceId(document.get(DocumentFields.TRACE_ID_FIELD));
        logList.setSpanId(document.get(DocumentFields.SPAN_ID_FIELD));
        logList.setMetadata(new Metadata(document.get(DocumentFields.PARENT_RESOURCE_ID_FIELD)));
//        logList.setItems(Arrays.stream(document.getFields(DocumentFields.ITEM_FIELD))
//                .map(IndexableField::stringValue).collect(Collectors.toList()));
//        shoppingList.setFileName(document.get(DocumentFields.FILE_NAME_FIELD));
//
        return logList;
    }
}

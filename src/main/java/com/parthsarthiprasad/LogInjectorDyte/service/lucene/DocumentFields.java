package com.parthsarthiprasad.LogInjectorDyte.service.lucene;

/**
 * Constants for document field names.
 *
 * @author parthsarthiprasad
 */
public final class DocumentFields {
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
    private DocumentFields() {
        throw new AssertionError("No instances for you!");
    }

    public static final String LEVEL_FIELD = "level";
    public static final String MESSAGE_FIELD = "message";
    public static final String RESOURCE_ID_FIELD = "resourceId";
    public static final String TIMESTAMP_FIELD = "timestamp";
    public static final String TRACE_ID_FIELD = "traceId";
    public static final String SPAN_ID_FIELD = "spanId";
    public static final String COMMIT_FIELD = "commit";
    public static final String PARENT_RESOURCE_ID_FIELD = "parentResourceId";

}

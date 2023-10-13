package project.studycafe.helper.aop.logging.trace.object;

public class TraceStatus{

    private TraceId traceId;
    private Long startTimeMs;
    private String message;

    public static TraceStatus createTraceStatus(TraceId traceId, Long startTimeMs, String message) {
        TraceStatus traceStatus = new TraceStatus();
        traceStatus.traceId = traceId;
        traceStatus.startTimeMs = startTimeMs;
        traceStatus.message = message;

        return traceStatus;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }

}

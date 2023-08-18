package project.studycafe.helper.aop.logging.trace.object;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class TraceId{

    private String id;
    private int level;

    public static TraceId createTraceId(){
        TraceId traceId = new TraceId();
        traceId.id = UUID.randomUUID().toString().substring(0, 8);
        traceId.level = 0;
        return traceId;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }


    public boolean isFirstLevel() {
        return level == 0;
    }

    //String 과 int 타입은 불변 타입이라, 그냥 복사해도 다른 주소를 가짐.
    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}

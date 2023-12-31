package project.studycafe.app.domain.base;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

//통계
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Statistics {
    @NotNull
    private Integer readCount;
    @NotNull
    private Integer likeCount;

    //영속상태로 변하기 직전에 시점에 시작됨.
    @PrePersist
    public void setting(){
        if (this.likeCount == null) {
            this.likeCount = 0;
        }
        if (this.readCount == null) {
            this.readCount = 0;
        }
    }

    public static Statistics createStatistics(int readCount,int likeCount) {
        Statistics statistics = new Statistics(readCount, likeCount);
        return statistics;
    }

    public Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("readCount", readCount);
        map.put("likeCount", likeCount);
        return map;
    }

    public void upLikeCount() {
        this.likeCount += 1;
    }
    public void downLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount -= 1;
        }
    }
    public void upReadCount() {
        this.readCount += 1;
    }
    public void downReadCount() {
        if (this.readCount > 0) {
            this.readCount -= 1;
        }
    }
}

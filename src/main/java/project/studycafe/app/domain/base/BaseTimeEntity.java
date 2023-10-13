package project.studycafe.app.domain.base;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass // BaseEntity 를 상속한 엔티티들은 아래 필드들을 컬럼으로 인식하게 된다.
@EntityListeners(AuditingEntityListener.class)  //Spring Data JPA가 Auditing(자동으로 값 매핑) 기능 추가
//도메인을 영속성 컨텍스트에 저장하거나 조회를 수행한 후에 update를 하는 경우,
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    public LocalDateTime getCreatedTime() {
        LocalDateTime newCreateTime = createdTime;
        return newCreateTime;
    }

    public LocalDateTime getUpdatedTime() {
        LocalDateTime newUpdatedTime = updatedTime;
        return newUpdatedTime;
    }

}

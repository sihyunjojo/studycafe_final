package project.studycafe.app.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberLevel {
    MASTER("ROLE_GUEST","마스터"),
    USER("ROLE_USER", "일반 사용자"),
    GUEST("ROLE_GUEST","손님");

    MemberLevel(String key, String title) {
    }
}

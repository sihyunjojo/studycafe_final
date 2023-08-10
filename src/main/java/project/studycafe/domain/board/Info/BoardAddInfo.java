package project.studycafe.domain.board.Info;

import lombok.ToString;
import project.studycafe.domain.board.AttachmentFile;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Embeddable
public class BoardAddInfo {

    //Cascadetype.all을 하게되면 세션에 2개의 같은 pk를 가진 attach 엔티티가 발생하여서 에러가 발생한다.
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<AttachmentFile> attachmentFiles = new ArrayList<>(); // 추후에 객체 따로만들어야할지도


    public void removeAttachmentFile(AttachmentFile attachmentFile) {
        this.attachmentFiles.remove(attachmentFile);
    }

    public void addAttachmentFile(AttachmentFile attachmentFile) {
        this.attachmentFiles.add(attachmentFile);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("attachmentFiles", attachmentFiles);
        return map;
    }


    public List<AttachmentFile> getAttachmentFiles() {
        return attachmentFiles.stream()
                .map(attachmentFile -> AttachmentFile.createAttachmentFile(attachmentFile))
                .collect(Collectors.toUnmodifiableList());
    }

    private void setAttachmentFiles(List<AttachmentFile> attachmentFiles) {
        this.attachmentFiles = attachmentFiles;
    }

}

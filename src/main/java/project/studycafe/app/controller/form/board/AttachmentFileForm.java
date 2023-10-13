package project.studycafe.app.controller.form.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.enums.FileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class AttachmentFileForm {

    private String uniqueFileName;
    private String attachmentFileName;
    private long attachmentFileSize;
    private FileType attachmentFileType;

    public static List<AttachmentFileForm> createAttachmentFileForms(List<AttachmentFile> attachmentFiles) {
        List<AttachmentFileForm> attachmentFileForms = new ArrayList<>();
        log.info("attachmentFiles={}", attachmentFiles); // 여기서 sql 처음 attachmentfiles 부를때 sql 발생.
        for (AttachmentFile attachmentFile : attachmentFiles) {
            Map<String, Object> attachmentFileMap = attachmentFile.toMap();
            AttachmentFileForm attachmentFileForm = new AttachmentFileForm();

            attachmentFileForm.setUniqueFileName((String) attachmentFileMap.get("uniqueFileName"));
            attachmentFileForm.setAttachmentFileName((String) attachmentFileMap.get("attachmentFileName"));
            attachmentFileForm.setAttachmentFileSize((Long) attachmentFileMap.get("attachmentFileSize"));
            attachmentFileForm.setAttachmentFileType((FileType) attachmentFileMap.get("attachmentFileType"));
            attachmentFileForms.add(attachmentFileForm);
        }

        return attachmentFileForms;
    }

}

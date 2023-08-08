package project.studycafe.domain.form.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.studycafe.domain.board.AttachmentFile;
import project.studycafe.domain.enums.FileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentFileForm {

    private String uniqueFileName;
    private String attachmentFileName;
    private long attachmentFileSize;
    private FileType attachmentFileType;

    public static AttachmentFileForm createAttachmentFileForm(AttachmentFile attachmentFile) {
        Map<String, Object> attachmentFileMap = attachmentFile.toMap();
        AttachmentFileForm attachmentFileForm = new AttachmentFileForm();
        
        attachmentFileForm.setUniqueFileName((String) attachmentFileMap.get("uniqueFileName"));
        attachmentFileForm.setAttachmentFileName((String) attachmentFileMap.get("attachmentFileName"));
        attachmentFileForm.setAttachmentFileSize((Long) attachmentFileMap.get("attachmentFileSize"));
        attachmentFileForm.setAttachmentFileType((FileType) attachmentFileMap.get("attachmentFileType"));
        return attachmentFileForm;
    }
    public static List<AttachmentFileForm> createAttachmentFileForms(List<AttachmentFile> attachmentFiles) {
        List<AttachmentFileForm> attachmentFileForms = new ArrayList<>();

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

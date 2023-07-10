package project.studycafe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.studycafe.domain.AttachmentFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    // 파일 등록하는 순간 파일이 여기로 복사되서 들어감.(저장소의 위치)
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // 이미지 여러개 날라올때
    public List<AttachmentFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<AttachmentFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                AttachmentFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }

    // 이미지 하나 날라올때
    public AttachmentFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        //image.png
        String originalFilename = multipartFile.getOriginalFilename();
        //서버에 저장하는 파일명 uuid+.+확장자
        String storeFileName = createStoreFileName(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName))); //업로드된 파일을 지정된 경로로 전송(이동)하는 기능을 제공한다.

        return new AttachmentFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}

package org.likelion.jangsu.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));

        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName){
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName){
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile){
        String name = targetFile.getName();

        // convert() 과정에서 로컬에 생성된 파일을 삭제
        if (targetFile.delete()){
            log.info(name + "파일 삭제 완료");
        } else {
            log.info(name + "파일 삭제 실패");
        }
    }

    public Optional<File> convert(MultipartFile multipartFile) throws IOException{
        File convertFile = new File(multipartFile.getOriginalFilename());
        if (convertFile.createNewFile()){ // 해당 경로에 파일이 없을 경우, 새 파일 생성

            try (FileOutputStream fos = new FileOutputStream(convertFile)) {

                // multipartFile의 내용을 byte로 가져와서 write
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}

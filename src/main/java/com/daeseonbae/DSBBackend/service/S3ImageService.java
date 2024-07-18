package com.daeseonbae.DSBBackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ImageService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    //이미지 null check
    public String upload(MultipartFile image) throws IOException {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new IllegalArgumentException("이미지가 비어있거나 null 입니다.");
        }
        return this.uploadImage(image);
    }

    //이미지 파일 확장자를 검증하고 이미지를 S3에 업로드합니다.
    private String uploadImage(MultipartFile image){
        this.validateImageFileExtention(image.getOriginalFilename());
        try{
            return this.uploadImageToS3(image);
        }catch(Exception e){
            throw new RuntimeException("S3에 이미지를 업로드하는데 실패 했습니다.", e);
        }
    }

    //주어진 파일명의 확장자 검증
    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("파일에 확장자가 없습니다.");
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtensionList.contains(extension)) {
            throw new IllegalArgumentException("존재하지 않는 확장자 입니다.");
        }
    }

    private String uploadImageToS3(MultipartFile image) throws Exception {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf("."));//확장자 명

        //파일 원본 이름이 중복될 때를 대비해서 UUID로 파일 이름을 새롭게 생성
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is); //이미지를 byte[]로 변환

        //S3에 업로드할 파일의 메데이터를 설정함
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/"+extention); //콘텐츠 타입
        metadata.setContentLength(bytes.length); //콘텐츠 길이

        //S3에 요청할 때 사용할 byteInputStream 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            //S3로 putObject 할 때 사용할 요청 객체
            //생성자 : bucket 이름, 파일 명, byteInputStream, metadata
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,s3FileName,byteArrayInputStream,metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            //실제로 S3에 이미지 데이터를 넣는 부분
            amazonS3.putObject(putObjectRequest);
        }catch (Exception e){
            throw new Exception("S3 Request 실패 :",e);
        }finally{
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e) {
            throw new RuntimeException("Failed to delete image from S3: " + e.getMessage(), e);
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format: " + imageAddress, e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to decode URL: " + imageAddress, e);
        }
    }

}

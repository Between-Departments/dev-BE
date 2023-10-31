package com.gwakkili.devbe.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    List<String> uploadImage(List<MultipartFile> images) throws IOException;

    void deleteImage(String ImgUrl);

    void deleteImageList(List<String> ImgUrlList);
}

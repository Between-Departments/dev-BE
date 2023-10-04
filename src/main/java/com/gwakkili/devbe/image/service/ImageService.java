package com.gwakkili.devbe.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    List<String> upload(List<MultipartFile> images) throws IOException;

    void delete(String ImgName);
}

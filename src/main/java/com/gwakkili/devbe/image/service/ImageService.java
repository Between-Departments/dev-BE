package com.gwakkili.devbe.image.service;

import com.gwakkili.devbe.image.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    List<ImageDto> upload(List<MultipartFile> images) throws IOException;

    void delete(String ImgName);
}

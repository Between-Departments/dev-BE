package com.gwakkili.devbe.image.controller;

import com.gwakkili.devbe.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> upload(@RequestParam(value = "images", required = false) List<MultipartFile> multipartFiles) throws IOException {
        log.info("이미지 업로드 요청");
        return imageService.upload(multipartFiles);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("imageUrl") String imageUrl){
        imageService.delete(imageUrl);
    }

}

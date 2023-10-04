package com.gwakkili.devbe.image.controller;

import com.gwakkili.devbe.image.dto.ImageDto;
import com.gwakkili.devbe.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "Image", description = "이미지 API")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "이미지 업로드")
    @ApiResponse(responseCode = "201", description = "이미지 url 리스트", useReturnTypeSchema = true)
    public List<ImageDto> upload(@RequestParam(value = "images", required = false) List<MultipartFile> multipartFiles) throws IOException {
        log.info("이미지 업로드 요청");
        return imageService.upload(multipartFiles);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "이미지 삭제 요청")
    public void delete(@Parameter(description = "이미지 url") @RequestParam("imageUrl") String imageUrl) {
        imageService.delete(imageUrl);
    }

}

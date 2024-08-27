//package soomsheo.Telo.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Collections;
//
//@Controller
//public class ImageController {
//    private static final String UPLOADED_FOLDER = "uploaded-images/";
//
//    @PostMapping("/api/upload-image")
//    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
//        try {
//            File directory = new File(UPLOADED_FOLDER);
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            String fileName = file.getOriginalFilename();
//            Path path = Paths.get(UPLOADED_FOLDER + fileName);
//            Files.write(path, file.getBytes());
//
//            String fileURL = "/uploaded-images/" + fileName;
//            return ResponseEntity.ok().body(Collections.singletonMap("imageURL", fileURL));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("파일 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}

package com.example.ArtHub.Controller;

import com.example.ArtHub.Entity.Image;
import com.example.ArtHub.InterfaceOfControllers.IImageController;
import com.example.ArtHub.Repository.CourseRepository;
import com.example.ArtHub.Repository.ImageRepository;
import com.example.ArtHub.ResponeObject.ResponeObject;
import com.example.ArtHub.Service.ServiceOfCourse;
import com.example.ArtHub.Service.ServiceOfFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@CrossOrigin(origins = "https://arthubplatform1.azurewebsites.net/", maxAge = 3600)
@RestController
public class ControllerOfImage implements IImageController {

    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    Path staticPath = Paths.get("static");
    Path imagePath = Paths.get("images");

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ServiceOfFile serviceOfFile;


    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("text/xml")
                || contentType.equals("application/pdf")
                || contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }

    public String getImageName(MultipartFile image) throws IOException {
        String randomName = UUID.randomUUID().toString().substring(0, 5)+image.getOriginalFilename();
        Path file = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(randomName);
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        return randomName;
    }

    public ResponseEntity<ResponeObject> saveCourseImages(@RequestParam MultipartFile one, @RequestParam MultipartFile two, @RequestParam MultipartFile three, @RequestParam MultipartFile four, @RequestParam int courseId) throws IOException {

        List<MultipartFile> Arr = Arrays.asList(one, two, three, four);

        List<String> imageNames = new ArrayList<>();
        for (MultipartFile image : Arr) {
            imageNames.add(image.getOriginalFilename());
            serviceOfFile.uploadFile(image);

        }

        Image image = new Image();
        image.setOne(imageNames.get(0));
        image.setTwo(imageNames.get(1));
        image.setThree(imageNames.get(2));
        image.setFour(imageNames.get(3));
        image.setCourse(courseRepository.findById(courseId).orElseThrow());

        if (imageRepository.existsByCourseId(courseId)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponeObject("ok", "Images updated successfully!!", imageRepository.updateImages(image.getOne(), image.getTwo(), image.getThree(), image.getFour(), image.getCourse().getId()))
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponeObject("ok", "Images updated successfully!!", imageRepository.save(image))
            );
        }
    }
    }


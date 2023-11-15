//package com.example.ArtHub.Service;
//
//import com.example.ArtHub.DTO.CreateCourseDTO;
//import com.example.ArtHub.Entity.Account;
//import com.example.ArtHub.Entity.Course;
//import com.example.ArtHub.Entity.Role;
//import com.example.ArtHub.Repository.AccountRepository;
//import com.example.ArtHub.Repository.CourseRepository;
//import com.example.ArtHub.Repository.RoleRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.verify;
//
//@DataJpaTest
//@ExtendWith(MockitoExtension.class)
//class ServiceOfCourseTest {
//
//
//    @Mock
//    private CourseRepository courseRepository;
//
//    @Mock
//    private RoleRepository roleRepository;
//    @Mock
//    private AccountRepository accountRepository;
//    private ServiceOfCourse serviceOfCourse;
//
//
//    @BeforeEach
//    void setUp() {
//        serviceOfCourse = new ServiceOfCourse(courseRepository, accountRepository);
//    }
//
//    @Test
//    void CangetCourseList() {
//        serviceOfCourse.getCourseList();
//        verify(courseRepository).findAll();
//    }
//
//    private static final Logger logger = LoggerFactory.getLogger(ServiceOfCourseTest.class);
//
//    @Test
//    void CanSaveCourse() {
//        Role Role = new Role(1,"ad");
//        roleRepository.save(Role);
//
//        Account RECORD_2 = new Account("Acc1","1232","123","123","123","123","123","123","1","123","123","123","123","123","123");
//        accountRepository.save(RECORD_2);
//
//        Course RECORD_1 = new Course("CourseTest1","CourseTest1","CourseTest1","1","English",(float)44444,(float)0.1,false,1,"null",null,RECORD_2);
//        courseRepository.save(RECORD_1);
//
//
//        CreateCourseDTO dto = new CreateCourseDTO(RECORD_1.getId(),RECORD_1.getName(),RECORD_1.getDescription(),RECORD_1.getIntroduction(),RECORD_1.getLevel(),RECORD_1.getLanguage(), RECORD_1.getPrice(),RECORD_1.getCoupon(), RECORD_1.getPassed(),false,RECORD_1.getAccount().getId(),RECORD_1.getImage(),RECORD_1.getDate()
//                , null, null, null);
//
//        serviceOfCourse.createCourse(dto);
//
//        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
//
//        verify(courseRepository).save(courseArgumentCaptor.capture());
//
//        Course capturedCourse = courseArgumentCaptor.getValue();
//
//        assertThat(capturedCourse.getId()).isEqualTo(RECORD_1.getId());
//
//    }
//
//
////    @Test
////    void CanAddNewCourse() {
////        serviceOfCourse.createCourse(courseRepository);
////        verify(courseRepository).findAll();
////    }
//}
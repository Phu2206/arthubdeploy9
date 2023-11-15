package com.example.ArtHub.Repository;

import com.example.ArtHub.Entity.Account;
import com.example.ArtHub.Entity.Course;
import com.example.ArtHub.Entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest {


    @Autowired
    private CourseRepository courseRepositor;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private RoleRepository roleRepository;


    @Test
    void ShouldAddCourseSuccessfully() {


        Role Role = new Role(1,"ad");
        roleRepository.save(Role);

        Account RECORD_2 = new Account("Acc1","1232","123","123","123","123","123","123","1","123","123","123","123","123","123");
        accountRepository.save(RECORD_2);

        Course RECORD_1 = new Course("CourseTest1","CourseTest1","CourseTest1","1","English",(float)44444,(float)0.1,false,1,"null",null,RECORD_2);
        courseRepositor.save(RECORD_1);

        Course RECORD5 = new Course("CourseTest2","CourseTest1","CourseTest1","1","English",(float)44444,(float)0.1,false,1,"null",null,RECORD_2);
        courseRepositor.save(RECORD5);

        Course RECORD_3 = new Course("CourseTest3","CourseTest1","CourseTest1","1","English",(float)44444,(float)0.1,false,1,"null",null,RECORD_2);
        courseRepositor.save(RECORD_3);


        Course course = courseRepositor.findById(1).orElseThrow();


        assertThat(course).isNotNull();
        assertEquals(1, course.getId());
        assertThat(course.getName()).isEqualTo("CourseTest1");
        assertThat(courseRepositor.findAll().size()).isEqualTo(3);

    }
}
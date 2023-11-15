
package com.example.ArtHub.Controller;

import com.example.ArtHub.AppServiceExeption;
import com.example.ArtHub.DTO.CreateLearnerDTO;
import com.example.ArtHub.DTO.ResponeAccountDTO;
import com.example.ArtHub.DTO.ResponseLearnerDTO;
import com.example.ArtHub.Entity.Account;
import com.example.ArtHub.Entity.Learner;
import com.example.ArtHub.InterfaceOfControllers.ILearnerController;
import com.example.ArtHub.InterfaceOfControllers.ILearnerController;
import com.example.ArtHub.Repository.AccountRepository;
import com.example.ArtHub.Repository.CourseRepository;
import com.example.ArtHub.Repository.LearnerRepository;
import com.example.ArtHub.Service.ServiceOfCourse;
import com.example.ArtHub.Service.ServiceOfLearner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://arthubplatform1.azurewebsites.net/", maxAge = 3600)
@RestController
public class ControllerOfLearner implements ILearnerController {
    @Autowired
    LearnerRepository learnerRepository;

    @Autowired
    ServiceOfLearner learnerService;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ServiceOfCourse serviceOfCourse;

    @Autowired
    AccountRepository accountRepository;


    public static ResponeAccountDTO fromAccount(Account account) {
        ResponeAccountDTO accountResponseDTO = new ResponeAccountDTO();
        accountResponseDTO.setId(account.getId());
        accountResponseDTO.setImage(account.getImage());
        accountResponseDTO.setUsername(account.getUsername());
        accountResponseDTO.setAddress(account.getAddress());
        accountResponseDTO.setLastname(account.getLastname());
        accountResponseDTO.setFirstname(account.getFirstname());
        accountResponseDTO.setEmail(account.getEmail());
        accountResponseDTO.setRoleId(account.getRoleId());
        accountResponseDTO.setTwitter(account.getTwitter());
        accountResponseDTO.setFacebook(account.getFacebook());
        accountResponseDTO.setPassword(account.getPassword());
        return accountResponseDTO;
    }

    public ResponseEntity<List<ResponseLearnerDTO>> showStudentPurchase(Integer accountId) {
        try {
            Account accountOptional = accountRepository.findById(accountId).orElseThrow();
            if(fromAccount(accountOptional) == null) {
                return ResponseEntity.badRequest().body(null);
            }

            else{
                List<Learner> learnerList = learnerRepository.showStudentPurchaseByAccountId(accountId);
                return ResponseEntity.ok(fromLearnerListToResponseLearnerDTOList(learnerList));
            }

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    public ResponseLearnerDTO fromLearnerToResponseLearnerDTO (Learner learner) {
        ResponseLearnerDTO learnerDTO = new ResponseLearnerDTO();
        learnerDTO.setId(learner.getId());
        learnerDTO.setAccountId(learner.getAccount().getId());
        learnerDTO.setCourseId(learner.getCourse().getId());
        learnerDTO.setOwnerCourse(courseRepository.findById(learner.getCourse().getId()).get().getAccount().getId());
        learnerDTO.setDate(learner.getDate());
        learnerDTO.setPrice(learner.getPrice());
        learnerDTO.setStatus(learner.getStatus());
        learnerDTO.setSenderId(learner.getSenderId());
        learnerDTO.setMessage(learner.getMessage());
        learnerDTO.setCourse(serviceOfCourse.fromCourseToResponeCourseDTO3(learner.getCourse()));
        learnerDTO.setAccount(fromAccount(learner.getAccount()));
        return learnerDTO;


    }

    public List<ResponseLearnerDTO> fromLearnerListToResponseLearnerDTOList(List<Learner> LearnerList) {
        List<ResponseLearnerDTO> ResponseLearnerDTOList = new ArrayList<>();
        for (Learner learner: LearnerList) {
            ResponseLearnerDTOList.add(fromLearnerToResponseLearnerDTO(learner));
        }
        return ResponseLearnerDTOList;
    }

    public List<ResponseLearnerDTO> getLearnerList() {
        List<Learner> learnerListFromDB = learnerRepository.findAll();
        List<ResponseLearnerDTO> responseLearnerDTOList = fromLearnerListToResponseLearnerDTOList(learnerListFromDB);
        return responseLearnerDTOList;
    }

    public ResponseLearnerDTO createLearner(CreateLearnerDTO dto) throws AppServiceExeption, IOException {
        Learner learner = learnerService.createLearner(dto);

        return fromLearnerToResponseLearnerDTO(learner);

    }

    @Override
    public int countLearner(int owner) throws AppServiceExeption, IOException {
        return learnerRepository.countDistinctAccountIdByOwner(owner);
    }

    @Override
    public double getProfitByOwner(int owner) throws AppServiceExeption, IOException {
        return learnerRepository.sumOfProfit(owner);
    }


}
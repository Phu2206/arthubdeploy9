package com.example.ArtHub.Controller;


import com.example.ArtHub.AppServiceExeption;
import com.example.ArtHub.DTO.CreateAccountDTO;
import com.example.ArtHub.DTO.ResponeAccountDTO;
import com.example.ArtHub.DTO.ResponseAccountDTO;
import com.example.ArtHub.Entity.Account;
import com.example.ArtHub.MailConfig.InterfaceOfMailService;
import com.example.ArtHub.MailConfig.MailDetail;
import com.example.ArtHub.Repository.AccountRepository;
import com.example.ArtHub.ResponeObject.ResponeObject;
import com.example.ArtHub.Service.ServiceOfFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://arthubplatform1.azurewebsites.net/")
@RestController
@RequestMapping("/api")
public class AccountController {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ServiceOfFile serviceOfFile;
    @Autowired
    private InterfaceOfMailService mailService;

    //    @GetMapping("/accounts")
//    public ResponseEntity<List<Account>> getAllAccounts(@RequestParam(required = false) String name) {
//        try {
//            List<Account> accounts = new ArrayList<Account>();
//            if (name == null)
//                accountRepository.findAll().forEach(accounts::add);
//            else
//                accountRepository.findByNameContaining(name).forEach(accounts::add);
//
//            if (accounts.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//            return new ResponseEntity<>(accounts, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//
//        }
//    }

    @GetMapping("/testConnection")
    public ResponseEntity<ResponeObject> testConnection()
    {
        return new ResponseEntity<>(new ResponeObject("Ok","Connect to server successfully!",null),HttpStatus.OK);
    }
    @GetMapping("/accounts")
    public ResponseEntity<List<ResponeAccountDTO>> getAllAccounts2(@RequestParam(required = false) String name) {
        try {
            List<ResponeAccountDTO> accounts;

            if (name == null) {
                accounts = accountRepository.findAll().stream()
                        .map(account -> fromAccount(account))
                        .collect(Collectors.toList());
            } else {
                accounts = accountRepository.findByUsernameContaining(name).stream()
                        .map(account -> fromAccount(account))
                        .collect(Collectors.toList());
            }

            if (accounts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/accounts/GoogleLogin")
    public ResponseEntity<ResponseAccountDTO> LoginGoogleAccount(@RequestParam String email) {
        ResponeAccountDTO userAccount = fromAccount(accountRepository.findByEmail(email).orElseThrow());
        if (userAccount != null ) {
            ResponseAccountDTO responseAccountDTO = new ResponseAccountDTO();
            responseAccountDTO.setId(userAccount.getId());
            responseAccountDTO.setUsername(userAccount.getUsername());
            responseAccountDTO.setAddress(userAccount.getAddress());
            responseAccountDTO.setLastname(userAccount.getLastname());
            responseAccountDTO.setFirstname(userAccount.getFirstname());
            responseAccountDTO.setPhone(userAccount.getPhone());
            responseAccountDTO.setImage(userAccount.getImage());
            responseAccountDTO.setEmail(userAccount.getEmail());
            responseAccountDTO.setRoleId(userAccount.getRoleId());
            responseAccountDTO.setTwitter(userAccount.getTwitter());
            responseAccountDTO.setFacebook(userAccount.getFacebook());
            responseAccountDTO.setBio(userAccount.getBio());
            responseAccountDTO.setIsActive(userAccount.getIsActive());
            responseAccountDTO.setToken(userAccount.getToken());
            responseAccountDTO.setIsPremium(userAccount.getIsPremium());
            return new ResponseEntity<>(responseAccountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accounts/changePassword")
    public ResponseEntity<ResponseAccountDTO> ChangePassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String oldPassword) {
        Optional<Account> userAccount = accountRepository.findByEmail(email);
        if (userAccount.isPresent()) {
            if (!userAccount.get().getPassword().equals(oldPassword)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                int changed = accountRepository.updatePassword(email, newPassword);
                if (changed != 0) {
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accounts/login")
    public ResponseEntity<ResponseAccountDTO> LoginAccount(@RequestParam String email, @RequestParam String password) {
        ResponeAccountDTO userAccount = fromAccount(accountRepository.findByEmail(email).orElseThrow());
        if (userAccount != null) {
            if (userAccount.getPassword().equals(password)) {
                ResponseAccountDTO responseAccountDTO = new ResponseAccountDTO();
                responseAccountDTO.setId(userAccount.getId());
                responseAccountDTO.setUsername(userAccount.getUsername());
                responseAccountDTO.setAddress(userAccount.getAddress());
                responseAccountDTO.setLastname(userAccount.getLastname());
                responseAccountDTO.setFirstname(userAccount.getFirstname());
                responseAccountDTO.setPhone(userAccount.getPhone());
                responseAccountDTO.setImage(userAccount.getImage());
                responseAccountDTO.setEmail(userAccount.getEmail());
                responseAccountDTO.setRoleId(userAccount.getRoleId());
                responseAccountDTO.setTwitter(userAccount.getTwitter());
                responseAccountDTO.setFacebook(userAccount.getFacebook());
                responseAccountDTO.setBio(userAccount.getBio());
                responseAccountDTO.setIsActive(userAccount.getIsActive());
                responseAccountDTO.setToken(userAccount.getToken());
                responseAccountDTO.setIsPremium(userAccount.getIsPremium());
                return new ResponseEntity<>(responseAccountDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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
        accountResponseDTO.setToken(account.getToken());
        return accountResponseDTO;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<ResponeAccountDTO> getAccountById(@PathVariable("id") int id) {
        ResponeAccountDTO accountData = fromAccount(accountRepository.findById(id).orElseThrow());

        if (accountData != null) {
            return new ResponseEntity<>(accountData, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accounts/checkToken")
    public ResponseEntity<ResponeAccountDTO> checkToken2(@RequestParam int id, @RequestParam String token) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            ResponeAccountDTO userAccount = fromAccount(optionalAccount.get());
            if (userAccount.getToken().equals(token)) {
                int updateToken = accountRepository.updateToken(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public boolean sendMailToReceiver(String email, String token) throws AppServiceExeption, IOException {
        String messageBody = "";
        String subject = "";

        messageBody = "Hello,\n" +
                "Please use the verification code below on the ArtHub website\n\n" +
                token + "\n\n" +
                "If you didn't request this, you can ignore this email or let us know.\n" +
                "Thanks! ArtHub team";
        subject = "ArtHub OTP verification";

        MailDetail mailDetail = new MailDetail();
        mailDetail.setMsgBody(messageBody);
        mailDetail.setRecipient(email);
        mailDetail.setSubject(subject);
        mailService.sendMail(mailDetail);
        return true;
    }

    @PostMapping("/accounts")
    public ResponseEntity<ResponeAccountDTO> createAccount(@RequestBody Account account) {
        try {
            Optional<Account> accountbyEmail = accountRepository.findByEmail(account.getEmail());
            Optional<Account> accountbyUsername = accountRepository.findByUsername(account.getUsername());
            if (!accountbyEmail.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); //email exist
            } else if (!accountbyUsername.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); //username exist
            } else {
                if(account.getToken() != null)//send otp
                {
                    boolean sendToken = sendMailToReceiver(account.getEmail(), account.getToken());
                }
                Account _account = accountRepository.save(new Account(
                        account.getUsername(),
                        account.getPassword(),
                        account.getAddress(),
                        account.getLastname(),
                        account.getFirstname(),
                        account.getPhone(),
                        account.getImage(),
                        account.getEmail(),
                        account.getRoleId(),
                        account.getTwitter(),
                        account.getFacebook(),
                        account.getBio(),
                        account.getIsActive(),
                        account.getToken(),
                        account.getIsPremium()));
                return new ResponseEntity<>(fromAccount(_account), HttpStatus.CREATED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AppServiceExeption e) {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/accounts")
    public ResponseEntity<ResponeAccountDTO> updateAccount(@RequestBody Account account) {
        Optional<Account> accountData = accountRepository.findById(account.getId());

        if (accountData.isPresent()) {
            Account _account = accountData.get();
            _account.setUsername(account.getUsername());
            if (account.getPassword() != null) {
                _account.setPassword(account.getPassword());
            }
            _account.setAddress(account.getAddress());
            _account.setLastname(account.getLastname());
            _account.setFirstname(account.getFirstname());
            _account.setPhone(account.getPhone());
            _account.setEmail(account.getEmail());
            _account.setBio(account.getBio());
            _account.setRoleId(account.getRoleId());
            _account.setTwitter(account.getTwitter());
            _account.setFacebook(account.getFacebook());
            _account.setIsActive(account.getIsActive());
            _account.setIsPremium(account.getIsPremium());
            return new ResponseEntity<>(fromAccount(accountRepository.save(_account)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") int id) {
        try {
            accountRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/accounts")
    public ResponseEntity<HttpStatus> deleteAllAccounts() {
        try {
            accountRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts/role")
    public ResponseEntity<List<Account>> findByRole() {
        try {
            List<Account> accounts = accountRepository.findByRoleId("1");
            if (accounts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateAccountImage")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponeObject> updateMainImageOfCourse(@RequestParam int accountId, @RequestParam MultipartFile image) throws AppServiceExeption, IOException {
        serviceOfFile.uploadFile(image);

        int rs = accountRepository.updateMainImageAccount(accountId, image.getOriginalFilename());

        if (rs != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponeObject("ok", "update account image successfully!", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponeObject("ok", "update account image failed!", "")
        );
    }


}

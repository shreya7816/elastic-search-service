package com.es.demo.controller;

import com.es.demo.manager.MemberManager;
import com.es.demo.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/v1/elasticsearch/member")
public class ElasticSearchController {

    @Autowired
    MemberManager memberManager;

    @PutMapping(
            value = "/createDocument"
    )
    public ResponseEntity<?> createDocument (@RequestBody Member member){
        try {
            String response = memberManager.create(member);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam(value = "id") UUID id){
        try {
            Member member = memberManager.findById(id);
            return new ResponseEntity<>(member, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Search Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/findByAmenityId")
    public ResponseEntity<?> findByAmenityId(@RequestParam(value = "id") UUID id){
        try {
            List<Member> member = memberManager.findByAmenityId(id);
            return new ResponseEntity<>(member, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Search Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("deleteDocuments/{id}")
    public ResponseEntity<?> deleteDocuments(@PathVariable UUID id) throws Exception {
        String response = memberManager.deleteDocument(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("findAll")
    public ResponseEntity<?> findAll() throws Exception {
        List<Member> members = memberManager.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PutMapping("updateDocument")
    public ResponseEntity<?> updateProfile(@RequestBody Member member) throws Exception {
        return new ResponseEntity(memberManager.updateProfile(member), HttpStatus.CREATED);
    }


}

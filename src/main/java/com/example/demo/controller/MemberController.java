package com.example.demo.controller;

import com.example.demo.DTO.MemberDTO;
import com.example.demo.common.Const;
import com.example.demo.entity.MemberPO;
import com.example.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@Tag(name = "員工管理")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping
    @Operation(summary = "addMember")
    public ResponseEntity<MemberPO> addMember(@RequestBody @Valid MemberDTO.addMember member) {
        MemberPO newMemberPO = new MemberPO();
        newMemberPO.setName(member.getName());
        newMemberPO.setGender(member.getGender());
        newMemberPO.setEmail(member.getEmail());
        newMemberPO.setStatus(Const.STRING_MEMBER_STATUS_ACTIVE);
        newMemberPO.setBirthDate(member.getBirthDate());
        newMemberPO.setPhoneNumber(member.getPhoneNumber());
        newMemberPO.setJoinDate(LocalDate.now());
        return ResponseEntity.ok(newMemberPO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<MemberPO> updateMember(@RequestBody MemberPO memberPO) {
        MemberPO updatedMemberPO = memberService.updateMember(memberPO);
        return ResponseEntity.ok(updatedMemberPO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberPO> getMember(@PathVariable Long id) {
        Optional<MemberPO> member = memberService.getMember(id);
        return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MemberPO>> getAllMembers() {
        List<MemberPO> memberPOS = memberService.getAllMembers();
        return ResponseEntity.ok(memberPOS);
    }
}

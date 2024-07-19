package com.example.demo.service;

import com.example.demo.entity.MemberPO;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberPO addMember(MemberPO memberPO) {
        return memberRepository.save(memberPO);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberPO updateMember(MemberPO memberPO) {
        return memberRepository.save(memberPO);
    }

    public Optional<MemberPO> getMember(Long id) {
        return memberRepository.findById(id);
    }

    public List<MemberPO> getAllMembers() {
        return memberRepository.findAll();
    }
}

package com.example.demo.controller;

import com.example.demo.DTO.MemberDTO;
import com.example.demo.common.Const;
import com.example.demo.entity.MemberPO;
import com.example.demo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    private MemberPO testMember;

    @BeforeEach
    void setUp() {
        testMember = new MemberPO();
        testMember.setId(1L);
        testMember.setName("Test User");
        testMember.setEmail("test@example.com");
        testMember.setGender("Male");
        testMember.setStatus(Const.STRING_MEMBER_STATUS_ACTIVE);
        testMember.setBirthDate(LocalDate.of(1990, 1, 1));
        testMember.setPhoneNumber("0912345678");
        testMember.setJoinDate(LocalDate.now());
    }

    @Test
    void testAddMember() throws Exception {
        MemberDTO.addMember newMember = new MemberDTO.addMember();
        newMember.setName("New User");
        newMember.setEmail("new@example.com");
        newMember.setGender(Const.STRING_GENDER_MAN);
        newMember.setBirthDate(LocalDate.of(1995, 5, 5));
        newMember.setPhoneNumber("9876543210");

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMember)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.gender").value(Const.STRING_GENDER_MAN))
                .andExpect(jsonPath("$.status").value(Const.STRING_MEMBER_STATUS_ACTIVE))
                .andExpect(jsonPath("$.birthDate").value("1995-05-05"))
                .andExpect(jsonPath("$.phoneNumber").value("9876543210"))
                .andExpect(jsonPath("$.joinDate").isNotEmpty());
    }

    @Test
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/members/1"))
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).deleteMember(1L);
    }

    @Test
    void testUpdateMember() throws Exception {
        when(memberService.updateMember(any(MemberPO.class))).thenReturn(testMember);

        mockMvc.perform(put("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMember)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
    }
    @Test
    void testGetMember() throws Exception {
        when(memberService.getMember(1L)).thenReturn(Optional.of(testMember));

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testGetMemberNotFound() throws Exception {
        when(memberService.getMember(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/members/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllMembers() throws Exception {
        when(memberService.getAllMembers()).thenReturn(Arrays.asList(testMember));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }
}

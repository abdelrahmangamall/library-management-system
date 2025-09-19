package com.library.management.service;

import com.library.management.dto.MemberDTO;
import com.library.management.entity.Member;
import com.library.management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        return convertToDTO(member);
    }

    @Transactional
    public MemberDTO createMember(MemberDTO memberDTO) {
        if (memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + memberDTO.getEmail());
        }

        Member member = convertToEntity(memberDTO);
        Member savedMember = memberRepository.save(member);
        return convertToDTO(savedMember);
    }

    @Transactional
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));

        if (!existingMember.getEmail().equals(memberDTO.getEmail()) &&
                memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + memberDTO.getEmail());
        }

        existingMember.setFirstName(memberDTO.getFirstName());
        existingMember.setLastName(memberDTO.getLastName());
        existingMember.setEmail(memberDTO.getEmail());
        existingMember.setPhone(memberDTO.getPhone());
        existingMember.setAddress(memberDTO.getAddress());
        existingMember.setIsActive(memberDTO.getIsActive());

        Member updatedMember = memberRepository.save(existingMember);
        return convertToDTO(updatedMember);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        memberRepository.delete(member);
    }

    @Transactional
    public void deactivateMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        member.setIsActive(false);
        memberRepository.save(member);
    }

    public long getActiveMembersCount() {
        return memberRepository.countByIsActiveTrue();
    }

    private MemberDTO convertToDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setMemberId(member.getMemberId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setAddress(member.getAddress());
        dto.setIsActive(member.getIsActive());
        return dto;
    }

    private Member convertToEntity(MemberDTO dto) {
        Member member = new Member();
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());
        member.setIsActive(dto.getIsActive());
        return member;
    }
}
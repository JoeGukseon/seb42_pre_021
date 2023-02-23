package com.roseknife.stackoverflow.member.mapper;

import com.roseknife.stackoverflow.member.dto.MemberDto;
import com.roseknife.stackoverflow.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member memberPostToMember(MemberDto.Post requestBody);
    @Mapping(source = "location", target = "introduce.location")
    @Mapping(source = "title", target = "introduce.title")
    @Mapping(source = "content", target = "introduce.content")
    Member memberPatchToMember(MemberDto.Patch requestBody);

    @Mapping(source = "introduce.location", target = "location")
    @Mapping(source = "introduce.title", target = "title")
    @Mapping(source = "introduce.content", target = "content")
    MemberDto.Response membertoMemberResponse(Member member);

    List<MemberDto.Response> membersToMemberResponses(List<Member> members);

    Member memberLonginToMember(MemberDto.Login requestBody);
}

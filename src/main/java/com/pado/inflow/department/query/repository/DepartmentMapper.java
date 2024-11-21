package com.pado.inflow.department.query.repository;


import com.pado.inflow.department.query.dto.GetDepartmentDetailDTO;
import com.pado.inflow.department.query.dto.GetDepartmentMemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    // 1. 사원 코드, 사원명, 부서명의 검색 키워드를 통한 사원 목록 조회
    // keyword으로 검색하여 결과 조회
    List<GetDepartmentMemberDTO> findDepartmentMembersByKeyword(@Param("keyword") String keyword);

    // 2. 부서코드로 부서 해당하는 사원 목록 조회
    List<GetDepartmentMemberDTO> findDepartmentMembersByDepartmentCode(@Param("departmentCode") String departmentCode);


    // 3. 사원 코드로 사원 정보 상세 조회
    List<GetDepartmentMemberDTO> findDepartmentMemberDetailByMemberCode(@Param("employeeNumber") String employeeNumber);


    /* 관리자 권한 */
    // 1. 부서코드로 부서 상세정보 조회
    List<GetDepartmentDetailDTO> findDepartmentDetailByDepartmentCode(@Param("departmentCode") String departmentCode);








}

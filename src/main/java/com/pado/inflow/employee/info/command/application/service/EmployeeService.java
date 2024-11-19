package com.pado.inflow.employee.info.command.application.service;

import com.pado.inflow.common.exception.CommonException;
import com.pado.inflow.common.exception.ErrorCode;
import com.pado.inflow.employee.info.command.domain.aggregate.dto.request.RequestEmployeeDTO;
import com.pado.inflow.employee.info.command.domain.aggregate.dto.request.RequestUpdateEmployeeDTO;
import com.pado.inflow.employee.info.command.domain.aggregate.dto.response.ResponseEmployeeDTO;
import com.pado.inflow.employee.info.command.domain.aggregate.entity.Employee;
import com.pado.inflow.employee.info.command.domain.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("employeeCommandService")
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    // 추후 비밀번호 설정할 예정
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository
            , ModelMapper modelMapper
            , BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder =bCryptPasswordEncoder;
    }

    /**
     * 설명. 1. 사원 등록
     */
    @Transactional
    public List<ResponseEmployeeDTO> registerEmployees(List<RequestEmployeeDTO> employeeDTOs) {
        //설명. DTO -> Entity 변환 및 저장
        List<Employee> employees = employeeDTOs.stream()
                .map(dto -> modelMapper.map(dto, Employee.class))
                .collect(Collectors.toList());
        
        //설명. Jpa의 saveAll통한 리스트 순차 저장
        List<Employee> savedEmployees = employeeRepository.saveAll(employees);

        //설명. Entity -> Response DTO 변환
        return savedEmployees.stream()
                .map(employee -> modelMapper.map(employee, ResponseEmployeeDTO.class))
                .collect(Collectors.toList());
    }
    //설명. 스프링 시큐리티 활성화 전 API 개발
//    @Transactional
//    public List<ResponseEmployeeDTO> registerEmployees(List<RequestEmployeeDTO> employeeDTOs) {
//        // DTO -> Entity 변환 및 비밀번호 암호화
//        List<Employee> employees = employeeDTOs.stream()
//                .map(dto -> {
//                    Employee employee = modelMapper.map(dto, Employee.class);
//                    // 비밀번호 암호화 처리
//                    employee.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
//                    return employee;
//                })
//                .collect(Collectors.toList());
//
//        // Jpa의 saveAll을 통한 리스트 순차 저장
//        List<Employee> savedEmployees = employeeRepository.saveAll(employees);
//
//        // Entity -> Response DTO 변환
//        return savedEmployees.stream()
//                .map(employee -> modelMapper.map(employee, ResponseEmployeeDTO.class))
//                .collect(Collectors.toList());
//    }


    /**
     * 설명. 2.1 사원 정보 수정 (ID 기준)
     */
    @Transactional
    public ResponseEmployeeDTO updateEmployeeById(Long employeeId, RequestUpdateEmployeeDTO updateEmployeeDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EMPLOYEE));

        updateEmployeeFields(employee, updateEmployeeDTO);

        //설명. jpa를 통한 수정
        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, ResponseEmployeeDTO.class);
    }

    /**
     * 설명. 2.2 사원 정보 수정 (사번 기준)
     */
    @Transactional
    public ResponseEmployeeDTO updateEmployeeByEmployeeNumber(String employeeNumber, RequestUpdateEmployeeDTO updateEmployeeDTO) {
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EMPLOYEE));
        
        updateEmployeeFields(employee, updateEmployeeDTO);
    
        //설명. jpa를 통한 수정
        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, ResponseEmployeeDTO.class);
    }

    //설명. 사원 정보 업데이트시 공통 필드 업데이트 로직
    private void updateEmployeeFields(Employee employee, RequestUpdateEmployeeDTO updateEmployeeDTO) {
        // 설명. 요청 DTO 필드에 값이 있는 경우에만
        if (updateEmployeeDTO.getEmail() != null) {
            employee.setEmail(updateEmployeeDTO.getEmail());
        }
        if (updateEmployeeDTO.getPhoneNumber() != null) {
            employee.setPhoneNumber(updateEmployeeDTO.getPhoneNumber());
        }
        if (updateEmployeeDTO.getProfileImgUrl() != null) {
            employee.setProfileImgUrl(updateEmployeeDTO.getProfileImgUrl());
        }
        if (updateEmployeeDTO.getStreetAddress() != null) {
            employee.setStreetAddress(updateEmployeeDTO.getStreetAddress());
        }
        if (updateEmployeeDTO.getDetailedAddress() != null) {
            employee.setDetailedAddress(updateEmployeeDTO.getDetailedAddress());
        }
    }

    // 설명.3. 시큐리티를 위한 설정 메서드
    // 설명.3.1 사번으로 사원 조회하기
    public Employee findByEmployeeNumber(String employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }

    // 설명.3.2 시큐리티를 위한 설정
    //  로그인 시 security가 자동으로 호출하는 메소드 */
    @Override
    public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {
        // 1. employeeNumber를 기준으로 사용자 조회
        Employee loginEmployee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new CommonException(ErrorCode.UNAUTHORIZED_ACCESS));

        // 2. 비밀번호 처리 (소셜 로그인 시 비밀번호가 없을 경우 기본값 설정)
        String encryptedPwd = loginEmployee.getPassword();
        if (encryptedPwd == null) {
            encryptedPwd = "{noop}";  // 비밀번호가 없을 경우 기본값 설정
        }

        // 3. 권한 정보를 userRole 필드에서 가져와서 변환
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(
                // "ROLE_EMPLOYEE" 또는 "ROLE_HR" 또는 "ROLE_MANAGER " 또는 "ROLE_ADMIN"
                new SimpleGrantedAuthority("ROLE_" + loginEmployee.getEmployeeRole().name())
        );

        // 4. UserDetails 객체 반환
        return new User(loginEmployee.getEmployeeNumber(), encryptedPwd,
                true, true, true, true,
                grantedAuthorities);
    }

}

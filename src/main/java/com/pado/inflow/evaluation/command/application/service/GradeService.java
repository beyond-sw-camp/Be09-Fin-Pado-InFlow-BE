package com.pado.inflow.evaluation.command.application.service;

import com.pado.inflow.evaluation.command.domain.aggregate.dto.request.CreateGradeRequestDTO;
import com.pado.inflow.evaluation.command.domain.aggregate.dto.response.GradeResponseDTO;

import java.util.List;

public interface GradeService {
    List<GradeResponseDTO> createGrade(CreateGradeRequestDTO createGradeRequestDTO, Integer year, String half);

    GradeResponseDTO updateGrade(Long gradeId);

    void deleteGrade(Long gradeId, Integer year, String half);
}

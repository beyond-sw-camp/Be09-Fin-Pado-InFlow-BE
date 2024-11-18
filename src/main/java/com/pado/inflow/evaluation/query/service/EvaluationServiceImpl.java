package com.pado.inflow.evaluation.query.service;

import com.pado.inflow.common.exception.CommonException;
import com.pado.inflow.common.exception.ErrorCode;
import com.pado.inflow.evaluation.query.dto.EvaluationDTO;
import com.pado.inflow.evaluation.query.repository.EvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private final EvaluationMapper evaluationMapper;

    public EvaluationServiceImpl(EvaluationMapper evaluationMapper) {
        this.evaluationMapper = evaluationMapper;
    }


    @Override
    public EvaluationDTO findEvaluationGrade(Long empId, Integer year, String half) {
        EvaluationDTO finalGrade = evaluationMapper.getFinalEvaluationGrade(empId, year, half);
        if (finalGrade == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_GRADE);
        }
        return finalGrade;
    }
}
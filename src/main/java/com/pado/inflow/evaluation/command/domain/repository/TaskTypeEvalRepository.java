package com.pado.inflow.evaluation.command.domain.repository;

import com.pado.inflow.evaluation.command.domain.aggregate.entity.TaskTypeEvalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTypeEvalRepository extends JpaRepository<TaskTypeEvalEntity, Long> {
    List<TaskTypeEvalEntity> findByEvaluationId(Long evaluationId);
}

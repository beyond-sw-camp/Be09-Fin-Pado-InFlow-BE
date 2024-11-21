package com.pado.inflow.evaluation.command.domain.aggregate.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.pado.inflow.evaluation.command.domain.aggregate.entity.FeedbackEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeedbackRequestDTO {


    @JsonProperty("content")
    private String content;

    @JsonProperty("evaluation_id")
    private Long evaluationId;

    // Entity 변환 메서드
    public FeedbackEntity toEntity() {
        return FeedbackEntity.builder()
                .content(this.content)
                .evaluationId(this.evaluationId)
                .build();
    }
}

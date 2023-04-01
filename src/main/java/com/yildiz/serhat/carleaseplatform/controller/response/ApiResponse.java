package com.yildiz.serhat.carleaseplatform.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    @JsonProperty("meta")
    @Builder.Default
    private OperationResult operationResult = new OperationResult("0", "success");

    @JsonProperty("data")
    private T operationResultData;

    public static ApiResponse createApiResponse(int errorCode, String errorMessage) {
        return ApiResponse.builder()
                .operationResult(OperationResult.builder()
                        .returnCode(Integer.toString(errorCode))
                        .returnMessage(errorMessage)
                        .build())
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OperationResult {

        @JsonProperty("return-code")
        private String returnCode;

        @JsonProperty("return-message")
        private String returnMessage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldErrorData {

        @JsonProperty("field")
        private String field;

        @JsonProperty("message")
        private String message;
    }
}
package org.vktest.vktestapp.data.remote.api;

public class VKBaseResponse<R> {

    private R response;

    private VKErrorResponse error;

    public VKBaseResponse(R response, VKErrorResponse error) {
        this.response = response;
        this.error = error;
    }

    public boolean isSuccessful() {
        return response != null;
    }

    public R getSuccess() {
        return response;
    }

    public void setSuccess(R response) {
        this.response = response;
    }

    public VKErrorResponse getError() {
        return error;
    }

    public void setError(VKErrorResponse error) {
        this.error = error;
    }

    public static final class VKErrorResponse{
        Integer error_code;
        String error_msg;

        public VKErrorResponse(Integer error_code, String error_msg) {
            this.error_code = error_code;
            this.error_msg = error_msg;
        }

        public Integer getError_code() {
            return error_code;
        }

        public void setError_code(Integer error_code) {
            this.error_code = error_code;
        }

        public String getError_msg() {
            return error_msg;
        }

        public void setError_msg(String error_msg) {
            this.error_msg = error_msg;
        }
    }

}

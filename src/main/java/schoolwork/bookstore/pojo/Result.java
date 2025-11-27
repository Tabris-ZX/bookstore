package schoolwork.bookstore.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    public static final Integer SUCCESS_CODE = 200;
    public static final Integer ERROR_CODE = 0;

    private Integer code;
    private String message;
    private Object data;

    //成功响应
    public static Result success() {
        return new Result(SUCCESS_CODE, "success", null);
    }

    public static Result success(Object data) {
        return new Result(SUCCESS_CODE, "success", data);
    }

    //错误响应
    public static Result error() {
        return new Result(ERROR_CODE, "error", null);
    }

    public static Result error(String message) {
        return new Result(ERROR_CODE, message, null);
    }

}

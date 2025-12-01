package schoolwork.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import schoolwork.bookstore.dto.Result;
import schoolwork.bookstore.service.CartService;
import schoolwork.bookstore.util.JwtUtil;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PutMapping
    public Result updateBooksInCart(HttpServletRequest request,
                                    @RequestParam long bid,
                                    @RequestParam int number) {
        boolean result = cartService.updateBooksInCart(JwtUtil.getUid(request), bid, number);
        return result ? Result.success() : Result.error("更新图书失败");
    }

    @DeleteMapping("/{bid}")
    public Result removeBooksInCart(HttpServletRequest request,@PathVariable long bid) {
        boolean result = cartService.removeBooksInCart(JwtUtil.getUid(request),bid);
        return result ? Result.success() : Result.error("删除图书失败");
    }

    @DeleteMapping("/clear")
    public Result clearCart(HttpServletRequest request) {
        boolean result = cartService.clearCart(JwtUtil.getUid(request));
        return result ? Result.success() : Result.error("清空购物车失败");
    }

    @PostMapping("/checkout")
    public Result checkout(HttpServletRequest request) {
        try{
            cartService.cartCheckout(JwtUtil.getUid(request));
        }catch (Exception e){
            return Result.error("结算失败: " + e.getMessage());
        }
        return Result.success("结算成功");
    }


}

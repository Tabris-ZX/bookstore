package schoolwork.bookstore.controller;

import org.springframework.web.bind.annotation.*;
import schoolwork.bookstore.pojo.Result;
import schoolwork.bookstore.service.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/add")
    public Result addBooksToCart(
                                 @RequestParam long bid,
                                 @RequestParam int number) {
        boolean result = cartService.addBooksToCart(bid, number);
        return result? Result.success(): Result.error("添加图书失败");
    }


}

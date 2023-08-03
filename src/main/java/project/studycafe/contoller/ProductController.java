package project.studycafe.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.domain.Member;
import project.studycafe.domain.PageMaker;
import project.studycafe.domain.Product;
import project.studycafe.repository.product.dto.ProductSearchCond;
import project.studycafe.service.product.ProductService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private static final int BASIC_PER_PAGE_NUM = 10;    // 페이지당 보여줄 게시판 개수

    @GetMapping()
    public String products(@Validated @ModelAttribute("productSearch") ProductSearchCond searchCond, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        List<Product> products = productService.findProducts();
        searchCond.setPerPageNum(BASIC_PER_PAGE_NUM);

        List<Product> productList = productService.getProductList(page, BASIC_PER_PAGE_NUM, products);

        PageMaker pageMaker = new PageMaker(products.size(), page, BASIC_PER_PAGE_NUM);

        model.addAttribute("products", productList);
        model.addAttribute("pageMaker", pageMaker);
        return "product/products";

    }
    @GetMapping("/search")
    public String searchProducts(@Validated @ModelAttribute("productSearch") ProductSearchCond productSearch, @RequestParam(required = false, defaultValue = "1") int page, BindingResult bindingResult, Model model) {

        log.info("productSearch = {}", productSearch);
        if (productSearch.getMaxPrice() != null && productSearch.getMinPrice() != null && productSearch.getMaxPrice() < productSearch.getMinPrice() ) {
            bindingResult.reject("maxPrice is not higher minPrice", "최대 가격은 최소 가격보다 작을 수 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            List<Product> products = productService.findProducts();
            productSearch.setPerPageNum(BASIC_PER_PAGE_NUM);

            List<Product> productList = productService.getProductList(page, BASIC_PER_PAGE_NUM, products);

            PageMaker pageMaker = new PageMaker(products.size(), page, BASIC_PER_PAGE_NUM);

            model.addAttribute("products", productList);
            model.addAttribute("pageMaker", pageMaker);

            log.info("errors = {}", bindingResult);
            return "product/products";
        }

        List<Product> findProducts = productService.findSearchedAndSortedProducts(productSearch);

        List<Product> productList = productService.getProductList(page, productSearch.getPerPageNum(), findProducts);
        PageMaker pageMaker = new PageMaker(findProducts.size(), page, productSearch.getPerPageNum());
        if (productSearch.getSort() != null) {
            if (productSearch.getSort().equals("productReadCountUp")) {
                productSearch.setSort("productReadCountDown");
            } else if (productSearch.getSort().equals("productReadCountDown")) {
                productSearch.setSort("productReadCountUp");
            } else if (productSearch.getSort().equals("productLikeCountUp")) {
                productSearch.setSort("productLikeCountDown");
            } else if (productSearch.getSort().equals("productLikeCountDown")) {
                productSearch.setSort("productLikeCountUp");
            }
        }
        log.info("productSearch = {}", productSearch);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("products", productList);
        model.addAttribute("productSearch", productSearch);

        return "product/products";
    }

    @GetMapping("/{productId}")
    public String product(@PathVariable long productId, Model model) {
        Product product = productService.findById(productId).orElseThrow();
        productService.increaseReadCount(product);
        model.addAttribute("product", product);

        return "product/product";
    }

    @GetMapping("/add")
    public String addForm(@Login Member member, Model model) {
        if (member == null) {
            return "redirect:/login?redirectURL=/product/add";
        }

        model.addAttribute("product", new Product());
        model.addAttribute("loginMember", member);
        log.info("loginMember={}", member);
        return "product/addProductForm";
    }

    @PostMapping("/add")
    public String add(Product product) {
        log.info("product={}", product);
        productService.addProduct(product);
        return "redirect:/product"; // 일단 home으로 보내주자 나중에 product목록으로 보내주고
    }

    @GetMapping("/{productId}/edit")
    public String editForm(@PathVariable Long productId, Model model) {
        Product product = productService.findById(productId).orElseThrow();
        model.addAttribute("product", product);
        return "product/editProductForm";
    }

    @PostMapping("/{productId}/edit")
    public String edit(Product product, @PathVariable Long productId) {
        productService.updateProduct(productId, product);

        return "redirect:/product";
    }


    @GetMapping("/{productId}/likeCountUp")
    public String upLikeCountEdit(@PathVariable Long productId) {
        productService.upLikeCountProduct(productId);
        return "redirect:/product/"+ productId;
    }

    @GetMapping("/{productId}/likeCountDown")
    public String downLikeCountEdit(@PathVariable Long productId) {
        productService.downLikeCountProduct(productId);
        return "redirect:/product/"+ productId;
    }

    @GetMapping("/{productId}/delete")
    public String delete(@PathVariable long productId) {
        productService.deleteProduct(productId);
        return "redirect:/product"; // 삭제 후 목록 페이지로 리다이렉트
    }
}
package project.studycafe.app.controller.home;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.app.controller.form.board.BoardForm;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.product.Product;
import project.studycafe.helper.resolver.argumentresolver.Login;
import project.studycafe.app.service.dto.searchDto.ProductSearchCond;
import project.studycafe.app.service.board.BoardService;
import project.studycafe.app.service.product.ProductService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static project.studycafe.app.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;
    private final ProductService productService;
    private final ApplicationContext applicationContext;


    public void printAllBeans() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }


    @GetMapping("/")
    public String home(HttpServletRequest request, @Login Member loginMember, @ModelAttribute("productSearch") ProductSearchCond productSearch, Model model) {
//        printAllBeans();

        setupPopup(request);

        if (loginMember != null) {
            model.addAttribute(LOGIN_MEMBER, loginMember);
        }

        List<Board> boards = boardService.getHomeBoards();
        List<BoardForm> boardForms = boardService.boardsToBoardForms(boards);
        model.addAttribute("boards", boardForms);

        List<Product> products = productService.findProductsTop5LikeCount(productSearch);
        model.addAttribute("products", products);

        return "home";
    }

    private static void setupPopup(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("popupShown") && cookie.getValue().equals("true")) {
                    // 이미 팝업을 보았으면 홈 화면으로 이동
                    log.info("cookie={},{}", cookie.getValue(), cookie.getName());
                }
            }
        }
    }

}
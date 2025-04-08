package view;

import config.AppConfig;
import controller.ProductController;
import controller.UserController;
import domain.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * 애플리케이션의 진입점 및 메인 메뉴를 관리하는 클래스
 */
public class Main {
    public static void main(String[] args) {
        try {
            // AppConfig에서 필요한 컴포넌트 가져오기
            AppConfig appConfig = AppConfig.getInstance();
            ConsoleService consoleService = appConfig.getConsoleService();
            UserController userController = appConfig.getUserController();
            ProductController productController = appConfig.getProductController();

            // 시작 메시지
            consoleService.printMessage("===== Java 콘솔 기반 e-Commerce 애플리케이션 =====");

            boolean exit = false;
            while (!exit) {
                try {
                    // 로그인 상태에 따라 메뉴 분기
                    if (!userController.isLoggedIn()) {
                        // 로그인되지 않은 상태 - 메인 메뉴 표시
                        exit = showMainMenu(consoleService, userController);
                    } else {
                        // 로그인된 상태 - 권한별 메뉴 표시
                        if (userController.isAdmin()) {
                            exit = showAdminMenu(consoleService, userController, productController);
                        } else {
                            exit = showUserMenu(consoleService, userController, productController);
                        }
                    }
                } catch (Exception e) {
                    consoleService.printError("오류가 발생했습니다: " + e.getMessage());
                    consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                }
            }

            // 종료 메시지
            consoleService.printMessage("애플리케이션을 종료합니다. 이용해 주셔서 감사합니다.");

        } catch (Exception e) {
            System.err.println("애플리케이션 실행 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 비로그인 상태 메인 메뉴 표시
     */
    private static boolean showMainMenu(ConsoleService consoleService, UserController userController) {
        List<String> options = Arrays.asList(
                "로그인",
                "회원가입",
                "종료"
        );

        int choice = consoleService.showMenu("메인 메뉴", options);

        switch (choice) {
            case 1: // 로그인
                User loggedInUser = userController.login();
                if (loggedInUser == null) {
                    consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                }
                return false;

            case 2: // 회원가입
                userController.register();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 3: // 종료
                return true;

            default:
                return false;
        }
    }

    /**
     * 일반 사용자 메뉴 표시
     */
    private static boolean showUserMenu(ConsoleService consoleService, UserController userController, ProductController productController) {
        List<String> options = Arrays.asList(
                "상품 목록 보기",
                "상품 상세 보기",
                "내 정보 수정",
                "비밀번호 변경",
                "회원 탈퇴 요청",
                "로그아웃"
        );

        int choice = consoleService.showMenu("사용자 메뉴", options);

        switch (choice) {
            case 1: // 상품 목록 보기
                productController.listProducts();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 2: // 상품 상세 보기
                productController.viewProductDetail();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 3: // 내 정보 수정
                userController.updateUserInfo();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 4: // 비밀번호 변경
                userController.changePassword();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 5: // 회원 탈퇴 요청
                userController.requestWithdrawal();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 6: // 로그아웃
                userController.logout();
                return false;

            default:
                return false;
        }
    }

    /**
     * 관리자 메뉴 표시
     */
    private static boolean showAdminMenu(ConsoleService consoleService, UserController userController, ProductController productController) {
        List<String> options = Arrays.asList(
                "상품 등록",
                "상품 수정",
                "상품 삭제",
                "상품 목록 보기",
                "상품 상세 보기",
                "상품 판매 상태 관리",
                "재고 관리",
                "품절 처리",
                "내 정보 수정",
                "비밀번호 변경",
                "회원 탈퇴 요청",
                "로그아웃"
        );

        int choice = consoleService.showMenu("관리자 메뉴", options);

        switch (choice) {
            case 1: // 상품 등록
                productController.registerProduct();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 2: // 상품 수정
                productController.updateProduct();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 3: // 상품 삭제
                productController.deleteProduct();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 4: // 상품 목록 보기
                productController.listProducts();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 5: // 상품 상세 보기
                productController.viewProductDetail();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 6: // 상품 판매 상태 관리
                productController.manageSaleStatus();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 7: // 재고 관리
                productController.manageStock();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 8: // 품절 처리
                productController.markProductOutOfStock();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 9: // 내 정보 수정
                userController.updateUserInfo();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 10: // 비밀번호 변경
                userController.changePassword();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 11: // 회원 탈퇴 요청
                userController.requestWithdrawal();
                consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                return false;

            case 12: // 로그아웃
                userController.logout();
                return false;

            default:
                return false;
        }
    }
}
package config;

import controller.ProductController;
import controller.UserController;
import domain.repository.ProductRepository;
import domain.repository.ProductRepositoryImpl;
import domain.repository.UserRepository;
import domain.repository.UserRepositoryImpl;
import service.AuthService;
import service.ProductService;
import service.UserService;
import util.DatabaseConnection;
import view.ConsoleService;

import java.sql.Connection;

public class AppConfig {
    private static AppConfig instance;

    private Connection conn;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private UserService userService;
    private ProductService productService;
    private AuthService authService;
    private ConsoleService consoleService;
    private UserController userController;
    private ProductController productController;

    private AppConfig() {
        initComponents();
    }

    /**
     * 싱글톤 인스턴스 반환
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    /**
     * 모든 컴포넌트 초기화 및 의존성 주입
     */
    private void initComponents() {
        // 데이터베이스 연결
        this.conn = DatabaseConnection.getConnection();

        // 리포지토리 계층
        this.userRepository = new UserRepositoryImpl(conn);
        this.productRepository = new ProductRepositoryImpl(conn);

        // 서비스 계층
        this.userService = new UserService(userRepository);
        this.authService = new AuthService(userRepository);
        this.productService = new ProductService(productRepository);

        // 뷰 계층
        this.consoleService = new ConsoleService(System.in, System.out);

        // 컨트롤러 계층
        this.userController = new UserController(userService, authService, consoleService);
        this.productController = new ProductController(productService, consoleService, authService);
    }

    // Getter 메서드 - main에서 사용하기 위함

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public UserService getUserService() {
        return userService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ConsoleService getConsoleService() {
        return consoleService;
    }

    public UserController getUserController() {
        return userController;
    }

    public ProductController getProductController() {
        return productController;
    }

    public void closeResources() {
        DatabaseConnection.closeConnection();
    }
}

package controller;

import domain.model.User;
import exception.*;
import service.AuthService;
import view.ConsoleService;
import service.UserService;

public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final ConsoleService consoleService;

    public UserController(UserService userService, AuthService authService, ConsoleService consoleService) {
        this.userService = userService;
        this.authService = authService;
        this.consoleService = consoleService;
    }

    public void register() {
        consoleService.printMessage("===== 회원가입 =====");

        try {
            String email = consoleService.readLine("이메일을 입력하세요: ");
            String password = consoleService.readLine("비밀번호를 입력하세요: ");
            String name = consoleService.readLine("이름을 입력하세요: ");
            String mobile = consoleService.readLine("휴대전화 번호를 입력하세요: ");
            String userType = consoleService.readLine("일반 사용자로 가입은 10, 관리자로 가입은 20을 입력해주세요: ");

            User user = new User(email,email,password,name,mobile,userType);
            userService.register(user);
        } catch (DuplicateEmailException | InvalidEmailException | InvalidPasswordException e) {
            consoleService.printMessage(e.getMessage());
        }catch (Exception e) {
            // 예상치 못한 예외
            consoleService.printMessage(e.getMessage());
        }
    }


    public User login() {
        consoleService.printMessage("===== 로그인 =====");
        try {
            String email = consoleService.readLine("이메일을 입력하세요: ");
            String password = consoleService.readLine("비밀번호를 입력하세요: ");

            return authService.login(email, password);


        } catch (UserNotFoundException | UserWithdrawnException | AuthenticationException e) {
            consoleService.printMessage(e.getMessage());
        }catch (Exception e) {
            // 예상치 못한 예외
            consoleService.printMessage(e.getMessage());
        }
        return null;
    }

    public void logout() {
        try {
            User user = authService.getCurrentUser();
            authService.logout();
        }catch (NotLoggedInException e) {
            consoleService.printMessage(e.getMessage());
        }
    }

    public void changePassword() {
        try {
            consoleService.printMessage("===== 비밀번호 변경 =====");
            User user = authService.getCurrentUser();
            String oldPassword = consoleService.readLine("현재 비밀번호를 입력하세요: ");
            String newPassword = consoleService.readLine("새로운 비밀번호를 입력하세요: ");
            userService.changePassword(user.getEmail(), oldPassword, newPassword);

        }catch (NotLoggedInException | UserNotFoundException | InvalidPasswordException | AuthenticationException e) {
            consoleService.printMessage(e.getMessage());
        }

    }

    public void updateUserInfo() {
        try {
            consoleService.printMessage("===== 회원 정보 변경 =====");
            User user = authService.getCurrentUser();
            consoleService.printMessage("휴대전화, 이름, 사용자 구분 코드만 변경 가능합니다.");
            String name = consoleService.readLine("이름을 입력하세요: ");
            String mobile = consoleService.readLine("휴대전화 번호를 입력하세요: ");
            String userType = consoleService.readLine("일반 사용자로 가입은 10, 관리자로 가입은 20을 입력해주세요: ");

            user.setStatus(name);
            user.setMobileNumber(mobile);
            user.setUserType(userType);

            userService.updateUser(user);

        }catch (NotLoggedInException e) {
            consoleService.printMessage(e.getMessage());
        }
    }

    public void requestWithdrawal() {
        try {
            consoleService.printMessage("===== 회원 탈퇴 =====");
            User user = authService.getCurrentUser();
            userService.requestWithdrawal(user.getEmail());
            consoleService.printMessage("===== 회원 탈퇴가 완료 되었습니다. =====");
        } catch (NotLoggedInException e) {
            consoleService.printMessage(e.getMessage());
        }
    }

    public boolean isLoggedIn() {
        return authService.isLoggedIn();
    }

    public boolean isAdmin() {
        try {
            User currentUser = authService.getCurrentUser();
            return currentUser != null && "20".equals(currentUser.getUserType());
        } catch (NotLoggedInException e) {
            return false;
        }
    }

}

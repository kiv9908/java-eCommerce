package controller;

import domain.model.User;
import exception.*;
import service.AuthService;
import view.ConsoleService;
import service.UserService;

import java.util.Arrays;
import java.util.List;

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
    
    // 관리자: 모든 사용자 조회
    public void listAllUsers() {
        try {
            consoleService.printMessage("\n===== 회원 목록 =====");
            
            // 관리자 권한 체크
            if (!isAdmin()) {
                consoleService.printError("관리자만 접근할 수 있는 기능입니다.");
                return;
            }
            
            // 모든 사용자 조회
            List<User> users = userService.getAllUsers();
            
            if (users.isEmpty()) {
                consoleService.printMessage("등록된 회원이 없습니다.");
                return;
            }
            
            // 헤더 출력
            String headerFormat = "%-25s %-15s %-15s %-10s %-10s";
            String dataFormat = "%-25s %-15s %-15s %-10s %-10s";
            
            consoleService.printMessage(String.format(headerFormat, 
                "이메일", "이름", "연락처", "상태", "사용자 유형"));
            
            // 구분선
            consoleService.printMessage("--------------------------------------------------------------------------------");
            
            // 사용자 목록 출력
            for (User user : users) {
                String userType = "10".equals(user.getUserType()) ? "일반 사용자" : "관리자";
                String status = "ST01".equals(user.getStatus()) ? "활성" : ("ST02".equals(user.getStatus()) ? "탈퇴" : user.getStatus());
                
                consoleService.printMessage(String.format(dataFormat,
                    user.getEmail(),
                    user.getUserName(),
                    user.getMobileNumber(),
                    status,
                    userType));
            }
            
            consoleService.printMessage("\n총 " + users.size() + "명의 회원이 조회되었습니다.");
            
        } catch (Exception e) {
            consoleService.printError("회원 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 관리자: 일반 사용자 권한 변경
    public void changeUserRole() {
        try {
            consoleService.printMessage("\n===== 사용자 권한 변경 =====");
            
            // 관리자 권한 체크
            if (!isAdmin()) {
                consoleService.printError("관리자만 접근할 수 있는 기능입니다.");
                return;
            }
            
            // 변경할 사용자 이메일 입력
            String email = consoleService.readLine("권한을 변경할 사용자의 이메일을 입력하세요: ");
            
            // 사용자 존재 여부 확인
            User user = userService.getUserByEmail(email);
            if (user == null) {
                consoleService.printError("해당 이메일의 사용자가 존재하지 않습니다.");
                return;
            }
            
            // 현재 로그인한 사용자 확인
            User currentUser = authService.getCurrentUser();
            boolean isSelfModification = currentUser.getEmail().equals(email);
            
            if (isSelfModification) {
                boolean confirmed = consoleService.confirm("경고: 자신의 권한을 변경하는 경우 즉시 로그아웃됩니다. 계속하시겠습니까?");
                if (!confirmed) {
                    consoleService.printMessage("권한 변경이 취소되었습니다.");
                    return;
                }
            }
            
            // 현재 권한 표시
            String currentRole = "10".equals(user.getUserType()) ? "일반 사용자" : "관리자";
            consoleService.printMessage("현재 권한: " + currentRole);
            
            // 변경할 권한 선택
            List<String> options = Arrays.asList(
                "일반 사용자로 변경",
                "관리자로 변경"
            );
            
            int choice = consoleService.showMenu("변경할 권한 선택", options);
            
            String newUserType = (choice == 1) ? "10" : "20";
            
            // 현재 권한과 같은 경우 처리
            if (newUserType.equals(user.getUserType())) {
                consoleService.printMessage("이미 동일한 권한을 가지고 있습니다.");
                return;
            }
            
            // 권한 변경 확인
            boolean confirmed = consoleService.confirm("정말로 이 사용자의 권한을 변경하시겠습니까?");
            if (!confirmed) {
                consoleService.printMessage("권한 변경이 취소되었습니다.");
                return;
            }
            
            // 권한 변경 실행
            boolean success = userService.changeUserRole(email, newUserType);
            if (success) {
                String newRole = "10".equals(newUserType) ? "일반 사용자" : "관리자";
                consoleService.printMessage("사용자 권한이 " + newRole + "로 변경되었습니다.");
                
                // 자신의 권한을 변경한 경우 자동 로그아웃
                if (isSelfModification) {
                    consoleService.printMessage("본인의 권한이 변경되어 자동으로 로그아웃됩니다.");
                    consoleService.waitForEnter("계속하려면 Enter 키를 누르세요");
                    authService.logout();
                }
            } else {
                consoleService.printError("권한 변경에 실패했습니다.");
            }
            
        } catch (Exception e) {
            consoleService.printError("사용자 권한 변경 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

package service;

import domain.model.User;
import domain.repository.UserRepository;
import exception.AuthenticationException;
import exception.NotLoggedInException;
import exception.UserNotFoundException;
import exception.UserWithdrawnException;

public class AuthService {
    private final UserRepository userRepository;

    private User currentUser = null;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String userId, String password)
            throws UserNotFoundException, AuthenticationException, UserWithdrawnException {
        // 이메일로 사용자 검색
        User user = userRepository.findByUserId(userId);

        // 사용자가 존재하지 않는 경우
        if (user == null) {
            throw new UserNotFoundException("이메일에 해당하는 사용자를 찾을 수 없습니다: " + userId);
        }

        // 탈퇴한 회원인 경우
        if ("ST02".equals(user.getStatus())) {
            throw new UserWithdrawnException("탈퇴한 회원입니다: " + userId);
        }

        // 비밀번호 검증
        if (!password.equals(user.getPassword())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
        this.currentUser = user;
        return user;
    }


    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() throws NotLoggedInException {
        if (this.currentUser == null) {
            throw new NotLoggedInException("로그인되지 않은 상태입니다.");
        }
        return this.currentUser;
    }

    public boolean isLoggedIn() {
        return this.currentUser != null;
    }

    public boolean isAdmin() throws NotLoggedInException {
        if (!isLoggedIn()) {
            throw new NotLoggedInException("로그인되지 않은 상태입니다.");
        }
        return "20".equals(this.currentUser.getUserType());
    }

}

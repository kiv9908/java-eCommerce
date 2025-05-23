package domain.repository;

import domain.model.User;

import java.util.List;

public interface UserRepository {
    User findByUserId(String email);

    void save(User user);

    void update(User user);

    List<User> findAll();

    boolean deleteUser(String userId);
    
    // 사용자 권한 변경
    void updateUserRole(User user);
}

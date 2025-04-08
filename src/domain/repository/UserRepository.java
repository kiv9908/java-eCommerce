package domain.repository;

import domain.model.User;

import java.util.List;

public interface UserRepository {
    User findByUserId(String email);

    void save(User user);

    void update(User user);

    List<User> findAll();

    boolean deleteUser(String userId);
    // 추가 메서드가 필요하다면 여기에 선언

}

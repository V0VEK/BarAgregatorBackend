package app;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserInfoDBRepository extends CrudRepository<UserInfoDB, Integer> {

    public List<UserInfoDB> findByEmail(String email);

    public List<UserInfoDB> findByUserTokenAndPasswordHash(String userToken, String passwordHash);

    public List<UserInfoDB> findByUserToken(String userToken);

    public List<UserInfoDB> findByPasswordHash(String passwordHash);

    public List<UserInfoDB> findByOtpAndEmail(String otp, String email);

    public List<UserInfoDB> findBySessionTokenAndRoleID(String sessionToken, String roleID);

    public List<UserInfoDB> findBySessionToken(String sessionToken);

    public List<UserInfoDB> findByRoleID(String roleID);

    public List<UserInfoDB> findByUserID(Integer userID);
}

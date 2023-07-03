package com.homework.springboot.featureNote.user;

import com.homework.springboot.featureNote.user.dto.UserDto;
import com.homework.springboot.featureNote.user.dto.UserInfo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;
//    private final SessionFactory sessionFactory;

    private final NamedParameterJdbcTemplate jdbcTemplate;



    @PostConstruct
    public void init() {
//        //CREATE
//        userRepository.save(User user);
//        //READ
//        userRepository.findAll();
//        userRepository.findById(" gf");
//        //UPDATE
//        userRepository.save(User user);
//        //DELETE
//        userRepository.delete(user);
//        userRepository.deleteById("user");
        System.out.println("userRepository.getClass() = " + userRepository.getClass());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean exist(String email) {
        if (email == null) return false;

        // jdbcTemplate
        Integer userCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM \"user\" WHERE email  = :email",
                Map.of("email", email),
                Integer.class
        );

        return userCount == 1;
        // entityManager
//        User user = entityManager.find(User.class, email);
//        return user != null;

        // userRepository
//        return userRepository.existsById(email);
    }

    public void deleteByEmail(String email) {
        userRepository.deleteById(email);
    }


    public void deleteByIds(List<String> emails) {
        jdbcTemplate.update("DELETE FROM \"user\" WHERE email IN (:emails)",
                Map.of("emails", emails));
    }

    @Transactional
    public void deleteAll(List<String> emails) {
        userRepository.deleteAllByEmails(emails);
    }

    public List<String> searchQueryEmails(String query) {
        return userRepository.searchEmails("%" + query + "%");
    }

//    public List<User> searchQuery(String query) {
//        return userRepository.findAllById(userRepository.searchEmails("%" + query + "%"));
//    }

    public List<User> searchQuery(String query) {
        return userRepository.searchByNativeSqlQuery("%" + query + "%");
    }
    public List<User> searchJdbc(String query) {
        String sql = "SELECT email, full_name, birthday, gender\n" +
                "FROM \"user\"\n" +
                "WHERE lower(email) LIKE lower(:query)\n" +
                "OR lower(full_name) LIKE lower(:query)";
        return jdbcTemplate.query(
                sql,
                Map.of("query", "%" + query + "%"),
                new UserRowMapper()
        );
    }

    private static class UserRowMapper implements  RowMapper<User>{

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User build = User.builder()
                    .email(rs.getString("email"))
                    .fullName(rs.getString("full_name"))
                    .birthday((LocalDate.parse(rs.getString("birthday"))))
                    .gender(Gender.valueOf(rs.getString("gender")))
                    .build();
            return build;
        }
    }
    public int countPeopleOlderThan(int age) {
        LocalDate maxBirthday = LocalDate.now().minusYears(age);
        return userRepository.countOlderThan(maxBirthday);
//        return (int) findAll()
//                .stream()
//                .filter(u -> {
//                   int userAge =(int) ChronoUnit.YEARS.between(
//                           u.getBirthday(), LocalDate.now()
//                   );
//                   return userAge > age;
//                })
//                .count();
    }

    public UserInfo getUserInfo(String email) {
        String sql = "SELECT u.email as email, full_name, birthday, gender, address\n"  +
                     "FROM \"user\" u\n"  +
                     "JOIN user_address ua ON u.email = ua.email\n" +
                     "WHERE u.email = :email";
        List<UserInfoItem> items = jdbcTemplate.query(sql,
                Map.of("email", email),
                new UserInfoItemMapper());

        UserInfoItem userInfoItem = items.get(0);
        int age =(int) ChronoUnit.YEARS.between(userInfoItem.getBirthday(), LocalDate.now());

        UserDto userDto = UserDto.builder()
                .email(userInfoItem.getEmail())
                .fullName(userInfoItem.getFullName())
                .birthday(userInfoItem.getBirthday())
                .gender(userInfoItem.getGender())
                .age(age)
                .build();

        List<String> addressList = items.stream()
                .map(it -> it.getAddress())
                .filter(it -> it != null)
                .toList();

        return UserInfo.builder()
                .userDto(userDto)
                .addresses(addressList)
                .build();
    }

    public UserInfo getUserInfoV2(String email) {
        Optional<User> user = userRepository.findById(email);

        String sql = "SELECT address FROM user_address WHERE email = :email";
        List<String> addressList = jdbcTemplate.queryForList(sql,
                Map.of("email", email),
                String.class);


        return UserInfo.builder()
                .userDto(UserDto.fromUser(user.get() ))
                .addresses(addressList)
                .build();
    }

    public List<User> getUsersBetween(LocalDate start, LocalDate end) {
        Specification<User> betweenSpec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"),start),
                        criteriaBuilder.lessThanOrEqualTo(root.get("birthday"),end)
                );

            }
        };
//        return userRepository.findAll(betweenSpec);

//        return userRepository.findAll(
//                (root, query, criteriaBuilder) -> criteriaBuilder.and(
//                        criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), start),
//                        criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), end)
//                ));

        return userRepository.findAll(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.between(root.get("birthday"), start, end)
                );
    }

    private static class UserInfoItemMapper implements RowMapper<UserInfoItem> {
        @Override
        public UserInfoItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return UserInfoItem.builder()
                    .email(rs.getString("email"))
                    .fullName(rs.getString("full_name"))
                    .birthday((LocalDate.parse(rs.getString("birthday"))))
                    .gender(Gender.valueOf(rs.getString("gender")))
                    .address(rs.getString("address"))
                    .build();
        }
    }

    @Data
    @Builder
    private static class UserInfoItem {
       private String email;
       private String fullName;
       private LocalDate birthday;
       private Gender gender;
       private String address;
    }
}

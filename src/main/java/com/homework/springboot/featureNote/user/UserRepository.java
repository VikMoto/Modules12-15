package com.homework.springboot.featureNote.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>,
        JpaSpecificationExecutor<User> {
    @Query("from User u WHERE lower(u.email) LIKE lower(:query) OR lower(u.fullName) LIKE lower(:query) ")
    List<User> search(@Param("query") String query);

    @Query(nativeQuery = true, value =
                    "SELECT email, full_name, birthday, gender\n" +
                            "FROM \"user\"\n" +
                            "WHERE lower(email) LIKE lower(:query)\n" +
                            "OR lower(full_name) LIKE lower(:query)")
    List<User> searchByNativeSqlQuery(@Param("query") String query);

    @Query(nativeQuery = true, value =
            "SELECT email\n" +
                    "FROM \"user\"\n" +
                    "WHERE lower(email) LIKE lower(:query)\n")
    List<String> searchEmails(String query);

    @Query(nativeQuery = true, value =
            "SELECT count(*)\n" +
                    "FROM \"user\"\n" +
                    "WHERE birthday < :maxBirthday\n")
    int countOlderThan(LocalDate maxBirthday);


    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM \"user\" WHERE email IN (:emails)")
    void deleteAllByEmails(@Param("emails") List<String> emails);
}

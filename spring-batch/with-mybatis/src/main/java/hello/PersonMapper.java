package hello;

import org.apache.ibatis.annotations.*;

@Mapper
public interface PersonMapper {
    @Insert("INSERT INTO people_upper_case (first_name, last_name) VALUES (#{firstName}, #{lastName})")
    void insert(@Param("lastName") String lastName, @Param("firstName") String firstName);

    @Results(id = "person", value = {
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name")
    })
    @Select("SELECT first_name, last_name FROM people ORDER BY person_id ASC LIMIT #{_skiprows}, #{_pagesize}")
    Person select();
}

package hello;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PersonMapper {
    @Insert("INSERT INTO people (first_name, last_name) VALUES (#{firstName}, #{lastName})")
    void insert(@Param("lastName") String lastName, @Param("firstName") String firstName);
}

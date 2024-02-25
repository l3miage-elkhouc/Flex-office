package com.soprasteria.flexOfficebackend.repository;
import com.soprasteria.flexOfficebackend.model.User;
import org.springframework.data.repository.CrudRepository;;;

public interface UserRepository extends CrudRepository<User,Integer>  {
    
}

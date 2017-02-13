package me.pixka.update.r;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.pixka.update.d.Updatevalue;

@Repository
public interface UpdatevalueRepo extends CrudRepository<Updatevalue, Long> {

}
